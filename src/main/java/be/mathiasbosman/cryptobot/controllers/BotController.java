package be.mathiasbosman.cryptobot.controllers;

import be.mathiasbosman.cryptobot.api.entities.Order;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import be.mathiasbosman.cryptobot.api.entities.TickerPrice;
import be.mathiasbosman.cryptobot.persistency.entities.TradeEntity;
import be.mathiasbosman.cryptobot.services.CryptoCurrencyService;
import be.mathiasbosman.cryptobot.services.TradeService;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController("/api")
public class BotController implements ApiController, TradeController {

  private final CryptoCurrencyService cryptoCurrencyService;
  private final TradeService tradeService;

  @Override
  @GetMapping("/symbol")
  public Symbol getSymbol(@RequestParam String symbolCode) {
    log.info("Getting symbol {}", symbolCode);
    return cryptoCurrencyService.getSymbol(symbolCode);
  }

  @Override
  @GetMapping("/crypto")
  public List<Symbol> getSymbols() {
    return cryptoCurrencyService.getCurrentCrypto(Collections.emptyList());
  }

  @Override
  @GetMapping("/market/price")
  public TickerPrice getTickerPrice(@RequestParam String marketCode) {
    return cryptoCurrencyService.getTickerPrice(marketCode);
  }

  @Override
  @PostMapping("/order/buy")
  public Order buy(@RequestParam String marketCode, @RequestParam double amount) {
    return cryptoCurrencyService.buy(marketCode, amount);
  }

  @Override
  @PostMapping("/order/sell")
  public Order sell(String marketCode, double amount) {
    return cryptoCurrencyService.sell(marketCode, amount);
  }

  @Override
  @GetMapping("/trades")
  public List<TradeEntity> getTrades(@RequestParam int limit) {
    return tradeService.getLatestTrades(limit);
  }
}
