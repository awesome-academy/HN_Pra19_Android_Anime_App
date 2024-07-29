package com.example.anidb.screen.detail.presenter

import com.example.anidb.data.model.Anime
import com.example.anidb.data.model.AnimeRelations

interface DetailContract {
    interface Presenter {
        fun getRelationsAnime(id: Int)

        fun getDetailAnime(id: Int)
    }

    interface View {
        fun onError(error: String)

        fun onGetRelationsAnimeSuccess(list: List<AnimeRelations>)

        fun onGetDetailAnimeSuccess(anime: Anime)

        fun onAnimeDetailsFetched(data: List<Anime>)
    }
}
