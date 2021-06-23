package be.mathiasbosman.crytpobot.services;

import org.springframework.http.HttpHeaders;

public interface RestService {

  <T> T postEntity(String endpoint, HttpHeaders headers, String body, Class<T> objectClass);

  <T> T getEntity(String endpoint, Class<T> objectClass);

  <T> T getEntity(String endpoint, HttpHeaders headers, Class<T> objectClass);

  <T> T getEntity(String endpoint, HttpHeaders headers, Class<T> objectClass, String body);
}
