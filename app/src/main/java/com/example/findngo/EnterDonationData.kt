package com.example.findngo

import android.os.Bundle
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
    private lateinit var DataToVerification:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingEnterNGODonationData = ActivityEnterDonationDataBinding.inflate(layoutInflater)
        setContentView(bindingEnterNGODonationData.root)


        try {
            bindingEnterNGODonationData.UploadNGODonationDataButton.setOnClickListener {
                val NGOName: String = bindingEnterNGODonationData.uploadNGOName.text.toString()
                val NGOFundUse: String =
                    bindingEnterNGODonationData.uploadNGOFundUse.text.toString()
                val NGOLogoImage: String =
                    bindingEnterNGODonationData.uploadNGODonationImage.text.toString()
                val NGOSiteLink: String =
                    bindingEnterNGODonationData.uploadNGODonationSiteLink.text.toString()
                val NGODonationPageLink: String =bindingEnterNGODonationData.uploadNGODonationPageLink.text.toString()


                databaseref = FirebaseDatabase.getInstance().getReference("donation")
                DataToVerification = FirebaseDatabase.getInstance()
                    .getReference("DonationDataToVerify")   //DB Ref for Data Left to verify
                val Totalkeys: ArrayList<String> = ArrayList()  //Store Keys that verify by Admin
                val TotalVerifykeys: ArrayList<String> =
                    ArrayList()   //Store keys that not verified by Admin



                DataToVerification.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (d in dataSnapshot.children) {
                                TotalVerifykeys.add(d.key.toString())
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {} //onCancelled
                })


                databaseref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (d in dataSnapshot.children) {
                                Totalkeys.add(d.key.toString())  //Store Keys that Verify
                            }
                        }


                        val NewKeyNumber: Int = Totalkeys.size + 1 + TotalVerifykeys.size

                        val NewDataKey = "$NewKeyNumber"

                        val ngoData: ArrayList<String> = arrayListOf()


                        if (NGOName.isNotEmpty() && NGOFundUse.isNotEmpty() && NGOLogoImage.isNotEmpty() && NGOSiteLink.isNotEmpty() && NGODonationPageLink.isNotEmpty()) {

                            ngoData.add(NGOName)
                            ngoData.add(NGOFundUse)
                            ngoData.add(NGOLogoImage)
                            ngoData.add(NGODonationPageLink)
                            ngoData.add(NGOSiteLink)

                            DataToVerification.child(NewDataKey).setValue(ngoData)
                                .addOnSuccessListener {

                                    ClearInput()

                                    Toast.makeText(
                                        this@EnterDonationData,
                                        "Data will be publish, as soon as Admin Approve!",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()


                                }.addOnFailureListener {
                                    Toast.makeText(
                                        this@EnterDonationData,
                                        "Failed to Save Data",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        } else {
                            Toast.makeText(
                                this@EnterDonationData,
                                "Please fill all the filed!",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })


            }
        } catch (_: Exception) {

        }
    }

            fun ClearInput() {
                bindingEnterNGODonationData.uploadNGOName.text.clear()
                bindingEnterNGODonationData.uploadNGOFundUse.text.clear()
                bindingEnterNGODonationData.uploadNGODonationImage.text.clear()
                bindingEnterNGODonationData.uploadNGODonationSiteLink.text.clear()
                bindingEnterNGODonationData.uploadNGODonationPageLink.text.clear()

                /* Toast.makeText(this@EnterDonationData, "Data is Saved", Toast.LENGTH_LONG).show()*/
            }
    }
