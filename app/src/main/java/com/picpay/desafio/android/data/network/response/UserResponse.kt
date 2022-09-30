package com.picpay.desafio.android.data.network.response

import com.picpay.desafio.android.domain.model.User
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    val id: Int,
    val name: String,
    val username: String,
    val img: String
)

fun UserResponse.toUser() = User(
    id = this.id,
    name = this.name,
    username = this.username,
    img = this.img
)