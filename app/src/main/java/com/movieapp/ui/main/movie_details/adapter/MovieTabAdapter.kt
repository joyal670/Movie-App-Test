package com.movieapp.ui.main.movie_details.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.movieapp.model.movie_details.Movy
import com.movieapp.ui.main.movie_details.fragment.MovieFragment


class MovieTabAdapter(activity: AppCompatActivity, val list: ArrayList<Movy>) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2 // Adjust the number of tabs as needed
    }

    override fun createFragment(position: Int): Fragment {
        // Depending on your requirements, you can pass different data to each fragment
        return when (position) {
            0 -> MovieFragment.newInstance(getMoviesForTab(position))
            1 -> MovieFragment.newInstance(getMoviesForTab(position))
            else -> throw IllegalArgumentException("Invalid tab position: $position")
        }
    }


    private fun getMoviesForTab(position: Int): List<Movy> {
        // Sort movies by rating in ascending order and descending order
        return if (position == 0) {
            list.sortedByDescending { it.movie_rating }
        } else {
            list.sortedBy { it.movie_rating }
        }
    }
}