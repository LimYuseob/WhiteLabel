package com.board.whitelabel.entity;

import lombok.Getter;

@Getter
public enum MemberRole {

    USER("ROLE_USER"), ADMIN("ROLE_ADMIN"), MANAGER("ROLE_MANAGER");
    MemberRole(String value) {
        this.value = value;
    }
    private String value;
    }