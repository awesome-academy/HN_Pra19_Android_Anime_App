package com.example.anidb.screen.detail

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.anidb.R
import com.example.anidb.data.model.Anime
import com.example.anidb.data.model.AnimeRelations
import com.example.anidb.data.repository.AnimeRepository
import com.example.anidb.data.repository.source.local.AnimeLocalDataSource
import com.example.anidb.data.repository.source.remote.AnimeRemoteDataSource
import com.example.anidb.databinding.DetailAnimeBinding
import com.example.anidb.databinding.FragmentDetailBinding
import com.example.anidb.databinding.NavbarBinding
import com.example.anidb.screen.detail.adapter.AnimeRelateAdapter
import com.example.anidb.screen.detail.presenter.DetailContract
import com.example.anidb.screen.detail.presenter.DetailPresenter
import com.example.anidb.utils.base.BaseFragment

class DetailFragment : BaseFragment<FragmentDetailBinding>(), DetailContract.View {
    private lateinit var presenter: DetailPresenter
    private lateinit var animeRelateAdapter: AnimeRelateAdapter
    private var isHeartFilled = false
    private var isExpanded = false

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater)
    }

    override fun initView() {
//        animeRelateAdapter = AnimeRelateAdapter(emptyList())

        animeRelateAdapter =
            AnimeRelateAdapter(emptyList()) { anime ->
                val bundle =
                    Bundle().apply {
                        putSerializable("anime", anime)
                    }

                // Tạo và hiển thị DetailFragment với dữ liệu
                val detailFragment =
                    DetailFragment().apply {
                        arguments = bundle
                    }

                // Chuyển đến DetailFragment+
                parentFragmentManager.beginTransaction()
                    .replace(R.id.layoutContainer, detailFragment)
                    .addToBackStack(null)
                    .commit()
            }

        viewBinding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = animeRelateAdapter
        }

        // Gán binding cho navbar
        val navbarBinding = NavbarBinding.bind(viewBinding.root.findViewById(R.id.navbar))
        val detailAnimeBinding = DetailAnimeBinding.bind(viewBinding.root.findViewById(R.id.detail_anime))
        val seeMoreListener =
            View.OnClickListener {
                if (isExpanded) {
                    // Collapse the description
                    detailAnimeBinding.animeDescriptionTextView.maxLines = 3
                    detailAnimeBinding.animeDescriptionTextView.ellipsize = TextUtils.TruncateAt.END
                    detailAnimeBinding.seeMore.text = getString(R.string.see_more)
                    detailAnimeBinding.imgDown.setImageResource(R.drawable.ic_down) // Update to the "down" arrow icon
                } else {
                    // Expand the description
                    detailAnimeBinding.animeDescriptionTextView.maxLines = Int.MAX_VALUE
                    detailAnimeBinding.animeDescriptionTextView.ellipsize = null
                    detailAnimeBinding.seeMore.text = getString(R.string.see_less) // You can use a different string resource for "See Less"
                    detailAnimeBinding.imgDown.setImageResource(R.drawable.ic_up) // Update to the "up" arrow icon
                }
                isExpanded = !isExpanded
            }
        detailAnimeBinding.seeMore.setOnClickListener(seeMoreListener)
        detailAnimeBinding.imgDown.setOnClickListener(seeMoreListener)

        navbarBinding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        navbarBinding.heartIcon.setOnClickListener {
            if (isHeartFilled) {
                // Chuyển về hình trái tim rỗng
                navbarBinding.heartIcon.setImageResource(R.drawable.ic_empty_heart)
            } else {
                // Chuyển về hình trái tim đầy
                navbarBinding.heartIcon.setImageResource(R.drawable.ic_heart)
            }
            isHeartFilled = !isHeartFilled // Đảo ngược trạng thái
        }
    }

    override fun initData() {
        val anime = arguments?.getSerializable(ARG_ANIME) as? Anime

        presenter =
            DetailPresenter(
                AnimeRepository.getInstance(
                    AnimeRemoteDataSource.getInstance(),
                    AnimeLocalDataSource.getInstance(),
                ),
            )
        presenter.setView(this)

        anime?.let {
            presenter.getRelationsAnime(anime.id)

            val detailAnimeBinding = DetailAnimeBinding.bind(viewBinding.root.findViewById(R.id.detail_anime))

            detailAnimeBinding?.apply {
                animeTitleTextView.text = anime.title
                statusValue.text = anime.status
                scoreValue.text = anime.score.toString()
                popularityValue.text = anime.popularity.toString()
                durationValue.text = anime.duration
                studiosValue.text = anime.studios.joinToString(", ") { it.name }
                membersValue.text = anime.members.toString()
                rankValue.text = anime.rank.toString()
                favoritesValue.text = anime.favorites.toString()
                seasonValue.text = anime.season
                genresValue.text = anime.genres.joinToString(", ") { it.name }
                animeDescriptionTextView.text = anime.description
                airedFromValue.text = anime.airedFrom
                airedToValue.text = anime.airedTo

                Glide.with(requireContext())
                    .load(anime.image)
                    .into(animeImageView)
            }
        }
    }

    override fun onError(message: String) {
        // Xử lý hiển thị lỗi
    }

    override fun onGetRelationsAnimeSuccess(data: List<AnimeRelations>) {
        // Không cần thiết nếu bạn không xử lý trực tiếp dữ liệu này
    }

    override fun onGetDetailAnimeSuccess(anime: Anime) {
        TODO("Not yet implemented")
    }

    override fun onAnimeDetailsFetched(animeDetails: List<Anime>) {
        animeRelateAdapter.updateData(animeDetails)
    }

    companion object {
        private const val ARG_ANIME = "anime"

        fun newInstance(anime: Anime): DetailFragment {
            return DetailFragment().apply {
                arguments =
                    Bundle().apply {
                        putSerializable(ARG_ANIME, anime)
                    }
            }
        }
    }
}
