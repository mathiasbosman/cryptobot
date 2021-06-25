package be.mathiasbosman.cryptobot.api.entities.bitvavo;

import be.mathiasbosman.cryptobot.api.entities.Asset;
import be.mathiasbosman.cryptobot.api.entities.AssetStatus;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitvavoAsset implements Asset {

  private String symbol;
  private String name;
  private int decimals;
  private double depositFee;
  private int depositConfirmations;
  private AssetStatus depositStatus;
  private double withdrawalFee;
  private double withdrawalMinAmount;
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
