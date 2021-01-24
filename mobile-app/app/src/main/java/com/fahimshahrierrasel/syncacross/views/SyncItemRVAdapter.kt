package com.fahimshahrierrasel.syncacross.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fahimshahrierrasel.syncacross.R
import com.fahimshahrierrasel.syncacross.databinding.ItemFileBinding
import com.fahimshahrierrasel.syncacross.databinding.ItemImageBinding
import com.fahimshahrierrasel.syncacross.databinding.ItemMessageBinding
import com.fahimshahrierrasel.syncacross.models.ItemType
import com.fahimshahrierrasel.syncacross.models.SyncItem
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions

class SyncItemRVAdapter(options: FirestorePagingOptions<SyncItem>) :
    FirestorePagingAdapter<SyncItem, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ItemType.MESSAGE.value -> {
                val binding = DataBindingUtil.inflate<ItemMessageBinding>(
                    layoutInflater,
                    R.layout.item_message,
                    parent,
                    false
                )
                return SyncMessageViewHolder(binding)
            }
            ItemType.FILE.value -> {
                val binding = DataBindingUtil.inflate<ItemFileBinding>(
                    layoutInflater,
                    R.layout.item_file,
                    parent,
                    false
                )
                return SyncFileViewHolder(binding)
            }
            else -> {
                val binding = DataBindingUtil.inflate<ItemImageBinding>(
                    layoutInflater,
                    R.layout.item_image,
                    parent,
                    false
                )
                return SyncImageViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: SyncItem) {
        val id = getItem(position)?.id!!
        when (model.type) {
            ItemType.MESSAGE -> {
                (holder as SyncMessageViewHolder).bind(id, model)
            }
            ItemType.FILE -> {
                (holder as SyncFileViewHolder).bind(id, model)
            }
            else -> {
                (holder as SyncImageViewHolder).bind(id, model)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.toObject(SyncItem::class.java)?.type?.value!!
    }
}