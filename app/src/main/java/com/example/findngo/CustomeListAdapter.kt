package com.example.findngo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.tlc.findngo.R


class CustomArrayAdapter(context: Context, private val items: List<String>, private val ImageLink: MutableList<List<String>>) :
    ArrayAdapter<String>(context, R.layout.custome_ngo_list, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.custome_ngo_list, parent, false)

        val textView = rowView.findViewById<TextView>(R.id.listItemName)
        val imageView=rowView.findViewById<ImageView>(R.id.listImageView)
        textView.text = items[position]
        Log.d("RTDB_Value",items.last())

        Picasso.get()
            .load(ImageLink[position].last())
            .into(imageView)


        return rowView
    }
}
