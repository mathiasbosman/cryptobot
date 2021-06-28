package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.persistency.entities.DbEntity;
import org.springframework.data.domain.Sort;

public interface EntityService<E extends DbEntity> {

  Sort getDefaultSort();

  E save(E entity);
}
