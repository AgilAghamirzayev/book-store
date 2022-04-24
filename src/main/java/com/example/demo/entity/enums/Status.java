package com.example.demo.entity.enums;

public enum Status {
    ONLINE("online"),
    OFFLINE("offline"),
    DELETED("deleted");

    private String status;

    Status(String status) {
        this.status = status;
    }
}