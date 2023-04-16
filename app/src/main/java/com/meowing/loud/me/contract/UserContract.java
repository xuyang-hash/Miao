package com.meowing.loud.me.contract;

import com.meowing.loud.arms.base.IModel;
import com.meowing.loud.arms.base.IView;
import com.meowing.loud.arms.resp.UserResp;

public interface UserContract {

    interface View extends IView {
        /**
         * 修改头像回调
         * @param isSuccess
         */
        default void onUpdateHeadResult(boolean isSuccess) {

        }

        default void onFindUserResult(boolean isSuccess, UserResp userResp, int errorId) {

        }
    }

    interface Model extends IModel {
        int UPDATE_HEAD = 0;

        /**
         * 修改头像
         * @param userResp
         * @param listener
         */
        void updateHead(UserResp userResp, Listener listener);

        int OPT_FIND_USER = 1;

        /**
         * 根据用户名去查找用户信息
         * @param username  用户名
         * @return  若查到了，则传值，若查不到，则传null
         */
        void findUser(String username, Listener listener);

        interface Listener {
            void onSuccess(Object obj);

            void onFailed(int errorId);
        }
    }
}
