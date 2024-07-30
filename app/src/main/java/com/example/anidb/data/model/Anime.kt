package com.example.anidb.data.model

import java.io.Serializable

data class Anime(
    var id: Int = 0,
    var title: String = "",
    var image: String = "",
    var description: String = "",
    var status: String = "",
    var airedTo: String = "",
    var airedFrom: String = "",
    var members: Int = 0,
    var score: Double = 0.0,
    var rank: Int = 0,
    var popularity: Int = 0,
    var favorites: Int = 0,
    var duration: String = "",
    var season: String = "",
    var episodes: Int = 0,
    var genres: List<Genre> = emptyList(),
    var studios: List<Studio> = emptyList(),
) : Serializable
