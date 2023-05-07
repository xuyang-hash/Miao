package com.meowing.loud.home.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.dialog.BaseCustomDialog;
import com.meowing.loud.arms.dialog.CSeeLoadingDialog;
import com.meowing.loud.arms.manager.LocalDataManager;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.utils.ArmsUtils;
import com.meowing.loud.arms.utils.AudioUtils;
import com.meowing.loud.arms.utils.DialogUtil;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.ActivityHomeAddMusicLayoutBinding;
import com.meowing.loud.home.contract.HomeContract;
import com.meowing.loud.home.di.component.DaggerHomeComponent;
import com.meowing.loud.home.di.module.HomeModule;
import com.meowing.loud.home.presenter.HomePresenter;

public class HomeAddMusicActivity extends BaseActivity<ActivityHomeAddMusicLayoutBinding, HomePresenter> implements HomeContract.View, View.OnClickListener {

    /**
     * 音频文件选取
     */
    private static final int REQUEST_CODE_FILE_PICKER_AUDIO = 0x01;

    /**
     * 图片选取
     */
    private static final int REQUEST_CODE_FILE_PICKER_IMAGE = 0x02;

    private MusicResp musicResp;


    public static void start(Context context) {
        Intent intent = new Intent(context, HomeAddMusicActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initView() {
        musicResp = new MusicResp();
        outChooseMusicDialog();
        binding.ivMusicHead.setOnClickListener(this);
        binding.tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_music_head) {
            outChooseHeadDialog();
        } else {
            submit();
        }
    }

    /**
     * 弹出选择音乐确认弹窗
     */
    private void outChooseMusicDialog() {
        new DialogUtil(this, getString(R.string.common_dialog_title),
                getString(R.string.music_add_dialog_content),
                getString(R.string.common_cancel), getString(R.string.common_confirm), false)
                .setDialogCallback(new BaseCustomDialog.DialogCallback() {
                    @Override
                    public void onConfirm() {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("audio/*");  // 设置文件类型为音频文件
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, REQUEST_CODE_FILE_PICKER_AUDIO);
                    }

                    @Override
                    public void onCancel() {
                        finish();
                    }
                }).show();
    }

    /**
     * 弹出选择头像确认弹窗
     */
    private void outChooseHeadDialog() {
        new DialogUtil(this, getString(R.string.common_dialog_title),
                getString(R.string.music_add_set_head))
                .setDialogCallback(new BaseCustomDialog.DialogCallback() {
                    @Override
                    public void onConfirm() {
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        galleryIntent.setType("image/*");//图片
                        startActivityForResult(galleryIntent, REQUEST_CODE_FILE_PICKER_IMAGE);
                    }
                }).show();
    }

    /**
     * 提交音乐
     */
    private void submit() {
        String musicName = binding.etMusicName.getText().toString().trim();
        if (!StringUtils.isStringNULL(musicName)) {
            musicResp.setName(musicName);
            musicResp.setUsername(LocalDataManager.getInstance().getUserInfo().getUsername());
            musicResp.setUserHeadString(LocalDataManager.getInstance().getUserInfo().getImageString());
            showLoading();
            mPresenter.addMusic(musicResp);
        } else {
            ToastUtils.showShort(this, R.string.music_add_set_name);
        }
    }

    @Override
    public void addMusicResult(boolean isSuccess) {
        if (isSuccess) {
            ToastUtils.showShort(this, R.string.music_add_sumit_success);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FILE_PICKER_AUDIO) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData(); // 获取选择的文件的uri
                musicResp.setUrl(AudioUtils.toAudioStringFromFile(this, uri));
                // 文件上传成功
                ToastUtils.showShort(HomeAddMusicActivity.this, R.string.music_add_set_music_success);
            } else {
                ToastUtils.showShort(this, R.string.music_add_set_music_failed);
                finish();
            }
        } else if (requestCode == REQUEST_CODE_FILE_PICKER_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                binding.ivMusicHead.setImageURI(uri);
                musicResp.setHeadString(ArmsUtils.toImageStringFromUri(this, uri));
                ToastUtils.showShort(this, R.string.music_add_set_head_success);
            } else {
                ToastUtils.showShort(this, R.string.music_add_set_head_failed);
            }
        }
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
