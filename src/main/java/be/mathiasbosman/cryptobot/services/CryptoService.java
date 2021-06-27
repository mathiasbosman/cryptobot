package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.entities.Order;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import java.util.List;

public interface CryptoService {

  Symbol getSymbol(String symbolCode);

  List<Symbol> getCurrentCrypto();

  Order sell(String symbolCode, double amount);

  Order buy(String marketCode, double amount);

  String getMarketName(String symbolCode, String targetMarketCode);

  void sellOnProfit(double profitQuote, String liquidCurrency, double autoRebuy);

  void withdraw(String targetSymbol, double treshold, String address);

  double getMarketPrice(String marketCode);
}
