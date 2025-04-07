package com.certuit.base.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TicketStatus {
    NEW(1, "Nuevo"),
    ASSIGNED(2, "Asignado"),
    RESOLVED(3,"Resuelto"),
    NEW_ANSWER_AUTHOR(4, "Respuesta nueva cliente"),
    SERVING(5, "Atendiendo"),
    NEW_ANSWER_OWNER(6, "Respuesta nueva asesor"),
    CLOSE(7, "Cerrado");


    private int id;
    private String name;


    TicketStatus(int id, String name) {
        this.id = id;
        this.name = name;

    }

    public static TicketStatus of(int ticketStatus) {
        return Stream.of(TicketStatus.values())
                .filter(p -> p.getId() == ticketStatus)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
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
