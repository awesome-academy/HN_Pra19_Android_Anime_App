package com.example.anidb.screen.favorite.presenter

import com.example.anidb.data.model.Anime
import com.example.anidb.data.repository.AnimeRepository
import com.example.anidb.data.repository.source.remote.OnResultListener

class FavoritePresenter(private val animeRepository: AnimeRepository) : FavoriteContract.Presenter {
    private var hView: FavoriteContract.View? = null

    fun setView(view: FavoriteContract.View) {
        this.hView = view
    }

    override fun getFavoriteAnime(
        limit: Int,
        page: Int,
    ) {
        animeRepository.getAnimeFavorite(
            limit,
            page,
            object : OnResultListener<List<Anime>> {
                override fun onSuccess(data: List<Anime>) {
                    hView?.onGetFavoriteAnimeSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    hView?.onError(exception.toString())
                }
            },
        )
    }

    override fun deleteAnimeFavorite(id: Int) {
        animeRepository.deleteAnimeFavorite(
            id,
            object : OnResultListener<Boolean> {
                override fun onSuccess(data: Boolean) {
                    hView?.onDeleteAnimeFavoriteSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    hView?.onError(exception.toString())
                }
            },
        )
    }

    override fun isAnimeFavorite(id: Int) {
        animeRepository.isAnimeFavoriteExists(
            id,
            object : OnResultListener<Boolean> {
                override fun onSuccess(data: Boolean) {
                    hView?.onIsAnimeFavoriteSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    hView?.onError(exception.toString())
                }
            },
        )
    }
}
