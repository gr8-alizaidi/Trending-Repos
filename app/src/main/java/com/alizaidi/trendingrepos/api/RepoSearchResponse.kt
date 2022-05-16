package com.alizaidi.trendingrepos.api

import com.alizaidi.trendingrepos.data.model.Repo
import com.google.gson.annotations.SerializedName

data class RepoSearchResponse(

    @SerializedName("total_count")
    val total: Int = 0,

    @SerializedName("items")
    val items: List<Repo> = emptyList(),

    val nextPage: Int? = null
)