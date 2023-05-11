package com.amorphteam.ketub.ui.main.tabs.library

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.database.book.BookDatabase
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.database.reference.ReferenceDatabase
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.databinding.FragmentLibraryBinding
import com.amorphteam.ketub.model.CatSection
import com.amorphteam.ketub.model.CategoryModel
import com.amorphteam.ketub.model.ReferenceModel
import com.amorphteam.ketub.ui.adapter.*
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.search.SearchActivity
import com.amorphteam.ketub.utility.FileManager
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.OnlineReference


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

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao
        val referenceDao = ReferenceDatabase.getInstance(application).referenceDatabaseDao

        val bookRepository = BookRepository(bookDao)
        val referenceRepository = ReferenceRepository(referenceDao)
        val viewModelFactory = LibraryViewModelFactory(bookRepository, referenceRepository)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[LibraryViewModel::class.java]


        binding.viewModel = viewModel
        binding.catSection1 = CatSection(
            resources.getString(R.string.ejtehad_title),
            resources.getString(R.string.ejtehad_caption),
            ResourcesCompat.getDrawable(resources, R.drawable.ejtihad_logo, null)!!
        )
        binding.catSection2 = CatSection(
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
            if (it !=null) {
                val bundle = Bundle()

                bundle.putSerializable(Keys.NAV_CAT_SECTION, it)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_library_to_detailFragment, bundle)
            }
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

        viewModel.bookItems.observe(viewLifecycleOwner){
            if (it.size == 1) {
                val fileManager = FileManager(requireActivity())

                val bookAddress = fileManager.getBookAddress(it[0].bookPath!!)

                Log.i("ssssss", bookAddress.toString())

                viewModel.openEpubAct()
            }else{
                //TODO THIS SECTION MUST BE COMPLETED
            }
        }


        viewModel.errorTocRecieve.observe(viewLifecycleOwner) {
            Log.i(Keys.LOG_NAME, it)
        }

        viewModel.recommendedToc.observe(viewLifecycleOwner) {
            handleRecyclerView(it, OnlineReference.RECOMMENDED_ONLINE)
        }

        viewModel.readMoreToc.observe(viewLifecycleOwner) {
            handleRecyclerView(it, OnlineReference.READMORE_ONLINE)
        }


        return binding.root
    }

    private fun handleRecyclerView(list: List<ReferenceModel>, onlineReference: OnlineReference) {
        val adapter = ReferenceAdapter(ItemClickListener {
            viewModel.openEpubAct()
        }, DeleteClickListener {
        })
        adapter.submitList(list)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        if (onlineReference == OnlineReference.RECOMMENDED_ONLINE) {
            binding.tocRecommanded.recyclerView.layoutManager = layoutManager
            binding.tocRecommanded.recyclerView.adapter = adapter
        }else {
            binding.tocReadMore.recyclerView.layoutManager = layoutManager
            binding.tocReadMore.recyclerView.adapter = adapter
        }
    }


    private fun handleCatBooks(bookArrayList: List<CategoryModel>, bookCat: Int) {
        val adapter = BookAdapter(BookClickListener { bookId ->
            viewModel.getCatId(bookId)
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