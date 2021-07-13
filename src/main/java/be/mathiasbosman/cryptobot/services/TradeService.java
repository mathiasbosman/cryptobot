package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoTrade;
import be.mathiasbosman.cryptobot.persistency.entities.TradeEntity;
import be.mathiasbosman.cryptobot.persistency.repositories.TradeRepository;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class TradeService extends AbstractEntityService<TradeEntity> {

  private final TradeRepository repository;
  private static final Sort defaultSort = Sort.sort(TradeEntity.class).by(TradeEntity::getTimestamp)
      .descending();

  @Override
  public Sort getDefaultSort() {
    return defaultSort;
  }

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

  @Override
  public TradeEntity save(TradeEntity t) {
    log.info("Saving trade {} - {} {} at {} (fee: {}) @ {}",
        t.getMarketCode(), t.getOrderSide(), t.getAmount(), t.getPrice(), t.getFeePaid(),
        t.getTimestamp());
    return repository.save(t);
  }

  /**
   * Returns all trades for a certain market
   *
   * @param marketCode The market's code
   * @return list of {@link TradeEntity}
   */
  @Transactional(readOnly = true)
  public List<TradeEntity> getAllTrades(String marketCode) {
    return repository.findAllByMarketCode(marketCode);
  }

  /**
   * Returns the latest trade in a certain market
   *
   * @param marketCode Market to search for
   * @return {@link TradeEntity}
   */
  @Transactional(readOnly = true)
  public TradeEntity getLatestTrade(String marketCode) {
    return repository.findFirstByMarketCodeOrderByTimestampDesc(marketCode);
  }

  /**
   * Returns the latest trades
   *
   * @param limit amount of trades
   * @return list of {@link TradeEntity}
   */
  @Transactional(readOnly = true)
  public List<TradeEntity> getLatestTrades(int limit) {
    Pageable page = PageRequest.of(0, limit, getDefaultSort());
    return repository.findAll(page).getContent();
  }

  /**
   * Gets the current value of trades for a certain market
   *
   * @param trades The trades that need to be checked
   * @return double
   */
  public double calculateCurrentValue(List<TradeEntity> trades) {
    AtomicReference<Double> value = new AtomicReference<>(0.0);
    trades.forEach(t -> {
      double cost = calculateCost(t);
      value.updateAndGet(v -> t.getOrderSide().equals(OrderSide.BUY)
          ? v - cost : v + cost);
    });
    return value.get();
  }

  double calculateCost(TradeEntity tradeEntity) {
    double subCost = tradeEntity.getAmount() * tradeEntity.getPrice();
    double feePaid = tradeEntity.getFeePaid();
    return tradeEntity.getOrderSide().equals(OrderSide.BUY)
        ? subCost + feePaid
        : subCost - feePaid;
  }
}
