package com.movieapp.ui.main.movie_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.movieapp.R
import com.movieapp.databinding.LayoutMovieListBinding
import com.movieapp.model.movie_details.Movy


class MovieListAdapter(private val list: List<Movy>) :
    RecyclerView.Adapter<MovieListAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: LayoutMovieListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutMovieListBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        Glide.with(context!!).load(list[position].movie_image)
            .placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_image_placeholder)
            .into(holder.binding.ivMovie)

        holder.binding.tvMovieTitle.text = list[position].movie_name
        holder.binding.movieRating.rating = list[position].movie_rating.toFloat()
        holder.binding.tvDescription.text = list[position].movie_description


    }
}
