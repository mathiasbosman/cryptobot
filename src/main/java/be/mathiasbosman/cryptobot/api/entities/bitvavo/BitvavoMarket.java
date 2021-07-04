package be.mathiasbosman.cryptobot.api.entities.bitvavo;

import be.mathiasbosman.cryptobot.api.entities.Market;
import be.mathiasbosman.cryptobot.api.entities.MarketStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitvavoMarket implements Market {

  @JsonProperty("market")
  private String code;
  private MarketStatus status;
  private String quoteCurrency;
  private int pricePrecision;
  @JsonProperty("base")
  private String baseCurrency;
  private double minOrderInQuoteAsset;
  private double minOrderInBaseAsset;
}
