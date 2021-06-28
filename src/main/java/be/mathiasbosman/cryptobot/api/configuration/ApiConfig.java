package be.mathiasbosman.cryptobot.api.configuration;

import be.mathiasbosman.cryptobot.utils.RestUtils;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
  private String dateTimeFormat = "yyyy-MM-dd HH:mm";

  @Bean
  public DateTimeFormatter dateTimeFormatter() {
    return RestUtils.getDateTimeFormatter(dateTimeFormat);
  }
}

