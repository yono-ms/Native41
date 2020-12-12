package com.example.native41

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.RotateDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
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
    ): View {
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

            it.editTextLogin.setOnKeyListener { view, keyCode, event ->
                if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    viewModel.onSearch()
                    activity?.apply {
                        // hide keyboard
                        (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
                            hideSoftInputFromWindow(
                                view.windowToken,
                                InputMethodManager.HIDE_NOT_ALWAYS
                            )
                        }
                        // move focus
                        val parent = view.parent as View
                        parent.isFocusable = true
                        parent.isFocusableInTouchMode = true
                        parent.requestFocus()
                    }
                    true
                } else {
                    false
                }
            }

            it.recyclerView.layoutManager = LinearLayoutManager(context)
            it.recyclerView.adapter = RepoAdapter { repoModel ->
                logger.info("onClick $repoModel")
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToCommitsFragment(
                        repoModel.owner.login,
                        repoModel.name
                    )
                )
            }.also { adapter ->
                viewModel.items.observe(viewLifecycleOwner) { items ->
                    logger.info("items changed.")
                    adapter.submitList(items)
                }
            }

            viewModel.progress.observe(viewLifecycleOwner) { isProgress ->
                refreshIcon?.let { rotateDrawable ->
                    if (isProgress) {
                        objectAnimator =
                            ObjectAnimator.ofInt(rotateDrawable, "level", 0, 10000).apply {
                                duration = 1000
                                repeatCount = 100
                                start()
                            }
                    } else {
                        objectAnimator?.cancel()
                        ObjectAnimator.ofInt(rotateDrawable, "level", 10000).apply {
                            duration = 1000
                            start()
                        }
                    }
                }
            }
        }.root
    }

    private var objectAnimator: ObjectAnimator? = null
    private var refreshIcon: RotateDrawable? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        logger.info("onCreateOptionsMenu")
        inflater.inflate(R.menu.home, menu)
        menu.findItem(R.id.refresh).apply {
            kotlin.runCatching {
                icon as? RotateDrawable
            }.onSuccess {
                refreshIcon = it
            }.onFailure {
                logger.error("icon is not RotateDrawable", it)
            }
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

    class RepoAdapter(private val onClick: (RepoModel) -> Unit) :
        ListAdapter<RepoModel, RepoAdapter.ViewHolder>(object :
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
            return ViewHolder(binding).also {
                it.itemView.setOnClickListener {
                    binding.viewModel?.let(onClick)
                }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.viewModel = getItem(position)
        }
    }
}