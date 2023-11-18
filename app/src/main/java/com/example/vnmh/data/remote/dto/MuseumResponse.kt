package com.example.vnmh.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class MuseumItem(
    val id: String,
    val imageCopyright: String,
    val images: String,
    val imageDescription:String,
    val nonPresenterAuthorsName: String,
    val title: String,
    val year: String,
    val image1: String,
    val description1: String,
    val image2: String,
    val description2: String
)
