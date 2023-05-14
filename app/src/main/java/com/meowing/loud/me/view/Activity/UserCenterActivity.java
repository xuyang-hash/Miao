package com.meowing.loud.me.view.Activity;

import static com.meowing.loud.arms.constant.ReflectConstant.LOGIN_ACTIVITY;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ReflectUtils;
import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.dialog.CustomConfirmDialog;
import com.meowing.loud.arms.manager.LocalDataManager;
import com.meowing.loud.arms.resp.UserResp;
import com.meowing.loud.arms.utils.ArmsUtils;
import com.meowing.loud.arms.utils.DialogUtil;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.arms.widget.controls.CommonItemWidget;
import com.meowing.loud.databinding.ActivityUserCenterLayoutBinding;
import com.meowing.loud.login.view.activity.LoginActivity;
import com.meowing.loud.me.contract.UserContract;
import com.meowing.loud.me.di.component.DaggerUserComponent;
import com.meowing.loud.me.di.module.UserModule;
import com.meowing.loud.me.presenter.UserPresenter;

import java.io.ByteArrayOutputStream;

public class UserCenterActivity extends BaseActivity<ActivityUserCenterLayoutBinding, UserPresenter> implements UserContract.View, View.OnClickListener {

    private UserResp userResp;

    public static void start(Context context) {
        Intent intent = new Intent(context, UserCenterActivity.class);
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
        userResp = LocalDataManager.getInstance().getUserInfo();
        if (userResp != null) {
            binding.tvUsername.setText(userResp.getUsername());
            if (!StringUtils.isStringNULL(userResp.getImageString())) {
                binding.ivProfilePhoto.setImageBitmap(ArmsUtils.toBitmapFromString(userResp.getImageString()));
            }
        }
        binding.rlProfilePhoto.setOnClickListener(this);
        binding.itemChangePassword.setItemClickListener(new CommonItemWidget.ItemClickListener() {
            @Override
            public void onItemClickListener(int resId) {
                // 修改密码
                EditPwdActivity.start(UserCenterActivity.this);
            }
        });

        binding.itemChangeConfident.setItemClickListener(new CommonItemWidget.ItemClickListener() {
            @Override
            public void onItemClickListener(int resId) {
                EditConfidentActivity.start(UserCenterActivity.this);
            }
        });

        binding.tvLoginOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_login_out) {
            new DialogUtil(this, getString(R.string.common_dialog_title), getString(R.string.me_account_logout_title))
                    .setDialogCallback(new CustomConfirmDialog.DialogCallback() {
                        @Override
                        public void onConfirm() {
                            logout();
                        }
                    }).show();
        } else if (id == R.id.rl_profile_photo) {
            new DialogUtil(this, getString(R.string.common_dialog_title), getString(R.string.me_account_setting_profile_photo_tips))
                    .setDialogCallback(new CustomConfirmDialog.DialogCallback() {
                        @Override
                        public void onConfirm() {
                            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                            galleryIntent.setType("image/*");//图片
                            startActivityForResult(galleryIntent, 0);
                        }
                    }).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == -1) {
            Uri uri = data.getData();
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                updateHead(imageString);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void updateHead(String imageString) {
        userResp.setImageString(imageString);
        binding.ivProfilePhoto.setImageBitmap(ArmsUtils.toBitmapFromString(imageString));
        showLoading();
        mPresenter.updateHead(userResp);
    }

    @Override
    public void onUpdateHeadResult(boolean isSuccess) {
        if (isSuccess) {
            ToastUtils.showShort(this, R.string.me_account_setting_profile_photo_success);
        }
    }

    /**
     * 登出操作
     */
    private void logout() {
        LocalDataManager.getInstance().clear(true);
        //关闭除了登录页面的其他页面
        Class className = ReflectUtils.reflect(LOGIN_ACTIVITY).get();
        ActivityUtils.finishOtherActivities(className);
        //跳转到登录页面
        navigator(LoginActivity.class, true);
    }

    @Override
    public void error(String msg) {
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
