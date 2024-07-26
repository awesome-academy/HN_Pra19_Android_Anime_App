package com.example.anidb.screen.home.presenter

import com.example.anidb.data.model.Anime

interface HomeContract {
    interface Presenter {
        fun getPopularAnime(limit: Int, page: Int)
        fun getNewAnime(limit: Int, page: Int)
        fun getTopRateAnime(limit: Int, page: Int)
        fun getFavoriteAnime(limit: Int, page: Int)
    }

    interface View {
        fun onGetPopularAnimeSuccess(list: List<Anime>)
        fun onGetNewAnimeSuccess(list: List<Anime>)
        fun onGetTopRateAnimeSuccess(list: List<Anime>)
        fun onGetFavoriteAnimeSuccess(list: List<Anime>)
        fun onError(error: String)
    }
}
