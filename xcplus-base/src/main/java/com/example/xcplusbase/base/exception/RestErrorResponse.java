package com.example.xcplusbase.base.exception;
/*
 * author: oliver
 * date: 2024-02-16 19:29
 */

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class RestErrorResponse implements Serializable {
    private String errMessage;
    private LocalDateTime localDateTime;

    public RestErrorResponse(String errMessage) {
        this.errMessage = errMessage;
        this.localDateTime = LocalDateTime.now();
    }

    public RestErrorResponse() {
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
