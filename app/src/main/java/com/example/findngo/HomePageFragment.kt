package com.example.findngo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.squareup.picasso.Picasso
import com.tlc.findngo.R

import kotlin.collections.ArrayList

class HomePageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_home_page, container, false)

        
        val database = Firebase.database

        //Banner Image



        val BannerImages= listOf(R.id.PopulerImageCardViewBanner1,R.id.PopulerImageCardViewBanner2,R.id.PopulerImageCardViewBanner3,R.id.PopulerImageCardViewBanner4,R.id.PopulerImageCardViewBanner5,R.id.PopulerImageCardViewBanner6,R.id.PopulerImageCardViewBanner7,R.id.PopulerImageCardViewBanner8,R.id.PopulerImageCardViewBanner9)



        val FetchImageLinks = database.getReference("bannerImages")
        FetchImageLinks.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val ImageLinks=snapshot.getValue() as MutableList<*>

                if (ImageLinks != null) {
                    for (i in ImageLinks.indices) { // Iterate over the indices of the list
                        val imageUrl = ImageLinks[i].toString()
                        val imageView = view.findViewById<ImageView>(BannerImages[i])
                        Picasso.get().load(imageUrl).into(imageView) // Assuming BannerImages is an array or list of ImageView objects
                    }
                }

                Log.d("ImageLinks",ImageLinks.toString())
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        ///Ends Here





        //Cardview
        val populerCardView1:CardView=view.findViewById(R.id.PopulerNGOsCardView1)
        val populerCardView2:CardView=view.findViewById(R.id.PopulerNGOsCardView2)
        val populerCardView3:CardView=view.findViewById(R.id.PopulerNGOsCardView3)
        val populerCardView4:CardView=view.findViewById(R.id.PopulerNGOsCardView4)
        val populerCardView5:CardView=view.findViewById(R.id.PopulerNGOsCardView5)
        val populerCardView6:CardView=view.findViewById(R.id.PopulerNGOsCardView6)
        val populerCardView7:CardView=view.findViewById(R.id.PopulerNGOsCardView7)
        val populerCardView8:CardView=view.findViewById(R.id.PopulerNGOsCardView8)


        val allNgoButton:Button=view.findViewById(R.id.ShowAllNGOsListBtn)




        //Categories button

        val AgricultureCategory:CardView=view.findViewById(R.id.ngo_categories1)
        val ArtCultureCategory:CardView=view.findViewById(R.id.ngo_categories2)
        val childEduCategory:CardView=view.findViewById(R.id.ngo_categories3)
        val womenCategory:CardView=view.findViewById(R.id.ngo_categories4)
        val EvsCategory:CardView=view.findViewById(R.id.ngo_categories5)
        val EduCategory:CardView=view.findViewById(R.id.ngo_categories6)
        val AnimalCategory:CardView=view.findViewById(R.id.ngo_categories7)
        val HumanRightsCategory:CardView=view.findViewById(R.id.ngo_categories8)


        AgricultureCategory.setOnClickListener{
            categoryKeys(1)
        }

        ArtCultureCategory.setOnClickListener{
            categoryKeys(2)
        }

       childEduCategory.setOnClickListener{
            categoryKeys(3)
        }
        womenCategory.setOnClickListener{
            categoryKeys(4)
        }
        EvsCategory.setOnClickListener{
            categoryKeys(5)
        }
        EduCategory.setOnClickListener{
            categoryKeys(6)
        }
        AnimalCategory.setOnClickListener{
            categoryKeys(7)
        }
        HumanRightsCategory.setOnClickListener{
            categoryKeys(8)
        }





        allNgoButton.setOnClickListener{
            val intent = Intent(context,All_NGO_ListView::class.java)
            startActivity(intent)
        }




        val fetchDataKey = database.getReference("PopulerHomePageCardView")
        val ListOfKeys: ArrayList<String> = ArrayList<String>()
        showCardViewData()



        // Attach a listener to the root reference
        fetchDataKey.addValueEventListener(object : ValueEventListener {  // get keys for show NGO complete data
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if data exists
                if (dataSnapshot.exists()) {
                    // Loop through each child of the root reference
                    for (childSnapshot in dataSnapshot.children) {
                        /*val key = childSnapshot.key ?: "" // Get the key of the child*/
                        val value = childSnapshot.value // Get the value of the child



                        ListOfKeys.add("$value") // values store in list

                        /*Log.d("RTDB_Value", ListOfKeys[0])*/
                    }

                } else {
                    Log.d("RTDB_Value", "No data found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("RTDB_Value", "Failed to read value", databaseError.toException())
            }
        })


        fun OpenCardView(cardviewNumber:Int){   //open cardview on click
            if(ListOfKeys.isEmpty()){
                Toast.makeText(context, "Please Wait, Data is Loading", Toast.LENGTH_SHORT).show()
            }else {

                SendDataToNgoView(ListOfKeys, cardviewNumber)
            }

        }


        //call for open cardview
        populerCardView1.setOnClickListener(){
            OpenCardView(0)
        }

        populerCardView2.setOnClickListener(){
            OpenCardView(1)
        }
        populerCardView3.setOnClickListener(){
            OpenCardView(2)
        }
        populerCardView4.setOnClickListener(){
           OpenCardView(3)
        }

        populerCardView5.setOnClickListener(){
           OpenCardView(4)
        }

        populerCardView6.setOnClickListener(){
            OpenCardView(5)

        }
        populerCardView7.setOnClickListener(){
            OpenCardView(6)


        }
        populerCardView8.setOnClickListener(){
            OpenCardView(7)
        }


        return view
    }



    private fun showCardViewData() {  //show data in homepage cardview
        val NameList = arrayOf(R.id.PopulerNGOsName1, R.id.PopulerNGOsName2,R.id.PopulerNGOsName3,R.id.PopulerNGOsName4, R.id.PopulerNGOsName5,R.id.PopulerNGOsName6,R.id.PopulerNGOsName7,R.id.PopulerNGOsName8)

        val AddressList = arrayOf(R.id.PopulerNGOsAddress1, R.id.PopulerNGOsAddress2,R.id.PopulerNGOsAddress3,R.id.PopulerNGOsAddress4, R.id.PopulerNGOsAddress5,R.id.PopulerNGOsAddress6,R.id.PopulerNGOsAddress7,R.id.PopulerNGOsAddress8)

        val ImageList = arrayOf(R.id.PopulerImageCardView1, R.id.PopulerImageCardView2,R.id.PopulerImageCardView3,R.id.PopulerImageCardView4, R.id.PopulerImageCardView5,R.id.PopulerImageCardView6,R.id.PopulerImageCardView7,R.id.PopulerImageCardView8)

        val database = Firebase.database
        val fetchDataKey = database.getReference("PopulerHomePageCardView")

        try {

        fetchDataKey.addValueEventListener(object : ValueEventListener { //retrieve data from firebase DB

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val listOfKeys = ArrayList<String>()
                    for (childSnapshot in dataSnapshot.children) {
                        val value = childSnapshot.getValue(String::class.java)
                        value?.let {
                            listOfKeys.add(it)
                        }
                    }

                    for (i in listOfKeys.indices) {
                        val fetchData = database.getReference("NGO_DATA").child(listOfKeys[i])
                        fetchData.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                val listType = object : GenericTypeIndicator<List<String>>() {}
                                val list = snapshot.getValue(listType)


                                if (!list.isNullOrEmpty() && list.size >= 1) {
                                    val nameTextView = view?.findViewById<TextView>(NameList[i])
                                    val addressTextView = view?.findViewById<TextView>(AddressList[i])
                                    val imageShowView = view?.findViewById<ImageView>(ImageList[i])

                                    Picasso.get()
                                        .load(list[7].toString())
                                        .into(imageShowView)

                                    nameTextView?.text = list[0].toString()
                                    addressTextView?.text = list[1].toString()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("RTDB_Value", "Failed to read value", error.toException())
                            }
                        })
                    }
                } else {
                    Log.d("RTDB_Value", "No data found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("RTDB_Value", "Failed to read value", databaseError.toException())
            }

        })
    }catch (e:Exception){
        Log.e("AppCrash",e.toString())

        }    }




    private fun SendDataToNgoView(KeysValues:List<String>,CardViewNumber:Int){  //send user to next activity with showing data

        if(KeysValues.isEmpty()){
            Log.d("AppCrash","Data Loading")

        }else {
            val database = Firebase.database
            val fetchData = database.getReference("NGO_DATA").child(KeysValues[CardViewNumber])

        // Read from the database
try {

        fetchData.addValueEventListener(object : ValueEventListener {
            var list = mutableListOf<String>()


            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    Log.d("RTDB_Value", "Open Data Sending")

                    list = snapshot.getValue() as MutableList<String>

                    val intent = Intent(context, OpenNGOData::class.java)
                    intent.putStringArrayListExtra("All_NGO_Data", ArrayList(list))
                    Log.d("RTDB_Value", list[0])
                    startActivity(intent)
                    Log.d("RTDB_Value", "Activty Started")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("RTDB_Value", "Failed to read value.", error.toException())
            }

            /*  ShowDataInActivity(PopulerCardViewKey)*/

        })

    }catch (e:Exception){
        Log.e("AppCrash",e.toString())

}    } }




    fun categoryKeys(categoryNumber:Int){    //get key of category and send to next activity

        val database = Firebase.database
        val CategoryDataKey: ArrayList<String> = ArrayList<String>()
        val fetchData = database.getReference("NGO_DATA")
        val intent=Intent(context,CategoryListView::class.java)

        fetchData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {    //get all data from RTDB and check if data's 8th of data contains category tye or any work similer to category, if data match get kry of data and add into key variable

                    val ngoData = data.value as MutableList<String>

                    val keys = data.key ?: ""

                    when(categoryNumber){
                        1 -> if (ngoData[8].contains("Agriculture")) {
                            CategoryDataKey.add(keys)
                            intent.putExtra("categoryName","Agriculture")

                        }

                        2 -> if (ngoData[8].contains("Art") || ngoData[8].contains("culture") ) {
                                CategoryDataKey.add(keys)
                            intent.putExtra("categoryName","Arts and Culture")

                        }

                        3 -> if (ngoData[8].contains("Child") || ngoData[8].contains("children") || ngoData[8].contains("Child Education") || ngoData[8].contains("Education") ) {
                                CategoryDataKey.add(keys)
                            intent.putExtra("categoryName","Child Education")

                        }

                        4 -> if (ngoData[8].contains("Women") || ngoData[8].contains("Women's Development & Empowerment")) {
                                CategoryDataKey.add(keys)
                            intent.putExtra("categoryName","Women")

                        }

                        5 -> if (ngoData[8].contains("Environment & Forests") || ngoData[8].contains("Environment")) {
                                CategoryDataKey.add(keys)
                            intent.putExtra("categoryName","Environment")

                        }

                        6 -> if (ngoData[8].contains("Education") || ngoData[8].contains("Education & Literacy")) {
                                CategoryDataKey.add(keys)
                            intent.putExtra("categoryName","Education")

                        }

                        7 -> if (ngoData[8].contains("Animal") || ngoData[8].contains("Animal Husbandry") || ngoData[8].contains("Dairying & Fisheries") || ngoData[8].contains("animal welfare")) {
                            CategoryDataKey.add(keys)
                            intent.putExtra("categoryName","Animal's NGO")

                        }

                        8 -> if (ngoData[8].contains("Human Rights") || ngoData[8].contains("Right to Information & Advocacy,Rural Development & Poverty Alleviation")) {
                            CategoryDataKey.add(keys)
                            intent.putExtra("categoryName","Human Rights")

                        }

                    }




                    }

                intent.putStringArrayListExtra("categoryKeyList",CategoryDataKey)
                startActivity(intent)
//                Log.d("DataListView",CategoryDataKey.toString())

                }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }


}
