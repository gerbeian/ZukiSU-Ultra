package com.zukisu.ultra.ui.viewmodel

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zukisu.ultra.ui.screen.shizuku.ShizukuAppInfo
import com.zukisu.ultra.ui.screen.shizuku.ShizukuUiState
import com.zukisu.ultra.ui.util.ShizukuHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShizukuViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ShizukuUiState())
    val uiState: StateFlow<ShizukuUiState> = _uiState.asStateFlow()

    init {
        refreshStatus()
    }

    fun refreshStatus() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                withContext(Dispatchers.IO) {
                    val isRunning = ShizukuHelper.isServerRunning()
                    val isInstalled = ShizukuHelper.isShizukuInstalled(getApplication())
                    val serverVersion = ShizukuHelper.getServerVersion()
                    val apiVersion = ShizukuHelper.getApiVersion().toString()
                    val apps = loadAuthorizedApps()

                    _uiState.update {
                        it.copy(
                            isRunning = isRunning,
                            isServerInstalled = isInstalled,
                            serverVersion = serverVersion,
                            apiVersion = if (apiVersion != "-1") apiVersion else "",
                            authorizedApps = apps,
                            isLoading = false,
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to refresh status",
                    )
                }
            }
        }
    }

    fun startServer() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                withContext(Dispatchers.IO) {
                    val result = ShizukuHelper.startServer()
                    if (result.isFailure) {
                        throw result.exceptionOrNull() ?: Exception("Failed to start server")
                    }
                    // Wait for server to be ready
                    var retries = 0
                    while (retries < 10 && !ShizukuHelper.isServerRunning()) {
                        Thread.sleep(500)
                        retries++
                    }
                }
                refreshStatus()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to start Shizuku server",
                    )
                }
            }
        }
    }

    fun stopServer() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                withContext(Dispatchers.IO) {
                    ShizukuHelper.stopServer()
                }
                refreshStatus()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to stop Shizuku server",
                    )
                }
            }
        }
    }

    fun revokeApp(packageName: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val shell = com.zukisu.ultra.ui.util.getRootShell()
                    shell?.newJob()?.add("cmd appops set $packageName 92 deny")?.exec()
                }
                refreshStatus()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Failed to revoke permission")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private suspend fun loadAuthorizedApps(): List<ShizukuAppInfo> {
        return withContext(Dispatchers.IO) {
            val context = getApplication<Application>()
            val pm = context.packageManager
            val packages = ShizukuHelper.getAuthorizedPackages()

            packages.mapNotNull { pkg ->
                try {
                    val appInfo = pm.getApplicationInfo(pkg, 0)
                    ShizukuAppInfo(
                        packageName = pkg,
                        appName = pm.getApplicationLabel(appInfo).toString(),
                        uid = appInfo.uid,
                        isAuthorized = true,
                    )
                } catch (e: PackageManager.NameNotFoundException) {
                    null
                }
            }
        }
    }
}