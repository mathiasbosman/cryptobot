package be.mathiasbosman.cryptobot.api.entities;

public interface TickerPrice {

  String getMarketCode();

  double getPrice();
}
