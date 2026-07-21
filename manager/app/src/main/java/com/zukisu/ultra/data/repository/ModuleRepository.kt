package com.zukisu.ultra.data.repository

import com.zukisu.ultra.data.model.Module
import com.zukisu.ultra.data.model.ModuleUpdateInfo

interface ModuleRepository {
    suspend fun getModules(): Result<List<Module>>
    suspend fun checkUpdate(module: Module): Result<ModuleUpdateInfo>
}
