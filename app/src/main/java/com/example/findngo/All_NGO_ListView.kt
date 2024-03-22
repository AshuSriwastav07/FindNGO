package com.example.findngo

import BookmarkDB
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class All_NGO_ListView : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_ngo_list_view)

        val ListView: ListView=findViewById(R.id.ngo_listview)

        val MyDBHelper = BookmarkDB(this)


        val database = Firebase.database
        val getNgoData=database.getReference("NGO_DATA")

        getNgoData.addValueEventListener(object : ValueEventListener{
            var list = mutableListOf<String>()
            val DataToShowMain= mutableListOf<List<String>>()
            val ImageLinkData= mutableListOf<List<String>>()

            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){

                    val ngoData = i.getValue() as MutableList<String>

                    list.add((ngoData[0]))
                    DataToShowMain.add(listOf(ngoData[0],ngoData[1],ngoData[2],ngoData[3],ngoData[4],ngoData[5],ngoData[6],ngoData[7],ngoData[8],ngoData[9]))
                    ImageLinkData.add(listOf(ngoData[7]))


                }

                Log.d("ListViewData", list.toString())
                Log.d("ListViewData", DataToShowMain.toString())



                if(list.isNotEmpty() && ImageLinkData.isNotEmpty()) {
                    val customAdapter = CustomArrayAdapter(applicationContext, list, ImageLinkData)
                    ListView.adapter = customAdapter
                }


                ListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val selectedItem = DataToShowMain[position]
                    // Call your function with the clicked item's data here
                    yourFunction((selectedItem))
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }



        })



        }
    fun yourFunction(itemData: List<String>) {
        Log.d("ListViewData1", itemData.toString())
        val intent = Intent(this, OpenNGOData::class.java)
        intent.putStringArrayListExtra("All_NGO_Data", ArrayList(itemData))

        val check=itemData[8]
        if(check.contains("Art")){

            Log.d("ListViewData", "Yes")

        }

        startActivity(intent)
    }


}
