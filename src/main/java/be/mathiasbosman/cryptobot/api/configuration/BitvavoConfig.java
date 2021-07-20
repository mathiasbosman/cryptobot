package be.mathiasbosman.cryptobot.api.configuration;

import be.mathiasbosman.cryptobot.api.entities.FeeType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(BitvavoConfig.PREFIX)
public class BitvavoConfig {

  public static final String PREFIX = "bitvavo";

  /**
   * Whether or not to automatically re-buy
   */
  private boolean autoReBuy = false;

  /**
   * Indicates if the cheapest staking should be bought if the minimum re-buy threshold cannot be met
   * after selling an asset
   */
  private boolean autoBuyCheapestStaking = false;
  /**
   * Threshold at which the remaining currency will be withdraw to the withdraw address
   */
  private double autoWithdrawThreshold = Double.MAX_VALUE;
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
   * Address used when auto withdrawing
   */
  private String withdrawAddress;
  /**
   * Applicable fee type
   */
  private FeeType feeType = FeeType.TAKER;
  /**
   * Timestamp at which to start to calculate current values
   */
  private long startTimestamp = Long.MIN_VALUE;

  /**
   * List of endpoints;
   */
  private Endpoints endpoints;

  @Getter
  @Setter
  public static class Endpoints {

    private String account;
    private String asset;
    private String assets;
    private String market;
    private String markets;
    private String order;
    private String symbol;
    private String symbols;
    private String tickerPrice;
    private String trades;
    private String withdrawal;
  }
}
