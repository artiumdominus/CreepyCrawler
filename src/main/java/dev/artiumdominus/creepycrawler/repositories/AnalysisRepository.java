package dev.artiumdominus.creepycrawler.repositories;

import java.util.Map;
import java.util.HashMap;
import dev.artiumdominus.creepycrawler.models.AnalysisModel;

public class AnalysisRepository {

  private static final Map<String, AnalysisModel> repo = new HashMap<String, AnalysisModel>();
  private static final AnalysisModel reserveAnalisys = new AnalysisModel("null");

  public synchronized static boolean reserveSlot(String key) {
    if (repo.containsKey((key)))
      return false;

    repo.put(key, reserveAnalisys);
    return true;
  }

  public static void put(String key, AnalysisModel analysis) {
    repo.put(key, analysis);
  }

  public static AnalysisModel get(String key) {
    return repo.get(key);
  }

  public static boolean delete(String key) {
    return !(repo.remove(key) == null);
  }
}