package com.certuit.base.service.base;

import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeguimientoService {
    public JSONArray getPaquetesSeguimiento(int id, int tipo, Connection jdbcConnection)
            throws SQLException, Exception {
        if (tipo == 4 || tipo == 5){
            tipo = 3;
        }
        String query = "exec usp_ProSeguimientoPaquetesPQ " + id + "," + tipo;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getConceptosSeguimiento(int id, int tipo, Connection jdbcConnection)
            throws SQLException, Exception {
        if (tipo == 4 || tipo == 5){
            tipo = 3;
        }
        String query = "exec usp_ProSeguimientoConceptosPQ " + id + "," + tipo;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getBitacoraSeguimiento(int id, int tipo, Connection jdbcConnection)
            throws SQLException, Exception {
        if (tipo == 4 || tipo == 5){
            tipo = 3;
        }
        String query = "exec usp_ProSeguimientBitacoraPQ " + id + "," + tipo;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray jArr=UtilFuctions.convertArray(rs);
        int posGuia=-1;
        int posEmb=-1;
        for(int i=0;i<jArr.length();i++)
        {
            if(jArr.get(i).toString().contains("Se registró la guía"))
            {
                posGuia=i;
                continue;
            }
            if(jArr.get(i).toString().contains("Se registró el embarque"))
            {
                posEmb=i;
            }
        }
        if(posGuia>posEmb && posEmb>-1)
        {
            Object obj=jArr.get(posGuia);
            jArr.put(posGuia, jArr.get(posEmb));
            jArr.put(posEmb,obj);
        }
        return jArr;
    }

    public Object[] getFoliosRecoleccion(Connection jdbcConnection)
            throws SQLException, Exception {
        String query = "SELECT pe.FolioRecoleccion FROM ProRecoleccionPQ pe";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        List<String> al = new ArrayList<String>();
        while(rs.next()){
            al.add(rs.getString("FolioRecoleccion"));
        }
        return al.toArray();
    }

    public Object[] getFoliosEmbarques(Connection jdbcConnection)
            throws SQLException, Exception {
        String query = "SELECT pe.FolioEmbarque FROM ProEmbarquePQ pe";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        List<String> al = new ArrayList<String>();
        while(rs.next()){
            al.add(rs.getString("FolioEmbarque"));
        }
        return al.toArray();
    }
    public  Object[] getFoliosGuias(Connection jdbcConnection)
            throws SQLException, Exception {
        String query = "SELECT pg.FolioGuia FROM ProGuiaPQ pg";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        List<String> al = new ArrayList<String>();
        while(rs.next()){
            al.add(rs.getString("FolioGuia"));
        }
        return al.toArray();
    }
}
