package com.zenzer0s.kite.ui.page.settings.appearance

import android.content.Intent
import androidx.compose.ui.draw.clip
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.zenzer0s.kite.R
import com.zenzer0s.kite.ui.component.BackButton
import com.zenzer0s.kite.ui.component.PreferenceSingleChoiceItem
import com.zenzer0s.kite.ui.component.PreferenceSubtitle
import com.zenzer0s.kite.ui.component.PreferencesHintCard
import com.zenzer0s.kite.ui.theme.KiteTheme
import com.zenzer0s.kite.util.LocaleLanguageCodeMap
import com.zenzer0s.kite.util.PreferenceUtil
import com.zenzer0s.kite.util.setLanguage
import com.zenzer0s.kite.util.toDisplayName
import java.util.Locale

@Composable
fun LanguageBottomSheet(onDismissRequest: () -> Unit = {}) {
    val selectedLocale by remember { mutableStateOf(Locale.getDefault()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val intent =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(Settings.ACTION_APP_LOCALE_SETTINGS).apply {
                val uri = Uri.fromParts("package", context.packageName, null)
                data = uri
            }
        } else {
            Intent()
        }

    val preferredLocales = remember {
        val defaultLocaleListCompat = LocaleListCompat.getDefault()
        val mLocaleSet = mutableSetOf<Locale>()

        for (index in 0..defaultLocaleListCompat.size()) {
            val locale = defaultLocaleListCompat[index]
            if (locale != null) {
                mLocaleSet.add(locale)
            }
        }

        return@remember mLocaleSet
    }

    val supportedLocales = LocaleLanguageCodeMap.keys

    val suggestedLocales =
        remember(preferredLocales) {
            val localeSet = mutableSetOf<Locale>()

            preferredLocales.forEach { desired ->
                val matchedLocale =
                    supportedLocales.firstOrNull { supported ->
                        LocaleListCompat.matchesLanguageAndScript(
                            /* supported = */ desired,
                            /* desired = */ supported,
                        )
                    }
                if (matchedLocale != null) {
                    localeSet.add(matchedLocale)
                }
            }

            return@remember localeSet
        }

    val otherLocales = supportedLocales - suggestedLocales

    val isSystemLocaleSettingsAvailable =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_ALL)
                .isNotEmpty()
        } else {
            false
        }
    LanguageBottomSheetImpl(
        onDismissRequest = onDismissRequest,
        suggestedLocales = suggestedLocales,
        otherLocales = otherLocales,
        isSystemLocaleSettingsAvailable = isSystemLocaleSettingsAvailable,
        onNavigateToSystemLocaleSettings = {
            if (isSystemLocaleSettingsAvailable) {
                context.startActivity(intent)
            }
        },
        selectedLocale = selectedLocale,
    ) {
        PreferenceUtil.saveLocalePreference(it)
        setLanguage(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageBottomSheetImpl(
    onDismissRequest: () -> Unit = {},
    suggestedLocales: Set<Locale>,
    otherLocales: Set<Locale>,
    isSystemLocaleSettingsAvailable: Boolean = false,
    onNavigateToSystemLocaleSettings: () -> Unit,
    selectedLocale: Locale,
    onLanguageSelected: (Locale?) -> Unit = {},
) {
    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
    com.zenzer0s.kite.ui.component.SealModalBottomSheet(
        onDismissRequest = onDismissRequest,
        contentPadding = PaddingValues(0.dp),
    ) {
        com.zenzer0s.kite.ui.component.DrawerSheetSubtitle(text = stringResource(R.string.language), modifier = Modifier.padding(horizontal = 16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f, fill = false),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(com.zenzer0s.kite.ui.theme.GroupedListDefaults.VerticalSpacing)
        ) {
            if (suggestedLocales.isNotEmpty()) {
                item { PreferenceSubtitle(text = stringResource(id = R.string.suggested)) }
                
                val includeFollowSystem = !suggestedLocales.contains(Locale.getDefault())
                val suggestedCount = suggestedLocales.size + (if (includeFollowSystem) 1 else 0)
                
                if (includeFollowSystem) {
                    item {
                        com.zenzer0s.kite.ui.component.KitePreferenceItem(
                            modifier = Modifier.clip(com.zenzer0s.kite.ui.theme.GroupedListDefaults.getShape(0, suggestedCount)),
                            title = stringResource(id = R.string.follow_system),
                            trailingContent = {
                                androidx.compose.material3.RadioButton(
                                    selected = !suggestedLocales.contains(selectedLocale),
                                    onClick = null
                                )
                            }
                        ) {
                            onLanguageSelected(null)
                        }
                    }
                }

                suggestedLocales.forEachIndexed { index, locale ->
                    val actualIndex = index + if (includeFollowSystem) 1 else 0
                    item {
                        com.zenzer0s.kite.ui.component.KitePreferenceItem(
                            modifier = Modifier.clip(com.zenzer0s.kite.ui.theme.GroupedListDefaults.getShape(actualIndex, suggestedCount)),
                            title = locale.toDisplayName(),
                            trailingContent = {
                                androidx.compose.material3.RadioButton(
                                    selected = selectedLocale == locale,
                                    onClick = null
                                )
                            }
                        ) {
                            onLanguageSelected(locale)
                        }
                    }
                }
            }

            if (otherLocales.isNotEmpty()) {
                item { PreferenceSubtitle(text = stringResource(id = R.string.all_languages)) }
                
                val otherCount = otherLocales.size + if (isSystemLocaleSettingsAvailable) 1 else 0
                
                otherLocales.forEachIndexed { index, locale ->
                    item {
                        com.zenzer0s.kite.ui.component.KitePreferenceItem(
                            modifier = Modifier.clip(com.zenzer0s.kite.ui.theme.GroupedListDefaults.getShape(index, otherCount)),
                            title = locale.toDisplayName(),
                            trailingContent = {
                                androidx.compose.material3.RadioButton(
                                    selected = selectedLocale == locale,
                                    onClick = null
                                )
                            }
                        ) {
                            onLanguageSelected(locale)
                        }
                    }
                }
                
                if (isSystemLocaleSettingsAvailable) {
                    item {
                        com.zenzer0s.kite.ui.component.KitePreferenceItem(
                            modifier = Modifier.clip(com.zenzer0s.kite.ui.theme.GroupedListDefaults.getShape(otherLocales.size, otherCount)),
                            title = stringResource(R.string.system_settings),
                            trailingContent = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        ) {
                            onNavigateToSystemLocaleSettings()
                        }
                    }
                }
            }
        }
    }
}
