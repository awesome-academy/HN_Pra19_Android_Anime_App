package com.example.anidb.data.model

data class Anime(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val status: String,
    val airedTo: String,
    val airedFrom: String,
    val members: String,
    val score: Double,
    val rank: Int,
    val popularity: Int,
    val favorites: Int,
    val duration: String,
    val season: String,
    val episodes: Int,
    val genres: Genre,
    val studios: Studio
)
