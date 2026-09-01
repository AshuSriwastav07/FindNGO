package com.example.findngo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.squareup.picasso.Picasso
import com.tlc.findngo.R

class HomePageFragment : Fragment() {

    private val listOfKeys = ArrayList<String>()
    private var bannerRef: DatabaseReference? = null
    private var bannerListener: ValueEventListener? = null
    private var popularKeysRef: DatabaseReference? = null
    private var popularKeysListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        val database = Firebase.database

        // Banner Images
        val bannerImageIds = listOf(
            R.id.PopulerImageCardViewBanner1, R.id.PopulerImageCardViewBanner2,
            R.id.PopulerImageCardViewBanner3, R.id.PopulerImageCardViewBanner4,
            R.id.PopulerImageCardViewBanner5, R.id.PopulerImageCardViewBanner6,
            R.id.PopulerImageCardViewBanner7, R.id.PopulerImageCardViewBanner8,
            R.id.PopulerImageCardViewBanner9
        )

        bannerRef = database.getReference("bannerImages")
        bannerRef?.keepSynced(true)
        bannerListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded || view == null) return
                val imageLinks = snapshot.value as? List<*> ?: return
                for (i in imageLinks.indices) {
                    if (i < bannerImageIds.size) {
                        val imageUrl = imageLinks[i]?.toString() ?: continue
                        if (imageUrl.isNotBlank()) {
                            val imageView = view.findViewById<ImageView>(bannerImageIds[i])
                            if (imageView != null) {
                                Picasso.get()
                                    .load(imageUrl)
                                    .placeholder(R.drawable.explore1)
                                    .error(R.drawable.explore1)
                                    .resize(400, 200)
                                    .centerCrop()
                                    .into(imageView)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomePageFragment", "Banner error: ${error.message}")
            }
        }
        bannerRef?.addValueEventListener(bannerListener as ValueEventListener)

        // Category Cards - Instant navigation
        view.findViewById<CardView>(R.id.ngo_categories1)?.setOnClickListener { openCategory("Agriculture", "agriculture") }
        view.findViewById<CardView>(R.id.ngo_categories2)?.setOnClickListener { openCategory("Arts & Culture", "art") }
        view.findViewById<CardView>(R.id.ngo_categories3)?.setOnClickListener { openCategory("Child Education", "child") }
        view.findViewById<CardView>(R.id.ngo_categories4)?.setOnClickListener { openCategory("Women Empowerment", "women") }
        view.findViewById<CardView>(R.id.ngo_categories5)?.setOnClickListener { openCategory("Environment", "environment") }
        view.findViewById<CardView>(R.id.ngo_categories6)?.setOnClickListener { openCategory("Education & Literacy", "education") }
        view.findViewById<CardView>(R.id.ngo_categories7)?.setOnClickListener { openCategory("Animal Welfare", "animal") }
        view.findViewById<CardView>(R.id.ngo_categories8)?.setOnClickListener { openCategory("Human Rights", "human rights") }

        // All NGOs Button
        view.findViewById<Button>(R.id.ShowAllNGOsListBtn)?.setOnClickListener {
            startActivity(Intent(context, All_NGO_ListView::class.java))
        }

        // Popular Cards setup
        setupPopularCards(view)

        return view
    }

    private fun openCategory(name: String, filter: String) {
        val intent = Intent(context, CategoryListView::class.java).apply {
            putExtra("categoryName", name)
            putExtra("categoryFilter", filter)
        }
        startActivity(intent)
    }

    private fun setupPopularCards(view: View) {
        val popularCardIds = listOf(
            R.id.PopulerNGOsCardView1, R.id.PopulerNGOsCardView2,
            R.id.PopulerNGOsCardView3, R.id.PopulerNGOsCardView4,
            R.id.PopulerNGOsCardView5, R.id.PopulerNGOsCardView6,
            R.id.PopulerNGOsCardView7, R.id.PopulerNGOsCardView8
        )

        for (i in popularCardIds.indices) {
            view.findViewById<CardView>(popularCardIds[i])?.setOnClickListener {
                if (listOfKeys.size > i) {
                    sendDataToNgoView(listOfKeys[i])
                } else {
                    Toast.makeText(context, "Loading data, please wait...", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loadPopularCardsData(view)
    }

    private fun loadPopularCardsData(view: View) {
        val nameIds = listOf(
            R.id.PopulerNGOsName1, R.id.PopulerNGOsName2, R.id.PopulerNGOsName3, R.id.PopulerNGOsName4,
            R.id.PopulerNGOsName5, R.id.PopulerNGOsName6, R.id.PopulerNGOsName7, R.id.PopulerNGOsName8
        )
        val addressIds = listOf(
            R.id.PopulerNGOsAddress1, R.id.PopulerNGOsAddress2, R.id.PopulerNGOsAddress3, R.id.PopulerNGOsAddress4,
            R.id.PopulerNGOsAddress5, R.id.PopulerNGOsAddress6, R.id.PopulerNGOsAddress7, R.id.PopulerNGOsAddress8
        )
        val imageIds = listOf(
            R.id.PopulerImageCardView1, R.id.PopulerImageCardView2, R.id.PopulerImageCardView3, R.id.PopulerImageCardView4,
            R.id.PopulerImageCardView5, R.id.PopulerImageCardView6, R.id.PopulerImageCardView7, R.id.PopulerImageCardView8
        )

        val database = Firebase.database
        popularKeysRef = database.getReference("PopulerHomePageCardView")

        popularKeysListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!isAdded || getView() == null) return
                if (dataSnapshot.exists()) {
                    listOfKeys.clear()
                    for (childSnapshot in dataSnapshot.children) {
                        val value = childSnapshot.getValue(String::class.java)
                        if (value != null) {
                            listOfKeys.add(value)
                        }
                    }

                    for (i in listOfKeys.indices) {
                        if (i >= nameIds.size) break
                        val key = listOfKeys[i]
                        val ngoRef = database.getReference("NGO_DATA").child(key)
                        ngoRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (!isAdded || getView() == null) return
                                val item = NGOItem.fromSnapshotValue(key, snapshot.value) ?: return

                                val nameTv = view.findViewById<TextView>(nameIds[i])
                                val addressTv = view.findViewById<TextView>(addressIds[i])
                                val iv = view.findViewById<ImageView>(imageIds[i])

                                nameTv?.text = item.name
                                addressTv?.text = item.address

                                if (item.logoImage.isNotBlank() && iv != null) {
                                    Picasso.get()
                                        .load(item.logoImage)
                                        .placeholder(R.drawable.name)
                                        .error(R.drawable.name)
                                        .resize(100, 100)
                                        .centerInside()
                                        .into(iv)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("HomePageFragment", "Error loading card item $i: ${error.message}")
                            }
                        })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("HomePageFragment", "Failed to read popular keys", databaseError.toException())
            }
        }
        popularKeysRef?.addValueEventListener(popularKeysListener as ValueEventListener)
    }

    private fun sendDataToNgoView(key: String) {
        val database = Firebase.database
        val fetchData = database.getReference("NGO_DATA").child(key)

        fetchData.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val item = NGOItem.fromSnapshotValue(key, snapshot.value)
                    if (item != null) {
                        val intent = Intent(context, OpenNGOData::class.java).apply {
                            putStringArrayListExtra("All_NGO_Data", item.toArrayList())
                        }
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("HomePageFragment", "Failed to fetch NGO data", error.toException())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerListener?.let { bannerRef?.removeEventListener(it) }
        popularKeysListener?.let { popularKeysRef?.removeEventListener(it) }
    }
}
