package com.example.native41

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.native41.databinding.RepoFragmentBinding

class RepoFragment : BaseFragment() {

    private val viewModel by viewModels<RepoViewModel>()

    private val args by navArgs<RepoFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<RepoFragmentBinding>(
            inflater,
            R.layout.repo_fragment,
            container,
            false
        ).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner

            observeBaseViewModel(viewModel)

            viewModel.login.value = args.login
            viewModel.repo.value = args.repo
        }.root
    }
}