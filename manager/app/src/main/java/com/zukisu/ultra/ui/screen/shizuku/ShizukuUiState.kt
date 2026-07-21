package com.zukisu.ultra.ui.screen.shizuku

import androidx.compose.runtime.Immutable

@Immutable
data class ShizukuUiState(
    val isRunning: Boolean = false,
    val isServerInstalled: Boolean = false,
    val serverVersion: String = "",
    val apiVersion: String = "",
    val authorizedApps: List<ShizukuAppInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@Immutable
data class ShizukuAppInfo(
    val packageName: String,
    val appName: String,
    val uid: Int,
    val isAuthorized: Boolean,
    val icon: Any? = null,
)

data class ShizukuScreenActions(
    val onBack: () -> Unit = {},
    val onStartServer: () -> Unit = {},
    val onStopServer: () -> Unit = {},
    val onInstallServer: () -> Unit = {},
    val onRevokeApp: (String) -> Unit = {},
    val onRefresh: () -> Unit = {},
)