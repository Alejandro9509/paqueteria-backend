package com.certuit.base.service.base;

import com.certuit.base.domain.request.base.*;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Service
public class TarifasRangosService {

    public JSONArray getViajesLocalesByIdTarifa(int id, Connection jdbcConnection)
            throws SQLException, Exception {
        String query = "SELECT IdViajeLocal, IdSucursal, IdConcepto, IdTarifa, IdTipoMedida\n" +
                "FROM CatTarifasRangosViajesLocalesPQ \n" +
                "WHERE IdTarifa = " + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONObject validarTarifaRango(List<ViajeForaneoRequest> request, Connection jdbcConnection)
            throws SQLException, Exception{
       int setterIdViaje=0;
       int setterIdGrupo=0;
       JSONObject regreso=new JSONObject();
       for(ViajeForaneoRequest viaje : request)
       {
           viaje.setIdViajeForaneo(setterIdViaje);
           for(ViajeForaneoGrupo grupo :viaje.getGrupos())
           {
               grupo.setIdViajeForaneo(setterIdViaje);
               grupo.setIdViajeForaneoGrupo(setterIdGrupo);
               setterIdGrupo++;
           }
           setterIdViaje++;
       }
       for(ViajeForaneoRequest viajeQueSeVaACompararConLosDemas:request)
       {
           for(ViajeForaneoRequest viajeConElQueSeCompara :request)
           {
               if ((viajeQueSeVaACompararConLosDemas.getIdViajeForaneo()!=viajeConElQueSeCompara.getIdViajeForaneo())
                       && (viajeQueSeVaACompararConLosDemas.getIdOrigen()==viajeConElQueSeCompara.getIdOrigen())
                       && (viajeQueSeVaACompararConLosDemas.getIdDestino()==viajeConElQueSeCompara.getIdDestino())
                       && (viajeQueSeVaACompararConLosDemas.getIdTipoMedida()==viajeConElQueSeCompara.getIdTipoMedida()))
                   {
                       for(ViajeForaneoGrupo grupoQueSeVaAComparar:viajeQueSeVaACompararConLosDemas.getGrupos())
                       {
                           for(ViajeForaneoGrupo grupoConElQueSeCompara : viajeConElQueSeCompara.getGrupos())
                           {
                               for(ViajeZonaRequest zonaQueSeVaAComparar:grupoQueSeVaAComparar.getZonas())
                               {
                                   for(ViajeZonaRequest zonaConLaQueSeCompara:grupoConElQueSeCompara.getZonas())
                                   {
                                       if(zonaQueSeVaAComparar.getIdZonaOperativa()==zonaConLaQueSeCompara.getIdZonaOperativa())
                                       {
                                           for(TarifaProductoRequest productoQueSeVaAComparar:grupoQueSeVaAComparar.getProductos())
                                           {
                                               for(TarifaProductoRequest productoConElQueSeCompara:grupoConElQueSeCompara.getProductos())
                                               {
                                                   if(productoQueSeVaAComparar.getIdProducto()==productoConElQueSeCompara.getIdProducto())
                                                   {
                                                       for(TarifaConceptoRequest conceptoQueSeVaAComparar:grupoQueSeVaAComparar.getConceptos())
                                                       {
                                                           for(TarifaConceptoRequest conceptoConElQueSeCompara:grupoConElQueSeCompara.getConceptos())
                                                           {
                                                               //Cuando es Peso
                                                               if((viajeQueSeVaACompararConLosDemas.getIdTipoMedida()==1)
                                                                       && (conceptoQueSeVaAComparar.getIdUnidadMedida()==conceptoConElQueSeCompara.getIdUnidadMedida()))
                                                               {
                                                                   if(conceptoQueSeVaAComparar.getMinimo()<conceptoConElQueSeCompara.getMaximo()
                                                                           && conceptoQueSeVaAComparar.getMaximo()>conceptoConElQueSeCompara.getMinimo())
                                                                   {
                                                                       regreso.put("success",false);
                                                                       regreso.put("motivo","Repetición en viajes de Milla Intermedia entre viaje No."
                                                                               +(viajeQueSeVaACompararConLosDemas.getIdViajeForaneo()+1)
                                                                               +" y viaje No."
                                                                               +(viajeConElQueSeCompara.getIdViajeForaneo()+1)
                                                                               +"");
                                                                       return regreso;
                                                                   }
                                                               }
                                                               else {
                                                                   regreso.put("success",true);
                                                                   return regreso;
                                                               }

                                                               //Cuando es porcentaje
                                                               if(viajeQueSeVaACompararConLosDemas.getIdTipoMedida()==3)
                                                               {
                                                                   regreso.put("success",false);
                                                                   regreso.put("motivo","Repetición en viajes de Milla Intermedia entre viaje No."
                                                                           +(viajeQueSeVaACompararConLosDemas.getIdViajeForaneo()+1)
                                                                           +" y viaje No."
                                                                           +(viajeConElQueSeCompara.getIdViajeForaneo()+1)
                                                                           +"");
                                                                   return regreso;
                                                               }

                                                              //Cuando son Piezas
                                                                if(conceptoQueSeVaAComparar.getMinimo()<conceptoConElQueSeCompara.getMaximo()
                                                                        && conceptoQueSeVaAComparar.getMaximo()>conceptoConElQueSeCompara.getMinimo())
                                                                {
                                                                    regreso.put("success",false);
                                                                    regreso.put("motivo","Repetición en viajes de Milla Intermedia entre viaje No."
                                                                            +(viajeQueSeVaACompararConLosDemas.getIdViajeForaneo()+1)
                                                                            +" y viaje No."
                                                                            +(viajeConElQueSeCompara.getIdViajeForaneo()+1)
                                                                            +"");
                                                                    return regreso;
                                                                }
                                                           }
                                                       }
                                                   }
                                               }
                                           }
                                       }
                                   }
                               }
                           }
                       }
                   }
           }
       }
        regreso.put("success",true);
        return regreso;
    }

    public JSONArray getViajesZonasByIdTarifa(int id, Connection jdbcConnection) throws SQLException, Exception {
        String query = "SELECT IdViajeZona, IdViajeLocal, IdViajeForaneoGrupo, IdZonaOperativa, zona.CodigoZona, zona.AplicaEntrega\n" +
                "FROM CatTarifasRangosViajesZonasPQ tarZona\n" +
                "         JOIN CatZonasOperativasPQ zona on zona.IdZona = tarZona.IdZonaOperativa\n" +
                "WHERE IdViajeLocal IN (SELECT IdViajeLocal\n" +
                "                       FROM CatTarifasRangosViajesLocalesPQ\n" +
                "                       WHERE IdTarifa = "+id+")\n" +
                "   OR IdViajeForaneoGrupo IN (SELECT IdViajeForaneoGrupo\n" +
                "                              FROM CatTarifasRangosViajesForaneosGruposPQ\n" +
                "                              WHERE IdViajeForaneo IN (SELECT IdViajeForaneo\n" +
                "                                                       FROM CatTarifasRangosViajesForaneosPQ\n" +
                "                                                       WHERE IdTarifa = "+id+"))";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getViajesForaneosByIdTarifa(int id, Connection jdbcConnection)
            throws SQLException, Exception {
        String query = "SELECT IdViajeForaneo, IdOrigen, IdDestino, IdTipoMedida, IdTarifa, fleteMinimo\n" +
                "FROM CatTarifasRangosViajesForaneosPQ\n" +
                "WHERE IdTarifa = "+id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getGruposByIdTarifa(int id, Connection jdbcConnection)
            throws SQLException, Exception {
        String query = "SELECT IdViajeForaneoGrupo, Referencia, IdViajeForaneo\n" +
                "FROM CatTarifasRangosViajesForaneosGruposPQ\n" +
                "WHERE IdViajeForaneo IN (SELECT IdViajeForaneo\n" +
                "                         FROM CatTarifasRangosViajesForaneosPQ\n" +
                "                         WHERE IdTarifa = "+id+")";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getConceptosByIdTarifa(int id, Connection jdbcConnection)
            throws SQLException, Exception {
        String query = "SELECT IdTarifaConcepto,\n" +
                "       tarconc.IdConceptoFacturacion,\n" +
                "       conc.ConceptoFacturacion,\n" +
                "       Minimo,\n" +
                "       Maximo,\n" +
                "       tarconc.IdUnidadMedida,\n" +
                "       med.UnidadMedida,\n" +
                "       ISNULL(IdTipoCalculo,0) AS IdTipoCalculo,\n" +
                "       ISNULL(cal.TarifaTipoCalculo, '') as TipoCalculo,\n" +
                "       IdViajeLocal,\n" +
                "       IdTarifa,\n" +
                "       IdViajeForaneoGrupo,\n" +
                "       Importe,\n" +
                "       Porcentaje\n" +
                "FROM CatTarifasRangosConceptosPQ tarconc\n" +
                "         LEFT JOIN CatConceptosFacturacion conc on conc.IdConceptoFacturacion = tarconc.IdConceptoFacturacion\n" +
                "         LEFT JOIN CatTarifaTipoCalculoPQ cal on cal.IdTarifaTipoCalculo = tarconc.IdTipoCalculo\n" +
                "         LEFT JOIN CatUnidadesMedida med on med.IdUnidadMedida = tarconc.IdUnidadMedida\n" +
                "WHERE IdViajeLocal IN (SELECT IdViajeLocal\n" +
                "                       FROM CatTarifasRangosViajesLocalesPQ\n" +
                "                       WHERE IdTarifa = "+id+")\n" +
                "   OR IdTarifa = "+id+"\n" +
                "   OR IdViajeForaneoGrupo IN (SELECT IdViajeForaneoGrupo\n" +
                "                              FROM CatTarifasRangosViajesForaneosGruposPQ\n" +
                "                              WHERE IdViajeForaneo IN (SELECT IdViajeForaneo\n" +
                "                                                       FROM CatTarifasRangosViajesForaneosPQ\n" +
                "                                                       WHERE IdTarifa = "+id+"))";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray array = new JSONArray();
        while (rs.next()) {
            JSONObject json = new JSONObject();
            json.put("IdTarifaConcepto", rs.getInt("IdTarifaConcepto"));
            json.put("IdConceptoFacturacion", rs.getInt("IdConceptoFacturacion"));
            json.put("ConceptoFacturacion", rs.getString("ConceptoFacturacion"));
            json.put("Minimo", rs.getFloat("Minimo"));
            json.put("Maximo", rs.getFloat("Maximo"));
            json.put("IdUnidadMedida", rs.getInt("IdUnidadMedida"));
            json.put("UnidadMedida", rs.getString("UnidadMedida"));
            json.put("IdTipoCalculo", rs.getInt("IdTipoCalculo"));
            json.put("TipoCalculo", rs.getString("TipoCalculo"));
            json.put("IdViajeLocal", rs.getInt("IdViajeLocal"));
            json.put("IdTarifa", rs.getInt("IdTarifa"));
            json.put("IdViajeForaneoGrupo", rs.getFloat("IdViajeForaneoGrupo"));
            json.put("Importe", rs.getInt("Importe"));
            json.put("Porcentaje", rs.getFloat("Porcentaje"));
            JSONArray productos = getProductosByConceptosByIdTarifa(rs.getInt("IdConceptoFacturacion"), rs.getInt("IdTarifa"), jdbcConnection);
            json.put("Productos", productos);
            array.put(json);
        }

        return array;
    }

    public JSONArray getProductosByConceptosByIdTarifa(int idConcepto, int idTarifa, Connection jdbcConnection) throws SQLException, Exception {
        String query = "SELECT\n" +
                "    CTRCPP.IdTarifa,\n" +
                "    CTRCPP.IdConceptoFacturacion,\n" +
                "    CTRCPP.IdProducto,\n" +
                "    CPP.Descripcion AS m_sDescripcion,\n" +
                "    (CAST(CTRCPP.IdProducto AS VARCHAR) + '.- ' + CPP.Descripcion) AS numeroDescripcion\n" +
                "FROM CatTarifasRangosConceptosProductosPQ CTRCPP\n" +
                "    LEFT JOIN dbo.CatProductosPQ CPP ON CPP.IdProducto=CTRCPP.IdProducto\n" +
                "WHERE IdTarifa = " + idTarifa + " AND IdConceptoFacturacion = " + idConcepto;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getProductosByIdTarifa(int id, Connection jdbcConnection)
            throws SQLException, Exception {
        String query = "SELECT IdTarifaProducto, tarpro.IdProducto, pro.Descripcion, pro.Activo, pro.NoProducto, " +
                "IdViajeLocal, IdViajeForaneoGrupo\n" +
                "                FROM CatTarifasRangosProductosPQ tarpro\n" +
                "                JOIN CatProductosPQ pro on pro.IdProducto = tarpro.IdProducto\n" +
                "                WHERE IdViajeLocal IN (SELECT IdViajeLocal\n" +
                "                                       FROM CatTarifasRangosViajesLocalesPQ \n" +
                "                                       WHERE IdTarifa = "+id+") \n" +
                "                   OR IdViajeForaneoGrupo IN (SELECT IdViajeForaneoGrupo \n" +
                "                                              FROM CatTarifasRangosViajesForaneosGruposPQ \n" +
                "                                              WHERE IdViajeForaneo IN (SELECT IdViajeForaneo \n" +
                "                                                                       FROM CatTarifasRangosViajesForaneosPQ \n" +
                "                                                                       WHERE IdTarifa = "+id+"))";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public void validarVigenciaTarifas(Connection jdbcConnection)
            throws SQLException, Exception {
        String query = "UPDATE CatTarifasRangosPQ SET Activo = IIF(Vigencia < " +
                "(convert(DATE,GETDATE())),CAST(0 AS BIT), CAST(1 AS BIT))";
        Statement statement = jdbcConnection.createStatement();
        statement.execute(query);
    }
}
