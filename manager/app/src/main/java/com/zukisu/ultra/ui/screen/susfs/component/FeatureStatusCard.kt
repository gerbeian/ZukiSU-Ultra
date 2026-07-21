package com.zukisu.ultra.ui.screen.susfs.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zukisu.ultra.ui.LocalUiMode
import com.zukisu.ultra.ui.UiMode
import com.zukisu.ultra.ui.screen.susfs.component.miuix.FeatureStatusCardMiuix
import com.zukisu.ultra.ui.screen.susfs.component.material.FeatureStatusCardMaterial
import com.zukisu.ultra.ui.screen.susfs.util.EnabledFeature

@Composable
fun FeatureStatusCard(
    feature: EnabledFeature,
    onRefresh: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    when (LocalUiMode.current) {
        UiMode.Miuix -> FeatureStatusCardMiuix(
            feature = feature,
            onRefresh = onRefresh,
            modifier = modifier
        )
        UiMode.Material -> FeatureStatusCardMaterial(
            feature = feature,
            onRefresh = onRefresh,
            modifier = modifier
        )
    }
}
