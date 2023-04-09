package com.meowing.loud.arms.base.code;

import com.meowing.loud.R;

public enum SuccessCode implements BusinessCode{
    SUCCESS(2000, R.string.common_success),
    SUCCESS_CAPS(200,R.string.common_success),
    SUCCESS_CHECK(0,R.string.common_success);
    private final int code;
    private final int message;

    SuccessCode(int code, int message) {
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
