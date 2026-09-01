package com.example.findngo

import java.io.Serializable

data class DonationItem(
    val id: String = "",
    val name: String = "",
    val details: String = "",
    val logoImage: String = "",
    val donationPageLink: String = "",
    val siteLink: String = ""
) : Serializable {

    companion object {
        fun fromSnapshotValue(key: String, value: Any?): DonationItem? {
            if (value is List<*>) {
                return DonationItem(
                    id = key,
                    name = value.getOrNull(0)?.toString() ?: "",
                    details = value.getOrNull(1)?.toString() ?: "",
                    logoImage = value.getOrNull(2)?.toString() ?: "",
                    donationPageLink = value.getOrNull(3)?.toString() ?: "",
                    siteLink = value.getOrNull(4)?.toString() ?: ""
                )
            } else if (value is Map<*, *>) {
                return DonationItem(
                    id = key,
                    name = value["name"]?.toString() ?: "",
                    details = (value["details"] ?: value["fundUse"])?.toString() ?: "",
                    logoImage = value["logoImage"]?.toString() ?: "",
                    donationPageLink = value["donationPageLink"]?.toString() ?: "",
                    siteLink = value["siteLink"]?.toString() ?: ""
                )
            }
            return null
        }
    }
}
