package com.meowing.loud.arms.manager;

import com.meowing.loud.arms.resp.AdminResp;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.resp.UserResp;
import com.meowing.loud.arms.utils.MeoSPUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalDataManager {
    private static LocalDataManager instance;
    /**
     * 当前正在登录的用户
     */
    private UserResp userResp;

    /**
     * 当前正在登录的管理员
     */
    private AdminResp adminResp;

    /**
     * 缓存从服务器获取到的待审核的音乐
     */
    private List<MusicResp> allWaitMusicList;

    /**
     * 缓存从服务器获取到的审核通过的音乐
     */
    private List<MusicResp> allPassMusicList;

    /**
     * 缓存从服务器获取到的审核未通过的音乐
     */
    private List<MusicResp> allRefuseMusicList;

    /**
     * 缓存从服务器获取到的当前登录用户收藏的音乐
     */
    private List<MusicResp> allLikeMusicList;

    private List<MusicResp> allMineMusicList;

    private HashMap<Integer, String> musicUrlMap;

    public LocalDataManager() {
        this.allWaitMusicList = new ArrayList<>();
        this.allPassMusicList = new ArrayList<>();
        this.allRefuseMusicList = new ArrayList<>();
        this.allLikeMusicList = new ArrayList<>();
        this.allMineMusicList = new ArrayList<>();
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

    public AdminResp getAdminInfo() {
        return adminResp;
    }

    public void setAdminInfo(AdminResp adminResp) {
        this.adminResp = adminResp;
    }

    /**
     * 获取所有待审核的音乐（无uri）
     * @return
     */
    public List<MusicResp> getAllWaitMusicList() {
        return allWaitMusicList;
    }

    /**
     * 缓存待审核的音乐到本地
     * @param musicResp
     */
    public void setWaitMusic(MusicResp musicResp) {
        if (musicResp != null) {
            allWaitMusicList.add(musicResp);
        }
    }

    /**
     * 清除本地缓存的待审核的音乐的所有数据
     */
    public void clearWaitMusicList() {
        if (allWaitMusicList != null) {
            allWaitMusicList.clear();
        } else {
            allWaitMusicList = new ArrayList<>();
        }
    }

    /**
     * 获取所有审核通过的音乐（无uri）
     * @return
     */
    public List<MusicResp> getAllPassMusicList() {
        return allPassMusicList;
    }

    /**
     * 缓存审核通过的音乐到本地
     * @param musicResp
     */
    public void setPassMusic(MusicResp musicResp) {
        if (musicResp != null) {
            for (MusicResp resp : allPassMusicList) {
                if (resp.getId() == musicResp.getId()) {
                    return;
                }
            }
            allPassMusicList.add(musicResp);
            // 若包含当前登录用户收藏的音乐，则也缓存到收藏的列表
            if (MeoSPUtil.isUserLogin() && musicResp.isLikeContainMe()) {
                allLikeMusicList.add(musicResp);
            }
        }
    }

    /**
     * 清除本地缓存的审核通过的音乐的所有数据
     */
    public void clearAllPassMusicList() {
        if (allPassMusicList != null) {
            allPassMusicList.clear();
        } else {
            allPassMusicList = new ArrayList<>();
        }
    }

    /**
     * 手动设置收藏的音乐
     * @param musicResp
     */
    public void setLikeMusic(MusicResp musicResp, boolean isAdd) {
        if (musicResp != null) {
            if (isAdd) {
                for (MusicResp resp : allLikeMusicList) {
                    if (resp.getId() == musicResp.getId()) {
                        return;
                    }
                }
                allLikeMusicList.add(musicResp);
            } else {
                for (MusicResp resp : allLikeMusicList) {
                    if (resp.getId() == musicResp.getId()) {
                        allLikeMusicList.remove(resp);
                        return;
                    }
                }
            }
        }
    }

    /**
     * 清除本地缓存的已收藏的音乐的所有数据
     */
    public void clearAllLikeMusicList() {
        if (allLikeMusicList != null) {
            allLikeMusicList.clear();
        } else {
            allLikeMusicList = new ArrayList<>();
        }
    }

    /**
     * 获取所有审核未通过的音乐
     * @return
     */
    public List<MusicResp> getAllRefuseMusicList() {
        return allRefuseMusicList;
    }

    /**
     * 缓存审核未通过的音乐到本地
     * @param musicResp
     */
    public void setRefuseMusic(MusicResp musicResp) {
        if (musicResp != null && !allRefuseMusicList.contains(musicResp)) {
            allRefuseMusicList.add(musicResp);
        }
    }

    /**
     * 清除本地缓存的审核未通过的音乐的所有数据
     */
    public void clearAllRefuseMusicList() {
        if (allRefuseMusicList != null) {
            allRefuseMusicList.clear();
        } else {
            allRefuseMusicList = new ArrayList<>();
        }
    }

    public List<MusicResp> getAllLikeMusicList() {
        return allLikeMusicList;
    }

    public List<MusicResp> getAllMineMusicList() {
        return allMineMusicList;
    }

    public void setMineMusic(MusicResp musicResp) {
        if (musicResp != null) {
            allMineMusicList.add(musicResp);
        }
    }



    /**
     * 清除本地缓存的我的音乐的所有数据
     */
    public void clearMineMusicList() {
        if (allMineMusicList != null) {
            allMineMusicList.clear();
        } else {
            allMineMusicList = new ArrayList<>();
        }
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
        if (allWaitMusicList != null) {
            allWaitMusicList.clear();
        }

        if (allPassMusicList != null) {
            allPassMusicList.clear();
        }

        if (allRefuseMusicList != null) {
            allRefuseMusicList.clear();
        }

        if (allLikeMusicList != null) {

        }

        if (musicUrlMap != null) {
            musicUrlMap.clear();
        }
        userResp = null;
    }
}
