package be.mathiasbosman.cryptobot.api.consumers;

import be.mathiasbosman.cryptobot.api.configuration.ApiConfig;
import be.mathiasbosman.cryptobot.api.configuration.BitvavoConfig;
import be.mathiasbosman.cryptobot.api.configuration.BitvavoConfig.Endpoints;
import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoAccount;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoAsset;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoBuyRequest;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoMarket;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoOrderRequest;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoOrderResponse;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoSellRequest;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoSymbol;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoTickerPrice;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoTrade;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoWithdrawalRequest;
import be.mathiasbosman.cryptobot.api.entities.bitvavo.BitvavoWithdrawalResponse;
import be.mathiasbosman.cryptobot.services.BitvavoRestService;
import be.mathiasbosman.cryptobot.utils.RestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class BitvavoConsumer implements ApiConsumer, SecuredApiConsumer {

  private final ApiConfig apiConfig;
  private final Endpoints endpoints;
  private final BitvavoRestService restService;
  private final String apiKey;
  private final String apiSecret;

  private static final ObjectMapper jsonMapper = new ObjectMapper();

  public BitvavoConsumer(ApiConfig apiConfig, BitvavoConfig bitvavoConfig,
      BitvavoRestService restService) {
    this.apiConfig = apiConfig;
    this.restService = restService;

    // set configurable fields
    this.endpoints = bitvavoConfig.getEndpoints();
    this.apiKey = apiConfig.getKey();
    this.apiSecret = apiConfig.getSecret();
  }

  @Override
  public String getBaseUri() {
    return apiConfig.getBaseUrl();
  }

  @Override
  public List<BitvavoMarket> getMarkets() {
    String uri = RestUtils.resolvePath(getBaseUri(), endpoints.getMarkets());
    BitvavoMarket[] marketArray = restService
        .getEntity(RestUtils.resolvePath(uri), BitvavoMarket[].class);
    return RestUtils.objectArrayToList(marketArray);
  }

  @Override
  public BitvavoMarket getMarket(String code) {
    String uri = RestUtils.resolvePath(getBaseUri(), endpoints.getMarket());
    String fullUri = RestUtils.resolveUri(uri, code);
    return restService.getEntity(fullUri, BitvavoMarket.class);
  }

  @Override
  public List<BitvavoAsset> getAssets() {
    String uri = RestUtils.resolvePath(getBaseUri(), endpoints.getAssets());
    BitvavoAsset[] assets = restService.getEntity(uri, BitvavoAsset[].class);
    return RestUtils.objectArrayToList(assets);
  }

  @Override
  public BitvavoAsset getAsset(String symbol) {
    String uri = RestUtils.resolvePath(getBaseUri(), endpoints.getAsset());
    String fullUri = RestUtils.resolveUri(uri, symbol);
    return restService.getEntity(fullUri, BitvavoAsset.class);
  }

  @Override
  public BitvavoTickerPrice getTickerPrice(String marketCode) {
    String uri = RestUtils.resolvePath(getBaseUri(), endpoints.getTickerPrice());
    String fullUri = RestUtils.resolveUri(uri, marketCode);
    return restService.getEntity(fullUri, BitvavoTickerPrice.class);
  }

  @Override
  public String getSecret() {
    return this.apiSecret;
  }

  @Override
  public String getKey() {
    return this.apiKey;
  }

  @Override
  public List<BitvavoTrade> getTrades(String marketCode, Instant start) {
    String timeStamp = start != null ? String.valueOf(start.toEpochMilli() + 1) : "0";
    String endpoint = MessageFormat.format(endpoints.getTrades(), marketCode, timeStamp);
    String uri = RestUtils.resolvePath(getBaseUri(), endpoint);
    BitvavoTrade[] trades = restService
        .getEntity(uri, buildHeaders(endpoint), BitvavoTrade[].class);
    return RestUtils.objectArrayToList(trades);
  }

  @Override
  public List<BitvavoSymbol> getSymbols() {
    String endpoint = endpoints.getSymbols();
    String uri = RestUtils.resolvePath(getBaseUri(), endpoint);
    BitvavoSymbol[] symbols = restService
        .getEntity(uri, buildHeaders(endpoint), BitvavoSymbol[].class);
    return RestUtils.objectArrayToList(symbols);
  }

  @Override
  public BitvavoSymbol getSymbol(String symbol) {
    String endpoint = MessageFormat.format(endpoints.getSymbol(), symbol);
    String uri = RestUtils.resolvePath(getBaseUri(), endpoint);
    BitvavoSymbol[] symbols = restService
        .getEntity(uri, buildHeaders(endpoint), BitvavoSymbol[].class);
    Optional<BitvavoSymbol> firstElement = Arrays.stream(symbols).findFirst();
    return firstElement.orElse(null);
  }

  public BitvavoAccount getAccountInfo() {
    String endpoint = endpoints.getAccount();
    String uri = RestUtils.resolvePath(getBaseUri(), endpoint);
    return restService.getEntity(uri, buildHeaders(endpoint), BitvavoAccount.class);
  }

  @Override
  public void withdraw(String symbol, double amount, String address) {
    BitvavoWithdrawalRequest withdrawalRequest = new BitvavoWithdrawalRequest(symbol,
        String.valueOf(amount), address);
    String endpoint = endpoints.getWithdrawal();
    String fullUri = RestUtils.resolvePath(getBaseUri(), endpoint);
    String body = buildRestBody(withdrawalRequest);
    restService.postEntity(fullUri, buildHeaders(endpoint, HttpMethod.POST, body), body,
        BitvavoWithdrawalResponse.class);
  }

  @Override
  public BitvavoOrderResponse newOrder(String marketCode, OrderSide side, OrderType type,
      double amount) {
    BitvavoOrderRequest request = side.equals(OrderSide.BUY)
        ? new BitvavoBuyRequest(marketCode, type, amount)
        : new BitvavoSellRequest(marketCode, type, amount);
    String endpoint = endpoints.getOrder();
    String body = buildRestBody(request);
    return restService.postEntity(
        RestUtils.resolvePath(getBaseUri(), endpoint),
        buildHeaders(endpoint, HttpMethod.POST, body),
        body,
        BitvavoOrderResponse.class
    );
  }

  private String buildRestBody(Object bodyObject) {
    StringBuilder bodyBuilder = new StringBuilder();
    if (bodyObject != null) {
      try {
        bodyBuilder.append(jsonMapper.writeValueAsString(bodyObject));
      } catch (JsonProcessingException e) {
        throw new IllegalArgumentException("Could not parse value to json", e);
      }
    }
    return bodyBuilder.toString();
  }

  private HttpHeaders buildHeaders(String uri) {
    return buildHeaders(uri, HttpMethod.GET, null);
  }

  @SuppressWarnings("UastIncorrectHttpHeaderInspection")
  private HttpHeaders buildHeaders(String uri, HttpMethod httpMethod, String body) {
    long serverTime = Instant.now().toEpochMilli();
    String signature = getAuthenticationSignature(serverTime, httpMethod, uri, body);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.add("BITVAVO-ACCESS-KEY", getKey());
    httpHeaders.add("BITVAVO-ACCESS-TIMESTAMP", String.valueOf(serverTime));
    httpHeaders.add("BITVAVO-ACCESS-SIGNATURE", signature);
    return httpHeaders;
  }

  private String getAuthenticationSignature(long time, HttpMethod method, String endpoint,
      String body) {
    if (!StringUtils.hasLength(getSecret()) || !StringUtils.hasLength(getKey())) {
      throw new IllegalStateException(
          "Could not create authentication signature. The API key or secret is missing.");
    }
    String toHash = time
        + method.name()
        + endpoint
        + (StringUtils.hasLength(body) ? body : "");
    try {
      Mac sha256 = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec = new SecretKeySpec(getSecret().getBytes(StandardCharsets.UTF_8),
          sha256.getAlgorithm());
      sha256.init(secretKeySpec);
      return new String(Hex.encodeHex(sha256.doFinal(toHash.getBytes(StandardCharsets.UTF_8))));
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      log.error("Caught exception while creating signature", e);
      throw new IllegalStateException("Creating the authentication signature failed", e);
    }
  }

}
