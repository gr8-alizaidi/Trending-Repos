package com.alizaidi.trendingrepos.di

import android.content.Context
import androidx.room.Room
import com.alizaidi.trendingrepos.api.GithubApi
import com.alizaidi.trendingrepos.db.RepoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(GithubApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideTrendingApi(retrofit: Retrofit): GithubApi =
        retrofit.create(GithubApi::class.java)


    @Singleton
    @Provides
    fun provideRepoDatabase(
        @ApplicationContext app:Context
    ) = Room.databaseBuilder(
        app,
        RepoDatabase::class.java,
        "repo_db"
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideRepoDao(db: RepoDatabase) = db.getRepoDao()


}