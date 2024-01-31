package com.movieapp.ui.main.movie_details.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.movieapp.base.BaseFragment
import com.movieapp.databinding.FragmentMovieBinding
import com.movieapp.model.movie_details.Movy
import com.movieapp.ui.main.movie_details.viewmodel.CategoryViewModal
import com.movieapp.ui.main.movie_details.adapter.MovieListAdapter

private const val ARG_MOVIES = "arg_movies"


class MovieFragment : BaseFragment<CategoryViewModal, FragmentMovieBinding>() {

    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var movieList: List<Movy>
    override fun initViews() {
        arguments?.let {
            movieList = it.getParcelableArrayList(ARG_MOVIES) ?: emptyList()
        }

        Log.e("TAG", "onCreate: "+ movieList )

        /* set adapter and layout manager for recyclerview */
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMovie.layoutManager = layoutManager
        movieListAdapter = MovieListAdapter(movieList)
        binding.rvMovie.adapter = movieListAdapter

        if(movieList.isEmpty()){
            binding.tvNoData.visibility = View.VISIBLE
        } else {
            binding.tvNoData.visibility = View.GONE
        }
    }

    override fun setUpObserver() {

    }

    override fun setOnClick() {

    }

    override fun getViewBinding(): FragmentMovieBinding {
       return  FragmentMovieBinding.inflate(layoutInflater)
    }

    companion object {
        @JvmStatic
        fun newInstance(movies: List<Movy>): MovieFragment {
            val fragment = MovieFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_MOVIES, ArrayList(movies))
            fragment.arguments = args
            return fragment
        }
    }
}