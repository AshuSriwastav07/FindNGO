package com.example.findngo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class CategoryListView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list_view)

        val ListView: ListView =findViewById(R.id.category_ngo_listview)

        val textview:TextView=findViewById(R.id.textView5)

        val ListDataKeys: MutableList<String>? = intent.getStringArrayListExtra("categoryKeyList")
        val categoryName=intent.getStringExtra("categoryName")
        textview.setText(categoryName)

        Log.d("DataListView1",ListDataKeys.toString())

        var list = mutableListOf<String>()
        val DataToShowMain= mutableListOf<List<String>>()
        val ImageLinkData= mutableListOf<List<String>>()
        val database = Firebase.database
        val getNgoData=database.getReference("NGO_DATA")


        getNgoData.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){

                    val key=i.key?: ""

                    if(ListDataKeys?.contains(key.toString()) == true){
                        val ngoData = i.getValue() as MutableList<String>

                        list.add((ngoData[0]))
                        DataToShowMain.add(listOf(ngoData[0],ngoData[1],ngoData[2],ngoData[3],ngoData[4],ngoData[5],ngoData[6],ngoData[7],ngoData[8],ngoData[9]))
                        ImageLinkData.add(listOf(ngoData[7]))

                    }

                }

                val customAdapter = CustomArrayAdapter(applicationContext,list,ImageLinkData)
                ListView.adapter = customAdapter


                ListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val selectedItem = DataToShowMain[position]
                    // Call your function with the clicked item's data here
                    yourFunction((selectedItem))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




        }

    fun yourFunction(itemData: List<String>) {
        Log.d("ListViewData1", itemData.toString())
        val intent = Intent(this, OpenNGOData::class.java)
        intent.putStringArrayListExtra("All_NGO_Data", ArrayList(itemData))

        val check=itemData[8]

        startActivity(intent)
    }

    }

