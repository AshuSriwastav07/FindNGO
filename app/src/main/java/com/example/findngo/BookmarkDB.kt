import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.findngo.NGO_Data_Model

class BookmarkDB(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "BookmarkDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Bookmarks"
        private const val KEY_ID = "id"

        private const val KEY_NAME = "name"  //0
        private const val KEY_ADD = "address" //1
        private const val KEY_REG_ID = "reg_id" //2
        private const val KEY_PHONE_NO = "Phone_NO" //3
        private const val KEY_EMAIL = "email" //4
        private const val KEY_TYPE = "ngo_type" //5
        private const val KEY_UNIQUE_ID = "unique_id" //6
        private const val KEY_LOGO_LINK = "ngo_logo" //7
        private const val KEY_SECTOR = "sector" //8
        private const val KEY_SITE_LINK = "site_link" //9

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME (" +
                "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +

                "$KEY_NAME UNIQUE, TEXT, " +
                "$KEY_ADD TEXT, " +
                "$KEY_REG_ID TEXT, " +
                "$KEY_PHONE_NO TEXT, " +
                "$KEY_EMAIL TEXT, " +
                "$KEY_TYPE TEXT, " +
                "$KEY_UNIQUE_ID TEXT, " +
                "$KEY_LOGO_LINK TEXT, " +
                "$KEY_SECTOR TEXT, " +
                "$KEY_SITE_LINK TEXT" +

                ")")

        Log.d("SQLDatabase", "DATABASE On CREATED")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        Log.d("SQLDatabase", "DATABASE OnUpgrade")
        onCreate(db)
    }

    fun addBookmark(
        NGO_Name: String,
        NGO_Address: String,
        NGO_Reg_ID: String,
        NGO_Phone_No: String,
        NGO_Email: String,
        NGO_Type: String,
        NGO_UniqueID: String,
        NGO_Logo: String,
        NGO_Sector: String,
        NGO_Site_Link: String,
    ) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(KEY_NAME, NGO_Name)
        values.put(KEY_ADD, NGO_Address)
        values.put(KEY_REG_ID, NGO_Reg_ID)
        values.put(KEY_PHONE_NO, NGO_Phone_No)
        values.put(KEY_EMAIL, NGO_Email)
        values.put(KEY_TYPE, NGO_Type)
        values.put(KEY_UNIQUE_ID, NGO_UniqueID)
        values.put(KEY_LOGO_LINK, NGO_Logo)
        values.put(KEY_SECTOR, NGO_Sector)
        values.put(KEY_SITE_LINK, NGO_Site_Link)

        db.insert(TABLE_NAME, null, values)
        Log.d("SQLDatabase", "DATABASE CREATED")
        db.close()
    }

    @SuppressLint("Range")
    fun getNGO_Data(): ArrayList<NGO_Data_Model> {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val data = ArrayList<NGO_Data_Model>()

        while (cursor.moveToNext()) {
            val model = NGO_Data_Model()

            // Check if each value retrieved from the cursor is not null before assigning it
            model.name = cursor.getString(1) ?: ""
            model.address = cursor.getString(cursor.getColumnIndex(KEY_ADD)) ?: ""
            model.reg_id = cursor.getString(cursor.getColumnIndex(KEY_REG_ID)) ?: ""
            model.phone_no = cursor.getString(cursor.getColumnIndex(KEY_PHONE_NO)) ?: ""
            model.email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL)) ?: ""
            model.type = cursor.getString(cursor.getColumnIndex(KEY_TYPE)) ?: ""
            model.unique_id = cursor.getString(cursor.getColumnIndex(KEY_UNIQUE_ID)) ?: ""
            model.logo_image = cursor.getString(cursor.getColumnIndex(KEY_LOGO_LINK)) ?: ""
            model.sector = cursor.getString(cursor.getColumnIndex(KEY_SECTOR)) ?: ""
            model.site_link = cursor.getString(cursor.getColumnIndex(KEY_SITE_LINK)) ?: ""

            data.add(model)
        }

        cursor.close()
        db.close()

        return data
    }


}
