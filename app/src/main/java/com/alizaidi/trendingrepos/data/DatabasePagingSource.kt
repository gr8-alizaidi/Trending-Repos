package com.alizaidi.trendingrepos.data

import android.util.Log
import androidx.paging.PagingSource
import com.alizaidi.trendingrepos.data.model.Repo
import com.alizaidi.trendingrepos.db.dao.RepoDao
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class DatabasePagingSource(
    private val repo: RepoDao
): PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val position = params.key ?: STARTING_PAGE_INDEX
        Log.e("Loading","database $position")
        return try {
            val response = repo.getRepo()
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = null
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        }
    }

}