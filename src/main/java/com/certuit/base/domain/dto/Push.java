package com.certuit.base.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Push {
    private String to;
    private String priority;
    private Notification notification;
    @JsonProperty(value = "registration_ids")
    private List<String> registrationIds;
    private DataNotification data;

    /**
     *
     * @param priority
     * @param notification
     * @param registrationds
     */
    public Push(String priority, Notification notification, List<String> registrationds) {
        this.priority = priority;
        this.notification = notification;
        this.registrationIds = registrationds;
//        this.data = notification;

    }

    /**
     *
     * @param to
     * @param priority
     * @param notification
     */
    public Push(String to, String priority, Notification notification) {
        this.to = to;
        this.priority = priority;
        this.notification = notification;

    }

    /**
     *
     * @param to
     * @param priority
     * @param dataNotification
     */
    public Push(String to, String priority, DataNotification dataNotification) {
        this.to = to;
        this.priority = priority;
        this.data = dataNotification;

    }

    /**
     *
     * @param to
     * @param priority
     * @param notification
     */
    public Push(String to, String priority, Notification notification, DataNotification dataNotification) {
        this.to = to;
        this.priority = priority;
        this.notification = notification;
        this.data = dataNotification;

    }

    /**
     *
     */
    public Push() {

    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public List<String> getRegistrationIds() {
        return registrationIds;
    }

    public void setRegistrationIds(List<String> registrationIds) {
        this.registrationIds = registrationIds;
    }

    public DataNotification getData() {
        return data;
    }

    public void setData(DataNotification data) {
        this.data = data;
    }
}
