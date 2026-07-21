package com.zukisu.ultra.ui.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import rikka.shizuku.Shizuku
import java.io.File

/**
 * Helper class for Shizuku server management via KernelSU root.
 */
object ShizukuHelper {

    private const val SHIZUKU_SERVER_PATH = "/data/local/tmp/shizuku_server"
    private const val SHIZUKU_PACKAGE = "moe.shizuku.privileged.api"

    /**
     * Check if Shizuku server is currently running.
     */
    fun isServerRunning(): Boolean {
        return try {
            Shizuku.pingBinder()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Check if Shizuku manager app is installed.
     */
    fun isShizukuInstalled(context: Context): Boolean {
        return try {
            context.packageManager.getPackageInfo(SHIZUKU_PACKAGE, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * Get Shizuku server version (API version).
     */
    fun getServerVersion(): String {
        return try {
            if (Shizuku.pingBinder()) {
                "v${Shizuku.getVersion()}"
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Start Shizuku server using KernelSU root.
     * This requires the Shizuku server binary to be present.
     */
    fun startServer(): Result<Unit> {
        return try {
            val shell = getRootShell()
            if (shell == null) {
                return Result.failure(Exception("Root access not available"))
            }

            // Check if server binary exists
            val checkCmd = shell.newJob().add("test -f $SHIZUKU_SERVER_PATH && echo 'EXISTS' || echo 'NOT_FOUND'")
            checkCmd.exec()
            val result = checkCmd.out.joinToString("\n").trim()

            if (!result.contains("EXISTS")) {
                return Result.failure(Exception("Shizuku server binary not found at $SHIZUKU_SERVER_PATH"))
            }

            // Start the server
            val startCmd = shell.newJob().add(
                "nohup $SHIZUKU_SERVER_PATH &"
            )
            startCmd.exec()

            // Wait a moment for the server to start
            Thread.sleep(500)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Stop Shizuku server.
     */
    fun stopServer(): Result<Unit> {
        return try {
            val shell = getRootShell()
            if (shell == null) {
                return Result.failure(Exception("Root access not available"))
            }

            val stopCmd = shell.newJob().add(
                "pkill -f shizuku_server"
            )
            stopCmd.exec()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get Shizuku API version code.
     */
    fun getApiVersion(): Int {
        return try {
            Shizuku.getVersion()
        } catch (e: Exception) {
            -1
        }
    }

    /**
     * Check if an app is authorized for Shizuku.
     */
    fun isAppAuthorized(packageName: String, uid: Int): Boolean {
        return try {
            Shizuku.checkRemotePermission(packageName, null, uid) == PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Request Shizuku permission for the current app.
     */
    fun requestSelfPermission(): Boolean {
        return try {
            if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                Shizuku.requestPermission(0)
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get the list of packages that have Shizuku permission.
     */
    fun getAuthorizedPackages(): List<String> {
        return try {
            val binder = Shizuku.getBinder()
            if (binder == null) return emptyList()

            // Use Shizuku's newProcess to get authorized packages
            // This is a simplified approach - in production, you'd use the Shizuku API properly
            val args = arrayOf("cmd", "appops", "get", SHIZUKU_PACKAGE)
            val process = Runtime.getRuntime().exec(args)
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()

            output.lines()
                .filter { it.contains("PERMISSION_GRANTED") }
                .map { it.substringBefore(":").trim() }
                .filter { it.isNotEmpty() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}