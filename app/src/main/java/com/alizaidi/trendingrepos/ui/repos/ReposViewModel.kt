package com.alizaidi.trendingrepos.ui.repos

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alizaidi.trendingrepos.data.GithubRepository
import com.alizaidi.trendingrepos.data.model.Repo
import kotlinx.coroutines.launch

class ReposViewModel @ViewModelInject constructor (
    private val repository: GithubRepository
) : ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)
     var dbResult=MutableLiveData<PagingData<Repo>>()
    val repos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchRepos(query: String) {
        currentQuery.value = query
    }

    fun dataDb(){
        dbResult= repository.dataFromDb() as MutableLiveData<PagingData<Repo>>
    }

    fun deleteDb(){
        viewModelScope.launch {  repository.deleteDbData()
        }
    }
    companion object {
        private const val DEFAULT_QUERY = "google"
    }

}