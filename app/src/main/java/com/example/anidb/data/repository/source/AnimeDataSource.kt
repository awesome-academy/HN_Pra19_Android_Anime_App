package com.example.anidb.data.repository.source

import com.example.anidb.data.model.Anime
import com.example.anidb.data.repository.source.remote.OnResultListener

interface AnimeDataSource {
    interface Local

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

        fun getAnimeFavorite(
            limit: Int,
            page: Int,
            listener: OnResultListener<List<Anime>>,
        )

        fun getAnimeDetail(
            id: Int,
            listener: OnResultListener<Anime>,
        )
    }
}
