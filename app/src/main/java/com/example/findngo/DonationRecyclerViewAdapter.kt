package com.example.findngo

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tlc.findngo.R

class DonationRecyclerViewAdapter :
    ListAdapter<DonationItem, DonationRecyclerViewAdapter.DonationViewHolder>(DonationDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.donation_item_list, parent, false)
        return DonationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.DonationNgoName)
        private val detailsView: TextView = itemView.findViewById(R.id.DonationNgoDetails)
        private val imageView: ImageView = itemView.findViewById(R.id.donationImageView)
        private val donationButton: Button = itemView.findViewById(R.id.DonationPage)
        private val siteButton: Button = itemView.findViewById(R.id.DonationNGOSitePage)

        fun bind(item: DonationItem) {
            nameView.text = item.name
            detailsView.text = item.details

            if (item.logoImage.isNotBlank()) {
                Picasso.get()
                    .load(item.logoImage)
                    .placeholder(R.drawable.explore1)
                    .error(R.drawable.explore1)
                    .resize(300, 200)
                    .centerInside()
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.explore1)
            }

            donationButton.setOnClickListener {
                val context = itemView.context
                if (item.donationPageLink.isNotBlank()) {
                    val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.donationPageLink))
                    if (item.siteLink.isNotBlank() && !item.donationPageLink.contains(item.siteLink)) {
                        Toast.makeText(
                            context,
                            "Given Donation site is Different from Main WebSite. Please visit Main Website for Donation.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    try {
                        context.startActivity(urlIntent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Cannot open donation link", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "No donation link available", Toast.LENGTH_SHORT).show()
                }
            }

            siteButton.setOnClickListener {
                val context = itemView.context
                if (item.siteLink.isNotBlank()) {
                    val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.siteLink))
                    try {
                        context.startActivity(urlIntent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Cannot open website link", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "No website link available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    object DonationDiffCallback : DiffUtil.ItemCallback<DonationItem>() {
        override fun areItemsTheSame(oldItem: DonationItem, newItem: DonationItem): Boolean {
            return if (oldItem.id.isNotBlank() && newItem.id.isNotBlank()) {
                oldItem.id == newItem.id
            } else {
                oldItem.name == newItem.name
            }
        }

        override fun areContentsTheSame(oldItem: DonationItem, newItem: DonationItem): Boolean {
            return oldItem == newItem
        }
    }
}
