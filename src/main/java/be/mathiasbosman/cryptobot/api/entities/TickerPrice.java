package be.mathiasbosman.cryptobot.api.entities;

/**
 * Information about the ticker price
 */
public interface TickerPrice {

  /**
   * The market code
   *
   * @return market code string
   */
  String getMarketCode();

  /**
   * The ticker price
   *
   * @return price
   */
  double getPrice();
}
