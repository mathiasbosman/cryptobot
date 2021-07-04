package be.mathiasbosman.cryptobot.api.entities;

/**
 * Holds information about a trade
 */
public interface Trade {

  /**
   * Code of the market
   *
   * @return market code string
   */
  String getMarketCode();

  /**
   * Id of the order
   *
   * @return order id string
   */
  String getOrderId();

  /**
   * The amount traded
   *
   * @return amount traded
   */
  double getAmount();

  /**
   * Price at which was traded
   *
   * @return price
   */
  double getPrice();

  /**
   * Fee that was paid
   *
   * @return fee
   */
  double getFee();

}
