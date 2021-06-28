package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.persistency.entities.DbEntity;
import org.springframework.data.domain.Sort;

public abstract class AbstractEntityService<E extends DbEntity> implements EntityService<E> {

  @Override
  public Sort getDefaultSort() {
    return Sort.sort(DbEntity.class).by(DbEntity::getId).ascending();
  }
}
