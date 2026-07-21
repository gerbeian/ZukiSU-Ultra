package com.zukisu.ultra.ui.screen.susfs.content

import androidx.compose.runtime.Composable
import com.zukisu.ultra.ui.LocalUiMode
import com.zukisu.ultra.ui.UiMode
import com.zukisu.ultra.ui.screen.susfs.content.miuix.EnabledFeaturesContentMiuix
import com.zukisu.ultra.ui.screen.susfs.content.material.EnabledFeaturesContentMaterial
import com.zukisu.ultra.ui.screen.susfs.util.EnabledFeature

@Composable
fun EnabledFeaturesContent(
    enabledFeatures: List<EnabledFeature>,
    onRefresh: () -> Unit
) {
    when (LocalUiMode.current) {
        UiMode.Miuix -> EnabledFeaturesContentMiuix(
            enabledFeatures = enabledFeatures,
            onRefresh = onRefresh
        )
        UiMode.Material -> EnabledFeaturesContentMaterial(
            enabledFeatures = enabledFeatures,
            onRefresh = onRefresh
        )
    }
}
