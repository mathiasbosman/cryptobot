package be.mathiasbosman.crytpobot.configuration;

import be.mathiasbosman.crytpobot.entities.FeeType;
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

  private double autoSellProfit;
  private String defaultCurrency = "EUR";
  private String withdrawAddress;
  private FeeType feeType = FeeType.TAKER;

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
    private String time;
    private String tickerPrice;
    private String withdrawal;
  }
}
