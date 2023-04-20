package com.meowing.loud.arms.manager;

import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.resp.UserResp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalDataManager {
    private static LocalDataManager instance;
    /**
     * 当前正在登录的用户
     */
    private UserResp userResp;

    private List<MusicResp> musicRespList;

    private List<Integer> allMusicIdList;

    private HashMap<Integer, String> musicUrlMap;

    public LocalDataManager() {
        this.musicRespList = new ArrayList<>();
        this.allMusicIdList = new ArrayList<>();
        this.musicUrlMap = new HashMap<>();
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

    public List<Integer> getAllMusicIdList() {
        return allMusicIdList;
    }

    public void setAllMusicIdList(List<Integer> allMusicIdList) {
        this.allMusicIdList = allMusicIdList;
    }

    public void addMusicUrl(int musicId, String musicUrl) {
        if (musicUrlMap == null) {
            musicUrlMap = new HashMap<>();
        }
        musicUrlMap.put(musicId, musicUrl);
    }

    public String getMusicUrl(int musicId) {
        if (musicUrlMap != null) {
            if (musicUrlMap.containsKey(musicId)) {
                return musicUrlMap.get(musicId);
            }
        }
        return null;
    }

    /**
     * 清除所有缓存数据 包括登录状态
     * @param isClearLoginStatus    是否清除登录状态
     */
    public void clear(boolean isClearLoginStatus) {
        if (musicRespList != null) {
            musicRespList.clear();
        }

        if (allMusicIdList != null) {
            allMusicIdList.clear();
        }

        if (musicUrlMap != null) {
            musicUrlMap.clear();
        }
        userResp = null;
    }
}
