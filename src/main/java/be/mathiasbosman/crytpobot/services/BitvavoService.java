package be.mathiasbosman.crytpobot.services;

import be.mathiasbosman.crytpobot.api.consumers.BitvavoConsumer;
import be.mathiasbosman.crytpobot.configuration.BitvavoConfig;
import be.mathiasbosman.crytpobot.entities.FeeType;
import be.mathiasbosman.crytpobot.entities.Market;
import be.mathiasbosman.crytpobot.entities.Order;
import be.mathiasbosman.crytpobot.entities.OrderSide;
import be.mathiasbosman.crytpobot.entities.OrderType;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoMarket;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoSymbol;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BitvavoService implements CryptoService<BitvavoMarket, BitvavoSymbol> {

  private final BitvavoConsumer apiConsumer;
  private final BitvavoConfig config;

  public BitvavoService(BitvavoConsumer apiConsumer, BitvavoConfig bitvavoConfig) {
    this.apiConsumer = apiConsumer;
    this.config = bitvavoConfig;

    getCurrentCrypto().forEach(s -> {
      log.info("holding {} of {}", s.getAvailable(), s.getCode());
      BitvavoMarket market = getMarket(s, getCurrencySymbol());
      double v = estimateSellingReturn(market, s.getAvailable());
      log.info("Estimate return when selling {} would be {}", s.getAvailable(), v);
    });
  }

  @Override
  public List<BitvavoSymbol> getCurrentCrypto() {
    return apiConsumer.getSymbols().stream()
        .filter(s -> s.getAvailable() > 0 && !s.getCode().equals(getCurrencySymbol().getCode()))
        .collect(Collectors.toList());
  }

  @Override
  public double calculateFee(FeeType type, double amount) {
    return type.equals(FeeType.TAKER) ? calculateTakerFee(amount) : calculateMakerFee(amount);
  }

  private double calculateTakerFee(double amount) {
    return amount * apiConsumer.getAccountInfo().getFees().getTaker();
  }

  private double calculateMakerFee(double amount) {
    return amount * apiConsumer.getAccountInfo().getFees().getMaker();
  }

  @Override
  public double estimateBuyCost(BitvavoMarket market, double amount) {
    double buyingAmount = amount - calculateFee(config.getFeeType(), amount);
    return buyingAmount / apiConsumer.getTickerPrice(market).getPrice();
  }

  @Override
  public double estimateSellingReturn(BitvavoMarket market, double amount) {
    double currentPrice = apiConsumer.getTickerPrice(market).getPrice();
    double subtotal = amount * currentPrice;
    return subtotal - calculateFee(config.getFeeType(), amount);
  }

  @Override
  public Order sellAll(BitvavoSymbol symbol) {
    return sell(symbol, symbol.getAvailable());
  }

  @Override
  public Order sell(BitvavoSymbol symbol, double amount) {
    Market market = getMarket(symbol, getCurrencySymbol());
    return apiConsumer
        .newOrder(market, OrderSide.SELL, OrderType.MARKET, amount);
  }

  @Override
  public Order buy(BitvavoMarket market, double amount) {
    if (amount < market.getMinOrderInQuoteAsset()) {
      log.warn("Could not buy for {} as the minium amount of the quote asset is {}",
          amount, market.getMinOrderInQuoteAsset());
      return null;
    }
    return apiConsumer.newOrder(market, OrderSide.BUY, OrderType.MARKET, amount);
  }

  @Override
  public BitvavoSymbol getCurrencySymbol() {
    return apiConsumer.getSymbol(config.getDefaultCurrency());
  }

  @Override
  public BitvavoMarket getMarket(BitvavoSymbol source, BitvavoSymbol target) {
    return apiConsumer.getMarket(source.getCode() + "-" + target.getCode());
  }
}
