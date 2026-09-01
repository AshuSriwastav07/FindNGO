package com.example.findngo

import BookmarkDB
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tlc.findngo.R

class BookmarkPageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyContainer: View
    private lateinit var searchInput: EditText
    private lateinit var adapter: NGORecyclerViewAdapter
    private lateinit var dbHelper: BookmarkDB
    private var allBookmarks = listOf<NGOItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookmark_page, container, false)

        recyclerView = view.findViewById(R.id.bookmarkPageRecyclerView)
        emptyContainer = view.findViewById(R.id.emptyBookmarksContainer)
        searchInput = view.findViewById(R.id.bookmarkSearchInput)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        dbHelper = BookmarkDB(requireContext())

        adapter = NGORecyclerViewAdapter { selectedNgo ->
            openNGODetails(selectedNgo)
        }
        recyclerView.adapter = adapter

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterBookmarks(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        loadBookmarks()
        return view
    }

    override fun onResume() {
        super.onResume()
        loadBookmarks()
    }

    private fun loadBookmarks() {
        allBookmarks = dbHelper.getAllBookmarks()
        filterBookmarks(searchInput.text.toString())
    }

    private fun filterBookmarks(query: String) {
        val filtered = if (query.isBlank()) {
            allBookmarks
        } else {
            val q = query.trim().lowercase()
            allBookmarks.filter {
                it.name.lowercase().contains(q) ||
                it.address.lowercase().contains(q) ||
                it.sector.lowercase().contains(q)
            }
        }

        adapter.submitList(filtered)
        if (filtered.isEmpty()) {
            emptyContainer.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyContainer.visibility = View.GONE
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
