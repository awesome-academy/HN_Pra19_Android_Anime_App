package com.example.anidb.data.repository.source.remote.fetchjson

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

fun <T> parseJsonToListData(
    jsonObject: JSONObject?,
    keyEntity: String,
    parser: (JSONObject) -> T
): List<T> {
    val data = mutableListOf<T>()
    try {
        val jsonArray = jsonObject?.getJSONArray(keyEntity)
        for (i in 0 until (jsonArray?.length() ?: 0)) {
            val item = jsonArray?.getJSONObject(i)?.let { parser(it) }
            item?.let {
                data.add(it)
            }
        }
    } catch (e: JSONException) {
        Log.e("ParseDataWithJson", "parseJsonToData: ", e)
    }
    return data
}

fun <T> parseJsonToDetailData(
    jsonObject: JSONObject?,
    keyEntity: String,
    parser: (JSONObject) -> T
): T? {
    var data: T? = null
    try {
        data = jsonObject?.getJSONObject(keyEntity)?.let {
            parser(it)
        }
    } catch (e: JSONException) {
        Log.e("ParseDataWithJson", "parseJsonToData: ", e)
    }
    return data
}
