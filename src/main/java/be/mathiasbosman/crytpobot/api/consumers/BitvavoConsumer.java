package be.mathiasbosman.crytpobot.api.consumers;

import be.mathiasbosman.crytpobot.configuration.ApiConfig;
import be.mathiasbosman.crytpobot.configuration.BitvavoConfig;
import be.mathiasbosman.crytpobot.configuration.BitvavoConfig.Endpoints;
import be.mathiasbosman.crytpobot.entities.Market;
import be.mathiasbosman.crytpobot.entities.OrderSide;
import be.mathiasbosman.crytpobot.entities.OrderType;
import be.mathiasbosman.crytpobot.entities.Symbol;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoAccount;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoAsset;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoMarket;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoOrderRequest;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoOrderResponse;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoSymbol;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoTickerPrice;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoWithdrawalRequest;
import be.mathiasbosman.crytpobot.entities.bitvavo.BitvavoWithdrawalResponse;
import be.mathiasbosman.crytpobot.entities.bitvavo.Time;
import be.mathiasbosman.crytpobot.services.RestService;
import be.mathiasbosman.crytpobot.services.RestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
  private final RestService restService;
  private final String apiKey;
  private final String apiSecret;

  private static final ObjectMapper jsonMapper = new ObjectMapper();

  public BitvavoConsumer(ApiConfig apiConfig, BitvavoConfig bitvavoConfig, RestService restService) {
    this.apiConfig = apiConfig;
    this.restService = restService;

    // set confgurable fields
    this.endpoints = bitvavoConfig.getEndpoints();
    this.apiKey = apiConfig.getKey();
    this.apiSecret = apiConfig.getSecret();
  }

  @Override
  public String getBaseUri() {
    return apiConfig.getBaseUrl();
  }

  @Override
  public Long getServerTime() {
    String uri = RestUtils.resolvePath(getBaseUri(), endpoints.getTime());
    Time time = restService.getEntity(uri, Time.class);
    return time != null ? time.getTime() : null;
  }

  @Override
  public List<BitvavoMarket> getMarkets() {
    String uri = RestUtils.resolvePath(getBaseUri(), endpoints.getMarkets());
    BitvavoMarket[] marketArray = restService.getEntity(RestUtils.resolvePath(uri), BitvavoMarket[].class);
    return marketArray != null ? Arrays.stream(marketArray).collect(Collectors.toList()) : null;
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
    return assets != null ? Arrays.stream(assets).collect(Collectors.toList()) : null;
  }

  @Override
  public BitvavoAsset getAsset(String symbol) {
    String uri = RestUtils.resolvePath(getBaseUri(), endpoints.getAsset());
    String fullUri = RestUtils.resolveUri(uri, symbol);
    return restService.getEntity(fullUri, BitvavoAsset.class);
  }

  @Override
  public BitvavoTickerPrice getTickerPrice(Market market) {
    String uri = RestUtils.resolvePath(getBaseUri(), endpoints.getTickerPrice());
    String fullUri = RestUtils.resolveUri(uri, market.getCode());
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
  public List<BitvavoSymbol> getSymbols() {
    String endpoint = endpoints.getSymbols();
    String uri = RestUtils.resolvePath(getBaseUri(), endpoint);
    BitvavoSymbol[] symbols = restService.getEntity(uri, buildHeaders(endpoint), BitvavoSymbol[].class);
    return symbols != null ? Arrays.stream(symbols).collect(Collectors.toList()) : null;
  }

  @Override
  public BitvavoSymbol getSymbol(String symbol) {
    String endpoint = RestUtils.formatUri(endpoints.getSymbol(), symbol);
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
  public boolean withdraw(String symbol, double amount, String address) {
    BitvavoWithdrawalRequest withdrawalRequest = new BitvavoWithdrawalRequest(symbol, String.valueOf(amount), address);

    String endpoint = endpoints.getWithdrawal();
    String fullUri = RestUtils.resolvePath(getBaseUri(), endpoint);
    String body = buildRestBody(withdrawalRequest);
    BitvavoWithdrawalResponse withdrawalResult = restService
        .postEntity(fullUri, buildHeaders(endpoint, HttpMethod.POST, body), body,
            BitvavoWithdrawalResponse.class);
    return withdrawalResult.isSuccess();
  }

  @Override
  public BitvavoOrderResponse newOrder(Market market, OrderSide side, OrderType type, double amount) {
    BitvavoOrderRequest request = new BitvavoOrderRequest(market, side, type, amount);
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
        log.error("Could not parse value to json", e);
      }
    }
    return bodyBuilder.toString();
  }

  private HttpHeaders buildHeaders(String uri) {
    return buildHeaders(uri, HttpMethod.GET, null);
  }

  private HttpHeaders buildHeaders(String uri, HttpMethod httpMethod, String body) {
    long serverTime = getServerTime();
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
      log.error("Could not set API key or secret");
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
    }

    return null;
  }

}
