package com.example.native41

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.native41.database.CalPageWithCalModel
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

            viewModel.pageItems.value?.let { list ->
                it.viewPager2.adapter = CalendarPagerAdapter(this, list).apply {
                    viewModel.pageItems.observe(viewLifecycleOwner) {
                        notifyDataSetChanged()
                    }
                }
            }

        }.root
    }

    class CalendarPagerAdapter(fragment: Fragment, val items: List<CalPageWithCalModel>) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return items.size
        }

        override fun createFragment(position: Int): Fragment {
            val item = items[position]
            return CalendarFragment.newInstance(item.calPage.id)
        }

    }
}