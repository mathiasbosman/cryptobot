package be.mathiasbosman.cryptobot.controllers;

import be.mathiasbosman.cryptobot.persistency.entities.TradeEntity;
import java.util.List;

public interface TradeController {

  /**
   * Returns all saved trades
   *
   * @param limit limit of trades to return
   * @return list of {@link TradeEntity}
   */
  List<TradeEntity> getTrades(int limit);

  /**
   * Returns saved trades for certain market
   *
   * @param marketCode code of the market
   * @return list of {@link TradeEntity}
   */
  List<TradeEntity> getTradesInMarket(String marketCode);
}
