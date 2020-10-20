package com.example.native41

import androidx.lifecycle.viewModelScope
import com.example.native41.network.ServerAPI
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {
    fun initialize() {
        logger.info("initialize")
        viewModelScope.launch {
            kotlin.runCatching {
                ServerAPI.getUsers("google")
            }.onSuccess {
                logger.debug("$it")
            }.onFailure {
                logger.error("initialize", it)
            }
        }
    }
}