package com.nic.nurserygarden.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.databinding.OrderBasicHistoryItemViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewAdapter extends RecyclerView.Adapter<HistoryViewAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<NurserySurvey> historyItemList;
    private List<NurserySurvey> historyItemDetailsList;
    private Context context;
    private int pos=-1;
    private String type;
    private String which_class;
    private Animation fadeIn_out;

    private HistoryDetailsViewAdapter historyDetailsViewAdapter;
    public HistoryViewAdapter(List<NurserySurvey> historyItemList,List<NurserySurvey> historyItemDetailsList, Context context, String type, String which_class) {
        this.historyItemList = historyItemList;
        this.historyItemDetailsList = historyItemDetailsList;
        this.context = context;
        this.type=type;
        this.which_class=which_class;
        fadeIn_out = new AlphaAnimation(1,0);
        fadeIn_out.reset();
    }

    @NonNull
    @Override
    public HistoryViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        OrderBasicHistoryItemViewBinding orderBasicHistoryItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.order_basic_history_item_view, viewGroup, false);
        return new MyViewHolder(orderBasicHistoryItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryViewAdapter.MyViewHolder holder, final int position) {

       /* if(position ==0){
            holder.historyItemViewBinding.titleLayout.setVisibility(View.VISIBLE);
        }
        else {
            holder.historyItemViewBinding.titleLayout.setVisibility(View.GONE);
        }*/
        if(pos==position){
            holder.historyItemViewBinding.historyDetailsLayout.setVisibility(View.VISIBLE);
        }
        else {
            holder.historyItemViewBinding.historyDetailsLayout.setVisibility(View.GONE);
        }
        holder.historyItemViewBinding.date.setText(""+historyItemList.get(position).getOrder_date());
        holder.historyItemViewBinding.orderId.setText(""+historyItemList.get(position).getSapling_order_id());
        if(historyItemList.get(position).getBuyer_type_id()==3){
            holder.historyItemViewBinding.mgOrBuyerLayout.setBackgroundColor(context.getResources().getColor(R.color.green_combo));
            holder.historyItemViewBinding.buyreOrMgnregsId.setText("MGNREGS");
            holder.historyItemViewBinding.buyreOrMgnregsIdValue.setText(""+historyItemList.get(position).getMgnregs_id());
        }
        else {
            holder.historyItemViewBinding.mgOrBuyerLayout.setBackgroundColor(context.getResources().getColor(R.color.gradStart));
            holder.historyItemViewBinding.buyreOrMgnregsId.setText(context.getResources().getString(R.string.buyer_name));
            holder.historyItemViewBinding.buyreOrMgnregsIdValue.setText(""+historyItemList.get(position).getBuyer_name());
        }
        int sapling_order_id = historyItemList.get(position).getSapling_order_id();
        ArrayList<NurserySurvey> filterList = new ArrayList<>();
        for (int i = 0; i < historyItemDetailsList.size(); i++) {
            if (sapling_order_id == historyItemDetailsList.get(i).getSapling_order_id()) {
                NurserySurvey nurserySurvey1 = new NurserySurvey();
                nurserySurvey1.setSapling_order_id(historyItemDetailsList.get(i).getSapling_order_id());

                nurserySurvey1.setSapling_item_delivery_id(historyItemDetailsList.get(i).getSapling_item_delivery_id());
                nurserySurvey1.setSpecies_name_en(historyItemDetailsList.get(i).getSpecies_name_en());
                nurserySurvey1.setSpecies_name_ta(historyItemDetailsList.get(i).getSpecies_name_ta());
                nurserySurvey1.setNo_of_saplings(historyItemDetailsList.get(i).getNo_of_saplings());
                nurserySurvey1.setHeight_in_cm(historyItemDetailsList.get(i).getHeight_in_cm());
                nurserySurvey1.setAge_in_days(historyItemDetailsList.get(i).getAge_in_days());

                filterList.add(nurserySurvey1);

            }
        }
        if (filterList.size() > 0) {
            holder.historyItemViewBinding.detailsRecycler.setLayoutManager(new LinearLayoutManager(context));
            //RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context.getApplicationContext(),DividerItemDecoration.VERTICAL);
            // Get drawable object
           /* Drawable mDivider = ContextCompat.getDrawable(context, R.drawable.line_draw);
            // Create a DividerItemDecoration whose orientation is Horizontal
            DividerItemDecoration hItemDecoration = new DividerItemDecoration(context,
                    DividerItemDecoration.HORIZONTAL);
            // Set the drawable on it
            hItemDecoration.setDrawable(mDivider);*/
            //holder.historyItemViewBinding.detailsRecycler.addItemDecoration(itemDecoration);
            historyDetailsViewAdapter = new HistoryDetailsViewAdapter(filterList, context, "", which_class);
            holder.historyItemViewBinding.detailsRecycler.setAdapter(historyDetailsViewAdapter);
        }
        else {
            holder.historyItemViewBinding.detailsRecycler.setVisibility(View.GONE);
        }

        holder.historyItemViewBinding.viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos==position){
                    pos=-1;
                    fadeInAnimation(holder.historyItemViewBinding.historyDetailsLayout);
                    notifyDataSetChanged();
                }
                else {
                    fadeOutAnimation(holder.historyItemViewBinding.historyDetailsLayout);
                    pos = position;
                    //SlideToDown(holder.historyItemViewBinding.historyDetailsLayout);
                    notifyDataSetChanged();
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return historyItemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private OrderBasicHistoryItemViewBinding historyItemViewBinding;

        public MyViewHolder(OrderBasicHistoryItemViewBinding Binding) {
            super(Binding.getRoot());
            historyItemViewBinding = Binding;
        }
    }


    public void SlideToDown(RelativeLayout rl_footer) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 5.2f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        rl_footer.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                rl_footer.clearAnimation();

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        rl_footer.getWidth(), rl_footer.getHeight());
                lp.setMargins(0, rl_footer.getWidth(), 0, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rl_footer.setLayoutParams(lp);

            }

        });

    }
    /*public void fadeOutAnimation(View viewToFadeOut) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(viewToFadeOut, "alpha", 1f, 0f);

        fadeOut.setDuration(500);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // We wanna set the view to GONE, after it's fade out. so it actually disappear from the layout & don't take up space.
                viewToFadeOut.setVisibility(View.GONE);
            }
        });

        fadeOut.start();
    }
    */

    private void fadeInAnimation(final View view) {
        fadeIn_out.reset();
        fadeIn_out = new AlphaAnimation(0, 1);
        fadeIn_out.setInterpolator(new DecelerateInterpolator());
        fadeIn_out.setDuration(500);
        fadeIn_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(fadeIn_out);

    }
     private void fadeOutAnimation(final View view) {
         fadeIn_out.reset();
         fadeIn_out = new AlphaAnimation(1, 0);
         fadeIn_out.setInterpolator(new AccelerateInterpolator());
         fadeIn_out.setStartOffset(500);
         fadeIn_out.setDuration(500);
         fadeIn_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(fadeIn_out);

    }
    private void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private   void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}

