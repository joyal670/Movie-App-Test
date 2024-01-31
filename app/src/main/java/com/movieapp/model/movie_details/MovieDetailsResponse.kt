package com.movieapp.model.movie_details

data class MovieDetailsResponse(
    val movies: List<Movy>,
    val success: Boolean,
    val message: String
)