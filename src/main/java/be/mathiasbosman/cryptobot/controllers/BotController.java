package be.mathiasbosman.cryptobot.controllers;

import be.mathiasbosman.cryptobot.api.entities.Order;
import be.mathiasbosman.cryptobot.api.entities.Symbol;
import be.mathiasbosman.cryptobot.services.CryptoService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
@AllArgsConstructor
public class BotController implements ApiController {

  private final CryptoService cryptoService;

  @Override
  @GetMapping("/crypto/")
  public Symbol getSymbol(@RequestParam String symbolCode) {
    return cryptoService.getSymbol(symbolCode);
  }

  @Override
  @GetMapping("/crypto")
  public List<Symbol> getSymbols() {
    return cryptoService.getCurrentCrypto();
  }

  @Override
  @GetMapping("/market/price")
  public double getTickerPrice(@RequestParam String marketCode) {
    return cryptoService.getMarketPrice(marketCode);
  }

  @Override
  @PostMapping("/order/buy")
  public Order buy(@RequestParam String marketCode, @RequestParam double amount) {
    return cryptoService.buy(marketCode, amount);
  }

  @Override
  @PostMapping("/order/sell")
  public Order sell(String marketCode, double amount) {
    return cryptoService.sell(marketCode, amount);
  }
}
