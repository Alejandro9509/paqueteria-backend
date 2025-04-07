package com.certuit.base.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {
    private String sound;
    private String title;
    private String body;
    @JsonProperty("click_action")
    private String clickAction;
    private int id;
    private String nombre;

    public Notification() {
    }

    public Notification(String sound, String title, String body) {
        this.sound = sound;
        this.title = title;
        this.body = body;
    }

    public Notification(String sound, String title, String body, String clickAction) {
        this.sound = sound;
        this.title = title;
        this.body = body;
        this.clickAction = clickAction;
    }

    public Notification(String sound, String title, String body, String clickAction, int id) {
        this.sound = sound;
        this.title = title;
        this.body = body;
        this.clickAction = clickAction;
        this.id = id;
    }

    public Notification(String sound, String title, String body, String clickAction, String nombre) {
        this.sound = sound;
        this.title = title;
        this.body = body;
        this.clickAction = clickAction;
        this.nombre = nombre;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getClickAction() {
        return clickAction;
    }

    public void setClickAction(String clickAction) {
        this.clickAction = clickAction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
