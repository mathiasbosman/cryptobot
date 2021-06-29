package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.configuration.BitvavoConfig.CryptoDetail;
import be.mathiasbosman.cryptobot.api.entities.Order;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface CryptoService {

  Symbol getSymbol(String symbolCode);

  List<Symbol> getCurrentCrypto(List<String> exclusions);

  Order sell(String symbolCode, double amount);

  Order buy(String marketCode, double amount);

  String getMarketName(String symbolCode, String targetMarketCode);

  void sellOnProfit(String currencySymbolCode,
      Map<String, CryptoDetail> cryptoDetails, double defaultSellTreshold,
      Instant defaultStartTime, boolean autoRebuy, boolean autoBuyCheapestStaking);

  void withdraw(String targetSymbol, double treshold, String address);

  double getMarketPrice(String marketCode);
}
