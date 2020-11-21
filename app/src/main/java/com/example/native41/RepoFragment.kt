package com.example.native41

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
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

            it.viewPager2.adapter = CalendarPagerAdapter(this, viewModel.pageIds)

        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.login.value = args.login
        viewModel.repo.value = args.repo
    }

    class CalendarPagerAdapter(fragment: Fragment, private val items: LiveData<List<Int>>) :
        FragmentStateAdapter(fragment) {

        init {
            items.observe(fragment.viewLifecycleOwner) {
                notifyDataSetChanged()
            }
        }

        override fun getItemCount(): Int {
            return items.value?.size ?: 0
        }

        override fun createFragment(position: Int): Fragment {
            return items.value?.get(position)?.let { item ->
                CalendarFragment.newInstance(item)
            } ?: throw IllegalStateException("no item.")
        }

    }
}