package com.meowing.loud.home.contract;

import com.meowing.loud.arms.base.IModel;
import com.meowing.loud.arms.base.IView;
import com.meowing.loud.arms.resp.MusicResp;

public interface HomeContract {
    interface View extends IView {
        /**
         * 查找音乐列表成功
         * @param musicRespList
         */
        default void findAllMusicSuccess(){

        }

        /**
         * 更新点赞失败
         * @param isAdd true为点赞，false为取消点赞
         */
        default void updateMusicGoodResult(boolean isSuccess, MusicResp resp, boolean isAdd) {

        }
    }

    interface Model extends IModel {
        int FIND_ALL_MUSIC_DATA = 0;

        void findAllMusicData(Listener listener);

        int UPDATE_MUSIC_GOOD = 1;

        void updateMusicGood(String username, MusicResp musicResp, Listener listener);

        interface Listener {
            void onSuccess(Object obj);

            void onFailed(int errorId);
        }
    }
}
