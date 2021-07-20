package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.entities.Asset;
import be.mathiasbosman.cryptobot.api.entities.Market;
import be.mathiasbosman.cryptobot.api.entities.Order;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import be.mathiasbosman.cryptobot.api.entities.TickerPrice;
import java.time.Instant;
import java.util.List;

public interface CryptoCurrencyService {

  Asset getAsset(String assetCode);

  Symbol getSymbol(String symbolCode);

  List<Symbol> getCurrentCrypto(List<String> exclusions);

  Market getMarket(String marketCode);

  Order sell(String symbolCode, double amount);

  Order buy(String marketCode, double amount);

  String getMarketName(String symbolCode, String targetMarketCode);

  void sellOnProfit(String currencySymbolCode, double defaultProfitThreshold,
      Double defaultReBuyAt, Double defaultStopThreshold,
      Instant defaultStartTime, boolean autoReBuy, boolean autoBuyCheapestStaking);

  void withdraw(String targetSymbol, double threshold, String address);

  TickerPrice getTickerPrice(String marketCode);
}
