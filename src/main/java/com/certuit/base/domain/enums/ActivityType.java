package com.certuit.base.domain.enums;

public enum  ActivityType {
    INTERNAL(1, "Interna"),
    SYSTEM(2, "Sistema"),
    USER(3, "Usuario");

    private int id;
    private String name;

    ActivityType(int id) {
        this.id = id;
    }

    ActivityType(int id, String name) {
        this.id = id;
        this.name = name;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
