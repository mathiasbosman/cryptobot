package be.mathiasbosman.crytpobot.services;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.web.util.UriComponentsBuilder;

public class RestUtils {

  public static String resolvePath(String... paths) {
    return Arrays.stream(paths).filter(p -> !p.isBlank()).map(p -> {
      String trimmedPath = p.endsWith("/") ? p.substring(0, p.length() - 1) : p;
      return trimmedPath.startsWith("/") ? trimmedPath.substring(1) : trimmedPath;
    }).collect(Collectors.joining("/"));
  }

  public static String formatUri(String url, Object... params) {
    return MessageFormat.format(url, params);
  }

  public static String resolveUri(String url, Object... params) {
    return UriComponentsBuilder.fromHttpUrl(formatUri(url, params)).toUriString();
  }
}
