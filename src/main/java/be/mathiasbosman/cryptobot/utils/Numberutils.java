package be.mathiasbosman.cryptobot.utils;

import java.text.NumberFormat;

public abstract class Numberutils {

  public Numberutils() {
    // util class
  }

  /**
   * Short way for formatting numbers with decimals into string
   *
   * @param number   The number to format
   * @param decimals The amount of decimals
   * @return the formatted number as string
   */
  public static String format(Number number, int decimals) {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(decimals);
    return nf.format(number);
  }
}
