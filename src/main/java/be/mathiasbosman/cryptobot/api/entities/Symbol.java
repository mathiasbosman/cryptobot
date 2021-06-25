package be.mathiasbosman.cryptobot.api.entities;

public interface Symbol {
  String getCode();

  double getAvailable();

  double getInOrder();
}
