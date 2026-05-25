package com.example.multimodule.core.data.repository

import com.example.multimodule.core.data.mapper.toDomain
import com.example.multimodule.core.data.mapper.toEntity
import com.example.multimodule.core.database.dao.UserDao
import com.example.multimodule.core.domain.model.User
import com.example.multimodule.core.domain.repository.AuthRepository
import com.example.multimodule.core.network.ApiService
import com.example.multimodule.core.data.security.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : AuthRepository {

    override fun getLoggedInUser(): Flow<User?> {
        return userDao.getLoggedInUserFlow().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun login(email: String, password: String): User {
        val request = mapOf("email" to email, "password" to password)
        val response = apiService.login(request)
        val user = response.toDomain()
        
        // Cache locally in SQLite
        userDao.clearUser()
        userDao.insertUser(user.toEntity())
        
        // Save token securely in EncryptedSharedPreferences
        sessionManager.saveAccessToken(user.token)
        
        return user
    }

    override suspend fun register(email: String, password: String): User {
        val request = mapOf("email" to email, "password" to password)
        val response = apiService.register(request)
        val user = response.toDomain()
        
        // Cache locally in SQLite
        userDao.clearUser()
        userDao.insertUser(user.toEntity())
        
        // Save token securely in EncryptedSharedPreferences
        sessionManager.saveAccessToken(user.token)
        
        return user
    }

    override suspend fun logout() {
        userDao.clearUser()
        sessionManager.clearSession()
    }
}
