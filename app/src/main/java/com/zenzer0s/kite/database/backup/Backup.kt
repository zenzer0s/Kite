package com.zenzer0s.kite.database.backup

import com.zenzer0s.kite.database.objects.CommandTemplate
import com.zenzer0s.kite.database.objects.DownloadedVideoInfo
import com.zenzer0s.kite.database.objects.OptionShortcut
import kotlinx.serialization.Serializable

@Serializable
data class Backup(
    val templates: List<CommandTemplate>? = null,
    val shortcuts: List<OptionShortcut>? = null,
    val downloadHistory: List<DownloadedVideoInfo>? = null,
)
