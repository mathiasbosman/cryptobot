package be.mathiasbosman.cryptobot.api.configuration;

import be.mathiasbosman.cryptobot.api.entities.FeeType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = BotConfig.PREFIX)
public class BotConfig {
  public static final String PREFIX = "bot";

  /**
   * Whether or not to automatically re-buy
   */
  private boolean autoReBuy = false;

  /**
   * Whether or not to automatically sell on profit
   */
  private boolean autoSellOnProfit = false;

  /**
   * Default symbol code of the currency used
   */
  private String defaultCurrency = "EUR";

  /**
   * Default threshold (in percentage) at which to sell. Can be overridden for individual cryptos
   */
  private Double defaultProfitThreshold = null;

  /**
   * Default amount (in quote currency) to re-buy at; if none set no re-buy will take place
   */
  private Double defaultReBuyAt = null;

  /**
   * Threshold (in base currency) where if the market price surpasses there will be no re-buy. If not
   * set this will not be taken into account
   */
  private Double defaultStopThreshold = null;

  /**
   * Applicable fee type
   */
  private FeeType feeType = FeeType.TAKER;

  /**
   * Timestamp at which to start to calculate current values
   */
  private long startTimestamp = Long.MIN_VALUE;

  /**
   * States if transactions should actually take place or rather should be logged
   */
  private boolean transactionMock = false;

}
