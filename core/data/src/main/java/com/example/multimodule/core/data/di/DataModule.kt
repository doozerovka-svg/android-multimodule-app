package com.example.multimodule.core.data.di

import com.example.multimodule.core.data.repository.AiPredictRepositoryImpl
import com.example.multimodule.core.data.repository.AuthRepositoryImpl
import com.example.multimodule.core.data.repository.TaskRepositoryImpl
import com.example.multimodule.core.domain.repository.AiPredictRepository
import com.example.multimodule.core.domain.repository.AuthRepository
import com.example.multimodule.core.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindAiPredictRepository(
        aiPredictRepositoryImpl: AiPredictRepositoryImpl
    ): AiPredictRepository
}
