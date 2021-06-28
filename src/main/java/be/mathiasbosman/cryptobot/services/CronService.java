package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.configuration.BitvavoConfig;
import be.mathiasbosman.cryptobot.api.consumers.BitvavoConsumer;
import be.mathiasbosman.cryptobot.persistency.entities.TradeEntity;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CronService {

  private final BitvavoConfig config;
  private final BitvavoConsumer consumer;
  private final BitvavoService cryptoService;
  private final BitvavoRestService restService;
  private final TradeService tradeService;

  /**
   * Checks for every holding symbol if there's profit to be made Runs every 30 seconds
   */
  @Scheduled(fixedRate = 10000)
  public void autoSellOnProfit() {
    if (!restService.canExecute(50)) {
      return;
    }
    updateTrades();
    cryptoService.sellOnProfit(
        config.getAutoSellTreshold(),
        config.getDefaultCurrency(),
        config.getAutoRebuy());
  }

  /**
   * Check every 6 hours if we have more then 100 of the currency symbol
   */
  @Scheduled(cron = "0 0 0/6 ? * *")
  public void autoWithdrawal() {
    if (!restService.canExecute(10)) {
      return;
    }
    cryptoService.withdraw(
        config.getDefaultCurrency(),
        config.getAutoWithdrawTreshold(),
        config.getWithdrawAddress()
    );
  }

  private void updateTrades() {
    List<String> marketCodesToGet = cryptoService.getCurrentCrypto().stream()
        .map(s -> cryptoService.getMarketName(s.getCode(), config.getDefaultCurrency()))
        .collect(Collectors.toList());

    marketCodesToGet.forEach(m -> {
      // find latest timestamp for each marketcode so we don't query too much
      TradeEntity latestTrade = tradeService.getLatestTrade(m);
      Instant latestTimestamp = latestTrade != null
          ? latestTrade.getTimestamp()
          : Instant.ofEpochMilli(config.getStartTimestamp());
      // get trades since this timestamp
      consumer.getTrades(m, latestTimestamp).forEach(t -> {
        double buyPrice = t.getAmount() * t.getPrice();
        double total = buyPrice + t.getFee();
        log.debug("Saving trade in market {} amount: {}, fee ({}): {}, price: {} => TOTAL COST: {}",
            t.getMarketCode(),
            t.getAmount(),
            t.getFeeCurrency(),
            t.getFee(),
            t.getPrice(),
            total
        );
        tradeService.save(TradeService.createTradeEntity(t));
      });
    });
  }
}
