package com.example.anidb.data.repository.source.local

import android.content.Context
import com.example.anidb.data.model.Anime
import com.example.anidb.data.repository.source.AnimeDataSource
import com.example.anidb.data.repository.source.remote.OnResultListener

class AnimeLocalDataSource(context: Context) : AnimeDataSource.Local {
    private val animeDAO = AnimeFavoriteDAO(context)

    override fun addAnimeFavorite(
        anime: Anime,
        listener: OnResultListener<Long>,
    ) {
        val result = animeDAO.insertAnime(anime)
        if (result != -1L) {
            listener.onSuccess(result)
        }
    }

    override fun getAnimeFavorite(
        limit: Int,
        page: Int,
        listener: OnResultListener<List<Anime>>,
    ) {
        listener.onSuccess(animeDAO.getListAnime(page, limit))
    }

    override fun deleteAnimeFavorite(
        id: Int,
        listener: OnResultListener<Boolean>,
    ) {
        listener.onSuccess(animeDAO.deleteAnime(id))
    }

    override fun isAnimeFavoriteExists(
        id: Int,
        listener: OnResultListener<Boolean>,
    ) {
        listener.onSuccess(animeDAO.isAnimeExists(id))
    }

    companion object {
        private var instance: AnimeLocalDataSource? = null

        fun getInstance(context: Context) =
            synchronized(this) {
                instance ?: AnimeLocalDataSource(context).also { instance = it }
            }
    }
}
