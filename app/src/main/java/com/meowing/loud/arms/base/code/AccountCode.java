package com.meowing.loud.arms.base.code;

import com.meowing.loud.R;

public enum AccountCode implements BusinessCode {

    UNKNOW_ERROR(-1, R.string.common_unknown_error),
    CONNECT_FAILED(-2, R.string.common_connect_failed),
    USER_UNKNOWN_ERROR(-4000, R.string.account_login_unknown_error),
    LOGIN_USER_OR_PWD_ERROR(-4001, R.string.account_login_error_name_psw),
    LOGIN_USERNAME_NOT_EXIST(-4002, R.string.account_login_find_user_empty),
    LOGIN_CONNECT_FAILED(-4003, R.string.account_register_connect_failed),
    REGISTER_ALREADY_ADD(-4010, R.string.account_login_name_already_exist),
    REGISTER_SUBMITT_FAILED(-4011, R.string.account_register_submit_failed),
    REGISTER_CONNECT_FAILED(-4012, R.string.account_register_connect_failed),
    FIND_USER_IS_EMPTY(-4020, R.string.account_login_find_user_empty),
    FIND_USER_NO_EMPTY(-4021, R.string.account_login_find_user_no_empty),
    FIND_USER_CONNECT_FAILED(-4022, R.string.account_register_connect_failed),
    SET_QUESTION_AND_ANSWER_FAILED(-4030, R.string.common_save_failed),
    SET_QUESTION_AND_ANSWER_CONNECT_ERROR(-4031, R.string.account_set_confidentiality_connect_failed),

    UPDATE_PASS_FAILED(-4040, R.string.common_save_failed),
    UPDATE_PASS_CONNECT_ERROR(-4041, R.string.account_set_confidentiality_connect_failed),

    UPDATE_HEAD_FAILED(-4051, R.string.me_account_setting_profile_photo_failed),

    UPDATE_MUSIC_GOOD_FAILED(-5001, R.string.music_good_add_failed),

    UPDATE_MUSIC_LIKE_FAILED(-5011, R.string.music_like_add_failed),

    UPDATE_MUSIC_STATE_FAILED(-5021, R.string.music_state_pass_failed),

    ADD_MUSIC_SAVE_FAILED(-5031, R.string.common_save_failed),
    ;

    private final int code;
    private final int message;

    AccountCode(Integer code, int message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public int getMessage() {
        return message;
    }
}
