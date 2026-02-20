package com.zenzer0s.kite.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarScrollBehavior
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.zenzer0s.kite.R
import com.zenzer0s.kite.ui.common.Route
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FloatingNavToolbar(
    currentRoute: String?,
    scrollBehavior: FloatingToolbarScrollBehavior,
    onNavigate: (String) -> Unit,
    onDownload: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val containerColor = MaterialTheme.colorScheme.primaryContainer
    val onContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    val isAnyTabSelected = currentRoute in listOf(Route.HOME, Route.DOWNLOADS, Route.SETTINGS_PAGE)
    val spacing = if (isAnyTabSelected) 1.dp else 8.dp

    Row(
        modifier = modifier.padding(top = FloatingToolbarDefaults.ScreenOffset),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalFloatingToolbar(
            expanded = true,
            scrollBehavior = scrollBehavior,
            shape = CircleShape,
            colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(
                toolbarContainerColor = containerColor,
                toolbarContentColor = onContainerColor,
            ),
            modifier = Modifier
                .wrapContentWidth()
                .height(60.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = Color.Black.copy(alpha = 0.2f),
                    spotColor = Color.Black.copy(alpha = 0.3f),
                )
                .zIndex(1f),
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NavToolbarItem(
                    label = "Queue",
                    icon = Icons.Filled.Queue,
                    selected = currentRoute == Route.HOME,
                    primary = primaryColor,
                    onPrimary = onPrimaryColor,
                    onPrimaryContainer = onContainerColor,
                    onClick = {
                        scope.launch { haptic.performHapticFeedback(HapticFeedbackType.Confirm) }
                        onNavigate(Route.HOME)
                    },
                )
                NavToolbarItem(
                    label = stringResource(R.string.downloads_history),
                    icon = Icons.Filled.Download,
                    selected = currentRoute == Route.DOWNLOADS,
                    primary = primaryColor,
                    onPrimary = onPrimaryColor,
                    onPrimaryContainer = onContainerColor,
                    onClick = {
                        scope.launch { haptic.performHapticFeedback(HapticFeedbackType.Confirm) }
                        onNavigate(Route.DOWNLOADS)
                    },
                )
                NavToolbarItem(
                    label = stringResource(R.string.settings),
                    icon = Icons.Filled.Settings,
                    selected = currentRoute == Route.SETTINGS_PAGE,
                    primary = primaryColor,
                    onPrimary = onPrimaryColor,
                    onPrimaryContainer = onContainerColor,
                    onClick = {
                        scope.launch { haptic.performHapticFeedback(HapticFeedbackType.Confirm) }
                        onNavigate(Route.SETTINGS_PAGE)
                    },
                )
            }
        }

        if (onDownload != null) {
            FilledIconButton(
                onClick = {
                    scope.launch { haptic.performHapticFeedback(HapticFeedbackType.Confirm) }
                    onDownload()
                },
                modifier = Modifier
                    .size(50.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = Color.Black.copy(alpha = 0.2f),
                        spotColor = Color.Black.copy(alpha = 0.3f),
                    )
                    .zIndex(1f),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = primaryColor,
                    contentColor = onPrimaryColor,
                ),
            ) {
                Icon(
                    imageVector = Icons.Filled.Download,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun NavToolbarItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    primary: Color,
    onPrimary: Color,
    onPrimaryContainer: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ToggleButton(
        checked = selected,
        onCheckedChange = { onClick() },
        contentPadding = PaddingValues(0.dp),
        colors = ToggleButtonDefaults.toggleButtonColors(
            containerColor = Color.Transparent,
            contentColor = onPrimaryContainer,
            checkedContainerColor = primary,
            checkedContentColor = onPrimary,
        ),
        shapes = ToggleButtonDefaults.shapes(CircleShape, CircleShape, CircleShape),
        modifier = modifier
            .height(40.dp)
            .wrapContentWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (selected) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .size(18.dp),
                )
            }
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                lineHeight = 20.sp,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
                textAlign = TextAlign.Center,
            )
        }
    }
}