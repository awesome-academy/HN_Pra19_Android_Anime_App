package com.example.anidb.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anidb.R
import com.example.anidb.data.model.Anime
import com.example.anidb.data.repository.AnimeRepository
import com.example.anidb.data.repository.source.local.AnimeLocalDataSource
import com.example.anidb.data.repository.source.remote.AnimeRemoteDataSource
import com.example.anidb.databinding.FragmentHomeBinding
import com.example.anidb.databinding.HeaderHomeBinding
import com.example.anidb.screen.home.adapter.AnimeHomeAdapter
import com.example.anidb.screen.home.presenter.HomeContract
import com.example.anidb.screen.home.presenter.HomePresenter
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment(), HomeContract.View {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var headerBinding: HeaderHomeBinding
    private lateinit var homePresenter: HomePresenter
    private lateinit var popularAdapter: AnimeHomeAdapter
    private lateinit var newAdapter: AnimeHomeAdapter
    private lateinit var topRateAdapter: AnimeHomeAdapter
    private lateinit var favoriteAdapter: AnimeHomeAdapter
    private var isScroll = false
    private var isTabSelected = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        headerBinding = binding.layoutHeader
        initView()
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        listenerChangeLanguageClick()
        listenerSearchClick()
        listenerTabLayout()
        listenerScrollView()
    }

    override fun onGetPopularAnimeSuccess(list: List<Anime>) {
        popularAdapter.setData(list)
    }

    override fun onGetNewAnimeSuccess(list: List<Anime>) {
        newAdapter.setData(list)
    }

    override fun onGetTopRateAnimeSuccess(list: List<Anime>) {
        topRateAdapter.setData(list)
    }

    override fun onGetFavoriteAnimeSuccess(list: List<Anime>) {
        favoriteAdapter.setData(list)
    }

    override fun onError(error: String) {
        // TODO()
    }

    private fun initData() {
        homePresenter =
            HomePresenter(
                AnimeRepository.getInstance(
                    AnimeRemoteDataSource.getInstance(),
                    AnimeLocalDataSource.getInstance(),
                ),
            )
        homePresenter.setView(this)
        homePresenter.getPopularAnime(ANIME_LIMIT, FIRST_PAGE)
        homePresenter.getNewAnime(ANIME_LIMIT, FIRST_PAGE)
        homePresenter.getTopRateAnime(ANIME_LIMIT, FIRST_PAGE)
        homePresenter.getFavoriteAnime(ANIME_LIMIT, FIRST_PAGE)
    }

    private fun initView() {
        binding.apply {
            popularAdapter = AnimeHomeAdapter(emptyList())
            newAdapter = AnimeHomeAdapter(emptyList())
            topRateAdapter = AnimeHomeAdapter(emptyList())
            favoriteAdapter = AnimeHomeAdapter(emptyList())
            rvPopular.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvNew.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvRate.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvFavorite.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

            rvPopular.adapter = popularAdapter
            rvNew.adapter = newAdapter
            rvRate.adapter = topRateAdapter
            rvFavorite.adapter = favoriteAdapter

            popularAdapter.onItemClick = {
                onItemClick(it)
            }
            newAdapter.onItemClick = {
                onItemClick(it)
            }
            topRateAdapter.onItemClick = {
                onItemClick(it)
            }
            favoriteAdapter.onItemClick = {
                onItemClick(it)
            }

            popularAdapter.onSeeMoreClick = {
                onSeeMoreClick(POPULAR_INDEX)
            }
            newAdapter.onSeeMoreClick = {
                onSeeMoreClick(NEW_INDEX)
            }
            topRateAdapter.onSeeMoreClick = {
                onSeeMoreClick(RATE_INDEX)
            }
            favoriteAdapter.onSeeMoreClick = {
                onSeeMoreClick(FAVORITE_INDEX)
            }
        }
    }

    private fun onItemClick(anime: Anime) {
        Toast.makeText(activity, anime.title, Toast.LENGTH_SHORT).show()
    }

    private fun onSeeMoreClick(type: Int) {
        Toast.makeText(activity, "see more $type", Toast.LENGTH_SHORT).show()
    }

    private fun listenerSearchClick() {
        headerBinding.imgSearch.setOnClickListener {
        }
    }

    private fun listenerChangeLanguageClick() {
        headerBinding.imgFlag.setOnClickListener {
            // handle change language
        }
    }

    private fun listenerTabLayout() {
        binding.apply {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.popular))
            tabLayout.addTab(tabLayout.newTab().setText(R.string.New))
            tabLayout.addTab(tabLayout.newTab().setText(R.string.top_rate1))
            tabLayout.addTab(tabLayout.newTab().setText(R.string.favorite))
        }

        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    if (!isScroll) {
                        isTabSelected = true
                        val position = tab.position
                        val targetView: View? =
                            when (position) {
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
                    // TODO()
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                    // TODO()
                }
            },
        )
    }

    private fun listenerScrollView() {
        binding.apply {
            scrollView.setOnScrollChangeListener { _: NestedScrollView, _: Int, scrollY: Int, _: Int, _: Int ->
                if (!isTabSelected) {
                    isScroll = true
                    val position =
                        when {
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

    companion object {
        const val POPULAR_INDEX = 0
        const val NEW_INDEX = 1
        const val RATE_INDEX = 2
        const val FAVORITE_INDEX = 3
        const val LAST_ITEM_DISTANCE = 1000
        const val ANIME_LIMIT = 3
        const val FIRST_PAGE = 1
    }
}
