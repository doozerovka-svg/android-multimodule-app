package com.example.multimodule.core.data.mapper

import com.example.multimodule.core.database.entity.UserEntity
import com.example.multimodule.core.domain.model.User
import com.example.multimodule.core.network.model.AuthResponseDto

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        email = email,
        token = token
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        token = token
    )
}

fun AuthResponseDto.toDomain(): User {
    return User(
        id = id,
        email = email,
        token = accessToken
    )
}
