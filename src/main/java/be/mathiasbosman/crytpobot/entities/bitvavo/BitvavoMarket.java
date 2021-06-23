package be.mathiasbosman.crytpobot.entities.bitvavo;

import be.mathiasbosman.crytpobot.entities.Market;
import be.mathiasbosman.crytpobot.entities.MarketStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitvavoMarket implements Market {
  @JsonProperty("market")
  private String code;
  private MarketStatus status;
  private String quote;
  private String pricePrecision;
  @JsonProperty("base")
  private String baseCurrency;
  private double minOrderInQuoteAsset;
  private double minOrderInBaseAsset;
}
