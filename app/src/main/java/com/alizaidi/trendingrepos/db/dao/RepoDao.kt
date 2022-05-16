package com.alizaidi.trendingrepos.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alizaidi.trendingrepos.data.model.Repo


@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRepo(rp: Repo)

    @Query("SELECT * FROM repo_table")
    fun getRepo(): List<Repo>

    @Query("DELETE FROM repo_table")
    suspend fun deleteRepo()
}