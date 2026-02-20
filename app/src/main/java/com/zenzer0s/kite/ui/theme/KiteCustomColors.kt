package com.zenzer0s.kite.ui.theme

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

object KiteCustomColors {
    var black = false

    @OptIn(ExperimentalMaterial3Api::class)
    val topBarColors: TopAppBarColors
        @Composable get() =
            TopAppBarDefaults.topAppBarColors(
                containerColor = if (!black) colorScheme.surfaceContainer else colorScheme.surface,
                scrolledContainerColor = if (!black) colorScheme.surfaceContainer else colorScheme.surface
            )

    val listItemColors: ListItemColors
        @Composable get() =
            ListItemDefaults.colors(containerColor = if (!black) colorScheme.surfaceBright else colorScheme.surfaceContainerHigh)

    val selectedListItemColors: ListItemColors
        @Composable get() =
            ListItemDefaults.colors(
                containerColor = colorScheme.secondaryContainer,
                headlineColor = colorScheme.secondary,
                leadingIconColor = colorScheme.onSecondaryContainer,
                supportingColor = colorScheme.onSecondaryContainer,
                trailingIconColor = colorScheme.onSecondaryContainer
            )

    val switchColors: SwitchColors
        @Composable get() = SwitchDefaults.colors(
            checkedIconColor = colorScheme.primary,
        )
}
