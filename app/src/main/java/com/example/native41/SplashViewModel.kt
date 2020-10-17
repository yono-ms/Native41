package com.example.native41

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : BaseViewModel() {

    val busy by lazy { MutableLiveData(false) }
    val initialized: MutableLiveData<Unit> by lazy { MutableLiveData(null) }

    fun initialize() {
        logger.info("initialize")
        if (busy.value == true) {
            logger.info("...busy.")
            return
        }
        busy.value = true
        viewModelScope.launch {
            delay(1000)
            initialized.value = Unit
            busy.value = false
        }
    }
}