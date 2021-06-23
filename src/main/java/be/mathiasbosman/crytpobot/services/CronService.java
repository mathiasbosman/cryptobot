package be.mathiasbosman.crytpobot.services;

import be.mathiasbosman.crytpobot.api.consumers.BitvavoConsumer;
import be.mathiasbosman.crytpobot.configuration.BitvavoConfig;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoMarket;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoSymbol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CronService {

  private final BitvavoConfig config;
  private final BitvavoConsumer consumer;
  private final BitvavoService cryptoService;

  public CronService(BitvavoService cryptoService, BitvavoConfig config, BitvavoConsumer consumer) {
    this.cryptoService = cryptoService;
    this.consumer = consumer;
    this.config = config;
  }

  @Scheduled(fixedRate = 10000)
  public void autoSellOnProfit() {
    log.info("Checking for auto profits");
    final double autoSellOnProfit = config.getAutoSellProfit();
    if (autoSellOnProfit > 0) {
      // loop trough current symbols
      cryptoService.getCurrentCrypto().forEach(s -> {
        BitvavoMarket targetMarket = cryptoService.getMarket(s, cryptoService.getCurrencySymbol());
        double estimated = cryptoService.estimateSellingReturn(targetMarket, s.getAvailable());
        log.info("Estimated return when selling all in market {}({}) is {}",
            targetMarket.getCode(), s.getAvailable(), estimated
        );
        if (estimated >= autoSellOnProfit) {
          log.info("Estimated ({}) is higher then auto sell profit ({}) selling {} {}",
              estimated, autoSellOnProfit, s.getCode(), s.getAvailable());
          //cryptoService.sellAll(s);
        }
      });
    }
  }

  @Scheduled(fixedRate = 30000)
  public void autoWithdrawal() {
    BitvavoSymbol currencySymbol = cryptoService.getCurrencySymbol();
    log.info("Checking for auto redrawal. Currently available: {}", currencySymbol.getAvailable());
    if (currencySymbol.getAvailable() > 100) {
      log.info("Auto withdrawing 100");
      consumer.withdraw(currencySymbol.getCode(), 100, config.getWithdrawAddress());
    }
  }
}
