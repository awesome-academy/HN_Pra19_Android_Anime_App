package com.example.anidb.screen.home.presenter

import com.example.anidb.data.model.Anime
import com.example.anidb.data.repository.AnimeRepository
import com.example.anidb.data.repository.source.remote.OnResultListener

class HomePresenter(private val animeRepository: AnimeRepository) : HomeContract.Presenter {
    private var hView: HomeContract.View? = null

    fun setView(view: HomeContract.View) {
        this.hView = view
    }

    override fun getPopularAnime(
        limit: Int,
        page: Int,
    ) {
        animeRepository.getAnimePopular(
            limit,
            page,
            object : OnResultListener<List<Anime>> {
                override fun onSuccess(data: List<Anime>) {
                    hView?.onGetPopularAnimeSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    hView?.onError(exception.toString())
                }
            },
        )
    }

    override fun getNewAnime(
        limit: Int,
        page: Int,
    ) {
        animeRepository.getAnimeNew(
            limit,
            page,
            object : OnResultListener<List<Anime>> {
                override fun onSuccess(data: List<Anime>) {
                    hView?.onGetNewAnimeSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    hView?.onError(exception.toString())
                }
            },
        )
    }

    override fun getTopRateAnime(
        limit: Int,
        page: Int,
    ) {
        animeRepository.getAnimeTopRate(
            limit,
            page,
            object : OnResultListener<List<Anime>> {
                override fun onSuccess(data: List<Anime>) {
                    hView?.onGetTopRateAnimeSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    hView?.onError(exception.toString())
                }
            },
        )
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
}
