# Engine Platform LWJGL Module Documentation

The `engine-platform-lwjgl` module provides a desktop platform implementation for XNGIN using LWJGL. It implements the interfaces defined in `engine-platform-api`, delivering robust window management, input handling, and system services for desktop environments.

---

## Responsibilities

- **LWJGL Integration**: Uses LWJGL to provide access to GLFW (windowing/input), Vulkan/OpenGL (graphics), and other native libraries.
- **Window Management**: Creates and manages application windows, supporting features such as resizing, fullscreen, and V-Sync.
- **Input Handling**: Captures keyboard, mouse, and (optionally) gamepad input, forwarding events to the engine's input system.
- **System Services**: Provides high-resolution timers, clipboard access, and other desktop utilities.
- **Cross-Module Integration**: Works seamlessly with rendering, input, and audio modules for a complete platform solution.

---

## Key Concepts

### GLFW Windowing
- Initialises and manages GLFW windows and event loops.
- Handles window events (resize, focus, close, etc.).

### Input Devices
- Captures and processes keyboard and mouse events.
- (Planned) Support for gamepads and other input devices.

### System Utilities
- Provides access to timers, clipboard, and other OS features via LWJGL.

---

## Example Usage

```java
import com.xngin.platform.lwjgl.LwjglPlatform;

LwjglPlatform platform = new LwjglPlatform();
platform.createWindow("XNGIN Game", 1280, 720);
platform.pollEvents();
```

---

## Extending the Module

- Add support for new input devices or window features as needed.
- Optimise for specific desktop environments or hardware.
- Integrate with other modules for advanced platform features.

---

## Further Reading

- [LWJGL Documentation](https://www.lwjgl.org/guide)
- [GLFW Documentation](https://www.glfw.org/docs/latest/)
- [Architecture Guide](architecture.md)
- [Module Documentation](.)

For questions or suggestions, please open an issue or discussion on the project's repository.
