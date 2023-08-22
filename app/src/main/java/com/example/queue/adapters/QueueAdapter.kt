package com.example.queue.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.queue.databinding.ItemQueueBinding
import com.example.queue.models.QueueModel

class QueueAdapter : RecyclerView.Adapter<QueueAdapter.QueueViewHolder>(){

    inner class QueueViewHolder(val binding: ItemQueueBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<QueueModel>() {
        override fun areItemsTheSame(oldItem: QueueModel, newItem: QueueModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: QueueModel, newItem: QueueModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueueViewHolder {
        val binding = ItemQueueBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QueueViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((QueueModel) -> Unit)? = null

    override fun onBindViewHolder(holder: QueueViewHolder, position: Int) {
        val queue = differ.currentList[position]
        val binding = holder.binding
        holder.itemView.apply {
            binding.tvTitle.text = queue.name

            binding.root.setOnClickListener {
                onItemClickListener?.let { it(queue) }
            }
        }
    }

    fun setOnItemClickListener(listener: (QueueModel) -> Unit) {
        onItemClickListener = listener
    }
}