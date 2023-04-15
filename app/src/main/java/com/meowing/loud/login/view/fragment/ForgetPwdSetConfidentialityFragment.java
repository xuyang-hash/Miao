package com.meowing.loud.login.view.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseFragment;
import com.meowing.loud.arms.constant.EventConstant;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.dialog.ConditionTypeDialog;
import com.meowing.loud.arms.entity.MessageWrap;
import com.meowing.loud.arms.resp.UserResp;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.FragmentSetConfidentialityLayoutBinding;
import com.meowing.loud.login.contract.LoginContract;
import com.meowing.loud.login.di.component.DaggerLoginComponent;
import com.meowing.loud.login.di.module.LoginModule;
import com.meowing.loud.login.presenter.LoginPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ForgetPwdSetConfidentialityFragment extends BaseFragment<FragmentSetConfidentialityLayoutBinding, LoginPresenter> implements LoginContract.View, View.OnClickListener {

    private UserResp userResp;

    /**
     * 问题的弹窗
     */
    private ConditionTypeDialog conditionTypeDialog;

    private boolean isShow;

    public static ForgetPwdSetConfidentialityFragment getInstance() {
        return new ForgetPwdSetConfidentialityFragment();
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initView(View mView) {
        binding.tvBindTitle.setText(R.string.account_reset_pwd_title);
        binding.llNicknameInput.setVisibility(View.VISIBLE);
        binding.cslQuestion2.setVisibility(View.GONE);
        binding.tvSetConfidentialitySubmit.setOnClickListener(this);
        binding.tvQuestion1.setOnClickListener(this);
        binding.tvQuestion1Title.setText(R.string.account_set_confidentiality_question_title);
        binding.tvSetConfidentialitySubmit.setText(R.string.common_next_tip);
        binding.tvSetConfidentialitySubmit.setOnClickListener(this);
        initConditionTypeDialog();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_set_confidentiality_submit) {
            String username = binding.etUsername.getText().toString().trim();
            String answer1 = binding.etAnswer1.getText().toString().trim();
            if (StringUtils.isStringNULL(username)) {
                ToastUtils.showShort(getContext(), R.string.account_username_is_empty);
                return;
            }

            if (StringUtils.isStringNULL(answer1)) {
                ToastUtils.showShort(getContext(), R.string.account_set_confidentiality_submit_error3);
                return;
            }

            if (mPresenter != null) {
                if (userResp != null) {
                    mateQAndA();
                } else {
                    showLoading();
                    mPresenter.findUser(username);
                }
            }
        } else if (id == R.id.tv_question1 || id == R.id.iv_condition_limit_status1) {
            outConditionTypeDialog();
        }
    }

    /**
     * 弹出选择弹窗
     *
     */
    private void outConditionTypeDialog() {
        new XPopup.Builder(getContext())
                .atView(binding.ivConditionLimitStatus1)
                .isCenterHorizontal(true)
                .setPopupCallback(new XPopupCallback() {
                    @Override
                    public void onCreated(BasePopupView popupView) {

                    }

                    @Override
                    public void beforeShow(BasePopupView popupView) {

                    }

                    @Override
                    public void onShow(BasePopupView popupView) {

                    }

                    @Override
                    public void onDismiss(BasePopupView popupView) {

                    }

                    @Override
                    public void beforeDismiss(BasePopupView popupView) {
                        isShow = false;
                        binding.ivConditionLimitStatus1.setImageResource(R.mipmap.ic_gray_down);
                    }

                    @Override
                    public boolean onBackPressed(BasePopupView popupView) {
                        return false;
                    }

                    @Override
                    public void onKeyBoardStateChanged(BasePopupView popupView, int height) {

                    }

                    @Override
                    public void onDrag(BasePopupView popupView, int value, float percent, boolean upOrLeft) {

                    }

                    @Override
                    public void onClickOutside(BasePopupView popupView) {

                    }
                })
                .asCustom(conditionTypeDialog)
                .show();
        isShow = !isShow;
        binding.ivConditionLimitStatus1.setImageResource(isShow ? R.mipmap.ic_gray_up : R.mipmap.ic_gray_down);
    }

    @Override
    public void onFindUserResult(boolean iSuccess, UserResp userResp, int errorId) {
        if (iSuccess) {
            this.userResp = userResp;
            mateQAndA();
        } else {
            ToastUtils.showShort(getContext(), R.string.account_login_find_user_empty);
        }
    }

    /**
     * 匹配问题和答案
     */
    private void mateQAndA() {
        String question1 = binding.tvQuestion1.getText().toString().trim();
        String answer1 = binding.etAnswer1.getText().toString().trim();
        if (StringUtils.contrast(question1, userResp.getQuestion1())) {
            if (StringUtils.contrast(answer1, userResp.getAnswer1())) {
                ToastUtils.showShort(getContext(), R.string.account_reset_pwd_mate_user_success);
                EventBus.getDefault().post(new MessageWrap(EventConstant.ModuleLogin.ACCOUNT_FORGET_PWD_MATE_Q_AND_A_SUCCESS, userResp));
            } else {
                ToastUtils.showShort(getContext(), R.string.account_reset_pwd_mate_user_failed);
            }
        } else if (StringUtils.contrast(question1, userResp.getQuestion2())) {
            if (StringUtils.contrast(answer1, userResp.getAnswer2())) {
                ToastUtils.showShort(getContext(), R.string.account_reset_pwd_mate_user_success);
                EventBus.getDefault().post(new MessageWrap(EventConstant.ModuleLogin.ACCOUNT_FORGET_PWD_MATE_Q_AND_A_SUCCESS, userResp));
            } else {
                ToastUtils.showShort(getContext(), R.string.account_reset_pwd_mate_user_failed);
            }
        } else {
            ToastUtils.showShort(getContext(), R.string.account_reset_pwd_mate_user_failed);
        }
    }

    /**
     * 初始化问题选择弹窗
     */
    private void initConditionTypeDialog() {
        ArrayList<String> questionList = new ArrayList<>();
        questionList.add(getString(R.string.account_set_confidentiality_question_tip1));
        questionList.add(getString(R.string.account_set_confidentiality_question_tip2));
        questionList.add(getString(R.string.account_set_confidentiality_question_tip3));

        conditionTypeDialog = new ConditionTypeDialog(getContext());
        conditionTypeDialog.setItemClickListener(new ConditionTypeDialog.ItemClickListener() {
            @Override
            public void onItemClickListener(String s, int position) {
                binding.tvQuestion1.setText(s);
            }
        });
        conditionTypeDialog.setList(questionList);
    }

    @Override
    public void error(String msg) {
        hideLoading();
        ToastUtils.showShort(getContext(), msg);
    }

    @Override
    public void showLoading() {
        CSeeLoadingDialog.getInstance(getContext()).show();
    }

    @Override
    public void hideLoading() {
        CSeeLoadingDialog.getInstance(getContext()).dismiss();
    }
}
