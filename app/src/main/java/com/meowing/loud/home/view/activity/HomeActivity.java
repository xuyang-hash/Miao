package com.meowing.loud.home.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.constant.ARouterConstant;
import com.meowing.loud.arms.constant.AppConstant;
import com.meowing.loud.arms.constant.MMKConstant;
import com.meowing.loud.arms.utils.MeoSPUtil;
import com.meowing.loud.databinding.ActivityHomeBinding;
import com.meowing.loud.home.presenter.HomePresenter;
import com.meowing.loud.home.view.fragment.CollectFragment;
import com.meowing.loud.home.view.fragment.HomeFragment;
import com.meowing.loud.home.view.fragment.RefuseFragment;
import com.meowing.loud.home.view.fragment.WaitMusicFragment;
import com.meowing.loud.me.view.Fragment.UserFragment;
import com.roughike.bottombar.OnTabSelectListener;

@Route(path = ARouterConstant.HomeConstant.HOME_PAGE)
public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomePresenter> implements OnTabSelectListener {
    public static final int HOME = 0;
    public static final int USER = 1;

    public static final int COLLECT = 2;

    public static final int REFUSE = 3;

    public static final int WAITMUSIC = 4;

    private static final String FRAGMENT_STATE = "fragment_state";
    private static final String HOME_FRAGMENT = "homeFragment";

    private static final String COLLECT_FRAGMENT = "colletFragment";

    private static final String REFUSE_FRAGMENT = "refuseFragment";

    private static final String WAIT_MUSIC_FRAGMENT = "waitMusicFragment";

    private static final String USER_FRAGMENT = "userFragment";
    private FragmentManager fragmentManager;
    /**
     * 首页
     */
    private HomeFragment homeFragment;

    /**
     * 收藏音乐页
     */
    private CollectFragment collectFragment;

    /**
     * 审核未通过页
     */
    private RefuseFragment refuseFragment;

    /**
     * 待审核页
     */
    private WaitMusicFragment waitMusicFragment;

    /**
     * 我的
     */
    private UserFragment userFragment;

    /**
     * 退出间隔时间
     */
    private long exitTime = 0L;

    private int userType = MeoSPUtil.getInt(MMKConstant.LOGIN_USER_TYPE, AppConstant.ROLE_TYPE_USER);

    @Override
    public void initView() {
        fragmentManager = getSupportFragmentManager();
        if (userType == AppConstant.ROLE_TYPE_USER) {
            binding.bcWait.setVisibility(View.GONE);
            binding.bcRefuse.setVisibility(View.GONE);
            binding.bcCollect.setVisibility(View.VISIBLE);
            binding.bcMine.setVisibility(View.VISIBLE);
        } else if (userType == AppConstant.ROLE_TYPE_ADMIN) {
            binding.bcWait.setVisibility(View.VISIBLE);
            binding.bcRefuse.setVisibility(View.VISIBLE);
            binding.bcCollect.setVisibility(View.GONE);
            binding.bcMine.setVisibility(View.GONE);
        }
        binding.bcHome.setOnButtonClick((bc, bBeforeChecked) -> {
            setSelectTab(HOME);
            return false;
        });

        binding.bcMine.setOnButtonClick((bc, bBeforeChecked) -> {
            setSelectTab(USER);
            return false;
        });

        binding.bcCollect.setOnButtonClick((bc, bBeforeChecked) -> {
            setSelectTab(COLLECT);
            return false;
        });

        binding.bcWait.setOnButtonClick((bc, bBeforeChecked) -> {
            setSelectTab(WAITMUSIC);
            return false;
        });

        binding.bcRefuse.setOnButtonClick((bc, bBeforeChecked) -> {
            setSelectTab(REFUSE);
            return false;
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
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }

        if (userType == AppConstant.ROLE_TYPE_USER) {
            collectFragment = (CollectFragment) fragmentManager.findFragmentByTag(COLLECT_FRAGMENT);
            if (collectFragment != null) {
                fragmentTransaction.hide(collectFragment);
            }

            userFragment = (UserFragment) fragmentManager.findFragmentByTag(USER_FRAGMENT);
            if (userFragment != null) {
                fragmentTransaction.hide(userFragment);
            }
        } else if (userType == AppConstant.ROLE_TYPE_ADMIN) {
            waitMusicFragment = (WaitMusicFragment) fragmentManager.findFragmentByTag(WAIT_MUSIC_FRAGMENT);
            if (waitMusicFragment != null) {
                fragmentTransaction.hide(waitMusicFragment);
            }

            refuseFragment = (RefuseFragment) fragmentManager.findFragmentByTag(REFUSE_FRAGMENT);
            if (refuseFragment != null) {
                fragmentTransaction.hide(refuseFragment);
            }
        }
    }

    /**
     * 删除所有的选中状态
     */
    private void clearSelection() {
        binding.bcHome.setBtnValue(AppConstant.Switch.Close);
        binding.bcCollect.setBtnValue(AppConstant.Switch.Close);
        binding.bcMine.setBtnValue(AppConstant.Switch.Close);
        binding.bcWait.setBtnValue(AppConstant.Switch.Close);
        binding.bcRefuse.setBtnValue(AppConstant.Switch.Close);
    }

    private void setSelectTab(int position) {
        if (position == HOME && binding.bcHome.getBtnValue() == AppConstant.Switch.Open) {
            return;
        }

        if (position == COLLECT && binding.bcCollect.getBtnValue() == AppConstant.Switch.Open) {
            return;
        }

        if (position == USER && binding.bcMine.getBtnValue() == AppConstant.Switch.Open) {
            return;
        }

        if (position == WAITMUSIC && binding.bcWait.getBtnValue() == AppConstant.Switch.Open) {
            return;
        }

        if (position == REFUSE && binding.bcRefuse.getBtnValue() == AppConstant.Switch.Open) {
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
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case COLLECT:
                binding.bcCollect.setBtnValue(AppConstant.Switch.Open);
                if (collectFragment == null) {
                    collectFragment = new CollectFragment();
                    transaction.add(R.id.fl_container, collectFragment, COLLECT_FRAGMENT);
                } else {
                    transaction.show(collectFragment);
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
            case WAITMUSIC:
                binding.bcWait.setBtnValue(AppConstant.Switch.Open);
                if (waitMusicFragment == null) {
                    waitMusicFragment = new WaitMusicFragment();
                    transaction.add(R.id.fl_container, waitMusicFragment, WAIT_MUSIC_FRAGMENT);
                } else {
                    transaction.show(waitMusicFragment);
                }
                break;
            case REFUSE:
                binding.bcRefuse.setBtnValue(AppConstant.Switch.Open);
                if (refuseFragment == null) {
                    refuseFragment = new RefuseFragment();
                    transaction.add(R.id.fl_container, refuseFragment, REFUSE_FRAGMENT);
                } else {
                    transaction.show(refuseFragment);
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
