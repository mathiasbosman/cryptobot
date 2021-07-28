package be.mathiasbosman.cryptobot.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BitvavoServiceTest {

  private final BitvavoService service = new BitvavoService(null, null, null, null);

  @Test
  void hasProfit() {
    assertThat(service.hasProfit(10, 4, -30, 1, 5)).isTrue();
    assertThat(service.hasProfit(10, 4, -30, 1, 50)).isFalse();
    assertThat(service.hasProfit(3, 100, -288, 12, 0)).isTrue();
    assertThat(service.hasProfit(3, 100, -288, 12, 1)).isFalse();
    assertThat(service.hasProfit(3, 100, -288, 9.12, 1)).isTrue();
  }

  @Test
  void getFee() {
    assertThat(service.getFee(100, 0.5, 0.0025)).isEqualTo(0.13);
    assertThat(service.getFee(1304.36625307, 0.005066, 0.0025)).isEqualTo(0.0221); //0.01791943805262
    assertThat(service.getFee(1292.96553663, 0.0050841, 0.0025)).isEqualTo(0.0165); //0.016433915219417
    assertThat(service.getFee(0.0331, 4951, 0.001)).isEqualTo(0.1719);
  }
}