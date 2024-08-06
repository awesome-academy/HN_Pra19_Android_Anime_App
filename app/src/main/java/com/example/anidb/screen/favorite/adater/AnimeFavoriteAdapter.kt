package com.example.anidb.screen.favorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anidb.data.model.Anime
import com.example.anidb.databinding.ItemAnimeFavoriteBinding

class AnimeFavoriteAdapter(
    private val animeList: MutableList<Anime>,
    private val onItemClickListener: (Anime) -> Unit,
    private val onDeleteClickListener: (Int) -> Unit,
) : RecyclerView.Adapter<AnimeFavoriteAdapter.AnimeFavoriteViewHolder>() {
    var isDeleteMode = false
    private var selectedPosition = RecyclerView.NO_POSITION

    inner class AnimeFavoriteViewHolder(private val binding: ItemAnimeFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnLongClickListener {
                // Bật chế độ xóa khi nhấn giữ
                if (!isDeleteMode) {
                    isDeleteMode = true
                    selectedPosition = adapterPosition
                    notifyDataSetChanged()
                }
                true
            }

            binding.txtDelete.setOnClickListener {
                val animeId = animeList[adapterPosition].id
                onDeleteClickListener(animeId)
            }

            // Thoát chế độ xóa khi nhấn vào item nếu đang ở chế độ xóa
            binding.root.setOnClickListener {
                if (isDeleteMode) {
                    exitDeleteMode()
                } else {
                    onItemClickListener(animeList[adapterPosition])
                }
            }
        }

        fun bind(anime: Anime) {
            binding.txtTitle.text = anime.title
            binding.txtGenre.text = anime.genres.joinToString(", ") { it.name }
            binding.txtScore.text = anime.score.toString()

            Glide.with(binding.root.context)
                .load(anime.image)
                .into(binding.imgPoster)

            // Hiển thị hoặc ẩn nút xóa và xử lý sự kiện click
            if (isDeleteMode && adapterPosition == selectedPosition) {
                binding.txtDelete.visibility = View.VISIBLE
                binding.root.isClickable = false // Vô hiệu hóa click khi ở chế độ xóa
            } else {
                binding.txtDelete.visibility = View.GONE
                binding.root.isClickable = true // Kích hoạt lại click khi không ở chế độ xóa
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AnimeFavoriteViewHolder {
        val binding =
            ItemAnimeFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return AnimeFavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AnimeFavoriteViewHolder,
        position: Int,
    ) {
        holder.bind(animeList[position])
    }

    override fun getItemCount(): Int = animeList.size

    fun setData(newAnimeList: List<Anime>) {
        animeList.clear()
        animeList.addAll(newAnimeList)
        notifyDataSetChanged()
    }

    fun exitDeleteMode() {
        isDeleteMode = false
        selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }
}
