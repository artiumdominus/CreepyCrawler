package dev.artiumdominus.creepycrawler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class Config {

    private static final Set<String> SUPPORTED_URI_SCHEMES = Set.of("http", "https");
    private static final int DEFAULT_MAX_RESULTS = -1;

    public static URI baseUrl() {
        var value = System.getenv("BASE_URL");
        if (value != null) {
            return absoluteUriWithValidScheme(value);
        } else {
            throw new IllegalArgumentException("Missing required environment variable: BASE_URL");
        }
    }

    private static URI absoluteUriWithValidScheme(String value) {
        if (!value.endsWith("/")) {
            value += "/";
        }

        try {
            var result = new URI(value);
            if (!result.isAbsolute() || !SUPPORTED_URI_SCHEMES.contains(result.getScheme())) {
                throw new IllegalArgumentException("Invalid environment variable value: BASE_URL");
            }
            return result;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid environment variable value: BASE_URL");
        }
    }

    public static int maxResults() {
        try {
            var value = System.getenv("MAX_RESULTS");
            if (value != null) {
                var result = Integer.parseInt(value);
                if (result == DEFAULT_MAX_RESULTS || result > 0) {
                    return result;
                }
            }
            return DEFAULT_MAX_RESULTS;
        } catch (NumberFormatException e) {
            return DEFAULT_MAX_RESULTS;
        }
    }
}
