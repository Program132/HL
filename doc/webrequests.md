# 🌐 WebRequest API Documentation

The `WebRequest` utility class simplifies making HTTP requests (GET, POST, PUT, DELETE) within your Hytale mod. It supports both synchronous (blocking) and asynchronous (non-blocking) operations.

### 1. Asynchronous GET Request (Recommended)
Use this method to fetch data without fetching blocking the main server thread.

```java
new WebRequest("https://example.com")
    .header("User-Agent", "HytaleLoader") // Optional headers
    .timeout(10)                          // Timeout in seconds (default: 30)
    .getAsync()
    .thenAccept(response -> {
        // Runs when response is received
        System.out.println("Response: " + response);
    })
    .exceptionally(ex -> {
        System.err.println("Error: " + ex.getMessage());
        return null;
    });
```

### 2. POST Request with JSON body
Sending data to an external API (e.g., logging, stats, interaction).

```java
String jsonBody = "{\"username\": \"Player1\", \"score\": 100}";

new WebRequest("https://blazzeaezeazlehazjehzajieheajzhehzezahje.com/submit") // (it's a random URL)
    .header("Content-Type", "application/json")
    .postAsync(jsonBody)
    .thenAccept(response -> System.out.println("Data saved!"));
```

### 3. PUT Request
Updating an existing resource asynchronously.

```java
String updateData = "{\"status\": \"active\"}";

new WebRequest("https://my-api.com/update/123")
    .header("Authorization", "Bearer my-token")
    .putAsync(updateData)
    .thenAccept(response -> System.out.println("Updated!"));
```

### 4. DELETE Request
Deleting a resource asynchronously.

```java
new WebRequest("https://my-api.com/users/42")
    .header("Authorization", "Bearer my-token")
    .deleteAsync()
    .thenAccept(response -> System.out.println("Deleted!"));
```

## Features
- **Methods:** `get()`, `post()`, `put()`, `delete()`
- **Async:** `getAsync()`, `postAsync()` return `CompletableFuture<String>`
- **Headers:** Chainable `.header("Key", "Value")`
- **Timeouts:** Customizable `.timeout(int seconds)`
