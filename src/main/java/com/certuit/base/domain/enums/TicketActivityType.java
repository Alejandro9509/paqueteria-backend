package com.certuit.base.domain.enums;

import java.util.stream.Stream;

public enum TicketActivityType {
    ASSIGN(1, "ASSIGN", ActivityType.INTERNAL),
    COMMENT(2, "COMMENT", ActivityType.USER),
    CLOSE(3, "CLOSE", ActivityType.INTERNAL),
    UN_ASSIGN(4, "UN_ASSIGN", ActivityType.INTERNAL),
    RE_OPEN(5, "RE_OPEN", ActivityType.INTERNAL),
    PRIORITY_CHANGED(6, "PRIORITY_CHANGED", ActivityType.INTERNAL),
    TYPE_CHANGED(7, "TYPE_CHANGED", ActivityType.INTERNAL),
    OTHER(8, "OTHER", ActivityType.SYSTEM);


    private int id;
    private String description;
    private ActivityType type;

    TicketActivityType(int id, String description, ActivityType type) {
        this.id = id;
        this.description = description;
        this.type = type;
    }

    public static TicketActivityType of(int tiketActivityStatus) {
        return Stream.of(TicketActivityType.values())
                .filter(p -> p.getId() == tiketActivityStatus)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }
}
