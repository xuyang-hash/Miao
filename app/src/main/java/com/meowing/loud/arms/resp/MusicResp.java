package com.meowing.loud.arms.resp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meowing.loud.arms.constant.MMKConstant;
import com.meowing.loud.arms.utils.MeoSPUtil;
import com.meowing.loud.arms.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MusicResp implements Serializable {
    private int id;
    /**
     * 音乐名称
     */
    private String name;

    /**
     * 音乐播放url
     */
    private String url;

    /**
     * 头像String
     */
    private String headString;

    /**
     * 发布者
     */
    private String username;

    /**
     * 发布者头像
     */
    private String userHeadString;

    /**
     * 点赞的人
     */
    private String goodUsers;

    /**
     * 收藏的人
     */
    private String likeUsers;

    /**
     * 音乐的发布状态
     * 0：待审核
     * 1：审核通过，
     * -1：审核失败
     */
    private int state;

    /**
     * 点赞的人列表形式
     */
    private List<String> goodUserList;

    private List<String> likeUserList;

    public MusicResp() {

    }

    public MusicResp(String name, String headString, String username, String userHeadString, String goodUsers, String likeUsers) {
        this.name = name;
        this.headString = headString;
        this.username = username;
        this.userHeadString = userHeadString;
        this.goodUsers = goodUsers;
        this.likeUsers = likeUsers;
        initGoogUserList();
        initLikeUserList();
    }

    public MusicResp(String name, String url, String headString, String username, String userHeadString) {
        this.name = name;
        this.url = url;
        this.headString = headString;
        this.username = username;
        this.userHeadString = userHeadString;
    }

    public MusicResp(int id, String name, String headString, String username, String userHeadString, String goodUsers, String likeUsers) {
        this.id = id;
        this.name = name;
        this.headString = headString;
        this.username = username;
        this.userHeadString = userHeadString;
        this.goodUsers = goodUsers;
        this.likeUsers = likeUsers;
        initGoogUserList();
        initLikeUserList();
    }

    public MusicResp(int id, String name, String url, String headString, String username, String userHeadString, String goodUsers, String likeUsers) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.headString = headString;
        this.username = username;
        this.userHeadString = userHeadString;
        this.goodUsers = goodUsers;
        this.likeUsers = likeUsers;
        initGoogUserList();
        initLikeUserList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeadString() {
        return headString;
    }

    public void setHeadString(String headString) {
        this.headString = headString;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserHeadString() {
        return userHeadString;
    }

    public void setUserHeadString(String userHeadString) {
        this.userHeadString = userHeadString;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    /**
     * 初始化点赞列表
     */
    private void initGoogUserList() {
        if (!StringUtils.isStringNULL(goodUsers)) {
            List<String> list = new Gson().fromJson(goodUsers, new TypeToken<List<String>>() {
            }.getType());

            if (list == null || list.isEmpty()) {
                list = new ArrayList<>();
            }
            this.goodUserList = list;
        } else {
            this.goodUserList = new ArrayList<>();
        }
    }

    /**
     * 初始化收藏列表
     */
    private void initLikeUserList() {
        if (!StringUtils.isStringNULL(likeUsers)) {
            List<String> list = new Gson().fromJson(likeUsers, new TypeToken<List<String>>() {
            }.getType());

            if (list == null || list.isEmpty()) {
                list = new ArrayList<>();
            }
            this.likeUserList = list;
        } else {
            this.likeUserList = new ArrayList<>();
        }
    }

    /**
     * 点赞
     * @param username
     */
    public void addGood(String username) {
        if (!goodUserList.contains(username)) {
            goodUserList.add(username);
        }
    }

    /**
     * 收藏
     * @param username
     */
    public void addLike(String username) {
        if (!likeUserList.contains(username)) {
            likeUserList.add(username);
        }
    }

    /**
     * 取消点赞
     * @param username
     */
    public void delGood(String username) {
        goodUserList.remove(username);
    }

    /**
     * 取消收藏
     * @param username
     */
    public void delLike(String username) {
        likeUserList.remove(username);
    }

    public String getGoogUsers() {
        goodUsers = new Gson().toJson(goodUserList);
        return goodUsers;
    }

    public String getLikeUsers() {
        likeUsers = new Gson().toJson(likeUserList);
        return likeUsers;
    }

    /**
     * 点赞者是否包含当前登录用户
     * @return
     */
    public boolean isGoodContainMe() {
        if (goodUserList != null && !goodUserList.isEmpty()) {
            return goodUserList.contains(MeoSPUtil.getString(MMKConstant.LOGIN_USER_NAME));
        }
        return false;
    }

    /**
     * 收藏者是否包含当前登录用户
     * @return
     */
    public boolean isLikeContainMe() {
        if (likeUserList != null && !likeUserList.isEmpty()) {
            return likeUserList.contains(MeoSPUtil.getString(MMKConstant.LOGIN_USER_NAME));
        }
        return false;
    }

    /**
     * 当前歌曲点赞数量
     * @return
     */
    public int getGoodNum() {
        if (goodUserList != null) {
            return goodUserList.size();
        }
        return 0;
    }

    /**
     * 当前歌曲收藏数量
     * @return
     */
    public int getLikeNum() {
        if (likeUserList != null) {
            return likeUserList.size();
        }
        return 0;
    }
}
