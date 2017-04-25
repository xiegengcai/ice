package com.xiegengcai.ice.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/23.
 */
@Service
public class BlacklistServiceImpl implements BlacklistService{
    @Override
    public Set<String> blacklist() {
        return Collections.EMPTY_SET;
    }
}
