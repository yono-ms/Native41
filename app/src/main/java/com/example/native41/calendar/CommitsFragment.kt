package com.example.native41.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.native41.BaseFragment
import com.example.native41.MainActivity
import com.example.native41.R
import com.example.native41.database.CommitEntity
import com.example.native41.databinding.CommitItemBinding
import com.example.native41.databinding.CommitsFragmentBinding

class CommitsFragment : BaseFragment() {

    private val args by navArgs<CommitsFragmentArgs>()
    private val viewModel by viewModels<CommitsViewModel> {
        CommitsViewModel.Factory(
            args.login,
            args.repo
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<CommitsFragmentBinding>(
            inflater,
            R.layout.commits_fragment,
            container,
            false
        ).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            observeBaseViewModel(viewModel)

            // calendar pager
            it.viewPager2.adapter = object : FragmentStateAdapter(this) {
                override fun getItemCount(): Int {
                    return viewModel.yearMonths.value?.size ?: 0
                }

                override fun createFragment(position: Int): Fragment {
                    return viewModel.yearMonths.value?.get(position)?.let { item ->
                        MonthFragment.newInstance(item.year, item.month)
                    } ?: throw IllegalStateException("no item.")
                }

            }.apply {
                viewModel.yearMonths.observe(viewLifecycleOwner) {
                    notifyDataSetChanged()
                }
            }

            // recycler
            it.recyclerView.layoutManager = LinearLayoutManager(context)
            it.recyclerView.adapter = CommitAdapter().apply {
                viewModel.commitEntities.observe(viewLifecycleOwner) { list ->
                    submitList(list)
                }
            }
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? MainActivity)?.supportActionBar?.title = "${args.login} ${args.repo}"
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    class CommitAdapter : ListAdapter<CommitEntity, CommitAdapter.CommitViewHolder>(object :
        DiffUtil.ItemCallback<CommitEntity>() {
        override fun areItemsTheSame(
            oldItem: CommitEntity,
            newItem: CommitEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CommitEntity,
            newItem: CommitEntity
        ): Boolean {
            return oldItem == newItem
        }
    }) {
        class CommitViewHolder(val binding: CommitItemBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder {
            return DataBindingUtil.inflate<CommitItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.commit_item,
                parent,
                false
            ).let {
                CommitViewHolder(it)
            }
        }

        override fun onBindViewHolder(holder: CommitViewHolder, position: Int) {
            holder.binding.viewModel = getItem(position)
        }
    }
}