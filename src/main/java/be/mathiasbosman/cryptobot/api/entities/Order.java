package be.mathiasbosman.cryptobot.api.entities;

public interface Order {

  String getId();

  OrderStatus getStatus();
}
