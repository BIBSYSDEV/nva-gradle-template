/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MessageUtilsTest {
  @Test
  void testGetMessage() {
    assertEquals("Hello      World!", MessageUtils.getMessage());
  }
}
