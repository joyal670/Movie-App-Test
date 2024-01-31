package com.movieapp.ui.main.add_movie.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.movieapp.databinding.LayoutAddImageBinding


class AddMovieImageListAdapter(private val list: ArrayList<Uri>, private var onClickMovie: (Int) -> Unit) : RecyclerView.Adapter<AddMovieImageListAdapter.ViewHold>() {
    private var context: Context? = null

    class ViewHold(var binding: LayoutAddImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutAddImageBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        Glide.with(context!!)
            .load(list[position])
            .into(holder.binding.ivMovie)

        holder.binding.tvRemove.setOnClickListener {
            onClickMovie.invoke(position)
        }
    }
}
