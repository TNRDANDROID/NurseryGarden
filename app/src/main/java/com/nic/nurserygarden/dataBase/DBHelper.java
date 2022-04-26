package com.nic.nurserygarden.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NurseryGarden";
    private static final int DATABASE_VERSION = 1;


    public static final String VILLAGE_TABLE_NAME = " villageTable";
    public static final String HABITATION_TABLE_NAME = " habitaionTable";
    public static final String PMAY_LIST_TABLE_NAME = "PMAYList";
    public static final String SAVE_PMAY_DETAILS = "SavePMAYDetails";
    public static final String SAVE_PMAY_IMAGES = "SavePMAYImages";

    ///New Table
    public static final String MASTER_FIN_YEAR_TABLE = "fin_year";
    public static final String MASTER_WORK_TYPE_TABLE = "work_type";
    public static final String MASTER_SELF_HELP_GROUP_TABLE = "self_help_group";
    public static final String MASTER_SELF_HELP_GROUP_MEMBER_TABLE = "self_help_group_member";
    public static final String SAVE_TREE_IMAGE_TABLE = "save_tree_image_table";
    public static final String SAVE_BEFORE_TREE_IMAGE_TABLE = "save_before_tree_image_table";
    public static final String SAVE_AFTER_TREE_IMAGE_TABLE = "save_after_tree_image_table";

    ///Nursery Tables
    public static final String NURSERY_USER_DETAILS = "nursery_user_details";
    public static final String NURSERY_LAND_TYPE = "nursery_land_type";
    public static final String NURSERY_LAND_SAVE_DETAILS = "nursery_land_save_details";
    public static final String BATCH_DETAILS = "batch_details";
    public static final String BATCH_IMAGES_DETAILS = "batch_images_details";
    public static final String BATCH_SPECIES_DETAILS = "batch_species_details";

    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    //creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + VILLAGE_TABLE_NAME + " ("
                + "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "pvname TEXT)");

        db.execSQL("CREATE TABLE " + HABITATION_TABLE_NAME + " ("
                + "dcode TEXT," +
                "bcode TEXT," +
                "pvcode TEXT," +
                "habitation_code TEXT," +
                "habitation_name TEXT)");

        db.execSQL("CREATE TABLE " + PMAY_LIST_TABLE_NAME + " ("
                + "pvcode  TEXT," +
                "habcode  TEXT," +
                "beneficiary_name  TEXT," +
                "secc_id  TEXT," +
                "habitation_name TEXT," +
                "person_alive TEXT," +
                "legal_heir_available TEXT," +
                "person_migrated TEXT," +
                "pvname TEXT)");

        db.execSQL("CREATE TABLE " + SAVE_PMAY_DETAILS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "dcode TEXT," +
                "bcode TEXT," +
                "pvcode TEXT," +
                "habcode TEXT," +
                "pvname TEXT," +
                "habitation_name TEXT," +
                "secc_id TEXT," +
                "beneficiary_name TEXT," +
                "person_alive TEXT," +
                "legal_heir_available TEXT," +
                "person_migrated TEXT," +
                "button_text TEXT," +
                "beneficiary_father_name TEXT)");


        db.execSQL("CREATE TABLE " + SAVE_PMAY_IMAGES + " ("
                + "pmay_id INTEGER,"+
                "image BLOB," +
                "latitude TEXT," +
                "longitude TEXT," +
                "type_of_photo TEXT)");

        ///new
        db.execSQL("CREATE TABLE " + MASTER_FIN_YEAR_TABLE + " ("
                +"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "fin_year TEXT)");
        db.execSQL("CREATE TABLE " + MASTER_WORK_TYPE_TABLE + " ("
                +"work_code INTEGER,"+
                "work_name TEXT)");
        db.execSQL("CREATE TABLE " + MASTER_SELF_HELP_GROUP_TABLE + " ("
                +"shg_code INTEGER,"+
                "shg_name TEXT)");
        db.execSQL("CREATE TABLE " + MASTER_SELF_HELP_GROUP_MEMBER_TABLE + " ("
                +"shg_code INTEGER,"+
                "shg_member_code INTEGER,"+
                "member_name TEXT)");

        db.execSQL("CREATE TABLE " + SAVE_TREE_IMAGE_TABLE + " ("
                +"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "shg_code INTEGER,"+
                "shg_member_code INTEGER,"+
                "work_code INTEGER,"+
                "shg_name TEXT," +
                "member_name TEXT," +
                "work_name TEXT," +
                "before_photo BLOB," +
                "before_photo_lat TEXT," +
                "before_photo_long TEXT," +
                "after_photo BLOB," +
                "after_photo_lat TEXT," +
                "after_photo_long TEXT)");

        db.execSQL("CREATE TABLE " + SAVE_BEFORE_TREE_IMAGE_TABLE + " ("
                +"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "shg_code INTEGER,"+
                "shg_member_code INTEGER,"+
                "work_code INTEGER,"+
                "shg_name TEXT," +
                "fin_year TEXT," +
                "member_name TEXT," +
                "work_name TEXT," +
                "before_photo BLOB," +
                "before_photo_lat TEXT," +
                "before_photo_long TEXT)");
        db.execSQL("CREATE TABLE " + SAVE_AFTER_TREE_IMAGE_TABLE + " ("
                +"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "shg_code INTEGER,"+
                "shg_member_code INTEGER,"+
                "work_code INTEGER,"+
                "shg_name TEXT," +
                "fin_year TEXT," +
                "member_name TEXT," +
                "work_name TEXT," +
                "after_photo BLOB," +
                "after_photo_lat TEXT," +
                "after_photo_long TEXT)");


        /////Nursery Tables
        db.execSQL("CREATE TABLE " + NURSERY_USER_DETAILS + " ("
                +"nursery_id INTEGER,"+
                "nursery_level_id INTEGER,"+
                "nursery_name_en TEXT," +
                "nursery_name_ta TEXT," +
                "nursery_address TEXT," +
                "dname TEXT," +
                "bname TEXT," +
                "pvname TEXT)");

        db.execSQL("CREATE TABLE " + NURSERY_LAND_TYPE + " ("
                +"land_type_id INTEGER,"+
                "land_type_name_en TEXT,"+
                "land_type_name_ta TEXT)");

        db.execSQL("CREATE TABLE " + NURSERY_LAND_SAVE_DETAILS + " ("
                +"land_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "land_type_id INTEGER,"+
                "image BLOB,"+
                "server_flag TEXT,"+
                "latitude TEXT,"+
                "longtitude TEXT)");

        db.execSQL("CREATE TABLE " + BATCH_DETAILS + " ("
                +"batch_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "batch_id INTEGER,"+
                "batch_number INTEGER,"+
                "created_date TEXT,"+
                "server_flag TEXT)");

        db.execSQL("CREATE TABLE " + BATCH_IMAGES_DETAILS + " ("
                +"batch_images_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "batch_primary_id INTEGER,"+
                "batch_id INTEGER,"+
                "image BLOB,"+
                "lattitude TEXT,"+
                "longtitude TEXT)");

        db.execSQL("CREATE TABLE " + BATCH_SPECIES_DETAILS + " ("
                +"batch_species_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "batch_primary_id INTEGER,"+
                "batch_id INTEGER,"+
                "species_id TEXT,"+
                "no_of_count TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + VILLAGE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + PMAY_LIST_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + HABITATION_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_PMAY_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_PMAY_IMAGES);

            //New Tables
            db.execSQL("DROP TABLE IF EXISTS " + MASTER_FIN_YEAR_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + MASTER_WORK_TYPE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + MASTER_SELF_HELP_GROUP_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + MASTER_SELF_HELP_GROUP_MEMBER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_TREE_IMAGE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_BEFORE_TREE_IMAGE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_AFTER_TREE_IMAGE_TABLE);
            onCreate(db);
        }
    }


}
