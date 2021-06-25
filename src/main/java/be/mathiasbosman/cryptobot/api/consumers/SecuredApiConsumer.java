package be.mathiasbosman.cryptobot.api.consumers;

public interface SecuredApiConsumer {
  String getSecret();

  String getKey();
}
