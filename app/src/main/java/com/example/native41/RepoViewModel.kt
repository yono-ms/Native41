package com.example.native41

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RepoViewModel : BaseViewModel() {
    val login by lazy { MutableLiveData<String>() }
    val repo by lazy { MutableLiveData<String>() }
}