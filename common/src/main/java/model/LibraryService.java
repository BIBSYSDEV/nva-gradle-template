package model;

import java.util.List;

public class LibraryService {

  private LibraryService() {
    // private constructor to hide the implicit public one
  }

  public static Book getMyBook() {
    var chapter0 = new Chapter("Chapter 0", 42);
    return new Book("My book", List.of(chapter0));
  }

  public static Book getAnotherBook() {
    var chapter1 = new Chapter("Chapter 1", 4);
    var chapter2 = new Chapter("Chapter 2", 2);
    return new Book("Another book", List.of(chapter1, chapter2));
  }
}
