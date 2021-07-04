package be.mathiasbosman.cryptobot.api.entities;

/**
 * Holds Symbol information
 */
public interface Symbol {

  /**
   * The code of the symbol used in the market
   *
   * @return code string
   */
  String getCode();

  /**
   * The amount of this symbol available in the wallet
   *
   * @return amount of the symbol
   */
  double getAvailable();

  /**
   * Amount of the symbol currently held in ordes
   *
   * @return amount in orders
   */
  double getInOrder();
}
