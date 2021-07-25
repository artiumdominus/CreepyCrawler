package dev.artiumdominus.creepycrawler.util;

import java.util.regex.*;

public class BodySearcher {
  
  private static final String regex = ">([^<>]+)<";
  private static final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

  public static boolean find(String html, String keyword) {

    if (html == null)
      throw new IllegalArgumentException("HTML cannot be null");

    if (keyword == null)
      throw new IllegalArgumentException("keyword cannot be null");

    var matcher = pattern.matcher(html);

    while (matcher.find()) {
      var text = matcher.group(1).toLowerCase();

      if (text.contains(keyword))
        return true;
    }

    return false;
  }
}