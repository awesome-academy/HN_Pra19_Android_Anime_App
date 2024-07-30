package com.example.anidb.data.repository.source.remote

import com.example.anidb.data.model.Anime
import com.example.anidb.data.model.AnimeRelations
import com.example.anidb.data.repository.source.AnimeDataSource
import com.example.anidb.data.repository.source.remote.fetchjson.GetJsonFromUrl
import com.example.anidb.data.repository.source.remote.fetchjson.ParseJson
import com.example.anidb.data.repository.source.remote.fetchjson.parseJsonToDetailData
import com.example.anidb.data.repository.source.remote.fetchjson.parseJsonToListData
import com.example.anidb.utils.BASE_FILTER_NO_ADULT
import com.example.anidb.utils.BASE_LIMIT
import com.example.anidb.utils.BASE_MIN_SCORE
import com.example.anidb.utils.BASE_NEW
import com.example.anidb.utils.BASE_PAGE
import com.example.anidb.utils.BASE_POPULAR
import com.example.anidb.utils.BASE_SORT_DESC
import com.example.anidb.utils.BASE_TOP_RATE
import com.example.anidb.utils.BASE_URL_DETAIL
import com.example.anidb.utils.BASE_URL_SEARCH
import com.example.anidb.utils.entry.ANIME
import org.json.JSONObject

class AnimeRemoteDataSource : AnimeDataSource.Remote {
    override fun getAnimePopular(
        limit: Int,
        page: Int,
        listener: OnResultListener<List<Anime>>,
    ) {
        GetJsonFromUrl.getInstance(
            urlString =
                BASE_URL_SEARCH +
                    BASE_POPULAR +
                    BASE_MIN_SCORE +
                    BASE_LIMIT + limit +
                    BASE_PAGE + page +
                    BASE_FILTER_NO_ADULT,
            keyEntity = ANIME,
            listener = listener,
            parseJsonToData = { responseJson, keyEntity ->
                parseJsonToListAnime(responseJson, keyEntity)
            },
        ).getAnimeData()
    }

    override fun getAnimeNew(
        limit: Int,
        page: Int,
        listener: OnResultListener<List<Anime>>,
    ) {
        GetJsonFromUrl.getInstance(
            urlString =
                BASE_URL_SEARCH +
                    BASE_NEW +
                    BASE_SORT_DESC +
                    BASE_MIN_SCORE +
                    BASE_LIMIT + limit +
                    BASE_PAGE + page +
                    BASE_FILTER_NO_ADULT,
            keyEntity = ANIME,
            listener = listener,
            parseJsonToData = { responseJson, keyEntity ->
                parseJsonToListAnime(responseJson, keyEntity)
            },
        ).getAnimeData()
    }

    override fun getAnimeTopRate(
        limit: Int,
        page: Int,
        listener: OnResultListener<List<Anime>>,
    ) {
        GetJsonFromUrl.getInstance(
            urlString =
                BASE_URL_SEARCH +
                    BASE_TOP_RATE +
                    BASE_SORT_DESC +
                    BASE_LIMIT + limit +
                    BASE_PAGE + page +
                    BASE_FILTER_NO_ADULT,
            keyEntity = ANIME,
            listener = listener,
            parseJsonToData = { responseJson, keyEntity ->
                parseJsonToListAnime(responseJson, keyEntity)
            },
        ).getAnimeData()
    }

    override fun getAnimeDetail(
        id: Int,
        listener: OnResultListener<Anime>,
    ) {
        GetJsonFromUrl.getInstance(
            urlString = BASE_URL_DETAIL + id,
            keyEntity = ANIME,
            listener = listener,
            parseJsonToData = { responseJson, keyEntity ->
                parseJsonToAnime(responseJson, keyEntity)
            },
        ).getAnimeData()
    }

    override fun getAnimeRelations(
        id: Int,
        listener: OnResultListener<List<AnimeRelations>>,
    ) {
        GetJsonFromUrl.getInstance(
            urlString = BASE_URL_DETAIL + id + "/relations",
            keyEntity = ANIME,
            listener = listener,
            parseJsonToData = { responseJson, keyEntity ->
                parseJsonToListAnimeRelations(responseJson, keyEntity)
            },
        ).getAnimeData()
    }

    private fun parseJsonToListAnimeRelations(
        responseJson: String,
        keyEntity: String,
    ): List<AnimeRelations> {
        return try {
            parseJsonToListData(JSONObject(responseJson), keyEntity) {
                ParseJson().animeRelationsParseJson(it)
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    private fun parseJsonToListAnime(
        responseJson: String,
        keyEntity: String,
    ): List<Anime> {
        return try {
            parseJsonToListData(JSONObject(responseJson), keyEntity) {
                ParseJson().animeParseJson(it)
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    private fun parseJsonToAnime(
        responseJson: String,
        keyEntity: String,
    ): Anime {
        return try {
            parseJsonToDetailData(JSONObject(responseJson), keyEntity) {
                ParseJson().animeParseJson(it)
            } as Anime
        } catch (e: Exception) {
            Anime()
        }
    }

    companion object {
        private var instance: AnimeRemoteDataSource? = null

        fun getInstance() =
            synchronized(this) {
                instance ?: AnimeRemoteDataSource().also { instance = it }
            }
    }
}
