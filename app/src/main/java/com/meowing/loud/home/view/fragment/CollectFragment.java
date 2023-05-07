package com.meowing.loud.home.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meowing.loud.R;
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
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.listener.OnMultiListener;

import java.util.ArrayList;
import java.util.List;

public class CollectFragment extends BaseFragment<FragmentHomeBinding, HomePresenter> implements HomeContract.View, MusicSimpleAdapter.Listener {

    private List<MusicResp> musicRespList;

    private MusicSimpleAdapter musicAdapter;

    /**
     * 下拉刷新时间
     */
    private long pullRefreshTime;

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
        binding.smlDevice.setOnMultiListener(new OnMultiListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {

            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {

            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                binding.smlDevice.finishRefresh();
                if (System.currentTimeMillis() - pullRefreshTime < AppConstant.PULL_REFRESH_TIME_INTERVAL) {
                    ToastUtils.showShort(getContext(), R.string.common_pull_refresh_frequently);
                    return;
                }
                mPresenter.findAllPassMusicData();
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

            }
        });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (musicRespList == null) {
            musicRespList = new ArrayList<>();
        } else {
            musicRespList.clear();
        }
        musicRespList.addAll(LocalDataManager.getInstance().getAllLikeMusicList());
        musicAdapter.setList(musicRespList);
    }

    @Override
    public void findAllPassMusicResult() {
        if (musicRespList == null) {
            musicRespList = new ArrayList<>();
        } else {
            musicRespList.clear();
        }
        musicRespList.addAll(LocalDataManager.getInstance().getAllLikeMusicList());
        musicAdapter.setList(musicRespList);

        if (musicRespList.isEmpty()) {
            binding.tvEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.tvEmpty.setVisibility(View.GONE);
        }
        pullRefreshTime = System.currentTimeMillis();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void onItemClickListener(int position) {
        PlayActivity.start(getContext(), AppConstant.MUSIC_TYPE_LIKE, position);
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
