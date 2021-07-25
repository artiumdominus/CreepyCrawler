package dev.artiumdominus.creepycrawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

import dev.artiumdominus.creepycrawler.controllers.*;

import java.net.URI;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    config();

    get("/crawls", CrawlerController.list);
    get("/crawls/:id", CrawlerController.get);
    post("/crawls", CrawlerController.post);
    delete("/crawls/:id", CrawlerController.delete);
            
    LOGGER.info("HTTP API initialized");
  }

  private static void config() {
    try {
      Options.BASE_URL = Config.baseUrl();
    } catch (IllegalArgumentException e) {
      LOGGER.warn(e.getMessage());
      System.exit(-1);
    }

    try {
      Options.MAX_RESULTS = Config.maxResults();
    } catch (IllegalArgumentException e) {
      LOGGER.warn(e.getMessage());
      System.exit(-1);
    }
  }
}
