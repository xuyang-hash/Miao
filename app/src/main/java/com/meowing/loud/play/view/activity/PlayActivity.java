package com.meowing.loud.play.view.activity;

import static com.meowing.loud.arms.manager.play.IPlayerController.PLAY_STATE_PAUSE;
import static com.meowing.loud.arms.manager.play.IPlayerController.PLAY_STATE_START;
import static com.meowing.loud.arms.manager.play.IPlayerController.PLAY_STATE_STOP;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.constant.AppConstant;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.manager.LocalDataManager;
import com.meowing.loud.arms.manager.play.IPlayerController;
import com.meowing.loud.arms.manager.play.IPlayerViewController;
import com.meowing.loud.arms.manager.play.PlayService;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.ActivityPlayLayoutBinding;
import com.meowing.loud.play.contract.PlayContract;
import com.meowing.loud.play.presenter.PlayPresenter;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class PlayActivity extends BaseActivity<ActivityPlayLayoutBinding, PlayPresenter> implements PlayContract.View {

    private static final String TAG = "PlayerActivity";

    private ObjectAnimator mRotation;

    private IPlayerController mController;

    private PlayerConnection mPlayerConnection;

    private boolean isTouch = false;

    private static int mPosition;

    private MusicResp musicInfo;

    private boolean isNew = false;

    private List<MusicResp> musicList = new ArrayList<>();

    public Handler sHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            String showInfo = "";
            if (msg.what == 301) {
                showInfo = getString(R.string.play_download_success);
            } else if (msg.what == 302) {
                showInfo = getString(R.string.play_download_failed);
            } else if (msg.what == 303) {
                showInfo = getString(R.string.play_download_exist_tip);
            }
            ToastUtils.showShort(PlayActivity.this, showInfo);
            return false;
        }
    });

    public static void start(Context context, int musicType, int mPosition) {
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra("position", mPosition);
        intent.putExtra("musicType", musicType);
        context.startActivity(intent);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public void initView() {
        initMusicInfo();
        if (musicInfo != null) {
            binding.tvMusicName.setText(musicInfo.getName());
            binding.tvMusicUsername.setText(musicInfo.getUsername());
            binding.tvEditGood.setText(musicInfo.getGoodNum() + "");
            binding.ivEditGood.setSelected(musicInfo.isGoodContainMe());
            binding.tvEditLike.setText(musicInfo.getLikeNum() + "");
            binding.ivEditLike.setSelected(musicInfo.isLikeContainMe());
        }
        if (mRotation != null) {
            mRotation.start();
            return;
        }
        //设置旋转动画
        mRotation = ObjectAnimator
                .ofFloat(binding.ivPlayerPic, "rotation", 0, 360)
                .setDuration(15000);
        mRotation.setRepeatCount(Animation.INFINITE);
        mRotation.setInterpolator(new LinearInterpolator());
        mRotation.start();
        //初始化控件的点击事件
        initListener();
        initService();
        initBindService();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (mPresenter != null) {
            showLoading();
            mPresenter.findMusicUrl(musicInfo.getId());
        }
    }

    /**
     * 初始化音乐数据
     */
    private void initMusicInfo() {
        switch (getIntent().getIntExtra("musicType", 0)) {
            case AppConstant.MUSIC_TYPE_PASS:
                musicList = LocalDataManager.getInstance().getAllPassMusicList();
                break;
            case AppConstant.MUSIC_TYPE_WAIT:
                musicList = LocalDataManager.getInstance().getAllWaitMusicList();
                break;
            case AppConstant.MUSIC_TYPE_REFUSE:
                musicList = LocalDataManager.getInstance().getAllRefuseMusicList();
                break;
            case AppConstant.MUSIC_TYPE_LIKE:
                musicList = LocalDataManager.getInstance().getAllLikeMusicList();
                break;
            case AppConstant.MUSIC_TYPE_MINE:
                musicList = LocalDataManager.getInstance().getAllMineMusicList();
                break;
        }
        mPosition = getIntent().getIntExtra("position", 0);
        musicInfo = musicList.get(mPosition);
    }

    /**
     * 设置监听
     */
    private void initListener() {
        binding.sbPlaySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // 当进度条发生改变
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //当开始触摸进度条
                isTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //停止触摸进度条
                if (mController != null) {
                    mController.seekToPlay(seekBar.getProgress());
                    isTouch = false;
                }
            }
        });

        //上一首音乐按钮实现
        binding.ivControlPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMusic(false);
            }
        });

        //开始或暂停按钮播放按钮的点击事件
        binding.ivControlPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mController != null) {
                    mController.pauseOrResumePlay();
                }
            }
        });

        //下一首音乐实现
        binding.ivControlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMusic(true);
            }
        });

        binding.ivEditGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.updateMusicGood(musicInfo, binding.ivEditGood.isSelected());
            }
        });

        // 添加喜欢的音乐
        binding.ivEditLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.updateMusicLike(musicInfo, binding.ivEditLike.isSelected());
            }
        });
    }

    /**
     * 初始化音乐服务
     */
    private void initService() {
        Log.i(TAG, "->initService");
        startService(new Intent(this, PlayService.class));
    }

    /**
     * 绑定音乐服务
     */
    private void initBindService() {
        Log.i(TAG, "->initBindService");
        Intent intent = new Intent(this, PlayService.class);
        if (mPlayerConnection == null) {
            Log.i(TAG, "->mPlayerConnection");
            mPlayerConnection = new PlayerConnection();
        }
        bindService(intent, mPlayerConnection, BIND_AUTO_CREATE);
    }

    /**
     * 切换音乐
     *
     * @param isNext 下一首，false则为上一首
     */
    private void changeMusic(boolean isNext) {
        int newPosition = isNext ? mPosition + 1 : mPosition - 1;
        if (newPosition < 0 || newPosition >= musicList.size()) {
            ToastUtils.showShort(this,R.string.play_change_music_no_more_tips);
            return;
        }
        isNew = true;
        mPosition = newPosition;
        musicInfo = musicList.get(mPosition);
        showLoading();
        mPresenter.findMusicUrl(musicInfo.getId());
    }

    @Override
    public void findMusicUrlResult(String url) {
        if (!StringUtils.isStringNULL(url) && mPresenter != null) {
            musicInfo.setUrl(url);
            isNew = true;
            mController.startPlay(url, isNew);
        }
    }

    @Override
    public void updateMusicGoodResult(boolean isSuccess, MusicResp resp, boolean isAdd) {
        if (isSuccess) {
            ToastUtils.showShort(this, isAdd ? R.string.music_good_add_success : R.string.music_good_cancel_success);
            if (isAdd) {
                musicInfo.addGood(LocalDataManager.getInstance().getUserInfo().getUsername());
            } else {
                musicInfo.delGood(LocalDataManager.getInstance().getUserInfo().getUsername());
            }
            binding.tvEditGood.setText(musicInfo.getGoodNum() + "");
        } else {
            ToastUtils.showShort(this, isAdd ? R.string.music_good_add_failed : R.string.music_good_cancel_failed);
            binding.ivEditGood.setSelected(!binding.ivEditGood.isSelected());
        }
    }

    @Override
    public void updateMusicLikeResult(boolean isSuccess, MusicResp resp, boolean isLike) {
        if (isSuccess) {
            ToastUtils.showShort(this, isLike ? R.string.music_like_add_success : R.string.music_like_cancel_success);
            if (isLike) {
                musicInfo.addGood(LocalDataManager.getInstance().getUserInfo().getUsername());
            } else {
                musicInfo.delGood(LocalDataManager.getInstance().getUserInfo().getUsername());
            }
            binding.tvEditLike.setText(musicInfo.getGoodNum() + "");
        } else {
            ToastUtils.showShort(this, isLike ? R.string.music_like_add_failed : R.string.music_like_cancel_failed);
            binding.ivEditLike.setSelected(!binding.ivEditLike.isSelected());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerConnection != null) {
            unbindService(mPlayerConnection);
            mPlayerViewController = null;
        }
    }

    private IPlayerViewController mPlayerViewController = new IPlayerViewController() {
        @Override
        public void onPlayStateChange(int state) {
            switch (state) {
                case PLAY_STATE_START:
                    mRotation.resume();
                    binding.ivControlPlay.setImageResource(R.mipmap.ic_control_pause);
                    break;
                case PLAY_STATE_PAUSE:
                    mRotation.pause();
                    binding.ivControlPlay.setImageResource(R.mipmap.ic_control_play);
                    //设置暂停
                case PLAY_STATE_STOP:
                    break;
            }
        }

        @Override
        public void onSeekChange(int seek) {

            /**
             * 设置进度条的进度
             */
            if (!isTouch) {
                binding.sbPlaySeek.setProgress(seek);
                if (seek == 100) {
                    mController.seekToPlay(0);
                }
            }
        }
    };

    private class PlayerConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Timber.tag(TAG).i("->onServiceConnected");
            mController = (IPlayerController) service;
            //服务完成绑定后将UI控制器传到逻辑层
            mController.registerIPlayViewController(mPlayerViewController);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Timber.tag(TAG).i("->onServiceDisconnected");
            mController = null;
        }
    }
}
