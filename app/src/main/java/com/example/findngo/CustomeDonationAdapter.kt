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
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.tlc.findngo.R

class CustomeDonationAdapter(
    context: Context,
    private val items: MutableList<String>, // Change to String instead of MutableList<*>
    private val details: MutableList<String>, // Change to String instead of MutableList<*>
    private val ImageLink: MutableList<String>, // Change to String instead of MutableList<*>
    private val NGODonationPage: MutableList<String>, // Change to String instead of MutableList<*>
    private val NGOSitePage: MutableList<String> // Change to String instead of MutableList<*>
) : ArrayAdapter<String>(context, R.layout.donation_item_list, R.id.DonationNgoName, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.donation_item_list, parent, false)

        val NameView= rowView.findViewById<TextView>(R.id.DonationNgoName)
        val DetailsView=rowView.findViewById<TextView>(R.id.DonationNgoDetails)
        val imageView=rowView.findViewById<ImageView>(R.id.donationImageView)
        val DonationPage=rowView.findViewById<Button>(R.id.DonationPage)
        val DonationNGOSitePage=rowView.findViewById<Button>(R.id.DonationNGOSitePage)



        if(items.isNotEmpty() && details.isNotEmpty() && ImageLink.isNotEmpty() && NGOSitePage.isNotEmpty()) {
            NameView.text = items[position]
            DetailsView.text = details[position]


            Log.d("RTDB_Value", items.last().toString())

            Picasso.get()
                .load(ImageLink[position])
                .into(imageView)



            DonationPage.setOnClickListener{
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(NGODonationPage[position])
                )

                if(NGODonationPage[position].contains(NGOSitePage[position])){
                    context.startActivity(urlIntent)
                }else{
                    Toast.makeText(context, "Given Donation site is Different from Main WebSite. Please visit Main Website for Donation.", Toast.LENGTH_SHORT).show()
                    context.startActivity(urlIntent)

                }
            }

            DonationNGOSitePage.setOnClickListener {
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(NGOSitePage[position])
                )


                context.startActivity(urlIntent)
            }
        }else{
            Toast.makeText(context, "No Donation Data Available", Toast.LENGTH_SHORT).show()

        }


        return rowView
    }
}
