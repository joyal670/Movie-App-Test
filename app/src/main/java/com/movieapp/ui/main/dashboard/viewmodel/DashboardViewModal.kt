package com.movieapp.ui.main.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieapp.api.ApiRepositoryProvider
import com.movieapp.api.MainRepository
import com.movieapp.api.Resource
import com.movieapp.model.movie_category.MovieCategoryResponse
import com.movieapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModal @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val mutableLiveDataMovieCategory = MutableLiveData<Resource<MovieCategoryResponse>>()
    val dashboardMovieCategoryLiveData: LiveData<Resource<MovieCategoryResponse>> get() = mutableLiveDataMovieCategory

    fun getMovieCategory() {
        mutableLiveDataMovieCategory.postValue(Resource.loading(null))
        viewModelScope.launch {

            try {
                repository.getMovieCategory().let {
                    val response = it.body()!!

                    if (response.success) {
                        mutableLiveDataMovieCategory.postValue(Resource.success(response))
                    } else {
                        mutableLiveDataMovieCategory.postValue(
                            Resource.error(
                                response.message, null
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                mutableLiveDataMovieCategory.postValue(Resource.error("Something went wrong", null))
            }
        }

    }

}
