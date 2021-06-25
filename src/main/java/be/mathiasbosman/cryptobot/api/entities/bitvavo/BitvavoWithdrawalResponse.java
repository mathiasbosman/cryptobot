package be.mathiasbosman.cryptobot.api.entities.bitvavo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitvavoWithdrawalResponse {
  private boolean success;
  private String symbol;
  private double amount;
}
