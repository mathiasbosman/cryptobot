package be.mathiasbosman.crytpobot.entities.bitvavo;

import be.mathiasbosman.crytpobot.entities.TickerPrice;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitvavoTickerPrice implements TickerPrice {
  @JsonProperty("market")
  private String marketCode;
  private double price;

  @Override
  public String getMarketCode() {
    return marketCode;
  }

  public double getPrice() {
    return price;
  }
}
