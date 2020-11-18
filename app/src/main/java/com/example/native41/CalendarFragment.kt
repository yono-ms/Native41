package com.example.native41

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.native41.database.CalModel
import com.example.native41.database.CalPageWithCalModel
import com.example.native41.databinding.CalendarFragmentBinding
import com.example.native41.databinding.CalendarItemBinding

class CalendarFragment : Fragment() {

    companion object {
        private const val KEY_PAGE_ID = "PAGE_ID"
        fun newInstance(pageId: Int): CalendarFragment {
            val args = Bundle().apply {
                putInt(KEY_PAGE_ID, pageId)
            }
            val fragment = CalendarFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel by viewModels<CalendarViewModel>()

    lateinit var items: LiveData<List<CalModel>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<CalendarFragmentBinding>(
            inflater,
            R.layout.calendar_fragment,
            container,
            false
        ).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner

            it.recyclerViewCalendar.layoutManager = GridLayoutManager(context, 7)
            it.recyclerViewCalendar.adapter = CalendarAdapter().also { calendarAdapter ->
                items.observe(viewLifecycleOwner){calModels->
                    calendarAdapter.submitList(calModels)
                }
            }
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt(KEY_PAGE_ID)?.let {
            items = App.db.calModelDao().getAll(it)
        }
    }

    class CalendarAdapter: ListAdapter<CalModel, CalendarAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<CalModel>(){
        override fun areItemsTheSame(oldItem: CalModel, newItem: CalModel): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: CalModel, newItem: CalModel): Boolean {
            return oldItem == newItem
        }
    }) {
        class ViewHolder(val binding: CalendarItemBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return DataBindingUtil.inflate<CalendarItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.calendar_item,
                parent,
                false
            ).let {
                ViewHolder(it)
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)
            holder.binding.viewModel = item
        }
    }
}