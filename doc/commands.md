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

The method must accept a single `CommandContext` parameter.

```java
import fr.hytale.loader.command.Command;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.Message;

public class MyCommands {

    @Command(name = "greet", aliases = {"hi", "hello"}, description = "Greets the player")
    public void onGreet(CommandContext ctx) {
        ctx.sender().sendMessage(Message.raw("Greetings!"));
    }
    
    @Command(name = "admin", permission = "mymod.admin")
    public void onAdmin(CommandContext ctx) {
        ctx.sender().sendMessage(Message.raw("Hello Admin!"));
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
