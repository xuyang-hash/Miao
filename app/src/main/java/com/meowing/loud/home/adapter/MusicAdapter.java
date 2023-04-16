package com.meowing.loud.home.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.meowing.loud.R;
import com.meowing.loud.arms.constant.MMKConstant;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.utils.ArmsUtils;
import com.meowing.loud.arms.utils.MeoSPUtil;
import com.meowing.loud.arms.utils.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MusicAdapter extends BaseQuickAdapter<MusicResp, BaseViewHolder> {

    private Listener listener;

    public MusicAdapter() {
        super(R.layout.ry_music_list_item_layout);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MusicResp musicResp) {
        TextView tvUserName = baseViewHolder.getView(R.id.tv_user_name);
        ImageView ivUserHead = baseViewHolder.getView(R.id.iv_user_head);
        ImageView ivMusicHead = baseViewHolder.getView(R.id.iv_music_head);
        TextView tvMusicName = baseViewHolder.getView(R.id.tv_music_name);
        TextView tvMusicTime = baseViewHolder.getView(R.id.tv_music_time);
        ImageView ivGood = baseViewHolder.getView(R.id.iv_music_good);
        TextView tvGoodNum = baseViewHolder.getView(R.id.tv_music_good_num);
        CardView cslMusicLayout = baseViewHolder.getView(R.id.csl_music_info);

        if (!StringUtils.isStringNULL(musicResp.getUsername())) {
            tvUserName.setText(musicResp.getUsername());
        }

        if (!StringUtils.isStringNULL(musicResp.getUserHeadString())) {
            ivUserHead.setImageBitmap(ArmsUtils.toBitmapFromString(musicResp.getUserHeadString()));
        }

        if (!StringUtils.isStringNULL(musicResp.getHeadString())) {
            ivMusicHead.setImageBitmap(ArmsUtils.toBitmapFromString(musicResp.getHeadString()));
        }

        if (musicResp.getUpDate() != null) {
            DateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm");
            String upDate = sdf.format(musicResp.getUpDate());
            tvMusicTime.setText(upDate);
        }

        if (!StringUtils.isStringNULL(musicResp.getName())) {
            tvMusicName.setText(musicResp.getName());
        }
        ivGood.setSelected(musicResp.isGood(MeoSPUtil.getString(MMKConstant.LOGIN_USER_NAME)));
        tvGoodNum.setText(String.valueOf(musicResp.getGoodNum()));

        cslMusicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(baseViewHolder.getPosition(), musicResp);
            }
        });
        ivGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.updateGoodState(baseViewHolder.getPosition(), ivGood.isSelected());
            }
        });
    }

    public interface Listener {
        /**
         * 更新点赞状态
         * @param isAdd 点赞，false则为取消点赞
         */
        void updateGoodState(int position, boolean isAdd);

        void onItemClickListener(int position, MusicResp resp);
    }
}
