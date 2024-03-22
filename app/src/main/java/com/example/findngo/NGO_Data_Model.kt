package com.example.findngo

class NGO_Data_Model {
    var name: String = ""
    var address: String = ""
    var reg_id: String = ""
    var phone_no: String = ""
    var email: String = ""
    var type: String = ""
    var unique_id: String = ""
    var logo_image: String = ""
    var sector: String = ""
    var site_link: String = ""

    override fun toString(): String {
        return "NGO_Data_Model(name='$name', address='$address', reg_id='$reg_id', phone_no='$phone_no', email='$email', type='$type', unique_id='$unique_id', logo_image='$logo_image', sector='$sector', site_link='$site_link')"
    }

}
