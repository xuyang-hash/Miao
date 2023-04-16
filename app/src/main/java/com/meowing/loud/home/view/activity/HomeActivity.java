package com.meowing.loud.home.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.constant.ARouterConstant;
import com.meowing.loud.arms.constant.AppConstant;
import com.meowing.loud.arms.widget.controls.ButtonCheck;
import com.meowing.loud.databinding.ActivityHomeBinding;
import com.meowing.loud.home.presenter.HomePresenter;
import com.meowing.loud.home.view.fragment.HomeFragment;
import com.meowing.loud.me.view.Fragment.UserFragment;
import com.meowing.loud.trends.view.Fragment.TrendFragment;
import com.roughike.bottombar.OnTabSelectListener;

@Route(path = ARouterConstant.HomeConstant.HOME_PAGE)
public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomePresenter> implements OnTabSelectListener {
    public static final int HOME = 0;
    public static final int TRENDS = 1;
    public static final int USER = 2;
    private static final String FRAGMENT_STATE = "fragment_state";
    private static final String HOME_FRAGMENT = "homeFragment";
    private static final String TRENDS_FRAGMENT = "trendsFragment";
    private static final String USER_FRAGMENT = "userFragment";
    private FragmentManager fragmentManager;
    /**
     * 首页
     */
    private HomeFragment homeFragment;
    /**
     * 动态页
     */
    private TrendFragment trendFragment;
    /**
     * 我的
     */
    private UserFragment userFragment;

    /**
     * 退出间隔时间
     */
    private long exitTime = 0L;

    @Override
    public void initView() {
        fragmentManager = getSupportFragmentManager();
        binding.bcHome.setOnButtonClick(new ButtonCheck.OnButtonClickListener() {
            @Override
            public boolean onButtonClick(ButtonCheck bc, boolean bBeforeChecked) {
                setSelectTab(HOME);
                return false;
            }
        });
        binding.bcTrend.setOnButtonClick(new ButtonCheck.OnButtonClickListener() {
            @Override
            public boolean onButtonClick(ButtonCheck bc, boolean bBeforeChecked) {
                setSelectTab(TRENDS);
                return false;
            }
        });
        binding.bcMine.setOnButtonClick(new ButtonCheck.OnButtonClickListener() {
            @Override
            public boolean onButtonClick(ButtonCheck bc, boolean bBeforeChecked) {
                setSelectTab(USER);
                return false;
            }
        });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setSelectTab(HOME);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setSelectTab(HOME);
    }

    private void hidAllFragment(FragmentTransaction fragmentTransaction) {
        if (fragmentTransaction == null) {
            return;
        }

        homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HOME_FRAGMENT);
        trendFragment = (TrendFragment) fragmentManager.findFragmentByTag(TRENDS_FRAGMENT);
        userFragment = (UserFragment) fragmentManager.findFragmentByTag(USER_FRAGMENT);
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }
        if (trendFragment != null) {
            fragmentTransaction.hide(trendFragment);
        }
        if (userFragment != null) {
            fragmentTransaction.hide(userFragment);
        }
    }

    /**
     * 删除所有的选中状态
     */
    private void clearSelection() {
        binding.bcHome.setBtnValue(AppConstant.Switch.Close);
        binding.bcTrend.setBtnValue(AppConstant.Switch.Close);
        binding.bcMine.setBtnValue(AppConstant.Switch.Close);
    }

    private void setSelectTab(int position) {
        if (position == HOME && binding.bcHome.getBtnValue() == AppConstant.Switch.Open) {
            return;
        }

        if (position == TRENDS && binding.bcTrend.getBtnValue() == AppConstant.Switch.Open) {
            return;
        }

        if (position == USER && binding.bcMine.getBtnValue() == AppConstant.Switch.Open) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hidAllFragment(transaction);
        clearSelection();
        switch (position) {
            case HOME:
                binding.bcHome.setBtnValue(AppConstant.Switch.Open);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_container, homeFragment, HOME_FRAGMENT);
                    if (userFragment == null) {
                        userFragment = new UserFragment();
                        transaction.add(R.id.fl_container, userFragment, USER_FRAGMENT);
                        transaction.hide(userFragment);
                    }
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case TRENDS:
                binding.bcTrend.setBtnValue(AppConstant.Switch.Open);
                if (trendFragment == null) {
                    trendFragment = new TrendFragment();
                    transaction.add(R.id.fl_container, trendFragment, TRENDS_FRAGMENT);
                } else {
                    transaction.show(userFragment);
                }
                break;
            case USER:
                binding.bcMine.setBtnValue(AppConstant.Switch.Open);
                if (userFragment == null) {
                    userFragment = new UserFragment();
                    transaction.add(R.id.fl_container, userFragment, USER_FRAGMENT);
                } else {
                    transaction.show(userFragment);
                }
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onTabSelected(int tabId) {
        setSelectTab(tabId);
    }
}
