package com.amorphteam.ketub.ui.main.tabs.library

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentLibraryBinding
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.main.tabs.library.adapter.BookAdapter
import com.amorphteam.ketub.ui.main.tabs.library.adapter.BookClickListener
import com.amorphteam.ketub.ui.main.tabs.library.adapter.MainTocAdapter
import com.amorphteam.ketub.ui.main.tabs.library.adapter.MainTocClickListener
import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabase
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc
import com.amorphteam.ketub.ui.main.tabs.library.model.TitleAndDes
import com.amorphteam.ketub.ui.search.SearchActivity
import com.amorphteam.ketub.utility.Keys


class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var viewModel: LibraryViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_library, container, false
        )

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = BookDatabase.getInstance(application).bookDatabaseDao
        val viewModelFactory = LibraryFragmentViewModelFactory(dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        viewModel =
            ViewModelProvider(this, viewModelFactory)[LibraryViewModel::class.java]

        binding.viewModel = viewModel
        binding.titleAndDes1 = TitleAndDes(
            resources.getString(R.string.ejtehad_title),
            resources.getString(R.string.ejtehad_caption),
            ResourcesCompat.getDrawable(resources, R.drawable.ejtihad_logo, null)!!
        )
        binding.titleAndDes2 = TitleAndDes(
            resources.getString(R.string.nosos_title),
            resources.getString(R.string.nosos_caption),
            ResourcesCompat.getDrawable(resources, R.drawable.nosos_logo, null)!!
        )

        binding.lifecycleOwner = this


        viewModel.startEpubAct.observe(viewLifecycleOwner) {
            if (it) startActivity(Intent(activity, EpubActivity::class.java))
        }

        viewModel.startSearchAct.observe(viewLifecycleOwner) {
            if (it) startActivity(Intent(activity, SearchActivity::class.java))
        }

        viewModel.startDetailFrag.observe(viewLifecycleOwner) {
            val bundle = Bundle()
            bundle.putSerializable("titleAndDes", it)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_library_to_detailFragment, bundle)
        }

        viewModel.readMoreToc.observe(viewLifecycleOwner) {
            handleReadMore(it)
            Log.i(Keys.LOG_NAME, "handleAllBooks")

        }


        viewModel.firstCatBooksNewItems.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                handleCatBooks(it, 1)
            }
        }

        viewModel.secondCatBooksNewItems.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                handleCatBooks(it, 2)
            }
        }



        viewModel.errorTocRecieve.observe(viewLifecycleOwner) {
            Log.i(Keys.LOG_NAME, it)
        }

        viewModel.recommendedToc.observe(viewLifecycleOwner) {
            handleRecommanded(it)
        }

        viewModel.errorTocRecieve.observe(viewLifecycleOwner) {
            Log.i(Keys.LOG_NAME, it)
        }

        return binding.root
    }

    private fun handleRecommanded(list: List<MainToc>) {
        val recommandedToc = MainTocAdapter(MainTocClickListener {
            viewModel.openEpubAct()
        })
        recommandedToc.submitList(list)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.tocRecommanded.recyclerView.layoutManager = layoutManager
        binding.tocRecommanded.recyclerView.adapter = recommandedToc
    }

    private fun handleReadMore(list: List<MainToc>) {
        val readMoreToc = MainTocAdapter(MainTocClickListener {
            viewModel.openEpubAct()
        })
        readMoreToc.submitList(list)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.tocReadMore.recyclerView.layoutManager = layoutManager
        binding.tocReadMore.recyclerView.adapter = readMoreToc
    }


    private fun handleCatBooks(bookArrayList: List<BookModel>, bookCat: Int) {
        val adapter = BookAdapter(BookClickListener { bookId ->
            viewModel.openEpubAct()
        })
        adapter.submitList(bookArrayList)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        when (bookCat) {
            1 -> {
                binding.firstCatBooksItems.recyclerView.layoutManager = layoutManager
                binding.firstCatBooksItems.recyclerView.adapter = adapter
            }

            2 -> {
                binding.secondCatBooksItems.recyclerView.layoutManager = layoutManager
                binding.secondCatBooksItems.recyclerView.adapter = adapter
            }
        }


    }

}