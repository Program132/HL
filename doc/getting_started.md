# Getting Started with HytaleLoader

This guide will walk you through setting up your first Hytale mod using `HytaleLoader`.

Make sure to read this article from Hytale: https://support.hytale.com/hc/en-us/articles/45326769420827-Hytale-Server-Manual#tips-tricks 

## Prerequisites

-   Java 25
-   Maven
-   `HytaleLoader` installed locally (run `mvn install` in the library directory)
-   `HytaleServer.jar`

## Project Setup

1.  **Create a Maven Project**: Create a new Maven project in your IDE.

2.  **Configure `pom.xml`**: Add dependencies for `HytaleLoader` and the `HytaleServer`.

    ```xml
    <dependencies>
        <!-- Hytale Server (System scope or local repo) -->
        <dependency>
            <groupId>com.hypixel.hytale</groupId>
            <artifactId>HytaleServer</artifactId>
            <version>1.0.0</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/../libs/HytaleServer.jar</systemPath>
        </dependency>

        <!-- HytaleLoader -->
        <dependency>
            <groupId>fr.hytale.loader</groupId>
            <artifactId>HytaleLoader</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
    ```

3.  **Shading**: Since `HytaleLoader` is not part of the standard Hytale server, you must include it in your mod JAR using the `maven-shade-plugin`.

    ```xml
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.5.1</version>
        <executions>
            <execution>
                <phase>package</phase>
                <goals><goal>shade</goal></goals>
            </execution>
        </executions>
    </plugin>
    ```

## Creating Your Mod

1.  **Extend `SimplePlugin`**: Create a class that extends `fr.hytale.loader.plugin.SimplePlugin`.
2.  **Implement Constructor**: You must provide a constructor matching `SimplePlugin(JavaPluginInit init)`.
3.  **Overrides**: Implement `onEnable()` and `onDisable()`.

```java
import fr.hytale.loader.plugin.SimplePlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class MyFirstMod extends SimplePlugin {

    public MyFirstMod(JavaPluginInit init) {
        super(init);
    }

    @Override
    public void onEnable() {
        getLogger().info("Mod enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Mod disabled.");
    }
}
```

## manifest.json

Create a `manifest.json` file in `src/main/resources`:

```json
{
  "Group": "com.yourname",
  "Name": "MyFirstMod",
  "Version": "1.0.0",
  "Main": "com.yourname.MyFirstMod",
  "Authors": [{"Name": "You"}]
}
```

## Building and Running

1.  Run `mvn package`.
2.  Copy the generated JAR (e.g., `target/MyFirstMod-1.0.0.jar`) to the `mods` folder of your Hytale server.
3.  Start the server!

## Run the server

Read [Server](doc/server.md) for more information.