package be.mathiasbosman.cryptobot.persistency.entities;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "cryptos")
public class CryptoEntity implements DbEntity {

  @Id
  @GeneratedValue
  private UUID id;
  private String code;
  private Double rebuyAt;
  private Double profitTreshold;
  private Double stopTreshold;
}
