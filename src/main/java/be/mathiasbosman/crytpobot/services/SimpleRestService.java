package be.mathiasbosman.crytpobot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SimpleRestService implements RestService {

  private final RestTemplate restTemplate;

  public SimpleRestService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public <T> T postEntity(String endpoint, HttpHeaders headers, String body,
      Class<T> objectClass) {
    return exchangeEntity(endpoint, HttpMethod.POST, headers, objectClass, body);
  }

  @Override
  public <T> T getEntity(String endpoint, HttpHeaders headers, Class<T> objectClass, String body) {
    return exchangeEntity(endpoint, HttpMethod.GET, headers, objectClass, body);
  }

  @Override
  public <T> T getEntity(String endpoint, HttpHeaders headers, Class<T> objectClass) {
    return getEntity(endpoint, headers, objectClass, null);
  }

  @Override
  public <T> T getEntity(String endpoint, Class<T> objectClass) {
    return getEntity(endpoint, null, objectClass);
  }

  private <T> T exchangeEntity(String endpoint, HttpMethod httpMethod, HttpHeaders headers, Class<T> objectClass, String body) {
    HttpEntity<String> httpEntity = StringUtils.hasLength(body)
        ? new HttpEntity<>(body, headers)
        : new HttpEntity<>(headers);
    ResponseEntity<T> responseEntity = restTemplate.exchange(
        endpoint,
        httpMethod,
        httpEntity,
        objectClass);
    return responseEntity.getBody();
  }
}