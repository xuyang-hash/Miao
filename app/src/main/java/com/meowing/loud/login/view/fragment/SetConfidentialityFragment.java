package com.meowing.loud.login.view.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

public class SetConfidentialityFragment extends BaseFragment<FragmentSetConfidentialityLayoutBinding, LoginPresenter> implements LoginContract.View {

    /**
     * 第一个问题的弹窗
     */
    private ConditionTypeDialog conditionTypeDialog1;

    /**
     * 第二个问题的弹窗
     */
    private ConditionTypeDialog conditionTypeDialog2;

    private String username;

    public static SetConfidentialityFragment getInstance() {
        return new SetConfidentialityFragment();
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
        initConditionTypeDialog();
        binding.tvSubmit.setOnClickListener(view -> submit());
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    /**
     * 初始化问题选择弹窗
     */
    private void initConditionTypeDialog() {
        conditionTypeDialog1 = new ConditionTypeDialog(getContext());
        conditionTypeDialog1.setItemClickListener(new ConditionTypeDialog.ItemClickListener() {
            @Override
            public void onItemClickListener(String s, int position) {
                binding.tvQuestion1.setText(s);
            }
        });
        ArrayList<String> questionList = new ArrayList<>();
        questionList.add(getString(R.string.account_set_confidentiality_question_tip1));
        questionList.add(getString(R.string.account_set_confidentiality_question_tip2));
        questionList.add(getString(R.string.account_set_confidentiality_question_tip3));
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

    private void submit() {
        String question1 = binding.tvQuestion1.getText().toString();
        String answer1 = binding.etAnswer1.getText().toString();
        String question2 = binding.tvQuestion1.getText().toString();
        String answer2 = binding.etAnswer1.getText().toString();

        if (StringUtils.isStringNULL(answer1)) {
            ToastUtils.showShort(getContext(), R.string.account_set_confidentiality_submit_error1);
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
        ToastUtils.showShort(getContext(), R.string.account_set_confidentiality_submit_error1);
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
