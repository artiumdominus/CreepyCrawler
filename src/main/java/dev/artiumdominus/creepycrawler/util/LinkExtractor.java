package dev.artiumdominus.creepycrawler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.*;
import java.util.List;
import java.util.LinkedList;
import java.net.URI;

public class LinkExtractor {

  private static final Logger LOGGER = LoggerFactory.getLogger(LinkExtractor.class);

  private static final String regex = "href=\"(((https?|ftp|gopher|telnet|file):((//)|(\\\\))+)?[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)\"";
  private static final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

  public static List<URI> extract(String HTML, URI rootURI, URI actualURI) {

    if (HTML == null)
      throw new IllegalArgumentException("HTML cannot be null");

    if (rootURI == null)
      throw new IllegalArgumentException("rootURI cannot be null");

    List<URI> links = new LinkedList<URI>();

    var matcher = pattern.matcher(HTML);

    while (matcher.find()) {
      var href = matcher.group(1);

      // absolute
      if ((href.startsWith("http://") || href.startsWith("https://"))) {
        if (href.startsWith(rootURI.toString())) {
          try {
            links.add(new URI(href));
          } catch (Exception e) {
            LOGGER.error(e.getMessage());
            continue;
          }
        }
      }
      // relative
      else {
        URI nextURI;
        try {
          nextURI = actualURI.resolve(href);
        } catch (Exception e) {
          LOGGER.error(e.getMessage());
          continue;
        }

        if (nextURI.toString().startsWith(rootURI.toString()))
          links.add(nextURI);
      }
    }

    return links;
  }
}