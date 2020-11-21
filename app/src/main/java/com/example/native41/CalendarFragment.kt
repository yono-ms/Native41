package com.example.native41

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.example.native41.database.CalModel
import com.example.native41.databinding.CalendarFragmentBinding
import com.example.native41.databinding.CalendarItemBinding
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CalendarFragment : Fragment() {

    val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

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

    private val pageId by lazy { arguments?.getInt(KEY_PAGE_ID) ?: 0 }

    private val viewModel by viewModels<CalendarViewModel> { CalendarViewModel.Factory(pageId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.info("onCreateView")
        return DataBindingUtil.inflate<CalendarFragmentBinding>(
            inflater,
            R.layout.calendar_fragment,
            container,
            false
        ).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner

            it.recyclerViewCalendar.also { recyclerView ->
                val layoutManager = GridLayoutManager(context, 7)
                recyclerView.addItemDecoration(
                    DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
                )
                recyclerView.addItemDecoration(
                    DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                )
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = CalendarAdapter().also { calendarAdapter ->
                    viewModel.items.observe(viewLifecycleOwner) { calModels ->
                        logger.info("calModels changed.")
                        calendarAdapter.submitList(calModels)
                    }
                }
            }
        }.root
    }

    class CalendarAdapter : ListAdapter<CalModel, CalendarAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<CalModel>() {
        override fun areItemsTheSame(oldItem: CalModel, newItem: CalModel): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: CalModel, newItem: CalModel): Boolean {
            return oldItem == newItem
        }
    }) {
        class ViewHolder(val binding: CalendarItemBinding) : RecyclerView.ViewHolder(binding.root)

        val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            logger.trace("onCreateViewHolder")
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
            logger.trace("onBindViewHolder")
            val item = getItem(position)
            holder.binding.viewModel = item
        }
    }
}