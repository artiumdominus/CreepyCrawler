package dev.artiumdominus.creepycrawler.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HtmlRetriever {

    private final HttpClient client;

    public HtmlRetriever() {
        this.client = HttpClient.newHttpClient();
    }

    public String retrieve(URI uri) {
        try {
            return tryToRetrieve(uri);
        } catch (IOException | InterruptedException e) {
            throw new HtmlRetrieverException(e);
        }
    }

    private String tryToRetrieve(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static class HtmlRetrieverException extends RuntimeException {
        public HtmlRetrieverException(Throwable cause) {
            super(cause);
        }
    }
}
