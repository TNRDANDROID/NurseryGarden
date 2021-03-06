package com.nic.nurserygarden.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.model.NurserySurvey;

import java.util.List;

/**
 * Created by shanmugapriyan on 25/05/16.
 */
public class CommonAdapter extends BaseAdapter {
    private List<NurserySurvey> pmgsySurveys;
    private Context mContext;
    private String type;


    public CommonAdapter(Context mContext, List<NurserySurvey> pmgsySurvey, String type) {
        this.pmgsySurveys = pmgsySurvey;
        this.mContext = mContext;
        this.type = type;
    }


    public int getCount() {
        return pmgsySurveys.size();
    }


    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.spinner_drop_down_item, parent, false);
//        TextView tv_type = (TextView) view.findViewById(R.id.tv_spinner_item);
        View view = inflater.inflate(R.layout.spinner_value, parent, false);
        TextView tv_type = (TextView) view.findViewById(R.id.spinner_list_value);
        NurserySurvey pmgsySurvey = pmgsySurveys.get(position);

        if (type.equalsIgnoreCase("BlockList")) {
            tv_type.setText(pmgsySurvey.getBlockName());
        } else if (type.equalsIgnoreCase("VillageList")) {
            tv_type.setText(pmgsySurvey.getPvName());
        } else if (type.equalsIgnoreCase("HabitationList")) {
            tv_type.setText(pmgsySurvey.getHabitationName());
        }
        else if (type.equalsIgnoreCase("self_GroupList")) {
            tv_type.setText(pmgsySurvey.getShg_name());
        }
        else if (type.equalsIgnoreCase("fin_Year_List")) {
            tv_type.setText(pmgsySurvey.getFin_year());
        }
        else if (type.equalsIgnoreCase("type_tree_List")) {
            tv_type.setText(pmgsySurvey.getWork_name());
        }

        else if (type.equalsIgnoreCase("landTypeList")) {
            tv_type.setText(pmgsySurvey.getLand_type_name_en());
        }
        else if (type.equalsIgnoreCase("speciesTypeList")) {
            tv_type.setText(pmgsySurvey.getSpecies_name_en());
        }
        else if (type.equalsIgnoreCase("deadStageList")) {
            tv_type.setText(pmgsySurvey.getDead_stage_name_en());
        }
        else if (type.equalsIgnoreCase("nurseryBuyerTypeList")) {
            tv_type.setText(pmgsySurvey.getBuyer_type_name_en());
        }
        else if (type.equalsIgnoreCase("expenditureTypeList")) {
            tv_type.setText(pmgsySurvey.getExpense_category_en());
        }
        else if (type.equalsIgnoreCase("expenditureUnitList")) {
            tv_type.setText(pmgsySurvey.getExpenditure_unit_en());
        }
        else if (type.equalsIgnoreCase("expenditureFoundSrcList")) {
            tv_type.setText(pmgsySurvey.getExpenditure_fund_src_en());
        }
        else if (type.equalsIgnoreCase("waterSourceTypeList")) {
            tv_type.setText(pmgsySurvey.getWater_source_type_name());
        }
        else if (type.equalsIgnoreCase("fencingTypeList")) {
            tv_type.setText(pmgsySurvey.getFencing_type_name());
        }
        else {
            tv_type.setText("");
        }
        return view;
    }

}