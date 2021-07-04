package be.mathiasbosman.cryptobot.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class RestUtils {

  RestUtils() {
    // util class
  }

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

  public static <T> List<T> objectArrayToList(T[] array) {
    return array != null ? Arrays.asList(array) : new ArrayList<>();
  }
}
