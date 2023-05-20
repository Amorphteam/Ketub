package com.amorphteam.ketub.ui.main.tabs.toc.tabs.first_and_second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.R
import com.amorphteam.ketub.database.book.BookDatabase
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.databinding.FragmentTocBinding
import com.amorphteam.ketub.model.NavResult
import com.amorphteam.ketub.model.TocGroupItem
import com.amorphteam.ketub.model.TreeBookHolder
import com.amorphteam.ketub.ui.adapter.IndexExpandableAdapter
import com.amorphteam.ketub.ui.main.tabs.toc.TreeViewHolder
import com.amorphteam.ketub.utility.NavTreeCreator
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView


class TocFragment(val catName:String) : Fragment() {
    private lateinit var binding: FragmentTocBinding
    private lateinit var viewModel: TocViewModel
    private var tView: AndroidTreeView? = null
    private val localNavResult: NavResult? = null
    private var books: Array<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_toc, container, false
        )

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao
        val bookRepository = BookRepository(bookDao)

        val viewModelFactory = TocViewModelFactory(catName, bookRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[TocViewModel::class.java]


        binding.viewModel = viewModel
        binding.lifecycleOwner = this




        viewModel.tocGroupItems.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                handleTocRecyclerView(it)
            }
        }


        binding.treeRoot.removeAllViews()
        viewModel.getIndexList(requireContext())

        return binding.root
    }

    private fun handleTocRecyclerView(it: List<TocGroupItem>) {
        val adapter = IndexExpandableAdapter()
        adapter.submitList(it)
        binding.expandableListView.setAdapter(adapter)

        adapter.clickListener.setOnGroupClickListener {
                //TODO:// MUST GET BOOKNAM AND NAVURO OR NAVINDEX
        }
        handleSearchView(adapter)
    }


    private fun setUpTree(navTrees: ArrayList<TreeBookHolder>) {
        val treeView: TreeNode = NavTreeCreator.getTabNavTree(navTrees)
        tView = AndroidTreeView(activity, treeView)
        tView?.setDefaultAnimation(false)
        tView?.setDefaultContainerStyle(R.style.TreeNodeStyle)
        tView?.setDefaultViewHolder(TreeViewHolder::class.java)
        binding.treeRoot.addView(tView?.view)
    }
    private fun handleSearchView(
        index: IndexExpandableAdapter
    ) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterSearch(newText, index)

                return true
            }
        })

    }

    private fun filterSearch(searchString: String, index: IndexExpandableAdapter) {
        index.filter.filter(searchString)
    }

}