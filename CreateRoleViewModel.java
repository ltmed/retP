package com.intellica.retouch.model;

import com.intellica.retouch.entity.RetouchRoleEntity;
import com.intellica.retouch.entity.RetouchRoleRightsEntity;

import java.util.List;

public class CreateRoleViewModel extends Model {

    private RetouchRoleEntity role;
    private List<RetouchRoleRightsEntity> rights;


    public RetouchRoleEntity getRole() {
        return role;
    }

    public void setRole(RetouchRoleEntity role) {
        this.role = role;
    }

    public List<RetouchRoleRightsEntity> getRights() {
        return rights;
    }

    public void setRights(List<RetouchRoleRightsEntity> rights) {
        this.rights = rights;
    }
}
