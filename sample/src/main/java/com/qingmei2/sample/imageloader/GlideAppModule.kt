package com.qingmei2.sample.imageloader

import android.content.Context
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.qingmei2.rximagepicker.core.RxImagePicker

/**
 *
 * In the [Glide] V4, it provides the [AppGlideModule] and the
 * [com.bumptech.glide.module.LibraryGlideModule] for delelopers using.
 *
 *
 * The developers should create [AppGlideModule] in their own project, and the
 * [RxImagePicker] library also provides [com.bumptech.glide.module.LibraryGlideModule]
 * to prevent Glide's dependency conflicts.
 *
 *
 * @see com.bumptech.glide.module.LibraryGlideModule
 *
 * @see AppGlideModule
 */
@GlideModule
class GlideAppModule : AppGlideModule() {

    /**
     * Implementations should return `false` after they and their dependencies have migrated
     * to Glide's annotation processor.
     */
    override fun isManifestParsingEnabled(): Boolean = false

    /**
     * Using the @GlideModule annotation requires a dependency on Glide’s annotations:
     */
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {

    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context,
                diskCacheFolderName(context),
                diskCacheSizeBytes()))
                .setMemoryCache(LruResourceCache(memoryCacheSizeBytes()))
    }

    /**
     * set the memory cache size, unit is the [Byte].
     */
    private fun memoryCacheSizeBytes(): Long {
        return 1024 * 1024 * 20 // 20 MB
    }

    /**
     * set the disk cache size, unit is the [Byte].
     */
    private fun diskCacheSizeBytes(): Long {
        return 1024 * 1024 * 512 // 512 MB
    }

    /**
     * set the disk cache folder's name.
     */
    private fun diskCacheFolderName(context: Context): String {
        return ContextCompat.getCodeCacheDir(context).path + "/rximagepicker"
    }

}
