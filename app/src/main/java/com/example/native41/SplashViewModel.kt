package com.example.native41

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            withContext(Dispatchers.IO) {
                App.db.clearAllTables()
                initialized.postValue(Unit)
                busy.postValue(false)
            }
        }
    }
}