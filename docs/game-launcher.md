# Game Launcher Module Documentation

The `game-launcher` module serves as the entry point for games built with XNGIN. It aggregates all required engine modules and provides a simple way to start a game application. This module demonstrates how to integrate the various engine components into a cohesive application.

---

## Responsibilities

- **Engine Integration**: Combines all necessary engine modules into a single application.
- **Application Entry Point**: Provides the main method and bootstrapping code for games.
- **Configuration Loading**: Loads and applies engine and game configuration.
- **Example Implementation**: Serves as a reference for creating custom game applications.

---

## Key Concepts

### Application Lifecycle

The game launcher manages the application lifecycle:

1. **Initialisation**: Sets up the engine and loads configuration.
2. **Game Loop**: Starts the engine's main loop.
3. **Shutdown**: Handles graceful shutdown when the application exits.

### Module Integration

- Demonstrates how to integrate various engine modules (rendering, audio, physics, etc.).
- Shows proper initialisation order and dependency management.

---

## Example Usage

```java
import com.xngin.GameLauncher;

public class MyGameLauncher {
    public static void main(String[] args) {
        GameLauncher launcher = new GameLauncher();
        launcher.launch(args);
    }
}
```

This example shows how to create a custom launcher that extends the base functionality.

---

## Customising the Launcher

To create your own game launcher:

1. Create a new module for your game.
2. Depend on the necessary engine modules.
3. Create a main class that initialises the engine with your game-specific configuration.
4. Add your game logic, scenes, and assets.

---

## Further Reading

- [Quick Start Guide](quickstart.md)
- [Architecture Guide](architecture.md)
- [Engine Core Documentation](engine-core.md)
- [Module Documentation](.)

For questions or suggestions, please open an issue or discussion on the project's repository.