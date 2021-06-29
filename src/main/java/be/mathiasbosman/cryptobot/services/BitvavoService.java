package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.configuration.BitvavoConfig.CryptoDetail;
import be.mathiasbosman.cryptobot.api.consumers.BitvavoConsumer;
import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoAccount.Fees;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoOrderResponse;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoSymbol;
import be.mathiasbosman.cryptobot.persistency.entities.TradeEntity;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class BitvavoService implements CryptoService {

  private final BitvavoConsumer apiConsumer;
  private final TradeService tradeService;

  /**
   * Returns the crypto Symbol based on its code
   *
   * @param symbolCode The symbolcode
   * @return Symbol
   */
  @Override
  public Symbol getSymbol(String symbolCode) {
    return apiConsumer.getSymbol(symbolCode);
  }

  /**
   * Get all symbols that currently have an available amount except the currency symbol configured
   *
   * @param exclusions List of symbol codes to exclude
   * @return List of symbols
   */
  @Override
  public List<Symbol> getCurrentCrypto(List<String> exclusions) {
    return apiConsumer.getSymbols().stream()
        .filter(s -> s.getAvailable() > 0 && !exclusions.contains(s.getCode()))
        .collect(Collectors.toList());
  }

  /**
   * Selss a certain amount in a certain market
   *
   * @param marketCode Market to sell in
   * @param amount     Amount to sell
   * @return BitvavoOrderResponse
   */
  @Override
  public BitvavoOrderResponse sell(String marketCode, double amount) {
    log.info("Selling {} in {}", amount, marketCode);
    return apiConsumer
        .newOrder(marketCode, OrderSide.SELL, OrderType.MARKET, amount);
  }

  /**
   * Buy a certain amount in a certain market
   *
   * @param marketCode Market to buy in
   * @param amount     Amount to buy
   * @return BitvavoOrderResponse
   */
  @Override
  public BitvavoOrderResponse buy(String marketCode, double amount) {
    log.info("Buying {} in {}", amount, marketCode);
    return apiConsumer.newOrder(marketCode, OrderSide.BUY, OrderType.MARKET, amount);
  }

  /**
   * Returns the market name used in Bitvavo
   *
   * @param sourceCode       The first symbol's code
   * @param targetMarketCode The target's symbol code
   * @return Marketname (split with -)
   */
  @Override
  public String getMarketName(String sourceCode, String targetMarketCode) {
    return sourceCode + "-" + targetMarketCode;
  }

  /**
   * Reteruns the market price (ticker price) of a market
   *
   * @param marketCode The market's code
   * @return The market price
   */
  @Override
  public double getMarketPrice(String marketCode) {
    return apiConsumer.getTickerPrice(marketCode).getPrice();
  }

  @Override
  public void sellOnProfit(String currencySymbolCode,
      Map<String, CryptoDetail> cryptoDetails, double defaultSellTreshold,
      Instant defaultStartTime, boolean autoRebuy, boolean autoBuyCheapestStaking) {
    List<Symbol> currentCrypto = getCurrentCrypto(Collections.singletonList(currencySymbolCode));
    if (currentCrypto.isEmpty()) {
      log.warn("Not holding any crypto.");
      return;
    }
    Fees fees = apiConsumer.getAccountInfo().getFees();
    for (Symbol symbol : currentCrypto) {
      String symbolCode = symbol.getCode();
      CryptoDetail cryptoDetail = cryptoDetails.get(symbol.getCode());
      double sellTreshold =
          cryptoDetail != null ? cryptoDetail.getSellTreshold() : defaultSellTreshold;
      if (sellTreshold <= 0) {
        log.warn("No sell treshold could be determined for {}.", symbolCode);
        continue;
      }
      log.debug("Checking profit for {}. Sell treshold: {}", symbolCode, sellTreshold);
      String marketCode = getMarketName(symbol.getCode(), currencySymbolCode);
      TradeEntity latestTrade = tradeService.getLatestTrade(marketCode);
      updateTrades(marketCode,
          latestTrade.getTimestamp() != null ? latestTrade.getTimestamp() : defaultStartTime);
      double available = symbol.getAvailable();
      double currentValue = getCurrentValue(tradeService.getAllTrades(marketCode));
      double marketPrice = getMarketPrice(marketCode);
      double fee = getFee(available, marketPrice, fees.getMaker());
      if (!hasProfit(marketPrice, available, currentValue, fee, sellTreshold)) {
        continue;
      }
      BitvavoOrderResponse sellOrder = sell(marketCode, available);
      log.info("{}: sold {} at {} (fee: {})",
          sellOrder.getMarketCode(), sellOrder.getFilledAmount(), sellOrder.getPrice(),
          sellOrder.getFeePaid());
      if (!autoRebuy || (cryptoDetail == null && !autoBuyCheapestStaking)) {
        continue;
      }
      BitvavoSymbol currencySymbol = apiConsumer.getSymbol(currencySymbolCode);
      double availableCurrency = currencySymbol.getAvailable();

      if (cryptoDetail != null && cryptoDetail.getRebuyAt() > 0) {
        double amount = Math.min(availableCurrency, cryptoDetail.getRebuyAt());
        buy(marketCode, amount);
        continue;
      }

      if (autoBuyCheapestStaking) {
        String cheapestStaking = getCheapestStaking(cryptoDetails, currencySymbolCode);
        buy(getMarketName(cheapestStaking, currencySymbolCode), availableCurrency);
      }
    }
  }

  List<String> getStakingSymbols(Map<String, CryptoDetail> cryptos) {
    return cryptos.entrySet().stream()
        .filter(entry -> entry.getValue().isHasStaking())
        .map(Entry::getKey)
        .collect(Collectors.toList());
  }

  private String getCheapestStaking(Map<String, CryptoDetail> cryptos, String currencySymbolCode) {
    List<String> stakingSymbols = getStakingSymbols(cryptos);
    Map<String, Double> priceMap = new HashMap<>(stakingSymbols.size());
    stakingSymbols.forEach(symbolCode -> priceMap
        .put(symbolCode, getMarketPrice(getMarketName(symbolCode, currencySymbolCode))));
    return Collections.min(priceMap.entrySet(), Map.Entry.comparingByValue()).getKey();
  }

  private void updateTrades(String marketCode, Instant since) {
    apiConsumer.getTrades(marketCode, since != null ? since : Instant.EPOCH)
        .forEach(t -> tradeService.save(TradeService.createTradeEntity(t)));
  }

  /**
   * Calculates if profit would be made
   *
   * @param marketPrice     The current market price
   * @param availableAmount The amount that would be sold
   * @param currentValue    The current value we hold
   * @param fee             The fee that has to be paid
   * @param profitTreshold  The treshold in percentages that needs to be met
   * @return boolean
   */
  boolean hasProfit(double marketPrice, double availableAmount, double currentValue, double fee,
      double profitTreshold) {
    double estimation = availableAmount * marketPrice - fee;
    double absValue = Math.abs(currentValue);
    double valueToIncrease = absValue * (profitTreshold / 100);
    double valueToPass = absValue + valueToIncrease;
    boolean hasProfit = estimation >= valueToPass;
    if (hasProfit) {
      log.info("Estimating profit (> {}%). {} over {}", profitTreshold, estimation, valueToPass);
    }
    return hasProfit;
  }

  /**
   * Calculates the fee that will have to be paid. The fee gets rounded upward to a cent
   *
   * @param amount        The amount of the symbol
   * @param price         The price of the symbol
   * @param feeMultiplier The fee multiplier
   * @return double
   */
  double getFee(double amount, double price, double feeMultiplier) {
    double subCost = amount * price;
    double fee = subCost * feeMultiplier;
    return Math.ceil(fee * 100) / 100.0;
  }

  /**
   * Gets the current value of trades
   *
   * @param trades The trades that need to be checked
   * @return double
   */
  double getCurrentValue(List<TradeEntity> trades) {
    AtomicReference<Double> value = new AtomicReference<>(0.0);
    trades.forEach(t -> {
      double cost = calculateCost(t);
      value.updateAndGet(v -> t.getOrderSide().equals(OrderSide.BUY)
          ? v - cost : v + cost);
    });
    return value.get();
  }

  double calculateCost(TradeEntity tradeEntity) {
    double subCost = tradeEntity.getAmount() * tradeEntity.getPrice();
    double feePaid = tradeEntity.getFeePaid();
    return tradeEntity.getOrderSide().equals(OrderSide.BUY)
        ? subCost + feePaid
        : subCost - feePaid;
  }

  /**
   * Withdraw a certain symbol amount to the given address based on a treshold
   *
   * @param targetSymbol The symbol to withdaw
   * @param treshold     The treshold that needs to be passed
   * @param address      The target address
   */
  @Override
  public void withdraw(String targetSymbol, double treshold, String address) {
    if (0 >= treshold) {
      log.warn("No withdrawal treshold set. Not withdrawing.");
      return;
    }
    BitvavoSymbol symbol = apiConsumer.getSymbol(targetSymbol);
    log.info("Checking for auto withdrawal. Currently available: {}", symbol.getAvailable());
    if (treshold <= symbol.getAvailable()) {
      double toWithdraw = symbol.getAvailable() - treshold;
      log.info("Withdrawing {} to {}", toWithdraw, address);
      apiConsumer.withdraw(targetSymbol, toWithdraw, address);
    }
  }
}
