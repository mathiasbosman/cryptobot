package be.mathiasbosman.cryptobot.services;

import org.junit.jupiter.api.Test;

class BitvavoServiceTest {

  @Test
  void estimateBuyCost() {
    // assertThat(estimateBuyCost(842.37255405, 0.0050919, 0.0025)).isEqualTo(4.30);
    // assertThat(estimateBuyCost(1001.16706752, 0.0049319, 0.0025)).isEqualTo(4.95);
  }

  @Test
  void estimateSellingReturn() {
    // assertThat(estimateSellingReturn(995.36759319, 0.0049940, 0.0025)).isEqualTo(4.95);
    System.out.println(test(102.749696, 0.52982, 0.0025));
  }

  private double test(double amount, double price, double feeMultiplier) {
    double cost = amount * price;
    double fee = Math.ceil(cost * feeMultiplier * 100) / 100.0;
    double totalCost = cost - fee;
    return Math.round(totalCost * 100) / 100.0;
  }
}