package com.example.anidb.data.model

import java.io.Serializable

data class AnimeRelations(
    var relation: String = "",
    var entry: List<Entry> = emptyList(),
) : Serializable
