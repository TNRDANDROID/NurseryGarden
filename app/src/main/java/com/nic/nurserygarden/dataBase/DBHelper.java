package com.nic.nurserygarden.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "New_Nursery_Garden";
    private static final int DATABASE_VERSION = 2;


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
    public static final String NURSERY_SPECIES_TYPE = "nursery_species_types";
    public static final String NURSERY_WATER_SOURCE_TYPE = "nursery_water_source_types";
    public static final String NURSERY_FENCING_TYPE = "nursery_fencing_types";

    public static final String NURSERY_LAND_SAVE_DETAILS = "nursery_land_save_details";

    public static final String BATCH_DETAILS = "batch_details";
    public static final String BATCH_IMAGES_DETAILS = "batch_images_details";
    public static final String BATCH_SPECIES_DETAILS = "batch_species_details";

    public static final String BATCH_GROWTH_TRACKING_DETAILS = "batch_growth_tracking_details";
    public static final String BATCH_GROWTH_TRACKING_DATES = "batch_growth_tracking_dates";
    public static final String BATCH_GROWTH_TRACKING_PHOTOS_DETAILS = "batch_growth_tracking_photos_details";
    public static final String BATCH_GROWTH_TRACKING_SPECIES_DETAILS = "batch_growth_tracking_species_details";

    public static final String DEAD_STAGE = "dead_stage";
    public static final String DEAD_SAPLING_DETAILS = " dead_sapling_details";
    public static final String DEAD_SAPLING_DETAILS_SAVE = "dead_sapling_details_save";
    public static final String DEAD_SAPLING_DETAILS_NEW_SAVE = "dead_sapling_details_new_save";
    public static final String NURSERY_EXPENDITURE_TYPE = "nursery_expenditure_type";
    public static final String NURSERY_EXPENDITURE_UNIT = "nursery_expenditure_unit";
    public static final String NURSERY_EXPENDITURE_FOUND_SRC = "nursery_expenditure_found_src";
    public static final String NURSERY_EXPENDITURE_SAVE = "nursery_expenditure_save";

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
                "pia_member_count TEXT," +
                "pia_type_name_en TEXT," +
                "pia_type_name_ta TEXT," +
                "pia_name_en TEXT," +
                "pia_name_ta TEXT," +
                "pvname TEXT)");

        db.execSQL("CREATE TABLE " + NURSERY_LAND_TYPE + " ("
                +"land_type_id INTEGER,"+
                "land_type_name_en TEXT,"+
                "is_others TEXT,"+
                "land_type_name_ta TEXT)");

        db.execSQL("CREATE TABLE " + NURSERY_WATER_SOURCE_TYPE + " ("
                +"water_source_type_id TEXT," +
                 "is_others TEXT," +
                "water_source_type_name TEXT)");

        db.execSQL("CREATE TABLE " + NURSERY_FENCING_TYPE + " ("
                +"fencing_type_id TEXT,"+
                 "is_others TEXT,"+
                "fencing_type_name TEXT)");

        db.execSQL("CREATE TABLE " + NURSERY_SPECIES_TYPE + " ("
                +"species_type_id INTEGER,"+
                "species_name_en TEXT,"+
                "species_name_ta TEXT)");

        db.execSQL("CREATE TABLE " + DEAD_STAGE + " ("
                +"dead_stage_id INTEGER,"+
                "dead_stage_name_en TEXT,"+
                "dead_stage_name_ta TEXT)");

        db.execSQL("CREATE TABLE " + NURSERY_LAND_SAVE_DETAILS + " ("
                +"land_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "land_type_id INTEGER,"+
                "nursery_land_id INTEGER,"+
                "nursery_id INTEGER,"+
                "image BLOB,"+
                "server_flag TEXT,"+
                "land_address TEXT,"+
                "other_land_type TEXT,"+
                "water_source_type_id TEXT,"+
                "water_source_type_name TEXT,"+
                "fencing_type_id TEXT,"+
                "fencing_type_name TEXT,"+
                "other_fencing_type_name TEXT,"+
                "other_water_source_type_name TEXT,"+
                "area TEXT,"+
                "survey_number TEXT,"+
                "sub_div_no TEXT,"+
                "land_type_name_en TEXT,"+
                "land_type_name_ta TEXT,"+
                "latitude TEXT,"+
                "longtitude TEXT)");

        db.execSQL("CREATE TABLE " + BATCH_DETAILS + " ("
                +"batch_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "batch_id INTEGER,"+
                "batch_number INTEGER,"+
                "created_date TEXT,"+
                "is_batch_closed TEXT,"+
                "server_flag TEXT)");

        db.execSQL("CREATE TABLE " + BATCH_IMAGES_DETAILS + " ("
                +"batch_images_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "batch_primary_id INTEGER,"+
                "batch_id INTEGER,"+
                "image BLOB,"+
                "server_flag TEXT,"+
                "lattitude TEXT,"+
                "longtitude TEXT)");

        db.execSQL("CREATE TABLE " + BATCH_SPECIES_DETAILS + " ("
                +"batch_species_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "batch_primary_id INTEGER,"+
                "batch_id INTEGER,"+
                "batch_species_id INTEGER,"+
                "species_type_id INTEGER,"+
                "server_flag TEXT,"+
                "is_harvest_closed TEXT,"+
                "species_type_name_en TEXT,"+
                "species_type_name_ta TEXT,"+
                "no_of_count INTEGER)");

        db.execSQL("CREATE TABLE " + BATCH_GROWTH_TRACKING_DATES + " ("
                +"growth_tracking_date_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "batch_id INTEGER,"+
                "entry_date TEXT)");

        db.execSQL("CREATE TABLE " + BATCH_GROWTH_TRACKING_DETAILS + " ("
                +"batch_growth_tracking_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "growth_tracking_id INTEGER,"+
                "batch_primary_id INTEGER,"+
                "nursery_id INTEGER,"+
                "batch_id INTEGER,"+
                "entry_date TEXT,"+
                "server_flag TEXT)");

        db.execSQL("CREATE TABLE " + BATCH_GROWTH_TRACKING_PHOTOS_DETAILS + " ("
                +"batch_growth_tracking_images_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "batch_growth_tracking_primary_id INTEGER,"+
                "batch_primary_id INTEGER,"+
                "batch_id INTEGER,"+
                "image BLOB,"+
                "server_flag TEXT,"+
                "entry_date TEXT,"+
                "lattitude TEXT,"+
                "longtitude TEXT)");

        db.execSQL("CREATE TABLE " + BATCH_GROWTH_TRACKING_SPECIES_DETAILS + " ("
                +"batch_growth_tracking_species_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "batch_growth_tracking_primary_id INTEGER,"+
                "batch_primary_id INTEGER,"+
                "batch_id INTEGER,"+
                "growth_tracking_details_id INTEGER,"+
                "batch_species_id INTEGER,"+
                "species_type_id INTEGER,"+
                "server_flag TEXT,"+
                "entry_date TEXT,"+
                "no_of_saplings INTEGER,"+
                "height_in_cm TEXT,"+
                "age_in_days INTEGER)");

        db.execSQL("CREATE TABLE " + DEAD_SAPLING_DETAILS_SAVE + " ("
                +"dead_saplings_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "batch_primary_id INTEGER,"+
                "batch_id INTEGER,"+
                "batch_species_id INTEGER,"+
                "species_type_id INTEGER,"+
                "server_flag TEXT,"+
                "dead_stage TEXT,"+
                "no_of_dead_sapling INTEGER,"+
                "dead_reason TEXT)");


        db.execSQL("CREATE TABLE " + DEAD_SAPLING_DETAILS_NEW_SAVE + " ("
                +"dead_saplings_new_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "batch_id INTEGER,"+
                "batch_no INTEGER,"+
                "species_type_id INTEGER,"+
                "batch_species_id INTEGER,"+
                "growth_tracking_id INTEGER,"+
                "growth_tracking_details_id INTEGER,"+
                "sapling_height_in_cm TEXT,"+
                "sapling_age_in_days INTEGER,"+
                "no_of_dead_sapling INTEGER)");

        db.execSQL("CREATE TABLE " + DEAD_SAPLING_DETAILS + " ("
                +"dead_saplings_details_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nursery_id INTEGER,"+
                "batch_id INTEGER,"+
                "batch_species_id INTEGER,"+
                "growth_tracking_id INTEGER,"+
                "growth_tracking_details_id INTEGER,"+
                "batch_no INTEGER,"+
                "species_type_id INTEGR,"+
                "species_name_en TEXT,"+
                "species_name_ta TEXT,"+
                "sapling_height_in_cm TEXT,"+
                "dead_reason_text TEXT,"+
                "dead_typed_count_text TEXT,"+
                "sapling_age_in_days INTEGER,"+
                "dead_stage_position INTEGER,"+
                "no_of_saplings INTEGER)");
        db.execSQL("CREATE TABLE " + NURSERY_EXPENDITURE_TYPE + " ("
                +"expenditure_type_id INTEGER,"+
                "expense_category_id INTEGER,"+
                "expense_category_en TEXT,"+
                "expense_category_ta TEXT,"+
                "is_others TEXT)");
        db.execSQL("CREATE TABLE " + NURSERY_EXPENDITURE_FOUND_SRC + " ("
                +"expenditure_fund_src_id INTEGER,"+
                "expense_category_id INTEGER,"+
                "expenditure_fund_src_en TEXT,"+
                "expenditure_fund_src_ta TEXT)");
        db.execSQL("CREATE TABLE " + NURSERY_EXPENDITURE_UNIT + " ("
                +"expenditure_unit_id INTEGER,"+
                "expense_category_id INTEGER,"+
                "expenditure_unit_en TEXT,"+
                "expenditure_unit_ta TEXT)");

        db.execSQL("CREATE TABLE " + NURSERY_EXPENDITURE_SAVE + " ("
                +"expenditure_primary_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "fin_year TEXT,"+
                "entry_date TEXT,"+
                "expenditure_type_id INTEGER,"+
                "expense_category_id INTEGER,"+
                "expenditure_unit_id INTEGER,"+
                "expenditure_fund_src_id INTEGER,"+
                "expense_category_name TEXT,"+
                "expense_unit_name TEXT,"+
                "expense_found_src_name TEXT,"+
                "expense_category_others TEXT,"+
                "expenditure_unit_others TEXT,"+
                "expenditure_fund_src_others TEXT,"+
                "quantity TEXT,"+
                "amount TEXT)");


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


            db.execSQL("DROP TABLE IF EXISTS " + NURSERY_USER_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + NURSERY_LAND_TYPE);
            db.execSQL("DROP TABLE IF EXISTS " + NURSERY_SPECIES_TYPE);
            db.execSQL("DROP TABLE IF EXISTS " + NURSERY_LAND_SAVE_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + BATCH_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + BATCH_IMAGES_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + BATCH_SPECIES_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + BATCH_GROWTH_TRACKING_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + BATCH_GROWTH_TRACKING_DATES);
            db.execSQL("DROP TABLE IF EXISTS " + BATCH_GROWTH_TRACKING_PHOTOS_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + BATCH_GROWTH_TRACKING_SPECIES_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + DEAD_STAGE);
            db.execSQL("DROP TABLE IF EXISTS " + DEAD_SAPLING_DETAILS_SAVE);
            db.execSQL("DROP TABLE IF EXISTS " + DEAD_SAPLING_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + DEAD_SAPLING_DETAILS_NEW_SAVE);

            db.execSQL("DROP TABLE IF EXISTS " + NURSERY_EXPENDITURE_TYPE);
            db.execSQL("DROP TABLE IF EXISTS " + NURSERY_EXPENDITURE_UNIT);
            db.execSQL("DROP TABLE IF EXISTS " + NURSERY_EXPENDITURE_FOUND_SRC);
            db.execSQL("DROP TABLE IF EXISTS " + NURSERY_EXPENDITURE_SAVE);

            db.execSQL("DROP TABLE IF EXISTS " + NURSERY_WATER_SOURCE_TYPE);
            db.execSQL("DROP TABLE IF EXISTS " + NURSERY_FENCING_TYPE);
            onCreate(db);
        }
    }


}
