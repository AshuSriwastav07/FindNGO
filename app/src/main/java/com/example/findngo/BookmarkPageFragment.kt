package com.example.findngo

import BookmarkDB
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.tlc.findngo.R

class BookmarkPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        val view= inflater.inflate(R.layout.fragment_bookmark_page, container, false)

        val listview:ListView=view.findViewById(R.id.bookmarkPageListView)

        val MyDBHelper = BookmarkDB(requireContext())

        val data: ArrayList<NGO_Data_Model> = MyDBHelper.getNGO_Data()
        val list:ArrayList<String> = ArrayList()
        val ImageLinkData: MutableList<List<String>> = mutableListOf()
        val DataToShowMain= mutableListOf<List<String>>()



        if(data.isNotEmpty()) {

            for(i in data.indices){
                Log.d("BookmarkData",data.get(i).name)
                list.add(data.get(i).name)
                ImageLinkData.add(listOf(data.get(i).logo_image))
                DataToShowMain.add(listOf(data.get(i).name,data.get(i).address,data.get(i).reg_id,data.get(i).phone_no,data.get(i).email,data.get(i).type,data.get(i).unique_id,data.get(i).logo_image,data.get(i).sector,data.get(i).site_link))

            }

        }

        val customAdapter = CustomArrayAdapter(requireContext(), list, ImageLinkData)
        listview.adapter = customAdapter



        listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = DataToShowMain[position]
            // Call your function with the clicked item's data here
            OpenNGODetails((selectedItem))
        }

        return view
    }

    
    fun OpenNGODetails(itemData: List<String>) {
        Log.d("ListViewData1", itemData.toString())
        val intent = Intent(context, OpenNGOData::class.java)
        intent.putStringArrayListExtra("All_NGO_Data", ArrayList(itemData))
        startActivity(intent)
    }



}
