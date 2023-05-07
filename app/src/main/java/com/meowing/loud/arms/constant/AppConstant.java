package com.meowing.loud.arms.constant;

public class AppConstant {

    public static final int SHOW_LENGTH = 10;

    public static final int ROLE_TYPE_USER = 1;

    public static final int ROLE_TYPE_ADMIN = 2;

    /**
     * 音乐类型之待审核
     */
    public static final int MUSIC_TYPE_WAIT = 0;

    /**
     * 音乐类型之审核通过
     */
    public static final int MUSIC_TYPE_PASS = 1;

    /**
     * 音乐类型之审核不通过
     */
    public static final int MUSIC_TYPE_REFUSE = -1;

    /**
     * 音乐类型之收藏的
     */
    public static final int MUSIC_TYPE_LIKE = 2;

    /**
     * 音乐类型之我的
     */
    public static final int MUSIC_TYPE_MINE= 3;

    /**
     * 刷新间隔3秒
     */
    public static final long PULL_REFRESH_TIME_INTERVAL = 3000;

    public interface Switch {
        int Close = 0;
        int Open = 1;
        int Enable = 2;
        int Pause = 2;
    }
}
