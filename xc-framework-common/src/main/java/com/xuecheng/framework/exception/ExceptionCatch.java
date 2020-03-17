package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    /**
     * 线程安全 已进入就不可改变的map
     */
    private static ImmutableMap<Class,ResultCode> immutableMap;

    protected static ImmutableMap.Builder<Class,ResultCode> builder = ImmutableMap.builder();

    static {
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALIDPARAM);

    }


    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult getCustomException(CustomException customException){
        LOGGER.error("customException");
        return new ResponseResult(customException.getResultCode());
    }

    /**
     * 不可预知异常
     * @param excteption 不可预知浴场
     * @return 返回信息
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult getException(Exception excteption){
        LOGGER.error("customException");
        if(ObjectUtils.isEmpty(immutableMap)){
            immutableMap = builder.build();
        }
        if(immutableMap.containsKey(excteption.getClass())){
            return new ResponseResult(CommonCode.INVALIDPARAM);
        }
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }

}
