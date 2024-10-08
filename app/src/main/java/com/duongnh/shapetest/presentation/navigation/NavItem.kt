package com.duongnh.shapetest.presentation.navigation

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.duongnh.shapetest.R

sealed class NavItem(private val context: Context) {
    object Squares : Item(
        path = NavPath.SQUARES.toString(),
        titleSourceId = R.string.tab_squares,
        icon = Icons.Outlined.MailOutline
    )

    object Circles : Item(
        path = NavPath.CIRCLES.toString(),
        titleSourceId = R.string.tab_circles,
        icon = Icons.Outlined.AccountCircle
    )

    object Triangles : Item(
        path = NavPath.TRIANGLES.toString(),
        titleSourceId = R.string.tab_triangles,
        icon = Icons.Outlined.Warning
    )

    object All : Item(
        path = NavPath.ALL.toString(),
        titleSourceId = R.string.tab_all,
        icon = Icons.Outlined.Favorite
    )
}

enum class NavPath {
    SQUARES, CIRCLES, TRIANGLES, ALL
}

open class Item(val path: String, val titleSourceId: Int, val icon: ImageVector)