package com.example.findngo

import android.R.attr.name
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tlc.findngo.databinding.ActivityEnterNgodataBinding


class EnterNGOData : AppCompatActivity() {

    lateinit var bindingEnterNGOData: ActivityEnterNgodataBinding

    private lateinit var databaseref:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingEnterNGOData = ActivityEnterNgodataBinding.inflate(layoutInflater)
        setContentView(bindingEnterNGOData.root)


       bindingEnterNGOData.UploadNGODataButton.setOnClickListener{

            val NGOName=bindingEnterNGOData.uploadNGOName.text.toString()
            val NGOAddress=bindingEnterNGOData.uploadNGOAddress.text.toString()
            val NGORegID=bindingEnterNGOData.uploadNGORegID.text.toString()
            val NGOPhoneNo=bindingEnterNGOData.uploadNGOPhoneNo.text.toString()
            val NGOEmail=bindingEnterNGOData.uploadNGOEmail.text.toString()
            val NGOType=bindingEnterNGOData.uploadNGOType.text.toString()
            val NGOUniqueID=bindingEnterNGOData.uploadNGOUniqueID.text.toString()
            val NGOImage=bindingEnterNGOData.uploadNGOLogoImage.text.toString()
            val NGOSectors=bindingEnterNGOData.uploadNGOSectors.text.toString()
            val NGOSiteLink=bindingEnterNGOData.uploadNGOSiteLink.text.toString()


           databaseref = FirebaseDatabase.getInstance().getReference("NGO_DATA")
           val Totalkeys:ArrayList<String> = ArrayList()


           databaseref.addListenerForSingleValueEvent(object : ValueEventListener {
               override fun onDataChange(dataSnapshot: DataSnapshot) {


                   if (dataSnapshot.exists()) {
                       for (d in dataSnapshot.children) {
                           Totalkeys.add(d.key.toString())
                       }
                   }

                   val NewKeyNumber:Int=Totalkeys.size+1

                   val NewDataKey= "NGO_Finder_Data_$NewKeyNumber"

                   val ngoData:ArrayList<String> = arrayListOf()

                   if(NGOName.isNotEmpty() && NGOAddress.isNotEmpty() && NGORegID.isNotEmpty() && NGOPhoneNo.isNotEmpty() && NGOEmail.isNotEmpty() && NGOType.isNotEmpty() && NGOUniqueID.isNotEmpty() && NGOImage.isNotEmpty() && NGOSectors.isNotEmpty() && NGOSiteLink.isNotEmpty()){
                   ngoData.add(NGOName)
                   ngoData.add(NGOAddress)
                   ngoData.add(NGORegID)
                   ngoData.add(NGOPhoneNo)
                   ngoData.add(NGOEmail)
                   ngoData.add(NGOType)
                   ngoData.add(NGOUniqueID)
                   ngoData.add(NGOImage)
                   ngoData.add(NGOSectors)
                   ngoData.add(NGOSiteLink)

                       databaseref.child(NewDataKey).setValue(ngoData).addOnSuccessListener {

                           bindingEnterNGOData.uploadNGOName.text.clear()
                           bindingEnterNGOData.uploadNGOAddress.text.clear()
                           bindingEnterNGOData.uploadNGORegID.text.clear()
                           bindingEnterNGOData.uploadNGOPhoneNo.text.clear()
                           bindingEnterNGOData.uploadNGOEmail.text.clear()
                           bindingEnterNGOData.uploadNGOType.text.clear()
                           bindingEnterNGOData.uploadNGOUniqueID.text.clear()
                           bindingEnterNGOData.uploadNGOLogoImage.text.clear()
                           bindingEnterNGOData.uploadNGOSectors.text.clear()
                           bindingEnterNGOData.uploadNGOSiteLink.text.clear()

                           Toast.makeText(this@EnterNGOData, "Data is Saved", Toast.LENGTH_LONG)
                               .show()


                       }.addOnFailureListener {
                           Toast.makeText(
                               this@EnterNGOData,
                               "Failed to Save Data",
                               Toast.LENGTH_LONG
                           ).show()
                       }
                   }else{
                       Toast.makeText(this@EnterNGOData, "Please fill all the filed!", Toast.LENGTH_SHORT).show()

                   }
               }

               override fun onCancelled(error: DatabaseError) {} //onCancelled
           })




        }


        }
    }
