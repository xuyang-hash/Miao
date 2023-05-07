package com.meowing.loud.home.view.fragment;

import static com.meowing.loud.arms.constant.ReflectConstant.LOGIN_ACTIVITY;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ReflectUtils;
import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseFragment;
import com.meowing.loud.arms.constant.AppConstant;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.dialog.CustomConfirmDialog;
import com.meowing.loud.arms.manager.LocalDataManager;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.utils.DialogUtil;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.FragmentHomeBinding;
import com.meowing.loud.home.adapter.MusicSimpleAdapter;
import com.meowing.loud.home.contract.HomeContract;
import com.meowing.loud.home.di.component.DaggerHomeComponent;
import com.meowing.loud.home.di.module.HomeModule;
import com.meowing.loud.home.presenter.HomePresenter;
import com.meowing.loud.login.view.activity.LoginActivity;
import com.meowing.loud.play.view.activity.PlayActivity;

import java.util.ArrayList;
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void initView(View mView) {
        musicAdapter = new MusicSimpleAdapter();
        musicAdapter.setListener(this);
        binding.ryMusicList.setAdapter(musicAdapter);
        binding.ivAdd.setImageDrawable(getResources().getDrawable(R.mipmap.ic_common_setting));
        binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outLogoutDialog();
            }
        });
    }

    private void outLogoutDialog() {
        new DialogUtil(getContext(), getString(R.string.common_dialog_title), getString(R.string.me_account_logout_title))
                .setDialogCallback(new CustomConfirmDialog.DialogCallback() {
                    @Override
                    public void onConfirm() {
                        LocalDataManager.getInstance().clear(true);
                        //关闭除了登录页面的其他页面
                        Class className = ReflectUtils.reflect(LOGIN_ACTIVITY).get();
                        ActivityUtils.finishOtherActivities(className);
                        //跳转到登录页面
                        navigator(LoginActivity.class);
                    }
                }).show();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        showLoading();
        mPresenter.findAllWaitMusicData();
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoading();
        mPresenter.findAllWaitMusicData();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void findAllWaitMusicResult() {
        if (musicRespList == null) {
            musicRespList = new ArrayList<>();
        } else {
            musicRespList.clear();
        }
        musicRespList.addAll(LocalDataManager.getInstance().getAllWaitMusicList());
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
