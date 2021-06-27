package be.mathiasbosman.cryptobot.controllers;

import be.mathiasbosman.cryptobot.persistency.entities.TradeEntity;
import java.util.List;

public interface TradeController {

  List<TradeEntity> getTrades(int limit);
}
