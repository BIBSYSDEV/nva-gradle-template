package model;

import java.util.Collection;

public record Book(String title, Collection<Chapter> chapters) {

  public int getChapterCount() {
    return chapters.size();
  }
}
