package com.zukisu.ultra.ui.kernelFlash

import androidx.compose.runtime.Stable
import com.zukisu.ultra.ui.screen.install.InstallMethod

enum class KpmPatchOption {
    FOLLOW_KERNEL,
    PATCH_KPM,
    UNDO_PATCH_KPM
}

@Stable
data class AnyKernel3State(
    val kpmPatchOption: KpmPatchOption,
    val showSlotSelectionDialog: Boolean,
    val showKpmPatchDialog: Boolean,
    val onHorizonKernelSelected: (InstallMethod.HorizonKernel) -> Unit,
    val onSlotSelected: (String) -> Unit,
    val onDismissSlotDialog: () -> Unit,
    val onOptionSelected: (KpmPatchOption) -> Unit,
    val onDismissPatchDialog: () -> Unit,
    val onReopenSlotDialog: (InstallMethod.HorizonKernel) -> Unit,
    val onReopenKpmDialog: (InstallMethod.HorizonKernel) -> Unit
)
