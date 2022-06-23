package com.nic.nurserygarden.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.model.NurserySurvey;

import java.util.ArrayList;


public class dbData {
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Context context;

    public dbData(Context context){
        this.dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if(dbHelper != null) {
            dbHelper.close();
        }
    }

    /****** DISTRICT TABLE *****/


    /****** VILLAGE TABLE *****/
    public NurserySurvey insertVillage(NurserySurvey pmgsySurvey) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, pmgsySurvey.getDistictCode());
        values.put(AppConstant.BLOCK_CODE, pmgsySurvey.getBlockCode());
        values.put(AppConstant.PV_CODE, pmgsySurvey.getPvCode());
        values.put(AppConstant.PV_NAME, pmgsySurvey.getPvName());

        long id = db.insert(DBHelper.VILLAGE_TABLE_NAME,null,values);
        Log.d("Inserted_id_village", String.valueOf(id));

        return pmgsySurvey;
    }
    public ArrayList<NurserySurvey> getAll_Village(String dcode, String bcode) {

        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.VILLAGE_TABLE_NAME+" where dcode = "+dcode+" and bcode = "+bcode+" order by pvname asc",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setPvName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public NurserySurvey insertHabitation(NurserySurvey pmgsySurvey) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, pmgsySurvey.getDistictCode());
        values.put(AppConstant.BLOCK_CODE, pmgsySurvey.getBlockCode());
        values.put(AppConstant.PV_CODE, pmgsySurvey.getPvCode());
        values.put(AppConstant.HABB_CODE, pmgsySurvey.getHabCode());
        values.put(AppConstant.HABITATION_NAME, pmgsySurvey.getHabitationName());

        long id = db.insert(DBHelper.HABITATION_TABLE_NAME,null,values);
        Log.d("Inserted_id_habitation", String.valueOf(id));

        return pmgsySurvey;
    }
    public ArrayList<NurserySurvey> getAll_Habitation(String dcode, String bcode) {

        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.HABITATION_TABLE_NAME+" where dcode = "+dcode+" and bcode = "+bcode+" order by habitation_name asc",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setHabCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.HABB_CODE)));
                    card.setHabitationName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.HABITATION_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public NurserySurvey insertPMAY(NurserySurvey pmgsySurvey) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.PV_CODE, pmgsySurvey.getPvCode());
        values.put(AppConstant.HAB_CODE, pmgsySurvey.getHabCode());
        values.put(AppConstant.BENEFICIARY_NAME, pmgsySurvey.getBeneficiaryName());
        values.put(AppConstant.SECC_ID, pmgsySurvey.getSeccId());
        values.put(AppConstant.HABITATION_NAME, pmgsySurvey.getHabitationName());
        values.put(AppConstant.PV_NAME, pmgsySurvey.getPvName());
        values.put(AppConstant.PERSON_ALIVE, pmgsySurvey.getPersonAlive());
        values.put(AppConstant.LEGAL_HEIR_AVAILABLE, pmgsySurvey.getIsLegel());
        values.put(AppConstant.PERSON_MIGRATED, pmgsySurvey.getIsMigrated());

        long id = db.insert(DBHelper.PMAY_LIST_TABLE_NAME,null,values);
        Log.d("Inserted_id_PMAY_LIST", String.valueOf(id));

        return pmgsySurvey;
    }

    public ArrayList<NurserySurvey> getAll_PMAYList(String pvcode, String habcode) {

        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        String condition = "";

        if (habcode != "") {
            condition = " where pvcode = '" + pvcode+"' and habcode = '" + habcode+"'" ;
        }else {
            condition = "";
        }

        try {
            cursor = db.rawQuery("select * from "+DBHelper.PMAY_LIST_TABLE_NAME + condition,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setHabCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.HAB_CODE)));
                    card.setBeneficiaryName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BENEFICIARY_NAME)));
                    card.setSeccId(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.SECC_ID)));
                    card.setHabitationName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.HABITATION_NAME)));
                    card.setPvName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_NAME)));
                    card.setPersonAlive(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PERSON_ALIVE)));
                    card.setIsLegel(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.LEGAL_HEIR_AVAILABLE)));
                    card.setIsMigrated(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PERSON_MIGRATED)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<NurserySurvey> getSavedPMAYDetails() {

        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = null;
        String[] selectionArgs = null;


        try {
//            cursor = db.query(DBHelper.SAVE_PMAY_DETAILS,
//                    new String[]{"*"}, selection, selectionArgs, null, null, null);
            cursor = db.rawQuery("select * from "+DBHelper.SAVE_PMAY_DETAILS+" where id in (select pmay_id from "+DBHelper.SAVE_PMAY_IMAGES+")",null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    NurserySurvey card = new NurserySurvey();


                    card.setPmayId(cursor.getString(cursor
                            .getColumnIndexOrThrow("id")));
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setHabCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.HAB_CODE)));
                    card.setPvName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_NAME)));
                    card.setHabitationName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.HABITATION_NAME)));
                    card.setSeccId(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.SECC_ID)));
                    card.setBeneficiaryName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BENEFICIARY_NAME)));
                    card.setFatherName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BENEFICIARY_FATHER_NAME)));
                    card.setPersonAlive(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PERSON_ALIVE)));
                    card.setIsLegel(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.LEGAL_HEIR_AVAILABLE)));
                    card.setIsMigrated(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PERSON_MIGRATED)));
                    card.setButtonText(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BUTTON_TEXT)));


                    cards.add(card);
                }
            }
        } catch (Exception e) {
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        try {
//            cursor = db.query(DBHelper.SAVE_PMAY_DETAILS,
//                    new String[]{"*"}, selection, selectionArgs, null, null, null);
            cursor = db.rawQuery("select * from "+DBHelper.SAVE_PMAY_DETAILS+" where button_text = 'Save details'",null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    NurserySurvey card = new NurserySurvey();

                    card.setPmayId(cursor.getString(cursor
                            .getColumnIndexOrThrow("id")));
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setHabCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.HAB_CODE)));
                    card.setPvName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_NAME)));
                    card.setHabitationName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.HABITATION_NAME)));
                    card.setSeccId(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.SECC_ID)));
                    card.setBeneficiaryName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BENEFICIARY_NAME)));
                    card.setFatherName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BENEFICIARY_FATHER_NAME)));
                    card.setPersonAlive(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PERSON_ALIVE)));
                    card.setIsLegel(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.LEGAL_HEIR_AVAILABLE)));
                    card.setIsMigrated(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PERSON_MIGRATED)));
                    card.setButtonText(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BUTTON_TEXT)));


                    cards.add(card);
                }
            }
        } catch (Exception e) {
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }


    public ArrayList<NurserySurvey> getSavedPMAYImages(String pmay_id, String type_of_photo) {

        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = null;
        String[] selectionArgs = null;

        if(!type_of_photo.isEmpty()){
            selection = "pmay_id = ? and type_of_photo = ? ";
            selectionArgs = new String[]{pmay_id,type_of_photo};
        }
        else if(type_of_photo.isEmpty()) {
            selection = "pmay_id = ? ";
            selectionArgs = new String[]{pmay_id};
        }


        try {
            cursor = db.query(DBHelper.SAVE_PMAY_IMAGES,
                    new String[]{"*"}, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGE));
                    byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    NurserySurvey card = new NurserySurvey();


                    card.setPmayId(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PMAY_ID)));
                    card.setTypeOfPhoto(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.TYPE_OF_PHOTO)));
                    card.setLatitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LATITUDE)));
                    card.setLongitude(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LONGITUDE)));

                    card.setImage(decodedByte);

                    cards.add(card);
                }
            }
        } catch (Exception e) {
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }


    /////New ******************** Task****************
    public void insert_Master_Fin_Year(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("fin_year", pmgsySurvey.getFin_year());
        long id = db.insert(DBHelper.MASTER_FIN_YEAR_TABLE,null,values);
        Log.d("Inserted_id_fin_year", String.valueOf(id));
    }
    public void insert_Master_Work_Type(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("work_code", pmgsySurvey.getWork_code());
        values.put("work_name", pmgsySurvey.getWork_name());
        long id = db.insert(DBHelper.MASTER_WORK_TYPE_TABLE,null,values);
        Log.d("Inserted_id_work_type", String.valueOf(id));
    }
    public void insert_Master_Self_Help_Group(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("shg_code", pmgsySurvey.getShg_code());
        values.put("shg_name", pmgsySurvey.getShg_name());
        long id = db.insert(DBHelper.MASTER_SELF_HELP_GROUP_TABLE,null,values);
        Log.d("Inserted_id_s_g_t", String.valueOf(id));
    }
    public void insert_Master_Self_Help_Group_Member(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("shg_code", pmgsySurvey.getShg_code());
        values.put("shg_member_code", pmgsySurvey.getShg_member_code());
        values.put("member_name", pmgsySurvey.getMember_name());
        long id = db.insert(DBHelper.MASTER_SELF_HELP_GROUP_MEMBER_TABLE,null,values);
        Log.d("Inserted_id_s_g_m_t", String.valueOf(id));
    }
    public void insert_nursery_user_details(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("nursery_id", pmgsySurvey.getNursery_id());
        values.put("nursery_level_id", pmgsySurvey.getNursery_level_id());
        values.put("nursery_name_en", pmgsySurvey.getNursery_name_en());
        values.put("nursery_name_ta", pmgsySurvey.getNursery_name_ta());
        values.put("nursery_address", pmgsySurvey.getNursery_address());
        values.put("dname", pmgsySurvey.getNursery_dname());
        values.put("bname", pmgsySurvey.getNursery_bname());
        values.put("pvname", pmgsySurvey.getNursery_pvname());
        long id = db.insert(DBHelper.NURSERY_USER_DETAILS,null,values);
        Log.d("Insert_NURSERY_USER", String.valueOf(id));
    }
    public void insert_nursery_land_type(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("land_type_id", pmgsySurvey.getLand_type_id());
        values.put("land_type_name_en", pmgsySurvey.getLand_type_name_en());
        values.put("land_type_name_ta", pmgsySurvey.getLand_type_name_ta());
        long id = db.insert(DBHelper.NURSERY_LAND_TYPE,null,values);
        Log.d("Insert_nursery_land", String.valueOf(id));
    }
    public void insert_nursery_land_details(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("nursery_land_id", pmgsySurvey.getNursery_land_id());
        values.put("nursery_id", pmgsySurvey.getNursery_id());
        values.put("land_type_id", pmgsySurvey.getLand_type_id());
        values.put("land_type_name_en", pmgsySurvey.getLand_type_name_en());
        values.put("land_type_name_ta", pmgsySurvey.getLand_type_name_ta());
        values.put("image", pmgsySurvey.getImageInByte());
        values.put("server_flag", pmgsySurvey.getServer_flag());
        values.put("land_address", pmgsySurvey.getLand_address());
        values.put("other_land_type", pmgsySurvey.getOther_land_type());
        values.put("area", pmgsySurvey.getArea());
        values.put("survey_number", pmgsySurvey.getSurvey_number());
        values.put("sub_div_no", pmgsySurvey.getSub_div_no());
        values.put("latitude", pmgsySurvey.getLatitude());
        values.put("longtitude", pmgsySurvey.getLongitude());
        long id = db.insert(DBHelper.NURSERY_LAND_SAVE_DETAILS,null,values);
        Log.d("Insert_NURSERY_land", String.valueOf(id));
    }
    public void insert_nursery_species_type(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("species_type_id", pmgsySurvey.getSpecies_type_id());
        values.put("species_name_en", pmgsySurvey.getSpecies_name_en());
        values.put("species_name_ta", pmgsySurvey.getSpecies_name_ta());
        long id = db.insert(DBHelper.NURSERY_SPECIES_TYPE,null,values);
        Log.d("Insert_nursery_species", String.valueOf(id));
    }
    public void insert_dead_stage(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("dead_stage_id", pmgsySurvey.getDead_stage_id());
        values.put("dead_stage_name_en", pmgsySurvey.getDead_stage_name_en());
        values.put("dead_stage_name_ta", pmgsySurvey.getDead_stage_name_ta());
        long id = db.insert(DBHelper.DEAD_STAGE,null,values);
        Log.d("Insert_dead_stage", String.valueOf(id));
    }
    public long insert_nursery_batch_details(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("batch_id", pmgsySurvey.getBatch_id());
        values.put("batch_number", pmgsySurvey.getBatch_number());
        values.put("created_date", pmgsySurvey.getCreated_date());
        values.put("server_flag", pmgsySurvey.getServer_flag());
        values.put("is_batch_closed", pmgsySurvey.getIs_batch_closed());
        long id = db.insert(DBHelper.BATCH_DETAILS,null,values);
        Log.d("Insert_NURSERY_batch", String.valueOf(id));
        return id;
    }
    public void insert_nursery_batch_species_details(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("batch_primary_id", pmgsySurvey.getBatch_primary_id());
        values.put("batch_id", pmgsySurvey.getBatch_id());
        values.put("batch_species_id", pmgsySurvey.getBatch_species_id());
        values.put("species_type_id", pmgsySurvey.getSpecies_type_id());
        values.put("server_flag", pmgsySurvey.getServer_flag());
        values.put("species_type_name_en", pmgsySurvey.getSpecies_name_en());
        values.put("species_type_name_ta", pmgsySurvey.getSpecies_name_ta());
        values.put("no_of_count", pmgsySurvey.getNo_of_count());
        values.put("is_harvest_closed", pmgsySurvey.getIs_harvest_closed());
        long id = db.insert(DBHelper.BATCH_SPECIES_DETAILS,null,values);
        Log.d("Insert_batch_SPECIES", String.valueOf(id));
    }
    public void insert_nursery_batch_growth_tracking_dates(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("entry_date", pmgsySurvey.getCreated_date());
        values.put("batch_id", pmgsySurvey.getBatch_id());
        long id = db.insert(DBHelper.BATCH_GROWTH_TRACKING_DATES,null,values);
        Log.d("Insert_dates", String.valueOf(id));
    }
    public long insert_nursery_batch_growth_details(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("growth_tracking_id", pmgsySurvey.getGrowth_tracking_id());
        values.put("batch_primary_id", pmgsySurvey.getBatch_primary_id());
        values.put("batch_id", pmgsySurvey.getBatch_id());
        values.put("entry_date", pmgsySurvey.getCreated_date());
        values.put("server_flag", pmgsySurvey.getServer_flag());

        long id = db.insert(DBHelper.BATCH_GROWTH_TRACKING_DETAILS,null,values);
        Log.d("Insert_batch_growth", String.valueOf(id));
        return id;
    }
    public void insert_nursery_batch_growth_species_details(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("batch_growth_tracking_primary_id", pmgsySurvey.getBatch_growth_tracking_primary_id());
        values.put("batch_primary_id", pmgsySurvey.getBatch_primary_id());
        values.put("growth_tracking_details_id", pmgsySurvey.getGrowth_tracking_details_id());
        values.put("batch_id", pmgsySurvey.getBatch_id());
        values.put("batch_species_id", pmgsySurvey.getBatch_species_id());
        values.put("species_type_id", pmgsySurvey.getSpecies_type_id());
        values.put("server_flag", pmgsySurvey.getServer_flag());
        values.put("entry_date", pmgsySurvey.getCreated_date());
        values.put("no_of_saplings", pmgsySurvey.getNo_of_saplings());
        values.put("height_in_cm", pmgsySurvey.getHeight_in_cm());
        values.put("age_in_days", pmgsySurvey.getAge_in_days());
        long id = db.insert(DBHelper.BATCH_GROWTH_TRACKING_SPECIES_DETAILS,null,values);
        Log.d("Insert_growth_SPECIES", String.valueOf(id));
    }

    public void insert_dead_sapling_details(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("nursery_id", pmgsySurvey.getNursery_id());
        values.put("batch_id", pmgsySurvey.getBatch_id());
        values.put("batch_species_id", pmgsySurvey.getBatch_species_id());
        values.put("growth_tracking_id", pmgsySurvey.getGrowth_tracking_id());
        values.put("growth_tracking_details_id", pmgsySurvey.getGrowth_tracking_details_id());
        values.put("batch_no", pmgsySurvey.getBatch_number());
        values.put("species_type_id", pmgsySurvey.getSpecies_type_id());
        values.put("species_name_en", pmgsySurvey.getSpecies_name_en());
        values.put("species_name_ta", pmgsySurvey.getSpecies_name_ta());
        values.put("sapling_height_in_cm", pmgsySurvey.getHeight_in_cm());
        values.put("sapling_age_in_days", pmgsySurvey.getAge_in_days());
        values.put("no_of_saplings", pmgsySurvey.getNo_of_saplings());
        values.put("dead_stage_position", pmgsySurvey.getDead_stage_position());
        values.put("dead_reason_text", pmgsySurvey.getDead_reason_text());
        values.put("dead_typed_count_text", pmgsySurvey.getDead_typed_count_text());

        long id = db.insert(DBHelper.DEAD_SAPLING_DETAILS,null,values);
        Log.d("Insert_dead_sapling", String.valueOf(id));
    }

    public void insert_nursery_expenditure_type(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("expenditure_type_id", pmgsySurvey.getExpenditure_type_id());
        values.put("expense_category_id", pmgsySurvey.getExpense_category_id());
        values.put("expense_category_en", pmgsySurvey.getExpense_category_en());
        values.put("expense_category_ta", pmgsySurvey.getExpense_category_ta());
        values.put("is_others", pmgsySurvey.getIs_others());
        long id = db.insert(DBHelper.NURSERY_EXPENDITURE_TYPE,null,values);
        Log.d("Insert_expen_type", String.valueOf(id));
    }

    public void insert_nursery_expenditure_unit(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("expense_category_id", pmgsySurvey.getExpense_category_id());
        values.put("expenditure_unit_id", pmgsySurvey.getExpenditure_unit_id());
        values.put("expenditure_unit_en", pmgsySurvey.getExpenditure_unit_en());
        values.put("expenditure_unit_ta", pmgsySurvey.getExpenditure_unit_ta());
        long id = db.insert(DBHelper.NURSERY_EXPENDITURE_UNIT,null,values);
        Log.d("Insert_expen_unit", String.valueOf(id));
    }
    public void insert_nursery_expenditure_found_src(NurserySurvey pmgsySurvey) {
        ContentValues values = new ContentValues();
        values.put("expense_category_id", pmgsySurvey.getExpense_category_id());
        values.put("expenditure_fund_src_id", pmgsySurvey.getExpenditure_fund_src_id());
        values.put("expenditure_fund_src_en", pmgsySurvey.getExpenditure_fund_src_en());
        values.put("expenditure_fund_src_ta", pmgsySurvey.getExpenditure_fund_src_ta());
        long id = db.insert(DBHelper.NURSERY_EXPENDITURE_FOUND_SRC,null,values);
        Log.d("Insert_expen_found", String.valueOf(id));
    }


    public ArrayList<NurserySurvey> getAll_Master_Fin_Year() {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.MASTER_FIN_YEAR_TABLE,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setFin_year(cursor.getString(cursor
                            .getColumnIndexOrThrow("fin_year")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> getAll_Master_Work_Type() {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.MASTER_WORK_TYPE_TABLE,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setWork_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow("work_code")));
                    card.setWork_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("work_name")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> getAll_Master_Self_Help_Group() {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.MASTER_SELF_HELP_GROUP_TABLE,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setShg_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow("shg_code")));
                    card.setShg_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("shg_name")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> getAll_Master_Self_Help_Group_Member(int shg_code) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {
            //cursor = db.rawQuery("select * from "+DBHelper.MASTER_SELF_HELP_GROUP_MEMBER_TABLE,null);
            selection = "shg_code = ? ";
            selectionArgs = new String[]{String.valueOf(shg_code)};
            cursor = db.query(DBHelper.MASTER_SELF_HELP_GROUP_MEMBER_TABLE,
                    new String[]{"*"}, selection, selectionArgs, null, null, null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setShg_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow("shg_code")));
                    card.setShg_member_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow("shg_member_code")));
                    card.setMember_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("member_name")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> getParticular_Before_Save_Tree_Image_Table(int shg_code, int shg_member_code) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {
            //cursor = db.rawQuery("select * from "+DBHelper.MASTER_SELF_HELP_GROUP_MEMBER_TABLE,null);
            selection = "shg_code = ? and shg_member_code = ? ";
            selectionArgs = new String[]{String.valueOf(shg_code),String.valueOf(shg_member_code)};
            cursor = db.query(DBHelper.SAVE_BEFORE_TREE_IMAGE_TABLE,
                    new String[]{"*"}, selection, selectionArgs, null, null, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();

                    card.setShg_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow("shg_code")));
                    card.setShg_member_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow("shg_member_code")));
                    card.setMember_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("member_name")));
                    card.setBefore_photo_lat(cursor.getString(cursor
                            .getColumnIndexOrThrow("before_photo_lat")));
                    card.setBefore_photo_long(cursor.getString(cursor
                            .getColumnIndexOrThrow("before_photo_long")));
                    byte[] before_photo = cursor.getBlob(cursor.getColumnIndexOrThrow("before_photo"));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(before_photo, 0, before_photo.length, options);
                    card.setBefore_photo((bitmap));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> getParticular_After_Save_Tree_Image_Table(int shg_code, int shg_member_code) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {
            //cursor = db.rawQuery("select * from "+DBHelper.MASTER_SELF_HELP_GROUP_MEMBER_TABLE,null);
            selection = "shg_code = ? and shg_member_code = ? ";
            selectionArgs = new String[]{String.valueOf(shg_code),String.valueOf(shg_member_code)};
            cursor = db.query(DBHelper.SAVE_AFTER_TREE_IMAGE_TABLE,
                    new String[]{"*"}, selection, selectionArgs, null, null, null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();

                    card.setShg_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow("shg_code")));
                    card.setShg_member_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow("shg_member_code")));
                    card.setMember_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("member_name")));
                    card.setAfter_photo_lat(cursor.getString(cursor
                            .getColumnIndexOrThrow("after_photo_lat")));
                    card.setAfter_photo_long(cursor.getString(cursor
                            .getColumnIndexOrThrow("after_photo_long")));
                    byte[] after_photo = cursor.getBlob(cursor.getColumnIndexOrThrow("after_photo"));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(after_photo, 0, after_photo.length, options);
                    card.setAfter_photo((bitmap));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> getAllTreeImages(){
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {

            cursor = db.rawQuery(AppConstant.SQL_QUERY,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();

                    card.setShg_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow("shg_code")));
                    card.setShg_member_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow("shg_member_code")));
                    card.setWork_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow("work_code")));
                    card.setShg_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("shg_name")));
                    card.setMember_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("member_name")));
                    card.setWork_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("work_name")));
                    card.setFin_year(cursor.getString(cursor
                            .getColumnIndexOrThrow("fin_year")));



                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }




    public Bitmap bytearrtoBitmap(byte[] photo){
        byte[] imgbytes = Base64.decode(photo, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgbytes, 0,
                imgbytes.length);
        return bitmap;
    }


    //////Nursery Tables
    public ArrayList<NurserySurvey> get_nursery_user_details() {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.NURSERY_USER_DETAILS,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setNursery_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("nursery_id")));
                    card.setNursery_level_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("nursery_level_id")));
                    card.setNursery_name_en(cursor.getString(cursor
                            .getColumnIndexOrThrow("nursery_name_en")));
                    card.setNursery_name_ta(cursor.getString(cursor
                            .getColumnIndexOrThrow("nursery_name_ta")));
                    card.setNursery_address(cursor.getString(cursor
                            .getColumnIndexOrThrow("nursery_address")));
                    card.setNursery_dname(cursor.getString(cursor
                            .getColumnIndexOrThrow("dname")));
                    card.setNursery_bname(cursor.getString(cursor
                            .getColumnIndexOrThrow("bname")));
                    card.setNursery_pvname(cursor.getString(cursor
                            .getColumnIndexOrThrow("pvname")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> get_nursery_land_type() {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.NURSERY_LAND_TYPE,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setLand_type_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("land_type_id")));
                    card.setLand_type_name_en(cursor.getString(cursor
                            .getColumnIndexOrThrow("land_type_name_en")));
                    card.setLand_type_name_ta(cursor.getString(cursor
                            .getColumnIndexOrThrow("land_type_name_ta")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> get_nursery_species_type() {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.NURSERY_SPECIES_TYPE,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setSpecies_type_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("species_type_id")));
                    card.setSpecies_name_en(cursor.getString(cursor
                            .getColumnIndexOrThrow("species_name_en")));
                    card.setSpecies_name_ta(cursor.getString(cursor
                            .getColumnIndexOrThrow("species_name_ta")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> get_all_dead_stage() {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.DEAD_STAGE,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setDead_stage_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("dead_stage_id")));
                    card.setDead_stage_name_en(cursor.getString(cursor
                            .getColumnIndexOrThrow("dead_stage_name_en")));
                    card.setDead_stage_name_ta(cursor.getString(cursor
                            .getColumnIndexOrThrow("dead_stage_name_ta")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<NurserySurvey> get_all_expenditure_type() {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.NURSERY_EXPENDITURE_TYPE,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setExpenditure_type_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("expenditure_type_id")));
                    card.setExpense_category_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("expense_category_id")));
                    card.setExpense_category_en(cursor.getString(cursor
                            .getColumnIndexOrThrow("expense_category_en")));
                    card.setExpense_category_ta(cursor.getString(cursor
                            .getColumnIndexOrThrow("expense_category_ta")));
                    card.setIs_others(cursor.getString(cursor
                            .getColumnIndexOrThrow("is_others")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> get_all_expenditure_unit() {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.NURSERY_EXPENDITURE_UNIT,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setExpenditure_unit_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("expenditure_unit_id")));
                    card.setExpense_category_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("expense_category_id")));
                    card.setExpenditure_unit_en(cursor.getString(cursor
                            .getColumnIndexOrThrow("expenditure_unit_en")));
                    card.setExpenditure_unit_ta(cursor.getString(cursor
                            .getColumnIndexOrThrow("expenditure_unit_ta")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> get_all_expenditure_found_src() {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.NURSERY_EXPENDITURE_FOUND_SRC,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setExpenditure_fund_src_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("expenditure_fund_src_id")));
                    card.setExpense_category_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("expense_category_id")));
                    card.setExpenditure_fund_src_en(cursor.getString(cursor
                            .getColumnIndexOrThrow("expenditure_fund_src_en")));
                    card.setExpenditure_fund_src_ta(cursor.getString(cursor
                            .getColumnIndexOrThrow("expenditure_fund_src_ta")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<NurserySurvey> get_nursery_land_details(String type,String server_flag,String value) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {
            selection = "server_flag = ? and land_primary_id = ? ";
            selectionArgs = new String[]{server_flag,value};

            if(type.equals("All")){
                cursor = db.rawQuery("select * from "+DBHelper.NURSERY_LAND_SAVE_DETAILS,null);

            }
            else {
                cursor = db.query(DBHelper.NURSERY_LAND_SAVE_DETAILS,new String[]{"*"},
                        selection, selectionArgs, null, null, null);
            }

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setLand_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("land_primary_id")));
                    card.setLand_type_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("land_type_id")));
                    card.setNursery_land_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("nursery_land_id")));
                    card.setNursery_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("nursery_id")));
                    card.setServer_flag(cursor.getString(cursor
                            .getColumnIndexOrThrow("server_flag")));
                    card.setLand_address(cursor.getString(cursor
                            .getColumnIndexOrThrow("land_address")));
                    card.setOther_land_type(cursor.getString(cursor
                            .getColumnIndexOrThrow("other_land_type")));
                    card.setArea(cursor.getString(cursor
                            .getColumnIndexOrThrow("area")));
                    card.setSurvey_number(cursor.getString(cursor
                            .getColumnIndexOrThrow("survey_number")));
                    card.setSub_div_no(cursor.getString(cursor
                            .getColumnIndexOrThrow("sub_div_no")));
                    card.setLand_type_name_en(cursor.getString(cursor
                            .getColumnIndexOrThrow("land_type_name_en")));
                    card.setLand_type_name_ta(cursor.getString(cursor
                            .getColumnIndexOrThrow("land_type_name_ta")));
                    card.setLatitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("latitude")));
                    card.setLongitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("longtitude")));
                    byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);
                    card.setImage((bitmap));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> get_nursery_batch_details(String type,String server_flag,String value) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {
            if(type.equals("")){
                selection = "server_flag = ? and batch_primary_id = ? ";
                selectionArgs = new String[]{server_flag,value};
            }
            else {
                selection = "batch_primary_id = ? ";
                selectionArgs = new String[]{value};
            }


            if(type.equals("All")){
                cursor = db.rawQuery("select * from "+DBHelper.BATCH_DETAILS,null);

            }
            else {
                cursor = db.query(DBHelper.BATCH_DETAILS,new String[]{"*"},
                        selection, selectionArgs, null, null, null);
            }

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setBatch_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_primary_id")));
                    card.setBatch_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_id")));
                    card.setBatch_number(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_number")));
                    card.setServer_flag(cursor.getString(cursor
                            .getColumnIndexOrThrow("server_flag")));
                    card.setCreated_date(cursor.getString(cursor
                            .getColumnIndexOrThrow("created_date")));
                    card.setIs_batch_closed(cursor.getString(cursor
                            .getColumnIndexOrThrow("is_batch_closed")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> get_nursery_batch_photos_details(String value) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {
            selection = "batch_primary_id = ? ";
            selectionArgs = new String[]{value};

                cursor = db.query(DBHelper.BATCH_IMAGES_DETAILS,new String[]{"*"},
                        selection, selectionArgs, null, null, null);


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setBatch_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_primary_id")));
                    card.setBatch_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_id")));
                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length, options);
                    card.setImage(bitmap);
                    card.setLatitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("lattitude")));
                    card.setLongitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("longtitude")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> get_nursery_batch_species_details(String value,String type,String server_flag,String species_type_id) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {
            if(type.equals("All")){
                selection = "batch_primary_id = ? ";
                selectionArgs = new String[]{value};
            }
            else if(type.equals("")){
                selection = " batch_primary_id = ? and server_flag = ? ";
                selectionArgs = new String[]{value,server_flag};
            }
            else if(type.equals("local")){
                selection = "server_flag = ? ";
                selectionArgs = new String[]{server_flag};
            }
            else {
                selection = "batch_primary_id = ? and species_type_id = ? ";
                selectionArgs = new String[]{value,species_type_id};
            }


                cursor = db.query(DBHelper.BATCH_SPECIES_DETAILS,new String[]{"*"},
                        selection, selectionArgs, null, null, null);


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setBatch_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_primary_id")));
                    card.setBatch_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_id")));
                    card.setBatch_species_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_species_id")));
                    card.setSpecies_type_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("species_type_id")));
                    card.setNo_of_count(cursor.getInt(cursor
                            .getColumnIndexOrThrow("no_of_count")));
                    card.setServer_flag(cursor.getString(cursor
                            .getColumnIndexOrThrow("server_flag")));
                    card.setSpecies_name_en(cursor.getString(cursor
                            .getColumnIndexOrThrow("species_type_name_en")));
                    card.setSpecies_name_ta(cursor.getString(cursor
                            .getColumnIndexOrThrow("species_type_name_ta")));
                    card.setIs_harvest_closed(cursor.getString(cursor
                            .getColumnIndexOrThrow("is_harvest_closed")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
               Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<NurserySurvey> get_batch_growth_tracking_dates(String batch_id) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {
            selection = "batch_id = ? ";
            selectionArgs = new String[]{batch_id};
            //cursor = db.rawQuery("select * from "+DBHelper.BATCH_GROWTH_TRACKING_DATES,null);
            cursor = db.query(DBHelper.BATCH_GROWTH_TRACKING_DATES,new String[]{"*"},
                    selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setBatch_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_id")));
                    card.setCreated_date(cursor.getString(cursor
                            .getColumnIndexOrThrow("entry_date")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> get_nursery_batch_growth_tacking_details(String batch_id,String entry_date,String type) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {

            if(type.equals("All")){
                selection = "batch_id = ? and entry_date = ?";
                selectionArgs = new String[]{batch_id,entry_date};
            }
            else {
                selection = "batch_id = ? and entry_date = ?";
                selectionArgs = new String[]{batch_id,entry_date};
            }


            cursor = db.query(DBHelper.BATCH_GROWTH_TRACKING_DETAILS,new String[]{"*"},
                        selection, selectionArgs, null, null, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setBatch_growth_tracking_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_growth_tracking_primary_id")));
                    card.setGrowth_tracking_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("growth_tracking_id")));
                    card.setBatch_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_primary_id")));
                    card.setBatch_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_id")));
                    card.setServer_flag(cursor.getString(cursor
                            .getColumnIndexOrThrow("server_flag")));
                    card.setCreated_date(cursor.getString(cursor
                            .getColumnIndexOrThrow("entry_date")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> get_batch_growth_tracking_photos_details(String value,String entry_date) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {
            selection = "batch_id = ? and entry_date = ?";
            selectionArgs = new String[]{value,entry_date};

            cursor = db.query(DBHelper.BATCH_GROWTH_TRACKING_PHOTOS_DETAILS,new String[]{"*"},
                    selection, selectionArgs, null, null, null);


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setBatch_growth_tracking_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_growth_tracking_primary_id")));
                    card.setBatch_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_primary_id")));
                    card.setBatch_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_id")));
                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length, options);
                    card.setImage(bitmap);
                    card.setServer_flag(cursor.getString(cursor
                            .getColumnIndexOrThrow("server_flag")));
                    card.setLatitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("lattitude")));
                    card.setLongitude(cursor.getString(cursor
                            .getColumnIndexOrThrow("longtitude")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> get_batch_growth_species_details(String value,String type,String server_flag,String species_type_id,String entry_date) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {
            switch (type) {
                case "All":
                    selection = "batch_id = ? and entry_date = ? ";
                    selectionArgs = new String[]{value, entry_date};
                    break;
                case "":
                    selection = " batch_id = ? and server_flag = ?  and entry_date = ? ";
                    selectionArgs = new String[]{value, server_flag, entry_date};
                    break;
                case "local":
                    selection = "server_flag = ? ";
                    selectionArgs = new String[]{server_flag};
                    break;
                default:
                    selection = "batch_id = ? and species_type_id = ?  and entry_date = ? ";
                    selectionArgs = new String[]{value, species_type_id, entry_date};
                    break;
            }


            cursor = db.query(DBHelper.BATCH_GROWTH_TRACKING_SPECIES_DETAILS,new String[]{"*"},
                    selection, selectionArgs, null, null, null);


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setBatch_growth_tracking_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_growth_tracking_primary_id")));
                    card.setBatch_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_primary_id")));
                    card.setBatch_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_id")));
                    card.setBatch_species_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_species_id")));
                    card.setSpecies_type_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("species_type_id")));
                    card.setServer_flag(cursor.getString(cursor
                            .getColumnIndexOrThrow("server_flag")));
                    card.setNo_of_saplings(cursor.getInt(cursor
                            .getColumnIndexOrThrow("no_of_saplings")));
                    card.setHeight_in_cm(cursor.getString(cursor
                            .getColumnIndexOrThrow("height_in_cm")));
                    card.setAge_in_days(cursor.getInt(cursor
                            .getColumnIndexOrThrow("age_in_days")));
                    card.setCreated_date(cursor.getString(cursor
                            .getColumnIndexOrThrow("entry_date")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<NurserySurvey> get_particular_dead_sapling_details(String batch_id,String species_type_id,String server_flag,String type) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        if(type.equals("Particular")){
            selection = "batch_id = ? and species_type_id = ? and server_flag = ? ";
            selectionArgs = new String[]{batch_id,species_type_id,server_flag};
        }
        else {
            selection = " batch_id = ? and server_flag = ? ";
            selectionArgs = new String[]{batch_id,server_flag};
        }
        try {
            //cursor = db.rawQuery("select * from "+DBHelper.DEAD_SAPLING_DETAILS_SAVE,null);
            cursor = db.query(DBHelper.DEAD_SAPLING_DETAILS_SAVE,new String[]{"*"},
                    selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setBatch_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_primary_id")));
                    card.setBatch_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_id")));
                    card.setBatch_species_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_species_id")));
                    card.setSpecies_type_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("species_type_id")));
                    card.setServer_flag(cursor.getString(cursor
                            .getColumnIndexOrThrow("server_flag")));
                    card.setDead_stage_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("dead_stage")));
                    card.setNo_of_dead_sapling(cursor.getInt(cursor
                            .getColumnIndexOrThrow("no_of_dead_sapling")));
                    card.setDead_reason(cursor.getString(cursor
                            .getColumnIndexOrThrow("dead_reason")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<NurserySurvey> get_dead_sapling_details() {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {

            cursor = db.rawQuery("select * from "+DBHelper.DEAD_SAPLING_DETAILS,null);
            //cursor = db.rawQuery(DBHelper.DEAD_SAPLING_DETAILS,null);


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setNursery_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("nursery_id")));
                    card.setBatch_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_id")));
                    card.setBatch_species_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_species_id")));
                    card.setGrowth_tracking_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("growth_tracking_id")));
                    card.setGrowth_tracking_details_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("growth_tracking_details_id")));
                    card.setBatch_number(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_no")));
                    card.setSpecies_type_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("species_type_id")));
                    card.setSpecies_name_en(cursor.getString(cursor
                            .getColumnIndexOrThrow("species_name_en")));
                    card.setSpecies_name_ta(cursor.getString(cursor
                            .getColumnIndexOrThrow("species_name_ta")));
                    card.setHeight_in_cm(cursor.getString(cursor
                            .getColumnIndexOrThrow("sapling_height_in_cm")));
                    card.setAge_in_days(cursor.getInt(cursor
                            .getColumnIndexOrThrow("sapling_age_in_days")));
                    card.setNo_of_saplings(cursor.getInt(cursor
                            .getColumnIndexOrThrow("no_of_saplings")));
                    card.setDead_stage_position(cursor.getInt(cursor
                            .getColumnIndexOrThrow("dead_stage_position")));
                    card.setDead_reason_text(cursor.getString(cursor
                            .getColumnIndexOrThrow("dead_reason_text")));
                    card.setDead_typed_count_text(cursor.getString(cursor
                            .getColumnIndexOrThrow("dead_typed_count_text")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<NurserySurvey> get_dead_sapling_details_for_upload(String type,String batch_id) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        if(type.equals("Particular")){
            selection = "batch_id = ? ";
            selectionArgs = new String[]{batch_id};
            cursor = db.query(DBHelper.DEAD_SAPLING_DETAILS_NEW_SAVE,new String[]{"*"},
                    selection, selectionArgs, null, null, null);
        }
        else {
            cursor = db.rawQuery("select * from "+DBHelper.DEAD_SAPLING_DETAILS_NEW_SAVE,null);
        }
        try {

            //cursor = db.rawQuery("select * from "+DBHelper.DEAD_SAPLING_DETAILS_NEW_SAVE,null);
            //cursor = db.rawQuery(DBHelper.DEAD_SAPLING_DETAILS,null);


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setBatch_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_id")));
                    card.setBatch_number(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_no")));
                    card.setBatch_species_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_species_id")));
                    card.setGrowth_tracking_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("growth_tracking_id")));
                    card.setGrowth_tracking_details_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("growth_tracking_details_id")));
                    card.setSpecies_type_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("species_type_id")));
                    card.setHeight_in_cm(cursor.getString(cursor
                            .getColumnIndexOrThrow("sapling_height_in_cm")));
                    card.setAge_in_days(cursor.getInt(cursor
                            .getColumnIndexOrThrow("sapling_age_in_days")));
                    card.setNo_of_dead_sapling(cursor.getInt(cursor
                            .getColumnIndexOrThrow("no_of_dead_sapling")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<NurserySurvey> get_dead_sapling_batch_details() {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;

        try {

            cursor = db.rawQuery("select distinct batch_id,batch_no from "+DBHelper.DEAD_SAPLING_DETAILS_NEW_SAVE,null);
            //cursor = db.rawQuery(DBHelper.DEAD_SAPLING_DETAILS,null);


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setBatch_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_id")));
                    card.setBatch_number(cursor.getInt(cursor
                            .getColumnIndexOrThrow("batch_no")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<NurserySurvey> get_All_Or_Particular_Expenditure(String type,String date) {
        ArrayList<NurserySurvey> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        if(type.equals("Particular")){
            selection = " entry_date = ? ";
            selectionArgs = new String[]{date};
            cursor = db.query(DBHelper.NURSERY_EXPENDITURE_SAVE,new String[]{"*"},
                    selection, selectionArgs, null, null, null);
        }
        else {
            cursor = db.rawQuery("select * from "+DBHelper.NURSERY_EXPENDITURE_SAVE,null);
        }
        try {
            //cursor = db.rawQuery("select * from "+DBHelper.DEAD_SAPLING_DETAILS_SAVE,null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NurserySurvey card = new NurserySurvey();
                    card.setExpenditure_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("expenditure_primary_id")));
                    card.setFin_year(cursor.getString(cursor
                            .getColumnIndexOrThrow("fin_year")));
                    card.setEntry_date(cursor.getString(cursor
                            .getColumnIndexOrThrow("entry_date")));
                    card.setExpenditure_type_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("expenditure_type_id")));
                    card.setExpense_category_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("expense_category_id")));
                    card.setExpenditure_unit_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("expenditure_unit_id")));
                    card.setExpenditure_fund_src_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("expenditure_fund_src_id")));
                    card.setExpense_category_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("expense_category_name")));
                    card.setExpense_unit_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("expense_unit_name")));
                    card.setExpense_found_src_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("expense_found_src_name")));
                    card.setExpense_category_others(cursor.getString(cursor
                            .getColumnIndexOrThrow("expense_category_others")));
                    card.setExpenditure_unit_others(cursor.getString(cursor
                            .getColumnIndexOrThrow("expenditure_unit_others")));
                    card.setExpenditure_fund_src_others(cursor.getString(cursor
                            .getColumnIndexOrThrow("expenditure_fund_src_others")));
                    card.setQuantity(cursor.getString(cursor
                            .getColumnIndexOrThrow("quantity")));
                    card.setAmount(cursor.getString(cursor
                            .getColumnIndexOrThrow("amount")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }


    //////////////////////*****************/////////////


    public void deleteVillageTable() {
        db.execSQL("delete from " + DBHelper.VILLAGE_TABLE_NAME);
    }

    public void deletePMAYTable() {
        db.execSQL("delete from " + DBHelper.PMAY_LIST_TABLE_NAME);
    }

    public void deletePMAYDetails() { db.execSQL("delete from " + DBHelper.SAVE_PMAY_DETAILS); }

    public void deletePMAYImages() { db.execSQL("delete from " + DBHelper.SAVE_PMAY_IMAGES);}

    public void deletefin_year() { db.execSQL("delete from " + DBHelper.MASTER_FIN_YEAR_TABLE);}
    public void deletework_type() { db.execSQL("delete from " + DBHelper.MASTER_WORK_TYPE_TABLE);}
    public void deleteself_help_group() { db.execSQL("delete from " + DBHelper.MASTER_SELF_HELP_GROUP_TABLE);}
    public void deleteself_help_group_member() { db.execSQL("delete from " + DBHelper.MASTER_SELF_HELP_GROUP_MEMBER_TABLE);}
    public void deleteSAVE_BEFORE_TREE_IMAGE_TABLE() { db.execSQL("delete from " + DBHelper.SAVE_BEFORE_TREE_IMAGE_TABLE);}
    public void deleteSAVE_AFTER_TREE_IMAGE_TABLE() { db.execSQL("delete from " + DBHelper.SAVE_AFTER_TREE_IMAGE_TABLE);}

    /////Delete Nursery Tables
    public void delete_nursery_user_details() { db.execSQL("delete from " + DBHelper.NURSERY_USER_DETAILS);}
    public void delete_nursery_land_type() { db.execSQL("delete from " + DBHelper.NURSERY_LAND_TYPE);}
    public void delete_nursery_species_type() { db.execSQL("delete from " + DBHelper.NURSERY_SPECIES_TYPE);}

    public void delete_nursery_land_save_details() { db.execSQL("delete from " + DBHelper.NURSERY_LAND_SAVE_DETAILS);}
    public void delete_batch_details() { db.execSQL("delete from " + DBHelper.BATCH_DETAILS);}
    public void delete_batch_images_details() { db.execSQL("delete from " + DBHelper.BATCH_IMAGES_DETAILS);}
    public void delete_batch_species_details() { db.execSQL("delete from " + DBHelper.BATCH_SPECIES_DETAILS);}
    public void delete_batch_growth_tracking_dates() { db.execSQL("delete from " + DBHelper.BATCH_GROWTH_TRACKING_DATES);}
    public void delete_batch_growth_tracking_details() { db.execSQL("delete from " + DBHelper.BATCH_GROWTH_TRACKING_DETAILS);}
    public void delete_batch_growth_tracking_photos_details() { db.execSQL("delete from " + DBHelper.BATCH_GROWTH_TRACKING_PHOTOS_DETAILS);}
    public void delete_batch_growth_tracking_species_details() { db.execSQL("delete from " + DBHelper.BATCH_GROWTH_TRACKING_SPECIES_DETAILS);}
    public void delete_dead_stage() { db.execSQL("delete from " + DBHelper.DEAD_STAGE);}
    public void delete_DEAD_SAPLING_DETAILS() { db.execSQL("delete from " + DBHelper.DEAD_SAPLING_DETAILS);}
    public void delete_dead_sapling_details_save() { db.execSQL("delete from " + DBHelper.DEAD_SAPLING_DETAILS_SAVE);}
    public void delete_dead_sapling_details_new_save() { db.execSQL("delete from " + DBHelper.DEAD_SAPLING_DETAILS_NEW_SAVE);}

    public void delete_nursery_expenditure_type() { db.execSQL("delete from " + DBHelper.NURSERY_EXPENDITURE_TYPE);}
    public void delete_nursery_expenditure_unit() { db.execSQL("delete from " + DBHelper.NURSERY_EXPENDITURE_UNIT);}
    public void delete_nursery_expenditure_found_src() { db.execSQL("delete from " + DBHelper.NURSERY_EXPENDITURE_FOUND_SRC);}
    public void delete_nursery_expenditure_save() { db.execSQL("delete from " + DBHelper.NURSERY_EXPENDITURE_SAVE);}



    public void deleteAll() {

        deleteVillageTable();
        deletePMAYTable();
        deletePMAYDetails();
        deletePMAYImages();

        deletefin_year();
        deletework_type();
        deleteself_help_group();
        deleteself_help_group_member();

        deleteSAVE_BEFORE_TREE_IMAGE_TABLE();
        deleteSAVE_AFTER_TREE_IMAGE_TABLE();


        //////Nursery Tables
        delete_nursery_user_details();
        delete_nursery_land_type();
        delete_nursery_species_type();


        delete_nursery_land_save_details();
        delete_batch_details();
        delete_batch_images_details();
        delete_batch_species_details();
        delete_batch_growth_tracking_dates();
        delete_batch_growth_tracking_details();
        delete_batch_growth_tracking_photos_details();
        delete_batch_growth_tracking_species_details();
        delete_dead_stage();
        delete_DEAD_SAPLING_DETAILS();
        delete_dead_sapling_details_save();
        delete_dead_sapling_details_new_save();

        delete_nursery_expenditure_type();
        delete_nursery_expenditure_unit();
        delete_nursery_expenditure_found_src();
        delete_nursery_expenditure_save();
    }



}
