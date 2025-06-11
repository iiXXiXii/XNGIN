# Engine Scene Module Documentation

The `engine-scene` module forms the organisational backbone of XNGIN, providing a sophisticated entity-component-system (ECS) architecture combined with a hierarchical scene graph. This powerful combination enables developers to create complex, interactive game worlds whilst maintaining clean separation of concerns and optimal performance.

---

## Responsibilities

### Core Scene Management

- **Scene Graph Hierarchy**: Manages complex parent-child relationships between game objects with efficient spatial transformations
- **Entity Lifecycle**: Handles creation, destruction, activation, and deactivation of game entities
- **Scene Loading & Serialisation**: Supports loading scenes from files, runtime creation, and saving scene states
- **Multi-Scene Support**: Enables smooth transitions between different game areas or levels

### Entity-Component-System Architecture

- **Flexible Entity Composition**: Allows dynamic attachment and removal of components to create diverse game objects
- **Component Management**: Efficient storage, retrieval, and manipulation of component data
- **System Orchestration**: Coordinates the execution of systems that process entities and components
- **Type Safety**: Ensures component operations are type-safe and performant

### Performance Optimisation

- **Spatial Partitioning**: Implements efficient spatial data structures for collision detection and rendering optimisation
- **Component Pooling**: Reuses component instances to minimise garbage collection pressure
- **Batch Processing**: Groups similar operations for optimal performance
- **Culling Systems**: Automatically excludes non-visible or inactive entities from processing

---

## Architecture Overview

### Entity-Component-System Pattern

XNGIN implements a data-oriented ECS architecture that separates data (components) from behaviour (systems):

```
Entities: Containers holding components
    ↓
Components: Pure data structures
    ↓
Systems: Logic processors operating on components
```

### Core Classes

**Entity**: Lightweight identifier and component container

```java
public class Entity {
    private final EntityId id;
    private final ComponentMask componentMask;
    private final Scene parentScene;

    public <T extends Component> T addComponent(T component);
    public <T extends Component> T getComponent(Class<T> componentType);
    public boolean hasComponent(Class<? extends Component> componentType);
    public void removeComponent(Class<? extends Component> componentType);
}
```

**Component**: Base class for all component types

```java
public abstract class Component {
    private Entity entity;
    private boolean active = true;

    public void onAttached(Entity entity) { /* Override if needed */ }
    public void onDetached() { /* Override if needed */ }
    public void onEnabled() { /* Override if needed */ }
    public void onDisabled() { /* Override if needed */ }
}
```

**EntitySystem**: Processes entities with specific component combinations

```java
public abstract class EntitySystem {
    protected final ComponentMask requiredComponents;

    public abstract void update(float deltaTime);
    protected abstract void processEntity(Entity entity, float deltaTime);

    protected List<Entity> getEntitiesWithComponents(Class<?>... componentTypes);
}
```

### Scene Graph Structure

```
Scene
├── RootEntity
│   ├── Environment
│   │   ├── Lighting
│   │   └── Terrain
│   ├── GameObjects
│   │   ├── Player
│   │   ├── Enemies
│   │   └── Collectibles
│   └── UI
│       ├── HUD
│       └── Menus
```

---

## Core Components

### Transform Component

Manages position, rotation, and scale in 3D space:

```java
public class TransformComponent extends Component {
    private Vector3 position = new Vector3();
    private Quaternion rotation = new Quaternion();
    private Vector3 scale = new Vector3(1, 1, 1);

    private Matrix4 worldMatrix = new Matrix4();
    private boolean matrixDirty = true;

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        markMatrixDirty();
    }

    public Matrix4 getWorldMatrix() {
        if (matrixDirty) {
            updateWorldMatrix();
            matrixDirty = false;
        }
        return worldMatrix;
    }

    private void updateWorldMatrix() {
        worldMatrix.identity()
                  .translate(position)
                  .rotate(rotation)
                  .scale(scale);

        // Apply parent transformation if present
        Entity parent = getEntity().getParent();
        if (parent != null) {
            TransformComponent parentTransform = parent.getComponent(TransformComponent.class);
            if (parentTransform != null) {
                parentTransform.getWorldMatrix().mul(worldMatrix, worldMatrix);
            }
        }
    }
}
```

### Render Component

Defines visual representation:

```java
public class RenderComponent extends Component {
    private Mesh mesh;
    private Material material;
    private boolean visible = true;
    private float renderPriority = 0.0f;

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
        markForRenderUpdate();
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean shouldRender(Camera camera) {
        return visible && mesh != null && material != null;
    }
}
```

### Physics Component

Integrates with physics simulation:

```java
public class PhysicsComponent extends Component {
    private RigidBody rigidBody;
    private CollisionShape shape;
    private float mass = 1.0f;
    private float friction = 0.5f;
    private float restitution = 0.3f;

    @Override
    public void onAttached(Entity entity) {
        createPhysicsBody();
        registerWithPhysicsWorld();
    }

    @Override
    public void onDetached() {
        removeFromPhysicsWorld();
        destroyPhysicsBody();
    }

    public void applyForce(Vector3 force) {
        if (rigidBody != null) {
            rigidBody.applyForce(force);
        }
    }
}
```

### Script Component

Enables custom behaviour through scripting:

```java
public class ScriptComponent extends Component {
    private Script script;
    private Map<String, Object> scriptProperties = new HashMap<>();

    public void setScript(Script script) {
        this.script = script;
        script.setEntity(getEntity());
    }

    public void update(float deltaTime) {
        if (script != null && script.isEnabled()) {
            script.update(deltaTime);
        }
    }

    public void setProperty(String name, Object value) {
        scriptProperties.put(name, value);
        if (script != null) {
            script.setProperty(name, value);
        }
    }
}
```

---

## System Implementation

### Render System

Processes entities with render components:

```java
public class RenderSystem extends EntitySystem {

    private final Renderer renderer;
    private final List<RenderCommand> renderCommands = new ArrayList<>();

    public RenderSystem(Renderer renderer) {
        super(TransformComponent.class, RenderComponent.class);
        this.renderer = renderer;
    }

    @Override
    public void update(float deltaTime) {
        Camera activeCamera = getActiveCamera();
        if (activeCamera == null) return;

        renderCommands.clear();

        // Collect render commands from entities
        for (Entity entity : getRelevantEntities()) {
            processEntity(entity, deltaTime);
        }

        // Sort by render priority and material
        sortRenderCommands();

        // Submit to renderer
        renderer.beginFrame(activeCamera);
        for (RenderCommand command : renderCommands) {
            renderer.submitCommand(command);
        }
        renderer.endFrame();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        RenderComponent render = entity.getComponent(RenderComponent.class);

        if (render.shouldRender(getActiveCamera())) {
            RenderCommand command = new RenderCommand(
                render.getMesh(),
                render.getMaterial(),
                transform.getWorldMatrix(),
                render.getRenderPriority()
            );
            renderCommands.add(command);
        }
    }
}
```

### Physics System

Integrates entities with physics simulation:

```java
public class PhysicsSystem extends EntitySystem {

    private final PhysicsWorld physicsWorld;

    public PhysicsSystem(PhysicsWorld physicsWorld) {
        super(TransformComponent.class, PhysicsComponent.class);
        this.physicsWorld = physicsWorld;
    }

    @Override
    public void update(float deltaTime) {
        // Pre-physics: update physics bodies from transforms
        for (Entity entity : getRelevantEntities()) {
            updatePhysicsFromTransform(entity);
        }

        // Step physics simulation
        physicsWorld.step(deltaTime);

        // Post-physics: update transforms from physics bodies
        for (Entity entity : getRelevantEntities()) {
            updateTransformFromPhysics(entity);
        }
    }

    private void updatePhysicsFromTransform(Entity entity) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        PhysicsComponent physics = entity.getComponent(PhysicsComponent.class);

        if (!physics.isKinematic()) {
            return; // Physics controls this entity
        }

        // Update physics body from transform
        physics.getRigidBody().setPosition(transform.getPosition());
        physics.getRigidBody().setRotation(transform.getRotation());
    }

    private void updateTransformFromPhysics(Entity entity) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        PhysicsComponent physics = entity.getComponent(PhysicsComponent.class);

        if (physics.isKinematic()) {
            return; // Transform controls this entity
        }

        // Update transform from physics body
        RigidBody body = physics.getRigidBody();
        transform.setPosition(body.getPosition());
        transform.setRotation(body.getRotation());
    }
}
```

---

## Practical Examples

### Creating a Player Entity

```java
public class PlayerFactory {

    public static Entity createPlayer(Scene scene, Vector3 position) {
        Entity player = scene.createEntity("Player");

        // Transform component for position/rotation/scale
        TransformComponent transform = new TransformComponent();
        transform.setPosition(position);
        player.addComponent(transform);

        // Render component for visual representation
        RenderComponent render = new RenderComponent();
        render.setMesh(AssetManager.load("models/player.fbx", Mesh.class));
        render.setMaterial(AssetManager.load("materials/player.mat", Material.class));
        player.addComponent(render);

        // Physics component for collision and movement
        PhysicsComponent physics = new PhysicsComponent();
        physics.setMass(75.0f); // 75kg
        physics.setShape(new CapsuleShape(0.5f, 1.8f)); // Capsule for character
        physics.setFriction(0.8f);
        player.addComponent(physics);

        // Input component for player control
        InputComponent input = new InputComponent();
        input.bindAction("move_forward", KeyCode.W);
        input.bindAction("move_backward", KeyCode.S);
        input.bindAction("move_left", KeyCode.A);
        input.bindAction("move_right", KeyCode.D);
        input.bindAction("jump", KeyCode.SPACE);
        player.addComponent(input);

        // Script component for player behaviour
        ScriptComponent script = new ScriptComponent();
        script.setScript(new PlayerControllerScript());
        player.addComponent(script);

        // Audio component for sound effects
        AudioComponent audio = new AudioComponent();
        audio.addSound("footstep", AssetManager.load("audio/footstep.wav", AudioClip.class));
        audio.addSound("jump", AssetManager.load("audio/jump.wav", AudioClip.class));
        player.addComponent(audio);

        return player;
    }
}
```

### Custom Component Example

```java
public class HealthComponent extends Component {

    private float maxHealth = 100.0f;
    private float currentHealth = 100.0f;
    private boolean isDead = false;

    public void takeDamage(float damage) {
        if (isDead) return;

        currentHealth = Math.max(0, currentHealth - damage);

        // Publish health changed event
        EventBus.publish(new HealthChangedEvent(getEntity(), currentHealth, maxHealth));

        if (currentHealth <= 0 && !isDead) {
            die();
        }
    }

    public void heal(float amount) {
        if (isDead) return;

        currentHealth = Math.min(maxHealth, currentHealth + amount);
        EventBus.publish(new HealthChangedEvent(getEntity(), currentHealth, maxHealth));
    }

    private void die() {
        isDead = true;
        EventBus.publish(new EntityDeathEvent(getEntity()));

        // Trigger death animation
        AnimationComponent animation = getEntity().getComponent(AnimationComponent.class);
        if (animation != null) {
            animation.playAnimation("death");
        }
    }

    public float getHealthPercentage() {
        return maxHealth > 0 ? currentHealth / maxHealth : 0.0f;
    }
}
```

### Scene Loading and Management

```java
public class SceneManager {

    private Scene activeScene;
    private final Map<String, Scene> loadedScenes = new HashMap<>();

    public void loadScene(String scenePath) {
        Scene scene = SceneLoader.loadFromFile(scenePath);
        loadedScenes.put(scenePath, scene);
    }

    public void setActiveScene(String scenePath) {
        Scene newScene = loadedScenes.get(scenePath);
        if (newScene == null) {
            throw new IllegalArgumentException("Scene not loaded: " + scenePath);
        }

        // Deactivate current scene
        if (activeScene != null) {
            activeScene.onDeactivated();
        }

        // Activate new scene
        activeScene = newScene;
        activeScene.onActivated();

        // Update camera and rendering context
        updateActiveCamera();
        updateRenderingContext();
    }

    public void unloadScene(String scenePath) {
        Scene scene = loadedScenes.remove(scenePath);
        if (scene != null) {
            scene.destroy();
        }
    }

    public void preloadScenes(List<String> scenePaths) {
        for (String path : scenePaths) {
            CompletableFuture.runAsync(() -> loadScene(path));
        }
    }
}
```

### Entity Queries and Filtering

```java
public class EntityQuery {

    public static List<Entity> findEntitiesWithTag(Scene scene, String tag) {
        return scene.getEntities().stream()
                   .filter(entity -> entity.hasTag(tag))
                   .collect(Collectors.toList());
    }

    public static <T extends Component> List<Entity> findEntitiesWithComponent(
            Scene scene, Class<T> componentType) {
        return scene.getEntities().stream()
                   .filter(entity -> entity.hasComponent(componentType))
                   .collect(Collectors.toList());
    }

    public static List<Entity> findEntitiesInRadius(Scene scene, Vector3 center, float radius) {
        float radiusSquared = radius * radius;
        return scene.getEntities().stream()
                   .filter(entity -> entity.hasComponent(TransformComponent.class))
                   .filter(entity -> {
                       Vector3 pos = entity.getComponent(TransformComponent.class).getPosition();
                       return pos.distanceSquared(center) <= radiusSquared;
                   })
                   .collect(Collectors.toList());
    }

    public static Optional<Entity> findEntityByName(Scene scene, String name) {
        return scene.getEntities().stream()
                   .filter(entity -> name.equals(entity.getName()))
                   .findFirst();
    }
}
```

---

## Performance Optimisation

### Component Caching

```java
public class ComponentCache {

    private final Map<Class<?>, List<Component>> componentsByType = new HashMap<>();
    private boolean dirty = true;

    public <T extends Component> List<T> getComponents(Class<T> componentType) {
        if (dirty) {
            rebuildCache();
        }

        @SuppressWarnings("unchecked")
        List<T> components = (List<T>) componentsByType.get(componentType);
        return components != null ? components : Collections.emptyList();
    }

    public void markDirty() {
        dirty = true;
    }

    private void rebuildCache() {
        componentsByType.clear();

        for (Entity entity : scene.getEntities()) {
            for (Component component : entity.getComponents()) {
                componentsByType.computeIfAbsent(component.getClass(), k -> new ArrayList<>())
                              .add(component);
            }
        }

        dirty = false;
    }
}
```

### Spatial Partitioning

```java
public class SpatialGrid {

    private final float cellSize;
    private final Map<GridCell, List<Entity>> grid = new HashMap<>();

    public SpatialGrid(float cellSize) {
        this.cellSize = cellSize;
    }

    public void insert(Entity entity) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        if (transform != null) {
            GridCell cell = getCell(transform.getPosition());
            grid.computeIfAbsent(cell, k -> new ArrayList<>()).add(entity);
        }
    }

    public List<Entity> query(Vector3 position, float radius) {
        List<Entity> result = new ArrayList<>();

        // Calculate affected cells
        int minCellX = (int) Math.floor((position.x - radius) / cellSize);
        int maxCellX = (int) Math.ceil((position.x + radius) / cellSize);
        int minCellZ = (int) Math.floor((position.z - radius) / cellSize);
        int maxCellZ = (int) Math.ceil((position.z + radius) / cellSize);

        // Query all affected cells
        for (int x = minCellX; x <= maxCellX; x++) {
            for (int z = minCellZ; z <= maxCellZ; z++) {
                GridCell cell = new GridCell(x, z);
                List<Entity> entities = grid.get(cell);
                if (entities != null) {
                    result.addAll(entities);
                }
            }
        }

        return result;
    }
}
```

---

## Testing Scene Components

### Unit Testing Components

```java
class HealthComponentTest {

    private Entity entity;
    private HealthComponent healthComponent;

    @BeforeEach
    void setUp() {
        Scene scene = new Scene();
        entity = scene.createEntity("TestEntity");
        healthComponent = new HealthComponent();
        entity.addComponent(healthComponent);
    }

    @Test
    @DisplayName("Should reduce health when taking damage")
    void shouldReduceHealthWhenTakingDamage() {
        // Given
        float initialHealth = healthComponent.getCurrentHealth();
        float damage = 25.0f;

        // When
        healthComponent.takeDamage(damage);

        // Then
        assertThat(healthComponent.getCurrentHealth()).isEqualTo(initialHealth - damage);
    }

    @Test
    @DisplayName("Should trigger death event when health reaches zero")
    void shouldTriggerDeathEventWhenHealthReachesZero() {
        // Given
        AtomicBoolean deathEventFired = new AtomicBoolean(false);
        EventBus.subscribe(EntityDeathEvent.class, event -> deathEventFired.set(true));

        // When
        healthComponent.takeDamage(healthComponent.getMaxHealth());

        // Then
        assertThat(healthComponent.isDead()).isTrue();
        assertThat(deathEventFired.get()).isTrue();
    }
}
```

### Integration Testing

```java
@IntegrationTest
class SceneIntegrationTest {

    @Test
    @DisplayName("Should process entities through multiple systems")
    void shouldProcessEntitiesThroughMultipleSystems() {
        // Given
        Scene scene = new Scene();
        Entity entity = PlayerFactory.createPlayer(scene, new Vector3(0, 0, 0));

        RenderSystem renderSystem = new RenderSystem(mockRenderer);
        PhysicsSystem physicsSystem = new PhysicsSystem(mockPhysicsWorld);

        scene.addSystem(renderSystem);
        scene.addSystem(physicsSystem);

        // When
        scene.update(1.0f / 60.0f); // One frame at 60 FPS

        // Then
        verify(mockRenderer).beginFrame(any(Camera.class));
        verify(mockRenderer).submitCommand(any(RenderCommand.class));
        verify(mockPhysicsWorld).step(eq(1.0f / 60.0f));
    }
}
```

---

## Extending the Scene Module

### Creating Custom Components

```java
public class InventoryComponent extends Component {

    private final Map<String, Integer> items = new HashMap<>();
    private int maxSlots = 20;

    public boolean addItem(String itemType, int quantity) {
        if (items.size() >= maxSlots && !items.containsKey(itemType)) {
            return false; // Inventory full
        }

        items.merge(itemType, quantity, Integer::sum);
        EventBus.publish(new InventoryChangedEvent(getEntity(), itemType, quantity));
        return true;
    }

    public boolean removeItem(String itemType, int quantity) {
        Integer currentQuantity = items.get(itemType);
        if (currentQuantity == null || currentQuantity < quantity) {
            return false; // Not enough items
        }

        if (currentQuantity == quantity) {
            items.remove(itemType);
        } else {
            items.put(itemType, currentQuantity - quantity);
        }

        EventBus.publish(new InventoryChangedEvent(getEntity(), itemType, -quantity));
        return true;
    }
}
```

### Custom System Implementation

```java
public class AISystem extends EntitySystem {

    public AISystem() {
        super(TransformComponent.class, AIComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : getRelevantEntities()) {
            processAI(entity, deltaTime);
        }
    }

    private void processAI(Entity entity, float deltaTime) {
        AIComponent ai = entity.getComponent(AIComponent.class);
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        // Update AI state machine
        ai.updateStateMachine(deltaTime);

        // Process current behaviour
        AIBehaviour currentBehaviour = ai.getCurrentBehaviour();
        if (currentBehaviour != null) {
            currentBehaviour.execute(entity, deltaTime);
        }

        // Update pathfinding
        if (ai.hasPathfindingTarget()) {
            updatePathfinding(entity, ai, transform);
        }
    }
}
```

---

## Further Reading

**Core Documentation:**

- [Architecture Guide](architecture.md) - Overall engine design and module interactions
- [Engine Core](engine-core.md) - Central engine coordination and lifecycle management
- [Engine Math](engine-math.md) - Mathematical foundations for transformations and calculations

**Related Modules:**

- [Engine Renderer API](engine-renderer-api.md) - Graphics rendering integration
- [Engine Physics API](engine-physics-api.md) - Physics simulation integration
- [Engine Input](engine-input.md) - Input handling and event processing

**External Resources:**

- [Entity-Component-System Architecture](https://en.wikipedia.org/wiki/Entity_component_system)
- [Data-Oriented Design](https://dataorienteddesign.com/dodbook/)
- [Game Programming Patterns - Component](https://gameprogrammingpatterns.com/component.html)

For questions about scene architecture, component design, or performance optimisation, please engage with the community through GitHub Issues and Discussions.
