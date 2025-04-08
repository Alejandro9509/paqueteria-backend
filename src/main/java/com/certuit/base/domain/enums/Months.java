package com.certuit.base.domain.enums;

public enum Months {
    JANUARY(1, "Enero"),
    FEBRUARY(2, "Febrero"),
    MARCH(3, "Marzo"),
    APRIL(4,"Abril"),
    MAY(5, "Mayo"),
    JUNE(6, "Junio"),
    JULY(7, "Julio"),
    AUGUST(8, "Agosto"),
    SEPTEMBER(9, "Septiembre"),
    OCTOBER(10, "Octubre"),
    NOVEMBER(11, "Noviembre"),
    DECEMBER(12, "Diciembre");

    private int id;
    private String name;

    Months(int id) {
        this.id = id;
    }

    Months(int id, String name) {
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

    public static String getMonthByInt(int id){
        switch (id){
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
            default:
                return "Error de Mes";
        }
    }
}
