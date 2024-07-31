package com.example.anidb.screen.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anidb.data.model.Anime
import com.example.anidb.databinding.ItemLayoutBinding

class AnimeRelateAdapter(private var animeList: List<Anime>, private val onItemClickListener: (Anime) -> Unit) :
    RecyclerView.Adapter<AnimeRelateAdapter.AnimeRelateViewHolder>() {
    inner class AnimeRelateViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(anime: Anime) {
            binding.txtTitle.text = anime.title
            binding.txtGenre.text = anime.genres.joinToString(", ") { it.name }
            binding.txtScore.text = anime.score.toString()

            Glide.with(binding.root.context)
                .load(anime.image)
                .into(binding.imgPoster)

            binding.root.setOnClickListener {
                onItemClickListener(anime)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AnimeRelateViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeRelateViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AnimeRelateViewHolder,
        position: Int,
    ) {
        holder.bind(animeList[position])
    }

    override fun getItemCount(): Int {
        return animeList.size
    }

    fun updateData(newAnimeList: List<Anime>) {
        animeList = newAnimeList.filter { it.title.isNotEmpty() && it.image.isNotEmpty() }
        notifyDataSetChanged()
    }
}
