# XNGIN

<div align="center">
  <img src="docs/images/xngin.png" alt="XNGIN Logo" width="180"/>
  <br>
  <em>Modern, modular Java engine for 2D & 3D games</em>
  <br><br>
  <a href="LICENSE.md"><img src="https://img.shields.io/badge/Licence-MIT-yellow.svg"></a>
  <a href="https://www.oracle.com/java/"><img src="https://img.shields.io/badge/Java-24-orange"></a>
  <a href="https://github.com/iiXXiXii/XNGIN"><img src="https://img.shields.io/badge/Status-Active%20Development-brightgreen"></a>
</div>

---

## Overview

**XNGIN** is a next-generation Java game engine focused on developer productivity, modularity, and performance. It abstracts the technical complexity of graphics, physics, audio, and input, letting you focus on gameplay and creativity.

- **2D & 3D support**
- **Modern Java (24+)**
- **Cross-platform (desktop, future mobile/web)**
- **Modular, plugin-friendly architecture**

---

## Quick Start

### Requirements

- Java 24+
- Gradle 8+ (wrapper included)

### Build & Run Demo

```bash
git clone https://github.com/iiXXiXii/XNGIN.git
cd XNGIN
./gradlew build
./gradlew :game-launcher:run
```

---

## Project Structure

XNGIN is organized into focused modules:

| Module                   | Purpose                                  |
|--------------------------|------------------------------------------|
| `engine-core`            | Main engine logic, lifecycle, logging    |
| `engine-math`            | Vectors, matrices, math utilities        |
| `engine-assets`          | Asset loading, caching, hot-reloading    |
| `engine-scene`           | Scene graph, ECS, entity management      |
| `engine-platform-api`    | Cross-platform system interfaces         |
| `engine-platform-lwjgl`  | Desktop implementation (LWJGL)           |
| `engine-renderer-api`    | Rendering abstraction layer              |
| `engine-renderer-gl`     | OpenGL backend                          |
| `engine-renderer-vk`     | Vulkan backend (planned)                 |
| `engine-audio-api`       | Audio abstraction                        |
| `engine-audio-openal`    | OpenAL audio backend                     |
| `engine-physics-api`     | Physics abstraction                      |
| `engine-physics-dyn4j`   | 2D physics (Dyn4j)                       |
| `engine-input`           | Unified input system                     |
| `game-launcher`          | Example/demo launcher                    |

---

## Create Your Game

1. **Add dependencies** in your `build.gradle.kts`:

    ```kotlin
    dependencies {
        implementation(project(":engine-core"))
        implementation(project(":engine-platform-lwjgl"))
        implementation(project(":engine-renderer-gl"))
        // ...other modules as needed
    }
    ```

2. **Implement your game:**

    ```java
    import com.xngin.Engine;
    import com.xngin.Game;

    public class MyGame extends Game {
        public static void main(String[] args) {
            new Engine().run(new MyGame());
        }
        @Override public void initialise() { /* setup */ }
        @Override public void update(float dt) { /* logic */ }
    }
    ```

3. **Scene example:**

    ```java
    import com.xngin.scene.Scene;
    import com.xngin.scene.Entity;
    import com.xngin.scene.components.TransformComponent;
    import com.xngin.scene.components.MeshRendererComponent;

    Scene scene = new Scene();
    Entity player = scene.createEntity("Player");
    player.addComponent(new TransformComponent());
    player.addComponent(new MeshRendererComponent());
    ```

---

## Technology Stack

- **LWJGL 3** (native bindings)
- **JOML** (math)
- **Dyn4j** (2D physics)
- **JUnit 5** (testing)
- **SLF4J + Logback** (logging)
- **Gradle (Kotlin DSL)**

---

## Extending XNGIN

- Add modules: create a directory, register in `settings.gradle.kts`
- Use `ServiceLoader` for runtime discovery
- Follow API/implementation separation
- Write tests and documentation

---

## Contributing

1. Fork & branch from `main`
2. Follow code style & modular patterns
3. Add tests and docs for new features
4. Submit a PR

See [docs/CONTRIBUTING.md](docs/CONTRIBUTING.md) for details.

---

## Testing

```bash
./gradlew test
./gradlew :engine-core:test
```

---

## Documentation

- [Architecture Guide](docs/architecture.md)
- [Quick Start](docs/quickstart.md)
- [Module Docs](docs/)

---

## Roadmap

- [ ] Core: game loop, event system, resource management
- [ ] Rendering: OpenGL (in progress), Vulkan (planned), materials, lighting
- [ ] Physics: API, collision, scene integration
- [ ] Audio: OpenAL, spatial audio
- [ ] Input: controller support, mapping
- [ ] Asset pipeline: loading, hot-reload
- [ ] Editor tools, scripting, networking, AI, mobile support

---

## License

MIT — see [LICENSE.md](LICENSE.md).

---

<div align="center">
  <strong>Build something extraordinary with XNGIN!</strong><br>
  <em>Questions or ideas? Open an issue or join the discussion.</em>
</div>