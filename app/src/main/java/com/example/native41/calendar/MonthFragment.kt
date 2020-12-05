package com.example.native41.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.example.native41.BaseFragment
import com.example.native41.R
import com.example.native41.databinding.MonthFragmentBinding
import com.example.native41.databinding.MonthItemBinding

class MonthFragment : BaseFragment() {

    companion object {
        enum class Key {
            YEAR,
            MONTH,
        }

        fun newInstance(year: Int, month: Int): MonthFragment {
            return MonthFragment().apply {
                arguments = bundleOf(
                    Key.YEAR.name to year,
                    Key.MONTH.name to month,
                )
            }
        }
    }

    private val viewModel by viewModels<MonthViewModel> {
        MonthViewModel.Factory(
            arguments?.getInt(Key.YEAR.name) ?: 2000,
            arguments?.getInt(Key.MONTH.name) ?: 1
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<MonthFragmentBinding>(
            inflater,
            R.layout.month_fragment,
            container,
            false
        ).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner

            it.recyclerView.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
            )
            it.recyclerView.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
            it.recyclerView.layoutManager = GridLayoutManager(context, 7)
            it.recyclerView.adapter = MonthAdapter().also { adapter ->
                viewModel.items.observe(viewLifecycleOwner) { items ->
                    adapter.submitList(items)
                }
            }
        }.root
    }

    class MonthAdapter : ListAdapter<MonthItem, MonthAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<MonthItem>() {
        override fun areItemsTheSame(oldItem: MonthItem, newItem: MonthItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MonthItem, newItem: MonthItem): Boolean {
            return oldItem == newItem
        }
    }) {
        class ViewHolder(val binding: MonthItemBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.month_item,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.viewModel = getItem(position)
        }

    }

}