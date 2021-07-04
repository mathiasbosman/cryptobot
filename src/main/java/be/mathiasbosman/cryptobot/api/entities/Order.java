package be.mathiasbosman.cryptobot.api.entities;

/**
 * Represents an order
 */
public interface Order {

  /**
   * The unique id of the order
   *
   * @return order id string
   */
  String getId();

  /**
   * The status of the order
   *
   * @return the {@link OrderStatus}
   */
  OrderStatus getStatus();
}
