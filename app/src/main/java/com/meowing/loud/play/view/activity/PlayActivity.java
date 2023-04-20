package com.meowing.loud.play.view.activity;

import static com.meowing.loud.arms.manager.play.IPlayerController.PLAY_STATE_PAUSE;
import static com.meowing.loud.arms.manager.play.IPlayerController.PLAY_STATE_START;
import static com.meowing.loud.arms.manager.play.IPlayerController.PLAY_STATE_STOP;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
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
import androidx.annotation.RequiresApi;

import com.meowing.loud.R;
import com.meowing.loud.arms.base.BaseActivity;
import com.meowing.loud.arms.di.component.AppComponent;
import com.meowing.loud.arms.manager.play.IPlayerController;
import com.meowing.loud.arms.manager.play.IPlayerViewController;
import com.meowing.loud.arms.manager.play.PlayService;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.arms.utils.ToastUtils;
import com.meowing.loud.databinding.ActivityPlayLayoutBinding;
import com.meowing.loud.play.contract.PlayContract;
import com.meowing.loud.play.presenter.PlayPresenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends BaseActivity<ActivityPlayLayoutBinding, PlayPresenter> implements PlayContract.View {

    private static final int LAST_MUSIC = -1;
    private static final int NEXT_MUSIC = 1;
    private static final String TAG = "Player";

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

    public static void start(Context context, ArrayList<MusicResp> musicList, int mPosition) {
        Bundle b = new Bundle();
        b.putSerializable("musicList", (Serializable) musicList);
        b.putInt("position", mPosition);
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtras(b);
        context.startActivity(intent);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public void initView() {
        musicList = (List<MusicResp>) getIntent().getSerializableExtra("musicList");
        mPosition = getIntent().getIntExtra("position", 0);
        musicInfo = musicList.get(mPosition);
        if (musicInfo != null) {
            binding.tvMusicName.setText(musicInfo.getName());
            binding.tvMusicUsername.setText(musicInfo.getUsername());
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
        initEventListener();
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
     * 设置监听
     */
    private void initEventListener() {
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
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerConnection != null) {
            unbindService(mPlayerConnection);
            mPlayerViewController = null;
        }
    }

    private IPlayerViewController mPlayerViewController = new IPlayerViewController() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            Log.i(TAG, "->onServiceConnected");
            mController = (IPlayerController) service;
            //服务完成绑定后将UI控制器传到逻辑层
            mController.registerIPlayViewController(mPlayerViewController);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "->onServiceDisconnected");
            mController = null;
        }
    }
}
