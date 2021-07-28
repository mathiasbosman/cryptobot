package be.mathiasbosman.cryptobot.controllers;

import be.mathiasbosman.cryptobot.api.dto.CryptoEntityDto;
import be.mathiasbosman.cryptobot.persistency.entities.CryptoEntity;
import java.util.List;

public interface CryptoController {

  CryptoEntityDto getCrypto(String cryptoCode);

  List<CryptoEntityDto> getCryptos();

  CryptoEntityDto updateCrypto(String code, CryptoEntityDto cryptoEntityDto);

}
