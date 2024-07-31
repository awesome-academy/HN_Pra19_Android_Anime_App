package com.example.anidb.data.repository.source

import com.example.anidb.data.model.Anime
import com.example.anidb.data.model.AnimeRelations
import com.example.anidb.data.repository.source.remote.OnResultListener

interface AnimeDataSource {
    interface Local {
        fun addAnimeFavorite(
            anime: Anime,
            listener: OnResultListener<Long>,
        )

        fun getAnimeFavorite(
            limit: Int,
            page: Int,
            listener: OnResultListener<List<Anime>>,
        )

        fun deleteAnimeFavorite(
            id: Int,
            listener: OnResultListener<Boolean>,
        )

        fun isAnimeFavoriteExists(
            id: Int,
            listener: OnResultListener<Boolean>,
        )
    }

    interface Remote {
        fun getAnimePopular(
            limit: Int,
            page: Int,
            listener: OnResultListener<List<Anime>>,
        )

        fun getAnimeNew(
            limit: Int,
            page: Int,
            listener: OnResultListener<List<Anime>>,
        )

        fun getAnimeTopRate(
            limit: Int,
            page: Int,
            listener: OnResultListener<List<Anime>>,
        )

        fun getAnimeDetail(
            id: Int,
            listener: OnResultListener<Anime>,
        )

        fun getAnimeRelations(
            id: Int,
            listener: OnResultListener<List<AnimeRelations>>,
        )
    }
}
