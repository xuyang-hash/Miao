package com.meowing.loud.home.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.meowing.loud.R;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.utils.ArmsUtils;
import com.meowing.loud.arms.utils.MeoSPUtil;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.arms.utils.ToastUtils;

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
        ImageView ivGood = baseViewHolder.getView(R.id.iv_music_good);
        TextView tvGoodNum = baseViewHolder.getView(R.id.tv_music_good_num);
        ImageView ivLike = baseViewHolder.getView(R.id.iv_music_like);
        TextView tvLikeNum = baseViewHolder.getView(R.id.tv_music_like_num);
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

        if (!StringUtils.isStringNULL(musicResp.getName())) {
            tvMusicName.setText(musicResp.getName());
        }
        ivGood.setSelected(musicResp.isGoodContainMe());
        tvGoodNum.setText(String.valueOf(musicResp.getGoodNum()));
        ivLike.setSelected(musicResp.isLikeContainMe());
        tvLikeNum.setText(String.valueOf(musicResp.getLikeNum()));

        cslMusicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(baseViewHolder.getPosition(), musicResp);
            }
        });
        ivGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MeoSPUtil.isUserLogin()) {
                    ivGood.setSelected(!ivGood.isSelected());
                    listener.updateGoodState(baseViewHolder.getPosition(), ivGood.isSelected());
                } else {
                    ToastUtils.showShort(getContext(), R.string.account_admin_good_like_tips);
                }
            }
        });

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MeoSPUtil.isUserLogin()) {
                    ivLike.setSelected(!ivLike.isSelected());
                    listener.updateLikeState(baseViewHolder.getPosition(), ivLike.isSelected());
                } else {
                    ToastUtils.showShort(getContext(), R.string.account_admin_good_like_tips);
                }
            }
        });
    }

    public interface Listener {
        /**
         * 更新点赞状态
         *
         * @param isAdd 点赞，false则为取消点赞
         */
        void updateGoodState(int position, boolean isAdd);

        /**
         * 更新收藏状态
         * @param isLike 收藏，false则为取消收藏
         */
        void updateLikeState(int position, boolean isLike);

        void onItemClickListener(int position, MusicResp resp);
    }
}
