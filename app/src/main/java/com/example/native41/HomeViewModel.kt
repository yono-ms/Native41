package com.example.native41

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.native41.network.RepoModel
import com.example.native41.network.ServerAPI
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer

class HomeViewModel : BaseViewModel() {

    var login = MutableLiveData<String?>(App.prefs.login)

    val items by lazy { MutableLiveData<List<RepoModel>>() }

    fun saveInstanceState() {
        App.prefs.login = login.value
    }

    fun initialize() {
        logger.info("initialize")
        if (login.value.isNullOrEmpty()) {
            return
        }
        login.value?.let {
            request(it)
        }
    }

    fun onSearch() {
        logger.info("onSearch")
        if (login.value.isNullOrEmpty()) {
            throwable.value = LoginNameException()
            return
        }
        login.value?.let {
            request(it)
        }
    }

    private fun request(login: String) {
        viewModelScope.launch {
            progress.value = true
            kotlin.runCatching {
                ServerAPI.getUsers(login)
            }.onSuccess { userModel ->
                logger.debug("$userModel")
                kotlin.runCatching {
                    ServerAPI.getDecode(
                        userModel.reposUrl,
                        ListSerializer(RepoModel.serializer())
                    )
                }.onSuccess { repos ->
                    logger.debug("$repos")
                    repos.forEach {
                        logger.debug("name=${it.name}")
                        logger.debug("updatedAt=${it.updatedAt}")
                    }
                    items.value = repos
                    App.prefs.login = login
                }.onFailure {
                    logger.error("initialize repos", it)
                    throwable.value = it
                    items.value = listOf()
                }
            }.onFailure {
                logger.error("initialize users", it)
                throwable.value = it
                items.value = listOf()
            }.also {
                progress.value = false
            }
        }
    }
}