package be.mathiasbosman.crytpobot.entities.bitvavo;

import be.mathiasbosman.crytpobot.entities.Symbol;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitvavoSymbol implements Symbol {
  @JsonProperty("symbol")
  private String code;
  private double available;
  private double inOrder;
}
