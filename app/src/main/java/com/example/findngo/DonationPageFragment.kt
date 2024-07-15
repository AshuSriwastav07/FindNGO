package com.example.findngo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.tlc.findngo.R

class DonationPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_donation_page, container, false)

        val listView=view.findViewById<ListView>(R.id.donationListview)

        val database = Firebase.database
        val getDonationData=database.getReference("donation")

        try {
            getDonationData.addValueEventListener(object : ValueEventListener {
                var name = mutableListOf<String>()
                val details = mutableListOf<String>()
                val ImageLinkData = mutableListOf<String>()
                val NGODonationPage = mutableListOf<String>()
                val NGOSitePage = mutableListOf<String>()


                override fun onDataChange(snapshot: DataSnapshot) {
                    name.clear()
                    details.clear()
                    ImageLinkData.clear()
                    NGODonationPage.clear()

                    if(snapshot.exists()){
                    for (i in snapshot.children) {

                        val ngoData = i.getValue() as MutableList<String>

                        name.add(ngoData[0])
                        details.add(ngoData[1])
                        ImageLinkData.add(ngoData[2])
                        NGODonationPage.add(ngoData[3])
                        NGOSitePage.add(ngoData[4])


//                        Log.e("FirebaseError",ngoData[0])
//                        Log.e("FirebaseError",ngoData[1])
//                        Log.e("FirebaseError",ngoData[2])
//                        Log.e("FirebaseError",ngoData[3])
//                        Log.e("FirebaseError",ngoData[4])
//                        Log.e("FirebaseError","---------------------------")
//

                    }

                    /*Log.d("ListViewData", name.toString())
                Log.d("ListViewData", details.toString())
*/
                    if(name.isNotEmpty() && details.isNotEmpty() && ImageLinkData.isNotEmpty() && NGODonationPage.isNotEmpty()) {
                        val adapter = CustomeDonationAdapter(
                            requireContext(),
                            name,
                            details,
                            ImageLinkData,
                            NGODonationPage,
                            NGOSitePage
                        )
                        listView.adapter = adapter
                    }

                }else{
                        Toast.makeText(context, "Please Wait, Data is Loading", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DonationPageFragment", "Error fetching data: $error")
                    // Handle the error appropriately, e.g., display an error message to the user
                }



            })
        }catch (e:Exception){
            Log.e("AppCrash",e.toString())
        }



        return view
    }


}

