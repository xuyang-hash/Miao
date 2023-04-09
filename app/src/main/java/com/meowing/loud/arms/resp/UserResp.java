package com.meowing.loud.arms.resp;

public class UserResp {

    /**
     * 用户id
     */
    private int uid;

    /**
     * 用户名（昵称）
     */
    private String username;

    /**
     * 用户名
     */
    private String password;

    /**
     * 用户头像
     */
    private String imageString;

    /**
     * 密保问题1
     */
    private String question1;

    /**
     * 密保答案1
     */
    private String answer1;

    /**
     * 密保问题2
     */
    private String question2;
    /**
     * 密保答案2
     */
    private String answer2;

    public UserResp(int uid, String username, String password, String imageString, String question1, String answer1, String question2, String answer2) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.imageString = imageString;
        this.question1 = question1;
        this.answer1 = answer1;
        this.question2 = question2;
        this.answer2 = answer2;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getImageString() {
        return imageString;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer2() {
        return answer2;
    }
}
