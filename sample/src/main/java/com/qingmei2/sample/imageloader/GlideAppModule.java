package com.qingmei2.sample.imageloader;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.qingmei2.rximagepicker.core.RxImagePicker;

/**
 * <p>In the {@link Glide} V4, it provides the {@link AppGlideModule} and the
 * {@link com.bumptech.glide.module.LibraryGlideModule} for delelopers using.
 * </p>
 * <p>The developers should create {@link AppGlideModule} in their own project, and the
 * {@link RxImagePicker} library also provides {@link com.bumptech.glide.module.LibraryGlideModule}
 * to prevent Glide's dependency conflicts.
 * </p>
 *
 * @see com.bumptech.glide.module.LibraryGlideModule
 * @see AppGlideModule
 */
@com.bumptech.glide.annotation.GlideModule
public class GlideAppModule extends AppGlideModule {

    /**
     * Using the @GlideModule annotation requires a dependency on Glide’s annotations:
     */
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context,
                diskCacheFolderName(context),
                diskCacheSizeBytes()))
                .setMemoryCache(new LruResourceCache(memoryCacheSizeBytes()));
    }

    /**
     * Implementations should return {@code false} after they and their dependencies have migrated
     * to Glide's annotation processor.
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    /**
     * set the memory cache size, unit is the {@link Byte}.
     */
    private int memoryCacheSizeBytes() {
        return 1024 * 1024 * 20; // 20 MB
    }

    /**
     * set the disk cache size, unit is the {@link Byte}.
     */
    private int diskCacheSizeBytes() {
        return 1024 * 1024 * 512; // 512 MB
    }

    /**
     * set the disk cache folder's name.
     */
    private String diskCacheFolderName(Context context) {
        return ContextCompat.getCodeCacheDir(context).getPath() + "/rximagepicker";
    }

}
