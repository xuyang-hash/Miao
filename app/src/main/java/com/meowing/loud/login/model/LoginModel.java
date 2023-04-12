package com.meowing.loud.login.model;

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
import com.meowing.loud.arms.utils.StringUtils;
import com.meowing.loud.login.contract.LoginContract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.inject.Inject;

@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {
    private String timeMills;
    private HashMap<Integer, Listener> listenerHashMap;

    @Inject
    public LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        this.listenerHashMap = new HashMap<>();
    }

    private static final int OPT_LOGIN_USER = 0;

    @Override
    public void userLogin(String account, String password, Listener listener) {

        listenerHashMap.put(OPT_LOGIN_USER, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "select password from User where username = ?";
                PreparedStatement ps = null;
                ResultSet resultSet = null;
                Message msg = new Message();
                msg.what = OPT_LOGIN_USER;
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, account);
                    resultSet = ps.executeQuery();

                    if (resultSet.next()) {
                        String realpassword = resultSet.getString(1);
                        if (password.equals(realpassword)) {
                            msg.arg1 = SuccessCode.SUCCESS.getCode();
                        } else {
                            msg.arg1 = AccountCode.LOGIN_USER_OR_PWD_ERROR.getCode();
                        }
                    } else {
                        msg.arg1 = AccountCode.LOGIN_USERNAME_NOT_EXIST.getCode();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    msg.arg1 = AccountCode.LOGIN_CONNECT_FAILED.getCode();
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

    private static final int OPT_LOGIN_ADMIN = 1;

    @Override
    public void adminLogin(String account, String password, Listener listener) {
        listenerHashMap.put(OPT_LOGIN_ADMIN, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "select password from Admin where username = ?";
                PreparedStatement ps = null;
                ResultSet resultSet = null;
                Message msg = new Message();
                msg.what = OPT_LOGIN_ADMIN;
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, account);
                    resultSet = ps.executeQuery();

                    if (resultSet.next()) {
                        String realpassword = resultSet.getString(1);
                        if (password.equals(realpassword)) {
                            msg.arg1 = SuccessCode.SUCCESS.getCode();
                        } else {
                            msg.arg1 = AccountCode.LOGIN_USER_OR_PWD_ERROR.getCode();
                        }
                    } else {
                        msg.arg1 = AccountCode.LOGIN_USERNAME_NOT_EXIST.getCode();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    msg.arg1 = AccountCode.LOGIN_CONNECT_FAILED.getCode();
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

    private static final int OPT_REGISTER = 2;

    @Override
    public void registerUser(String username, String password, Listener listener) {
        listenerHashMap.put(OPT_REGISTER, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "insert into User(username,password) " +
                        "values(?,?)";
                PreparedStatement ps = null;
                Message msg = new Message();
                msg.what = OPT_REGISTER;
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, username);
                    ps.setString(2, password);
                    int rs = ps.executeUpdate();
                    if (rs > 0) {
                        msg.arg1 = SuccessCode.SUCCESS.getCode();
                    } else {
                        msg.arg1 = AccountCode.REGISTER_SUBMITT_FAILED.getCode();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    msg.arg1 = AccountCode.REGISTER_CONNECT_FAILED.getCode();
                } finally {
                    try {
                        connection.close();
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                handler.handleMessage(msg);
            }
        }.start();
    }

    private static final int OPT_FIND_USER = 3;

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

    private static final int SET_Q_AND_A = 4;
    @Override
    public void setQuestionAndAnswer(String username, String question1, String answer1, String question2, String answer2, Listener listener) {
        listenerHashMap.put(SET_Q_AND_A, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql;
                if (StringUtils.isStringNULL(answer2)) {
                    sql = "update User set question1 = ?, answer1 = ? where username = ?";
                } else {
                    sql = "update User set question1 = ?, answer1 = ?, question2 = ?, answer2 = ? where username = ?";
                }

                PreparedStatement statement = null;
                Message msg = new Message();
                msg.what = SET_Q_AND_A;
                try {
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, question1);
                    statement.setString(2, answer1);
                    if (StringUtils.isStringNULL(answer2)) {
                        statement.setString(3, username);
                    } else {
                        statement.setString(3, question2);
                        statement.setString(4, answer2);
                        statement.setString(5, username);
                    }
                    if (statement.executeUpdate() > 0) {
                        msg.arg1 = SuccessCode.SUCCESS.getCode();
                    } else {
                        msg.arg1 = AccountCode.SET_QUESTION_AND_ANSWER_FAILED.getCode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.arg1 = AccountCode.SET_QUESTION_AND_ANSWER_CONNECT_ERROR.getCode();
                } finally {
                    try {
                        connection.close();
                        statement.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public interface Listener {
        void onSuccess(Object obj);

        void onFailed(int errorId);
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
