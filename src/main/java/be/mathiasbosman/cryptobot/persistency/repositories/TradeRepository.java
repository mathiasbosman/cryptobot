package be.mathiasbosman.cryptobot.persistency.repositories;

import be.mathiasbosman.cryptobot.persistency.entities.TradeEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<TradeEntity, UUID> {

  List<TradeEntity> findAllByMarketCode(String marketCode);

  TradeEntity findFirstByMarketCodeOrderByTimestampDesc(String marketCode);
}
