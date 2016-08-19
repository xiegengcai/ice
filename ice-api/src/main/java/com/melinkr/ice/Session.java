package com.melinkr.ice;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/18.
 */
public interface Session extends Serializable {
    /**
     * 设置属性
     * @param name
     * @param value
     */
    void setAttribute(String name, Object value);

    /**
     * 获取属性
     * @param name
     * @return
     */
    Object getAttribute(String name);

    /**
     * 获取所有的属性
     * @return
     */
    Map<String, Object> getAllAttributes();

    /**
     * 删除属性条目
     * @param name
     */
    void removeAttribute(String name);
}
