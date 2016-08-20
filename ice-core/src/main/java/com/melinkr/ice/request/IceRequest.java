package com.melinkr.ice.request;

import com.melinkr.ice.context.IceRequestContext;

/**
 * <pre>
 *    ICE服务的请求对象，请求方法的入参必须是继承于该类。
 * </pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public interface IceRequest {
    IceRequestContext getIceRequestContext() ;

    void setIceRequestContext(IceRequestContext iceRequestContext);
}
