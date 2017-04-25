package com.xiegengcai.ice.request;

import com.xiegengcai.ice.annotation.HttpAction;

import java.util.Map;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public class IceHttpRequest {

    private Map<String, String> params;
    // 可能是IP6、IP4
    private String clientIp;
    private HttpAction httpAction;

    public IceHttpRequest() {
    }

    public IceHttpRequest(Map<String, String> params, String clientIp, HttpAction httpAction) {
        this.params = params;
        this.clientIp = clientIp;
        this.httpAction = httpAction;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getParameter(String name) {
        return this.params.get(name);
    }

    public HttpAction getHttpAction() {
        return httpAction;
    }

    public void setHttpAction(HttpAction httpAction) {
        this.httpAction = httpAction;
    }
}
