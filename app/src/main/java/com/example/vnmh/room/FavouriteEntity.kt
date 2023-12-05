package com.example.vnmh.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteItem(
    @PrimaryKey (autoGenerate = false)
    val id: String,
    val images: String,
    val imageDescription: String,
    val nonPresenterAuthorsName: String,
    val title: String,
    val year: String,
    val image1: String,
    val description1: String,
    val image2: String,
    val description2: String,
    val image3: String,
    val description3: String,
    var isFavourite: Boolean = false,
)
