package com.zenzer0s.kite.ui.page.settings.about

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import com.zenzer0s.kite.ui.theme.KiteCustomColors
import com.zenzer0s.kite.ui.theme.GroupedListDefaults
import com.zenzer0s.kite.ui.component.KitePreferenceSwitchItem
import com.zenzer0s.kite.ui.component.KitePreferenceItem
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material.icons.outlined.UpdateDisabled
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.zenzer0s.kite.App
import com.zenzer0s.kite.App.Companion.packageInfo
import com.zenzer0s.kite.R
import com.zenzer0s.kite.ui.component.BackButton
import com.zenzer0s.kite.ui.component.ConfirmButton
import com.zenzer0s.kite.ui.component.PreferenceItem
import com.zenzer0s.kite.ui.component.PreferenceSwitchWithDivider
import com.zenzer0s.kite.util.AUTO_UPDATE
import com.zenzer0s.kite.util.PreferenceUtil
import com.zenzer0s.kite.util.ToastUtil

private const val releaseURL = "https://github.com/zenzer0s/Kite/releases"
private const val repoUrl = "https://github.com/zenzer0s/Kite"
const val YtdlpRepository = "https://github.com/yt-dlp/yt-dlp"
private const val githubIssueUrl = "https://github.com/zenzer0s/Kite/issues"
private const val telegramChannelUrl = "https://t.me/zen0saospforge"
private const val TAG = "AboutPage"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutPage(
    onNavigateBack: () -> Unit,
    onNavigateToCreditsPage: () -> Unit,
    onNavigateToUpdatePage: () -> Unit,
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState(),
            canScroll = { true },
        )
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    //    val configuration = LocalConfiguration.current
    //    val screenDensity = configuration.densityDpi / 160f
    //    val screenHeight = (configuration.screenHeightDp.toFloat() * screenDensity).roundToInt()
    //    val screenWidth = (configuration.screenWidthDp.toFloat() * screenDensity).roundToInt()
    var isAutoUpdateEnabled by remember { mutableStateOf(PreferenceUtil.isAutoUpdateEnabled()) }

    val info = App.getVersionReport()
    val versionName = packageInfo.versionName

    //        infoBuilder.append("App version: $versionName ($versionCode)\n")
    //            .append("Device information: Android $release (API ${Build.VERSION.SDK_INT})\n")
    //            .append("Supported ABIs: ${Build.SUPPORTED_ABIS.contentToString()}\n")
    //            .append("\nScreen resolution: $screenHeight x $screenWidth")
    //            .append("Yt-dlp Version:
    // ${YoutubeDL.version(context.applicationContext)}").toString()

    val uriHandler = LocalUriHandler.current
    fun openUrl(url: String) {
        uriHandler.openUri(url)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            LargeTopAppBar(
                colors = KiteCustomColors.topBarColors,
title = { Text(modifier = Modifier, text = stringResource(id = R.string.about)) },
                navigationIcon = { BackButton { onNavigateBack() } },
                scrollBehavior = scrollBehavior,
            )
        },
        content = {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(it), contentPadding = PaddingValues(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(GroupedListDefaults.VerticalSpacing)) {
                item {
                    KitePreferenceItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(0, 6)),
                        title = stringResource(R.string.readme),
                        description = stringResource(R.string.readme_desc),
                        icon = Icons.Outlined.Description,
                    ) {
                        openUrl(repoUrl)
                    }
                }
                item {
                    KitePreferenceItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(1, 6)),
                        title = stringResource(R.string.release),
                        description = stringResource(R.string.release_desc),
                        icon = Icons.Outlined.NewReleases,
                    ) {
                        openUrl(releaseURL)
                    }
                }
                item {
                    KitePreferenceItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(2, 6)),
                        title = stringResource(R.string.telegram_channel),
                        description = telegramChannelUrl,
                        icon = painterResource(id = R.drawable.icons8_telegram_app),
                    ) {
                        openUrl(telegramChannelUrl)
                    }
                }
                item {
                    KitePreferenceItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(3, 6)),
                        title = stringResource(id = R.string.credits),
                        description = stringResource(id = R.string.credits_desc),
                        icon = Icons.Outlined.AutoAwesome,
                    ) {
                        onNavigateToCreditsPage()
                    }
                }
                item {
                    KitePreferenceSwitchItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(4, 6)),
                        title = stringResource(R.string.auto_update),
                        description = stringResource(R.string.check_for_updates_desc),
                        icon =
                            if (isAutoUpdateEnabled) Icons.Outlined.Update
                            else Icons.Outlined.UpdateDisabled,
                        checked = isAutoUpdateEnabled,
                        enabled = !App.isFDroidBuild(),
                        onClick = onNavigateToUpdatePage,
                        onCheckedChange = {
                            isAutoUpdateEnabled = !isAutoUpdateEnabled
                            PreferenceUtil.updateValue(AUTO_UPDATE, isAutoUpdateEnabled)
                        },
                    )
                }
                item {
                    KitePreferenceItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(5, 6)),
                        title = stringResource(R.string.version),
                        description = versionName,
                        icon = Icons.Outlined.Info,
                    ) {
                        clipboardManager.setText(AnnotatedString(info))
                        ToastUtil.makeToast(R.string.info_copied)
                    }
                }
                item {
                    KitePreferenceItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(0, 1)),title = "Package name", description = context.packageName) {
                        clipboardManager.setText(AnnotatedString(context.packageName))
                        ToastUtil.makeToast(R.string.info_copied)
                    }
                }
            }
        },
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
@Preview
fun AutoUpdateUnavailableDialog(onDismissRequest: () -> Unit = {}) {
    val uriHandler = LocalUriHandler.current
    val hapticFeedback = LocalHapticFeedback.current
    val hyperLinkText = stringResource(id = R.string.switch_to_github_builds)
    val text = stringResource(id = R.string.auto_update_disabled_msg, "F-Droid", hyperLinkText)

    val annotatedString = buildAnnotatedString {
        append(text)
        val startIndex = text.indexOf(hyperLinkText)
        val endIndex = startIndex + hyperLinkText.length
        addUrlAnnotation(
            UrlAnnotation("https://github.com/zenzer0s/Kite/releases/latest"),
            start = startIndex,
            end = endIndex,
        )
        addStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.tertiary,
                textDecoration = TextDecoration.Underline,
            ),
            start = startIndex,
            end = endIndex,
        )
    }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            ConfirmButton(stringResource(id = R.string.got_it)) { onDismissRequest() }
        },
        icon = { Icon(Icons.Outlined.UpdateDisabled, null) },
        title = {
            Text(
                text = stringResource(id = R.string.feature_unavailable),
                textAlign = TextAlign.Center,
            )
        },
        text = {
            ClickableText(
                text = annotatedString,
                onClick = { index ->
                    annotatedString.getUrlAnnotations(index, index).firstOrNull()?.let {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        uriHandler.openUri(it.item.url)
                    }
                },
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        MaterialTheme.colorScheme.onSurfaceVariant
                    ),
            )
        },
    )
}
