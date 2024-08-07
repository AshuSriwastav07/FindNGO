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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.show_ngo_data_page)

        val MyDBHelper = BookmarkDB(this)

        val AllNGODatatoShow: MutableList<String>? = intent.getStringArrayListExtra("All_NGO_Data")



        if (AllNGODatatoShow != null) {
            // Log the first item to ensure data is received
            Log.d("RTDB_Value4", AllNGODatatoShow.toString())
            // Show the received data in the activity
            ShowDataInActivity(ArrayList(AllNGODatatoShow),MyDBHelper)


        } else {
            Log.d("RTDB_Value5", "No data received from previous activity")
        }


    }
    private fun ShowDataInActivity(toShow: List<String>?,DB_Helper:BookmarkDB) {
        if (toShow != null) {
            Log.d("RTDB_Value6", toShow[0].toString())
        }
        if (toShow != null) { // Ensure there's enough data
            Log.d("RTDB_Value7", toShow[0])

            val NGO_Name: TextView = findViewById(R.id.NGOName)
            val NGO_Address: TextView = findViewById(R.id.NGOAddress)
            val NGO_Reg_ID: TextView = findViewById(R.id.NGO_RegistrationNo)
            val NGO_Phone: TextView = findViewById(R.id.NGO_PhoneNo)
            val NGO_Mail: TextView = findViewById(R.id.NGO_MailID)
            val NGO_Type: TextView = findViewById(R.id.NGO_Type)
            val NGO_UniqueID: TextView = findViewById(R.id.NGO_UniqueID)
            val NGO_Sector: TextView = findViewById(R.id.NGO_Sectors)
            val NGO_Site: Button = findViewById(R.id.NGO_site)
            val NGO_Imageview:ImageView=findViewById(R.id.NGO_Logo_Image)
            val bookmarkButton:ImageButton=findViewById(R.id.bookmarkButton)
            val copyRegNumber:ImageButton=findViewById(R.id.copyRegistrationNumber)
            val copyMailId:ImageButton=findViewById(R.id.copyEmailId)
            val copyContactNumber:ImageButton=findViewById(R.id.copyContactNumber)

            // Set data to TextViews
            NGO_Name.text = toShow[0]
            NGO_Address.text = toShow[1]
            NGO_Reg_ID.text = toShow[2]
            NGO_Phone.text = toShow[3]
            NGO_Mail.text = toShow[4]
            NGO_Type.text = toShow[5]
            NGO_UniqueID.text = toShow[6]
            Picasso.get().load(toShow[7]).into(NGO_Imageview);

            NGO_Sector.text=toShow[8]

            NGO_Site.setOnClickListener{
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(toShow[9])
                )
                startActivity(urlIntent)
            }

            copyRegNumber.setOnClickListener{
                val textToCopy:String=NGO_Reg_ID.text.toString()
                copyToClipboard(textToCopy)
                Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            }

            copyMailId.setOnClickListener {
                val MailId=NGO_Mail.text.toString()
                sendEmail(MailId)
            }

            copyContactNumber.setOnClickListener{
                val number:String=NGO_Phone.text.toString()
                copyToClipboard(number)
            }


            //Maintain DB for Book Marks
            val MyDBHelper = BookmarkDB(this)
            val data: ArrayList<NGO_Data_Model> = MyDBHelper.getNGO_Data()
            val list:ArrayList<String> = ArrayList()

            if(data.isNotEmpty()) {

                for(i in data.indices){
                    Log.d("BookmarkData",data.get(i).name)
                    list.add(data.get(i).name)
                }
            }


            if(list.contains(toShow[0])){
                    bookmarkButton.setImageResource(R.drawable.baseline_bookmark_added_24)
            }else{

            bookmarkButton.setOnClickListener{
                DB_Helper.addBookmark(toShow[0],toShow[1],toShow[2], toShow[3], toShow[4], toShow[5], toShow[6],toShow[7],toShow[8],toShow[9])
                bookmarkButton.setImageResource(R.drawable.baseline_bookmark_added_24)

                        }
            }


            Log.d("ShowDataActivity", "Data Set on TextView")
        } else {
            Log.d("ShowDataActivity", "Insufficient data received")
        }
    }



    private fun copyToClipboard(text:String){
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData .newPlainText("Copied Text", text)
        clipboardManager.setPrimaryClip(clipData)
    }


    private fun sendEmail(mailId:String) {

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailId))

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }


}

