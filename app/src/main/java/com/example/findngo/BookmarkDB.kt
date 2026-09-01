import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.findngo.NGOItem
import com.example.findngo.NGO_Data_Model

class BookmarkDB(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "BookmarkDB"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "Bookmarks"
        private const val KEY_ID = "id"

        private const val KEY_NAME = "name"
        private const val KEY_ADD = "address"
        private const val KEY_REG_ID = "reg_id"
        private const val KEY_PHONE_NO = "Phone_NO"
        private const val KEY_EMAIL = "email"
        private const val KEY_TYPE = "ngo_type"
        private const val KEY_UNIQUE_ID = "unique_id"
        private const val KEY_LOGO_LINK = "ngo_logo"
        private const val KEY_SECTOR = "sector"
        private const val KEY_SITE_LINK = "site_link"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$KEY_NAME TEXT UNIQUE," +
                    "$KEY_ADD TEXT," +
                    "$KEY_REG_ID TEXT," +
                    "$KEY_PHONE_NO TEXT," +
                    "$KEY_EMAIL TEXT," +
                    "$KEY_TYPE TEXT," +
                    "$KEY_UNIQUE_ID TEXT," +
                    "$KEY_LOGO_LINK TEXT," +
                    "$KEY_SECTOR TEXT," +
                    "$KEY_SITE_LINK TEXT" +
                    ")"
        )
        Log.d("BookmarkDB", "Database table created successfully")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addBookmark(
        ngoName: String,
        ngoAddress: String,
        ngoRegId: String,
        ngoPhoneNo: String,
        ngoEmail: String,
        ngoType: String,
        ngoUniqueId: String,
        ngoLogo: String,
        ngoSector: String,
        ngoSiteLink: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, ngoName)
            put(KEY_ADD, ngoAddress)
            put(KEY_REG_ID, ngoRegId)
            put(KEY_PHONE_NO, ngoPhoneNo)
            put(KEY_EMAIL, ngoEmail)
            put(KEY_TYPE, ngoType)
            put(KEY_UNIQUE_ID, ngoUniqueId)
            put(KEY_LOGO_LINK, ngoLogo)
            put(KEY_SECTOR, ngoSector)
            put(KEY_SITE_LINK, ngoSiteLink)
        }

        val result = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
        return result
    }

    fun addBookmarkItem(item: NGOItem): Long {
        return addBookmark(
            item.name,
            item.address,
            item.regId,
            item.phoneNo,
            item.email,
            item.ngoType,
            item.uniqueId,
            item.logoImage,
            item.sector,
            item.siteLink
        )
    }

    fun isBookmarked(name: String): Boolean {
        if (name.isBlank()) return false
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM $TABLE_NAME WHERE $KEY_NAME = ? LIMIT 1", arrayOf(name))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun removeBookmark(name: String): Int {
        val db = this.writableDatabase
        val deletedRows = db.delete(TABLE_NAME, "$KEY_NAME = ?", arrayOf(name))
        db.close()
        return deletedRows
    }

    fun getAllBookmarks(): List<NGOItem> {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $KEY_ID DESC", null)
        val items = ArrayList<NGOItem>()

        cursor.use {
            if (it.moveToFirst()) {
                val nameIdx = it.getColumnIndex(KEY_NAME)
                val addIdx = it.getColumnIndex(KEY_ADD)
                val regIdx = it.getColumnIndex(KEY_REG_ID)
                val phoneIdx = it.getColumnIndex(KEY_PHONE_NO)
                val emailIdx = it.getColumnIndex(KEY_EMAIL)
                val typeIdx = it.getColumnIndex(KEY_TYPE)
                val uniqueIdx = it.getColumnIndex(KEY_UNIQUE_ID)
                val logoIdx = it.getColumnIndex(KEY_LOGO_LINK)
                val sectorIdx = it.getColumnIndex(KEY_SECTOR)
                val siteIdx = it.getColumnIndex(KEY_SITE_LINK)

                do {
                    items.add(
                        NGOItem(
                            name = if (nameIdx >= 0) it.getString(nameIdx) ?: "" else "",
                            address = if (addIdx >= 0) it.getString(addIdx) ?: "" else "",
                            regId = if (regIdx >= 0) it.getString(regIdx) ?: "" else "",
                            phoneNo = if (phoneIdx >= 0) it.getString(phoneIdx) ?: "" else "",
                            email = if (emailIdx >= 0) it.getString(emailIdx) ?: "" else "",
                            ngoType = if (typeIdx >= 0) it.getString(typeIdx) ?: "" else "",
                            uniqueId = if (uniqueIdx >= 0) it.getString(uniqueIdx) ?: "" else "",
                            logoImage = if (logoIdx >= 0) it.getString(logoIdx) ?: "" else "",
                            sector = if (sectorIdx >= 0) it.getString(sectorIdx) ?: "" else "",
                            siteLink = if (siteIdx >= 0) it.getString(siteIdx) ?: "" else ""
                        )
                    )
                } while (it.moveToNext())
            }
        }
        db.close()
        return items
    }

    @SuppressLint("Range")
    fun getNGO_Data(): ArrayList<NGO_Data_Model> {
        val items = getAllBookmarks()
        val legacyList = ArrayList<NGO_Data_Model>()
        for (item in items) {
            val model = NGO_Data_Model().apply {
                name = item.name
                address = item.address
                reg_id = item.regId
                phone_no = item.phoneNo
                email = item.email
                type = item.ngoType
                unique_id = item.uniqueId
                logo_image = item.logoImage
                sector = item.sector
                site_link = item.siteLink
            }
            legacyList.add(model)
        }
        return legacyList
    }
}
