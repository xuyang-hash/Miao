package com.meowing.loud.login.contract;

import com.meowing.loud.arms.base.IModel;
import com.meowing.loud.arms.base.IView;
import com.meowing.loud.arms.resp.UserResp;
import com.meowing.loud.login.model.LoginModel;

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
    }

    /**
     * Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
     */
    interface Model extends IModel {
        /**
         * 用户登录
         * @param account   用户名
         * @param pwd       密码
         * @param listener
         */
        void userLogin(String account, String pwd, LoginModel.Listener listener);

        /**
         * 管理员登录
         * @param account   用户名
         * @param pwd       密码
         * @param listener
         */
        void adminLogin(String account, String pwd, LoginModel.Listener listener);

        /**
         * 注册账号
         * @param username  用户名
         * @param password  密码
         * @param listener
         */
        void registerUser(String username, String password, LoginModel.Listener listener);

        /**
         * 根据用户名去查找用户信息
         * @param username  用户名
         * @return  若查到了，则传值，若查不到，则传null
         */
        void findUser(String username, LoginModel.Listener listener);
    }
}
