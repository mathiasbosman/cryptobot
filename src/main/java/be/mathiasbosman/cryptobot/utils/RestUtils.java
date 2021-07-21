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

  /**
   * Resolves multiple strings into a valid path seperated by "/". This does NOT add http or https!
   *
   * @param paths Array of paths to combine
   * @return the resolved path
   */
  public static String resolvePath(String... paths) {
    return Arrays.stream(paths).filter(p -> !p.isBlank()).map(p -> {
      String trimmedPath = p.endsWith("/") ? p.substring(0, p.length() - 1) : p;
      return trimmedPath.startsWith("/") ? trimmedPath.substring(1) : trimmedPath;
    }).collect(Collectors.joining("/"));
  }

  /**
   * Short way of resolving a URI with parameters
   *
   * @param url    The uri to resolve
   * @param params The parameters to add to the uri
   * @return the resolved uri
   */
  public static String resolveUri(String url, Object... params) {
    return UriComponentsBuilder.fromHttpUrl(MessageFormat.format(url, params)).toUriString();
  }

  /**
   * Short way of turning object array into a Java list
   *
   * @param array The array of objects to convert
   * @param <T>   The class of the objects
   * @return the converted list
   */
  public static <T> List<T> objectArrayToList(T[] array) {
    return array != null ? Arrays.asList(array) : new ArrayList<>();
  }
}
