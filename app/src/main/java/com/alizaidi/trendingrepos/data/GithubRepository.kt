package com.alizaidi.trendingrepos.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.alizaidi.trendingrepos.api.GithubApi
import com.alizaidi.trendingrepos.data.DatabasePagingSource
import com.alizaidi.trendingrepos.data.GithubPagingSource
import com.alizaidi.trendingrepos.data.model.Repo
import com.alizaidi.trendingrepos.db.dao.RepoDao

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(private val githubApi: GithubApi, private val repoDao: RepoDao) {

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GithubPagingSource(githubApi, query) }
        ).liveData

    suspend fun insertToDb(repo: Repo)=repoDao.insertRepo(repo)

    fun dataFromDb()=Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { DatabasePagingSource(repoDao) }
    ).liveData

    suspend fun deleteDbData(){
        repoDao.deleteRepo()
    }

}