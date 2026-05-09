package com.rodriguezcortez.desafio.model

data class Resource(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val tipo: String = "",
    val enlace: String = "",
    val imagen: String = "",
    val autor: String = "",
    val release_date: String = "",
    val category: String = "",
    val rating: Int = 0
)