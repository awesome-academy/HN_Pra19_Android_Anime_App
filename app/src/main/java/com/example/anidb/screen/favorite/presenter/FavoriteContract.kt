package com.example.anidb.screen.favorite.presenter

import com.example.anidb.data.model.Anime

interface FavoriteContract {
    interface Presenter {
        fun getFavoriteAnime(
            limit: Int,
            page: Int,
        )

        fun deleteAnimeFavorite(id: Int)

        fun isAnimeFavorite(id: Int)
    }

    interface View {
        fun onGetFavoriteAnimeSuccess(list: List<Anime>)

        fun onDeleteAnimeFavoriteSuccess(data: Boolean)

        fun onIsAnimeFavoriteSuccess(isFavorite: Boolean)

        fun onError(error: String)
    }
}
