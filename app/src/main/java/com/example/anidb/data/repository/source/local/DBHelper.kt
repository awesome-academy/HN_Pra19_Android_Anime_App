package com.example.anidb.data.repository.source.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createAnimeTable = (
            "CREATE TABLE $TABLE_ANIME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_IMAGE TEXT," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_STATUS TEXT," +
                "$COLUMN_AIRED_TO TEXT," +
                "$COLUMN_AIRED_FROM TEXT," +
                "$COLUMN_MEMBERS INTEGER," +
                "$COLUMN_SCORE REAL," +
                "$COLUMN_RANK INTEGER," +
                "$COLUMN_POPULARITY INTEGER," +
                "$COLUMN_FAVORITES INTEGER," +
                "$COLUMN_DURATION TEXT," +
                "$COLUMN_SEASON TEXT," +
                "$COLUMN_EPISODES INTEGER," +
                "$COLUMN_GENRES TEXT," +
                "$COLUMN_STUDIOS TEXT," +
                "$COLUMN_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
        )
        db?.execSQL(createAnimeTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int,
    ) {
        // TODO("Not yet implemented")
    }

    companion object {
        private const val DATABASE_NAME = "anime.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_ANIME = "anime_favorite"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_STATUS = "status"
        const val COLUMN_AIRED_TO = "airedTo"
        const val COLUMN_AIRED_FROM = "airedFrom"
        const val COLUMN_MEMBERS = "members"
        const val COLUMN_SCORE = "score"
        const val COLUMN_RANK = "rank"
        const val COLUMN_POPULARITY = "popularity"
        const val COLUMN_FAVORITES = "favorites"
        const val COLUMN_DURATION = "duration"
        const val COLUMN_SEASON = "season"
        const val COLUMN_EPISODES = "episodes"
        const val COLUMN_GENRES = "genres"
        const val COLUMN_STUDIOS = "studios"
        const val COLUMN_TIMESTAMP = "timestamp"
    }
}
