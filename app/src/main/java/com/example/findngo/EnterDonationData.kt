package com.example.findngo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tlc.findngo.databinding.ActivityEnterDonationDataBinding

class EnterDonationData : AppCompatActivity() {

    lateinit var bindingEnterNGODonationData: ActivityEnterDonationDataBinding

    private lateinit var databaseref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingEnterNGODonationData = ActivityEnterDonationDataBinding.inflate(layoutInflater)
        setContentView(bindingEnterNGODonationData.root)


        try {
            bindingEnterNGODonationData.UploadNGODonationDataButton.setOnClickListener {
                val NGOName: String? = bindingEnterNGODonationData.uploadNGOName.text.toString()
                val NGOFundUse: String? =
                    bindingEnterNGODonationData.uploadNGOFundUse.text.toString()
                val NGOLogoImage: String? =
                    bindingEnterNGODonationData.uploadNGODonationImage.text.toString()
                val NGOSiteLink: String? =
                    bindingEnterNGODonationData.uploadNGODonationSiteLink.text.toString()


                databaseref = FirebaseDatabase.getInstance().getReference("donation")


                databaseref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val Totalkeys: ArrayList<String> = ArrayList()

                        if (dataSnapshot.exists()) {
                            for (d in dataSnapshot.children) {
                                Totalkeys.add(d.key.toString())
                            }
                        }

                        val NewKeyNumber: Int = Totalkeys.size + 1

                        val NewDataKey = "$NewKeyNumber"

                        val ngoData: ArrayList<String> = arrayListOf()

                        if (NGOName != null && NGOFundUse != null && NGOLogoImage != null && NGOSiteLink != null) {
                            ngoData.add(NGOName)
                            ngoData.add(NGOFundUse)
                            ngoData.add(NGOLogoImage)
                            ngoData.add(NGOSiteLink)
                        }

                        databaseref.child(NewDataKey).setValue(ngoData).addOnSuccessListener {
                            ClearInput()
                        }.addOnFailureListener {
                            ClearInput()
                            Toast.makeText(
                                this@EnterDonationData,
                                "Failed to Save Data",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {} //onCancelled
                })

            }


        }catch (e:Exception){
            Log.d("AppCrash",e.toString())
        }
    }

    fun ClearInput() {
        bindingEnterNGODonationData.uploadNGOName.text.clear()
        bindingEnterNGODonationData.uploadNGOFundUse.text.clear()
        bindingEnterNGODonationData.uploadNGODonationImage.text.clear()
        bindingEnterNGODonationData.uploadNGODonationSiteLink.text.clear()

        Toast.makeText(this@EnterDonationData, "Data is Saved", Toast.LENGTH_LONG).show()
    }

}