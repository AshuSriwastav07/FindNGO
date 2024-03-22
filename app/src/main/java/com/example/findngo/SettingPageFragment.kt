package com.example.findngo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class SettingPageFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_setting_page, container, false)

        val SettingListView:ListView=view.findViewById(R.id.settingListView)
        val arrayAdapter: ArrayAdapter<*>
        val menu = arrayOf(
            "Enter NGO Data", "Enter Donation Data", "About US"
        )

        // access the listView from xml file
        arrayAdapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_list_item_1, menu)
        SettingListView.adapter = arrayAdapter


        SettingListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            OpenActivities(position)
        }



    return view
    }

    fun OpenActivities(clickPosition:Int){

        when(clickPosition){
            0 ->{
                val intent=Intent(context,EnterNGOData::class.java)
                startActivity(intent)
            }

            1-> {
                val intent=Intent(context,EnterDonationData::class.java)
                startActivity(intent)
            }
            2-> {
                val intent=Intent(context,AboutUs::class.java)
                startActivity(intent)
            }
        }

    }


}
