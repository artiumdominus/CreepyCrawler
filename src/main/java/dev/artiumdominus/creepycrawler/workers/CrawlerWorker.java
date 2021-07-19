package dev.artiumdominus.creepycrawler.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.artiumdominus.creepycrawler.Options;
import dev.artiumdominus.creepycrawler.models.AnalysisModel;
import dev.artiumdominus.creepycrawler.models.AnalysisModel.Status;

import java.util.Set;
import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import java.util.regex.*;

import dev.artiumdominus.creepycrawler.util.HtmlRetriever;
import dev.artiumdominus.creepycrawler.util.HtmlRetriever.HtmlRetrieverException;
import dev.artiumdominus.creepycrawler.util.LinkExtractor;

public class CrawlerWorker implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerWorker.class);

  private String keyword;
  private AnalysisModel analysis;
  private URI rootURI;
  private int foundMatches;
  private Set<URI> exploredURIs;
  private Set<URI> foundURIs;

  public CrawlerWorker(String keyword, AnalysisModel analysis) {
    this.keyword = keyword;
    this.analysis = analysis;
    this.foundMatches = 0;
    this.rootURI = Options.BASE_URL;
    this.exploredURIs = new HashSet<URI>();
    this.foundURIs = new HashSet<URI>();
    foundURIs.add(Options.BASE_URL);
  }

  @Override
  public void run() {
    var retriever = new HtmlRetriever();
    var HTML = "";

    do {
      var uri = foundURIs.iterator().next();

      LOGGER.info("[" + this.analysis.id + "]\n\t> crawl! : " + uri.toString());

      try {
        HTML = retriever.retrieve(uri);
      } catch (HtmlRetrieverException e) {
        HTML = "";
        LOGGER.error(e.getMessage());
      }

      if (HTML.toLowerCase().contains(this.keyword.toLowerCase())) {
        this.analysis.urls.add(uri.toString());
        this.foundMatches++;
      }

      var links = LinkExtractor.extract(HTML, rootURI, uri);

      for (URI link : links)
        if (!(exploredURIs.contains(link) || foundURIs.contains(link)))
          foundURIs.add(link);
      
      foundURIs.remove(uri);  
      exploredURIs.add(uri);

    } while (foundMatches != Options.MAX_RESULTS && !foundURIs.isEmpty());

    analysis.status = Status.DONE;
  }
}