package com.zukisu.ultra.ui.screen.susfs.component

import androidx.compose.runtime.Composable
import com.zukisu.ultra.ui.LocalUiMode
import com.zukisu.ultra.ui.UiMode
import com.zukisu.ultra.ui.screen.susfs.component.miuix.AddAppPathDialogMiuix
import com.zukisu.ultra.ui.screen.susfs.component.material.AddAppPathDialogMaterial
import com.zukisu.ultra.ui.screen.susfs.util.AppInfo

@Composable
fun AddAppPathDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit,
    isLoading: Boolean,
    apps: List<AppInfo> = emptyList(),
    onLoadApps: () -> Unit,
    existingSusPaths: Set<String> = emptySet()
) {
    when (LocalUiMode.current) {
        UiMode.Miuix -> AddAppPathDialogMiuix(
            showDialog = showDialog,
            onDismiss = onDismiss,
            onConfirm = onConfirm,
            isLoading = isLoading,
            apps = apps,
            onLoadApps = onLoadApps,
            existingSusPaths = existingSusPaths
        )
        UiMode.Material -> AddAppPathDialogMaterial(
            showDialog = showDialog,
            onDismiss = onDismiss,
            onConfirm = onConfirm,
            isLoading = isLoading,
            apps = apps,
            onLoadApps = onLoadApps,
            existingSusPaths = existingSusPaths
        )
    }
}
