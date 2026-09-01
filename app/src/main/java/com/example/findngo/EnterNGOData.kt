package com.example.findngo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tlc.findngo.databinding.ActivityEnterNgodataBinding

class EnterNGOData : AppCompatActivity() {

    private lateinit var bindingEnterNGOData: ActivityEnterNgodataBinding
    private lateinit var databaseRef: DatabaseReference
    private lateinit var dataToVerification: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingEnterNGOData = ActivityEnterNgodataBinding.inflate(layoutInflater)
        setContentView(bindingEnterNGOData.root)

        bindingEnterNGOData.submitNgoToolbar.setNavigationOnClickListener {
            finish()
        }

        bindingEnterNGOData.UploadNGODataButton.setOnClickListener {
            val ngoName = bindingEnterNGOData.uploadNGOName.text.toString().trim()
            val ngoAddress = bindingEnterNGOData.uploadNGOAddress.text.toString().trim()
            val ngoRegID = bindingEnterNGOData.uploadNGORegID.text.toString().trim()
            val ngoPhoneNo = bindingEnterNGOData.uploadNGOPhoneNo.text.toString().trim()
            val ngoEmail = bindingEnterNGOData.uploadNGOEmail.text.toString().trim()
            val ngoType = bindingEnterNGOData.uploadNGOType.text.toString().trim()
            val ngoUniqueID = bindingEnterNGOData.uploadNGOUniqueID.text.toString().trim()
            val ngoImage = bindingEnterNGOData.uploadNGOLogoImage.text.toString().trim()
            val ngoSectors = bindingEnterNGOData.uploadNGOSectors.text.toString().trim()
            val ngoSiteLink = bindingEnterNGOData.uploadNGOSiteLink.text.toString().trim()

            if (ngoName.isEmpty() || ngoAddress.isEmpty() || ngoRegID.isEmpty() ||
                ngoPhoneNo.isEmpty() || ngoEmail.isEmpty() || ngoType.isEmpty() ||
                ngoUniqueID.isEmpty() || ngoImage.isEmpty() || ngoSectors.isEmpty() || ngoSiteLink.isEmpty()
            ) {
                Toast.makeText(this@EnterNGOData, "Please fill in all required fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            databaseRef = FirebaseDatabase.getInstance().getReference("NGO_DATA")
            dataToVerification = FirebaseDatabase.getInstance().getReference("DataToVerify")

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
                            val newDataKey = "NGO_Finder_Data_$newKeyNumber"

                            val ngoData = arrayListOf(
                                ngoName,
                                ngoAddress,
                                ngoRegID,
                                ngoPhoneNo,
                                ngoEmail,
                                ngoType,
                                ngoUniqueID,
                                ngoImage,
                                ngoSectors,
                                ngoSiteLink
                            )

                            dataToVerification.child(newDataKey).setValue(ngoData)
                                .addOnSuccessListener {
                                    clearInput()
                                    Toast.makeText(
                                        this@EnterNGOData,
                                        "Submission received! It will be published upon admin verification.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this@EnterNGOData,
                                        "Failed to save data. Please try again.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@EnterNGOData, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@EnterNGOData, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun clearInput() {
        bindingEnterNGOData.uploadNGOName.text?.clear()
        bindingEnterNGOData.uploadNGOAddress.text?.clear()
        bindingEnterNGOData.uploadNGORegID.text?.clear()
        bindingEnterNGOData.uploadNGOPhoneNo.text?.clear()
        bindingEnterNGOData.uploadNGOEmail.text?.clear()
        bindingEnterNGOData.uploadNGOType.text?.clear()
        bindingEnterNGOData.uploadNGOUniqueID.text?.clear()
        bindingEnterNGOData.uploadNGOLogoImage.text?.clear()
        bindingEnterNGOData.uploadNGOSectors.text?.clear()
        bindingEnterNGOData.uploadNGOSiteLink.text?.clear()
    }
}
