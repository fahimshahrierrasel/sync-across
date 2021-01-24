package com.fahimshahrierrasel.syncacross.views

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Paint
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.fahimshahrierrasel.syncacross.config.FirebaseConfig
import com.fahimshahrierrasel.syncacross.databinding.ItemFileBinding
import com.fahimshahrierrasel.syncacross.databinding.ItemImageBinding
import com.fahimshahrierrasel.syncacross.databinding.ItemMessageBinding
import com.fahimshahrierrasel.syncacross.models.ItemType
import com.fahimshahrierrasel.syncacross.models.SyncItem
import com.fahimshahrierrasel.syncacross.models.URLRegex
import com.fahimshahrierrasel.syncacross.utils.getFileNameFromURL
import com.fahimshahrierrasel.syncacross.utils.openInBrowser
import kotlinx.coroutines.launch


class SyncMessageViewHolder(binding: ItemMessageBinding) :
    MultiViewHolder<ItemMessageBinding>(binding) {
    private val itemBinding: ItemMessageBinding = binding
    override fun bind(id: String, item: SyncItem) {
        itemBinding.syncItem = item

        if (URLRegex.matchEntire(item.value) != null) {
            itemBinding.tvMessage.paintFlags =
                itemBinding.tvMessage.paintFlags or Paint.UNDERLINE_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
            itemBinding.tvMessage.setOnClickListener {
                openInBrowser(item.value)
            }
        }

        itemBinding.btnCopy.setOnClickListener {
            val clipboard: ClipboardManager? = getSystemService(
                it.context,
                ClipboardManager::class.java
            )
            val clip = ClipData.newPlainText("message", item.value)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(it.context, "Message copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        itemBinding.btnDelete.setOnClickListener {
            MainActivity.Instance.networkScope.launch {
                deleteItem(id, item)
                MainActivity.Instance.adapter.refresh()
            }
        }
    }
}

class SyncFileViewHolder(binding: ItemFileBinding) : MultiViewHolder<ItemFileBinding>(binding) {
    private val itemBinding: ItemFileBinding = binding
    override fun bind(id: String, item: SyncItem) {
        itemBinding.syncItem = item

        if (URLRegex.matchEntire(item.value) != null) {
            itemBinding.tvFileName.paintFlags =
                itemBinding.tvFileName.paintFlags or Paint.UNDERLINE_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
            itemBinding.tvFileName.setOnClickListener {
                openInBrowser(item.value)
            }
        }

        itemBinding.btnDownload.setOnClickListener {
            openInBrowser(item.value)
        }

        itemBinding.btnDelete.setOnClickListener {
            MainActivity.Instance.networkScope.launch {
                deleteItem(id, item)
                MainActivity.Instance.adapter.refresh()
            }
        }
    }
}

//https://firebasestorage.googleapis.com/v0/b/syncacross-a008d.appspot.com/o/sync_files%2FIMG_20210114_010706.jpg?alt=media&token=422e21a8-07b9-48de-9e3f-09ab3bbf3324

class SyncImageViewHolder(binding: ItemImageBinding) : MultiViewHolder<ItemImageBinding>(binding) {
    private val itemBinding: ItemImageBinding = binding
    override fun bind(id: String, item: SyncItem) {
        itemBinding.syncItem = item

        itemBinding.ivImage.setOnClickListener {
            openInBrowser(item.value)
        }

        itemBinding.btnDownload.setOnClickListener {
            openInBrowser(item.value)
        }

        itemBinding.btnDelete.setOnClickListener {
            MainActivity.Instance.networkScope.launch {
                deleteItem(id, item)
                MainActivity.Instance.adapter.refresh()
            }
        }
    }
}

abstract class MultiViewHolder<T : ViewBinding>(binding: T) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(id: String, item: SyncItem)

    suspend fun deleteItem(id: String, item: SyncItem) {
        if (item.type == ItemType.FILE || item.type == ItemType.IMAGE) {
            val isFileDeleted = FirebaseConfig.deleteFile(item.value.getFileNameFromURL())
            if (!isFileDeleted) {
                MainActivity.Instance.showSnackBar("File can not be deleted!!")
                return
            }
        }
        val isMessageDeleted = FirebaseConfig.deleteMessage(id)
        if (!isMessageDeleted) {
            MainActivity.Instance.showSnackBar("Message can not be deleted!!")
            return
        }
    }
}