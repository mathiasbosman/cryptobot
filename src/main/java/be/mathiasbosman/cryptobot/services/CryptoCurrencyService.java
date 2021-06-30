package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.entities.Order;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import java.time.Instant;
import java.util.List;

public interface CryptoCurrencyService {

  Symbol getSymbol(String symbolCode);

  List<Symbol> getCurrentCrypto(List<String> exclusions);

  Order sell(String symbolCode, double amount);

  Order buy(String marketCode, double amount);

  String getMarketName(String symbolCode, String targetMarketCode);

  void sellOnProfit(String currencySymbolCode, double defaultSellTreshold,
      Instant defaultStartTime, boolean autoRebuy, boolean autoBuyCheapestStaking);

  void withdraw(String targetSymbol, double treshold, String address);

  double getMarketPrice(String marketCode);
}
