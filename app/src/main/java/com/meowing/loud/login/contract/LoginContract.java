package com.meowing.loud.login.contract;

import com.meowing.loud.arms.base.IModel;
import com.meowing.loud.arms.base.IView;
import com.meowing.loud.arms.resp.UserResp;

public interface LoginContract {
    /**
     * 对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IView {
        /**
         * 登录成功回调
         */
        default void onAccountLoginResult() {

        }

        /**
         * 注册成功回调
         */
        default void onRegisterResult() {

        }

        default void onFindUserResult(boolean iSuccess, UserResp userResp, int errorId) {

        }

        default void setQuestionAndAnswerResult() {

        }

        default void updatePassResult() {

        }
    }

    /**
     * Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
     */
    interface Model extends IModel {

        int OPT_LOGIN_USER = 0;

        /**
         * 用户登录
         * @param account   用户名
         * @param pwd       密码
         * @param listener
         */
        void userLogin(String account, String pwd, Listener listener);

        int OPT_LOGIN_ADMIN = 1;

        /**
         * 管理员登录
         * @param account   用户名
         * @param pwd       密码
         * @param listener
         */
        void adminLogin(String account, String pwd, Listener listener);

        int OPT_REGISTER = 2;

        /**
         * 注册账号
         * @param username  用户名
         * @param password  密码
         * @param listener
         */
        void registerUser(String username, String password, Listener listener);

        int OPT_FIND_USER = 3;

        /**
         * 根据用户名去查找用户信息
         * @param username  用户名
         * @return  若查到了，则传值，若查不到，则传null
         */
        void findUser(String username, Listener listener);

        int SET_Q_AND_A = 4;

        /**
         * 设置密保问题和答案
         * @param username      用户名
         * @param question1     密保问题1
         * @param answer1       密保答案1
         * @param question2     密保问题2
         * @param answer2       密保答案2
         */
        void setQuestionAndAnswer(String username, String question1, String answer1, String question2, String answer2, Listener listener);

        int UPDATE_PASS = 5;

        /**
         * 更改密码
         * @param username  用户名
         * @param password  需要更新的密码
         */
        void updatePass(String username, String password, Listener listener);

        public interface Listener {
            void onSuccess(Object obj);

            void onFailed(int errorId);
        }
    }
}
