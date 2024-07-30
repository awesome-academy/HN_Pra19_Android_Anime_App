package com.example.anidb.data.repository.source.local

import com.example.anidb.data.repository.source.AnimeDataSource

class AnimeLocalDataSource : AnimeDataSource.Local {
    companion object {
        private var instance: AnimeLocalDataSource? = null

        fun getInstance() =
            synchronized(this) {
                instance ?: AnimeLocalDataSource().also { instance = it }
            }
    }
}
