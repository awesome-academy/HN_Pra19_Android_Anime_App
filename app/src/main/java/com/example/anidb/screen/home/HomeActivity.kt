package com.example.anidb.screen.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anidb.R
import com.example.anidb.data.model.Anime
import com.example.anidb.data.model.Genre
import com.example.anidb.databinding.ActivityHomeBinding
import com.example.anidb.screen.home.adapter.AnimeHomeAdapter
import com.example.anidb.screen.home.presenter.HomeContract
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity(), HomeContract.View {
    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: AnimeHomeAdapter
    private var isScroll = false
    private var isTabSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        createAdapter()
        initFakeData()
        listenerTabLayout()
        listenerScrollView()
    }

    override fun onGetPopularAnimeSuccess(list: List<Anime>) {
    }

    override fun onGetNewAnimeSuccess(list: List<Anime>) {
    }

    override fun onGetTopRateAnimeSuccess(list: List<Anime>) {
    }

    override fun onGetFavoriteAnimeSuccess(list: List<Anime>) {
    }

    override fun onError(error: String) {
    }

    private fun createAdapter() {
        binding.apply {
            adapter = AnimeHomeAdapter(emptyList())
            rvPopular.layoutManager =
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            rvNew.layoutManager =
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            rvRate.layoutManager =
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            rvFavorite.layoutManager =
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)

            rvPopular.adapter = adapter
            rvNew.adapter = adapter
            rvRate.adapter = adapter
            rvFavorite.adapter = adapter

            adapter.onItemClick = {
                Toast.makeText(this@HomeActivity, it.title, Toast.LENGTH_SHORT).show()
            }
            adapter.onSeeMoreClick = {
                Toast.makeText(this@HomeActivity, "See More", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun listenerTabLayout() {
        binding.apply {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.popular))
            tabLayout.addTab(tabLayout.newTab().setText(R.string.New))
            tabLayout.addTab(tabLayout.newTab().setText(R.string.top_rate1))
            tabLayout.addTab(tabLayout.newTab().setText(R.string.favorite))
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (!isScroll) {
                    isTabSelected = true
                    val position = tab.position
                    val targetView: View? = when (position) {
                        POPULAR_INDEX -> binding.txtPopular
                        NEW_INDEX -> binding.txtNew
                        RATE_INDEX -> binding.txtRate
                        FAVORITE_INDEX -> binding.txtFavorite
                        else -> null
                    }

                    targetView?.let {
                        binding.scrollView.post {
                            binding.scrollView.smoothScrollTo(0, it.top)
                            isTabSelected = false
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun listenerScrollView() {
        binding.apply {
            scrollView.setOnScrollChangeListener { _: NestedScrollView, _: Int, scrollY: Int, _: Int, _: Int ->
                if (!isTabSelected) {
                    isScroll = true
                    val position = when {
                        scrollY >= txtFavorite.top - LAST_ITEM_DISTANCE -> FAVORITE_INDEX
                        scrollY >= txtRate.top -> RATE_INDEX
                        scrollY >= txtNew.top -> NEW_INDEX
                        else -> POPULAR_INDEX
                    }
                    if (tabLayout.selectedTabPosition != position) {
                        tabLayout.getTabAt(position)?.select()
                    }
                    isScroll = false
                }
            }
        }
    }

    private fun initFakeData() {
        val listAnime = listOf(
            Anime(
                id = 0,
                title = "Jujutsu Kaisen",
                image = "https://cdn.myanimelist.net/images/anime/1171/109222l.jpg",
                description = "",
                status = "",
                airedTo = "",
                airedFrom = "",
                members = "",
                score = 8.0,
                rank = 0,
                popularity = 0,
                favorites = 0,
                duration = "",
                season = "",
                episodes = 0,
                genres = listOf(Genre(1, "Action"), Genre(2, "Adventure"), Genre(3, "Comedy")),
                studios = listOf()
            ),
            Anime(
                id = 0,
                title = "Jujutsu Kaisen 0 Movie",
                image = "https://cdn.myanimelist.net/images/anime/1121/119044l.jpg",
                description = "",
                status = "",
                airedTo = "",
                airedFrom = "",
                members = "",
                score = 8.9,
                rank = 0,
                popularity = 0,
                favorites = 0,
                duration = "",
                season = "",
                episodes = 0,
                genres = listOf(Genre(1, "Action"), Genre(2, "Adventure"), Genre(3, "Comedy")),
                studios = listOf()
            ),
            Anime(
                id = 0,
                title = "Jujutsu Kaisen 2nd Season",
                image = "https://cdn.myanimelist.net/images/anime/1792/138022l.jpg",
                description = "",
                status = "",
                airedTo = "",
                airedFrom = "",
                members = "",
                score = 9.2,
                rank = 0,
                popularity = 0,
                favorites = 0,
                duration = "",
                season = "",
                episodes = 0,
                genres = listOf(Genre(1, "Action"), Genre(2, "Adventure"), Genre(3, "Comedy")),
                studios = listOf()
            )
        )
        adapter.setData(listAnime)
    }

    companion object {
        const val POPULAR_INDEX = 0
        const val NEW_INDEX = 1
        const val RATE_INDEX = 2
        const val FAVORITE_INDEX = 3
        const val LAST_ITEM_DISTANCE = 1000
    }
}
