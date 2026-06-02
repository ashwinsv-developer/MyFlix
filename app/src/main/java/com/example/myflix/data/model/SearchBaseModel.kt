package com.example.myflix.data.model

import com.google.gson.annotations.SerializedName

data class SearchBaseModel(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<SearchItem>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)