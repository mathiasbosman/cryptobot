package be.mathiasbosman.crytpobot.entities.bitvavo;

import be.mathiasbosman.crytpobot.entities.Asset;
import be.mathiasbosman.crytpobot.entities.AssetStatus;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitvavoAsset implements Asset {
  private String symbol;
  private String name;
  private int decimals;
  private String depositFee;
  private int depositConfirmations;
  private AssetStatus depositStatus;
  private String withdrawalFee;
  private String withdrawalMinAmount;
  private AssetStatus withdrawalStatus;
  private List<String> networks;
  private String message;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getSymbol() {
    return symbol;
  }
}
