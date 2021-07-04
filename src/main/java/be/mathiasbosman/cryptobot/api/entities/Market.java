package be.mathiasbosman.cryptobot.api.entities;

/**
 * Holds information on the market
 */
public interface Market {

  /**
   * The market's code
   *
   * @return the code string
   */
  String getCode();

  /**
   * The base currency
   *
   * @return code of the base currency
   */
  String getBaseCurrency();

  /**
   * The current status of the market
   *
   * @return the {@link MarketStatus}
   */
  MarketStatus getStatus();

  /**
   * The quote currency
   *
   * @return code of the quote currency
   */
  String getQuoteCurrency();

  /**
   * Price precision determines how many significant digits are allowed. The rationale behind this
   * is that for higher amounts, smaller price increments are less relevant. Examples of valid
   * prices for precision 5 are: 100010, 11313, 7500.10, 7500.20, 500.12, 0.0012345. Examples of
   * precision 6 are: 11313.1, 7500.11, 7500.25, 500.123, 0.00123456.
   *
   * @return the price precision
   */
  int getPricePrecision();

  /**
   * The minimum amount in quote currency
   *
   * @return the minimum amount
   */
  double getMinOrderInQuoteAsset();

}
