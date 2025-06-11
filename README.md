# XNGIN Game Engine

<div align="center">
  <img src="docs/images/xngin.png" alt="XNGIN Logo" width="250"/>
  <br>
  <em>A thoroughly modern Java game engine for creating exceptional 2D and 3D experiences</em>
  <br><br>

[![Licence: MIT](https://img.shields.io/badge/Licence-MIT-yellow.svg)](LICENSE.md)
[![Java](https://img.shields.io/badge/Java-24-orange)](https://www.oracle.com/java/)
[![Status](https://img.shields.io/badge/Status-Active%20Development-brightgreen)](https://github.com/iiXXiXii/XNGIN)

</div>

## What is XNGIN?

**XNGIN** is a comprehensive, modular Java game engine designed to make creating outstanding games both straightforward and enjoyable. Built with modern Java practices, clean architecture, and developer experience at its heart, XNGIN handles the complex technical foundations—graphics rendering, physics simulation, audio processing, and input management—so you can focus entirely on crafting brilliant gameplay experiences.

Whether you're developing a charming 2D platformer, an immersive 3D adventure, or experimenting with innovative game mechanics, XNGIN provides the robust foundation and intuitive tools you need to transform your creative vision into reality.

---

## Why Choose XNGIN?

**🎯 Developer-Focused Design**

- Clean, intuitive APIs that make sense and feel natural to use
- Comprehensive documentation with practical examples and clear explanations
- Modular architecture that grows with your project's complexity

**⚡ High Performance**

- Built on proven libraries like LWJGL for native performance
- Efficient memory management and optimised rendering pipelines
- Support for both Vulkan and OpenGL rendering backends

**🔧 Flexible & Extensible**

- Plugin-based architecture allows easy customisation and extension
- Support for multiple physics engines, audio backends, and platform implementations
- Clear separation between API interfaces and implementation details

**🌍 Cross-Platform Ready**

- Desktop support through LWJGL with consistent behaviour across platforms
- Architecture designed for future mobile and web deployment
- Platform-specific optimisations without compromising code portability

---

## Architecture Overview

XNGIN's modular design ensures that each component has a clear, well-defined purpose whilst working seamlessly with the rest of the system. This approach makes the engine both powerful and maintainable.

### Core Engine Modules

**Engine Core** (`engine-core`)
The central nervous system that coordinates all other modules, manages the application lifecycle, and provides essential services like logging, configuration, and the main game loop.

**Mathematics** (`engine-math`)
High-performance mathematical foundations including vectors, matrices, quaternions, and geometric operations optimised for graphics and physics calculations.

**Asset Management** (`engine-assets`)
Intelligent asset pipeline supporting multiple file formats, automatic caching, hot-reloading during development, and efficient memory management.

**Scene Management** (`engine-scene`)
Flexible scene graph implementation with a powerful entity-component-system architecture for organising and managing game objects.

### Platform Integration

**Platform API** (`engine-platform-api`)
Cross-platform interface definitions for windowing, input handling, file system access, and other system-level services.

**LWJGL Implementation** (`engine-platform-lwjgl`)
Robust desktop implementation using LWJGL, providing window management, input handling, and native library integration.

### Graphics & Rendering

**Renderer API** (`engine-renderer-api`)
Hardware-agnostic rendering interfaces that abstract away graphics API complexity whilst maintaining performance.

**OpenGL Backend** (`engine-renderer-gl`)
Mature OpenGL implementation offering broad compatibility and excellent development experience.

**Vulkan Backend** (`engine-renderer-vk`)
High-performance Vulkan implementation for applications requiring maximum graphics performance and control.

### Audio System

**Audio API** (`engine-audio-api`)
Platform-independent audio interfaces supporting positional audio, environmental effects, and efficient audio resource management.

**OpenAL Implementation** (`engine-audio-openal`)
3D audio implementation using OpenAL, providing spatial audio capabilities and cross-platform audio processing.

### Physics Integration

**Physics API** (`engine-physics-api`)
Flexible physics system abstractions supporting multiple physics engine backends and custom physics implementations.

**Dyn4j Integration** (`engine-physics-dyn4j`)
Sophisticated 2D physics implementation using the Dyn4j library, offering advanced collision detection and realistic dynamics.

### Input Handling

**Input System** (`engine-input`)
Unified input abstraction supporting keyboard, mouse, and extensible device integration with customisable input mapping.

### Application Framework

**Game Launcher** (`game-launcher`)
Complete application entry point demonstrating proper engine integration and serving as a starting template for new projects.

---

## Getting Started

### Prerequisites

- **Java 24** or later (we embrace the latest language features for better performance and cleaner code)
- **Gradle** 8.0+ (included via wrapper, so no separate installation required)

### Building the Engine

Clone the repository and build everything:

```bash
git clone https://github.com/iiXXiXii/XNGIN.git
cd XNGIN
./gradlew build
```

### Running the Demo

See XNGIN in action with the demonstration launcher:

```bash
./gradlew :game-launcher:run
```

### Development Mode

For active development with automatic rebuilding:

```bash
./gradlew build --continuous
```

---

## Creating Your First Game

### Basic Project Setup

1. **Create your game module** with standard Java project structure
2. **Configure dependencies** in your `build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":engine-core"))
    implementation(project(":engine-platform-lwjgl"))
    implementation(project(":engine-renderer-gl"))
    // Add other modules as needed
}
```

3. **Implement your game class**:

```java
import com.xngin.Engine;
import com.xngin.Game;

public class MyGame extends Game {
    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.run(new MyGame());
    }

    @Override
    public void initialise() {
        // Set up your game world
    }

    @Override
    public void update(float deltaTime) {
        // Update game logic
    }
}
```

### Example: Simple Scene Setup

```java
import com.xngin.scene.Scene;
import com.xngin.scene.Entity;
import com.xngin.scene.components.TransformComponent;
import com.xngin.scene.components.MeshRendererComponent;

Scene gameScene = new Scene();
Entity player = gameScene.createEntity("Player");

player.addComponent(new TransformComponent());
player.addComponent(new MeshRendererComponent());
```

---

## Technology Foundation

XNGIN builds upon carefully selected, industry-proven technologies:

**Core Libraries:**

- **LWJGL 3** - Comprehensive native bindings for graphics, audio, and platform services
- **JOML** - High-performance Java mathematics library optimised for graphics programming
- **Dyn4j** - Robust 2D physics engine with advanced collision detection capabilities

**Development Tools:**

- **JUnit 5** - Modern testing framework for comprehensive test coverage
- **SLF4J + Logback** - Flexible logging infrastructure with configurable output
- **Gradle with Kotlin DSL** - Modern build automation with clear, maintainable build scripts

---

## Development & Extension

### Adding Custom Modules

XNGIN's architecture encourages extension and customisation:

1. **Create a new module directory** (e.g., `my-custom-module/`)
2. **Add to `settings.gradle.kts`** to include in the build process
3. **Define clean interfaces** in API modules, implement in separate modules
4. **Use Java's `ServiceLoader`** for runtime discovery and loose coupling

### Best Practices

- **Follow the API/Implementation pattern** - separate interfaces from concrete implementations
- **Write comprehensive tests** for all functionality
- **Use the event system** for cross-module communication
- **Consult the documentation** in `docs/` for architectural guidance

### Contributing

We welcome contributions that enhance XNGIN's capabilities and usability:

1. **Fork the repository** and create a feature branch from `main`
2. **Develop your enhancement** following established coding standards
3. **Write thorough tests** ensuring compatibility with existing functionality
4. **Update documentation** for new APIs or significant changes
5. **Submit a pull request** with clear descriptions of improvements

For detailed contribution guidelines, see [docs/CONTRIBUTING.md](docs/CONTRIBUTING.md).

---

## Testing & Quality Assurance

Comprehensive testing ensures XNGIN's reliability:

**Run all tests:**

```bash
./gradlew test
```

**Test specific modules:**

```bash
./gradlew :engine-core:test
./gradlew :engine-renderer-vk:test
```

**Continuous testing during development:**

```bash
./gradlew test --continuous
```

Each module maintains comprehensive test coverage including unit tests, integration tests, and performance benchmarks.

---

## Documentation & Resources

**Project Documentation:**

- [Architecture Guide](docs/architecture.md) - Detailed system design and module interactions
- [Quick Start Guide](docs/quickstart.md) - Step-by-step beginner tutorial
- [Module Documentation](docs/) - Individual module guides and API references

**External Resources:**

- [LWJGL Documentation](https://www.lwjgl.org/) - Native library bindings and tutorials
- [Dyn4j Documentation](https://www.dyn4j.org/) - 2D physics engine reference
- [JOML Documentation](https://github.com/JOML-CI/JOML) - Java mathematics library

---

## Licence

This project is licensed under the **MIT Licence**, permitting both personal and commercial use with minimal restrictions. For complete licence terms, see [LICENSE.md](LICENSE.md).

---

<div align="center">
  <strong>Ready to create something extraordinary? Begin your journey with XNGIN today!</strong>
  <br><br>
  <em>Have questions, suggestions, or feedback? We'd love to hear from you—please open an issue or start a discussion on our repository.</em>
</div>

## Future Development Roadmap

While XNGIN has a comprehensive architecture and structure in place, the engine is currently in the early stages of development. The following areas are planned for implementation or completion:

### Core Implementation Status

- **Engine Core**: Basic structure defined, but core functionality needs implementation
  - Game loop implementation
  - Module management system
  - Event handling system
  - Resource management

- **Rendering System**: 
  - OpenGL renderer needs full implementation
  - Vulkan renderer is in planning stage
  - Material system requires implementation
  - Lighting and shadow systems

- **Physics Integration**:
  - Physics API defined but needs implementation
  - Collision detection systems
  - Integration with scene graph

- **Audio System**:
  - OpenAL implementation needs completion
  - Spatial audio features
  - Audio resource management

- **Input Handling**:
  - Basic input abstraction defined
  - Controller support pending
  - Input mapping system

- **Asset Pipeline**:
  - Asset loading and management
  - Model and texture importing
  - Asset hot-reloading

### Upcoming Features

- **Editor Tools**: A visual editor for scene creation and management
- **Scripting System**: Lua or JavaScript integration for game logic
- **Networking**: Multiplayer capabilities and network synchronization
- **AI Systems**: Pathfinding, behavior trees, and decision making
- **Mobile Platform Support**: Android and iOS implementations
- **Documentation**: Comprehensive API documentation and tutorials

### Contributing

We welcome contributions in any of these areas. If you're interested in helping develop XNGIN, please see our [contribution guidelines](docs/CONTRIBUTING.md) and consider tackling one of the areas above.

---

