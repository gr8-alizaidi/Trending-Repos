package com.alizaidi.trendingrepos.data

import android.util.Log
import androidx.paging.PagingSource
import com.alizaidi.trendingrepos.api.GithubApi
import com.alizaidi.trendingrepos.data.model.Repo
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class GithubPagingSource(
    private val githubApi: GithubApi,
    private val query: String
): PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val position = params.key ?: STARTING_PAGE_INDEX
        Log.e("Loading","api")

        return try {
            val response = githubApi.getTrendingRepos(query, position, params.loadSize)
            val repos = response.items
            Log.e("res",response.toString())

            LoadResult.Page(
                data = repos,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (repos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            Log.e("exc",exception.toString())
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

}