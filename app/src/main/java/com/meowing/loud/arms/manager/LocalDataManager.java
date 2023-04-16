package com.meowing.loud.arms.manager;

import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.resp.UserResp;

import java.util.ArrayList;
import java.util.List;

public class LocalDataManager {
    private static LocalDataManager instance;
    /**
     * 当前正在登录的用户
     */
    private UserResp userResp;

    private List<MusicResp> musicRespList;

    public LocalDataManager() {
        this.musicRespList = new ArrayList<>();
    }

    public synchronized static LocalDataManager getInstance() {
        if (instance == null) {
            instance = new LocalDataManager();
        }

        return instance;
    }

    public UserResp getUserInfo() {
        return userResp;
    }

    public void setUserInfo(UserResp userInfo) {
        this.userResp = userInfo;
    }

    public List<MusicResp> getMusicRespList() {
        return musicRespList;
    }

    public void setMusicRespList(List<MusicResp> musicRespList) {
        this.musicRespList = musicRespList;
    }

    /**
     * 清除所有缓存数据 包括登录状态
     * @param isClearLoginStatus    是否清除登录状态
     */
    public void clear(boolean isClearLoginStatus) {
        userResp = null;
    }
}
