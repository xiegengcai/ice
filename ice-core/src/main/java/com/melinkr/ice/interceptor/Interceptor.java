package com.melinkr.ice.interceptor;

import com.melinkr.ice.context.IceRequestContext;
import com.melinkr.ice.request.IceRequest;
import com.melinkr.ice.response.IceResponse;

import java.util.List;

/**
 * 拦截器接口
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/23.
 */
public interface Interceptor<T> {
    /**
     * 设置连接属性列表
     * @param ignoreParams
     */
    void setIgnoreParams(List<String> ignoreParams);

    /**
     * 设置忽略拦截值列表，对应 {@link Interceptor#setIgnoreParams(List)}方法的属性值
     * @param values
     */
    void setIgnoreValues(List<List<String>> values);

    /**
     * 在进行服务之前调用,如果在方法中往{@link IceRequest 设置了{@link IceResponse}（相当于已经产生了响应了）,
     * 所以服务将直接返回，不往下继续执行，反之服务会继续往下执行直到返回响应
     *
     * @param uipRequestContext
     */
    IceResponse<T> beforeService(IceRequest iceRequest);

    /**
     * 在进行服务之后调用,如果在方法中往{@link IceRequest}设置了{@link com.melinkr.ice.response.IceResponse}（相当于已经产生了响应了）,
     * 所以服务将直接返回，不往下继续执行，反之服务会继续往下执行直到返回响应
     *
     */
    IceResponse<T> afterService(IceRequest iceRequest, Object iceResponse);

    /**
     * 该方法返回true时才实施拦截，否则不拦截。可以通过{@link IceRequestContext}
     *
     * @param iceRequest
     * @return
     */
    boolean isMatch(IceRequest iceRequest);
}
