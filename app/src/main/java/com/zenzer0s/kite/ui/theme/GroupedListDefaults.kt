package com.zenzer0s.kite.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.ListItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object GroupedListDefaults {

    val VerticalSpacing: Dp = 3.dp

    @Composable
    fun listColors(): ListItemColors =
        KiteCustomColors.listItemColors

    @Composable
    fun getShape(index: Int, totalItems: Int): CornerBasedShape {
        return when {
            totalItems == 1 -> KiteShapeDefaults.cardShape
            index == 0 -> KiteShapeDefaults.topListItemShape
            index == totalItems - 1 -> KiteShapeDefaults.bottomListItemShape
            else -> KiteShapeDefaults.middleListItemShape
        }
    }
}
