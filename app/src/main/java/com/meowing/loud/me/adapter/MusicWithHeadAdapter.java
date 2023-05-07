package com.meowing.loud.me.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.meowing.loud.R;
import com.meowing.loud.arms.constant.AppConstant;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.utils.ArmsUtils;
import com.meowing.loud.arms.utils.StringUtils;

public class MusicWithHeadAdapter extends BaseQuickAdapter<MusicResp, BaseViewHolder> {

    private Listener listener;

    public MusicWithHeadAdapter() {
        super(R.layout.ry_music_list_item_with_head_layout);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MusicResp musicResp) {
        ImageView ivMusicHead = baseViewHolder.getView(R.id.iv_music_head);
        TextView tvMusicName = baseViewHolder.getView(R.id.tv_music_name);
        ConstraintLayout cslMusicLayout = baseViewHolder.getView(R.id.csl_music_info);
        LinearLayout llGoodLike = baseViewHolder.getView(R.id.ll_good_user);
        TextView tvGoodNum = baseViewHolder.getView(R.id.tv_good);
        TextView tvLikeNum = baseViewHolder.getView(R.id.tv_like);
        TextView tvState = baseViewHolder.getView(R.id.tv_state);

        if (!StringUtils.isStringNULL(musicResp.getHeadString())) {
            ivMusicHead.setImageBitmap(ArmsUtils.toBitmapFromString(musicResp.getHeadString()));
        }

        if (!StringUtils.isStringNULL(musicResp.getName())) {
            tvMusicName.setText(musicResp.getName());
        }

        if (musicResp.getState() == AppConstant.MUSIC_TYPE_PASS) {
            llGoodLike.setVisibility(View.VISIBLE);
            tvState.setVisibility(View.GONE);

            int goodNum = musicResp.getGoodNum();
            tvGoodNum.setText("+" + goodNum);
            int likeNum = musicResp.getLikeNum();
            tvLikeNum.setText("+" + likeNum);
        } else {
            llGoodLike.setVisibility(View.GONE);
            tvState.setVisibility(View.VISIBLE);
            if (musicResp.getState() == AppConstant.MUSIC_TYPE_WAIT) {
                tvState.setText(R.string.music_state_wait_title);
                tvState.setTextColor(getContext().getResources().getColor(R.color.music_state_wait));
            } else if (musicResp.getState() == AppConstant.MUSIC_TYPE_REFUSE) {
                tvState.setText(R.string.music_state_refuse_title);
                tvState.setTextColor(getContext().getResources().getColor(R.color.music_state_refuse));
            }
        }

        cslMusicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(baseViewHolder.getPosition());
            }
        });
    }

    public interface Listener {

        void onItemClickListener(int position);
    }
}
