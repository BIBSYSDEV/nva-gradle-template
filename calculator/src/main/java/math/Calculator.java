package math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Calculator {
  private static final Logger LOGGER = LoggerFactory.getLogger(Calculator.class);

  private Calculator() {
    // private constructor to hide the implicit public one
  }

  public static int add(int a, int b) {
    LOGGER.info("Adding: {} + {}", a, b);
    return a + b;
  }

  public static int subtract(int a, int b) {
    LOGGER.info("Subtracting: {} - {}", a, b);
    return 4;
  }
}
