package com.zenzer0s.kite.ui.page.settings.troubleshooting

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import com.zenzer0s.kite.ui.theme.KiteCustomColors
import com.zenzer0s.kite.ui.theme.GroupedListDefaults
import com.zenzer0s.kite.ui.component.KitePreferenceSwitchItem
import com.zenzer0s.kite.ui.component.KitePreferenceItem
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Cookie
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Spellcheck
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zenzer0s.kite.App
import com.zenzer0s.kite.R
import com.zenzer0s.kite.ui.common.Route
import com.zenzer0s.kite.ui.common.booleanState
import com.zenzer0s.kite.ui.component.PreferenceInfo
import com.zenzer0s.kite.ui.component.PreferenceItem
import com.zenzer0s.kite.ui.component.PreferenceSubtitle
import com.zenzer0s.kite.ui.component.PreferenceSwitch
import com.zenzer0s.kite.ui.page.settings.BasePreferencePage
import com.zenzer0s.kite.ui.page.settings.general.YtdlpUpdateChannelDialog
import com.zenzer0s.kite.util.PreferenceUtil.getString
import com.zenzer0s.kite.util.PreferenceUtil.updateBoolean
import com.zenzer0s.kite.util.RESTRICT_FILENAMES
import com.zenzer0s.kite.util.UpdateUtil
import com.zenzer0s.kite.util.YT_DLP_VERSION
import com.zenzer0s.kite.util.makeToast
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TroubleShootingPage(
    modifier: Modifier = Modifier,
    onNavigateTo: (String) -> Unit,
    onBack: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    BasePreferencePage(
        modifier = modifier,
        title = stringResource(R.string.trouble_shooting),
        onBack = onBack,
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(it), contentPadding = PaddingValues(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(GroupedListDefaults.VerticalSpacing)) {
            item {
                OutlinedCard(modifier = Modifier.padding(16.dp)) {
                    PreferenceInfo(
                        modifier = Modifier,
                        text = stringResource(R.string.issue_tracker_hint),
                    )
                    val knownIssueUrlKite = "https://github.com/zenzer0s/Kite/issues"
                    KitePreferenceItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(0, 2)),
                        title = "Kite Issue Tracker",
                        description = null,
                        icon = Icons.AutoMirrored.Outlined.OpenInNew,
                        onClick = { uriHandler.openUri(knownIssueUrlKite) },
                    )

                    val knownIssueUrlYtdlp = "https://github.com/yt-dlp/yt-dlp/issues/3766"
                    KitePreferenceItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(1, 2)),
                        title = "yt-dlp Issue Tracker",
                        description = null,
                        icon = Icons.AutoMirrored.Outlined.OpenInNew,
                        onClick = { uriHandler.openUri(knownIssueUrlYtdlp) },
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }
            item { PreferenceSubtitle(text = stringResource(R.string.update)) }
            item {
                var isUpdating by remember { mutableStateOf(false) }
                var showYtdlpDialog by remember { mutableStateOf(false) }

                var ytdlpVersion by remember {
                    mutableStateOf(
                        YoutubeDL.getInstance().version(context.applicationContext)
                            ?: context.getString(R.string.ytdlp_update)
                    )
                }
                KitePreferenceItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(0, 1)),
                    title = stringResource(id = R.string.ytdlp_update_action),
                    description = ytdlpVersion,
                    leadingContent = {
                        if (isUpdating) {
                            CircularProgressIndicator(
                                modifier =
                                    Modifier.padding(start = 8.dp, end = 16.dp)
                                        .size(24.dp)
                                        .padding(2.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.Update,
                                contentDescription = null,
                                modifier = Modifier.padding(start = 8.dp, end = 16.dp).size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            runCatching {
                                    isUpdating = true
                                    UpdateUtil.updateYtDlp()
                                    ytdlpVersion = YT_DLP_VERSION.getString()
                                }
                                .onFailure { th ->
                                    th.printStackTrace()
                                    withContext(Dispatchers.Main) {
                                        context.makeToast(
                                            App.context.getString(R.string.yt_dlp_update_fail)
                                        )
                                    }
                                }
                                .onSuccess {
                                    withContext(Dispatchers.Main) {
                                        context.makeToast(
                                            context.getString(R.string.yt_dlp_up_to_date) +
                                                " (${YT_DLP_VERSION.getString()})"
                                        )
                                    }
                                }
                            isUpdating = false
                        }
                    },
                                        trailingContent = {
                        IconButton(onClick = { showYtdlpDialog = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = stringResource(id = R.string.open_settings),
                            )
                        }
                    },
                )
                if (showYtdlpDialog) {
                    YtdlpUpdateChannelDialog(onDismissRequest = { showYtdlpDialog = false })
                }
            }

            item { PreferenceSubtitle(text = stringResource(R.string.network)) }
            item {
                KitePreferenceItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(0, 1)),
                    title = stringResource(R.string.cookies),
                    description = stringResource(R.string.cookies_desc),
                    icon = Icons.Outlined.Cookie,
                    onClick = { onNavigateTo(Route.COOKIE_PROFILE) },
                )
            }
            item { PreferenceSubtitle(text = stringResource(R.string.download_directory)) }
            item {
                var restrictFilenames by RESTRICT_FILENAMES.booleanState
                KitePreferenceSwitchItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(0, 1)),
                    title = stringResource(id = R.string.restrict_filenames),
                    icon = Icons.Outlined.Spellcheck,
                    description = stringResource(id = R.string.restrict_filenames_desc),
                    checked = restrictFilenames,
                ) {
                    restrictFilenames = !restrictFilenames
                    RESTRICT_FILENAMES.updateBoolean(restrictFilenames)
                }
            }
        }
    }
}
