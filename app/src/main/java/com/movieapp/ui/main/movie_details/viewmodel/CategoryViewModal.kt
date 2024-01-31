package com.movieapp.ui.main.movie_details.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieapp.api.ApiRepositoryProvider
import com.movieapp.api.Resource
import com.movieapp.model.add_movie.AddMovieResponse
import com.movieapp.model.movie_details.MovieDetailsResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class CategoryViewModal() : ViewModel() {

    private val mutableLiveDataMovieDetails = MutableLiveData<Resource<MovieDetailsResponse>>()
    val movieCategoryDetailsLiveData: LiveData<Resource<MovieDetailsResponse>> get() = mutableLiveDataMovieDetails

    fun getCategoryDetails(categoryId: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        mutableLiveDataMovieDetails.postValue(Resource.loading(null))
        viewModelScope.launch {

            try {
                repository.getCategoryDetails(categoryId).let {
                    val response = it.body()!!

                    if (response.success) {
                        mutableLiveDataMovieDetails.postValue(Resource.success(response))
                    } else {
                        mutableLiveDataMovieDetails.postValue(
                            Resource.error(
                                response.message, null
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                mutableLiveDataMovieDetails.postValue(Resource.error("Something went wrong", null))
            }
        }

    }

    private val mutableLiveDataAddMovie = MutableLiveData<Resource<AddMovieResponse>>()
    val addMovieDetailsLiveData: LiveData<Resource<AddMovieResponse>> get() = mutableLiveDataAddMovie

    fun addMovie(params: Map<String, RequestBody>, image: Uri, context: Context) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        mutableLiveDataAddMovie.postValue(Resource.loading(null))
        viewModelScope.launch {

            try {

                val imageList: ArrayList<MultipartBody.Part> = ArrayList()

                val inputStream = context.contentResolver.openInputStream(image)
                val requestImageBody =
                    inputStream?.readBytes()?.toRequestBody("*/*".toMediaTypeOrNull())
                val imagePart =
                    MultipartBody.Part.createFormData("movie_image", image.toString(), requestImageBody!!)
                imageList.add(imagePart)
                inputStream.close()



                repository.addMovie(params, imageList).let {
                    val response = it.body()!!

                    if (response.success) {
                        mutableLiveDataAddMovie.postValue(Resource.success(response))
                    } else {
                        mutableLiveDataAddMovie.postValue(
                            Resource.error(
                                response.message, null
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                Log.e("TAG", "addMovie: "+ ex)
                mutableLiveDataAddMovie.postValue(Resource.error("Something went wrong", null))
            }
        }

    }

}
