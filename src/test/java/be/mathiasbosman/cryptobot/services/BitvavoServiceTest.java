package be.mathiasbosman.cryptobot.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BitvavoServiceTest {

  private final BitvavoService service = new BitvavoService(null, null, null, null);

  @Test
  void hasProfit() {
    assertThat(service.hasProfit(10, 4, -30, 1, 5)).isTrue();
    assertThat(service.hasProfit(10, 4, -30, 1, 50)).isFalse();
  }

  @Test
  void getFee() {
    assertThat(service.getFee(100, 0.5, 0.0025)).isEqualTo(0.13);
  }
}