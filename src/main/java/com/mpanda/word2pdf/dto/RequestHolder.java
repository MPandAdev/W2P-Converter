package com.mpanda.word2pdf.dto;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class RequestHolder {

    /**
     * 初始化线程本地
     */
    private static final ThreadLocal<String> requestHolder = new ThreadLocal<>();
    
    /**
     * 当前线程添加request
     *
     * @param request
     */
    public static void add(String request){
        requestHolder.set(request);
    }
    
    /**
     * 获取request id
     *
     * @return
     */
    public static String getRequestId(){
        String requestId;
        if(requestHolder.get() == null){
            requestId = UUID.randomUUID().toString().replaceAll("-", "");
            requestHolder.set(requestId);
        }else{
            requestId = requestHolder.get();
        }
        return requestId;
    }

    /**
     * 删除线程本地
     */
    public static void remove(){
        requestHolder.remove();
    }
    
}