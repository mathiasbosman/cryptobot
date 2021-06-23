package be.mathiasbosman.crytpobot.api.consumers;

import be.mathiasbosman.crytpobot.entities.Asset;
import be.mathiasbosman.crytpobot.entities.Market;
import be.mathiasbosman.crytpobot.entities.OrderSide;
import be.mathiasbosman.crytpobot.entities.OrderType;
import be.mathiasbosman.crytpobot.entities.Symbol;
import be.mathiasbosman.crytpobot.entities.TickerPrice;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoAsset;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoOrderResponse;
import java.util.List;
import java.util.Map;

public interface ApiConsumer {

  String getBaseUri();

  Long getServerTime();

  List<? extends Market> getMarkets();

  Market getMarket(String code);

  List<? extends Asset> getAssets();

  Asset getAsset(String symbol);

  TickerPrice getTickerPrice(Market market);

  List<? extends Symbol> getSymbols();

  Symbol getSymbol(String symbol);

  boolean withdraw(String symbol, double amount, String address);

  BitvavoOrderResponse newOrder(Market market, OrderSide side, OrderType type, double amount);

}
