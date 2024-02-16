package com.example.xcplusbase.base.exception;
/*
 * author: oliver
 * date: 2024-02-16 19:41
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalHandleException {

    @ResponseBody
    @ExceptionHandler(XcplusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse customExcption(XcplusException e){
        log.error("系统异常:",e.getMessage(),e);
        String errMessage  = e.getMessage();
        RestErrorResponse restErrorResponse = new RestErrorResponse(errMessage );
        return  restErrorResponse;

    }


    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse excption(Exception e){
        log.error("系统异常:",e.getMessage(),e);
        String errMessage  = e.getMessage();
        RestErrorResponse restErrorResponse = new RestErrorResponse(CommonErrorEnum.UNKOWN_ERROR.getErrMessage() );
        return  restErrorResponse;

    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        List<String > errors = new ArrayList<>();
        bindingResult.getFieldErrors().stream().forEach(item->errors.add(item.getDefaultMessage()));
        String errMessage  = StringUtils.join(errors,",");
        RestErrorResponse restErrorResponse = new RestErrorResponse(errMessage);
        return  restErrorResponse;

    }

}
