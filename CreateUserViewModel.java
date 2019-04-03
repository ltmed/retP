package com.intellica.retouch.model;

import com.intellica.retouch.entity.RetouchUserEntity;

import java.util.List;

public class CreateUserViewModel extends Model {
    private RetouchUserEntity user;
    private List<String> roleIdList;

    public RetouchUserEntity getUser() {
        return user;
    }

    public void setUser(RetouchUserEntity user) {
        this.user = user;
    }

    public List<String> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
