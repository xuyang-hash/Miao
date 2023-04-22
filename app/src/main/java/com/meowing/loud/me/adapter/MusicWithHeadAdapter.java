package com.meowing.loud.me.adapter;

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
        CardView cslMusicLayout = baseViewHolder.getView(R.id.csl_music_info);

        if (!StringUtils.isStringNULL(musicResp.getHeadString())) {
            ivMusicHead.setImageBitmap(ArmsUtils.toBitmapFromString(musicResp.getHeadString()));
        }

        if (!StringUtils.isStringNULL(musicResp.getName())) {
            tvMusicName.setText(musicResp.getName());
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
