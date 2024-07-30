package com.example.anidb.data.repository

import com.example.anidb.data.model.Anime
import com.example.anidb.data.model.AnimeRelations
import com.example.anidb.data.repository.source.AnimeDataSource
import com.example.anidb.data.repository.source.remote.OnResultListener

class AnimeRepository(
    private val remote: AnimeDataSource.Remote,
    private val local: AnimeDataSource.Local,
) : AnimeDataSource.Local, AnimeDataSource.Remote {
    override fun getAnimePopular(
        limit: Int,
        page: Int,
        listener: OnResultListener<List<Anime>>,
    ) {
        remote.getAnimePopular(limit, page, listener)
    }

    override fun getAnimeNew(
        limit: Int,
        page: Int,
        listener: OnResultListener<List<Anime>>,
    ) {
        remote.getAnimeNew(limit, page, listener)
    }

    override fun getAnimeTopRate(
        limit: Int,
        page: Int,
        listener: OnResultListener<List<Anime>>,
    ) {
        remote.getAnimeTopRate(limit, page, listener)
    }

    override fun getAnimeDetail(
        id: Int,
        listener: OnResultListener<Anime>,
    ) {
        remote.getAnimeDetail(id, listener)
    }

    override fun getAnimeRelations(
        id: Int,
        listener: OnResultListener<List<AnimeRelations>>,
    ) {
        remote.getAnimeRelations(id, listener)
    }

    override fun addAnimeFavorite(
        anime: Anime,
        listener: OnResultListener<Long>,
    ) {
        local.addAnimeFavorite(anime, listener)
    }

    override fun getAnimeFavorite(
        limit: Int,
        page: Int,
        listener: OnResultListener<List<Anime>>,
    ) {
        local.getAnimeFavorite(limit, page, listener)
    }

    override fun deleteAnimeFavorite(
        id: Int,
        listener: OnResultListener<Boolean>,
    ) {
        local.deleteAnimeFavorite(id, listener)
    }

    override fun isAnimeFavoriteExists(
        id: Int,
        listener: OnResultListener<Boolean>,
    ) {
        local.isAnimeFavoriteExists(id, listener)
    }

    companion object {
        private var instance: AnimeRepository? = null

        fun getInstance(
            remote: AnimeDataSource.Remote,
            local: AnimeDataSource.Local,
        ) = synchronized(this) {
            instance ?: AnimeRepository(remote, local).also { instance = it }
        }
    }
}
