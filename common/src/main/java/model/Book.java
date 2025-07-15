package model;

import java.util.List;

public record Book(String title, List<Chapter> chapters) {

  public int getChapterCount() {
    return chapters.size();
  }
}
