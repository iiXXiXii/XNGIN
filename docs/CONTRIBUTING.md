# Contributing to XNGIN

Thank you for your interest in contributing to XNGIN! We warmly welcome contributions of all types, from bug fixes and feature implementations to documentation improvements and architectural enhancements. This comprehensive guide will help you contribute effectively whilst maintaining the high standards that make XNGIN a robust and reliable game engine.

---

## Table of Contents

- [Getting Started](#getting-started)
- [Development Environment](#development-environment)
- [Coding Standards](#coding-standards)
- [Architecture Guidelines](#architecture-guidelines)
- [Testing Requirements](#testing-requirements)
- [Documentation Standards](#documentation-standards)
- [Submission Process](#submission-process)
- [Community Guidelines](#community-guidelines)

---

## Getting Started

### Prerequisites

Before contributing, ensure you have:

- **Java 24 or later** installed and configured
- **Git** for version control
- **IntelliJ IDEA** (recommended) or another modern Java IDE
- Basic understanding of **Gradle** build system
- Familiarity with **modular Java development**

### Initial Setup

1. **Fork the repository** on GitHub to create your own copy
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/your-username/XNGIN.git
   cd XNGIN
   ```
3. **Add the upstream remote**:
   ```bash
   git remote add upstream https://github.com/iiXXiXii/XNGIN.git
   ```
4. **Create a feature branch**:
   ```bash
   git checkout -b feature/descriptive-name
   ```

### Building the Project

Verify your environment is properly configured:

```bash
./gradlew build
```

This will compile all modules and run the complete test suite.

---

## Development Environment

### IDE Configuration

**IntelliJ IDEA Setup:**

1. Import the project as a Gradle project
2. Enable annotation processing: `Settings → Build → Compiler → Annotation Processors`
3. Configure code style: `Settings → Editor → Code Style → Java`
   - Use 4 spaces for indentation
   - Line length: 120 characters
   - Import order: static imports first, then regular imports alphabetically
4. Install recommended plugins:
   - Gradle
   - Git integration
   - EditorConfig support

**Code Formatting:**

The project includes an `.editorconfig` file. Ensure your IDE respects these settings:

```properties
root = true

[*]
charset = utf-8
end_of_line = lf
indent_style = space
indent_size = 4
insert_final_newline = true
trim_trailing_whitespace = true

[*.md]
trim_trailing_whitespace = false
```

### Development Workflow

1. **Stay current** with the main branch:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```
2. **Make incremental commits** with descriptive messages
3. **Test frequently** during development:
   ```bash
   ./gradlew :module-name:test
   ```
4. **Document as you go** - update relevant documentation files

---

## Coding Standards

### Java Conventions

**Naming Conventions:**

- **Classes**: PascalCase (e.g., `AudioManager`, `OpenGLRenderer`)
- **Methods**: camelCase (e.g., `processInput()`, `calculateDistance()`)
- **Variables**: camelCase (e.g., `deltaTime`, `playerPosition`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_ENTITIES`, `DEFAULT_WINDOW_WIDTH`)
- **Packages**: lowercase with dots (e.g., `com.xngin.renderer.gl`)

**Code Structure:**

- **Maximum line length**: 120 characters
- **Indentation**: 4 spaces (no tabs)
- **Braces**: Opening braces on the same line (K&R style)
- **Imports**: Group static imports first, then regular imports alphabetically

**Example Code Style:**

```java
package com.xngin.renderer.gl;

import static org.lwjgl.opengl.GL45.*;

import com.xngin.math.Matrix4;
import com.xngin.renderer.Shader;
import java.util.HashMap;
import java.util.Map;

/**
 * OpenGL implementation of the Shader interface.
 * Manages shader compilation, linking, and uniform binding.
 */
public class OpenGLShader implements Shader {

    private static final int INVALID_PROGRAM = -1;

    private final Map<String, Integer> uniformLocations;
    private int programId;

    public OpenGLShader() {
        this.uniformLocations = new HashMap<>();
        this.programId = INVALID_PROGRAM;
    }

    @Override
    public void setUniform(String name, Matrix4 matrix) {
        int location = getUniformLocation(name);
        if (location != -1) {
            glUniformMatrix4fv(location, false, matrix.toFloatBuffer());
        }
    }

    private int getUniformLocation(String name) {
        return uniformLocations.computeIfAbsent(name,
            key -> glGetUniformLocation(programId, key));
    }
}
```

### Documentation Standards

**Javadoc Requirements:**

All public APIs must include comprehensive Javadoc:

```java
/**
 * Represents a 3D transformation in space with position, rotation, and scale.
 * <p>
 * This class provides methods for manipulating transformations and applying them
 * to vectors and other geometric primitives. All operations maintain precision
 * and handle edge cases appropriately.
 * </p>
 * <p>
 * Example usage:
 * <pre>{@code
 * Transform transform = new Transform();
 * transform.setPosition(1.0f, 2.0f, 3.0f);
 * transform.setRotation(Quaternion.fromEuler(0, 45, 0));
 *
 * Vector3 worldPosition = transform.transformPoint(localPosition);
 * }</pre>
 * </p>
 *
 * @since 1.0
 * @see Matrix4
 * @see Quaternion
 */
public class Transform {

    /**
     * Sets the position component of this transformation.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @throws IllegalArgumentException if any coordinate is NaN or infinite
     */
    public void setPosition(float x, float y, float z) {
        // Implementation...
    }
}
```

### Language Usage

**British English Requirements:**

- Use British spelling throughout: "colour" not "color", "realise" not "realize"
- Documentation should be professional but approachable
- Use clear, concise language avoiding unnecessary jargon
- Include practical examples where appropriate

---

## Architecture Guidelines

### Module Design Principles

**API-First Development:**

- Design clean, minimal APIs before implementation
- Separate interface definitions from concrete implementations
- Use `*-api` modules for contracts, `*-impl` modules for implementations

**Dependency Management:**

- Follow the established module hierarchy
- API modules should have minimal dependencies
- Implementation modules depend only on their corresponding API
- Avoid circular dependencies between modules

**Service Discovery:**

- Use Java's `ServiceLoader` for runtime discovery
- Register implementations in `META-INF/services/` files
- Design for multiple implementations of the same service

### Design Patterns

**Commonly Used Patterns:**

- **Service Locator**: Central registry for engine services
- **Observer/Event System**: Loose coupling between modules
- **Factory Pattern**: Object creation with proper initialisation
- **Strategy Pattern**: Pluggable algorithms and implementations

**Anti-Patterns to Avoid:**

- Singleton abuse (prefer dependency injection)
- God classes (keep classes focused and cohesive)
- Tight coupling between modules
- Premature optimisation at the expense of clarity

---

## Testing Requirements

### Test Coverage Standards

**Minimum Requirements:**

- All public APIs must have unit tests
- Complex algorithms require comprehensive test coverage
- Integration tests for module interactions
- Performance tests for critical paths

**Test Structure:**

```java
class OpenGLShaderTest {

    private OpenGLShader shader;

    @BeforeEach
    void setUp() {
        // Initialise test environment
        shader = new OpenGLShader();
    }

    @Test
    @DisplayName("Should set uniform matrix correctly")
    void shouldSetUniformMatrix() {
        // Given
        Matrix4 testMatrix = Matrix4.identity();

        // When
        shader.setUniform("u_modelMatrix", testMatrix);

        // Then
        // Verify the uniform was set correctly
        assertThat(shader.getUniform("u_modelMatrix")).isEqualTo(testMatrix);
    }

    @Test
    @DisplayName("Should handle invalid uniform names gracefully")
    void shouldHandleInvalidUniformNames() {
        // When/Then
        assertDoesNotThrow(() -> shader.setUniform("invalid_uniform", Matrix4.identity()));
    }
}
```

### Performance Testing

Critical paths should include performance benchmarks:

```java
@Test
@PerformanceTest
void shouldRenderManyEntitiesEfficiently() {
    // Given
    List<Entity> entities = createTestEntities(10000);

    // When
    long startTime = System.nanoTime();
    renderer.renderEntities(entities);
    long endTime = System.nanoTime();

    // Then
    long renderTime = endTime - startTime;
    assertThat(renderTime).isLessThan(16_000_000); // 16ms for 60fps
}
```

---

## Documentation Standards

### Module Documentation

Each module requires comprehensive documentation following this template:

````markdown
# [Module Name] Documentation

Brief description of the module's purpose and responsibilities.

## Responsibilities

- Primary responsibility 1
- Primary responsibility 2
- Primary responsibility 3

## Key Concepts

### Concept 1

Detailed explanation with examples.

### Concept 2

Detailed explanation with examples.

## Example Usage

```java
// Practical code examples
```
````

## Extending the Module

Guidelines for extending functionality.

## Further Reading

- Links to related documentation
- External resources

```

### README Updates

When adding new features or modules:

1. Update the main README.md architecture section
2. Add the module to the appropriate category
3. Update any installation or setup instructions
4. Include the module in example configurations

---

## Submission Process

### Pull Request Guidelines

**Before Submitting:**
1. Ensure all tests pass: `./gradlew test`
2. Run code quality checks: `./gradlew check`
3. Update relevant documentation
4. Write clear commit messages following conventional commits

**Commit Message Format:**
```

type(scope): brief description

Detailed explanation of the changes made, why they were necessary,
and any important details for reviewers.

Fixes #123

````

Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

**Pull Request Template:**

```markdown
## Description
Brief description of the changes and their motivation.

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] All existing tests pass
- [ ] New tests added for new functionality
- [ ] Manual testing completed

## Documentation
- [ ] Documentation updated
- [ ] API documentation updated
- [ ] Examples updated if necessary

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-review of code completed
- [ ] Comments added for complex logic
- [ ] No unnecessary console.log statements
````

### Code Review Process

**Review Criteria:**

- **Functionality**: Does the code work as intended?
- **Architecture**: Does it follow XNGIN's design principles?
- **Performance**: Are there any performance implications?
- **Testing**: Is the code adequately tested?
- **Documentation**: Is the documentation clear and complete?

**Review Timeline:**

- Initial review within 48 hours
- Follow-up reviews within 24 hours
- Approval requires at least one maintainer review

---

## Community Guidelines

### Communication Standards

**Professional Conduct:**

- Be respectful and constructive in all interactions
- Provide specific, actionable feedback
- Ask questions when something is unclear
- Help others learn and improve

**Issue Reporting:**
When reporting bugs or requesting features:

1. **Search existing issues** to avoid duplicates
2. **Use appropriate templates** for consistency
3. **Provide detailed information** including:
   - Steps to reproduce (for bugs)
   - Expected vs. actual behaviour
   - System information and versions
   - Minimal code examples

**Discussion Participation:**

- Use GitHub Discussions for questions and general conversation
- Keep discussions focused and on-topic
- Share knowledge and experiences with the community
- Be patient with newcomers and those learning

### Recognition

**Contributors are recognised through:**

- GitHub contributor graphs and statistics
- Mentions in release notes for significant contributions
- Special recognition for outstanding contributions
- Opportunities to become maintainers for consistent contributors

---

## Additional Resources

### Learning Materials

**Understanding XNGIN:**

- [Architecture Guide](architecture.md) - Comprehensive system overview
- [Quick Start Guide](quickstart.md) - Getting started tutorial
- [Module Documentation](.) - Detailed module guides

**External References:**

- [Java Language Specification](https://docs.oracle.com/javase/specs/)
- [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html)
- [LWJGL Documentation](https://www.lwjgl.org/guide)

### Development Tools

**Recommended Tools:**

- **IntelliJ IDEA** - Primary IDE recommendation
- **Git** - Version control system
- **GitHub CLI** - Command-line GitHub interaction
- **JProfiler** - Performance profiling
- **SonarLint** - Code quality analysis

---

## Getting Help

**If you need assistance:**

1. **Check existing documentation** - Most questions are answered in the guides
2. **Search GitHub Issues** - Someone may have encountered similar problems
3. **Ask in GitHub Discussions** - Community members are happy to help
4. **Contact maintainers** - For complex architectural questions

**Contact Information:**

- GitHub Issues: Technical problems and bug reports
- GitHub Discussions: General questions and community interaction
- Email: [project-email] for sensitive or private matters

---

**Thank you for contributing to XNGIN! Your efforts help make game development more accessible and enjoyable for everyone.**
