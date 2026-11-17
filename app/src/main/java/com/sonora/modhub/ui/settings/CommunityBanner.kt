package com.sonora.modhub.ui.settings

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

/**
 * Developer Instagram banner. The whole row is the button: it fills its slot in
 * the settings card edge to edge, so the card's own rounded corners shape it.
 * Pass a clip in [modifier] where it stands alone.
 */
@Composable
internal fun CommunityBanner(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Row(
        modifier
            .fillMaxWidth()
            .testTag("follow_developer_banner")
            .background(INSTAGRAM_GRADIENT)
            .clickable {
                runCatching {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, com.sonora.modhub.ui.components.Support.DEVELOPER_INSTAGRAM.toUri()),
                    )
                }
            }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(instagramLogo, null, Modifier.size(26.dp), tint = Color.White)
        Box(Modifier.width(26.dp))
        Column(Modifier.weight(1f)) {
            Text("Follow Developer", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = Color.White)
            Text(
                "Updates, previews and behind the scenes on Instagram",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.86f),
                modifier = Modifier.padding(top = 2.dp),
            )
        }
        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null, tint = Color.White, modifier = Modifier.padding(start = 12.dp))
    }
}

internal val INSTAGRAM_ACCENT = Color(0xFFC13584)
private val INSTAGRAM_GRADIENT = Brush.horizontalGradient(
    listOf(Color(0xFF833AB4), Color(0xFFC13584), Color(0xFFE1306C)),
)

internal val instagramLogo: ImageVector by lazy {
    ImageVector.Builder(name = "Instagram", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f)
        .apply {
            addPath(
                pathData = PathParser().parsePathString(INSTAGRAM_PATH).toNodes(),
                fill = SolidColor(Color.White),
            )
        }
        .build()
}

private const val INSTAGRAM_PATH =
    "M7.8,2h8.4C19.4,2 22,4.6 22,7.8v8.4a5.8,5.8 0 0,1 -5.8,5.8H7.8C4.6,22 2,19.4 2,16.2V7.8A5.8,5.8 0 0,1 7.8,2m-.2,2A3.6,3.6 0 0,0 4,7.6v8.8C4,18.39 5.61,20 7.6,20h8.8a3.6,3.6 0 0,0 3.6,-3.6V7.6C20,5.61 18.39,4 16.4,4H7.6m9.65,1.5a1.25,1.25 0 0,1 1.25,1.25A1.25,1.25 0 0,1 17.25,8 1.25,1.25 0 0,1 16,6.75a1.25,1.25 0 0,1 1.25,-1.25M12,7a5,5 0 0,1 5,5 5,5 0 0,1 -5,5 5,5 0 0,1 -5,-5 5,5 0 0,1 5,-5m0,2a3,3 0 0,0 -3,3 3,3 0 0,0 3,3 3,3 0 0,0 3,-3 3,3 0 0,0 -3,-3Z"
