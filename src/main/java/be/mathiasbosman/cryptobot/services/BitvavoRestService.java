package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.configuration.BitvavoConfig;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class BitvavoRestService extends SimpleRestService {

  private final DateTimeFormatter formatter;
  private final BitvavoConfig config;
  private Integer remainingLimit;

  public BitvavoRestService(RestTemplate restTemplate, DateTimeFormatter formatter,
      BitvavoConfig config) {
    super(restTemplate);
    this.formatter = formatter;
    this.config = config;
  }

  /**
   * Gets some header information after the exchange has happened
   *
   * @param responseEntity Returned entity
   * @param endpoint       URI of the endpoing
   */
  @Override
  public <T> void postExchange(ResponseEntity<T> responseEntity, String endpoint) {
    HttpHeaders headers = responseEntity.getHeaders();
    remainingLimit = Integer.parseInt(
        Objects.requireNonNull(headers.get("Bitvavo-Ratelimit-Remaining")).get(0));
    Instant limitReset = Instant
        .ofEpochMilli(Long.parseLong(
            Objects.requireNonNull(headers.get("Bitvavo-Ratelimit-ResetAt")).get(0)));
    log.trace("Post exchange ({}). Remaining limit = {}, reset at {}",
        endpoint, remainingLimit, formatter.format(limitReset));
  }

  /**
   * Returns wether or not the rest service can be called depending on the remaining limit as
   * configured.
   *
   * @return true or false
   */
  @Override
  public boolean canExecute() {
    if (remainingLimit != null && remainingLimit < config.getMinimumRemainingLimit()) {
      log.warn("Remaining API limit = {}", remainingLimit);
      return false;
    }
    return true;
  }
}
