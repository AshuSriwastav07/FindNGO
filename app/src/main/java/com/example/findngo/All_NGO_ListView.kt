package com.example.findngo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.tlc.findngo.R

class All_NGO_ListView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NGORecyclerViewAdapter
    private var databaseRef: DatabaseReference? = null
    private var valueEventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_ngo_list_view)

        recyclerView = findViewById(R.id.ngo_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        adapter = NGORecyclerViewAdapter { selectedNgo ->
            openNgoData(selectedNgo)
        }
        recyclerView.adapter = adapter

        val database = Firebase.database
        databaseRef = database.getReference("NGO_DATA")

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ngoList = ArrayList<NGOItem>()
                for (child in snapshot.children) {
                    val key = child.key ?: ""
                    val item = NGOItem.fromSnapshotValue(key, child.value)
                    if (item != null && item.name.isNotBlank()) {
                        ngoList.add(item)
                    }
                }
                adapter.submitList(ngoList)
                Log.d("All_NGO_ListView", "Loaded ${ngoList.size} NGOs")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("All_NGO_ListView", "Database error: ${error.message}", error.toException())
            }
        }

        databaseRef?.addValueEventListener(valueEventListener as ValueEventListener)
    }

    private fun openNgoData(item: NGOItem) {
        val intent = Intent(this, OpenNGOData::class.java).apply {
            putStringArrayListExtra("All_NGO_Data", item.toArrayList())
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        valueEventListener?.let { databaseRef?.removeEventListener(it) }
    }
}
