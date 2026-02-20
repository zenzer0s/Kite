package com.zenzer0s.kite.ui.page.settings.appearance

import android.os.Build
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import com.zenzer0s.kite.ui.theme.KiteCustomColors
import com.zenzer0s.kite.ui.theme.GroupedListDefaults
import com.zenzer0s.kite.ui.component.KitePreferenceSwitchItem
import com.zenzer0s.kite.ui.component.KitePreferenceItem
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.zenzer0s.kite.R
import com.zenzer0s.kite.ui.common.LocalDarkTheme
import com.zenzer0s.kite.ui.component.BackButton
import com.zenzer0s.kite.ui.component.PreferenceSingleChoiceItem
import com.zenzer0s.kite.ui.component.PreferenceSubtitle
import com.zenzer0s.kite.ui.component.PreferenceSwitchVariant
import com.zenzer0s.kite.util.DarkThemePreference.Companion.FOLLOW_SYSTEM
import com.zenzer0s.kite.util.DarkThemePreference.Companion.OFF
import com.zenzer0s.kite.util.DarkThemePreference.Companion.ON
import com.zenzer0s.kite.util.PreferenceUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DarkThemePreferences(onNavigateBack: () -> Unit) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState(),
            canScroll = { true },
        )
    val darkThemePreference = LocalDarkTheme.current
    val isHighContrastModeEnabled = darkThemePreference.isHighContrastModeEnabled
    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            LargeTopAppBar(
                colors = KiteCustomColors.topBarColors,
title = {
                    Text(modifier = Modifier, text = stringResource(id = R.string.dark_theme))
                },
                navigationIcon = { BackButton { onNavigateBack() } },
                scrollBehavior = scrollBehavior,
            )
        },
        content = {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(it), contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(GroupedListDefaults.VerticalSpacing)) {
                
                val supportsFollowSystem = Build.VERSION.SDK_INT >= 29
                val themeModeTotalItems = if (supportsFollowSystem) 3 else 2
                
                if (supportsFollowSystem) {
                    item {
                        KitePreferenceItem(
                            modifier = Modifier.clip(GroupedListDefaults.getShape(0, themeModeTotalItems)),
                            title = stringResource(R.string.follow_system),
                            trailingContent = {
                                androidx.compose.material3.RadioButton(
                                    selected = darkThemePreference.darkThemeValue == FOLLOW_SYSTEM,
                                    onClick = null
                                )
                            },
                        ) {
                            PreferenceUtil.modifyDarkThemePreference(FOLLOW_SYSTEM)
                        }
                    }
                }
                
                item {
                    val onIndex = if (supportsFollowSystem) 1 else 0
                    KitePreferenceItem(
                        modifier = Modifier.clip(GroupedListDefaults.getShape(onIndex, themeModeTotalItems)),
                        title = stringResource(R.string.on),
                        trailingContent = {
                            androidx.compose.material3.RadioButton(
                                selected = darkThemePreference.darkThemeValue == ON,
                                onClick = null
                            )
                        },
                    ) {
                        PreferenceUtil.modifyDarkThemePreference(ON)
                    }
                }
                
                item {
                    val offIndex = if (supportsFollowSystem) 2 else 1
                    KitePreferenceItem(
                        modifier = Modifier.clip(GroupedListDefaults.getShape(offIndex, themeModeTotalItems)),
                        title = stringResource(R.string.off),
                        trailingContent = {
                            androidx.compose.material3.RadioButton(
                                selected = darkThemePreference.darkThemeValue == OFF,
                                onClick = null
                            )
                        },
                    ) {
                        PreferenceUtil.modifyDarkThemePreference(OFF)
                    }
                }
                
                item { PreferenceSubtitle(text = stringResource(R.string.additional_settings)) }
                
                item {
                    KitePreferenceSwitchItem(
                        modifier = Modifier.clip(GroupedListDefaults.getShape(0, 1)),
                        title = stringResource(R.string.high_contrast),
                        icon = Icons.Outlined.Contrast,
                        checked = isHighContrastModeEnabled,
                        onCheckedChange = {
                            PreferenceUtil.modifyDarkThemePreference(
                                isHighContrastModeEnabled = !isHighContrastModeEnabled
                            )
                        },
                    )
                }
            }
        },
    )
}
