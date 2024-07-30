package com.example.anidb.data.repository.source.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.anidb.data.model.Anime
import com.example.anidb.data.model.Genre
import com.example.anidb.data.model.Studio
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_AIRED_FROM
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_AIRED_TO
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_DESCRIPTION
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_DURATION
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_EPISODES
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_FAVORITES
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_GENRES
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_ID
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_IMAGE
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_MEMBERS
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_POPULARITY
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_RANK
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_SCORE
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_SEASON
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_STATUS
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_STUDIOS
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_TIMESTAMP
import com.example.anidb.data.repository.source.local.DBHelper.Companion.COLUMN_TITLE
import com.example.anidb.data.repository.source.local.DBHelper.Companion.TABLE_ANIME

class AnimeFavoriteDAO(context: Context) {
    private val dbHelper: DBHelper = DBHelper(context)

    fun insertAnime(anime: Anime): Long {
        val db = dbHelper.writableDatabase
        val values =
            ContentValues().apply {
                put(COLUMN_ID, anime.id)
                put(COLUMN_TITLE, anime.title)
                put(COLUMN_IMAGE, anime.image)
                put(COLUMN_DESCRIPTION, anime.description)
                put(COLUMN_STATUS, anime.status)
                put(COLUMN_AIRED_TO, anime.airedTo)
                put(COLUMN_AIRED_FROM, anime.airedFrom)
                put(COLUMN_MEMBERS, anime.members)
                put(COLUMN_SCORE, anime.score)
                put(COLUMN_RANK, anime.rank)
                put(COLUMN_POPULARITY, anime.popularity)
                put(COLUMN_FAVORITES, anime.favorites)
                put(COLUMN_DURATION, anime.duration)
                put(COLUMN_SEASON, anime.season)
                put(COLUMN_EPISODES, anime.episodes)
                put(COLUMN_GENRES, anime.genres.joinToString(", ") { it.name })
                put(COLUMN_STUDIOS, anime.studios.joinToString(", ") { it.name })
            }
        return db.insert(TABLE_ANIME, null, values)
    }

    fun getListAnime(
        pageNumber: Int,
        pageSize: Int,
    ): List<Anime> {
        val db = dbHelper.readableDatabase
        val offset = (pageNumber - 1) * pageSize
        val cursor: Cursor =
            db.query(
                TABLE_ANIME,
                null,
                null,
                null,
                null,
                null,
                "$COLUMN_TIMESTAMP DESC",
                "$offset, $pageSize",
            )

        val animeList = mutableListOf<Anime>()
        with(cursor) {
            while (moveToNext()) {
                val anime =
                    Anime(
                        id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                        title = getString(getColumnIndexOrThrow(COLUMN_TITLE)),
                        image = getString(getColumnIndexOrThrow(COLUMN_IMAGE)),
                        description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        status = getString(getColumnIndexOrThrow(COLUMN_STATUS)),
                        airedTo = getString(getColumnIndexOrThrow(COLUMN_AIRED_TO)),
                        airedFrom = getString(getColumnIndexOrThrow(COLUMN_AIRED_FROM)),
                        members = getInt(getColumnIndexOrThrow(COLUMN_MEMBERS)),
                        score = getDouble(getColumnIndexOrThrow(COLUMN_SCORE)),
                        rank = getInt(getColumnIndexOrThrow(COLUMN_RANK)),
                        popularity = getInt(getColumnIndexOrThrow(COLUMN_POPULARITY)),
                        favorites = getInt(getColumnIndexOrThrow(COLUMN_FAVORITES)),
                        duration = getString(getColumnIndexOrThrow(COLUMN_DURATION)),
                        season = getString(getColumnIndexOrThrow(COLUMN_SEASON)),
                        episodes = getInt(getColumnIndexOrThrow(COLUMN_EPISODES)),
                        genres =
                            getString(getColumnIndexOrThrow(COLUMN_GENRES)).split(", ")
                                .map { Genre(name = it) },
                        studios =
                            getString(getColumnIndexOrThrow(COLUMN_STUDIOS)).split(", ")
                                .map { Studio(name = it) },
                    )
                animeList.add(anime)
            }
        }
        cursor.close()
        return animeList
    }

    fun deleteAnime(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        val deletedRows =
            db.delete(
                TABLE_ANIME,
                "$COLUMN_ID = ?",
                arrayOf(id.toString()),
            )
        return deletedRows > 0
    }

    fun isAnimeExists(animeId: Int): Boolean {
        val db = dbHelper.readableDatabase
        val cursor =
            db.query(
                TABLE_ANIME,
                arrayOf(COLUMN_ID),
                "$COLUMN_ID = ?",
                arrayOf(animeId.toString()),
                null,
                null,
                null,
            )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}
