package com.zenzer0s.kite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zenzer0s.kite.ui.common.LocalDarkTheme
import com.zenzer0s.kite.ui.common.SettingsProvider
import com.zenzer0s.kite.ui.component.FilledButtonWithIcon
import com.zenzer0s.kite.ui.theme.KiteTheme

class CrashReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val errorMessage: String = intent.getStringExtra("error_report").toString()

        setContent {
            SettingsProvider(WindowWidthSizeClass.Compact) {
                KiteTheme(
                    darkTheme = LocalDarkTheme.current.isDarkTheme(),
                ) {
                    val clipboard = LocalClipboard.current
                    val scope = rememberCoroutineScope()
                    CrashReportPage(errorMessage = errorMessage) {
                        scope.launch {
                            clipboard.setClipEntry(
                                ClipEntry(ClipData.newPlainText("error_report", errorMessage))
                            )
                        }
                        this.finishAffinity()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) finishAffinity()
    }
}

@Composable
@Preview
fun CrashReportPage(errorMessage: String = "ERROR_EXAMPLE", onClick: () -> Unit = {}) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            androidx.compose.material3.HorizontalDivider()

            FilledButtonWithIcon(
                modifier =
                    Modifier.fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = onClick,
                icon = Icons.Outlined.BugReport,
                text = stringResource(R.string.copy_and_exit),
            )
        },
    ) {
        Column(modifier = Modifier.padding(it).verticalScroll(rememberScrollState())) {
            Text(
                text = stringResource(R.string.unknown_error_title),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 12.dp),
            )
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
            )
        }
    }
}
