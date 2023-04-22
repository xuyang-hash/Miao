package com.meowing.loud.home.contract;

import com.meowing.loud.arms.base.IModel;
import com.meowing.loud.arms.base.IView;
import com.meowing.loud.arms.resp.MusicResp;

public interface HomeContract {
    interface View extends IView {

        /**
         * 查找待审核的音乐列表成功
         */
        default void findAllWaitMusicResult(){

        }

        /**
         * 查找审核通过的音乐列表成功
         */
        default void findAllPassMusicResult(){

        }

        /**
         * 查找审核未通过的音乐列表成功
         */
        default void findAllRefuseMusicResult(){

        }

        /**
         * 更新点赞失败
         * @param isAdd true为点赞，false为取消点赞
         */
        default void updateMusicGoodResult(boolean isSuccess, MusicResp resp, boolean isAdd) {

        }

        /**
         * 上传音乐回调
         * @param isSuccess
         */
        default void addMusicResult(boolean isSuccess) {

        }
    }

    interface Model extends IModel {
        int FIND_ALL_WAIT_MUSIC_DATA = 0;

        /**
         * 获取所有待审核的音乐
         */
        void findAllWaitMusicData(Listener listener);

        int FIND_ALL_PASS_MUSIC_DATA = 1;

        /**
         * 获取所有审核通过的音乐
         */
        void findAllPassMusicData(Listener listener);

        int FIND_ALL_REFUSE_MUSIC_DATA = 2;

        /**
         * 获取所有审核未通过的音乐
         */
        void findAllRefuseMusicData(Listener listener);

        int UPDATE_MUSIC_GOOD = 3;

        /**
         * 更新本音乐的赞
         */
        void updateMusicGood(String username, MusicResp musicResp, Listener listener);

        int UPDATE_MUSIC_LIKE = 4;
        /**
         * 更新本音乐的收藏
         */
        void updateMusicLike(String username, MusicResp musicResp, Listener listener);

        int ADD_MUSIC = 5;

        /**
         * 上传音乐
         */
        void addMusic(MusicResp musicResp, Listener listener);

        interface Listener {
            void onSuccess(Object obj);

            void onFailed(int errorId);
        }
    }
}
