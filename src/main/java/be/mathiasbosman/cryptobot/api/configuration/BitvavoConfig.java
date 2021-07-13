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
   * Wether or not to automatically rebuy
   */
  private boolean autoRebuy = false;

  /**
   * Indicates if the cheapest staking should be bought if the minimum rebuy treshold cannot be met
   * after selling an asset
   */
  private boolean autoBuyCheapestStaking = false;
  /**
   * Treshold at which the remaining currency will be withdraw to the withdraw address
   */
  private double autoWithdrawTreshold = Double.MAX_VALUE;
  /**
   * Default symbolcode of the currency used
   */
  private String defaultCurrency = "EUR";
  /**
   * Default treshold (in percentage) at which to sell. Can be overriden for individual cryptos
   */
  private Double defaultProfitTreshold = null;

  /**
   * Deafult amount (in quote currency) to rebuy at; if none set no rebuy will take place
   */
  private Double defaultRebuyAt = null;

  /**
   * Treshold (in base currency) where if the market price surpasses there will be no rebuy. If not
   * set this will not be taken into account
   */
  private Double defaultStopTreshold = null;
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
