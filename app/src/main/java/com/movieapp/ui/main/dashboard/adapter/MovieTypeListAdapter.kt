package com.movieapp.ui.main.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.movieapp.R
import com.movieapp.databinding.LayoutMovieTypesListBinding
import com.movieapp.model.movie_category.MovieCategory


class MovieTypeListAdapter(private val movieList: ArrayList<MovieCategory>, private var onClickMovie: (Int) -> Unit) : RecyclerView.Adapter<MovieTypeListAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: LayoutMovieTypesListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutMovieTypesListBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        Glide.with(context!!)
            .load(movieList[position].category_image)
            .placeholder(R.drawable.ic_image_placeholder)
            .error(R.drawable.ic_image_placeholder)
            .into(holder.binding.ivMovie)

      holder.binding.tvMovieTitle.text = movieList[position].category_name


        holder.itemView.setOnClickListener {
            onClickMovie.invoke(position)
        }
    }
}
