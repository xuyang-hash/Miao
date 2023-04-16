package com.meowing.loud.home.view.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseFragment;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.manager.LocalDataManager;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.FragmentHomeBinding;
import com.meowing.loud.home.adapter.MusicAdapter;
import com.meowing.loud.home.contract.HomeContract;
import com.meowing.loud.home.di.component.DaggerHomeComponent;
import com.meowing.loud.home.di.module.HomeModule;
import com.meowing.loud.home.presenter.HomePresenter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomePresenter> implements HomeContract.View, MusicAdapter.Listener{

    private MusicAdapter musicAdapter;

    private List<MusicResp> musicRespList = new ArrayList<>();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))//请将HomeModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public void initView(View mView) {
        musicAdapter = new MusicAdapter();
        musicAdapter.setListener(this);
        binding.ryMusicList.setAdapter(musicAdapter);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//        showLoading();
//        mPresenter.findAllMusicData();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void findAllMusicSuccess() {
        this.musicRespList.addAll(LocalDataManager.getInstance().getMusicRespList());
        musicAdapter.setList(LocalDataManager.getInstance().getMusicRespList());
    }

    @Override
    public void updateGoodState(int position, boolean isAdd) {
        MusicResp musicResp = musicRespList.get(position);
        String username = LocalDataManager.getInstance().getUserInfo().getUsername();
        if (isAdd) {
            musicResp.addGood(username);
        } else {
            musicResp.delGood(username);
        }
        musicAdapter.notifyItemChanged(position);
        mPresenter.updateMusicGood(username, musicResp, isAdd);
    }

    @Override
    public void onItemClickListener(int position, MusicResp resp) {
        //todo: 跳转到音乐播放界面
    }

    @Override
    public void updateMusicGoodResult(boolean isSuccess, MusicResp musicResp, boolean isAdd) {
        if (isSuccess) {
            ToastUtils.showShort(getContext(), isAdd ? R.string.music_good_add_success : R.string.music_good_cancel_success);
        } else {
            ToastUtils.showShort(getContext(), isAdd ? R.string.music_good_add_failed : R.string.music_good_cancel_failed);
            if (isAdd) {
                musicResp.delGood(LocalDataManager.getInstance().getUserInfo().getUsername());
            } else {
                musicResp.addGood(LocalDataManager.getInstance().getUserInfo().getUsername());
            }
            musicAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showLoading() {
        if (getContext() != null) {
            CSeeLoadingDialog.getInstance(getContext()).show();
        }
    }

    @Override
    public void hideLoading() {
        if (getContext() != null) {
            CSeeLoadingDialog.getInstance(getContext()).dismiss();
        }
    }

    @Override
    public void error(String msg) {
        hideLoading();
        ToastUtils.showShort(getContext(), msg);
    }
}
