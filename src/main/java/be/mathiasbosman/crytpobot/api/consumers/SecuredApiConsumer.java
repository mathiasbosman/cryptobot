package be.mathiasbosman.crytpobot.api.consumers;

public interface SecuredApiConsumer {
  String getSecret();

  String getKey();
}
