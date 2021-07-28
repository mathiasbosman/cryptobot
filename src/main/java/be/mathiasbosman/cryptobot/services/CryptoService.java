package be.mathiasbosman.cryptobot.services;

import be.mathiasbosman.cryptobot.api.dto.CryptoEntityDto;
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

  @Transactional
  public CryptoEntity getOrCreateCrypto(String code, Double profitThreshold, Double reBuyAt,
      Double stopThreshold) {
    CryptoEntity crypto = getCrypto(code);
    if (crypto != null) {
      return crypto;
    }
    CryptoEntity newEntity = new CryptoEntity();
    newEntity.setCode(code);
    newEntity.setReBuyAt(reBuyAt);
    newEntity.setStopThreshold(stopThreshold);
    newEntity.setProfitThreshold(profitThreshold);
    return save(newEntity);
  }

  @Transactional
  public CryptoEntity updateCrypto(String code, Double reBuyAt, Double profitThreshold,
      Double stopThreshold) {
    CryptoEntity crypto = getCrypto(code);
    crypto.setReBuyAt(reBuyAt);
    crypto.setStopThreshold(stopThreshold);
    crypto.setProfitThreshold(profitThreshold);
    return save(crypto);
  }
}
