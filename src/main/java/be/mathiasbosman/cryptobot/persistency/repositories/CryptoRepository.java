package be.mathiasbosman.cryptobot.persistency.repositories;

import be.mathiasbosman.cryptobot.persistency.entities.CryptoEntity;
import java.awt.print.Pageable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRepository extends JpaRepository<CryptoEntity, UUID> {

  CryptoEntity getByCode(String code);
}
