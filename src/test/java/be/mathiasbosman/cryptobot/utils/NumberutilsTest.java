package be.mathiasbosman.cryptobot.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import org.junit.jupiter.api.Test;

class NumberutilsTest {

  @Test
  void format() {
    Locale.setDefault(Locale.UK);
    assertThat(Numberutils.format(1234.56789, 2)).isEqualTo("1,234.57");
    assertThat(Numberutils.format(1234.565, 2)).isEqualTo("1,234.57");
    assertThat(Numberutils.format(1234.564, 2)).isEqualTo("1,234.56");
  }
}