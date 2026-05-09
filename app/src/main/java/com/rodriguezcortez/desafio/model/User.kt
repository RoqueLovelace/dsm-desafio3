package com.rodriguezcortez.desafio.model

data class User(
    val id: String = "",
    val name: String = "",
    val correo: String = "",
    val password: String = "",
    val createdAt: Long = 0,
    val rol: String = ""
)