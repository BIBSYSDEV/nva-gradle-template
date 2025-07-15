package math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import model.LibraryService;
import org.junit.jupiter.api.Test;

class CalculatorTest {

  @Test
  void add() {
    assertEquals(4, Calculator.add(2, 2));
  }

  @Test
  void subtract() {
    assertEquals(4, Calculator.subtract(6, 2));
  }

  @Test
  void subtractPageCounts() {
    var book = LibraryService.getAnotherBook();
    var chapters = List.copyOf(book.chapters());
    var chapter1PageCount = chapters.get(0).pageCount();
    var chapter2PageCount = chapters.get(1).pageCount();
    assertEquals(4, Calculator.subtract(chapter1PageCount, chapter2PageCount));
  }

  @Test
  void testChapterCountMethodFromOtherProject() {
    var book = LibraryService.getAnotherBook();
    var chapterCount = book.getChapterCount();
    assertEquals(2, chapterCount);
  }
}
