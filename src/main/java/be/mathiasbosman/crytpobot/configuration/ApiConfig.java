package be.mathiasbosman.crytpobot.configuration;

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

  private String key;
  private String secret;
  private String baseUrl;
}

