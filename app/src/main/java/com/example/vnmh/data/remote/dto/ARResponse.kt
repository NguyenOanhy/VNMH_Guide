package com.example.vnmh.data.remote.dto

import kotlinx.serialization.Serializable



@Serializable
data class ARItem(
    val id: String,
    val image: String,
    val name: String,
    val url: String
)
