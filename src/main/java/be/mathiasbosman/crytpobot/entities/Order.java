package be.mathiasbosman.crytpobot.entities;

public interface Order {

  String getId();

  int getCreated();

  int getUpdated();

  OrderStatus getStatus();
}
