package edu.vt.cs3714.spring2023.Challenge04

data class Movies(
    val results: List<MovieItem>,
    val total_pages: Int,
    val page: Int
)