package com.alizaidi.trendingrepos.ui.details

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alizaidi.trendingrepos.data.GithubRepository
import com.alizaidi.trendingrepos.data.model.Repo
import kotlinx.coroutines.launch

class DetailsViewModel @ViewModelInject constructor (
    private val repository: GithubRepository
) : ViewModel() {

    fun saveRepo(repo: Repo){
        viewModelScope.launch {
            repository.insertToDb(repo)
        }
        Log.e("saved","success")
    }

}