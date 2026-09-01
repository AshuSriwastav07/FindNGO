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

    private lateinit var bindingEnterNGODonationData: ActivityEnterDonationDataBinding
    private lateinit var databaseRef: DatabaseReference
    private lateinit var dataToVerification: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingEnterNGODonationData = ActivityEnterDonationDataBinding.inflate(layoutInflater)
        setContentView(bindingEnterNGODonationData.root)

        bindingEnterNGODonationData.submitDonationToolbar.setNavigationOnClickListener {
            finish()
        }

        bindingEnterNGODonationData.UploadNGODonationDataButton.setOnClickListener {
            val ngoName = bindingEnterNGODonationData.uploadNGOName.text.toString().trim()
            val ngoFundUse = bindingEnterNGODonationData.uploadNGOFundUse.text.toString().trim()
            val ngoLogoImage = bindingEnterNGODonationData.uploadNGODonationImage.text.toString().trim()
            val ngoDonationPageLink = bindingEnterNGODonationData.uploadNGODonationPageLink.text.toString().trim()
            val ngoSiteLink = bindingEnterNGODonationData.uploadNGODonationSiteLink.text.toString().trim()

            if (ngoName.isEmpty() || ngoFundUse.isEmpty() || ngoLogoImage.isEmpty() ||
                ngoDonationPageLink.isEmpty() || ngoSiteLink.isEmpty()
            ) {
                Toast.makeText(this@EnterDonationData, "Please fill in all required fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            databaseRef = FirebaseDatabase.getInstance().getReference("donation")
            dataToVerification = FirebaseDatabase.getInstance().getReference("DonationDataToVerify")

            val totalVerifyKeys = ArrayList<String>()
            dataToVerification.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (d in dataSnapshot.children) {
                            totalVerifyKeys.add(d.key.toString())
                        }
                    }

                    val totalKeys = ArrayList<String>()
                    databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (d in snapshot.children) {
                                    totalKeys.add(d.key.toString())
                                }
                            }

                            val newKeyNumber = totalKeys.size + 1 + totalVerifyKeys.size
                            val newDataKey = "$newKeyNumber"

                            val ngoData = arrayListOf(
                                ngoName,
                                ngoFundUse,
                                ngoLogoImage,
                                ngoDonationPageLink,
                                ngoSiteLink
                            )

                            dataToVerification.child(newDataKey).setValue(ngoData)
                                .addOnSuccessListener {
                                    clearInput()
                                    Toast.makeText(
                                        this@EnterDonationData,
                                        "Donation drive submitted! It will be published upon admin verification.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this@EnterDonationData,
                                        "Failed to submit. Please try again.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@EnterDonationData, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@EnterDonationData, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun clearInput() {
        bindingEnterNGODonationData.uploadNGOName.text?.clear()
        bindingEnterNGODonationData.uploadNGOFundUse.text?.clear()
        bindingEnterNGODonationData.uploadNGODonationImage.text?.clear()
        bindingEnterNGODonationData.uploadNGODonationSiteLink.text?.clear()
        bindingEnterNGODonationData.uploadNGODonationPageLink.text?.clear()
    }
}
