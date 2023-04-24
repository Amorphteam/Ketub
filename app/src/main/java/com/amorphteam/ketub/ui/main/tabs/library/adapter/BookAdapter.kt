package com.amorphteam.ketub.ui.main.tabs.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemBookBinding
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel

class BookAdapter(private val clickListener: BookClickListener) :
        ListAdapter<BookModel, BookAdapter.ViewHolder>(CustomizeDiffCallback()) {

        class ViewHolder private constructor(val binding: ItemBookBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(item: BookModel, clickListener: BookClickListener) {
                binding.item = item
                binding.bookClickListener = clickListener
                //TODO: IT MUST LOAD FROM VIEWMODEL
//                binding.itemImage.setImageResource(item.bookCover)
                //
                binding.executePendingBindings()
            }

            companion object {
                fun from(parent: ViewGroup): ViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding =
                        ItemBookBinding.inflate(layoutInflater, parent, false)
                    return ViewHolder(binding)

                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder.from(parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)
            holder.bind(item, clickListener)
        }
    }

    class CustomizeDiffCallback() : DiffUtil.ItemCallback<BookModel>() {

    override fun areItemsTheSame(p0: BookModel, p1: BookModel): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: BookModel, p1: BookModel): Boolean {
        return p0 == p1
    }

    }

class BookClickListener(val clickListener: (bookId: Int) -> Unit) {
    fun onClick(viewModel: BookModel) = clickListener(viewModel.id)

}

