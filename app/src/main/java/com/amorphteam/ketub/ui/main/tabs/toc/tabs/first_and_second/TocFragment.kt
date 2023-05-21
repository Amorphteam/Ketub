package com.amorphteam.ketub.ui.main.tabs.toc.tabs.first_and_second

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.database.book.BookDatabase
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.databinding.FragmentTocBinding
import com.amorphteam.ketub.model.IndexesInfo
import com.amorphteam.ketub.model.NavResult
import com.amorphteam.ketub.model.TreeBookHolder
import com.amorphteam.ketub.ui.adapter.EmptyTocListener
import com.amorphteam.ketub.ui.adapter.TocListAdapter
import com.amorphteam.ketub.ui.adapter.TocListItemClickListener
import com.amorphteam.ketub.ui.main.tabs.toc.TreeViewHolder
import com.amorphteam.ketub.utility.EpubHelper
import com.amorphteam.ketub.utility.FileManager
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.NavTreeCreator
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView


class TocFragment(val catName:String) : Fragment(), EmptyTocListener {
    private lateinit var binding: FragmentTocBinding
    private lateinit var viewModel: TocViewModel
    private var tView: AndroidTreeView? = null
    private val localNavResult: NavResult? = null
    var adapter: TocListAdapter? = null
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




        viewModel.treeTocNavResult.observe(viewLifecycleOwner) {
            if (it != null) {
                setUpTree(it.navTrees)
                setupListForSearch(it.navPoints)
                handleSearchView()
            }
        }

        viewModel.catBooksNewItems.observe(viewLifecycleOwner){
            if (it !=null){
                viewModel.getIndex(requireContext())
            }
        }


        return binding.root
    }

    private fun setupListForSearch(navPoints: ArrayList<IndexesInfo>) {
        adapter = TocListAdapter(this, TocListItemClickListener {
            val fileManager = FileManager(requireContext())
            val booksAddress: String? = fileManager.getBookAddress(it.bookPath)
            EpubHelper.openEpub(booksAddress.toString(),
                EpubHelper.getContentWithoutTag(it.navPoint.content), requireContext())
        })

        adapter?.submitList(navPoints)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.treeRoot.removeAllViews()
        viewModel.getCat()
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
    ) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterSearch(newText, adapter!!)

                return true
            }
        })

    }

    private fun filterSearch(searchString: String, adapter: TocListAdapter) {
        showTreeToc(false)
        adapter.filter.filter(searchString)
    }

    private fun showTreeToc(status:Boolean) {
        if (status) {
            binding.recyclerView.visibility = View.GONE
            binding.treeRoot.visibility = View.VISIBLE
        }else{
            binding.recyclerView.visibility = View.VISIBLE
            binding.treeRoot.visibility = View.GONE
        }
    }

    override fun onEmptyListReceived() {
        showTreeToc(true)

    }

}