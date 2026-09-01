package com.example.findngo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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

class CategoryListView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NGORecyclerViewAdapter
    private var databaseRef: DatabaseReference? = null
    private var valueEventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list_view)

        recyclerView = findViewById(R.id.category_ngo_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val textView: TextView = findViewById(R.id.textView5)
        val categoryName = intent.getStringExtra("categoryName") ?: "Category NGOs"
        val categoryFilter = intent.getStringExtra("categoryFilter") ?: ""
        val listDataKeys: MutableList<String>? = intent.getStringArrayListExtra("categoryKeyList")
        textView.text = categoryName

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
                        if (listDataKeys != null) {
                            if (listDataKeys.contains(key)) {
                                ngoList.add(item)
                            }
                        } else if (categoryFilter.isNotBlank()) {
                            if (matchesCategory(item.sector, categoryFilter)) {
                                ngoList.add(item)
                            }
                        } else {
                            ngoList.add(item)
                        }
                    }
                }
                adapter.submitList(ngoList)
                Log.d("CategoryListView", "Loaded ${ngoList.size} NGOs for $categoryName")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CategoryListView", "Database error: ${error.message}", error.toException())
            }
        }

        databaseRef?.addValueEventListener(valueEventListener as ValueEventListener)
    }

    private fun matchesCategory(sector: String, filter: String): Boolean {
        val s = sector.lowercase()
        return when (filter.lowercase()) {
            "agriculture" -> s.contains("agriculture")
            "art" -> s.contains("art") || s.contains("culture")
            "child" -> s.contains("child") || s.contains("children") || s.contains("child education") || s.contains("education")
            "women" -> s.contains("women")
            "environment" -> s.contains("environment") || s.contains("forest")
            "education" -> s.contains("education") || s.contains("literacy")
            "animal" -> s.contains("animal") || s.contains("husbandry") || s.contains("fisheries") || s.contains("welfare")
            "human rights" -> s.contains("human rights") || s.contains("advocacy") || s.contains("rural development") || s.contains("poverty")
            else -> s.contains(filter.lowercase())
        }
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
