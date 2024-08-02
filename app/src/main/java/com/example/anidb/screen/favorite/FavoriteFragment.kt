package com.example.anidb.screen.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.anidb.R
import com.example.anidb.data.model.Anime
import com.example.anidb.data.repository.AnimeRepository
import com.example.anidb.data.repository.source.local.AnimeLocalDataSource
import com.example.anidb.data.repository.source.remote.AnimeRemoteDataSource
import com.example.anidb.databinding.FragmentListFavoriteBinding
import com.example.anidb.screen.detail.DetailFragment
import com.example.anidb.screen.favorite.adapter.AnimeFavoriteAdapter
import com.example.anidb.screen.favorite.presenter.FavoriteContract
import com.example.anidb.screen.favorite.presenter.FavoritePresenter
import com.example.anidb.utils.ext.replaceFragment

class FavoriteFragment : Fragment(), FavoriteContract.View {
    private lateinit var binding: FragmentListFavoriteBinding
    private lateinit var favoritePresenter: FavoritePresenter
    private lateinit var favoriteAdapter: AnimeFavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentListFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    override fun onGetFavoriteAnimeSuccess(list: List<Anime>) {
        favoriteAdapter.setData(list)
    }

    override fun onDeleteAnimeFavoriteSuccess(data: Boolean) {
        if (data) {
            // Cập nhật danh sách sau khi xóa thành công
            favoritePresenter.getFavoriteAnime(ANIME_LIMIT, FIRST_PAGE)
            favoriteAdapter.exitDeleteMode() // Tắt chế độ xóa sau khi xóa
        }
    }

    override fun onIsAnimeFavoriteSuccess(isFavorite: Boolean) {
        // Xử lý sự kiện khi kiểm tra trạng thái yêu thích của anime
    }

    override fun onError(error: String) {
        // Hiển thị thông báo lỗi hoặc thực hiện các hành động khác nếu cần
    }

    private fun initData() {
        favoritePresenter =
            FavoritePresenter(
                AnimeRepository.getInstance(
                    AnimeRemoteDataSource.getInstance(),
                    AnimeLocalDataSource.getInstance(requireContext()),
                ),
            )
        favoritePresenter.setView(this)
        favoritePresenter.getFavoriteAnime(ANIME_LIMIT, FIRST_PAGE)
    }

    private fun initView() {
        favoriteAdapter =
            AnimeFavoriteAdapter(
                mutableListOf(),
                ::onItemClick,
                ::onDeleteClick,
            )
        binding.recyclerView.adapter = favoriteAdapter
        binding.root.findViewById<ImageView>(R.id.back_button).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.root.findViewById<TextView>(R.id.toolbar_title).text = getString(R.string.favorite_anime)
    }

    private fun onItemClick(anime: Anime) {
        if (!favoriteAdapter.isDeleteMode) {
            replaceFragment(R.id.layoutContainer, DetailFragment.newInstance(anime), true)
        }
    }

    private fun onDeleteClick(animeId: Int) {
        favoritePresenter.deleteAnimeFavorite(animeId)
    }

    companion object {
        private const val ANIME_LIMIT = 20
        private const val FIRST_PAGE = 1
    }
}
