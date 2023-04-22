package com.meowing.loud.me.view.Fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseFragment;
import com.meowing.loud.arms.constant.AppConstant;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.manager.LocalDataManager;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.resp.UserResp;
import com.meowing.loud.arms.utils.ArmsUtils;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.databinding.FragmentUserBinding;
import com.meowing.loud.me.adapter.MusicWithHeadAdapter;
import com.meowing.loud.me.contract.UserContract;
import com.meowing.loud.me.di.component.DaggerUserComponent;
import com.meowing.loud.me.di.module.UserModule;
import com.meowing.loud.me.presenter.UserPresenter;
import com.meowing.loud.me.view.Activity.UserCenterActivity;
import com.meowing.loud.play.view.activity.PlayActivity;

import java.util.List;

public class UserFragment extends BaseFragment<FragmentUserBinding, UserPresenter> implements UserContract.View, View.OnClickListener, MusicWithHeadAdapter.Listener{

    private UserResp userResp;

    private List<MusicResp> allMineMusicList;

    private MusicWithHeadAdapter adapter;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerUserComponent
                .builder()
                .appComponent(appComponent)
                .userModule(new UserModule(this))//请将HomeModule()第一个首字母改为小写
                .build()
                .inject(this);
    }


    @Override
    public void initView(View mView) {
        userResp = LocalDataManager.getInstance().getUserInfo();
        if (!StringUtils.isStringNULL(userResp.getImageString())) {
            binding.ivAccountHead.setImageBitmap(ArmsUtils.toBitmapFromString(userResp.getImageString()));
        }
        binding.tvAccountUsername.setText(userResp.getUsername());
        binding.ivAccountSetting.setOnClickListener(this);

        adapter = new MusicWithHeadAdapter();
        adapter.setListener(this);
        binding.ryMusicList.setAdapter(adapter);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        showLoading();
        mPresenter.findAllMineMusic();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_account_setting) {
            UserCenterActivity.start(getContext());
        }
    }

    @Override
    public void findAllMineMusicResult(boolean isSuccess) {
        if (isSuccess) {
            allMineMusicList = LocalDataManager.getInstance().getAllMineMusicList();
            adapter.setList(allMineMusicList);
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void onItemClickListener(int position) {
        PlayActivity.start(getContext(), AppConstant.MUSIC_TYPE_MINE, position);
    }
}
