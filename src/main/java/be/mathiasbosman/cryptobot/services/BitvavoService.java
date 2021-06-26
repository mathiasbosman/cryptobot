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
import java.text.DecimalFormat;
import java.util.List;
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
  private final BitvavoConfig config;
  private final TradeService tradeService;

  private static final DecimalFormat decimalFormat = new DecimalFormat("##.00");

  @Override
  public List<BitvavoSymbol> getCurrentCrypto() {
    BitvavoSymbol currencySymbol = getCurrencySymbol();
    return apiConsumer.getSymbols().stream()
        .filter(s -> s.getAvailable() > 0 && !s.getCode().equals(currencySymbol.getCode()))
        .collect(Collectors.toList());
  }

  @Override
  public double getFee(double amount, double price, double feeMultipler) {
    double subCost = amount * price;
    double fee = subCost * feeMultipler;
    return Math.ceil(fee * 100) / 100.0;
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
    return buy(market.getCode(), amount);
  }

  private BitvavoOrderResponse buy(String marketCode, double amount) {
    log.info("Buying {} in {}", amount, marketCode);
    return apiConsumer.newOrder(marketCode, OrderSide.BUY, OrderType.MARKET, amount);
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
    double increasedValue = currentValue * (percentageToSurpass / 100);
    double valueToPass = currentValue + increasedValue;
    double diff = estimation - valueToPass;
    log.info("Estimated return: {} value to pass: {} difference: {}",
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
    Fees fees = apiConsumer.getAccountInfo().getFees();
    // find all current symbols
    List<BitvavoSymbol> currentCrypto = getCurrentCrypto();
    currentCrypto.forEach(symbol -> {
      double availableAmount = symbol.getAvailable();
      String marketCode = getMarketName(symbol.getCode(), liquidCurrency);
      double currentValue = getCurrentValue(marketCode);
      if (currentValue <= 0) {
        throw new IllegalStateException(
            "Current value of " + symbol.getCode() + " is <= 0 (" + currentValue + ")");
      }
      double price = apiConsumer.getTickerPrice(marketCode).getPrice();
      double fee = getFee(availableAmount, price, fees.getMaker());
      double estimation = availableAmount * price - fee;
      log.info("Checking {} (currently: {}). Buying {} ~ {} (fee: {})",
          marketCode, decimalFormat.format(currentValue), availableAmount,
          decimalFormat.format(estimation), fee);

      if (hasProfit(currentValue, estimation, profitPercentage)) {
        log.info("Selling {}", availableAmount);
        BitvavoOrderResponse order = sell(symbol, availableAmount);
        log.info("{} sold {} at {}. (fee: {}) ", order.getMarketCode(),
            order.getFilledAmount(), order.getPrice(), order.getFeePaid());

        if (autoRebuy > 0) {
          BitvavoSymbol currency = apiConsumer.getSymbol(liquidCurrency);
          double quoteToRebuy = Math.max(autoRebuy, currency.getAvailable());
          buy(marketCode, quoteToRebuy);
        }
      }
    });
  }

  private double getCurrentValue(String marketCode) {
    AtomicReference<Double> value = new AtomicReference<>(0.0);
    tradeService.getAllTrades(marketCode).forEach(t -> {
      double subCost = t.getAmount() * t.getPrice();
      double paidFee = t.getFeePaid();
      value.updateAndGet(v -> t.getOrderSide().equals(OrderSide.BUY)
          ? v + subCost + paidFee
          : v - subCost - paidFee);

    });
    return value.get();
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
