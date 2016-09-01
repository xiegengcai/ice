package com.melinkr.ice.dispatcher;

import com.melinkr.ice.request.IceHttpRequest;
import com.melinkr.ice.response.IceResponse;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public interface Dispatcher {

    IceResponse dispatch(IceHttpRequest request);
}
