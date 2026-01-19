package fr.hytale.loader.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Utility class for making HTTP requests.
 * <p>
 * Provides simple methods for GET and POST requests with support for headers
 * and timeouts.
 * </p>
 * 
 * <h3>Usage Examples:</h3>
 * 
 * <pre>{@code
 * // Synchronous GET request
 * WebRequest request = new WebRequest("https://example.com/data");
 * String response = request.get();
 * 
 * // Asynchronous GET request
 * request.getAsync().thenAccept(response -> {
 *     System.out.println("Response: " + response);
 * });
 * }</pre>
 * 
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.6
 */
public class WebRequest {

    private static final HttpClient DEFAULT_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private final String url;
    private final Map<String, String> headers;
    private Duration timeout;
    private HttpClient client;

    /**
     * Creates a new WebRequest for the specified URL.
     * 
     * @param url the target URL
     */
    public WebRequest(String url) {
        this.url = url;
        this.headers = new HashMap<>();
        this.timeout = Duration.ofSeconds(30);
        this.client = DEFAULT_CLIENT;
    }

    /**
     * Adds a header to the request.
     * 
     * @param name  the header name
     * @param value the header value
     * @return this WebRequest for chaining
     */
    public WebRequest header(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * Sets the request timeout in seconds.
     * 
     * @param seconds the timeout in seconds
     * @return this WebRequest for chaining
     */
    public WebRequest timeout(int seconds) {
        this.timeout = Duration.ofSeconds(seconds);
        return this;
    }

    /**
     * Sets a custom HttpClient.
     * 
     * @param client the HttpClient to use
     * @return this WebRequest for chaining
     */
    public WebRequest client(HttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * Performs a synchronous GET request.
     * 
     * @return the response body as a String
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    public String get() throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .GET();

        // Add headers
        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    /**
     * Performs an asynchronous GET request.
     * 
     * @return a CompletableFuture that will contain the response body
     */
    public CompletableFuture<String> getAsync() {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .GET();

        // Add headers
        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    /**
     * Performs a synchronous POST request with a body.
     * 
     * @param body the request body
     * @return the response body as a String
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    public String post(String body) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .POST(HttpRequest.BodyPublishers.ofString(body));

        // Add headers
        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    /**
     * Performs an asynchronous POST request with a body.
     * 
     * @param body the request body
     * @return a CompletableFuture that will contain the response body
     */
    public CompletableFuture<String> postAsync(String body) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .POST(HttpRequest.BodyPublishers.ofString(body));

        // Add headers
        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    /**
     * Performs a synchronous PUT request with a body.
     * 
     * @param body the request body
     * @return the response body as a String
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    public String put(String body) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .PUT(HttpRequest.BodyPublishers.ofString(body));

        // Add headers
        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    /**
     * Performs an asynchronous PUT request with a body.
     * 
     * @param body the request body
     * @return a CompletableFuture that will contain the response body
     */
    public CompletableFuture<String> putAsync(String body) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .PUT(HttpRequest.BodyPublishers.ofString(body));

        // Add headers
        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    /**
     * Performs a synchronous DELETE request.
     * 
     * @return the response body as a String
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    public String delete() throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .DELETE();

        // Add headers
        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    /**
     * Performs an asynchronous DELETE request.
     * 
     * @return a CompletableFuture that will contain the response body
     */
    public CompletableFuture<String> deleteAsync() {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .DELETE();

        // Add headers
        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    /**
     * Makes a GET request and returns the full HttpResponse.
     * 
     * @return the HttpResponse
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    public HttpResponse<String> getResponse() throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .GET();

        // Add headers
        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // Static helper methods

    /**
     * Makes a simple GET request to a URL.
     * 
     * @param url the URL to request
     * @return the response body
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    public static String simpleGet(String url) throws IOException, InterruptedException {
        return new WebRequest(url).get();
    }

    /**
     * Makes a simple POST request to a URL with JSON body.
     * 
     * @param url      the URL to request
     * @param jsonBody the JSON body
     * @return the response body
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    public static String simplePost(String url, String jsonBody) throws IOException, InterruptedException {
        return new WebRequest(url)
                .header("Content-Type", "application/json")
                .post(jsonBody);
    }
}
