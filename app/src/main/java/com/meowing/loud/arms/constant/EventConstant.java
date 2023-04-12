package com.meowing.loud.arms.constant;

public interface EventConstant {
    interface ModuleLogin {
        /**
         * 注册账号已存在返回
         */
        public static final int ACCOUNT_REGISTER_SUCCESS = 0x100001;
        /**
         * 注册账号之设置密保问题成功
         */
        public static final int ACCOUNT_REGISTER_SET_Q_AND_A_SUCCESS = 0x100002;
    }
}
