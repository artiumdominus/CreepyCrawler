package dev.artiumdominus.creepycrawler.models;

public class ErrorResponseModel {
  public int status;
  public String message;

  public ErrorResponseModel(int status, String message) {
    this.status = status;
    this.message = message;
  }
}