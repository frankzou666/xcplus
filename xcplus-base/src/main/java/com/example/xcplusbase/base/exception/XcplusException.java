package com.example.xcplusbase.base.exception;
/*
 * author: oliver
 * date: 2024-02-16 19:32
 */

public class XcplusException extends RuntimeException {
    private String errMessage;

    public XcplusException() {
    }

    public XcplusException(String errMessage) {
        this.errMessage = errMessage;
    }

    public XcplusException(String message, String errMessage) {
        super(message);
        this.errMessage = errMessage;
    }
    public  static void cast(String message){
        throw new RuntimeException(message);

    }

    public  static void cast(CommonErrorEnum commonErrorEnum){
        throw new RuntimeException(commonErrorEnum.getErrMessage());

    }
}
