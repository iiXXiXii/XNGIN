# Engine Input Module Documentation

The `engine-input` module provides a robust abstraction for handling user input in XNGIN. It supports keyboard, mouse, and other input devices, and is designed to be extensible for future input methods such as gamepads or touch.

---

## Responsibilities

- **Input Abstraction**: Offers a unified API for querying input states, regardless of the underlying platform.
- **Event Handling**: Supports event-driven input (e.g., key presses, mouse movement, button clicks).
- **Input Mapping**: Allows mapping of physical inputs to logical actions, making it easy to configure controls.
- **Integration**: Works seamlessly with the platform and scene modules to deliver input to the correct game objects or systems.

---

## Key Concepts

### Input Devices
- Keyboard
- Mouse
- (Planned) Gamepad, touch, and other devices

### Input Events
- Key down/up
- Mouse button down/up
- Mouse movement and scroll
- Custom events for advanced input

### Input Mapping
- Map keys or buttons to named actions (e.g., "Jump", "Shoot").
- Supports rebinding and configuration at runtime or via settings files.

---

## Example Usage

```java
import com.xngin.input.Input;

if (Input.isKeyPressed(Key.SPACE)) {
    // Make the player jump
}
```

---

## Extending the Module

- Add support for new input devices by implementing the relevant interfaces.
- Integrate with the scene and asset modules for context-sensitive input handling.
- Provide custom input mappings for different game genres or accessibility needs.

---

## Further Reading

- [Architecture Guide](architecture.md)
- [Quick Start Guide](quickstart.md)
- [Module Documentation](.)

For questions or suggestions, please open an issue or discussion on the project's repository.

