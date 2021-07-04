package be.mathiasbosman.cryptobot.api.entities;

/**
 * An asset represents an item available in a certain market
 */
public interface Asset {

  /**
   * Short version of the asset name used in market names
   *
   * @return symbol code string
   */
  String getSymbol();

  /**
   * Returns the asset's name
   *
   * @return the full name of the asset
   */
  String getName();
}
