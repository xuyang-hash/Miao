package com.meowing.loud.arms.resp;

import com.meowing.loud.arms.base.code.ErrorCodeManager;
import com.meowing.loud.arms.base.code.SuccessCode;

public class BaseResp<T> {
    public String msg;
    public int code;
    public T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return ErrorCodeManager.isSuccessCode(code, SuccessCode.class);
    }

    public boolean isNormal() {
        return ErrorCodeManager.isSuccessCode(code, SuccessCode.class);
    }
}
