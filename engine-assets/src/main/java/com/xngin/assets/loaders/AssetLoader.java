package com.xngin.assets.loaders;

import com.xngin.assets.Asset;

/**
 * Base interface for asset loaders that handle specific file formats.
 * Asset loaders are responsible for reading files from disk or other sources
 * and converting them into engine-compatible asset objects. Each loader
 * typically handles one or more related file formats.
 *
 * @param <T> the type of asset this loader creates
 */
public interface AssetLoader<T extends Asset> {
}
