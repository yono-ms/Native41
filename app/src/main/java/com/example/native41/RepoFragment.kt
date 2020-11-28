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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.native41.database.CommitDateModel
import com.example.native41.databinding.CommitItemBinding
import com.example.native41.databinding.RepoFragmentBinding

class RepoFragment : BaseFragment() {

    private val viewModel by viewModels<RepoViewModel>()

    private val args by navArgs<RepoFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            viewModel.pageIds.observe(viewLifecycleOwner) { pageIds ->
                it.viewPager2.currentItem = pageIds.size - 1
            }

            it.recyclerViewCommit.layoutManager = LinearLayoutManager(context)
            it.recyclerViewCommit.adapter = CommitAdapter().also { commitAdapter ->
                viewModel.commitDateList.observe(viewLifecycleOwner) { items ->
                    commitAdapter.submitList(items)
                }
            }

        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.login.value = args.login
        viewModel.repo.value = args.repo
    }

    override fun onResume() {
        super.onResume()

        viewModel.getCommits()
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

    class CommitAdapter : ListAdapter<CommitDateModel, CommitAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<CommitDateModel>() {
        override fun areItemsTheSame(oldItem: CommitDateModel, newItem: CommitDateModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CommitDateModel,
            newItem: CommitDateModel
        ): Boolean {
            return oldItem == newItem
        }
    }) {
        class ViewHolder(val binding: CommitItemBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.commit_item,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)
            holder.binding.viewModel = item
        }
    }
}