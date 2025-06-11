package com.xngin.assets;

/**
 * Represents a reference to an asset that may or may not be loaded.
 * Asset handles provide a way to reference assets without immediately loading them,
 * supporting lazy loading and unloading for memory management. They also enable
 * hot-reloading of assets during development.
 *
 * @param <T> the type of asset this handle references
 */
public class AssetHandle<T extends Asset> {
}
