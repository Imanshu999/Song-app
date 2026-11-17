package com.sonora.modhub.playback

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal

/**
 * Guards Media3's Android Auto connection query against missing providers.
 *
 * Media3's `AndroidAutoConnectionStateObserver` unconditionally queries
 * `content://androidx.car.app.connection` on API 29+ even when Android Auto
 * is not installed on the device, causing Android's `ActivityThread` to log
 * `Failed to find provider info for androidx.car.app.connection`.
 *
 * When a device genuinely has the provider installed, calls pass straight
 * through to [ContentResolver.query] untouched.
 */
object CarProviderGuard {
    private const val CAR_CONNECTION_AUTHORITY = "androidx.car.app.connection"

    @Volatile
    private var appContext: Context? = null

    @JvmStatic
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    internal fun shouldSkipQuery(uri: Uri?, context: Context? = appContext): Boolean {
        if (uri?.authority != CAR_CONNECTION_AUTHORITY) return false
        val ctx = context ?: return false
        return runCatching {
            ctx.packageManager.resolveContentProvider(CAR_CONNECTION_AUTHORITY, 0) == null
        }.getOrDefault(false)
    }

    @JvmStatic
    fun queryIfProviderExists(
        resolver: ContentResolver,
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?,
    ): Cursor? {
        if (shouldSkipQuery(uri)) return null
        return resolver.query(uri, projection, selection, selectionArgs, sortOrder)
    }

    @JvmStatic
    fun queryIfProviderExists(
        resolver: ContentResolver,
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?,
        cancellationSignal: CancellationSignal?,
    ): Cursor? {
        if (shouldSkipQuery(uri)) return null
        return resolver.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal)
    }

    @JvmStatic
    fun queryIfProviderExists(
        resolver: ContentResolver,
        uri: Uri,
        projection: Array<String>?,
        queryArgs: Bundle?,
        cancellationSignal: CancellationSignal?,
    ): Cursor? {
        if (shouldSkipQuery(uri)) return null
        return resolver.query(uri, projection, queryArgs, cancellationSignal)
    }
}
