package com.movieapp.ui.main.dashboard.activity

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.movieapp.R
import com.movieapp.api.Status
import com.movieapp.base.BaseActivity
import com.movieapp.databinding.ActivityDashboardBinding
import com.movieapp.model.movie_category.MovieCategory
import com.movieapp.ui.main.dashboard.adapter.MovieTypeListAdapter
import com.movieapp.ui.main.dashboard.viewmodel.DashboardViewModal
import com.movieapp.ui.main.movie_details.viewmodel.CategoryViewModal
import com.movieapp.ui.main.movie_details.activity.MovieDetailsActivity
import com.movieapp.utils.CommonUtils.Companion.showErrorSnackBar
import com.movieapp.utils.Constants
import com.movieapp.utils.isConnected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseActivity<DashboardViewModal, ActivityDashboardBinding>() {

    private lateinit var movieListAdapter: MovieTypeListAdapter
    private var movieList = ArrayList<MovieCategory>()
    override fun setOnClick() {

    }

    override fun initViews() {

        setUpObserver()

        binding.toolbar.toolbarTitle.text = getString(R.string.movies)

        /* set adapter and layout manager for recyclerview */
        val layoutManager = GridLayoutManager(this, 2)
        binding.rvMovieList.layoutManager = layoutManager
        movieListAdapter = MovieTypeListAdapter(movieList) { pos: Int -> onClickMovie(pos) }
        binding.rvMovieList.adapter = movieListAdapter

        viewModel.getMovieCategory()


    }

    /* on click, open movie details screen */
    private fun onClickMovie(pos: Int) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(Constants.MOVIE_ID, movieList[pos].id)
        intent.putExtra(Constants.CATEGORY_NAME, movieList[pos].category_name)
        startActivity(intent)
    }

    private fun setUpObserver() {
        viewModel.dashboardMovieCategoryLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    movieList.clear()
                    movieList.addAll(it.data!!.movie_categories)
                    movieListAdapter.notifyDataSetChanged()
                }

                Status.ERROR -> {
                    dismissProgress()
                    if (isConnected) {
                        showErrorSnackBar(it.message.toString(), binding.root, this)
                    } else {
                        showErrorSnackBar(
                            getString(R.string.please_check_internet), binding.root, this
                        )
                    }

                }

                Status.LOADING -> {
                    showProgress()
                }

            }
        }
    }

    override fun getViewBinding(): ActivityDashboardBinding {
        return ActivityDashboardBinding.inflate(layoutInflater)
    }

}