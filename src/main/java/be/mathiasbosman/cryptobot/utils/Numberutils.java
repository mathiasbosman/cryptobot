package be.mathiasbosman.cryptobot.utils;

import java.text.NumberFormat;

public abstract class Numberutils {

  public Numberutils() {
    // util class
  }

  public static String format(Number number, int decimals) {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(decimals);
    return nf.format(number);
  }
}
