package com.admin.portfolio.libs

data class MoviesListResponse(
    val message: String,
    val movie_list: List<Movie>,
    val status: String
)