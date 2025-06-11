# XNGIN Documentation Index

Welcome to the comprehensive documentation for XNGIN, a modern Java game engine designed for creating exceptional 2D and 3D gaming experiences. This index provides organised access to all documentation, guides, and resources available in the project.

---

## Getting Started

### Essential Reading

- **[README](../README.md)** - Project overview, features, and quick introduction
- **[Quick Start Guide](quickstart.md)** - Step-by-step tutorial for setting up and creating your first game
- **[Architecture Guide](architecture.md)** - Comprehensive system design and module interactions
- **[Contributing Guidelines](CONTRIBUTING.md)** - Development practices, standards, and submission process

### Installation & Setup

1. Read the [prerequisites](quickstart.md#prerequisites--system-requirements) to ensure your system is ready
2. Follow the [development environment setup](quickstart.md#setting-up-your-development-environment) guide
3. Try the [example launcher](quickstart.md#running-the-example-launcher) to verify installation
4. Create your [first game module](quickstart.md#creating-your-first-game) to start developing

---

## Core Engine Modules

### Foundation Modules

- **[Engine Core](engine-core.md)** - Central engine coordination, lifecycle management, and service orchestration
- **[Engine Math](engine-math.md)** - Mathematical foundations, vectors, matrices, and geometric operations
- **[Engine Assets](engine-assets.md)** - Asset loading, caching, hot-reloading, and resource management

### Scene & Entities

- **[Engine Scene](engine-scene.md)** - Entity-component-system architecture and scene graph management

### Platform Integration

- **[Engine Platform API](engine-platform-api.md)** - Cross-platform interface definitions for system services
- **[Engine Platform LWJGL](engine-platform-lwjgl.md)** - Desktop implementation using LWJGL for Windows, macOS, and Linux

### Graphics & Rendering

- **[Engine Renderer API](engine-renderer-api.md)** - Hardware-agnostic rendering interfaces and abstractions
- **[Engine Renderer GL](engine-renderer-gl.md)** - OpenGL implementation for broad compatibility
- **[Engine Renderer VK](engine-renderer-vk.md)** - Vulkan implementation for high-performance graphics

### Audio System

- **[Engine Audio API](engine-audio-api.md)** - Platform-independent audio interfaces and effects
- **[Engine Audio OpenAL](engine-audio-openal.md)** - 3D spatial audio implementation using OpenAL

### Physics Integration

- **[Engine Physics API](engine-physics-api.md)** - Abstract physics interfaces supporting multiple backends
- **[Engine Physics Dyn4j](engine-physics-dyn4j.md)** - 2D physics implementation with advanced collision detection

### Input Handling

- **[Engine Input](engine-input.md)** - Unified input management for keyboard, mouse, and controllers

### Application Framework

- **[Game Launcher](game-launcher.md)** - Application entry point and integration examples

---

## Documentation Categories

### By Audience

**For New Developers:**

1. [README](../README.md) - What is XNGIN and why use it?
2. [Quick Start Guide](quickstart.md) - Hands-on tutorial to get started
3. [Engine Core](engine-core.md) - Understanding the engine foundation
4. [Engine Scene](engine-scene.md) - Creating game objects and worlds

**For Engine Contributors:**

1. [Contributing Guidelines](CONTRIBUTING.md) - Development standards and processes
2. [Architecture Guide](architecture.md) - Deep system design knowledge
3. Individual module documentation for specific areas of contribution

**For Game Developers:**

1. [Quick Start Guide](quickstart.md) - Setup and first project
2. Module-specific guides based on your game's requirements
3. [Engine Scene](engine-scene.md) - Essential for all game development

**For Technical Architects:**

1. [Architecture Guide](architecture.md) - System design and module interactions
2. [Engine Core](engine-core.md) - Central coordination mechanisms
3. API documentation for extension and customisation

### By Development Phase

**Project Setup:**

- [Quick Start Guide](quickstart.md#setting-up-your-development-environment)
- [Game Launcher](game-launcher.md) - Application structure
- [Contributing Guidelines](CONTRIBUTING.md#development-environment) - IDE configuration

**Core Development:**

- [Engine Scene](engine-scene.md) - Creating game entities and components
- [Engine Assets](engine-assets.md) - Loading and managing game resources
- [Engine Input](engine-input.md) - Handling player interaction

**Graphics & Rendering:**

- [Engine Renderer API](engine-renderer-api.md) - Understanding rendering abstractions
- [Engine Renderer GL](engine-renderer-gl.md) or [Engine Renderer VK](engine-renderer-vk.md) - Implementation specifics
- [Engine Math](engine-math.md) - Mathematical operations for graphics

**Audio Integration:**

- [Engine Audio API](engine-audio-api.md) - Audio system overview
- [Engine Audio OpenAL](engine-audio-openal.md) - 3D spatial audio implementation

**Physics Simulation:**

- [Engine Physics API](engine-physics-api.md) - Physics system abstractions
- [Engine Physics Dyn4j](engine-physics-dyn4j.md) - 2D physics implementation

**Platform Deployment:**

- [Engine Platform API](engine-platform-api.md) - Platform abstraction concepts
- [Engine Platform LWJGL](engine-platform-lwjgl.md) - Desktop deployment specifics

**Performance & Optimisation:**

- [Architecture Guide](architecture.md#performance-considerations) - System-level optimisation strategies
- [Engine Core](engine-core.md#performance-optimisation) - Central performance utilities
- [Engine Scene](engine-scene.md#performance-optimisation) - Entity and component optimisation

### By Module Type

**API Modules** (Define interfaces and contracts):

- [Engine Platform API](engine-platform-api.md)
- [Engine Renderer API](engine-renderer-api.md)
- [Engine Audio API](engine-audio-api.md)
- [Engine Physics API](engine-physics-api.md)

**Implementation Modules** (Concrete implementations):

- [Engine Platform LWJGL](engine-platform-lwjgl.md)
- [Engine Renderer GL](engine-renderer-gl.md)
- [Engine Renderer VK](engine-renderer-vk.md)
- [Engine Audio OpenAL](engine-audio-openal.md)
- [Engine Physics Dyn4j](engine-physics-dyn4j.md)

**Core Modules** (Essential engine functionality):

- [Engine Core](engine-core.md)
- [Engine Math](engine-math.md)
- [Engine Assets](engine-assets.md)
- [Engine Scene](engine-scene.md)
- [Engine Input](engine-input.md)

**Application Modules** (Entry points and integration):

- [Game Launcher](game-launcher.md)

---

## Quick Reference

### Common Tasks

**Creating a New Game:**

```bash
# 1. Set up development environment
git clone https://github.com/iiXXiXii/XNGIN.git
cd XNGIN
./gradlew build

# 2. Create game module (see Quick Start Guide)
mkdir my-game
# ... follow detailed steps in quickstart.md
```

**Adding Engine Dependencies:**

```kotlin
dependencies {
    implementation(project(":engine-core"))
    implementation(project(":engine-platform-lwjgl"))
    implementation(project(":engine-renderer-gl"))
    // Add other modules as needed
}
```

**Basic Engine Initialisation:**

```java
public class MyGame extends Game {
    public static void main(String[] args) {
        EngineConfig config = new EngineConfig.Builder()
            .windowTitle("My Game")
            .windowSize(1280, 720)
            .build();
        Engine engine = new Engine(config);
        engine.run(new MyGame());
    }
}
```

### Key Concepts Summary

**Entity-Component-System:**

- **Entities**: Containers for components (game objects)
- **Components**: Data structures defining properties
- **Systems**: Logic processors operating on components

**Module Architecture:**

- **API modules**: Define interfaces and contracts
- **Implementation modules**: Provide concrete functionality
- **Service Locator**: Central registry for module services
- **Event System**: Type-safe communication between modules

**Asset Management:**

- **Loading**: Automatic format detection and processing
- **Caching**: Intelligent memory management
- **Hot-reloading**: Live updates during development

### Troubleshooting Quick Links

**Build Issues:**

- [Quick Start Guide - Troubleshooting](quickstart.md#troubleshooting)
- [Contributing Guidelines - Development Environment](CONTRIBUTING.md#development-environment)

**Runtime Issues:**

- [Engine Core - Debugging and Development Tools](engine-core.md#debugging-and-development-tools)
- [Architecture Guide - Performance Considerations](architecture.md#performance-considerations)

**Integration Problems:**

- [Architecture Guide - Module Interaction](architecture.md#module-interaction-and-communication)
- [Engine Core - Service Locator Pattern](engine-core.md#service-locator-pattern)

---

## External Resources

### Learning Materials

- [Java Documentation](https://docs.oracle.com/en/java/) - Language reference and API documentation
- [LWJGL Documentation](https://www.lwjgl.org/guide) - Native library bindings and tutorials
- [OpenGL Reference](https://www.khronos.org/opengl/) - Graphics API documentation
- [Vulkan Guide](https://vulkan-tutorial.com/) - Modern graphics API tutorial

### Development Tools

- [IntelliJ IDEA](https://www.jetbrains.com/idea/) - Recommended IDE for XNGIN development
- [Gradle Documentation](https://docs.gradle.org/) - Build system reference
- [Git Documentation](https://git-scm.com/doc) - Version control system

### Game Development Resources

- [Game Programming Patterns](https://gameprogrammingpatterns.com/) - Software architecture patterns for games
- [Real-Time Rendering](http://www.realtimerendering.com/) - Computer graphics techniques
- [Game Engine Architecture](https://www.gameenginebook.com/) - Comprehensive engine design guide

---

## Community & Support

### Getting Help

- **GitHub Issues** - Report bugs, request features, or ask technical questions
- **GitHub Discussions** - Community conversations, showcase projects, and general Q&A
- **Documentation Updates** - Suggest improvements or corrections to documentation

### Contributing

1. Read the [Contributing Guidelines](CONTRIBUTING.md) thoroughly
2. Start with [good first issues](https://github.com/iiXXiXii/XNGIN/labels/good%20first%20issue)
3. Join discussions about new features and improvements
4. Help improve documentation and examples

### Staying Updated

- **Watch the repository** to receive notifications about releases and important updates
- **Follow release notes** for information about new features and breaking changes
- **Check the documentation** regularly as it's continuously improved

---

## Documentation Maintenance

This documentation is actively maintained and improved by the XNGIN community. If you find:

- **Outdated information** - Please open an issue or submit a pull request
- **Missing content** - Suggest additions through GitHub Issues
- **Unclear explanations** - Help us improve clarity and accessibility
- **Broken links** - Report them so they can be fixed promptly

**Last Updated:** June 2025
**Documentation Version:** 1.0
**Engine Version:** Active Development

---

_Thank you for using XNGIN! We're excited to see what you'll create with this engine._
