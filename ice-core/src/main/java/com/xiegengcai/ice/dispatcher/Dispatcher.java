package com.xiegengcai.ice.dispatcher;

import com.xiegengcai.ice.request.IceHttpRequest;
import com.xiegengcai.ice.response.IceResponse;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/20.
 */
public interface Dispatcher {

    IceResponse dispatch(IceHttpRequest request);
}
