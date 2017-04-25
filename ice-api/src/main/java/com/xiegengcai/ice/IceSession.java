package com.xiegengcai.ice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/18.
 */
public class IceSession implements Session {

    private static final long serialVersionUID = 955522908847575593L;
    private Map<String, Object> attributes = new HashMap<>();

    @Override
    public void setAttribute(String name, Object obj) {
        attributes.put(name, obj);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Map<String, Object> getAllAttributes() {
        Map<String, Object> tempAttributes = new HashMap<>(
                attributes.size());
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            tempAttributes.put(entry.getKey(), entry.getValue());
        }
        return tempAttributes;
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }
}
