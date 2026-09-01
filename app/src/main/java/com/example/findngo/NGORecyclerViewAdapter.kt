package com.example.findngo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tlc.findngo.R

class NGORecyclerViewAdapter(
    private val onItemClick: (NGOItem) -> Unit
) : ListAdapter<NGOItem, NGORecyclerViewAdapter.NGOViewHolder>(NGODiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NGOViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custome_ngo_list, parent, false)
        return NGOViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: NGOViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NGOViewHolder(
        itemView: View,
        private val onItemClick: (NGOItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.listItemName)
        private val logoImageView: ImageView = itemView.findViewById(R.id.listImageView)
        private val sectorTextView: TextView = itemView.findViewById(R.id.listItemSector)
        private var currentItem: NGOItem? = null

        init {
            itemView.setOnClickListener {
                currentItem?.let { onItemClick(it) }
            }
        }

        fun bind(item: NGOItem) {
            currentItem = item
            nameTextView.text = item.name

            if (item.sector.isNotBlank()) {
                val primarySector = item.sector.split(",").firstOrNull()?.trim() ?: item.sector
                sectorTextView.text = primarySector
                sectorTextView.visibility = View.VISIBLE
            } else {
                sectorTextView.text = "NGO"
                sectorTextView.visibility = View.VISIBLE
            }

            if (item.logoImage.isNotBlank()) {
                Picasso.get()
                    .load(item.logoImage)
                    .placeholder(R.drawable.name)
                    .error(R.drawable.name)
                    .resize(120, 120)
                    .centerInside()
                    .into(logoImageView)
            } else {
                logoImageView.setImageResource(R.drawable.name)
            }
        }
    }

    object NGODiffCallback : DiffUtil.ItemCallback<NGOItem>() {
        override fun areItemsTheSame(oldItem: NGOItem, newItem: NGOItem): Boolean {
            return if (oldItem.uniqueId.isNotBlank() && newItem.uniqueId.isNotBlank()) {
                oldItem.uniqueId == newItem.uniqueId
            } else {
                oldItem.name == newItem.name && oldItem.regId == newItem.regId
            }
        }

        override fun areContentsTheSame(oldItem: NGOItem, newItem: NGOItem): Boolean {
            return oldItem == newItem
        }
    }
}
