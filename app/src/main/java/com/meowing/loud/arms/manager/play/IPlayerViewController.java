package com.meowing.loud.arms.manager.play;

public interface IPlayerViewController {

    /**
     * 播放状态改变
     * @param state
     */
    void onPlayStateChange(int state);

    /**
     * 播放进度条更新
     * @param seek
     */
    void onSeekChange(int seek);
}
