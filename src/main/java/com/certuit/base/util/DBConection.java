package com.certuit.base.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Service
public class DBConection {
    public static HikariDataSource ds;
    private final Map<String, HikariDataSource> conecctions = new HashMap<>();


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
        boolean actualizado = false;

        Path jsonPath = Paths.get("/opt/apache-tomcat-8.5.40/webapps/rfc.json");

        try {
            String jsonContent = Files.readString(jsonPath);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonContent);
            JSONArray clientes = (JSONArray) jsonObject.get("clientes");

            for (Object obj : clientes) {
                JSONObject cliente = (JSONObject) obj;
                String rfcCliente = String.valueOf(cliente.get("rfc"));

                if (rfc.equalsIgnoreCase(rfcCliente)) {
                    actualizado = true;
                    String server = String.valueOf(cliente.get("server"));
                    String bdName = String.valueOf(cliente.get("bdName"));
                    String username = String.valueOf(cliente.get("username"));
                    String password = String.valueOf(cliente.get("password"));

                    config.setJdbcUrl(server + bdName + ";ssl=require;useLOBs=false;autoReconnect=true");
                    config.setUsername(username);
                    config.setPassword(password);
                    break;
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace(); // Idealmente usa logger: log.error("Error leyendo rfc.json", e);
        }

        if (!actualizado) {
            config.setJdbcUrl("jdbc:jtds:sqlserver://190.9.53.4:53999/GMTERP_WEB_PQ_" + rfc + ";ssl=require;useLOBs=false;autoReconnect=true");
            config.setUsername("sa");
            config.setPassword("GTpE003*");
        }

        return config;
    }
}
