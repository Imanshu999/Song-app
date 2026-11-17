package com.sonora.modhub.ui.components

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathParser

/**
 * The Sonora mark as path data in the launcher icon's 108 by 108 space,
 * the same shape as the monochrome/notification mark, for drawing it
 * at any size (the opening animation zooms into it).
 */
internal const val MARK_VIEWPORT = 108f

internal const val MARK_PATH_DATA: String =
    "M29,63V45a4,4 0,0 1,8 0v18a4,4 0,0 1,-8 0z " +
        "M43,73V35a4,4 0,0 1,8 0v38a4,4 0,0 1,-8 0z " +
        "M57,80V28a4,4 0,0 1,8 0v52a4,4 0,0 1,-8 0z " +
        "M71,66V42a4,4 0,0 1,8 0v24a4,4 0,0 1,-8 0z"

/** The mark as a [Path] in [MARK_VIEWPORT] units. */
internal fun markPath(): Path = PathParser().parsePathString(MARK_PATH_DATA).toPath()
