package com.nic.nurserygarden.model;

import android.graphics.Bitmap;

/**
 * Created by AchanthiSundar on 01-11-2017.
 */

public class NurserySurvey {

    private String distictCode;
    private String districtName;

    private String blockCode;

    public String getHabCode() {
        return HabCode;
    }

    public void setHabCode(String habCode) {
        HabCode = habCode;
    }

    private String HabCode;

    private String Description;
    private String Latitude;
    private String BeneficiaryName;
    private String HabitationName;
    private String pmayId;
    private String fatherName;
    private String personAlive;
    private String buttonText;

    /////New Variable
    private String fin_year;
    private int  work_code;
    private String  work_name;
    private int shg_code;
    private int shg_member_code;
    private String shg_name;
    private String member_name;
    private String before_photo_lat;
    private String before_photo_long;
    private String after_photo_lat;
    private String after_photo_long;
    private Bitmap before_photo;
    private Bitmap after_photo;

    public String getBefore_photo_lat() {
        return before_photo_lat;
    }

    public void setBefore_photo_lat(String before_photo_lat) {
        this.before_photo_lat = before_photo_lat;
    }

    public String getBefore_photo_long() {
        return before_photo_long;
    }

    public void setBefore_photo_long(String before_photo_long) {
        this.before_photo_long = before_photo_long;
    }

    public String getAfter_photo_lat() {
        return after_photo_lat;
    }

    public void setAfter_photo_lat(String after_photo_lat) {
        this.after_photo_lat = after_photo_lat;
    }

    public String getAfter_photo_long() {
        return after_photo_long;
    }

    public void setAfter_photo_long(String after_photo_long) {
        this.after_photo_long = after_photo_long;
    }

    public Bitmap getBefore_photo() {
        return before_photo;
    }

    public void setBefore_photo(Bitmap before_photo) {
        this.before_photo = before_photo;
    }

    public Bitmap getAfter_photo() {
        return after_photo;
    }

    public void setAfter_photo(Bitmap after_photo) {
        this.after_photo = after_photo;
    }

    public String getFin_year() {
        return fin_year;
    }

    public void setFin_year(String fin_year) {
        this.fin_year = fin_year;
    }

    public int getWork_code() {
        return work_code;
    }

    public void setWork_code(int work_code) {
        this.work_code = work_code;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public int getShg_code() {
        return shg_code;
    }

    public void setShg_code(int shg_code) {
        this.shg_code = shg_code;
    }

    public int getShg_member_code() {
        return shg_member_code;
    }

    public void setShg_member_code(int shg_member_code) {
        this.shg_member_code = shg_member_code;
    }

    public String getShg_name() {
        return shg_name;
    }

    public void setShg_name(String shg_name) {
        this.shg_name = shg_name;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getPersonAlive() {
        return personAlive;
    }

    public void setPersonAlive(String personAlive) {
        this.personAlive = personAlive;
    }

    public String getIsLegel() {
        return isLegel;
    }

    public void setIsLegel(String isLegel) {
        this.isLegel = isLegel;
    }

    public String getIsMigrated() {
        return isMigrated;
    }

    public void setIsMigrated(String isMigrated) {
        this.isMigrated = isMigrated;
    }

    private String isLegel;
    private String isMigrated;

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPmayId() {
        return pmayId;
    }

    public void setPmayId(String pmayId) {
        this.pmayId = pmayId;
    }


    public String getBeneficiaryName() {
        return BeneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        BeneficiaryName = beneficiaryName;
    }

    public String getHabitationName() {
        return HabitationName;
    }

    public void setHabitationName(String habitationName) {
        HabitationName = habitationName;
    }

    public String getSeccId() {
        return SeccId;
    }

    public void setSeccId(String seccId) {
        SeccId = seccId;
    }

    private String SeccId;


    private String PvCode;
    private String PvName;

    private String blockName;

    public String getTypeOfPhoto() {
        return typeOfPhoto;
    }

    public void setTypeOfPhoto(String typeOfPhoto) {
        this.typeOfPhoto = typeOfPhoto;
    }

    private String typeOfPhoto;
    private String imageRemark;
    private String dateTime;
    private String imageAvailable;



    public String getImageAvailable() {
        return imageAvailable;
    }

    public void setImageAvailable(String imageAvailable) {
        this.imageAvailable = imageAvailable;
    }



    public String getImageRemark() {
        return imageRemark;
    }

    public void setImageRemark(String imageRemark) {
        this.imageRemark = imageRemark;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }









    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }


    public String getPvName() {
        return PvName;
    }

    public void setPvName(String name) {
        PvName = name;
    }


    public String getDistictCode() {
        return distictCode;
    }

    public void setDistictCode(String distictCode) {
        this.distictCode = distictCode;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }





    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    private String Longitude;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    private Bitmap Image;



    public String getPvCode() {
        return PvCode;
    }

    public void setPvCode(String pvCode) {
        this.PvCode = pvCode;
    }



    //////New Nursery Tables Getter Setter

    //// Nursery User Details getter Setter
    private int nursery_id;
    private int nursery_level_id;
    private String nursery_name_en;
    private String nursery_name_ta;
    private String nursery_address;
    private String nursery_dname;
    private String nursery_bname;
    private String nursery_pvname;

    ///Master Table Land Type getter Setter
    private int land_type_id;
    private String land_type_name_en;
    private String land_type_name_ta;
    private byte[] imageInByte;
    private int nursery_land_id;
    private int land_primary_id;
    private String server_flag;
    private String land_address;

    public String getLand_address() {
        return land_address;
    }

    public void setLand_address(String land_address) {
        this.land_address = land_address;
    }

    public String getServer_flag() {
        return server_flag;
    }

    public void setServer_flag(String server_flag) {
        this.server_flag = server_flag;
    }

    public int getLand_primary_id() {
        return land_primary_id;
    }

    public void setLand_primary_id(int land_primary_id) {
        this.land_primary_id = land_primary_id;
    }

    public int getNursery_land_id() {
        return nursery_land_id;
    }

    public void setNursery_land_id(int nursery_land_id) {
        this.nursery_land_id = nursery_land_id;
    }

    public byte[] getImageInByte() {
        return imageInByte;
    }

    public void setImageInByte(byte[] imageInByte) {
        this.imageInByte = imageInByte;
    }

    public int getNursery_id() {
        return nursery_id;
    }

    public void setNursery_id(int nursery_id) {
        this.nursery_id = nursery_id;
    }

    public int getNursery_level_id() {
        return nursery_level_id;
    }

    public void setNursery_level_id(int nursery_level_id) {
        this.nursery_level_id = nursery_level_id;
    }

    public String getNursery_name_en() {
        return nursery_name_en;
    }

    public void setNursery_name_en(String nursery_name_en) {
        this.nursery_name_en = nursery_name_en;
    }

    public String getNursery_name_ta() {
        return nursery_name_ta;
    }

    public void setNursery_name_ta(String nursery_name_ta) {
        this.nursery_name_ta = nursery_name_ta;
    }

    public String getNursery_address() {
        return nursery_address;
    }

    public void setNursery_address(String nursery_address) {
        this.nursery_address = nursery_address;
    }

    public String getNursery_dname() {
        return nursery_dname;
    }

    public void setNursery_dname(String nursery_dname) {
        this.nursery_dname = nursery_dname;
    }

    public String getNursery_bname() {
        return nursery_bname;
    }

    public void setNursery_bname(String nursery_bname) {
        this.nursery_bname = nursery_bname;
    }

    public String getNursery_pvname() {
        return nursery_pvname;
    }

    public void setNursery_pvname(String nursery_pvname) {
        this.nursery_pvname = nursery_pvname;
    }

    public int getLand_type_id() {
        return land_type_id;
    }

    public void setLand_type_id(int land_type_id) {
        this.land_type_id = land_type_id;
    }

    public String getLand_type_name_en() {
        return land_type_name_en;
    }

    public void setLand_type_name_en(String land_type_name_en) {
        this.land_type_name_en = land_type_name_en;
    }

    public String getLand_type_name_ta() {
        return land_type_name_ta;
    }

    public void setLand_type_name_ta(String land_type_name_ta) {
        this.land_type_name_ta = land_type_name_ta;
    }

    ///Table For Batches And Species Getter Setter
    private int batch_primary_id;
    private int batch_id;
    private int batch_number;
    private String created_date;
    private String species_name_en;
    private String species_name_ta;
    private int species_type_id;
    private int batch_species_id;
    private int no_of_count;

    public int getBatch_species_id() {
        return batch_species_id;
    }

    public void setBatch_species_id(int batch_species_id) {
        this.batch_species_id = batch_species_id;
    }

    public String getSpecies_name_en() {
        return species_name_en;
    }

    public void setSpecies_name_en(String species_name_en) {
        this.species_name_en = species_name_en;
    }

    public String getSpecies_name_ta() {
        return species_name_ta;
    }

    public void setSpecies_name_ta(String species_name_ta) {
        this.species_name_ta = species_name_ta;
    }

    public int getBatch_primary_id() {
        return batch_primary_id;
    }

    public void setBatch_primary_id(int batch_primary_id) {
        this.batch_primary_id = batch_primary_id;
    }

    public int getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(int batch_id) {
        this.batch_id = batch_id;
    }

    public int getBatch_number() {
        return batch_number;
    }

    public void setBatch_number(int batch_number) {
        this.batch_number = batch_number;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public int getSpecies_type_id() {
        return species_type_id;
    }

    public void setSpecies_type_id(int species_type_id) {
        this.species_type_id = species_type_id;
    }

    public int getNo_of_count() {
        return no_of_count;
    }

    public void setNo_of_count(int no_of_count) {
        this.no_of_count = no_of_count;
    }
}