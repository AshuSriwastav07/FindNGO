package com.example.findngo

import java.io.Serializable

data class NGOItem(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val regId: String = "",
    val phoneNo: String = "",
    val email: String = "",
    val ngoType: String = "",
    val uniqueId: String = "",
    val logoImage: String = "",
    val sector: String = "",
    val siteLink: String = ""
) : Serializable {

    fun toArrayList(): ArrayList<String> {
        return arrayListOf(
            name,
            address,
            regId,
            phoneNo,
            email,
            ngoType,
            uniqueId,
            logoImage,
            sector,
            siteLink
        )
    }

    companion object {
        fun fromList(list: List<String>): NGOItem {
            return NGOItem(
                name = list.getOrElse(0) { "" },
                address = list.getOrElse(1) { "" },
                regId = list.getOrElse(2) { "" },
                phoneNo = list.getOrElse(3) { "" },
                email = list.getOrElse(4) { "" },
                ngoType = list.getOrElse(5) { "" },
                uniqueId = list.getOrElse(6) { "" },
                logoImage = list.getOrElse(7) { "" },
                sector = list.getOrElse(8) { "" },
                siteLink = list.getOrElse(9) { "" }
            )
        }

        fun fromSnapshotValue(key: String, value: Any?): NGOItem? {
            if (value is List<*>) {
                return NGOItem(
                    id = key,
                    name = value.getOrNull(0)?.toString() ?: "",
                    address = value.getOrNull(1)?.toString() ?: "",
                    regId = value.getOrNull(2)?.toString() ?: "",
                    phoneNo = value.getOrNull(3)?.toString() ?: "",
                    email = value.getOrNull(4)?.toString() ?: "",
                    ngoType = value.getOrNull(5)?.toString() ?: "",
                    uniqueId = value.getOrNull(6)?.toString() ?: "",
                    logoImage = value.getOrNull(7)?.toString() ?: "",
                    sector = value.getOrNull(8)?.toString() ?: "",
                    siteLink = value.getOrNull(9)?.toString() ?: ""
                )
            } else if (value is Map<*, *>) {
                return NGOItem(
                    id = key,
                    name = (value["name"] ?: value["NGO_Name"])?.toString() ?: "",
                    address = (value["address"] ?: value["NGO_Address"])?.toString() ?: "",
                    regId = (value["regId"] ?: value["reg_id"] ?: value["NGO_Reg_ID"])?.toString() ?: "",
                    phoneNo = (value["phoneNo"] ?: value["phone_no"] ?: value["NGO_Phone_No"])?.toString() ?: "",
                    email = (value["email"] ?: value["NGO_Email"])?.toString() ?: "",
                    ngoType = (value["ngoType"] ?: value["type"] ?: value["NGO_Type"])?.toString() ?: "",
                    uniqueId = (value["uniqueId"] ?: value["unique_id"] ?: value["NGO_UniqueID"])?.toString() ?: "",
                    logoImage = (value["logoImage"] ?: value["logo_image"] ?: value["NGO_Logo"])?.toString() ?: "",
                    sector = (value["sector"] ?: value["NGO_Sector"])?.toString() ?: "",
                    siteLink = (value["siteLink"] ?: value["site_link"] ?: value["NGO_Site_Link"])?.toString() ?: ""
                )
            }
            return null
        }
    }
}
