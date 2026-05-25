package com.example.multimodule.core.domain.repository

import com.example.multimodule.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getLoggedInUser(): Flow<User?>
    suspend fun login(email: String, password: String): User
    suspend fun register(email: String, password: String): User
    suspend fun logout()
}
