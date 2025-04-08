package com.certuit.base.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

@Service
public class DBConection {
    public static HikariDataSource ds;
    private final Map<String, HikariDataSource> conecctions = new HashMap<>();
    private Boolean actualizado = false;

    private  static Connection connectDB(String driverClassNameDB, String jdbcDriverUrl, String User, String Password) {
        Connection connection = null;

        try {
            Class.forName(driverClassNameDB);
        } catch (ClassNotFoundException e) {
            System.out.println("error en: " + driverClassNameDB);
        }
        try {
            connection = DriverManager.getConnection(jdbcDriverUrl, User, Password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static String getEnviromentURL(){
        return "http://paqueteria.desarrollo.gmtransport.co:8081/";
    }

    public synchronized  Connection getconnection(String rfc) throws SQLException, FileNotFoundException {
        if (conecctions.get(rfc) == null) {
            HikariConfig config = checkUpdatedRFC(rfc);
            /*config.setJdbcUrl("jdbc:jtds:sqlserver://190.9.53.4:53999/GMTERP_WEB_PQ_" + rfc + ";ssl=require;useLOBs=false;autoReconnect=true");
            config.setUsername("sa");
            config.setPassword("GTpE003*");*/
            config.setConnectionTestQuery("select 1");
            config.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
            conecctions.put(rfc,new HikariDataSource(config));
        }
        return conecctions.get(rfc).getConnection();
    }

    /**
        Proceso para buscar en el archivo del unificado si está el registro del RFC; en este archivo estará registrado
        el rfc, servidor, versión y accesos donde está la BD del rfc correspondiente, si no se encuentra el rfc en el
        archivo se utilizará la configuración por default.
    */
    private HikariConfig checkUpdatedRFC(String rfc) {
        HikariConfig config = new HikariConfig();
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("/opt/apache-tomcat-8.5.40/webapps/rfc.json"));
            JSONArray list = (JSONArray) jsonObject.get("clientes");
            Iterator<Object> iterator = list.iterator();
            while (iterator.hasNext()) {
                JSONObject json = (JSONObject) iterator.next();
                if(Objects.equals(rfc, json.get("rfc").toString())){
                    actualizado = true;
                    config.setJdbcUrl(json.get("server").toString() + json.get("bdName").toString() + ";ssl=require;useLOBs=false;autoReconnect=true");
                    config.setUsername(json.get("username").toString());
                    config.setPassword(json.get("password").toString());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!actualizado){
            config.setJdbcUrl("jdbc:jtds:sqlserver://190.9.53.4:53999/GMTERP_WEB_PQ_" + rfc + ";ssl=require;useLOBs=false;autoReconnect=true");
            config.setUsername("sa");
            config.setPassword("GTpE003*");
        }
        return config;
    }
}
