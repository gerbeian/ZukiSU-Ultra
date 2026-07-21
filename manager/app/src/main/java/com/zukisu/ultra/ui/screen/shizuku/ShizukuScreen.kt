package com.zukisu.ultra.ui.screen.shizuku

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zukisu.ultra.ui.LocalUiMode
import com.zukisu.ultra.ui.UiMode
import com.zukisu.ultra.ui.navigation3.LocalNavigator
import com.zukisu.ultra.ui.viewmodel.ShizukuViewModel

@Composable
fun ShizukuScreen() {
    val navigator = LocalNavigator.current
    val viewModel = viewModel<ShizukuViewModel>()
    val state = viewModel.uiState

    val actions = ShizukuScreenActions(
        onBack = dropUnlessResumed { navigator.pop() },
        onStartServer = { viewModel.startServer() },
        onStopServer = { viewModel.stopServer() },
        onRefresh = { viewModel.refreshStatus() },
        onRevokeApp = { pkg -> viewModel.revokeApp(pkg) },
    )

    when (LocalUiMode.current) {
        UiMode.Miuix -> ShizukuScreenMiuix(state = state.value, actions = actions)
        UiMode.Material -> ShizukuScreenMaterial(state = state.value, actions = actions)
    }
}