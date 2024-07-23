package com.example.anidb.screen.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anidb.data.model.Anime
import com.example.anidb.databinding.ItemAnimeBinding
import com.example.anidb.databinding.ItemSeemoreBinding

class AnimeHomeAdapter(private var listAnime: List<Anime>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Anime) -> Unit)? = null
    var onSeeMoreClick: (() -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listAnime: List<Anime>) {
        this.listAnime = listAnime
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == listAnime.size) {
            VIEW_TYPE_SEE_MORE
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding =
                    ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding)
            }

            VIEW_TYPE_SEE_MORE -> {
                val binding =
                    ItemSeemoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SeeMoreViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return listAnime.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_ITEM) {
            (holder as ItemViewHolder).onBind(listAnime[position])
            holder.itemView.setOnClickListener {
                onItemClick?.invoke(listAnime[position])
            }
        } else {
            (holder as SeeMoreViewHolder).onBind()
            holder.itemView.setOnClickListener {
                onSeeMoreClick?.invoke()
            }
        }
    }

    inner class ItemViewHolder(private val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun onBind(anime: Anime) {
            binding.apply {
                txtScore.text = anime.score.toString()
                txtTitle.text = anime.title
                txtGenre.text = anime.genres.joinToString(", ") { it.name }
                Glide.with(binding.root.context)
                    .load(anime.image)
                    .into(imgPoster)

                txtTitle.isSelected = true
                txtGenre.isSelected = true
            }
        }
    }

    inner class SeeMoreViewHolder(private val binding: ItemSeemoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
        }
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_SEE_MORE = 1
    }
}
