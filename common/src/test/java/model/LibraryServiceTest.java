package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LibraryServiceTest {

  @Test
  void getMyBook() {
    var myBook = LibraryService.getMyBook();
    assertEquals("My book", myBook.title());
  }
}
