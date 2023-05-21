package com.meowing.loud.me.view.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.constant.MMKConstant;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.dialog.ConditionTypeDialog;
import com.meowing.loud.arms.utils.MeoSPUtil;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.FragmentSetConfidentialityLayoutBinding;
import com.meowing.loud.me.contract.UserContract;
import com.meowing.loud.me.di.component.DaggerUserComponent;
import com.meowing.loud.me.di.module.UserModule;
import com.meowing.loud.me.presenter.UserPresenter;

import java.util.ArrayList;

public class EditConfidentActivity extends BaseActivity<FragmentSetConfidentialityLayoutBinding, UserPresenter> implements UserContract.View, View.OnClickListener {

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

    public static void start(Context context) {
        Intent intent = new Intent(context, EditConfidentActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerUserComponent
                .builder()
                .appComponent(appComponent)
                .userModule(new UserModule(this))//请将HomeModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public void initView() {
        username = MeoSPUtil.getString(MMKConstant.LOGIN_USER_NAME);
        binding.tvBindTitle.setText(R.string.account_edit_confident_title);
        binding.llNicknameInput.setVisibility(View.GONE);
        initConditionTypeDialog();
        binding.tvSetConfidentialitySubmit.setOnClickListener(this);
        binding.tvQuestion1.setOnClickListener(this);
        binding.ivConditionLimitStatus1.setOnClickListener(this);
        binding.tvQuestion2.setOnClickListener(this);
        binding.ivConditionLimitStatus2.setOnClickListener(this);
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

        conditionTypeDialog1 = new ConditionTypeDialog(this);
        conditionTypeDialog1.setItemClickListener(new ConditionTypeDialog.ItemClickListener() {
            @Override
            public void onItemClickListener(String s, int position) {
                binding.tvQuestion1.setText(s);
            }
        });
        conditionTypeDialog1.setList(questionList);

        conditionTypeDialog2 = new ConditionTypeDialog(this);
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
        new XPopup.Builder(this)
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
            ToastUtils.showShort(this, R.string.account_set_confidentiality_submit_error3);
            return;
        }

        if (!StringUtils.isStringNULL(answer2) && StringUtils.contrast(question1, question2)) {
            ToastUtils.showShort(this, R.string.account_set_confidentiality_submit_error2);
            return;
        }
        showLoading();
        mPresenter.setQuestionAndAnswer(username, question1, answer1, question2, answer2);
    }

    @Override
    public void setQuestionAndAnswerResult() {
        ToastUtils.showShort(this, R.string.account_set_confidentiality_submit_success);
        finish();
    }

    @Override
    public void error(String msg) {
        hideLoading();
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void showLoading() {
        CSeeLoadingDialog.getInstance(this).show();
    }

    @Override
    public void hideLoading() {
        CSeeLoadingDialog.getInstance(this).dismiss();
    }
}
