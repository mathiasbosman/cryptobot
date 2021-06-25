package be.mathiasbosman.cryptobot.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface RestService {

  boolean canExecute();

  void preExchange();

  <T> void postExchange(ResponseEntity<T> entity, String endpoint);

  <T> T postEntity(String endpoint, HttpHeaders headers, String body, Class<T> objectClass);

  <T> T getEntity(String endpoint, Class<T> objectClass);

  <T> T getEntity(String endpoint, HttpHeaders headers, Class<T> objectClass);

  <T> T getEntity(String endpoint, HttpHeaders headers, Class<T> objectClass, String body);
}
