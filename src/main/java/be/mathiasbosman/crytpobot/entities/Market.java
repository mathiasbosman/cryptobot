package be.mathiasbosman.crytpobot.entities;

public interface Market {

  String getCode();

  String getBaseCurrency();

  MarketStatus getStatus();

  String getQuote();

  String getPricePrecision();
}
