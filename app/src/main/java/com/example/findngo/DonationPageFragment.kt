package com.example.findngo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.tlc.findngo.R

class DonationPageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: DonationRecyclerViewAdapter
    private var databaseRef: DatabaseReference? = null
    private var valueEventListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donation_page, container, false)

        recyclerView = view.findViewById(R.id.donationRecyclerView)
        progressBar = view.findViewById(R.id.donationProgressBar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        adapter = DonationRecyclerViewAdapter()
        recyclerView.adapter = adapter

        val database = Firebase.database
        databaseRef = database.getReference("donation")

        progressBar.visibility = View.VISIBLE

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressBar.visibility = View.GONE
                val donationList = ArrayList<DonationItem>()

                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val key = child.key ?: ""
                        val item = DonationItem.fromSnapshotValue(key, child.value)
                        if (item != null && item.name.isNotBlank()) {
                            donationList.add(item)
                        }
                    }
                    adapter.submitList(donationList)
                } else {
                    if (isAdded) {
                        Toast.makeText(context, "No donation programs available right now", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                Log.e("DonationPageFragment", "Error fetching donation data: ${error.message}", error.toException())
            }
        }

        databaseRef?.addValueEventListener(valueEventListener as ValueEventListener)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        valueEventListener?.let { databaseRef?.removeEventListener(it) }
    }
}
