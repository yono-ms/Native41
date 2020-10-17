package com.example.native41

import android.os.Bundle
import androidx.fragment.app.Fragment
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BaseFragment : Fragment() {
    val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.info("onCreate savedInstanceState=$savedInstanceState")
    }

    override fun onStart() {
        super.onStart()
        logger.info("onStart")
    }

    override fun onResume() {
        super.onResume()
        logger.info("onResume")
    }

    override fun onPause() {
        super.onPause()
        logger.info("onPause")
    }

    override fun onStop() {
        super.onStop()
        logger.info("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.info("onDestroy")
    }
}