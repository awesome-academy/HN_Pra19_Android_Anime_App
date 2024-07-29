package com.example.anidb.screen.detail.presenter

import com.example.anidb.data.model.Anime
import com.example.anidb.data.model.AnimeRelations
import com.example.anidb.data.repository.AnimeRepository
import com.example.anidb.data.repository.source.remote.OnResultListener
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class DetailPresenter(private val animeRepository: AnimeRepository) : DetailContract.Presenter {
    private var hView: DetailContract.View? = null

    // Khai báo ExecutorService để thực hiện các tác vụ đồng thời
    private val executor = Executors.newFixedThreadPool(4)

    fun setView(view: DetailContract.View) {
        this.hView = view
    }

    override fun getRelationsAnime(id: Int) {
        animeRepository.getAnimeRelations(
            id,
            object : OnResultListener<List<AnimeRelations>> {
                override fun onSuccess(data: List<AnimeRelations>) {
                    val filteredData = data.filter { it.entry.any { entry -> entry.type == "anime" } }
                    hView?.onGetRelationsAnimeSuccess(filteredData)

                    val entries = filteredData.flatMap { it.entry }.filter { it.type == "anime" }

                    // Tạo một danh sách để lưu kết quả và sử dụng CountDownLatch để đợi tất cả các tác vụ hoàn thành
                    val animeDetails = mutableListOf<Anime>()
                    val countDownLatch = CountDownLatch(entries.size)

                    entries.forEach { entry ->
                        getDetailAnime(entry.mal_id) { anime ->
                            if (anime != null && anime.title.isNotEmpty() && anime.image.isNotEmpty()) {
                                synchronized(animeDetails) {
                                    animeDetails.add(anime)
                                }
                            }
                            countDownLatch.countDown()
                        }
                    }

                    // Đợi tất cả các tác vụ hoàn thành và cập nhật giao diện người dùng
                    executor.execute {
                        try {
                            countDownLatch.await() // Đợi cho đến khi tất cả các tác vụ hoàn tất
                            // Chuyển đến luồng chính để cập nhật giao diện người dùng
                            // Cần một cách để chuyển đổi về luồng chính, ví dụ như một handler
                            android.os.Handler(android.os.Looper.getMainLooper()).post {
                                hView?.onAnimeDetailsFetched(animeDetails)
                            }
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onError(exception: Exception?) {
                    hView?.onError(exception.toString())
                }
            },
        )
    }

    private fun getDetailAnime(
        id: Int,
        callback: (Anime?) -> Unit,
    ) {
        executor.execute {
            animeRepository.getAnimeDetail(
                id,
                object : OnResultListener<Anime> {
                    override fun onSuccess(data: Anime) {
                        callback(data)
                    }

                    override fun onError(exception: Exception?) {
                        callback(null) // Hoặc xử lý lỗi ở đây
                    }
                },
            )
        }
    }

    override fun getDetailAnime(id: Int) {
        animeRepository.getAnimeDetail(
            id,
            object : OnResultListener<Anime> {
                override fun onSuccess(data: Anime) {
                    // Chuyển đến luồng chính để cập nhật giao diện người dùng
                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                        hView?.onGetDetailAnimeSuccess(data)
                    }
                }

                override fun onError(exception: Exception?) {
                    hView?.onError(exception.toString())
                }
            },
        )
    }
}
