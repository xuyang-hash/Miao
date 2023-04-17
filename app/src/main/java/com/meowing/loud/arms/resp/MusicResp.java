package com.meowing.loud.arms.resp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meowing.loud.arms.utils.StringUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MusicResp {
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
     * 音乐发布日期
     */
    private Date upDate;

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
     * 点赞数
     */
    private int goodNum;

    /**
     * 点赞的人
     */
    private String googUsers;

    /**
     * 点赞的人列表形式
     */
    private List<String> googUserList;

    public MusicResp(int id, String name, String url, Date upDate, String headString, String username, String userHeadString, String googUsers, int good_num) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.upDate = upDate;
        this.headString = headString;
        this.username = username;
        this.userHeadString = userHeadString;
        this.googUsers = googUsers;
        this.goodNum = good_num;
        if (!StringUtils.isStringNULL(googUsers)) {
            initGoogUserList();
        }
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

    public Date getUpDate() {
        return upDate;
    }

    public String getHeadString() {
        return headString;
    }

    public String getUsername() {
        return username;
    }

    public String getUserHeadString() {
        return userHeadString;
    }

    public void initGoogUserList() {
        List<String> list = new Gson().fromJson(googUsers, new TypeToken<List<String>>() {
        }.getType());

        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
        }
        this.googUserList = list;
    }

    /**
     * 点赞
     * @param username
     */
    public void addGood(String username) {
        if (!googUserList.contains(username)) {
            googUserList.add(username);
        }
    }

    /**
     * 取消点赞
     * @param username
     */
    public void delGood(String username) {
        googUserList.remove(username);
    }

    /**
     * 当前用户是否点赞过
     * @param username
     * @return
     */
    public boolean isGood(String username) {
        return googUserList.contains(username);
    }

    public String getGoogUsers() {
        googUsers = new Gson().toJson(googUserList);
        return googUsers;
    }

    public void setGoogUsers(String googUsers) {
        this.googUsers = googUsers;
        initGoogUserList();
    }

    public int getGoodNum() {
        return goodNum;
    }
}
