package com.meowing.loud.home.model;

import static com.blankj.utilcode.util.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.meowing.loud.arms.base.BaseModel;
import com.meowing.loud.arms.base.code.AccountCode;
import com.meowing.loud.arms.base.code.SuccessCode;
import com.meowing.loud.arms.di.scope.ActivityScope;
import com.meowing.loud.arms.integration.IRepositoryManager;
import com.meowing.loud.arms.resp.MusicResp;
import com.meowing.loud.arms.utils.JDBCUtils;
import com.meowing.loud.home.contract.HomeContract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

@ActivityScope
public class HomeModel extends BaseModel implements HomeContract.Model {

    private HashMap<Integer, Listener> listenerHashMap;

    @Inject
    public HomeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        this.listenerHashMap = new HashMap<>();
    }

    @Override
    public void findAllMusicData(Listener listener) {
        listenerHashMap.put(FIND_ALL_MUSIC_DATA, listener);
        new Thread() {
            @Override
            public void run() {
                List<MusicResp> musicRespList = new ArrayList<>();
                Connection connection = JDBCUtils.getConn();
                String sql = "select * from Music order by mpf desc";
                PreparedStatement ps = null;
                ResultSet resultSet = null;
                Message msg = new Message();
                msg.what = FIND_ALL_MUSIC_DATA;
                try {
                    ps = connection.prepareStatement(sql);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        MusicResp musicResp = new MusicResp(resultSet.getInt(1),//id
                                resultSet.getString(2),//name
                                resultSet.getDate(3),//upDate
                                resultSet.getString(4),//headString
                                resultSet.getString(5),//username
                                resultSet.getString(6),//userHeadString
                                resultSet.getString(7),//goodUsers
                                resultSet.getInt(8) //googNums
                        );
                        musicRespList.add(musicResp);
                    }
                    msg.arg1 = SuccessCode.SUCCESS.getCode();
                    msg.obj = musicRespList;
                } catch (SQLException e) {
                    e.printStackTrace();
                    msg.arg1 = AccountCode.CONNECT_FAILED.getCode();
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

    @Override
    public void updateMusicGood(String username, MusicResp musicResp, Listener listener) {
        listenerHashMap.put(UPDATE_MUSIC_GOOD, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "update Music set goodusers = ?, goodnums = ? where id = ?";
                PreparedStatement statement = null;
                Message msg = new Message();
                msg.what = UPDATE_MUSIC_GOOD;
                try {
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, musicResp.getGoogUsers());
                    statement.setInt(2, musicResp.getGoodNum());
                    statement.setInt(3, musicResp.getId());
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
