package com.picpay.desafio.android

import com.picpay.desafio.android.domain.model.User

object MockModels {

    fun getUser() = User(
        id = 1,
        name = "Eduardo Santos",
        img = "https://randomuser.me/api/portraits/men/9.jpg",
        username = "User"
    )

    fun getUserList(size: Int): MutableList<User> {
        val users = mutableListOf<User>()
        for (i in 1..size) {
            users.add(
                User(
                    id = i,
                    name = "Name $i",
                    img = "https://randomuser.me/api/portraits/men/9.jpg",
                    username = "User Name $i"
                )
            )
        }

        return users
    }
}