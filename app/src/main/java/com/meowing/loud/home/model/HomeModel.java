package com.meowing.loud.home.model;

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
import com.meowing.loud.home.contract.HomeContract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
    public void findAllWaitMusicData(Listener listener) {
        listenerHashMap.put(FIND_ALL_WAIT_MUSIC_DATA, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "select id,name,headString,username,userHeadString,goodUsers,likeUsers from Music where state = ?";
                PreparedStatement ps = null;
                ResultSet resultSet = null;
                Message msg = new Message();
                msg.what = FIND_ALL_WAIT_MUSIC_DATA;
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, AppConstant.MUSIC_TYPE_WAIT);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        MusicResp musicResp = new MusicResp(resultSet.getInt(1),//id
                                resultSet.getString(2),//name
                                resultSet.getString(3),// headString
                                resultSet.getString(4),//username
                                resultSet.getString(5),//userHeadString
                                resultSet.getString(6),//goodUsers
                                resultSet.getString(7)//likeUsers
                        );
                        // 直接缓存到本地
                        LocalDataManager.getInstance().setWaitMusic(musicResp);
                    }
                    msg.arg1 = SuccessCode.SUCCESS.getCode();
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
    public void findAllPassMusicData(Listener listener) {
        listenerHashMap.put(FIND_ALL_PASS_MUSIC_DATA, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "select id,name,headString,username,userHeadString,goodUsers,likeUsers from Music where state = ?";
                PreparedStatement ps = null;
                ResultSet resultSet = null;
                Message msg = new Message();
                msg.what = FIND_ALL_PASS_MUSIC_DATA;
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, AppConstant.MUSIC_TYPE_PASS);
                    resultSet = ps.executeQuery();
                    LocalDataManager.getInstance().clearAllPassMusicList();

                    while (resultSet.next()) {
                        MusicResp musicResp = new MusicResp(resultSet.getInt(1),//id
                                resultSet.getString(2),//name
                                resultSet.getString(3),// headString
                                resultSet.getString(4),//username
                                resultSet.getString(5),//userHeadString
                                resultSet.getString(6),//goodUsers
                                resultSet.getString(7)//likeUsers
                        );
                        // 直接缓存到本地
                        LocalDataManager.getInstance().setPassMusic(musicResp);
                    }
                    msg.arg1 = SuccessCode.SUCCESS.getCode();
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
    public void findAllRefuseMusicData(Listener listener) {
        listenerHashMap.put(FIND_ALL_REFUSE_MUSIC_DATA, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "select id,name,headString,username,userHeadString,goodUsers,likeUsers from Music where state = ?";
                PreparedStatement ps = null;
                ResultSet resultSet = null;
                Message msg = new Message();
                msg.what = FIND_ALL_REFUSE_MUSIC_DATA;
                try {
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, AppConstant.MUSIC_TYPE_PASS);
                    resultSet = ps.executeQuery();

                    while (resultSet.next()) {
                        MusicResp musicResp = new MusicResp(resultSet.getInt(1),//id
                                resultSet.getString(2),//name
                                resultSet.getString(3),// headString
                                resultSet.getString(4),//username
                                resultSet.getString(5),//userHeadString
                                resultSet.getString(6),//goodUsers
                                resultSet.getString(7)//likeUsers
                        );
                        // 直接缓存到本地
                        LocalDataManager.getInstance().setRefuseMusic(musicResp);
                    }
                    msg.arg1 = SuccessCode.SUCCESS.getCode();
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
                    statement.setString(1, musicResp.getGoogUsers());
                    statement.setInt(2, musicResp.getId());
                    int rs = statement.executeUpdate();
                    if (rs > 0) {
                        msg.arg1 = SuccessCode.SUCCESS.getCode();
                    } else {
                        msg.arg1 = AccountCode.UPDATE_MUSIC_LIKE_FAILED.getCode();
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
    public void addMusic(MusicResp musicResp, Listener listener) {
        listenerHashMap.put(ADD_MUSIC, listener);
        new Thread() {
            @Override
            public void run() {
                Connection connection = JDBCUtils.getConn();
                String sql = "insert into Music(name,uri,headString,username,userHeadString,state) values (?,?,?,?,?,?)";
                PreparedStatement statement = null;
                Message msg = new Message();
                msg.what = ADD_MUSIC;
                try {
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, musicResp.getName());
                    statement.setString(2, musicResp.getUrl());
                    statement.setString(3, musicResp.getHeadString());
                    statement.setString(4, musicResp.getUsername());
                    statement.setString(5, musicResp.getUserHeadString());
                    statement.setInt(6, AppConstant.MUSIC_TYPE_WAIT);   //状态是待审核
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
