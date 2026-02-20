package com.zenzer0s.kite.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zenzer0s.kite.ui.theme.GroupedListDefaults
import com.zenzer0s.kite.ui.theme.KiteCustomColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KiteSwitchItem(
    @StringRes title: Int,
    @StringRes summary: Int? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    enabled: Boolean = true
) {
    ListItem(
        headlineContent = {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = if (summary != null) {
            {
                Text(
                    text = stringResource(summary),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else null,
        leadingContent = {
            if (icon != null) {
                Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = stringResource(title)
                    )
                }
            }
        },
        trailingContent = {
            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                Switch(
                    checked = checked,
                    enabled = enabled,
                    onCheckedChange = onCheckedChange,
                    colors = KiteCustomColors.switchColors,
                    thumbContent = if (checked) {
                        {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        }
                    } else null
                )
            }
        },
        modifier = modifier.clickable(
            enabled = enabled,
            onClick = { onCheckedChange(!checked) }
        ),
        colors = GroupedListDefaults.listColors()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KiteSliderItem(
    @StringRes title: Int,
    @StringRes summary: Int? = null,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    enabled: Boolean = true,
    valueText: String? = null
) {
    ListItem(
        headlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (valueText != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                        Text(
                            text = valueText,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        },
        supportingContent = {
            Column(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                if (summary != null) {
                    Text(
                        text = stringResource(summary),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Slider(
                    value = value,
                    onValueChange = onValueChange,
                    valueRange = valueRange,
                    steps = steps,
                    enabled = enabled,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        },
        leadingContent = {
            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = stringResource(title)
                )
            }
        },
        modifier = modifier,
        colors = GroupedListDefaults.listColors()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitePreferenceItem(
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier,
    icon: Any? = null,
    enabled: Boolean = true,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = if (description != null) {
            {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        } else null,
        leadingContent = leadingContent ?: if (icon != null) {
            {
                Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                    when (icon) {
                        is Int -> Icon(painter = painterResource(icon), contentDescription = title)
                        is androidx.compose.ui.graphics.vector.ImageVector -> Icon(imageVector = icon, contentDescription = title)
                        is androidx.compose.ui.graphics.painter.Painter -> Icon(painter = icon, contentDescription = title)
                    }
                }
            }
        } else null,
        trailingContent = trailingContent,
        modifier = modifier.clickable(
            enabled = enabled,
            onClick = onClick
        ),
        colors = GroupedListDefaults.listColors()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitePreferenceSwitchItem(
    title: String,
    description: String? = null,
    checked: Boolean,
    modifier: Modifier = Modifier,
    icon: Any? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = if (description != null) {
            {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        } else null,
        leadingContent = {
            if (icon != null) {
                Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                    when (icon) {
                        is Int -> Icon(painter = painterResource(icon), contentDescription = title)
                        is androidx.compose.ui.graphics.vector.ImageVector -> Icon(imageVector = icon, contentDescription = title)
                        is androidx.compose.ui.graphics.painter.Painter -> Icon(painter = icon, contentDescription = title)
                    }
                }
            }
        },
        trailingContent = {
            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                Switch(
                    checked = checked,
                    enabled = enabled,
                    onCheckedChange = onCheckedChange,
                    colors = KiteCustomColors.switchColors,
                    thumbContent = if (checked) {
                        {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        }
                    } else null
                )
            }
        },
        modifier = modifier.clickable(
            enabled = enabled,
            onClick = {
                if (onClick != null) onClick()
                else onCheckedChange(!checked)
            }
        ),
        colors = GroupedListDefaults.listColors()
    )
}
