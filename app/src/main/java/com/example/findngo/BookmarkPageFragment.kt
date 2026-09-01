package com.example.findngo

import BookmarkDB
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tlc.findngo.R

class BookmarkPageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var adapter: NGORecyclerViewAdapter
    private lateinit var dbHelper: BookmarkDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookmark_page, container, false)

        recyclerView = view.findViewById(R.id.bookmarkPageRecyclerView)
        emptyText = view.findViewById(R.id.emptyBookmarksText)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        dbHelper = BookmarkDB(requireContext())

        adapter = NGORecyclerViewAdapter { selectedNgo ->
            openNGODetails(selectedNgo)
        }
        recyclerView.adapter = adapter

        loadBookmarks()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadBookmarks()
    }

    private fun loadBookmarks() {
        val bookmarks = dbHelper.getAllBookmarks()
        adapter.submitList(bookmarks)
        if (bookmarks.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun openNGODetails(item: NGOItem) {
        val intent = Intent(context, OpenNGOData::class.java).apply {
            putStringArrayListExtra("All_NGO_Data", item.toArrayList())
        }
        startActivity(intent)
    }
}
