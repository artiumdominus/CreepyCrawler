package dev.artiumdominus.creepycrawler.controllers;

import spark.Route;
import spark.Request;
import spark.Response;

import com.google.gson.Gson;

import dev.artiumdominus.creepycrawler.util.IdGenerator;
import dev.artiumdominus.creepycrawler.models.AnalysisModel;
import dev.artiumdominus.creepycrawler.models.AnalysisModel.Status;
import dev.artiumdominus.creepycrawler.models.CrawlModel;
import dev.artiumdominus.creepycrawler.models.CrawlResponseModel;
import dev.artiumdominus.creepycrawler.models.ErrorResponseModel;
import dev.artiumdominus.creepycrawler.repositories.AnalysisRepository;
import dev.artiumdominus.creepycrawler.workers.CrawlerWorker;

public class CrawlerController {

  private static final Gson gson = new Gson();

  public static Route list = (Request req, Response res) -> {
    return gson.toJson(AnalysisRepository.list());
  };

  public static Route post = (Request req, Response res) -> {
    CrawlModel crawl = gson.fromJson(req.body(), CrawlModel.class);

    if (crawl.keyword == null
        || crawl.keyword.length() < 4
        || crawl.keyword.length() > 32)
    {
      res.status(400);
      return gson.toJson(new ErrorResponseModel(400, "field 'keyword' is required (from 4 up to 32 chars)"));
    }

    var idGenerator = new IdGenerator();
    String id;

    boolean ok;
    do {
      id = idGenerator.generate();
      ok = AnalysisRepository.reserveSlot(id);
    } while (!ok);

    AnalysisModel analysis = new AnalysisModel(id);
    AnalysisRepository.put(id, analysis);

    new Thread(new CrawlerWorker(crawl.keyword, analysis)).start();

    return gson.toJson(new CrawlResponseModel(id));
  };

  public static Route get = (Request req, Response res) ->
  {
    var id = req.params("id");
    var analysis = AnalysisRepository.get(id);

    if (analysis != null) {
      return gson.toJson(analysis);
    } else {
      res.status(404);
      return gson.toJson(new ErrorResponseModel(404, "crawl not found: " + id));
    }
  };

  public static Route delete = (Request req, Response res) -> {
    var id = req.params("id");
    var analysis = AnalysisRepository.get(id);

    if (analysis != null) {
      if (analysis.status == Status.DONE) {
        AnalysisRepository.delete(id);
        return "ok";
      } else {
        res.status(400);
        return gson.toJson(new ErrorResponseModel(400, "crawl " + id + " is still active."));
      }
    } else {
      res.status(404);
      return gson.toJson(new ErrorResponseModel(404, "crawl not found: " + id));
    }
  };
}