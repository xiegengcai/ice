package com.xiegengcai.ice.interceptor;

import java.util.List;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/23.
 */
public class IterceptorChain {
    private List<Interceptor> interceptors;

    public IterceptorChain(){

    }

    public IterceptorChain(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }
}
