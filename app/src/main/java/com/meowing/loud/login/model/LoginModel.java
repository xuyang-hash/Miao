package com.meowing.loud.login.model;

import android.app.Application;

import com.google.gson.Gson;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.arms.utils.JDBCUtils;
import com.meowing.loud.login.contract.LoginContract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

@ActivityScope
public class LoginModel implements LoginContract.Model {
    private Gson mGson;
    private Application mApplication;
    private String timeMills;

    @Inject
    public LoginModel(Gson gson, Application application) {
        mGson = gson;
        mApplication = application;
        timeMills = String.valueOf(System.currentTimeMillis());
    }
    @Override
    public void onDestroy() {
        this.mGson = null;
        this.mApplication = null;
    }

    /**
     *
     * 账号密码登录
     * @param account   用户名
     * @param password  密码
     * @param isUser    若为true，则为用户登录，若为false，则为管理员登录
     * @return  1.密码正确，2.密码错误，3.账号不存在
     */
    @Override
    public int login(String account, String password, boolean isUser) {
        Connection connection = JDBCUtils.getConn();
        String sql;
        if (isUser) {
            sql = "select password from User where account = ?";
        } else {
            sql = "select password from Admin where account = ?";
        }
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        int msg = 0;

        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, account);
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String realpassword = resultSet.getString(1);
                if(password.equals(realpassword)){
                    msg = 1;    //密码正确
                }else {
                    msg = 2;    //密码错误
                }
            }else msg = 3;//账号不存在
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                ps.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }
}
