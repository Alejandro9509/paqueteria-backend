package com.certuit.base.domain.enums;

public enum Rol {
    USER(1, "Usuario", "USER"),
    SUPER_USER(2, "Administrador", "SUPER_USER"),
    OPERATOR(3, "Operador", "OPERATOR"),
    ADMIN(4, "Jefe de área", "ADMIN"),
    CLIENT_MANAGER(5, "Administrador de clientes", "CLIENT_MANAGER"),
    CALL_ASSESSOR(6, "Asesor telefónico", "CALL_ASSESSOR");

    private int id;
    private String name;
    private String systemName;

    Rol(int id, String name, String systemName) {
        this.id = id;
        this.name = name;
        this.systemName = systemName;
    }


    public static Rol valueOf(int id) {
        switch (id) {

            case 1:
                return USER;
            case 2 :
                return SUPER_USER;
            case 3:
                return OPERATOR;
            case 4:
                return ADMIN;
            case 5:
                return CLIENT_MANAGER;
            case 6:
                return CALL_ASSESSOR;
            default:
                return USER;
        }
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

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
