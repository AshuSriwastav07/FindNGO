package com.example.findngo

import BookmarkDB
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.tlc.findngo.R

class OpenNGOData : AppCompatActivity() {

    private lateinit var dbHelper: BookmarkDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_ngo_data_page)

        dbHelper = BookmarkDB(this)

        val rawDataList = intent.getStringArrayListExtra("All_NGO_Data")
        if (rawDataList != null && rawDataList.isNotEmpty()) {
            val ngoItem = NGOItem.fromList(rawDataList)
            displayNgoData(ngoItem)
        } else {
            Log.e("OpenNGOData", "No NGO data passed to activity")
            Toast.makeText(this, "Failed to load NGO profile", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun displayNgoData(item: NGOItem) {
        val nameTv: TextView = findViewById(R.id.NGOName)
        val addressTv: TextView = findViewById(R.id.NGOAddress)
        val regIdTv: TextView = findViewById(R.id.NGO_RegistrationNo)
        val phoneTv: TextView = findViewById(R.id.NGO_PhoneNo)
        val mailTv: TextView = findViewById(R.id.NGO_MailID)
        val typeTv: TextView = findViewById(R.id.NGO_Type)
        val uniqueIdTv: TextView = findViewById(R.id.NGO_UniqueID)
        val sectorTv: TextView = findViewById(R.id.NGO_Sectors)
        val siteBtn: Button = findViewById(R.id.NGO_site)
        val logoIv: ImageView = findViewById(R.id.NGO_Logo_Image)
        val bookmarkBtn: ImageButton = findViewById(R.id.bookmarkButton)
        val copyRegBtn: ImageButton = findViewById(R.id.copyRegistrationNumber)
        val copyMailBtn: ImageButton = findViewById(R.id.copyEmailId)
        val copyPhoneBtn: ImageButton = findViewById(R.id.copyContactNumber)

        nameTv.text = item.name
        addressTv.text = item.address
        regIdTv.text = item.regId
        phoneTv.text = item.phoneNo
        mailTv.text = item.email
        typeTv.text = item.ngoType
        uniqueIdTv.text = item.uniqueId
        sectorTv.text = item.sector

        if (item.logoImage.isNotBlank()) {
            Picasso.get()
                .load(item.logoImage)
                .placeholder(R.drawable.name)
                .error(R.drawable.name)
                .resize(200, 200)
                .centerInside()
                .into(logoIv)
        } else {
            logoIv.setImageResource(R.drawable.name)
        }

        // Bookmark status & toggle
        updateBookmarkUI(bookmarkBtn, item)

        bookmarkBtn.setOnClickListener {
            if (dbHelper.isBookmarked(item.name)) {
                dbHelper.removeBookmark(item.name)
                bookmarkBtn.setImageResource(R.drawable.baseline_bookmark_add_24)
                Toast.makeText(this, "Removed from bookmarks", Toast.LENGTH_SHORT).show()
            } else {
                dbHelper.addBookmarkItem(item)
                bookmarkBtn.setImageResource(R.drawable.baseline_bookmark_added_24)
                Toast.makeText(this, "Saved to bookmarks", Toast.LENGTH_SHORT).show()
            }
        }

        siteBtn.setOnClickListener {
            if (item.siteLink.isNotBlank()) {
                val url = if (!item.siteLink.startsWith("http://") && !item.siteLink.startsWith("https://")) {
                    "https://${item.siteLink}"
                } else {
                    item.siteLink
                }
                try {
                    val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(urlIntent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Cannot open website", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No website link available", Toast.LENGTH_SHORT).show()
            }
        }

        copyRegBtn.setOnClickListener {
            copyToClipboard("Registration Number", item.regId)
            Toast.makeText(this, "Registration Number copied", Toast.LENGTH_SHORT).show()
        }

        copyMailBtn.setOnClickListener {
            if (item.email.isNotBlank()) {
                sendEmail(item.email)
            } else {
                Toast.makeText(this, "No email available", Toast.LENGTH_SHORT).show()
            }
        }

        copyPhoneBtn.setOnClickListener {
            copyToClipboard("Contact Number", item.phoneNo)
            Toast.makeText(this, "Contact Number copied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateBookmarkUI(btn: ImageButton, item: NGOItem) {
        if (dbHelper.isBookmarked(item.name)) {
            btn.setImageResource(R.drawable.baseline_bookmark_added_24)
        } else {
            btn.setImageResource(R.drawable.baseline_bookmark_add_24)
        }
    }

    private fun copyToClipboard(label: String, text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clipData)
    }

    private fun sendEmail(mailId: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$mailId")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(mailId))
            putExtra(Intent.EXTRA_SUBJECT, "Inquiry via FindNGO")
        }
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email via..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "No email client installed.", Toast.LENGTH_SHORT).show()
        }
    }
}
