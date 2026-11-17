package com.sonora.modhub.playback

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class CarProviderGuardTest {

    private class FakeCarConnectionProvider : ContentProvider() {
        override fun onCreate(): Boolean = true
        override fun query(
            uri: Uri,
            projection: Array<out String>?,
            selection: String?,
            selectionArgs: Array<out String>?,
            sortOrder: String?,
        ): Cursor = MatrixCursor(arrayOf("CarConnectionState")).apply { addRow(arrayOf(0)) }

        override fun getType(uri: Uri): String? = null
        override fun insert(uri: Uri, values: ContentValues?): Uri? = null
        override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
        override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
    }

    @Test
    fun skipsCarConnectionQueryWhenProviderMissing() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        CarProviderGuard.init(context)
        val uri = Uri.parse("content://androidx.car.app.connection")

        assertTrue(CarProviderGuard.shouldSkipQuery(uri, context))
        assertNull(CarProviderGuard.queryIfProviderExists(context.contentResolver, uri, null, null, null, null))
        assertNull(
            CarProviderGuard.queryIfProviderExists(
                context.contentResolver,
                uri,
                null,
                null,
                null,
                null,
                CancellationSignal(),
            ),
        )
        assertNull(
            CarProviderGuard.queryIfProviderExists(
                context.contentResolver,
                uri,
                null,
                Bundle(),
                CancellationSignal(),
            ),
        )
    }

    @Test
    fun doesNotSkipOtherAuthorities() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        CarProviderGuard.init(context)
        assertFalse(CarProviderGuard.shouldSkipQuery(Uri.parse("content://media/external/audio/media"), context))
    }

    @Test
    fun delegatesQueryWhenCarConnectionProviderExists() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        CarProviderGuard.init(context)
        val info = android.content.pm.ProviderInfo().apply {
            authority = "androidx.car.app.connection"
            packageName = context.packageName
            name = FakeCarConnectionProvider::class.java.name
        }
        org.robolectric.Shadows.shadowOf(context.packageManager).addOrUpdateProvider(info)
        Robolectric.setupContentProvider(FakeCarConnectionProvider::class.java, "androidx.car.app.connection")
        val uri = Uri.parse("content://androidx.car.app.connection")

        assertFalse(CarProviderGuard.shouldSkipQuery(uri, context))
        val cursor = CarProviderGuard.queryIfProviderExists(context.contentResolver, uri, null, null, null, null)
        assertNotNull(cursor)
        cursor!!.use {
            assertEquals(1, it.count)
        }
    }
}
