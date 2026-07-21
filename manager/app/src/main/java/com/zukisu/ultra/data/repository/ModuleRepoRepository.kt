package com.zukisu.ultra.data.repository

import com.zukisu.ultra.data.model.RepoModule

interface ModuleRepoRepository {
    suspend fun fetchModules(): Result<List<RepoModule>>
}
