# Engine Math Module Documentation

The `engine-math` module provides essential mathematical utilities for XNGIN, supporting graphics, physics, and general game logic. This module is designed for performance, accuracy, and ease of use, and can be used independently or alongside third-party libraries such as JOML.

---

## Responsibilities

- **Vectors and Matrices**: Implements common vector (2D, 3D, 4D) and matrix (2x2, 3x3, 4x4) types and operations.
- **Transforms**: Provides transformation utilities for position, rotation, and scaling in both 2D and 3D.
- **Math Utilities**: Supplies functions for interpolation, angle conversion, trigonometry, and more.
- **Precision and Performance**: Designed to minimise floating-point errors and maximise efficiency.

---

## Key Concepts

### Vectors
- Support for basic arithmetic (addition, subtraction, scaling).
- Dot and cross products.
- Normalisation and length calculations.

### Matrices
- Construction of identity, translation, rotation, and scaling matrices.
- Matrix multiplication and inversion.
- Useful for camera, projection, and model transformations.

### Transforms
- Combine position, rotation, and scale into a single structure.
- Apply transforms to vectors and matrices for scene manipulation.

---

## Example Usage

```java
import com.xngin.math.Vector3;

Vector3 position = new Vector3(1.0f, 2.0f, 3.0f);
Vector3 direction = position.normalised();
float distance = position.length();
```

---

## Extending the Module

- Add new mathematical types or utilities as needed for your game or engine features.
- Ensure all new code is well-tested and documented.
- Consider performance implications for real-time applications.

---

## Further Reading

- [Architecture Guide](architecture.md)
- [Quick Start Guide](quickstart.md)
- [Module Documentation](.)

For questions or suggestions, please open an issue or discussion on the project's repository.

