package com.launcher.hamcl.user;

public class UserListBean {
    public String getUserNmae () {
        return userNmae;
    }

    public void setUserNmae (String userNmae) {
        this.userNmae = userNmae;
    }

    public int getUserType () {
        return userType;
    }

    public void setUserType (int userType) {
        this.userType = userType;
    }

    private String userNmae;
    private int userType;

    public String getUserPassword () {
        return userPassword;
    }

    public void setUserPassword (String userPassword) {
        this.userPassword = userPassword;
    }

    private String userPassword;
}
