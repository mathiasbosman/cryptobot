package be.mathiasbosman.cryptobot.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class BitvavoServiceTest {

  private final BitvavoService service = new BitvavoService(null, null, null);

  @Test
  void getRoundedFee() {
    assertThat(service.getFee(100, 0.5, 0.0025)).isEqualTo(0.13);
  }
}