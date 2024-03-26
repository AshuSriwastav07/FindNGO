package com.example.findngo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.tlc.findngo.R

class CustomeDonationAdapter(
    context: Context,
    private val items: MutableList<String>, // Change to String instead of MutableList<*>
    private val details: MutableList<String>, // Change to String instead of MutableList<*>
    private val ImageLink: MutableList<String>, // Change to String instead of MutableList<*>
    private val DonationLink: MutableList<String> // Change to String instead of MutableList<*>
) : ArrayAdapter<String>(context, R.layout.donation_item_list, R.id.DonationNgoName, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.donation_item_list, parent, false)

        val NameView= rowView.findViewById<TextView>(R.id.DonationNgoName)
        val DetailsView=rowView.findViewById<TextView>(R.id.DonationNgoDetails)
        val imageView=rowView.findViewById<ImageView>(R.id.donationImageView)
        val donationbutton=rowView.findViewById<Button>(R.id.donationbutton)

        NameView.text = items[position]
        DetailsView.text = details[position]


        Log.d("RTDB_Value",items.last().toString())

        Picasso.get()
            .load(ImageLink[position])
            .into(imageView)


        donationbutton.setOnClickListener{
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(DonationLink[position])
            )

            context.startActivity(urlIntent)
        }

        return rowView
    }
}
