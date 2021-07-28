package be.mathiasbosman.cryptobot.api.dto;

import be.mathiasbosman.cryptobot.persistency.entities.CryptoEntity;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CryptoEntityDto {
  private final UUID id;
  private final String code;
  private final Double reBuyAt;
  private final Double profitThreshold;
  private final Double stopThreshold;

  public static CryptoEntityDto fromEntity(CryptoEntity entity) {
    return new CryptoEntityDto(entity.getId(), entity.getCode(), entity.getReBuyAt(),
        entity.getProfitThreshold(), entity.getStopThreshold());
  }
}
