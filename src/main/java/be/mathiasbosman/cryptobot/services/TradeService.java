package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoTrade;
import be.mathiasbosman.cryptobot.persistency.entities.TradeEntity;
import be.mathiasbosman.cryptobot.persistency.repositories.TradeRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TradeService {

  private final TradeRepository repository;

  public static TradeEntity createTradeEntity(BitvavoTrade t) {
    TradeEntity trade = new TradeEntity();
    trade.setOrderType(OrderType.MARKET);
    trade.setOrderId(t.getOrderId());
    trade.setAmount(t.getAmount());
    trade.setFeePaid(t.getFee());
    trade.setPrice(t.getPrice());
    trade.setMarketCode(t.getMarketCode());
    trade.setOrderSide(t.getSide());
    trade.setTimestamp(t.getTimestamp());
    trade.setTaker(t.isTaker());
    return trade;
  }

  public void save(TradeEntity t) {
    repository.save(t);
  }

  @Transactional(readOnly = true)
  public List<TradeEntity> getAllTrades() {
    return repository.findAll();
  }

  public long countAllTrades() {
    return repository.count();
  }

  @Transactional(readOnly = true)
  public List<TradeEntity> getAllTrades(String marketCode) {
    return repository.findAllByMarketCode(marketCode);
  }

  @Transactional(readOnly = true)
  public List<TradeEntity> getAllTrades(String marketCode, OrderSide side) {
    return repository.findAllByMarketCodeAndOrderSide(marketCode, side);
  }

  @Transactional(readOnly = true)
  public TradeEntity getTrade(String marketCode, String orderId) {
    return repository.getByOrderIdAndMarketCode(orderId, marketCode);
  }

  /**
   * Returns the latest trade in a certain market
   *
   * @param marketCode Market to search for
   * @return Latest trade
   */
  @Transactional(readOnly = true)
  public TradeEntity getLatestTrade(String marketCode) {
    return repository.findFirstByMarketCodeOrderByTimestampDesc(marketCode);
  }
}
