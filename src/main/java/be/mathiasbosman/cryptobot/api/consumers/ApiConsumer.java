package be.mathiasbosman.cryptobot.api.consumers;

import be.mathiasbosman.cryptobot.api.entities.Asset;
import be.mathiasbosman.cryptobot.api.entities.Market;
import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import be.mathiasbosman.cryptobot.api.entities.TickerPrice;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoOrderResponse;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoTrade;
import java.time.Instant;
import java.util.List;

public interface ApiConsumer {

  String getBaseUri();

  List<? extends Market> getMarkets();

  Market getMarket(String code);

  List<? extends Asset> getAssets();

  Asset getAsset(String symbol);

  TickerPrice getTickerPrice(String marketCode);

  List<BitvavoTrade> getTrades(String marketCode, Instant start);

  List<? extends Symbol> getSymbols();

  Symbol getSymbol(String symbol);

  void withdraw(String symbol, double amount, String address);

  BitvavoOrderResponse newOrder(String marketCode, OrderSide side, OrderType type, double amount);

}
