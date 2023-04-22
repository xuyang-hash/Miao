package com.meowing.loud.home.view.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meowing.loud.arms.base.BaseFragment;
import com.meowing.loud.arms.constant.AppConstant;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.manager.LocalDataManager;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.FragmentHomeBinding;
import com.meowing.loud.home.adapter.MusicSimpleAdapter;
import com.meowing.loud.home.contract.HomeContract;
import com.meowing.loud.home.di.component.DaggerHomeComponent;
import com.meowing.loud.home.di.module.HomeModule;
import com.meowing.loud.home.presenter.HomePresenter;
import com.meowing.loud.play.view.activity.PlayActivity;

import java.util.List;

public class WaitMusicFragment extends BaseFragment<FragmentHomeBinding, HomePresenter> implements HomeContract.View, MusicSimpleAdapter.Listener{
    private List<MusicResp> musicRespList;

    private MusicSimpleAdapter musicAdapter;

    public static CollectFragment newInstance() {
        return new CollectFragment();
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
        musicAdapter = new MusicSimpleAdapter();
        musicAdapter.setListener(this);
        binding.ryMusicList.setAdapter(musicAdapter);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        showLoading();
        mPresenter.findAllWaitMusicData();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void findAllWaitMusicResult() {
        musicRespList = LocalDataManager.getInstance().getAllWaitMusicList();
        musicAdapter.setList(musicRespList);
    }

    @Override
    public void onItemClickListener(int position) {
        PlayActivity.start(getContext(), AppConstant.MUSIC_TYPE_WAIT, position);
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
