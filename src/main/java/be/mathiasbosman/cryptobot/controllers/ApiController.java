package be.mathiasbosman.cryptobot.controllers;

import be.mathiasbosman.cryptobot.api.entities.Order;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import be.mathiasbosman.cryptobot.api.entities.TickerPrice;
import java.util.List;

public interface ApiController {

  /**
   * Returns are symbols
   *
   * @return list of {@link Symbol}
   */
  List<Symbol> getSymbols();

  /**
   * Get specific symbol by code
   *
   * @param symbolCode the symbol code to lookup
   * @return {@link Symbol}
   */
  Symbol getSymbol(String symbolCode);

  /**
   * Returns the ticker price for a certain market by code
   *
   * @param marketCode the market's code
   * @return the current {@link TickerPrice}
   */
  TickerPrice getTickerPrice(String marketCode);

  /**
   * Place a buy order
   *
   * @param marketCode the market to buy in
   * @param amount     the amount to buy
   * @return the {@link Order} placed
   */
  Order buy(String marketCode, double amount);

  /**
   * Place a sell order
   *
   * @param marketCode the market to sell in
   * @param amount     the amount to sell for
   * @return the {@link Order} placed
   */
  Order sell(String marketCode, double amount);
}
