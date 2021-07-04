package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.persistency.entities.DbEntity;
import org.springframework.data.domain.Sort;

/**
 * Service for database entities
 *
 * @param <E> the {@link DbEntity} used
 */
public interface EntityService<E extends DbEntity> {

  /**
   * Gets the default sorting method for the entity
   *
   * @return {@link Sort}
   */
  Sort getDefaultSort();

  /**
   * Saves and returns the entity
   *
   * @param entity entity to save
   * @return {@link DbEntity}
   */
  E save(E entity);
}
