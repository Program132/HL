# Command System

HytaleLoader simplifies command creation using the `@Command` annotation.

## The `@Command` Annotation

Annotate a method to turn it into a command executor.

| Attribute | Description | Required |
|-----------|-------------|----------|
| `name` | The primary name of the command. | Yes |
| `description` | A brief description of what the command does. | No (default empty) |
| `aliases` | Alternative names for the command. | No (default empty) |
| `permission` | The permission node required to run the command. | No |
| `requiresConfirmation` | Whether the command requires confirmation before executing. | No (default false) |

### usage

The method must accept `CommandContext` as the first parameter. You can then add parameters annotated with `@Arg` to automatically define and parse arguments.

**Supported Argument Types:**
* `String`
* `int` / `Integer`
* `float` / `Float`
* `boolean` / `Boolean`
* `double` / `Double` (mapped to Float)

**Example:**
```java
import fr.hytale.loader.command.Command;
import fr.hytale.loader.command.Arg;
import com.hypixel.hytale.server.core.command.system.CommandContext;

public class MyCommands {

@Command(name = "test", description = "Hello World")
public void onGreet(CommandContext ctx) {
    ctx.sender().sendMessage("Hello World!");
}

@Command(name = "greet", description = "Greets a target")
public void onGreet(CommandContext ctx, 
                    @Arg(name = "target", description = "Who to greet") String target) {
    ctx.sender().sendMessage("Hello " + target + "!");
}

@Command(name = "giveitem", description = "Give item with count")
public void onGive(CommandContext ctx,
                   @Arg(name = "item", description = "Item name") String item,
                   @Arg(name = "count", description = "Amount", optional = true) int count) {
    // 'count' will be 0 if not provided (default for int)
    int finalCount = count > 0 ? count : 1; 
        ctx.sender().sendMessage("Giving " + finalCount + "x " + item);
    }
}
```

## Registering Commands

### In Main Class
If your command methods are in your main class, they are automatically registered!

### In Separate Classes
Register separate command containers in `onEnable`:

```java
@Override
public void onEnable() {
    registerCommand(new MyCommands());
}
```
