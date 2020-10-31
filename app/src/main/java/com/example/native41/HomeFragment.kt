package com.example.native41

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.native41.databinding.HomeFragmentBinding
import com.example.native41.databinding.RepoItemBinding
import com.example.native41.network.RepoModel

class HomeFragment : BaseFragment() {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (savedInstanceState == null) {
            findNavController().navigate(R.id.action_homeFragment_to_splashFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.info("onCreateView")
        return DataBindingUtil.inflate<HomeFragmentBinding>(
            inflater,
            R.layout.home_fragment,
            container,
            false
        ).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner

            observeBaseViewModel(viewModel)

            it.button.setOnClickListener {
                viewModel.onClick()
            }

            it.recyclerView.layoutManager = LinearLayoutManager(context)
            it.recyclerView.adapter = RepoAdapter().also { adapter ->
                viewModel.items.observe(viewLifecycleOwner) { items ->
                    logger.info("items changed.")
                    adapter.submitList(items)
                }
            }

            viewModel.progress.observe(viewLifecycleOwner) {
                requireActivity().invalidateOptionsMenu()
            }
        }.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        logger.info("onCreateOptionsMenu")
        inflater.inflate(R.menu.home, menu)
        menu.findItem(R.id.refresh).apply {
            isVisible = viewModel.progress.value == true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        logger.info("onOptionsItemSelected")
        return when (item.itemId) {
            R.id.refresh -> {
                logger.info("refresh menu.")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initialize()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveInstanceState()
    }

    class RepoAdapter : ListAdapter<RepoModel, RepoAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<RepoModel>() {
        override fun areItemsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RepoModel, newItem: RepoModel): Boolean {
            return oldItem == newItem
        }
    }) {
        class ViewHolder(val binding: RepoItemBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = DataBindingUtil.inflate<RepoItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.repo_item,
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.viewModel = getItem(position)
        }
    }
}