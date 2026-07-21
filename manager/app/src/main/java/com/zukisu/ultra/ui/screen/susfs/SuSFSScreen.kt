package com.zukisu.ultra.ui.screen.susfs

import androidx.compose.runtime.Composable
import com.zukisu.ultra.ui.LocalUiMode
import com.zukisu.ultra.ui.UiMode

@Composable
fun SuSFSScreen() {
    when (LocalUiMode.current) {
        UiMode.Miuix -> SuSFSMiuix()
        UiMode.Material -> SuSFSMaterial()
    }
}
