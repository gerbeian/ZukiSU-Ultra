package com.zukisu.ultra.ui.viewmodel

import androidx.compose.runtime.Immutable
import com.zukisu.ultra.ui.UiMode
import com.zukisu.ultra.ui.theme.AppSettings

@Immutable
data class MainActivityUiState(
    val appSettings: AppSettings,
    val pageScale: Float,
    val enableBlur: Boolean,
    val enableFloatingBottomBar: Boolean,
    val enableFloatingBottomBarBlur: Boolean,
    val uiMode: UiMode,
)
