package com.xiegengcai.ice.request;

import com.xiegengcai.ice.annotation.NoSign;

import javax.validation.constraints.NotNull;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/22.
 */
public class LoginRequest extends AbstracetIceRequest{

    @NotNull
    private String accountName;

    @NotNull
    @NoSign
    private String password;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
