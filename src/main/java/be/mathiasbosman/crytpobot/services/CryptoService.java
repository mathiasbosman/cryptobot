package be.mathiasbosman.crytpobot.services;

import be.mathiasbosman.crytpobot.entities.FeeType;
import be.mathiasbosman.crytpobot.entities.Market;
import be.mathiasbosman.crytpobot.entities.Order;
import be.mathiasbosman.crytpobot.entities.Symbol;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoMarket;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoSymbol;
import java.util.List;

public interface CryptoService<M extends Market, S extends Symbol> {

  List<S> getCurrentCrypto();

  double calculateFee(FeeType type, double amount);

  double estimateBuyCost(M market, double amount);

  double estimateSellingReturn(M market, double amount);

  Order sellAll(S symbol);

  Order sell(S symbol, double amount);

  Order buy(M market, double amount);

  S getCurrencySymbol();

  M getMarket(S source, S target);
}
