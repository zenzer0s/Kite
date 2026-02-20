package com.zenzer0s.kite

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zenzer0s.kite.download.DownloaderV2
import com.zenzer0s.kite.download.Task
import com.zenzer0s.kite.ui.common.LocalDarkTheme
import com.zenzer0s.kite.ui.common.SettingsProvider
import com.zenzer0s.kite.ui.page.downloadv2.configure.Config
import com.zenzer0s.kite.ui.page.downloadv2.configure.DownloadDialog
import com.zenzer0s.kite.ui.page.downloadv2.configure.DownloadDialogViewModel
import com.zenzer0s.kite.ui.page.downloadv2.configure.DownloadDialogViewModel.Action
import com.zenzer0s.kite.ui.page.downloadv2.configure.DownloadDialogViewModel.SelectionState
import com.zenzer0s.kite.ui.page.downloadv2.configure.FormatPage
import com.zenzer0s.kite.ui.page.downloadv2.configure.PlaylistSelectionPage
import com.zenzer0s.kite.ui.theme.KiteTheme
import com.zenzer0s.kite.util.DownloadUtil
import com.zenzer0s.kite.util.FAST_MODE
import com.zenzer0s.kite.util.PreferenceUtil
import com.zenzer0s.kite.util.PreferenceUtil.getBoolean
import com.zenzer0s.kite.util.matchUrlFromSharedText
import com.zenzer0s.kite.util.setLanguage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.context.GlobalContext

class QuickDownloadActivity : ComponentActivity() {
    private var sharedUrlCached: String = ""

    private fun Intent.getSharedURL(): String? {
        val intent = this

        return when (intent.action) {
            Intent.ACTION_VIEW -> {
                intent.dataString
            }

            Intent.ACTION_SEND -> {
                intent.getStringExtra(Intent.EXTRA_TEXT)?.let { sharedContent ->
                    intent.removeExtra(Intent.EXTRA_TEXT)
                    matchUrlFromSharedText(sharedContent)
                }
            }

            else -> {
                null
            }
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getSharedURL()?.let { sharedUrlCached = it }

        if (sharedUrlCached.isEmpty()) {
            finish()
            return
        }

        if (FAST_MODE.getBoolean()) {
            val downloader: DownloaderV2 = GlobalContext.get().get()
            val task =
                Task(
                    url = sharedUrlCached,
                    preferences = DownloadUtil.DownloadPreferences.createFromPreferences(),
                )
            downloader.enqueue(task)
            App.startService()
            finish()
            return
        }

        App.startService()

        enableEdgeToEdge()

        window.run {
            setBackgroundDrawable(ColorDrawable(0))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            } else {
                setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
            }
        }

        if (Build.VERSION.SDK_INT < 33) {
            runBlocking { setLanguage(PreferenceUtil.getLocaleFromPreference()) }
        }

        val viewModel: DownloadDialogViewModel = getViewModel()
        viewModel.postAction(Action.ShowSheet(listOf(sharedUrlCached)))

        setContent {
            SettingsProvider(calculateWindowSizeClass(this).widthSizeClass) {
                KiteTheme(
                    darkTheme = LocalDarkTheme.current.isDarkTheme(),
                ) {
                    var preferences by remember {
                        mutableStateOf(DownloadUtil.DownloadPreferences.createFromPreferences())
                    }

                    val sheetValue = viewModel.sheetValueFlow.collectAsStateWithLifecycle().value

                    val state = viewModel.sheetStateFlow.collectAsStateWithLifecycle().value

                    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                    val selectionState =
                        viewModel.selectionStateFlow.collectAsStateWithLifecycle().value

                    var showDialog by remember { mutableStateOf(false) }

                    LaunchedEffect(sheetValue, selectionState) {
                        if (sheetValue == DownloadDialogViewModel.SheetValue.Expanded) {
                            showDialog = true
                        } else if (sheetValue == DownloadDialogViewModel.SheetValue.Hidden) {
                            launch { sheetState.hide() }
                                .invokeOnCompletion {
                                    showDialog = false
                                    if (selectionState == SelectionState.Idle) {
                                        this@QuickDownloadActivity.finish()
                                    }
                                }
                        }
                    }

                    if (showDialog) {
                        DownloadDialog(
                            state = state,
                            sheetState = sheetState,
                            config = Config(),
                            preferences = preferences,
                            onPreferencesUpdate = { },
                            onActionPost = { viewModel.postAction(it) },
                        )
                    }

                    when (selectionState) {
                        is SelectionState.FormatSelection ->
                            FormatPage(
                                state = selectionState,
                                onDismissRequest = {
                                    viewModel.postAction(Action.Reset)
                                    this.finish()
                                },
                            )

                        SelectionState.Idle -> {}
                        is SelectionState.PlaylistSelection -> {
                            PlaylistSelectionPage(
                                state = selectionState,
                                onDismissRequest = {
                                    viewModel.postAction(Action.Reset)
                                    this.finish()
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
