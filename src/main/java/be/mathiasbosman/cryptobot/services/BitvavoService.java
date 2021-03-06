package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.configuration.BotConfig;
import be.mathiasbosman.cryptobot.api.consumers.BitvavoConsumer;
import be.mathiasbosman.cryptobot.api.entities.FeeType;
import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import be.mathiasbosman.cryptobot.api.entities.TickerPrice;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoAccount.Fees;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoAsset;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoMarket;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoOrderResponse;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoSymbol;
import be.mathiasbosman.cryptobot.persistency.entities.CryptoEntity;
import be.mathiasbosman.cryptobot.persistency.entities.TradeEntity;
import be.mathiasbosman.cryptobot.utils.Numberutils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class BitvavoService implements CryptoCurrencyService {

  private final BitvavoConsumer apiConsumer;
  private final CryptoService cryptoService;
  private final TradeService tradeService;

  private final BotConfig botConfig;

  @Override
  public BitvavoAsset getAsset(String assetCode) {
    return apiConsumer.getAsset(assetCode);
  }

  /**
   * Returns the crypto Symbol based on its code
   *
   * @param symbolCode The symbol's code
   * @return Symbol
   */
  @Override
  public BitvavoSymbol getSymbol(String symbolCode) {
    return apiConsumer.getSymbol(symbolCode);
  }

  @Override
  public BitvavoMarket getMarket(String marketCode) {
    return apiConsumer.getMarket(marketCode);
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
   * Sells a certain amount in a certain market
   *
   * @param marketCode Market to sell in
   * @param amount     Amount to sell
   * @return BitvavoOrderResponse
   */
  @Override
  public BitvavoOrderResponse sell(String marketCode, double amount) {
    log.info("Selling {} in {}", amount, marketCode);
    if (botConfig.isTransactionMock()) {
      log.warn("Transaction mocks enabled. No transaction will take place.");
      return null;
    }
    BitvavoOrderResponse sellOrder = apiConsumer
        .newOrder(marketCode, OrderSide.SELL, OrderType.MARKET, amount);
    log.info("{}: sold {} at {} (fee: {})",
        sellOrder.getMarketCode(), sellOrder.getFilledAmount(), sellOrder.getPrice(),
        sellOrder.getFeePaid());
    return sellOrder;
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
    if (botConfig.isTransactionMock()) {
      log.warn("Transaction mocks enabled. No transaction will take place.");
      return null;
    }
    return apiConsumer.newOrder(marketCode, OrderSide.BUY, OrderType.MARKET, amount);
  }

  /**
   * Returns the market name used in Bitvavo
   *
   * @param sourceCode       The first symbol's code
   * @param targetMarketCode The target's symbol code
   * @return the market's name (split with -)
   */
  @Override
  public String getMarketName(String sourceCode, String targetMarketCode) {
    return sourceCode + "-" + targetMarketCode;
  }

  /**
   * Returns the market price (ticker price) of a market
   *
   * @param marketCode The market's code
   * @return The market price
   */
  @Override
  public TickerPrice getTickerPrice(String marketCode) {
    return apiConsumer.getTickerPrice(marketCode);
  }

  @Override
  public void sellOnProfit() {
    String currencySymbolCode = botConfig.getDefaultCurrency();
    List<Symbol> currentCrypto = getCurrentCrypto(Collections.singletonList(currencySymbolCode));
    if (currentCrypto.isEmpty()) {
      log.warn("Not holding any crypto.");
      return;
    }
    Fees fees = apiConsumer.getAccountInfo().getFees();
    for (Symbol symbol : currentCrypto) {
      String symbolCode = symbol.getCode();
      CryptoEntity cryptoEntity = cryptoService
          .getOrCreateCrypto(symbol.getCode(),
              botConfig.getDefaultProfitThreshold(),
              botConfig.getDefaultReBuyAt(),
              botConfig.getDefaultStopThreshold());
      double profitThreshold = cryptoEntity.getProfitThreshold();
      if (profitThreshold <= 0) {
        log.warn("No sell threshold could be determined for {}.", symbolCode);
        continue;
      }
      log.debug("Checking profit for {}. Sell threshold: {}", symbolCode, profitThreshold);
      String marketCode = getMarketName(symbol.getCode(), currencySymbolCode);

      // Make sure we got all trades available
      TradeEntity latestTrade = tradeService.getLatestTrade(marketCode);
      updateTrades(marketCode,
          latestTrade != null ?
              latestTrade.getTimestamp() :
              Instant.ofEpochMilli(botConfig.getStartTimestamp()));
      double available = symbol.getAvailable();
      double currentValue = tradeService
          .calculateCurrentValue(tradeService.getAllTrades(marketCode));
      double marketPrice = getTickerPrice(marketCode).getPrice();
      double feeMultiplier =
          botConfig.getFeeType().equals(FeeType.TAKER) ? fees.getTaker() : fees.getMaker();
      double fee = getFee(available, marketPrice, feeMultiplier);
      if (!hasProfit(marketPrice, available, currentValue, fee, profitThreshold)) {
        continue;
      }
      sell(marketCode, available);
      Double stopThreshold = cryptoEntity.getStopThreshold();
      boolean autoReBuy = botConfig.isAutoReBuy();
      if (!autoReBuy || (stopThreshold != null && stopThreshold >= marketPrice)) {
        log.info("Not re-buying. Auto re-buy: {}. Stop threshold: {}. Market price: {}",
            autoReBuy, stopThreshold, marketPrice);
      }

      BitvavoSymbol currencySymbol = apiConsumer.getSymbol(currencySymbolCode);
      double availableCurrency = currencySymbol.getAvailable();
      // decide amount to check against
      if (cryptoEntity.getReBuyAt() != null) {
        buy(marketCode, Math.min(availableCurrency, cryptoEntity.getReBuyAt()));
      }
    }
  }

  private void updateTrades(String marketCode, Instant since) {
    apiConsumer.getTrades(marketCode, since)
        .forEach(t -> tradeService.save(TradeService.createTradeEntity(t)));
  }

  /**
   * Calculates if profit would be made
   *
   * @param marketPrice     The current market price
   * @param availableAmount The amount that would be sold
   * @param currentValue    The current value we hold
   * @param fee             The fee that has to be paid
   * @param profitThreshold The threshold in percentages that needs to be met
   * @return boolean
   */
  boolean hasProfit(double marketPrice, double availableAmount, double currentValue, double fee,
      double profitThreshold) {
    double estimation = availableAmount * marketPrice - fee;
    double absValue = Math.abs(currentValue);
    double valueToIncrease = absValue * (profitThreshold / 100);
    double valueToPass = absValue + valueToIncrease;
    boolean hasProfit = estimation >= valueToPass;

    double diff = estimation - valueToPass;
    double percentageDiff = diff / valueToPass * 100;
    if (!hasProfit) {
      log.debug("Estimating profit (> {}%). {} over {} (= {}%)",
          profitThreshold,
          Numberutils.format(estimation, 4),
          Numberutils.format(valueToPass, 4),
          Numberutils.format(percentageDiff, 2));
    } else {
      log.info("Profit threshold ({}%) exceeded with {}%",
          profitThreshold,
          Numberutils.format(percentageDiff, 2));
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
    BigDecimal bAmount = new BigDecimal(Double.toString(amount));
    BigDecimal bPrice = new BigDecimal(Double.toString(price));

    BigDecimal subCost = bAmount.multiply(bPrice);
    BigDecimal transactionFee = subCost.multiply(new BigDecimal(Double.toString(feeMultiplier)));
    BigDecimal transactionCost = subCost.add(transactionFee);
    BigDecimal transactionCostRounded = transactionCost.setScale(2, RoundingMode.UP);
    BigDecimal difference = transactionCostRounded.subtract(transactionCost).setScale(8, RoundingMode.HALF_UP);
    BigDecimal totalFee = transactionFee.add(difference).setScale(4, RoundingMode.UP);
    return totalFee.doubleValue();
  }

  /**
   * Withdraw a certain symbol amount to the given address based on a threshold
   *
   * @param threshold The threshold that needs to be passed
   * @param address   The target address
   */
  @Override
  public void withdraw(double threshold, String address) {
    if (0 >= threshold) {
      log.warn("No withdrawal threshold set. Not withdrawing.");
      return;
    }
    String targetSymbol = botConfig.getDefaultCurrency();
    BitvavoSymbol symbol = apiConsumer.getSymbol(targetSymbol);
    log.info("Checking for auto withdrawal. Currently available: {}", symbol.getAvailable());
    if (threshold <= symbol.getAvailable()) {
      double toWithdraw = symbol.getAvailable() - threshold;
      log.info("Withdrawing {} to {}", toWithdraw, address);
      apiConsumer.withdraw(targetSymbol, toWithdraw, address);
    }
  }
}
