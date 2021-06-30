package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.persistency.entities.CryptoEntity;
import be.mathiasbosman.cryptobot.persistency.repositories.CryptoRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CryptoService extends AbstractEntityService<CryptoEntity> {

  private static final Sort defaultSort = Sort.sort(CryptoEntity.class).by(CryptoEntity::getCode)
      .ascending();
  private final CryptoRepository repository;

  @Override
  public Sort getDefaultSort() {
    return defaultSort;
  }

  @Override
  public CryptoEntity save(CryptoEntity entity) {
    return repository.save(entity);
  }

  @Transactional(readOnly = true)
  public List<CryptoEntity> getAllCrypto() {
    return repository.findAll(getDefaultSort());
  }

  @Transactional(readOnly = true)
  public CryptoEntity getCrypto(String code) {
    return repository.getByCode(code);
  }
}
