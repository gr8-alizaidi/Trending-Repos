package com.alizaidi.trendingrepos.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alizaidi.trendingrepos.data.model.Owner
import com.alizaidi.trendingrepos.data.model.Repo
import com.alizaidi.trendingrepos.db.Converters
import com.alizaidi.trendingrepos.db.dao.RepoDao


@Database(entities = [Repo::class, Owner::class], version = 2)
@TypeConverters(Converters::class)
abstract class RepoDatabase : RoomDatabase() {

    abstract fun getRepoDao(): RepoDao

}