package com.example.queue.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.queue.databinding.ItemMemberBinding
import com.example.queue.models.MemberModel

class MemberAdapter : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    inner class MemberViewHolder(val binding: ItemMemberBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<MemberModel>() {
        override fun areItemsTheSame(oldItem: MemberModel, newItem: MemberModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MemberModel, newItem: MemberModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding = ItemMemberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MemberViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((MemberModel) -> Unit)? = null

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val queue = differ.currentList[position]
        val binding = holder.binding
        holder.itemView.apply {
            binding.tvPosition.text = queue.position.toString()
            binding.tvMemberName.text = queue.username

            binding.root.setOnClickListener {
                onItemClickListener?.let { it(queue) }
            }
        }
    }

    fun setOnItemClickListener(listener: (MemberModel) -> Unit) {
        onItemClickListener = listener
    }
}