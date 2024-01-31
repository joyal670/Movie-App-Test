package com.movieapp.model.movie_category

data class MovieCategoryResponse(
    val movie_categories: List<MovieCategory>,
    val success: Boolean,
    val message: String
)