package com.example.native41

import androidx.lifecycle.ViewModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BaseViewModel : ViewModel() {
    val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }
}