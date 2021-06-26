package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.entities.Market;
import be.mathiasbosman.cryptobot.api.entities.Order;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import java.util.List;

public interface CryptoService {

  List<? extends Symbol> getCurrentCrypto();

  double getFee(double amount, double price, double feeMultipler);

  Order sellAll(Symbol symbolCode);

  Order sell(Symbol symbolCode, double amount);

  Order buy(Market market, double amount);

  Symbol getCurrencySymbol();

  String getMarketName(String symbolCode, String targetMarketCode);

  String getDefaultMarketName(String symbolCode);

  void sellOnProfit(double profitQuote, String liquidCurrency, double autoRebuy);

  void withdraw(String targetSymbol, double treshold, String address);
}
