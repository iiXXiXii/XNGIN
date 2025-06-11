# XNGIN Architecture Guide

Welcome to the comprehensive XNGIN architecture guide. This document provides a detailed overview of the engine's sophisticated modular structure, the responsibilities of each module, and how they interact to create a cohesive game development platform. It serves as essential reading for both users and contributors who wish to understand, extend, or optimise the engine.

---

## Philosophy and Design Principles

XNGIN is architected around several core principles:

**Modular Independence**: Each module can be developed, tested, and replaced in isolation whilst maintaining system integrity.

**Clear Separation of Concerns**: Every module has a well-defined responsibility, preventing tight coupling and promoting maintainability.

**API-First Design**: Implementation modules depend only on API interfaces, enabling multiple backends and future extensibility.

**Performance Consciousness**: Architecture decisions prioritise runtime performance whilst maintaining developer ergonomics.

---

## Modular Architecture Overview

XNGIN is engineered as a collection of independent, focused modules built using modern Java (Java 24+) and Gradle for sophisticated dependency management and builds.

### Comprehensive Module Hierarchy

```
┌─────────────────────┐
│   game-launcher     │ ← Application Entry Point
└─────────────────────┘
          │
          ▼
┌─────────────────────┐
│   engine-core       │ ← Central Orchestration
└─────────────────────┘
     │   │   │   │   │
     ▼   ▼   ▼   ▼   ▼
┌────────┬────────┬──────────┬──────────┬─────────┐
│  math  │ assets │  scene   │ platform │ network │
└────────┴────────┴──────────┴──────────┴─────────┘
     │        │        │          │         │
     ▼        ▼        ▼          ▼         ▼
┌─────────┬─────────┬─────────┬─────────┬─────────┐
│renderer │  audio  │ physics │  input  │   ai    │
└─────────┴─────────┴─────────┴─────────┴─────────┘
```

---

## Detailed Module Responsibilities

### Core Engine Modules

**engine-core**: The central nervous system of XNGIN

- Lifecycle management and state transitions
- Module coordination and dependency injection
- Event system and inter-module communication
- Configuration management and validation
- Memory management and object pooling
- Threading and task scheduling
- UI framework and widget systems
- Networking and multiplayer coordination
- AI systems and decision frameworks
- Particle systems and visual effects
- Debug tools and development utilities

**engine-math**: High-performance mathematical foundation

- Vector and matrix operations (2D, 3D, 4D)
- Quaternion mathematics for rotations
- Geometric primitives and collision shapes
- Interpolation and easing functions
- Curve mathematics (Bezier, splines)
- Statistical analysis and probability functions
- Advanced algorithms for computational geometry

**engine-assets**: Comprehensive asset management pipeline

- Multi-format asset loading and processing
- Intelligent caching and memory management
- Hot-reloading for rapid development iteration
- Animation systems and controllers
- Material and shader management
- Mesh generation and manipulation utilities

---

## Module Interaction and Communication

- Modules communicate via well-defined interfaces, typically declared in \*-api modules.
- Implementations are discovered at runtime using Java's `ServiceLoader` or a custom plugin system.
- The core engine depends only on API modules, not on specific implementations, ensuring loose coupling.
- Events and callbacks are used for cross-module communication where appropriate.

---

### Platform and Rendering Modules

**engine-platform-api & engine-platform-lwjgl**: Cross-platform abstraction and desktop implementation

- Window management and event handling
- Input device abstraction and event processing
- File system operations and resource access
- Threading utilities and platform-specific optimisations
- System information and capability detection

**engine-renderer-api, engine-renderer-gl & engine-renderer-vk**: Graphics abstraction and implementations

- Hardware-agnostic rendering interface definitions
- OpenGL backend for broad compatibility and development ease
- Vulkan backend for maximum performance and advanced features
- Shader management, compilation, and resource binding
- Modern rendering techniques and optimisation strategies

### Specialised Systems

**engine-scene**: Entity-component-system architecture

- Entity lifecycle management and hierarchical relationships
- Component registration, serialisation, and type safety
- System orchestration and execution scheduling
- Scene loading, saving, and runtime switching capabilities

**engine-physics-api & engine-physics-dyn4j**: Physics simulation framework

- Abstract physics interfaces supporting multiple backends
- 2D physics implementation with advanced collision detection
- Rigid body dynamics, constraints, and force applications
- Integration with scene management for seamless physics interactions

**engine-audio-api & engine-audio-openal**: Comprehensive audio system

- 3D positional audio with environmental effects processing
- Audio resource management and streaming capabilities
- Effect chains, filters, and real-time audio manipulation
- Cross-platform audio device abstraction and optimisation

**engine-input**: Unified input management

- Device-agnostic input abstraction supporting keyboard, mouse, and controllers
- Customisable input mapping and action binding systems
- Event-driven input processing with support for complex input combinations

---

## Module Dependencies and Coupling

The dependency graph follows a strict hierarchy to ensure clean separation:

```
game-launcher
    ↓
engine-core
    ↓
[API Modules] ← [Implementation Modules]
    ↓
engine-math (foundational, no dependencies)
```

**Key Principles:**

- **API modules** define contracts but contain no implementation details
- **Implementation modules** depend only on their corresponding API modules
- **Core module** orchestrates but doesn't implement domain-specific functionality
- **Math module** provides pure mathematical utilities with no external dependencies

---

## Runtime Architecture and Lifecycle

### Engine Initialisation Sequence

1. **Bootstrap Phase**: Load core configuration and detect platform capabilities
2. **Module Discovery**: Scan for available implementations using ServiceLoader mechanism
3. **Dependency Resolution**: Establish module loading order based on dependencies
4. **Service Registration**: Register services with the central service locator
5. **System Initialisation**: Initialise all engine subsystems in dependency order
6. **Application Handover**: Transfer control to the game application

### Main Loop Architecture

The engine employs a sophisticated main loop supporting variable timesteps and frame rate independence:

```java
while (running) {
    // Fixed timestep physics updates
    while (accumulator >= PHYSICS_TIMESTEP) {
        physicsSystem.update(PHYSICS_TIMESTEP);
        accumulator -= PHYSICS_TIMESTEP;
    }

    // Variable timestep game logic
    inputSystem.processEvents();
    gameLogic.update(deltaTime);

    // Rendering with interpolation
    renderer.render(interpolationFactor);

    // Timing and throttling
    frameTimer.sync();
}
```

### Memory Management Strategy

- **Object Pooling**: Reuse frequently allocated objects to reduce garbage collection pressure
- **Resource Caching**: Intelligent caching of assets with configurable eviction policies
- **Weak References**: Use weak references for optional caches to allow graceful memory reclamation
- **Native Resource Tracking**: Careful management of OpenGL/Vulkan resources with automatic cleanup

---

## Extending the Engine

### Adding New Modules

To create a new engine module:

1. **Define the API**: Create a new `engine-[feature]-api` module with clear interface definitions
2. **Implement the Feature**: Develop one or more implementation modules (e.g., `engine-[feature]-impl`)
3. **Register Services**: Use Java's `ServiceLoader` mechanism for runtime discovery
4. **Update Build Configuration**: Add the modules to `settings.gradle.kts` and configure dependencies
5. **Document the Module**: Create comprehensive documentation following the established template

### Implementing New Platform Backends

XNGIN's architecture makes adding new platform support straightforward:

1. **Implement Platform Interfaces**: Create implementations of platform API interfaces
2. **Handle Platform Specifics**: Address platform-unique requirements whilst maintaining API compatibility
3. **Optimise for Target**: Leverage platform-specific features for optimal performance
4. **Test Thoroughly**: Ensure consistent behaviour across all supported operations

### Creating Custom Renderers

The renderer abstraction supports multiple graphics backends:

1. **Implement Renderer API**: Provide concrete implementations of all rendering interfaces
2. **Handle Resource Management**: Implement efficient resource creation, binding, and cleanup
3. **Optimise Pipeline**: Design rendering pipelines optimised for the target graphics API
4. **Support Debugging**: Include comprehensive debugging and profiling capabilities

---

## Performance Considerations

### Threading Model

- **Main Thread**: Handles game logic, input processing, and rendering coordination
- **Physics Thread**: Dedicated thread for physics simulation with synchronised state transfer
- **Asset Loading**: Background threads for non-blocking asset loading and processing
- **Audio Thread**: Separate thread for audio processing to maintain low-latency audio

### Optimisation Strategies

- **Batch Operations**: Group similar operations to reduce API calls and improve performance
- **Spatial Partitioning**: Use efficient spatial data structures for collision detection and rendering
- **Level-of-Detail**: Implement LOD systems for both rendering and physics based on distance and importance
- **Culling Systems**: Frustum and occlusion culling to reduce unnecessary processing

---

## Integration Patterns

### Service Locator Pattern

The engine uses a central service locator for dependency injection and service discovery:

```java
ServiceLocator.register(AudioManager.class, new OpenALAudioManager());
AudioManager audio = ServiceLocator.get(AudioManager.class);
```

### Event System

Cross-module communication uses a type-safe event system:

```java
EventBus.publish(new EntityCreatedEvent(entity));
EventBus.subscribe(EntityCreatedEvent.class, this::onEntityCreated);
```

### Component System

The entity-component-system supports flexible game object composition:

```java
Entity player = scene.createEntity();
player.addComponent(new TransformComponent());
player.addComponent(new PhysicsComponent());
player.addComponent(new RenderComponent());
```

---

## Development Workflow

### Module Development

1. **API First**: Design clean, minimal APIs before implementation
2. **Test-Driven**: Write comprehensive tests for both API contracts and implementations
3. **Documentation**: Maintain up-to-date documentation with practical examples
4. **Performance**: Profile and optimise critical paths continuously

### Continuous Integration

- **Automated Testing**: All modules tested on every commit
- **Cross-Platform Validation**: Verify functionality across supported platforms
- **Performance Regression**: Monitor performance metrics to detect regressions
- **Documentation Generation**: Automatically generate and deploy API documentation

---

## Further Reading

- [README.md](../README.md) — Project overview and quick start guide
- [Quick Start Guide](quickstart.md) — Step-by-step development tutorial
- [CONTRIBUTING.md](CONTRIBUTING.md) — Comprehensive contribution guidelines
- [Module Documentation](.) — Detailed guides for each engine module

**External Resources:**

- [Java ServiceLoader Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ServiceLoader.html)
- [LWJGL Documentation](https://www.lwjgl.org/guide) — Native library integration
- [Entity-Component-System Architecture](https://en.wikipedia.org/wiki/Entity_component_system) — ECS design patterns

For questions, suggestions, or technical discussions, please open an issue or start a discussion on the project's repository.
