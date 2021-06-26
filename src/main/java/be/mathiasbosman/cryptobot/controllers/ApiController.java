package be.mathiasbosman.cryptobot.controllers;

import be.mathiasbosman.cryptobot.api.entities.Order;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import java.util.List;

public interface ApiController {

  List<Symbol> getSymbols();

  Symbol getSymbol(String symbolCode);

  double getTickerPrice(String marketCode);

  Order buy(String marketCode, double amount);

  Order sell(String marketCode, double amount);
}
