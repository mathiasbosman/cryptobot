package be.mathiasbosman.cryptobot.api.consumers;

public interface SecuredApiConsumer {

  /**
   * The secret used by the API
   *
   * @return secret string
   */
  String getSecret();

  /**
   * Key used by the API
   *
   * @return key string
   */
  String getKey();
}
