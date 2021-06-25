package be.mathiasbosman.cryptobot.api.entities;

public interface Trade {

  String getMarketCode();

  String getOrderId();

  double getAmount();

  double getPrice();

  double getFee();

}
