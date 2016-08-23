package com.melinkr.ice.interceptor;

import com.melinkr.ice.request.IceRequest;
import com.melinkr.ice.response.IceResponse;

import java.util.List;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/23.
 */
public class BaseInterceptor<T> implements Interceptor<T> {

    protected List<String> ignoreParams;
    protected List<List<String>> ignoreValues;

    @Override
    public void setIgnoreParams(List<String> ignoreParams) {
        this.ignoreParams = ignoreParams;
    }

    @Override
    public void setIgnoreValues(List<List<String>> values) {
        this.ignoreValues = values;
    }

    @Override
    public IceResponse<T> beforeService(IceRequest iceRequest) {
        return null;
    }

    @Override
    public IceResponse<T> afterService(IceRequest iceRequest, Object iceResponse) {
        return null;
    }

    @Override
    public boolean isMatch(IceRequest iceRequest) {
        // 有配置忽略属性
        if (this.ignoreParams != null && this.ignoreParams.size() > 0) {
            for (int i = 0; i < ignoreParams.size(); i++) {
                if (this.ignoreValues != null && this.ignoreValues.size() > i) {
                    // 当前属性值
                    String paramValue = iceRequest.getIceRequestContext().getParam(this.ignoreParams.get(i));
                    for(String value : ignoreValues.get(i)) {
                        // 寻找属性值，找到忽略
                        if (value.equals(paramValue)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
