package com.meowing.loud.arms.entity;

public class MessageWrap {
    public int requestCode;
    public  Object message;


    public MessageWrap(int mRequestCode, Object mMessage) {
        requestCode = mRequestCode;
        message = mMessage;
    }
}
