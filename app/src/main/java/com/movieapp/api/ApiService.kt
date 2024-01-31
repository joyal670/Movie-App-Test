package com.movieapp.api


import com.movieapp.model.add_movie.AddMovieResponse
import com.movieapp.model.movie_category.MovieCategoryResponse
import com.movieapp.model.movie_details.MovieDetailsResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("get-all-categories")
    suspend fun getMovieCategory(): Response<MovieCategoryResponse>

    @GET("get-category-movies")
    suspend fun getCategoryDetails(@Query("category_id") categoryId: Int): Response<MovieDetailsResponse>

    @Multipart
    @POST("add-new-movie")
    suspend fun addMovie(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Response<AddMovieResponse>


    companion object {
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .client(initializeClient())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl("https://appcomdev.com/appcomtest/public/api/")
                .build()
            return retrofit.create(ApiService::class.java)

        }

        private fun initializeClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            System.setProperty("http.keepAlive", "false")
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(interceptor)
                .addInterceptor(SupportInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)

            return builder.build()
        }
    }
}
