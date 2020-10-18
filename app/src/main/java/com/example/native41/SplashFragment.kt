package com.example.native41

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.native41.databinding.SplashFragmentBinding

class SplashFragment : BaseFragment() {


    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.info("onCreateView")
        return DataBindingUtil.inflate<SplashFragmentBinding>(
            inflater,
            R.layout.splash_fragment,
            container,
            false
        ).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner

            viewModel.initialized.observe(viewLifecycleOwner) { initialized ->
                if (initialized != null) {
                    findNavController().popBackStack()
                    viewModel.initialized.value = null
                }
            }
        }.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.initialize()
    }
}