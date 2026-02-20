package com.zenzer0s.kite.ui.page.settings.interaction

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import com.zenzer0s.kite.ui.theme.KiteCustomColors
import com.zenzer0s.kite.ui.theme.GroupedListDefaults
import com.zenzer0s.kite.ui.component.KitePreferenceSwitchItem
import com.zenzer0s.kite.ui.component.KitePreferenceItem
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.zenzer0s.kite.R
import com.zenzer0s.kite.ui.component.BackButton
import com.zenzer0s.kite.ui.component.PreferenceItem
import com.zenzer0s.kite.ui.component.PreferenceSubtitle
import com.zenzer0s.kite.util.DOWNLOAD_TYPE_INITIALIZATION
import com.zenzer0s.kite.util.PreferenceUtil.getInt
import com.zenzer0s.kite.util.PreferenceUtil.updateInt
import com.zenzer0s.kite.util.USE_PREVIOUS_SELECTION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractionPreferencePage(modifier: Modifier = Modifier, onBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var showDownloadTypeDialog by remember { mutableStateOf(false) }
    val initialType by
        remember(showDownloadTypeDialog) {
            mutableIntStateOf(DOWNLOAD_TYPE_INITIALIZATION.getInt())
        }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            LargeTopAppBar(
                colors = KiteCustomColors.topBarColors,
title = { Text(text = stringResource(id = R.string.interface_and_interaction)) },
                scrollBehavior = scrollBehavior,
                navigationIcon = { BackButton(onClick = onBack) },
            )
        },
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(it), contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(GroupedListDefaults.VerticalSpacing)) {
            item {
                PreferenceSubtitle(text = stringResource(id = R.string.settings_before_download))
            }

            item {
                KitePreferenceItem(
        modifier = Modifier.clip(GroupedListDefaults.getShape(0, 1)),
                    title = stringResource(id = R.string.download_type),
                    description =
                        when (initialType) {
                            USE_PREVIOUS_SELECTION ->
                                stringResource(id = R.string.use_previous_selection)
                            else -> stringResource(id = R.string.none)
                        },
                ) {
                    showDownloadTypeDialog = true
                }
            }
        }
    }

    if (showDownloadTypeDialog) {
        DownloadTypeCustomizationDialog(
            onDismissRequest = { showDownloadTypeDialog = false },
            selectedItem = initialType,
        ) {
            DOWNLOAD_TYPE_INITIALIZATION.updateInt(it)
            showDownloadTypeDialog = false
        }
    }
}
