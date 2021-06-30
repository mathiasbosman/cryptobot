package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.configuration.BitvavoConfig;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CronService {

  private final BitvavoConfig config;
  private final BitvavoService cryptoService;
  private final BitvavoRestService restService;

  /**
   * Checks for every holding symbol if there's profit to be made. Runs every 15 seconds
   */
  @Scheduled(fixedRate = 15000)
  public void autoSellOnProfit() {
    if (!restService.canExecute(50)) {
      return;
    }
    cryptoService.sellOnProfit(
        config.getDefaultCurrency(),
        config.getDefaultProfitTreshold(),
        Instant.ofEpochMilli(config.getStartTimestamp()),
        config.isAutoRebuy(),
        config.isAutoBuyCheapestStaking()
    );
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
}
