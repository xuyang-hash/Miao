package com.meowing.loud.arms.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lxj.xpopup.core.BubbleAttachPopupView;
import com.lxj.xpopup.widget.BubbleLayout;
import com.meowing.loud.R;
import com.meowing.loud.arms.utils.ArmsUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConditionTypeDialog extends BubbleAttachPopupView {

    private ConditionTypeListAdapter conditionTypeListAdapter;

    private RecyclerView ryList;
    private List<String> list = new ArrayList<>();

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClickListener(String s, int position);
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public ConditionTypeDialog(@NonNull Context context) {
        super(context);
        try {
            Field declaredField = BubbleAttachPopupView.class.getDeclaredField("bubbleContainer");
            declaredField.setAccessible(true);
            BubbleLayout bubbleLayout = (BubbleLayout) declaredField.get(this);
            bubbleLayout.setBubbleBorderColor(Color.parseColor("#14375aaa"));
            bubbleLayout.setBubbleBorderSize(2);
//            bubbleLayout.setBubbleImageBgRes(R.drawable.ic_bg_scene_menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setBubbleBgColor(getContext().getResources().getColor(R.color.white));
        this.setBubbleRadius(ArmsUtils.dip2px(context,6));
        this.setArrowHeight(25);
        this.setArrowWidth(20);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        ryList = findViewById(R.id.ry_room_list);
        conditionTypeListAdapter = new ConditionTypeListAdapter();
        conditionTypeListAdapter.addData(list);
        ryList.setAdapter(conditionTypeListAdapter);
    }


    private class ConditionTypeListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


        public ConditionTypeListAdapter() {
            super(R.layout.ry_common_list_item_layout);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            baseViewHolder.setText(R.id.tv_title, s);
            baseViewHolder.getView(R.id.tv_title)
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemClickListener != null) {
                                itemClickListener.onItemClickListener(s, baseViewHolder.getPosition());
                            }
                            notifyDataSetChanged();
                            dismiss();
                        }
                    });
            if (baseViewHolder.getPosition() == list.size() - 1) {
                baseViewHolder.getView(R.id.line).setVisibility(View.GONE);
            } else {
                baseViewHolder.getView(R.id.line).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_home_family_mode_layout;
    }
}

