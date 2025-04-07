package com.certuit.base.service.base;

import com.certuit.base.domain.request.base.InformeGuiaRequest;
import com.certuit.base.domain.request.base.PaqueteCubicar;
import com.certuit.base.domain.response.CubicajeResponse;
import com.certuit.base.util.UtilFuctions;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import xf.xflp.XFLP;
import xf.xflp.opt.XFLPOptType;
import xf.xflp.report.LPReport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class CubicarService {

    public CubicajeResponse cubicarGuia(Connection jdbcConnection, int idRemolque1, int idRemolque2, List<InformeGuiaRequest> guias) throws Exception {
        CubicajeResponse cubicajeResponse = new CubicajeResponse();

        XFLP xflp = new XFLP();
//            xflp.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_OPTIMIZER); NO JALO
//            xflp.setTypeOfOptimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER);
        xflp.setTypeOfOptimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER);
//            xflp.setTypeOfOptimization(XFLPOptType.FAST_MIN_CONTAINER_PACKER); NO JALO
//            xflp.setTypeOfOptimization(XFLPOptType.BEST_MIN_CONTAINER_PACKER); JALO CON ERROR "Index 0 out of bounds for length 0"
        JSONObject jsonRemolque = this.obtenerDatosContenedor(jdbcConnection, idRemolque1);
        if (jsonRemolque == null) {
            cubicajeResponse.setSuccess(false);
            cubicajeResponse.setMensaje("No hay remolque.");
            return cubicajeResponse;
        }
        if (jsonRemolque.getInt("largo") == 0 || jsonRemolque.getInt("ancho") == 0
                || jsonRemolque.getInt("alto") == 0 || jsonRemolque.getInt("capacidad") == 0) {
            cubicajeResponse.setSuccess(false);
            cubicajeResponse.setMensaje("El remolque " + jsonRemolque.getString("Codigo")
                    + " no cuenta con las dimensiones registradas. Favor de ingresar al catálogo de unidades y " +
                    "agregar la información para poder realizar el cúbicaje.");
            return cubicajeResponse;
        }
        xflp.addContainer()
                .setLength(jsonRemolque.getInt("largo"))
                .setWidth(jsonRemolque.getInt("ancho"))
                .setHeight(jsonRemolque.getInt("alto"))
                .setMaxWeight(jsonRemolque.getInt("capacidad"))
                .setContainerType("" + idRemolque1);
        xflp.addContainer();
        JSONObject jsonRemolque2 = this.obtenerDatosContenedor(jdbcConnection, idRemolque2);
        if (jsonRemolque2 != null) {
            if (jsonRemolque2.getInt("largo") == 0 || jsonRemolque2.getInt("ancho") == 0
                    || jsonRemolque2.getInt("alto") == 0 || jsonRemolque2.getInt("capacidad") == 0) {
                cubicajeResponse.setSuccess(false);
                cubicajeResponse.setMensaje("El remolque " + jsonRemolque2.getString("Codigo")
                        + " no cuenta con las dimensiones registradas. Favor de ingresar al catálogo de unidades y " +
                        "agregar la información para poder realizar el cúbicaje.");
                return cubicajeResponse;
            }
            xflp.addContainer()
                    .setLength(jsonRemolque2.getInt("largo") * 100)
                    .setWidth(jsonRemolque2.getInt("ancho") * 100)
                    .setHeight(jsonRemolque2.getInt("alto") * 100)
                    .setMaxWeight(jsonRemolque2.getInt("capacidad"))
                    .setContainerType("" + idRemolque2);
        }

//            AGREGANDO PRODUCTOS
//            CADA PRODUCTO TIENE N CANTIDAD DE PAQUETES. SE OCUPA SACAR EL TOTAL DE PAQUETES POR CADA PRODUCTO.
        JSONArray paquetes = this.obtenerPaquetesGuias(jdbcConnection, guias.stream().mapToInt(InformeGuiaRequest::getM_nIdGuia).toArray());
        for (int i = 0; i < paquetes.length(); i++) {
            JSONObject paquete = paquetes.getJSONObject(i);
            xflp.addItem().setLength(paquete.getInt("largo")).setWidth(paquete.getInt("ancho"))
                    .setHeight(paquete.getInt("alto")).setWeight(paquete.getInt("peso"));
        }

        xflp.executeLoadPlanning();
        LPReport report = xflp.getReport();
        if (report.getContainerReports().size() == 0) {
            cubicajeResponse.setSuccess(false);
            cubicajeResponse.setMensaje("No cupo ningun paquete");
            return cubicajeResponse;
        }

        float utilization = report.getSummary().getUtilization()*100;
        cubicajeResponse.setSuccess(true);
        cubicajeResponse.setUtilizacion(utilization);
        return cubicajeResponse;
    }

    public JSONObject obtenerDatosContenedor(Connection jdbcConnection, int idUnidad) throws Exception {
        String query = "";
        //DEFINICION DEL CONTENEDOR
//        query = "select \n" +
//                "    IdUnidad,\n" +
//                "    IdTipoUnidad,\n" +
//                "    Codigo,\n" +
//                "    Observaciones,\n" +
//                "    CAST(ROUND(Largo,0) as INT) AS largo,\n" +
//                "    CAST(ROUND(Ancho,0) as INT) as ancho,\n" +
//                "    CAST(ROUND(Alto,0) as INT) as alto,\n" +
//                "    Capacidad as capacidad\n" +
//                "from CatUnidades\n" +
//                "where IdUnidad = " + idUnidad;
        query = "SELECT IdUnidad, IdTipoUnidad, Codigo, Observaciones, CAST(Largo * 100 as INT) AS largo, " +
                "CAST(Ancho * 100 as INT) as ancho, CAST(Alto * 100 as INT) as alto, Capacidad as capacidad " +
                "FROM CatUnidades WHERE IdUnidad = " + idUnidad;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return UtilFuctions.convertObject(rs);
    }

    /**
     * SU UNICA CHAMBA ES RETORNAR EL LISTADO DE PAQUETES POR VIAJE
     */
    public JSONArray obtenerPaquetes(Connection jdbcConnection, int idViaje) throws Exception {
        String query = "";
        query = "SELECT \n" +
                "    IdEmbarqueDetalle as id,\n" +
                "    CAST(ROUND(paquete.Peso, 0) AS INT) AS peso, \n" +
                "    CAST(ROUND(Largo, 0) AS INT ) AS largo, \n" +
                "    CAST(ROUND(Ancho, 0) AS INT ) AS ancho, \n" +
                "    CAST(ROUND(Alto, 0) AS INT ) AS alto, \n" +
                "    CAST(ROUND(ctd, 0) AS INT ) AS cantidad\n" +
                "FROM ProEmbarqueDetallePQ paquete\n" +
                "         INNER JOIN ProGuiaPQ guia ON guia.IdEmbarque = paquete.IdEmbarque\n" +
                "         INNER JOIN ProInformeGuiaPQ informe ON informe.IdGuia = guia.IdGuia\n" +
                "         INNER JOIN ProViajeDetalleParadasPQ viaje ON viaje.IdInforme = informe.IdInforme\n" +
                "WHERE viaje.IdViaje = " + idViaje;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return UtilFuctions.convertArray(rs);
    }

    /**
     * SU UNICA CHAMBA ES RETORNAR EL LISTADO DE PAQUETES POR VIAJE
     */
    public JSONArray obtenerPaquetesGuias(Connection jdbcConnection, int[] idGuias) throws Exception {
        String query = "";
        JSONArray jsonArray = new JSONArray();
        if (idGuias.length > 0 ) {
            query = "SELECT\n" +
                    "    IdEmbarqueDetalle                   as id,\n" +
                    "    CAST(ROUND(paquete.Peso, 0) AS INT) AS peso,\n" +
                    "    CAST(ROUND(Largo, 0) AS INT)        AS largo,\n" +
                    "    CAST(ROUND(Ancho, 0) AS INT)        AS ancho,\n" +
                    "    CAST(ROUND(Alto, 0) AS INT)         AS alto,\n" +
                    "    CAST(ROUND(ctd, 0) AS INT)          AS cantidad\n" +
                    "FROM ProEmbarqueDetallePQ paquete\n" +
                    "    INNER JOIN ProGuiaPQ guia ON guia.IdEmbarque = paquete.IdEmbarque\n" +
                    "         CROSS APPLY (SELECT n\n" +
                    "                      FROM (SELECT ROW_NUMBER() OVER(ORDER BY 1/0) AS n\n" +
                    "                            FROM ProEmbarqueDetallePQ s1) AS sub\n" +
                    "                      WHERE  sub.n <= paquete.ctd) AS s2(Series)\n" +
                    "        WHERE guia.IdGuia in (" + convertIntArrayToString(idGuias, ",") + ")";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            jsonArray = UtilFuctions.convertArray(rs);
        }
        return jsonArray;
    }

    /**
     * SU UNICA CHAMBA ES RETORNAR EL LISTADO DE PAQUETES EN FORMATO DE CLASE A PARTIR DEL JSON DADO.
     * LOS NOMBRE DE LOS NODOS DEL JSON DEBEN COINCIDIR CON LOS NOMBRE DE LAS VARIABLES DE LA CLASE "PaqueteCubicar"
     */
    public List<PaqueteCubicar> convertPacksJsonArrarToList(JSONArray jsonArray) throws Exception {
        List<PaqueteCubicar> paquetes = new ArrayList<>();
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            PaqueteCubicar paquete = new Gson().fromJson(String.valueOf(jsonObject), PaqueteCubicar.class);
            paquetes.add(paquete);
        }
        return paquetes;
    }

    private static String convertIntArrayToString(int[] strArr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int str : strArr)
            sb.append(str).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }
}
