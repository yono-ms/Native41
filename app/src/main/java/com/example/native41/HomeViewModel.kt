package com.example.native41

import androidx.lifecycle.viewModelScope
import com.example.native41.network.RepoModel
import com.example.native41.network.ServerAPI
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer

class HomeViewModel : BaseViewModel() {
    fun initialize() {
        logger.info("initialize")
        viewModelScope.launch {
            kotlin.runCatching {
                ServerAPI.getUsers("google")
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
                }.onFailure {
                    logger.error("initialize repos", it)
                }
            }.onFailure {
                logger.error("initialize users", it)
            }
        }
    }
}