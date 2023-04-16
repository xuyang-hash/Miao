package com.meowing.loud.me.model;

import static com.blankj.utilcode.util.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.meowing.loud.arms.base.BaseModel;
import com.meowing.loud.arms.base.code.AccountCode;
import com.meowing.loud.arms.base.code.SuccessCode;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.arms.integration.IRepositoryManager;
import com.meowing.loud.arms.resp.UserResp;
import com.meowing.loud.arms.utils.JDBCUtils;
import com.meowing.loud.me.contract.UserContract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.inject.Inject;

@ActivityScope
public class UserModel extends BaseModel implements UserContract.Model {

    private HashMap<Integer, Listener> listenerHashMap;

    @Inject
    public UserModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        this.listenerHashMap = new HashMap<>();
    }

    @Override
    public void updateHead(UserResp userResp, Listener listener) {
        listenerHashMap.put(UPDATE_HEAD, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "update User set imageString = ? where username = ?";
                PreparedStatement statement = null;
                Message msg = new Message();
                msg.what = UPDATE_HEAD;
                try {
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, userResp.getImageString());
                    statement.setString(2, userResp.getUsername());
                    int rs = statement.executeUpdate();
                    if (rs > 0) {
                        msg.arg1 = SuccessCode.SUCCESS.getCode();
                    } else {
                        msg.arg1 = AccountCode.UPDATE_HEAD_FAILED.getCode();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    msg.arg1 = AccountCode.CONNECT_FAILED.getCode();
                } finally {
                    try {
                        connection.close();
                        statement.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                handler.handleMessage(msg);
            }
        }.start();
    }

    @Override
    public void findUser(String username, Listener listener) {
        listenerHashMap.put(OPT_FIND_USER, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "select * from User where username = ?";
                UserResp user = null;
                PreparedStatement ps = null;
                ResultSet resultSet = null;
                Message msg = new Message();
                msg.what = OPT_FIND_USER;
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, username);
                    resultSet = ps.executeQuery();

                    if (resultSet.next()) {
                        user = new UserResp(resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5),
                                resultSet.getString(6),
                                resultSet.getString(7),
                                resultSet.getString(8));
                        msg.arg1 = SuccessCode.SUCCESS.getCode();
                        msg.obj = user;
                    } else {
                        msg.arg1 = AccountCode.FIND_USER_IS_EMPTY.getCode();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    msg.arg1 = AccountCode.FIND_USER_CONNECT_FAILED.getCode();
                } finally {
                    try {
                        connection.close();
                        ps.close();
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                handler.handleMessage(msg);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Listener listener = listenerHashMap.get(msg.what);
                    if (listener != null) {
                        if (msg.arg1 >= 0) {
                            listener.onSuccess(msg.obj);
                        } else {
                            listener.onFailed(msg.arg1);
                        }
                    }
                }
            });
        }
    };
}
