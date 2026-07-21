package com.zukisu.ultra.ui.component.choosekmidialog

import androidx.compose.runtime.Composable
import com.zukisu.ultra.ui.LocalUiMode
import com.zukisu.ultra.ui.UiMode

@Composable
fun ChooseKmiDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    onSelected: (String?) -> Unit
) {
    when (LocalUiMode.current) {
        UiMode.Miuix -> ChooseKmiDialogMiuix(show, onDismissRequest, onSelected)
        UiMode.Material -> ChooseKmiDialogMaterial(show, onDismissRequest, onSelected)
    }
}