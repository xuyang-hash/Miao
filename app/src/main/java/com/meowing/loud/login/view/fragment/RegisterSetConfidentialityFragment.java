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
import com.meowing.loud.arms.constant.MMKConstant;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.dialog.ConditionTypeDialog;
import com.meowing.loud.arms.entity.MessageWrap;
import com.meowing.loud.arms.utils.MeoSPUtil;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.FragmentSetConfidentialityLayoutBinding;
import com.meowing.loud.login.contract.LoginContract;
import com.meowing.loud.login.di.component.DaggerLoginComponent;
import com.meowing.loud.login.di.module.LoginModule;
import com.meowing.loud.login.presenter.LoginPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class RegisterSetConfidentialityFragment extends BaseFragment<FragmentSetConfidentialityLayoutBinding, LoginPresenter> implements LoginContract.View, View.OnClickListener {

    /**
     * 第一个问题的弹窗
     */
    private ConditionTypeDialog conditionTypeDialog1;

    /**
     * 第二个问题的弹窗
     */
    private ConditionTypeDialog conditionTypeDialog2;

    private String username;

    /**
     * 是否正在展示消息弹窗
     */
    private boolean isShow = false;

    public static RegisterSetConfidentialityFragment getInstance() {
        return new RegisterSetConfidentialityFragment();
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
        username = MeoSPUtil.getString(MMKConstant.LOGIN_USER_NAME);
        binding.llNicknameInput.setVisibility(View.GONE);
        initConditionTypeDialog();
        binding.tvSetConfidentialitySubmit.setOnClickListener(this);
        binding.tvQuestion1.setOnClickListener(this);
        binding.ivConditionLimitStatus1.setOnClickListener(this);
        binding.tvQuestion2.setOnClickListener(this);
        binding.ivConditionLimitStatus2.setOnClickListener(this);
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
            submit();
        } else if (id == R.id.tv_question1 || id == R.id.iv_condition_limit_status1) {
            outConditionTypeDialog(true);
        } else if (id == R.id.tv_question2 || id == R.id.iv_condition_limit_status2) {
            outConditionTypeDialog(false);
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

        conditionTypeDialog1 = new ConditionTypeDialog(getContext());
        conditionTypeDialog1.setItemClickListener(new ConditionTypeDialog.ItemClickListener() {
            @Override
            public void onItemClickListener(String s, int position) {
                binding.tvQuestion1.setText(s);
            }
        });
        conditionTypeDialog1.setList(questionList);

        conditionTypeDialog2 = new ConditionTypeDialog(getContext());
        conditionTypeDialog2.setItemClickListener(new ConditionTypeDialog.ItemClickListener() {
            @Override
            public void onItemClickListener(String s, int position) {
                binding.tvQuestion2.setText(s);
            }
        });
        conditionTypeDialog2.setList(questionList);
    }

    /**
     * 弹出选择弹窗
     *
     * @param isQuestion1 是否为第一个问题，若false，则为第二个问题
     */
    private void outConditionTypeDialog(boolean isQuestion1) {
        new XPopup.Builder(getContext())
                .atView(isQuestion1 ? binding.ivConditionLimitStatus1 : binding.ivConditionLimitStatus2)
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
                        if (isQuestion1) {
                            binding.ivConditionLimitStatus1.setImageResource(R.mipmap.ic_gray_down);
                        } else {
                            binding.ivConditionLimitStatus2.setImageResource(R.mipmap.ic_gray_down);
                        }
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
                .asCustom(isQuestion1 ? conditionTypeDialog1 : conditionTypeDialog2)
                .show();
        isShow = !isShow;
        if (isQuestion1) {
            binding.ivConditionLimitStatus1.setImageResource(isShow ? R.mipmap.ic_gray_up : R.mipmap.ic_gray_down);
        } else {
            binding.ivConditionLimitStatus2.setImageResource(isShow ? R.mipmap.ic_gray_up : R.mipmap.ic_gray_down);
        }
    }

    private void submit() {
        String question1 = binding.tvQuestion1.getText().toString().trim();
        String answer1 = binding.etAnswer1.getText().toString().trim();
        String question2 = binding.tvQuestion2.getText().toString();
        String answer2 = binding.etAnswer2.getText().toString();

        if (StringUtils.isStringNULL(answer1) || StringUtils.isStringNULL(answer2)) {
            ToastUtils.showShort(getContext(), R.string.account_set_confidentiality_submit_error3);
            return;
        }

        if (!StringUtils.isStringNULL(answer2) && StringUtils.contrast(question1, question2)) {
            ToastUtils.showShort(getContext(), R.string.account_set_confidentiality_submit_error2);
            return;
        }
        showLoading();
        mPresenter.setQuestionAndAnswer(username, question1, answer1, question2, answer2);
    }

    @Override
    public void setQuestionAndAnswerResult() {
        ToastUtils.showShort(getContext(), R.string.account_set_confidentiality_submit_success);
        EventBus.getDefault().post(new MessageWrap(EventConstant.ModuleLogin.ACCOUNT_REGISTER_SET_Q_AND_A_SUCCESS, null));
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
