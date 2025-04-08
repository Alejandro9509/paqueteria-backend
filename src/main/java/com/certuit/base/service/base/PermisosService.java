package com.certuit.base.service.base;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class PermisosService {

    public JSONArray getPermisosUsuario(int id, Connection jdbcConnection) throws Exception{
        String query = "select IdProceso from CatUsuariosDerechos where IdUsuario ="+id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return UtilFuctions.convertArray(rs);
    }
}
