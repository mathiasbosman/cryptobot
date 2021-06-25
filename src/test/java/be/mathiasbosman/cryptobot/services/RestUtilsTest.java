package be.mathiasbosman.cryptobot.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class RestUtilsTest {

  @Test
  void resolvePath() {
    assertThat(RestUtils.resolvePath("path")).isEqualTo("path");
    assertThat(RestUtils.resolvePath("path", "subpath")).isEqualTo("path/subpath");
    assertThat(RestUtils.resolvePath("path/", "subpath")).isEqualTo("path/subpath");
    assertThat(RestUtils.resolvePath("path", "/subpath")).isEqualTo("path/subpath");
    assertThat(RestUtils.resolvePath("path/", "/subpath")).isEqualTo("path/subpath");
    assertThat(RestUtils.resolvePath("/path/", "/subpath/")).isEqualTo("path/subpath");

    assertThat(RestUtils.resolvePath("https://path")).isEqualTo("https://path");
    assertThat(RestUtils.resolvePath("https://path", "subpath")).isEqualTo("https://path/subpath");
  }

  @Test
  void resolveUri() {
    String baseUri = "https://path?param={0}&secondParam={1}";
    assertThat(RestUtils.resolveUri(baseUri, "param1", "param2"))
        .isEqualTo("https://path?param=param1&secondParam=param2");
    assertThat(RestUtils.resolveUri(baseUri, "param1", "param2", "param3"))
        .isEqualTo("https://path?param=param1&secondParam=param2");
    assertThat(RestUtils.resolveUri(baseUri, "param1", true))
        .isEqualTo("https://path?param=param1&secondParam=true");
    assertThat(RestUtils.resolveUri(baseUri, "param1", 2))
        .isEqualTo("https://path?param=param1&secondParam=2");
  }

  @Test
  void getFormatter() {
    Instant instant = Instant.ofEpochMilli(1624472520000L);
    String format = RestUtils.getFormatter("yyyy-MM-dd HH:mm::ss").format(instant);
    assertThat(format).isEqualTo("2021-06-23 20:22::00");
  }
}
