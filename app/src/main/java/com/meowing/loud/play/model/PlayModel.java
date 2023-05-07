package com.meowing.loud.play.model;

import static com.blankj.utilcode.util.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.meowing.loud.arms.base.BaseModel;
import com.meowing.loud.arms.base.code.AccountCode;
import com.meowing.loud.arms.base.code.SuccessCode;
import com.meowing.loud.arms.constant.AppConstant;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.arms.integration.IRepositoryManager;
import com.meowing.loud.arms.manager.LocalDataManager;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.utils.JDBCUtils;
import com.meowing.loud.play.contract.PlayContract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.inject.Inject;

@ActivityScope
public class PlayModel extends BaseModel implements PlayContract.Model {

    private HashMap<Integer, Listener> listenerHashMap;

    @Inject
    public PlayModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        this.listenerHashMap = new HashMap<>();
    }

    @Override
    public void updateMusicGood(String username, MusicResp musicResp, Listener listener) {
        listenerHashMap.put(UPDATE_MUSIC_GOOD, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "update Music set goodUsers = ? where id = ?";
                PreparedStatement statement = null;
                Message msg = new Message();
                msg.what = UPDATE_MUSIC_GOOD;
                try {
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, musicResp.getGoogUsers());
                    statement.setInt(2, musicResp.getId());
                    int rs = statement.executeUpdate();
                    if (rs > 0) {
                        msg.arg1 = SuccessCode.SUCCESS.getCode();
                    } else {
                        msg.arg1 = AccountCode.UPDATE_MUSIC_GOOD_FAILED.getCode();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    msg.arg1 = AccountCode.CONNECT_FAILED.getCode();
                } finally {
                    try {
                        connection.close();
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                handler.handleMessage(msg);
            }
        }.start();
    }

    @Override
    public void updateMusicLike(String username, MusicResp musicResp, Listener listener) {
        listenerHashMap.put(UPDATE_MUSIC_LIKE, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "update Music set likeUsers = ? where id = ?";
                PreparedStatement statement = null;
                Message msg = new Message();
                msg.what = UPDATE_MUSIC_LIKE;
                try {
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, musicResp.getLikeUsers());
                    statement.setInt(2, musicResp.getId());
                    int rs = statement.executeUpdate();
                    if (rs > 0) {
                        msg.arg1 = SuccessCode.SUCCESS.getCode();
                    } else {
                        msg.arg1 = AccountCode.UPDATE_MUSIC_GOOD_FAILED.getCode();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    msg.arg1 = AccountCode.CONNECT_FAILED.getCode();
                } finally {
                    try {
                        connection.close();
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                handler.handleMessage(msg);
            }
        }.start();
    }

    @Override
    public void updateMusicState(int musicId, boolean isPass, Listener listener) {
        listenerHashMap.put(UPDATE_MUSIC_STATE, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "update Music set state = ? where id = ?";
                PreparedStatement statement = null;
                Message msg = new Message();
                msg.what = UPDATE_MUSIC_STATE;
                try {
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, isPass ? AppConstant.MUSIC_TYPE_PASS : AppConstant.MUSIC_TYPE_REFUSE);
                    statement.setInt(2, musicId);
                    int rs = statement.executeUpdate();
                    if (rs > 0) {
                        msg.arg1 = SuccessCode.SUCCESS.getCode();
                    } else {
                        msg.arg1 = AccountCode.UPDATE_MUSIC_STATE_FAILED.getCode();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    msg.arg1 = AccountCode.CONNECT_FAILED.getCode();
                } finally {
                    try {
                        connection.close();
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                handler.handleMessage(msg);
            }
        }.start();
    }

    @Override
    public void findMusicUrl(int musicId, Listener listener) {
        listenerHashMap.put(FIND_MUSIC_URL, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "select uri from Music where id = ?";
                PreparedStatement statement = null;
                ResultSet resultSet = null;
                Message msg = new Message();
                msg.what = FIND_MUSIC_URL;
                try {
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, musicId);
                    resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        LocalDataManager.getInstance().addMusicUrl(musicId, resultSet.getString(1));
                        msg.obj = resultSet.getString(1);
                        msg.arg1 = SuccessCode.SUCCESS.getCode();
                    } else {
                        msg.arg1 = AccountCode.UPDATE_MUSIC_GOOD_FAILED.getCode();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    msg.arg1 = AccountCode.CONNECT_FAILED.getCode();
                } finally {
                    try {
                        connection.close();
                        statement.close();
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
