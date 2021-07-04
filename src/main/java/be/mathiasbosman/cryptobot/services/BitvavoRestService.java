package be.mathiasbosman.cryptobot.services;

import java.time.Instant;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class BitvavoRestService extends SimpleRestService {

  private Integer remainingLimit;
  private Instant resetTime;

  public BitvavoRestService(RestTemplate restTemplate) {
    super(restTemplate);
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
    resetTime = Instant
        .ofEpochMilli(Long.parseLong(
            Objects.requireNonNull(headers.get("Bitvavo-Ratelimit-ResetAt")).get(0)));
    log.trace("Post exchange ({}). Remaining limit = {}, reset at {}",
        endpoint, remainingLimit, resetTime);
  }

  /**
   * Returns wether or not the rest service can be called depending on the remaining limit as
   * configured.
   *
   * @return true or false
   */
  @Override
  public boolean canExecute() {
    // if not set yet we can assume it's safe to call the api
    if (remainingLimit == null || remainingLimit > 0) {
      log.trace("Remaining API limit = {}", remainingLimit);
      return true;
    }
    // if resetTime has passed we can reset
    if (resetTime.isBefore(Instant.now())) {
      remainingLimit = null;
      return true;
    }
    log.warn("Remaining API limit = {}. Reset time = {}", remainingLimit, resetTime);
    return false;
  }

  public boolean canExecute(int limitweight) {
    return remainingLimit == null || (canExecute() && remainingLimit >= limitweight);
  }
}
