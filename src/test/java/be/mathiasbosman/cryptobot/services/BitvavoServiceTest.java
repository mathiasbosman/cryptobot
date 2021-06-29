package be.mathiasbosman.cryptobot.services;

import static org.assertj.core.api.Assertions.assertThat;

import be.mathiasbosman.cryptobot.api.configuration.BitvavoConfig.CryptoDetail;
import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.fxtures.CryptoDetailFixture;
import be.mathiasbosman.cryptobot.fxtures.TradeEntityFixture;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class BitvavoServiceTest {

  private final BitvavoService service = new BitvavoService(null, null);

  @Test
  void hasProfit() {
    assertThat(service.hasProfit(10, 4, -30, 1, 5)).isTrue();
    assertThat(service.hasProfit(10, 4, -30, 1, 50)).isFalse();
  }

  @Test
  void getCurrentValue() {
    assertThat(service.getCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY)
    ))).isEqualTo(-504);
    assertThat(service.getCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY)
    ))).isEqualTo(-1008);
    assertThat(service.getCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.SELL)
    ))).isEqualTo(-8);
    assertThat(service.getCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(60, 10, 4, OrderSide.SELL)
    ))).isEqualTo(92);
    assertThat(service.getCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(60, 10, 4, OrderSide.SELL)
    ))).isEqualTo(-412);
  }

  @Test
  void getFee() {
    assertThat(service.getFee(100, 0.5, 0.0025)).isEqualTo(0.13);
  }

  @Test
  void getStakingSymbols() {
    Map<String, CryptoDetail> cryptos = Map.of(
        "ABC", CryptoDetailFixture.newCryptoDetail(false),
        "DEF", CryptoDetailFixture.newCryptoDetail(true),
        "GHI", CryptoDetailFixture.newCryptoDetail(true)
    );
    assertThat(service.getStakingSymbols(cryptos)).containsExactlyInAnyOrder("DEF", "GHI");
  }
}