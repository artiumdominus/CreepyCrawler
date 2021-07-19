package dev.artiumdominus.creepycrawler.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.LinkedList;

public class AnalysisModel {

  public enum Status {
    @SerializedName("active") ACTIVE,
    @SerializedName("done") DONE
  }

  public String id;
  public Status status;
  public List<String> urls;

  public AnalysisModel(String id) {
    this.id = id;
    this.status = Status.ACTIVE;
    this.urls = new LinkedList<String>();
  }
}