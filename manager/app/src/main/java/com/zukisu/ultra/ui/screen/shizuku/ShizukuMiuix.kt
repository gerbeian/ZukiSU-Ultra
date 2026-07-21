package com.zukisu.ultra.ui.screen.shizuku

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.ArrowBackIcon
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.ListItem
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icons.MiuixIcons
import top.yukonga.miuix.kmp.icons.icons.ArrowRight
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun ShizukuScreenMiuix(
    state: ShizukuUiState,
    actions: ShizukuScreenActions,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Shizuku",
                navigationIcon = {
                    IconButton(onClick = actions.onBack) {
                        ArrowBackIcon()
                    }
                },
                action = {
                    IconButton(onClick = actions.onRefresh) {
                        Icon(MiuixIcons.ArrowRight, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Status Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (state.isRunning) "Shizuku is Running" else "Shizuku is Stopped",
                                    color = if (state.isRunning) MiuixTheme.colorScheme.primary
                                    else MiuixTheme.colorScheme.onSurface,
                                )
                                if (state.serverVersion.isNotEmpty()) {
                                    Text(
                                        text = "Server: ${state.serverVersion}",
                                        color = MiuixTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                if (state.isLoading) {
                                    Text(
                                        text = "Loading...",
                                        color = MiuixTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                if (state.errorMessage != null) {
                                    Text(
                                        text = state.errorMessage,
                                        color = MiuixTheme.colorScheme.error,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Authorized Apps
            if (state.authorizedApps.isNotEmpty()) {
                item {
                    Text(
                        text = "Authorized Apps",
                        color = MiuixTheme.colorScheme.onSurface,
                    )
                }
                items(state.authorizedApps) { app ->
                    ListItem(
                        headline = app.appName,
                        supporting = app.packageName,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            // Info
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "About Shizuku",
                            color = MiuixTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Shizuku allows normal apps to use system APIs directly. " +
                                    "ZukiSU integrates Shizuku management for your convenience.",
                            color = MiuixTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}