package com.example.native41.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.native41.App
import com.example.native41.BaseViewModel
import com.example.native41.database.CommitEntity
import com.example.native41.network.CommitModel
import com.example.native41.network.ServerAPI
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer

class CommitsViewModel(private val login: String, private val repo: String) : BaseViewModel() {

    class Factory(private val login: String, private val repo: String) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CommitsViewModel(login, repo) as T
        }
    }

    val yearMonths by lazy { MutableLiveData<List<YearMonth>>() }

    val commitEntities by lazy { App.db.commitEntityDao().getAllLiveData() }

    fun refresh() {
        logger.info("$login $repo")
        viewModelScope.launch {
            progress.value = true
            kotlin.runCatching {
                App.db.commitEntityDao().deleteAll()
                val list = ServerAPI.getDecode(
                    ServerAPI.getCommitsUrl(login, repo),
                    ListSerializer(CommitModel.serializer())
                )
                logger.debug("$list")
                val entities = mutableListOf<CommitEntity>()
                list.forEach { commitModel ->
                    val commitEntity = CommitEntity.fromCommit(commitModel)
                    logger.debug("$commitEntity")
                    entities.add(commitEntity)
                }
                App.db.commitEntityDao().insertAll(*entities.toTypedArray())
            }.onSuccess {
                logger.info("success.")
                yearMonths.value =
                    listOf(YearMonth(2020, 9), YearMonth(2020, 10), YearMonth(2020, 11))
            }.onFailure {
                logger.error("refresh", it)
                throwable.value = it
            }.also {
                progress.value = false
            }
        }
    }
}