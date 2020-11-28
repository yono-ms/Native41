package com.example.native41

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.native41.database.CommitDateModel
import com.example.native41.network.CommitModel
import com.example.native41.network.ServerAPI
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer

class RepoViewModel : BaseViewModel() {
    val login by lazy { MutableLiveData<String>() }
    val repo by lazy { MutableLiveData<String>() }
    val pageIds by lazy { App.db.calModelDao().getPageIdsLiveData() }
    val commitDateList by lazy { App.db.commitDateModelDao().getAllLiveData() }

    fun getCommits() {
        viewModelScope.launch {
            progress.value = true
            kotlin.runCatching {
                ServerAPI.getDecode(
                    ServerAPI.getCommitsUrl(login.value, repo.value),
                    ListSerializer(CommitModel.serializer())
                )
            }.onSuccess { list ->
                logger.debug("$list")
                kotlin.runCatching {
                    val entities = mutableListOf<CommitDateModel>()
                    list.forEach { commitModel ->
                        val commitDateModel = CommitDateModel.fromCommit(commitModel)
                        logger.debug("$commitDateModel")
                        entities.add(commitDateModel)
                    }
                    App.db.commitDateModelDao().insertAll(*entities.toTypedArray())
                }.onSuccess {
                    logger.info("success.")
                }.onFailure {
                    logger.error("insertAll", it)
                    throwable.value = it
                }
            }.onFailure {
                logger.error("getCommitsUrl", it)
                throwable.value = it
            }.also {
                progress.value = false
            }
        }
    }
}