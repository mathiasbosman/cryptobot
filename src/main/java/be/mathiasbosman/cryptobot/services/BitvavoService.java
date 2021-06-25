package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.configuration.BitvavoConfig;
import be.mathiasbosman.cryptobot.api.consumers.BitvavoConsumer;
import be.mathiasbosman.cryptobot.api.entities.Market;
import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoAccount.Fees;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoOrderResponse;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoSymbol;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BitvavoService implements CryptoService {

  private final BitvavoConsumer apiConsumer;
  private final BitvavoConfig config;
  private final TradeService tradeService;

  private Fees fees;

  public BitvavoService(BitvavoConsumer apiConsumer,
      BitvavoConfig config, TradeService tradeService) {
    this.apiConsumer = apiConsumer;
    this.config = config;
    this.tradeService = tradeService;
  }

  @Override
  public List<BitvavoSymbol> getCurrentCrypto() {
    BitvavoSymbol currencySymbol = getCurrencySymbol();
    return apiConsumer.getSymbols().stream()
        .filter(s -> s.getAvailable() > 0 && !s.getCode().equals(currencySymbol.getCode()))
        .collect(Collectors.toList());
  }

  @Override
  public double estimateBuyCost(double amount, double price, double feeMultiplier) {
    double cost = amount * price;
    double fee = cost * feeMultiplier;
    double totalCost = cost + fee;
    return Math.round(totalCost * 100) / 100.0;
  }

  @Override
  public double estimateSellingReturn(double amount, double price, double feeMultiplier) {
    //todo, not sure if this is actually correct
    double cost = amount * price;
    double fee = Math.ceil(cost * feeMultiplier * 100) / 100.0;
    double totalCost = cost - fee;
    return Math.round(totalCost * 100) / 100.0;
  }

  @Override
  public BitvavoOrderResponse sellAll(Symbol symbol) {
    return sell(symbol, symbol.getAvailable());
  }

  @Override
  public BitvavoOrderResponse sell(Symbol symbol, double amount) {
    String marketCode = getDefaultMarketName(symbol.getCode());
    return apiConsumer
        .newOrder(marketCode, OrderSide.SELL, OrderType.MARKET, amount);
  }

  @Override
  public BitvavoOrderResponse buy(Market market, double amount) {
    log.info("Buying {} in {}", amount, market.getCode());
    return apiConsumer
        .newOrder(market.getCode(), OrderSide.BUY, OrderType.MARKET, amount);
  }

  @Override
  public BitvavoSymbol getCurrencySymbol() {
    return apiConsumer.getSymbol(config.getDefaultCurrency());
  }

  @Override
  public String getMarketName(String sourceCode, String targetMarketCode) {
    return sourceCode + "-" + targetMarketCode;
  }

  @Override
  public String getDefaultMarketName(String sourceCode) {
    return getMarketName(sourceCode, config.getDefaultCurrency());
  }

  private boolean hasProfit(double currentValue, double estimation, double percentageToSurpass) {
    double currentAbsValue = Math.abs(currentValue);
    double increasedValue = currentAbsValue * (percentageToSurpass / 100);
    double valueToPass = currentAbsValue + increasedValue;
    double diff = estimation - valueToPass;
    log.trace("Estimated return: {} value to pass: {} difference: {}",
        estimation, valueToPass, diff);
    return diff >= 0;
  }

  @Override
  public void sellOnProfit(double profitPercentage, String liquidCurrency, double autoRebuy) {
    if (profitPercentage <= 0) {
      log.info("No profit percentage set. Will not auto sell.");
      return;
    }

    // find fees
    fees = apiConsumer.getAccountInfo().getFees();
    log.info("Checking for auto profits ({}%) taker fee = {} maker fee = {}",
        profitPercentage, fees.getTaker(), fees.getMaker());
    // find all current symbols
    List<BitvavoSymbol> currentCrypto = getCurrentCrypto();
    currentCrypto.forEach(symbol -> {
      double availableAmount = symbol.getAvailable();
      String marketCode = getMarketName(symbol.getCode(), liquidCurrency);
      double currentValue = tradeService.getCurrentValue(marketCode);
      if (currentValue > 0) {
        throw new IllegalStateException(
            "Current value of " + symbol.getCode() + " is higher then 0 (" + currentValue + ")");
      }
      double price = apiConsumer.getTickerPrice(marketCode).getPrice();
      double estimation = estimateSellingReturn(availableAmount, price, fees.getMaker());
      log.debug("Checking {} ({} ~ {})", marketCode, availableAmount, estimation);

      if (hasProfit(currentValue, estimation, profitPercentage)) {
        log.info("Selling {}", availableAmount);
        BitvavoOrderResponse order = sell(symbol, availableAmount);
        log.info("{} sold {} at {}. (fee: {}) ", order.getMarketCode(),
            order.getFilledAmount(), order.getPrice(), order.getFeePaid());
      }
    });
  }

  @Override
  public void withdraw(String targetSymbol, double treshold, String address) {
    if (0 >= treshold) {
      log.info("No withdrawaltreshold set. Not withdrawing.");
      return;
    }
    BitvavoSymbol symbol = apiConsumer.getSymbol(targetSymbol);
    log.info("Checking for auto redrawal. Currently available: {}", symbol.getAvailable());
    if (treshold <= symbol.getAvailable()) {
      double toWithdraw = symbol.getAvailable() - treshold;
      log.info("Withdrawing {} to {}", toWithdraw, address);
      apiConsumer.withdraw(targetSymbol, toWithdraw, address);
    }
  }
}
