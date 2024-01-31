package com.movieapp.api



import com.movieapp.model.add_movie.AddMovieResponse
import com.movieapp.model.movie_category.MovieCategoryResponse
import com.movieapp.model.movie_details.MovieDetailsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class MainRepository(private val apiHelper: ApiService) {

    suspend fun getMovieCategory(): Response<MovieCategoryResponse> =
        apiHelper.getMovieCategory()

    suspend fun getCategoryDetails(categoryId: Int): Response<MovieDetailsResponse> =
        apiHelper.getCategoryDetails(categoryId)

    suspend fun addMovie(
        params: Map<String, RequestBody>,
        images: List<MultipartBody.Part>,
    ): Response<AddMovieResponse> = apiHelper.addMovie(params, images)



}
