# Engine Core Module Documentation

The `engine-core` module serves as the beating heart of XNGIN, orchestrating all engine subsystems whilst providing a clean, unified API for game development. It embodies the engine's central philosophy of modular design combined with ease of use, managing complex interactions between components so developers can focus on creating exceptional gaming experiences.

---

## Responsibilities

### Primary Functions

**Engine Lifecycle Management**

- Comprehensive initialisation sequence ensuring proper dependency resolution
- Sophisticated main loop implementation with variable and fixed timestep support
- Graceful shutdown procedures with proper resource cleanup and state persistence

**Module Orchestration**

- Dynamic discovery and loading of engine modules using Java's ServiceLoader mechanism
- Dependency injection and service registration through the central service locator
- Inter-module communication via type-safe event systems and well-defined interfaces

**Configuration Management**

- Flexible configuration system supporting files, environment variables, and programmatic setup
- Runtime configuration validation with helpful error messages and fallback defaults
- Hot-reloading of configuration during development for rapid iteration

**System Services**

- Centralised logging infrastructure with configurable output targets and filtering
- Performance monitoring and profiling capabilities for development and optimisation
- Memory management utilities including object pooling and garbage collection optimisation

**Error Handling & Resilience**

- Comprehensive error handling with graceful degradation strategies
- Diagnostic information collection for debugging and issue resolution
- Recovery mechanisms for non-critical failures to maintain application stability

---

## Core Architecture

### Engine Lifecycle Phases

The engine follows a carefully orchestrated lifecycle designed for reliability and extensibility:

#### 1. Bootstrap Phase

```java
// Platform detection and basic environment setup
PlatformDetector.initialise();
Logger.configure(config.getLoggingConfig());
```

#### 2. Configuration Phase

```java
// Load and validate configuration from multiple sources
EngineConfig config = ConfigurationManager.load()
    .fromFile("engine.properties")
    .fromEnvironment()
    .fromCommandLine(args)
    .validate();
```

#### 3. Module Discovery Phase

```java
// Discover and register available modules
ServiceLoader<EngineModule> modules = ServiceLoader.load(EngineModule.class);
ModuleRegistry.registerAll(modules);
```

#### 4. Dependency Resolution

```java
// Resolve module dependencies and establish loading order
DependencyGraph graph = DependencyResolver.resolve(registeredModules);
List<EngineModule> loadOrder = graph.topologicalSort();
```

#### 5. System Initialisation

```java
// Initialise modules in dependency order
for (EngineModule module : loadOrder) {
    module.initialise(engineContext);
    ServiceLocator.register(module.getServiceInterface(), module);
}
```

#### 6. Game Handover

```java
// Transfer control to the game application
Game game = config.getGameClass().getDeclaredConstructor().newInstance();
game.initialise();
```

### Main Loop Implementation

The engine implements a sophisticated main loop supporting both fixed and variable timesteps:

```java
public class GameLoop {
    private static final double PHYSICS_TIMESTEP = 1.0 / 60.0; // 60Hz physics
    private double accumulator = 0.0;

    public void run() {
        Timer frameTimer = new Timer();

        while (engine.isRunning()) {
            double deltaTime = frameTimer.getDeltaTime();
            accumulator += deltaTime;

            // Process input events
            inputManager.processEvents();

            // Fixed timestep physics updates
            while (accumulator >= PHYSICS_TIMESTEP) {
                physicsSystem.update(PHYSICS_TIMESTEP);
                accumulator -= PHYSICS_TIMESTEP;
            }

            // Variable timestep game logic
            game.update((float) deltaTime);
            sceneManager.update((float) deltaTime);

            // Render with interpolation for smooth visuals
            double interpolation = accumulator / PHYSICS_TIMESTEP;
            renderer.render((float) interpolation);

            // Frame rate limiting and V-Sync
            frameTimer.sync(config.getTargetFrameRate());
        }
    }
}
```

### Service Locator Pattern

The core module implements a sophisticated service locator for dependency injection:

```java
public class ServiceLocator {
    private static final Map<Class<?>, Object> services = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Class<?>> bindings = new ConcurrentHashMap<>();

    public static <T> void register(Class<T> serviceInterface, T implementation) {
        services.put(serviceInterface, implementation);
        Logger.debug("Registered service: {} -> {}",
                    serviceInterface.getSimpleName(),
                    implementation.getClass().getSimpleName());
    }

    public static <T> T get(Class<T> serviceInterface) {
        @SuppressWarnings("unchecked")
        T service = (T) services.get(serviceInterface);

        if (service == null) {
            throw new ServiceNotFoundException("No implementation registered for: "
                                             + serviceInterface.getSimpleName());
        }

        return service;
    }

    public static <T> Optional<T> getOptional(Class<T> serviceInterface) {
        @SuppressWarnings("unchecked")
        T service = (T) services.get(serviceInterface);
        return Optional.ofNullable(service);
    }
}
```

### Event System Implementation

Type-safe event communication between modules:

```java
public class EventBus {
    private static final Map<Class<?>, List<Consumer<?>>> subscribers = new ConcurrentHashMap<>();

    public static <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }

    public static <T> void publish(T event) {
        Class<?> eventType = event.getClass();
        List<Consumer<?>> handlers = subscribers.get(eventType);

        if (handlers != null) {
            for (Consumer<?> handler : handlers) {
                @SuppressWarnings("unchecked")
                Consumer<T> typedHandler = (Consumer<T>) handler;

                try {
                    typedHandler.accept(event);
                } catch (Exception e) {
                    Logger.error("Error processing event: {}", eventType.getSimpleName(), e);
                }
            }
        }
    }
}
```

---

## Configuration System

### Configuration Sources

The engine supports multiple configuration sources with a clear precedence hierarchy:

1. **Command line arguments** (highest priority)
2. **Environment variables**
3. **Configuration files** (engine.properties, engine.json)
4. **Programmatic configuration**
5. **Built-in defaults** (lowest priority)

### Configuration Example

```java
// Programmatic configuration
EngineConfig config = new EngineConfig.Builder()
    .windowTitle("My Awesome Game")
    .windowSize(1920, 1080)
    .fullscreen(false)
    .enableVSync(true)
    .targetFrameRate(60)
    .renderer(RendererType.OPENGL)
    .audioBackend(AudioBackend.OPENAL)
    .physicsEngine(PhysicsEngine.DYN4J)
    .logLevel(LogLevel.INFO)
    .enableHotReload(true)
    .assetPath("assets/")
    .build();

Engine engine = new Engine(config);
```

### Configuration File Format

```properties
# engine.properties
engine.window.title=My Game
engine.window.width=1280
engine.window.height=720
engine.window.fullscreen=false
engine.window.vsync=true

engine.renderer.backend=opengl
engine.renderer.msaa=4
engine.renderer.anisotropic_filtering=16

engine.audio.backend=openal
engine.audio.master_volume=1.0
engine.audio.spatial_audio=true

engine.physics.backend=dyn4j
engine.physics.gravity=-9.81
engine.physics.step_size=0.016667

engine.logging.level=info
engine.logging.console=true
engine.logging.file=logs/engine.log

engine.development.hot_reload=true
engine.development.debug_overlay=false
```

---

## Practical Examples

### Basic Game Implementation

```java
package com.mygame;

import com.xngin.Engine;
import com.xngin.EngineConfig;
import com.xngin.Game;
import com.xngin.ServiceLocator;
import com.xngin.scene.Scene;
import com.xngin.scene.SceneManager;
import com.xngin.input.InputManager;

public class MyGame extends Game {

    private Scene mainScene;
    private SceneManager sceneManager;
    private InputManager inputManager;

    public static void main(String[] args) {
        EngineConfig config = EngineConfig.fromFile("game.properties");
        Engine engine = new Engine(config);
        engine.run(new MyGame());
    }

    @Override
    public void initialise() {
        // Get required services
        sceneManager = ServiceLocator.get(SceneManager.class);
        inputManager = ServiceLocator.get(InputManager.class);

        // Create the main game scene
        mainScene = new Scene("MainScene");
        setupGameEntities();

        // Set as active scene
        sceneManager.setActiveScene(mainScene);

        Logger.info("Game initialised successfully");
    }

    @Override
    public void update(float deltaTime) {
        // Handle input
        handleInput();

        // Update game logic
        updateGameLogic(deltaTime);
    }

    @Override
    public void shutdown() {
        Logger.info("Game shutting down");
        // Perform cleanup
        if (mainScene != null) {
            mainScene.cleanup();
        }
    }

    private void setupGameEntities() {
        // Create game entities and components
        Entity player = mainScene.createEntity("Player");
        player.addComponent(new TransformComponent());
        player.addComponent(new RenderComponent());
        player.addComponent(new PlayerControllerComponent());

        Entity camera = mainScene.createEntity("MainCamera");
        camera.addComponent(new TransformComponent());
        camera.addComponent(new CameraComponent());
    }

    private void handleInput() {
        if (inputManager.isKeyPressed(KeyCode.ESCAPE)) {
            Engine.getInstance().requestShutdown();
        }

        if (inputManager.isKeyPressed(KeyCode.F11)) {
            EngineConfig.toggleFullscreen();
        }
    }

    private void updateGameLogic(float deltaTime) {
        // Implement game-specific logic here
        updatePlayerMovement(deltaTime);
        updateGameSystems(deltaTime);
        checkGameConditions();
    }
}
```

### Custom Module Integration

```java
// Define a custom module interface
public interface NetworkModule extends EngineModule {
    void startServer(int port);
    void connectToServer(String address, int port);
    void sendMessage(NetworkMessage message);
}

// Register and use the module
public class MyNetworkedGame extends Game {

    @Override
    public void initialise() {
        // Get the network module if available
        Optional<NetworkModule> network = ServiceLocator.getOptional(NetworkModule.class);

        if (network.isPresent()) {
            network.get().startServer(7777);
            Logger.info("Multiplayer server started on port 7777");
        } else {
            Logger.info("Network module not available, running in single-player mode");
        }
    }
}
```

### Event-Driven Architecture

```java
// Define custom events
public class PlayerLevelUpEvent {
    private final Entity player;
    private final int newLevel;

    public PlayerLevelUpEvent(Entity player, int newLevel) {
        this.player = player;
        this.newLevel = newLevel;
    }

    // Getters...
}

// Subscribe to events
public class UISystem {

    public void initialise() {
        EventBus.subscribe(PlayerLevelUpEvent.class, this::onPlayerLevelUp);
    }

    private void onPlayerLevelUp(PlayerLevelUpEvent event) {
        showLevelUpNotification(event.getPlayer(), event.getNewLevel());
        updatePlayerStatsUI(event.getPlayer());
    }
}

// Publish events
public class PlayerComponent extends Component {

    private void gainExperience(int amount) {
        experience += amount;

        if (experience >= getExperienceForNextLevel()) {
            level++;
            EventBus.publish(new PlayerLevelUpEvent(getEntity(), level));
        }
    }
}
```

---

## Performance Optimisation

### Memory Management

The core module provides several memory management utilities:

```java
// Object pooling for frequently created objects
public class ObjectPool<T> {
    private final Queue<T> pool = new ConcurrentLinkedQueue<>();
    private final Supplier<T> factory;

    public ObjectPool(Supplier<T> factory, int initialSize) {
        this.factory = factory;
        for (int i = 0; i < initialSize; i++) {
            pool.offer(factory.get());
        }
    }

    public T acquire() {
        T object = pool.poll();
        return object != null ? object : factory.get();
    }

    public void release(T object) {
        if (object instanceof Poolable) {
            ((Poolable) object).reset();
        }
        pool.offer(object);
    }
}

// Usage example
ObjectPool<Vector3> vectorPool = new ObjectPool<>(Vector3::new, 100);

// In performance-critical code
Vector3 temp = vectorPool.acquire();
try {
    // Use the vector
    temp.set(x, y, z);
    doCalculation(temp);
} finally {
    vectorPool.release(temp);
}
```

### Performance Monitoring

```java
public class PerformanceMonitor {

    public static void beginFrame() {
        currentFrame.startTime = System.nanoTime();
    }

    public static void endFrame() {
        currentFrame.endTime = System.nanoTime();
        updateFrameStatistics();
    }

    public static void beginSection(String name) {
        ProfileSection section = new ProfileSection(name, System.nanoTime());
        sectionStack.push(section);
    }

    public static void endSection() {
        ProfileSection section = sectionStack.pop();
        section.endTime = System.nanoTime();
        recordSectionTime(section);
    }

    public static PerformanceReport generateReport() {
        return new PerformanceReport(frameStatistics, sectionTimes);
    }
}
```

---

## Extending the Core Module

### Adding New Services

To add a new engine service:

1. **Define the service interface**:

```java
public interface CustomService {
    void processCustomData(CustomData data);
    CustomResult queryCustomInformation(String query);
}
```

2. **Implement the service**:

```java
public class CustomServiceImpl implements CustomService {

    @Override
    public void processCustomData(CustomData data) {
        // Implementation
    }

    @Override
    public CustomResult queryCustomInformation(String query) {
        // Implementation
        return result;
    }
}
```

3. **Register during engine initialisation**:

```java
public class CustomEngineModule implements EngineModule {

    @Override
    public void initialise(EngineContext context) {
        CustomService service = new CustomServiceImpl();
        ServiceLocator.register(CustomService.class, service);
    }
}
```

### Custom Configuration Providers

```java
public class DatabaseConfigurationProvider implements ConfigurationProvider {

    @Override
    public Properties loadConfiguration() {
        // Load configuration from database
        Properties config = new Properties();
        // ... database query logic ...
        return config;
    }

    @Override
    public boolean supports(String source) {
        return source.startsWith("database:");
    }
}

// Register the provider
ConfigurationManager.registerProvider(new DatabaseConfigurationProvider());
```

---

## Debugging and Development Tools

### Debug Overlay System

The core module includes a comprehensive debug overlay:

```java
public class DebugOverlay {

    public static void enable() {
        isEnabled = true;
        InputManager.registerHandler(KeyCode.F3, DebugOverlay::toggle);
    }

    public static void addDebugInfo(String category, String key, Object value) {
        debugData.computeIfAbsent(category, k -> new HashMap<>()).put(key, value);
    }

    public static void render(Renderer renderer) {
        if (!isEnabled) return;

        // Render performance metrics
        renderPerformancePanel(renderer);

        // Render debug information
        renderDebugPanels(renderer);

        // Render console
        renderConsole(renderer);
    }
}
```

### Development Console

```java
public class DeveloperConsole {

    public static void registerCommand(String name, ConsoleCommand command) {
        commands.put(name, command);
    }

    public static void executeCommand(String commandLine) {
        String[] parts = commandLine.split(" ");
        String commandName = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        ConsoleCommand command = commands.get(commandName);
        if (command != null) {
            command.execute(args);
        } else {
            Logger.warn("Unknown command: {}", commandName);
        }
    }
}

// Built-in commands
DebugConsole.registerCommand("engine.info", args -> {
    Logger.info("XNGIN Engine v{}", Engine.getVersion());
    Logger.info("Java: {}", System.getProperty("java.version"));
    Logger.info("Platform: {}", PlatformDetector.getPlatformName());
});

DebugConsole.registerCommand("scene.entities", args -> {
    Scene activeScene = SceneManager.getActiveScene();
    Logger.info("Active scene contains {} entities", activeScene.getEntityCount());
});
```

---

## Testing the Core Module

### Unit Testing Example

```java
class EngineTest {

    private Engine engine;
    private MockEngineConfig config;

    @BeforeEach
    void setUp() {
        config = new MockEngineConfig();
        engine = new Engine(config);
    }

    @Test
    @DisplayName("Should initialise all required services")
    void shouldInitialiseRequiredServices() {
        // When
        engine.initialise();

        // Then
        assertThat(ServiceLocator.get(InputManager.class)).isNotNull();
        assertThat(ServiceLocator.get(SceneManager.class)).isNotNull();
        assertThat(ServiceLocator.get(AssetManager.class)).isNotNull();
    }

    @Test
    @DisplayName("Should handle graceful shutdown")
    void shouldHandleGracefulShutdown() {
        // Given
        engine.initialise();

        // When
        engine.requestShutdown();

        // Then
        assertThat(engine.isRunning()).isFalse();
        verify(mockGame).shutdown();
    }
}
```

### Integration Testing

```java
@IntegrationTest
class EngineIntegrationTest {

    @Test
    @DisplayName("Should run complete game loop without errors")
    void shouldRunCompleteGameLoop() {
        // Given
        TestGame testGame = new TestGame();
        Engine engine = new Engine(createTestConfig());

        // When
        engine.run(testGame);

        // Then
        assertThat(testGame.wasInitialised()).isTrue();
        assertThat(testGame.getUpdateCount()).isGreaterThan(0);
        assertThat(testGame.wasShutdownGracefully()).isTrue();
    }
}
```

---

## Further Reading

**Core Documentation:**

- [Architecture Guide](architecture.md) - Comprehensive system design overview
- [Quick Start Guide](quickstart.md) - Step-by-step development tutorial
- [CONTRIBUTING.md](CONTRIBUTING.md) - Development practices and guidelines

**Module Documentation:**

- [Engine Math](engine-math.md) - Mathematical foundations and utilities
- [Engine Scene](engine-scene.md) - Entity-component-system architecture
- [Engine Assets](engine-assets.md) - Asset management and loading systems

**External Resources:**

- [Java ServiceLoader Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ServiceLoader.html)
- [Dependency Injection Patterns](https://martinfowler.com/articles/injection.html)
- [Game Loop Architectures](https://dewitters.com/dewitters-gameloop/)

For questions, technical discussions, or contribution ideas, please engage with the community through GitHub Issues and Discussions.
