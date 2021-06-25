package be.mathiasbosman.cryptobot.api.entities.bitvavo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BitvavoWithdrawalRequest {
  private final String symbol;
  private final String amount;
  private final String address;
}
