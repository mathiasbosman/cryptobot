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
   * Threshold at which the remaining currency will be withdraw to the withdraw address
   */
  private double autoWithdrawThreshold = Double.MAX_VALUE;

  /**
   * Address used when auto withdrawing
   */
  private String withdrawAddress;

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
