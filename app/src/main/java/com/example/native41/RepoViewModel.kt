package com.example.native41

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.native41.network.CommitModel
import com.example.native41.network.ServerAPI
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer

class RepoViewModel : BaseViewModel() {
    val login by lazy { MutableLiveData<String>() }
    val repo by lazy { MutableLiveData<String>() }
    val pageIds by lazy { App.db.calModelDao().getPageIdsLiveData() }

    fun getCommits() {
        viewModelScope.launch {
            progress.value = true
            kotlin.runCatching {
                ServerAPI.getDecode(
                    ServerAPI.getCommitsUrl(login.value, repo.value),
                    ListSerializer(CommitModel.serializer())
                )
            }.onSuccess {
                logger.debug("$it")
            }.onFailure {
                logger.error("", it)
                throwable.value = it
            }.also {
                progress.value = false
            }
        }
    }
}