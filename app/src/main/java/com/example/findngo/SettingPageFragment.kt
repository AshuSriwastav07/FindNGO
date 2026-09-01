package com.example.findngo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.tlc.findngo.R

class SettingPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting_page, container, false)

        view.findViewById<CardView>(R.id.cardSubmitNGO)?.setOnClickListener {
            startActivity(Intent(context, EnterNGOData::class.java))
        }

        view.findViewById<CardView>(R.id.cardSubmitDonation)?.setOnClickListener {
            startActivity(Intent(context, EnterDonationData::class.java))
        }

        view.findViewById<CardView>(R.id.cardAboutUs)?.setOnClickListener {
            startActivity(Intent(context, AboutUs::class.java))
        }

        return view
    }
}
