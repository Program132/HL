# HytaleLoader

**HytaleLoader** is a Java library designed to simplify the development of mods for the Hytale server. It provides an abstraction layer inspired by the Bukkit/Spigot API, allowing developers to create mods using annotations for event handling and command registration.

## Features

-   **Annotation-based Event Handling**: Use `@EventHandler` to easily register event listeners without manually managing the `EventRegistry`.
-   **Annotation-based Command Registration**: Use `@Command` to define commands with metadata (names, aliases, permissions) directly on methods.
-   **SimplePlugin Base Class**: Extend `SimplePlugin` to automatically handle lifecycle events and component registration.
-   **Maven Support**: Easily integrate into your project using Maven.

## Installation

To use `HytaleLoader` in your project, you can build it locally and add it as a dependency.

### Building locally

```bash
git clone <repository-url>
cd HytaleLoader
mvn clean install
```

### Adding dependency

Add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>fr.hytale.loader</groupId>
    <artifactId>HytaleLoader</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Documentation

Comprehensive documentation is available in the `doc/` directory:

-   [Getting Started](doc/getting_started.md)
-   [Event System](doc/events.md)
-   [Standard Events](doc/standard_events.md)
-   [Command System](doc/commands.md)
-   [Server](doc/server.md)

## Example

```java
public class MyMod extends SimplePlugin {

    public MyMod(JavaPluginInit init) {
        super(init);
    }

    @Override
    public void onEnable() {
        getLogger().info("MyMod enabled!");
    }

    @EventHandler
    public void onBoot(BootEvent event) {
        getLogger().info("Server booted!");
    }

    @Command(name = "hello", description = "Says hello")
    public void onHello(CommandContext ctx) {
        ctx.sender().sendMessage(Message.raw("Hello World!"));
    }
}
```
