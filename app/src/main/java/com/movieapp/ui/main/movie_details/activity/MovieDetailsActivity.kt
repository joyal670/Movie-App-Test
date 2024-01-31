package com.movieapp.ui.main.movie_details.activity

import android.util.Log
import com.google.android.material.tabs.TabLayoutMediator
import com.movieapp.R
import com.movieapp.api.Status
import com.movieapp.base.BaseActivity
import com.movieapp.databinding.ActivityMovieDetailsBinding
import com.movieapp.interfaces.DialogListener
import com.movieapp.model.movie_details.Movy
import com.movieapp.ui.main.add_movie.dialog.AddMovieModal
import com.movieapp.ui.main.movie_details.viewmodel.CategoryViewModal
import com.movieapp.ui.main.movie_details.adapter.MovieTabAdapter
import com.movieapp.utils.CommonUtils
import com.movieapp.utils.Constants
import com.movieapp.utils.isConnected

class MovieDetailsActivity : BaseActivity<CategoryViewModal, ActivityMovieDetailsBinding>(), DialogListener {

    private lateinit var movieTabAdapter: MovieTabAdapter
    private var list = ArrayList<Movy>()
    private var movieId = 0
    private var categoryName = ""

    override fun setOnClick() {

        binding.fabAddMovie.setOnClickListener {
            val addMovieModal = AddMovieModal(categoryName, this, movieId)
            addMovieModal.show(supportFragmentManager, addMovieModal.tag)

        }
    }


    override fun initViews() {
        getIntentData()
        setUpObserver()

        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.toolbarTitle.text = categoryName

        viewModel.getCategoryDetails(movieId)


    }

    private fun getIntentData() {
        movieId = intent.getIntExtra(Constants.MOVIE_ID, -1)
        categoryName = intent.getStringExtra(Constants.CATEGORY_NAME)!!
    }

    private fun setUpObserver() {
        viewModel.movieCategoryDetailsLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    if (it.data != null) {

                        list.clear()
                        list.addAll(it.data.movies)

                        // set tab layout and adapter
                        movieTabAdapter = MovieTabAdapter(this, list)
                        binding.vpMovie.adapter = movieTabAdapter
                        // Use TabLayoutMediator to connect TabLayout with ViewPager2
                        TabLayoutMediator(binding.tabs, binding.vpMovie) { tab, position ->
                            when (position) {
                                0 -> tab.text = "Top Rated"
                                1 -> tab.text = "Low Rated"
                            }
                        }.attach()

                    }


                }

                Status.ERROR -> {
                    dismissProgress()
                    if (isConnected) {
                        CommonUtils.showErrorSnackBar(it.message.toString(), binding.root, this)
                    } else {
                        CommonUtils.showErrorSnackBar(
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

    override fun getViewBinding(): ActivityMovieDetailsBinding {
        return ActivityMovieDetailsBinding.inflate(layoutInflater)
    }

    override fun onCloseDialog() {
        viewModel.getCategoryDetails(movieId)
    }

}