package com.meowing.loud.play.contract;

import com.meowing.loud.arms.base.IModel;
import com.meowing.loud.arms.base.IView;
import com.meowing.loud.arms.resp.MusicResp;

public interface PlayContract {
    interface View extends IView {

        /**
         * 获取音乐url结果
         * @param url
         */
        default void findMusicUrlResult(String url) {

        }

        /**
         * 更新点赞结果
         * @param isAdd true为点赞，false为取消点赞
         */
        default void updateMusicGoodResult(boolean isSuccess, MusicResp resp, boolean isAdd) {

        }

        /**
         * 更新收藏结果
         * @param isLike true为收藏，false为取消收藏
         */
        default void updateMusicLikeResult(boolean isSuccess, MusicResp resp, boolean isLike) {

        }

        /**
         * 更新收藏结果
         * @param isLike true为收藏，false为取消收藏
         */
        default void updateMusicStateResult(boolean isSuccess, MusicResp resp, boolean isPass) {

        }
    }

    interface Model extends IModel {

        int UPDATE_MUSIC_GOOD = 0;

        void updateMusicGood(String username, MusicResp musicResp, Listener listener);

        int UPDATE_MUSIC_LIKE = 1;

        void updateMusicLike(String username, MusicResp musicResp, Listener listener);

        int FIND_MUSIC_URL = 2;

        void findMusicUrl(int musicId, Listener listener);

        int UPDATE_MUSIC_STATE = 3;

        void updateMusicState(int musicId, boolean isPass, Listener listener);

        interface Listener {
            void onSuccess(Object obj);

            void onFailed(int errorId);
        }
    }
}
