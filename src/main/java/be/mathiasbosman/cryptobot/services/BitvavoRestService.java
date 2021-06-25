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

  @Override
  public boolean canExecute() {
    return remainingLimit == null || remainingLimit > config.getMinimumRemainingLimit();
  }
}
