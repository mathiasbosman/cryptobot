package be.mathiasbosman.cryptobot.api.configuration;

import be.mathiasbosman.cryptobot.api.entities.FeeType;
import java.util.Map;
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
  private double autoWithdrawTreshold;
  /**
   * List of crypto with custom settings
   */
  private Map<String, CryptoDetail> cryptos;
  /**
   * Default symbolcode of the currency used
   */
  private String defaultCurrency = "EUR";
  /**
   * Default treshold (in percentage) at which to sell. Can be overriden for individual cryptos
   */
  private double defaultSellTreshold;
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
  private long startTimestamp = 0;

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

  @Getter
  @Setter
  public static class CryptoDetail {

    /**
     * The amount of the currency to rebuy once sold
     */
    private double rebuyAt = 0;

    /**
     * The treshold in % this crypto has to surpass before being sold
     */
    private double sellTreshold = 0;

    /**
     * Wether or not staking is available for this crypto
     */
    private boolean hasStaking = false;
  }
}
