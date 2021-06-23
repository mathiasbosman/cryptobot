package be.mathiasbosman.crytpobot.entities.bitvavo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitvavoAccount {

  private Fees fees;

  @Getter
  @Setter
  public static class Fees {

    /**
     * Fee for trades that take liquidity from the orderbook.
     */
    private double taker;
    /**
     * Fee for trades that add liquidity to the orderbook.
     */
    private double maker;
    /**
     * Your trading volume in the last 30 days measured in EUR.
     */
    private double volume;
  }
}
