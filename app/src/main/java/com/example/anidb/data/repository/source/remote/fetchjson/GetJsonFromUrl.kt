package com.example.anidb.data.repository.source.remote.fetchjson

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.anidb.data.repository.source.remote.OnResultListener
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class GetJsonFromUrl<T>(
    private val urlString: String,
    private val keyEntity: String,
    private val listener: OnResultListener<T>,
    private val parseJsonToData: (String, String) -> T,
) {
    private val mExecutor: Executor = Executors.newSingleThreadExecutor()
    private val mHandler = Handler(Looper.getMainLooper())
    private var data: T? = null

    fun getAnimeData() {
        mExecutor.execute {
            val responseJson =
                getJsonStringFromUrl(urlString)
            data = parseJsonToData(responseJson, keyEntity)
            mHandler.post {
                data?.let { listener.onSuccess(it) }
            }
        }
    }

    private fun getJsonStringFromUrl(urlString: String): String {
        val stringBuilder = StringBuilder()
        try {
            val url = URL(urlString)
            val httpURLConnection = url.openConnection() as? HttpURLConnection
            httpURLConnection?.run {
                connectTimeout = TIME_OUT
                readTimeout = TIME_OUT
                requestMethod = METHOD_GET
                doOutput = true
                connect()
            }

            val bufferedReader = BufferedReader(InputStreamReader(url.openStream()))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            bufferedReader.close()
            httpURLConnection?.disconnect()
        } catch (e: FileNotFoundException) {
            Log.e("GetJsonFromUrl", "File not found: $urlString", e)
        } catch (e: IOException) {
            Log.e("GetJsonFromUrl", "Error reading from URL: $urlString", e)
        }
        return stringBuilder.toString()
    }

    companion object {
        private const val TIME_OUT = 15000
        private const val METHOD_GET = "GET"
        private val instanceMap = mutableMapOf<String, GetJsonFromUrl<*>>()

        fun <T> getInstance(
            urlString: String,
            keyEntity: String,
            listener: OnResultListener<T>,
            parseJsonToData: (String, String) -> T,
        ): GetJsonFromUrl<T> {
            val key = "$urlString-$keyEntity-$listener"
            return instanceMap.getOrPut(key) {
                GetJsonFromUrl(urlString, keyEntity, listener, parseJsonToData)
            } as GetJsonFromUrl<T>
        }
    }
}
