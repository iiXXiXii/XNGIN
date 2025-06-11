# Quick Start Guide

Welcome to XNGIN! This comprehensive guide will help you get up and running with the engine efficiently, whether you're a developer new to game engines or an experienced contributor looking to explore XNGIN's capabilities.

---

## Prerequisites & System Requirements

**Essential Requirements:**

- **Java 24 or later**: Ensure the latest Java version is installed and available in your system PATH
- **Gradle 8.0+**: The project includes a Gradle wrapper, so separate installation isn't necessary
- **Modern IDE**: IntelliJ IDEA is highly recommended, though any contemporary Java IDE will suffice

**Recommended System Specifications:**

- 8GB+ RAM for comfortable development experience
- OpenGL 4.5+ or Vulkan 1.2+ compatible graphics card
- Multi-core processor for optimal build performance
- Solid-state drive for faster asset loading during development

**Supported Platforms:**

- Windows 10/11 (x64)
- macOS 10.15+ (Intel and Apple Silicon)
- Linux distributions with glibc 2.17+ (x64)

---

## Setting Up Your Development Environment

### 1. Cloning the Repository

Begin by cloning the repository to your local development environment:

```bash
git clone https://github.com/iiXXiXii/XNGIN.git
cd XNGIN
```

### 2. Initial Build & Verification

Build all modules and verify everything functions correctly:

```bash
./gradlew build
```

This command will:

- Download all required dependencies automatically
- Compile all engine modules in the correct order
- Execute the complete test suite across all modules
- Generate necessary build artefacts and documentation
- Validate cross-module compatibility

**Initial build may take several minutes** as Gradle downloads dependencies and compiles the entire engine.

### 3. IDE Configuration

**For IntelliJ IDEA:**

1. Open IntelliJ IDEA and select "Open or Import"
2. Navigate to the XNGIN directory and select it
3. IntelliJ will automatically detect the Gradle project structure
4. Wait for indexing to complete and Gradle sync to finish
5. Verify all modules appear in the project tree

**For Eclipse:**

1. Install the Gradle (Buildship) plugin if not already present
2. File → Import → Gradle → Existing Gradle Project
3. Select the XNGIN root directory
4. Allow Eclipse to import and configure the project structure

---

## Running the Example Launcher

Test the engine installation by running the demonstration launcher:

```bash
./gradlew :game-launcher:run
```

**What to expect:**

- A window should open displaying the XNGIN demo scene
- Basic rendering, input handling, and physics simulation
- Console output showing engine initialisation and module loading
- Smooth frame rate with basic performance metrics

If you encounter issues, check the [troubleshooting section](#troubleshooting) below.

---

## Creating Your First Game

### Step 1: Create a New Game Module

Create a new directory for your game within the XNGIN project:

```bash
mkdir my-awesome-game
cd my-awesome-game
mkdir -p src/main/java/com/mygame
```

### Step 2: Configure the Build Script

Create `build.gradle.kts` in your game module:

```kotlin
plugins {
    id("java")
    id("application")
}

group = "com.mygame"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Core engine modules
    implementation(project(":engine-core"))
    implementation(project(":engine-platform-lwjgl"))
    implementation(project(":engine-renderer-gl"))
    implementation(project(":engine-math"))
    implementation(project(":engine-scene"))
    implementation(project(":engine-input"))

    // Optional modules (add as needed)
    implementation(project(":engine-physics-dyn4j"))
    implementation(project(":engine-audio-openal"))
    implementation(project(":engine-assets"))

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("com.mygame.MyGameLauncher")
}

tasks.test {
    useJUnitPlatform()
}
```

### Step 3: Register Your Module

Add your module to the main `settings.gradle.kts`:

```kotlin
rootProject.name = "xngin"

include(
    // ...existing modules...
    "my-awesome-game"
)
```

### Step 4: Implement Your Game

Create your main game class at `src/main/java/com/mygame/MyGameLauncher.java`:

```java
package com.mygame;

import com.xngin.Engine;
import com.xngin.EngineConfig;
import com.xngin.Game;
import com.xngin.scene.Scene;
import com.xngin.scene.Entity;
import com.xngin.scene.Component;

public class MyGameLauncher extends Game {

    public static void main(String[] args) {
        // Configure the engine
        EngineConfig config = new EngineConfig.Builder()
            .windowTitle("My Awesome Game")
            .windowSize(1280, 720)
            .enableVSync(true)
            .build();

        // Create and start the engine
        Engine engine = new Engine(config);
        engine.run(new MyGameLauncher());
    }

    @Override
    public void initialise() {
        // Create your game scene
        Scene mainScene = new Scene("MainScene");

        // Create a player entity
        Entity player = mainScene.createEntity("Player");

        // Add components to define behaviour
        // player.addComponent(new TransformComponent());
        // player.addComponent(new RenderComponent());
        // player.addComponent(new PlayerControllerComponent());

        // Set as the active scene
        getSceneManager().setActiveScene(mainScene);

        System.out.println("Game initialised successfully!");
    }

    @Override
    public void update(float deltaTime) {
        // Update game logic here
        // This method is called every frame
    }

    @Override
    public void shutdown() {
        // Clean up resources
        System.out.println("Game shutting down gracefully.");
    }
}
```

### Step 5: Build and Run Your Game

Build your game module:

```bash
./gradlew :my-awesome-game:build
```

Run your game:

```bash
./gradlew :my-awesome-game:run
```

---

## Development Workflow

### Hot Reloading During Development

Enable continuous building for rapid iteration:

```bash
./gradlew build --continuous
```

This monitors file changes and automatically rebuilds affected modules.

### Running Tests

Execute tests for your specific module:

```bash
./gradlew :my-awesome-game:test
```

Run all engine tests:

```bash
./gradlew test
```

### Working with Assets

1. Create an `assets/` directory in your game module
2. Place textures, models, audio files, and other resources there
3. Use the Asset Manager to load resources:

```java
AssetManager assets = ServiceLocator.get(AssetManager.class);
Texture playerTexture = assets.load("textures/player.png", Texture.class);
```

### Debugging

**Console Logging:**

```java
import com.xngin.Logger;

Logger.info("Game state: {}", currentState);
Logger.debug("Player position: {}", player.getPosition());
Logger.warn("Low health warning!");
Logger.error("Failed to load asset: {}", assetPath);
```

**Performance Profiling:**

- Use the built-in performance monitor: `PerformanceMonitor.enable()`
- Monitor frame times, memory usage, and module performance
- View detailed metrics in the console or debug overlay

---

## Troubleshooting

### Common Issues

**Build fails with "Could not find Java 24":**

- Verify Java 24+ is installed: `java --version`
- Update JAVA_HOME environment variable
- Restart your IDE after Java installation

**Window doesn't appear when running the launcher:**

- Check graphics drivers are up to date
- Verify OpenGL support: `glxinfo | grep OpenGL` (Linux)
- Try running with software rendering: `-Djava.awt.headless=false`

**Performance issues during development:**

- Increase JVM heap size: `-Xmx4G`
- Enable parallel garbage collection: `-XX:+UseG1GC`
- Use SSD storage for project files

**Module dependency errors:**

- Clean and rebuild: `./gradlew clean build`
- Refresh IDE project: reimport Gradle project
- Check module inclusion in `settings.gradle.kts`

### Getting Help

**Documentation Resources:**

- [Architecture Guide](architecture.md) - Understand engine structure
- [Module Documentation](.) - Detailed API references
- [Contributing Guide](CONTRIBUTING.md) - Development practices

**Community Support:**

- GitHub Issues - Report bugs and request features
- GitHub Discussions - Ask questions and share experiences
- Project Wiki - Additional tutorials and examples

---

## Next Steps

### Explore Advanced Features

1. **Entity-Component-System**: Build complex game objects using the flexible ECS architecture
2. **Physics Integration**: Add realistic physics simulation to your game
3. **Audio Systems**: Implement immersive 3D positional audio
4. **Custom Shaders**: Create stunning visual effects with custom OpenGL/Vulkan shaders
5. **Asset Pipeline**: Optimise loading and processing of game assets

### Example Projects

Study these example implementations:

- **Platformer Demo**: 2D platformer with physics and animations
- **3D Scene Explorer**: First-person 3D environment navigation
- **Audio Showcase**: Spatial audio and environmental effects
- **Physics Playground**: Interactive physics simulation

### Contributing to XNGIN

Consider contributing to the engine itself:

1. **Fix Bugs**: Help improve engine stability
2. **Add Features**: Implement new capabilities
3. **Improve Documentation**: Enhance guides and examples
4. **Optimise Performance**: Profile and optimise engine subsystems

For detailed contribution guidelines, see [CONTRIBUTING.md](CONTRIBUTING.md).

---

**Happy coding! Welcome to the XNGIN community!**

_If you build something amazing with XNGIN, we'd love to hear about it. Share your projects in the GitHub discussions or tag us on social media._
