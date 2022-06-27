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
    private String pia_members_count;
    private String pia_type_name_en;
    private String pia_type_name_ta;
    private String pia_name_en;
    private String pia_name_ta;


    ///Master Table Land Type getter Setter
    private int land_type_id;
    private String land_type_name_en;
    private String land_type_name_ta;
    private byte[] imageInByte;
    private int nursery_land_id;
    private int land_primary_id;
    private String server_flag;
    private String land_address;
    private String other_land_type;
    private String area;
    private String survey_number;
    private String sub_div_no;
    private String is_batch_closed;

    public String getPia_type_name_ta() {
        return pia_type_name_ta;
    }

    public void setPia_type_name_ta(String pia_type_name_ta) {
        this.pia_type_name_ta = pia_type_name_ta;
    }

    public String getPia_name_ta() {
        return pia_name_ta;
    }

    public void setPia_name_ta(String pia_name_ta) {
        this.pia_name_ta = pia_name_ta;
    }

    public String getPia_members_count() {
        return pia_members_count;
    }

    public void setPia_members_count(String pia_members_count) {
        this.pia_members_count = pia_members_count;
    }

    public String getPia_type_name_en() {
        return pia_type_name_en;
    }

    public void setPia_type_name_en(String pia_type_name_en) {
        this.pia_type_name_en = pia_type_name_en;
    }

    public String getPia_name_en() {
        return pia_name_en;
    }

    public void setPia_name_en(String pia_name_en) {
        this.pia_name_en = pia_name_en;
    }

    public String getOther_land_type() {
        return other_land_type;
    }

    public NurserySurvey setOther_land_type(String other_land_type) {
        this.other_land_type = other_land_type;
        return this;
    }

    public String getArea() {
        return area;
    }

    public NurserySurvey setArea(String area) {
        this.area = area;
        return this;
    }

    public String getSurvey_number() {
        return survey_number;
    }

    public NurserySurvey setSurvey_number(String survey_number) {
        this.survey_number = survey_number;
        return this;
    }

    public String getSub_div_no() {
        return sub_div_no;
    }

    public NurserySurvey setSub_div_no(String sub_div_no) {
        this.sub_div_no = sub_div_no;
        return this;
    }

    public String getIs_batch_closed() {
        return is_batch_closed;
    }

    public void setIs_batch_closed(String is_batch_closed) {
        this.is_batch_closed = is_batch_closed;
    }

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
    private String is_harvest_closed;
    private int growth_tracking_details_id;

    private int dead_stage_id;
    private String dead_stage_name_en;
    private String dead_stage_name_ta;
    private String dead_reason;
    private int no_of_dead_sapling;

    public String getDead_reason() {
        return dead_reason;
    }

    public void setDead_reason(String dead_reason) {
        this.dead_reason = dead_reason;
    }

    public int getNo_of_dead_sapling() {
        return no_of_dead_sapling;
    }

    public void setNo_of_dead_sapling(int no_of_dead_sapling) {
        this.no_of_dead_sapling = no_of_dead_sapling;
    }

    public int getDead_stage_id() {
        return dead_stage_id;
    }

    public void setDead_stage_id(int dead_stage_id) {
        this.dead_stage_id = dead_stage_id;
    }

    public String getDead_stage_name_en() {
        return dead_stage_name_en;
    }

    public void setDead_stage_name_en(String dead_stage_name_en) {
        this.dead_stage_name_en = dead_stage_name_en;
    }

    public String getDead_stage_name_ta() {
        return dead_stage_name_ta;
    }

    public void setDead_stage_name_ta(String dead_stage_name_ta) {
        this.dead_stage_name_ta = dead_stage_name_ta;
    }

    public int getGrowth_tracking_details_id() {
        return growth_tracking_details_id;
    }

    public void setGrowth_tracking_details_id(int growth_tracking_details_id) {
        this.growth_tracking_details_id = growth_tracking_details_id;
    }

    public String getIs_harvest_closed() {
        return is_harvest_closed;
    }

    public void setIs_harvest_closed(String is_harvest_closed) {
        this.is_harvest_closed = is_harvest_closed;
    }

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

    ////Tracking Growth Getter Setter
    private int growth_tracking_id;
    private int batch_growth_tracking_primary_id;
    private int no_of_saplings;
    private String height_in_cm;
    private int age_in_days;

    public int getGrowth_tracking_id() {
        return growth_tracking_id;
    }

    public void setGrowth_tracking_id(int growth_tracking_id) {
        this.growth_tracking_id = growth_tracking_id;
    }

    public int getBatch_growth_tracking_primary_id() {
        return batch_growth_tracking_primary_id;
    }

    public void setBatch_growth_tracking_primary_id(int batch_growth_tracking_primary_id) {
        this.batch_growth_tracking_primary_id = batch_growth_tracking_primary_id;
    }

    public int getNo_of_saplings() {
        return no_of_saplings;
    }

    public void setNo_of_saplings(int no_of_saplings) {
        this.no_of_saplings = no_of_saplings;
    }

    public String getHeight_in_cm() {
        return height_in_cm;
    }

    public void setHeight_in_cm(String height_in_cm) {
        this.height_in_cm = height_in_cm;
    }

    public int getAge_in_days() {
        return age_in_days;
    }

    public void setAge_in_days(int age_in_days) {
        this.age_in_days = age_in_days;
    }

    private int buyer_type_id;
    private String buyer_type_name_en;
    private String buyer_type_name_ta;
    private String buyer_name;
    private String buyer_mobile_no;
    private String buyer_address;
    int no_of_required_saplings;

    public String getBuyer_mobile_no() {
        return buyer_mobile_no;
    }

    public void setBuyer_mobile_no(String buyer_mobile_no) {
        this.buyer_mobile_no = buyer_mobile_no;
    }

    public String getBuyer_address() {
        return buyer_address;
    }

    public void setBuyer_address(String buyer_address) {
        this.buyer_address = buyer_address;
    }

    public int getNo_of_required_saplings() {
        return no_of_required_saplings;
    }

    public void setNo_of_required_saplings(int no_of_required_saplings) {
        this.no_of_required_saplings = no_of_required_saplings;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }



    public int getBuyer_type_id() {
        return buyer_type_id;
    }

    public void setBuyer_type_id(int buyer_type_id) {
        this.buyer_type_id = buyer_type_id;
    }

    public String getBuyer_type_name_en() {
        return buyer_type_name_en;
    }

    public void setBuyer_type_name_en(String buyer_type_name_en) {
        this.buyer_type_name_en = buyer_type_name_en;
    }

    public String getBuyer_type_name_ta() {
        return buyer_type_name_ta;
    }

    public void setBuyer_type_name_ta(String buyer_type_name_ta) {
        this.buyer_type_name_ta = buyer_type_name_ta;
    }

    private int dead_stage_position;
    private String dead_reason_text;
    private String dead_typed_count_text;
    private String sell_sapling_typed_count_text;

    public String getSell_sapling_typed_count_text() {
        return sell_sapling_typed_count_text;
    }

    public void setSell_sapling_typed_count_text(String sell_sapling_typed_count_text) {
        this.sell_sapling_typed_count_text = sell_sapling_typed_count_text;
    }

    public String getDead_reason_text() {
        return dead_reason_text;
    }

    public void setDead_reason_text(String dead_reason_text) {
        this.dead_reason_text = dead_reason_text;
    }

    public String getDead_typed_count_text() {
        return dead_typed_count_text;
    }

    public void setDead_typed_count_text(String dead_typed_count_text) {
        this.dead_typed_count_text = dead_typed_count_text;
    }

    public int getDead_stage_position() {
        return dead_stage_position;
    }

    public void setDead_stage_position(int dead_stage_position) {
        this.dead_stage_position = dead_stage_position;
    }

    /////Order History
    private int sapling_order_id;
    private int sapling_order_item_id;
    private int buyer_id;
    private String order_date;
    private String mgnregs_id;
    private int sapling_item_delivery_id;

    public int getSapling_item_delivery_id() {
        return sapling_item_delivery_id;
    }

    public void setSapling_item_delivery_id(int sapling_item_delivery_id) {
        this.sapling_item_delivery_id = sapling_item_delivery_id;
    }

    public int getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getSapling_order_id() {
        return sapling_order_id;
    }

    public void setSapling_order_id(int sapling_order_id) {
        this.sapling_order_id = sapling_order_id;
    }

    public int getSapling_order_item_id() {
        return sapling_order_item_id;
    }

    public void setSapling_order_item_id(int sapling_order_item_id) {
        this.sapling_order_item_id = sapling_order_item_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getMgnregs_id() {
        return mgnregs_id;
    }

    public void setMgnregs_id(String mgnregs_id) {
        this.mgnregs_id = mgnregs_id;
    }

    private int expenditure_primary_id;
    private int expenditure_type_id;
    private int expense_category_id;
    private int expenditure_fund_src_id;
    private int expenditure_unit_id;
    private String expense_category_en;
    private String expense_category_ta;
    private String is_others;
    private String expenditure_fund_src_en;
    private String expenditure_fund_src_ta;
    private String expenditure_unit_en;
    private String expense_category_others;
    private String expenditure_unit_others;
    private String expenditure_fund_src_others;
    private String quantity;
    private String amount;
    private String expense_category_name="";
    private String expense_unit_name="";
    private String expense_found_src_name="";
    private String expenditure_type="";

    public String getExpenditure_type() {
        return expenditure_type;
    }

    public void setExpenditure_type(String expenditure_type) {
        this.expenditure_type = expenditure_type;
    }

    public String getExpense_category_name() {
        return expense_category_name;
    }

    public void setExpense_category_name(String expense_category_name) {
        this.expense_category_name = expense_category_name;
    }

    public String getExpense_unit_name() {
        return expense_unit_name;
    }

    public void setExpense_unit_name(String expense_unit_name) {
        this.expense_unit_name = expense_unit_name;
    }

    public String getExpense_found_src_name() {
        return expense_found_src_name;
    }

    public void setExpense_found_src_name(String expense_found_src_name) {
        this.expense_found_src_name = expense_found_src_name;
    }

    public String getExpense_category_others() {
        return expense_category_others;
    }

    public void setExpense_category_others(String expense_category_others) {
        this.expense_category_others = expense_category_others;
    }

    public String getExpenditure_unit_others() {
        return expenditure_unit_others;
    }

    public void setExpenditure_unit_others(String expenditure_unit_others) {
        this.expenditure_unit_others = expenditure_unit_others;
    }

    public String getExpenditure_fund_src_others() {
        return expenditure_fund_src_others;
    }

    public void setExpenditure_fund_src_others(String expenditure_fund_src_others) {
        this.expenditure_fund_src_others = expenditure_fund_src_others;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    private String entry_date;

    public int getExpenditure_primary_id() {
        return expenditure_primary_id;
    }

    public void setExpenditure_primary_id(int expenditure_primary_id) {
        this.expenditure_primary_id = expenditure_primary_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getExpenditure_type_id() {
        return expenditure_type_id;
    }

    public void setExpenditure_type_id(int expenditure_type_id) {
        this.expenditure_type_id = expenditure_type_id;
    }

    public int getExpense_category_id() {
        return expense_category_id;
    }

    public void setExpense_category_id(int expense_category_id) {
        this.expense_category_id = expense_category_id;
    }

    public int getExpenditure_fund_src_id() {
        return expenditure_fund_src_id;
    }

    public void setExpenditure_fund_src_id(int expenditure_fund_src_id) {
        this.expenditure_fund_src_id = expenditure_fund_src_id;
    }

    public int getExpenditure_unit_id() {
        return expenditure_unit_id;
    }

    public void setExpenditure_unit_id(int expenditure_unit_id) {
        this.expenditure_unit_id = expenditure_unit_id;
    }

    public String getExpense_category_en() {
        return expense_category_en;
    }

    public void setExpense_category_en(String expense_category_en) {
        this.expense_category_en = expense_category_en;
    }

    public String getExpense_category_ta() {
        return expense_category_ta;
    }

    public void setExpense_category_ta(String expense_category_ta) {
        this.expense_category_ta = expense_category_ta;
    }

    public String getIs_others() {
        return is_others;
    }

    public void setIs_others(String is_others) {
        this.is_others = is_others;
    }

    public String getExpenditure_fund_src_en() {
        return expenditure_fund_src_en;
    }

    public void setExpenditure_fund_src_en(String expenditure_fund_src_en) {
        this.expenditure_fund_src_en = expenditure_fund_src_en;
    }

    public String getExpenditure_fund_src_ta() {
        return expenditure_fund_src_ta;
    }

    public void setExpenditure_fund_src_ta(String expenditure_fund_src_ta) {
        this.expenditure_fund_src_ta = expenditure_fund_src_ta;
    }

    public String getExpenditure_unit_en() {
        return expenditure_unit_en;
    }

    public void setExpenditure_unit_en(String expenditure_unit_en) {
        this.expenditure_unit_en = expenditure_unit_en;
    }

    public String getExpenditure_unit_ta() {
        return expenditure_unit_ta;
    }

    public void setExpenditure_unit_ta(String expenditure_unit_ta) {
        this.expenditure_unit_ta = expenditure_unit_ta;
    }

    private String expenditure_unit_ta;
    private String fencing_type_id;
    private String fencing_type_name;
    private String water_source_type_id;
    private String water_source_type_name;
    private String other_fencing_type_name;
    private String other_water_source_type_name;

    public String getOther_fencing_type_name() {
        return other_fencing_type_name;
    }

    public void setOther_fencing_type_name(String other_fencing_type_name) {
        this.other_fencing_type_name = other_fencing_type_name;
    }

    public String getOther_water_source_type_name() {
        return other_water_source_type_name;
    }

    public void setOther_water_source_type_name(String other_water_source_type_name) {
        this.other_water_source_type_name = other_water_source_type_name;
    }

    public String getFencing_type_id() {
        return fencing_type_id;
    }

    public void setFencing_type_id(String fencing_type_id) {
        this.fencing_type_id = fencing_type_id;
    }

    public String getFencing_type_name() {
        return fencing_type_name;
    }

    public void setFencing_type_name(String fencing_type_name) {
        this.fencing_type_name = fencing_type_name;
    }

    public String getWater_source_type_id() {
        return water_source_type_id;
    }

    public void setWater_source_type_id(String water_source_type_id) {
        this.water_source_type_id = water_source_type_id;
    }

    public String getWater_source_type_name() {
        return water_source_type_name;
    }

    public void setWater_source_type_name(String water_source_type_name) {
        this.water_source_type_name = water_source_type_name;
    }
}