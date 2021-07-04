package be.mathiasbosman.cryptobot.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface RestService {

  /**
   * Wether or not the API can be called
   *
   * @return boolean
   */
  boolean canExecute();

  /**
   * Function to execute before the call
   */
  void preExchange();

  /**
   * Function executed post call
   *
   * @param entity   the {@link ResponseEntity} returned
   * @param endpoint the endpoint called
   * @param <T>      {@link ResponseEntity}
   */
  <T> void postExchange(ResponseEntity<T> entity, String endpoint);

  /**
   * Execute a post call
   *
   * @param endpoint    the endpoint to call
   * @param headers     {@link HttpHeaders} to send
   * @param body        body to send
   * @param objectClass class to return
   * @param <T>         the {@link Class} to return
   * @return object of the given class
   */
  <T> T postEntity(String endpoint, HttpHeaders headers, String body, Class<T> objectClass);

  /**
   * Execute a gett call without headers and body
   *
   * @param endpoint    the endpoint to call
   * @param objectClass class to return
   * @param <T>         the {@link Class} to return
   * @return object of the given class
   */
  <T> T getEntity(String endpoint, Class<T> objectClass);

  /**
   * Execute a get call without body
   *
   * @param endpoint    the endpoint to call
   * @param headers     {@link HttpHeaders} to send
   * @param objectClass class to return
   * @param <T>         the {@link Class} to return
   * @return object of the given class
   */
  <T> T getEntity(String endpoint, HttpHeaders headers, Class<T> objectClass);

  /**
   * Exectue a get call
   *
   * @param endpoint    the endpoint to call
   * @param headers     {@link HttpHeaders} to send
   * @param objectClass class to return
   * @param <T>         the {@link Class} to return
   * @param body        the body to send
   * @return object of the given class
   */
  <T> T getEntity(String endpoint, HttpHeaders headers, Class<T> objectClass, String body);
}
