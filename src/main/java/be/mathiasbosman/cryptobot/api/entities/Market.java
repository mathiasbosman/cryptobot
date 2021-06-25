package be.mathiasbosman.cryptobot.api.entities;

public interface Market {

  String getCode();

  String getBaseCurrency();

  MarketStatus getStatus();

  String getQuote();

  String getPricePrecision();

  double getMinOrderInQuoteAsset();

}
