package be.mathiasbosman.cryptobot.api.consumers;

import be.mathiasbosman.cryptobot.api.entities.Asset;
import be.mathiasbosman.cryptobot.api.entities.Market;
import be.mathiasbosman.cryptobot.api.entities.Order;
import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import be.mathiasbosman.cryptobot.api.entities.TickerPrice;
import be.mathiasbosman.cryptobot.api.entities.Trade;
import java.time.Instant;
import java.util.List;

public interface ApiConsumer {

  /**
   * Returns the base URI used by the consumer
   *
   * @return the base URI (including prefix)
   */
  String getBaseUri();

  /**
   * Gets all markets available trough the API
   *
   * @return available list of {@link Market}
   */
  List<? extends Market> getMarkets();

  /**
   * Gets a singular market based on its unique code
   *
   * @param code The unique code of the market
   * @return {@link Market} found or null if none found
   */
  Market getMarket(String code);

  /**
   * Returns all supported assets
   *
   * @return list of {@link Asset}
   */
  List<? extends Asset> getAssets();

  /**
   * Returns a singular asset based on its symbol
   *
   * @param symbol The asset's symbol
   * @return the {@link Asset} or null if none found
   */
  Asset getAsset(String symbol);

  /**
   * Get the tickerprice for a certain market
   *
   * @param marketCode The unique code of the market
   * @return {@link TickerPrice} information
   */
  TickerPrice getTickerPrice(String marketCode);

  /**
   * Get all trades since a given time for a certain market
   *
   * @param marketCode The code of the market
   * @param start      Timestamp to start from
   * @return list of {@link Trade} since the given time for a certain market
   */
  List<? extends Trade> getTrades(String marketCode, Instant start);


  /**
   * Get all currently held symbols
   *
   * @return list of {@link Symbol}
   */
  List<? extends Symbol> getSymbols();

  /**
   * Get a symbol
   *
   * @param symbol The symbol's unique code
   * @return {@link Symbol} for the given code
   */
  Symbol getSymbol(String symbol);

  /**
   * Places a new order
   *
   * @param marketCode The market's unique code
   * @param side       The {@link OrderSide} of the order
   * @param type       The {@link OrderType} of the order
   * @param amount     The amount to place the order
   * @return {@link Order}
   */
  Order newOrder(String marketCode, OrderSide side, OrderType type, double amount);

  /**
   * Withdraw a given amount of a given symbol to a given address
   *
   * @param symbol  The code of the symbol to withdraw
   * @param amount  The amount to withdraw
   * @param address The target address to withdraw too
   */
  void withdraw(String symbol, double amount, String address);

}
