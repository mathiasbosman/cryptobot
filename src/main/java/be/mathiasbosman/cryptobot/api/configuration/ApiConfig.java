package be.mathiasbosman.cryptobot.api.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(ApiConfig.PREFIX)
public class ApiConfig {

  public static final String PREFIX = "api";

  /**
   * API key
   */
  private String key;
  /**
   * API secret
   */
  private String secret;
  /**
   * Base url of the API (if any)
   */
  private String baseUrl;
}

