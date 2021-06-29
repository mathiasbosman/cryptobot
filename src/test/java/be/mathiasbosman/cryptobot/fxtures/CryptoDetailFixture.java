package be.mathiasbosman.cryptobot.fxtures;

import be.mathiasbosman.cryptobot.api.configuration.BitvavoConfig.CryptoDetail;

public abstract class CryptoDetailFixture {

  public CryptoDetailFixture() {
    // ficture
  }

  public static CryptoDetail newCryptoDetail(boolean staking, double rebuyAt, double sellTreshold) {
    CryptoDetail cryptoDetail = new CryptoDetail();
    cryptoDetail.setHasStaking(staking);
    cryptoDetail.setRebuyAt(rebuyAt);
    cryptoDetail.setSellTreshold(sellTreshold);
    return cryptoDetail;
  }

  public static CryptoDetail newCryptoDetail(boolean staking) {
    return newCryptoDetail(staking, 0, 0);
  }
}
