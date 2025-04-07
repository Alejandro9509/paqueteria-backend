package com.certuit.base.domain.enums;

public enum ClickAction {
    OPEN_TICKET_ACTIVITY(1, "OPEN_TICKET_ACTIVITY"),
    OPEN_NOTICE_ACTIVITY(2, "OPEN_NOTICE_ACTIVITY"),
    OPEN_CHAT_ACTIVITY(3, "OPEN_CHAT_ACTIVITY");

    int id;
    String descripcion;

    private ClickAction(int id) {
        this.id = id;
    }

    private ClickAction(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public static ClickAction valueOf(int id) {
        switch (id) {
            case 1:
                return OPEN_TICKET_ACTIVITY;
            case 2:
                return OPEN_NOTICE_ACTIVITY;
            default:
                return OPEN_CHAT_ACTIVITY;
        }
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
