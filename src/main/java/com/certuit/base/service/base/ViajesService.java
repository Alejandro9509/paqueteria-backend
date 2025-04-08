package com.certuit.base.service.base;
import com.certuit.base.domain.request.base.GuiaRequest;
import com.certuit.base.domain.request.base.PaqueteRequest;
import com.certuit.base.domain.request.base.ViajeRequest;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Service
public class ViajesService {

    public JSONArray getListadoByViajeId(int id, Connection jdbcConnection) throws Exception{
        String query = "SELECT\n" +
                "\t\n" +
                "      i.IdViaje\n" +
                "     ,i.FolioInforme\n" +
                "\t ,i.IdRuta\n" +
                "\t \n" +
                "\t ,(Select(Nombres + ' ' + ApellidoPaterno + ' ' + ApellidoPaterno) as NombreCompleto " +
                "from CatOperadores where IdOperador= i.IdOperador) as NombreOperador\n" +
                "    ,(Select OrigenDestino from CatOrigenesDestinos where IdOrigenDestino=i.IdCiudadOrigen) as CiudadOrigen\n" +
                "\t,(Select OrigenDestino from CatOrigenesDestinos where IdOrigenDestino=i.IdCiudadDestino) as CiudadDestino\n" +
                "\t,i.IdOperador\n" +
                "\t,i.IdDolly\n" +
                "\t,i.IdRemolque1\n" +
                "\t,i.IdRemolque2\n" +
                "\t,(Select Descripcion from CatUnidades where IdUnidad=i.IdDolly) as Dolly\n" +
                "\t,(Select Descripcion from CatUnidades where IdUnidad=i.IdRemolque1) as Remolque1\n" +
                "\t,(Select Descripcion from CatUnidades where IdUnidad=i.IdRemolque2) as Remolque2\n" +
                "\n" +
                "\n" +
                "\tFROM ProInformePQ i \n" +
                "\tWHERE IdViaje ="+id;

        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        while(rs.next()) {
            json.put("m_nIdViaje", rs.getInt("IdViaje"));
            json.put("m_sFolioInforme", rs.getString("FolioInforme"));
            json.put("m_sRuta", rs.getString("Ruta"));
            json.put("m_sNombreCompleto", rs.getString("NombreOperador"));
            json.put("m_sCiudadOrigen", rs.getString("CiudadOrigen"));
            json.put("m_sCiudadDestino", rs.getString("CiudadDestino"));
            json.put("m_sDolly", rs.getString("Dolly"));
            json.put("m_sRemolque1", rs.getString("Remolque1"));
            json.put("m_sRemolque2", rs.getString("Remolque2"));
            array.put(json);
        }
        return array;
    }
    public void enviarCorreoStatus(Connection jdbcConnection, Statement statement, int nIdGuia,
                                   String[] correos,Boolean correoDefault,String sRFC, String mensaje) throws Exception {
        try{
            GuiaRequest guiaRequest = new GuiaRequest();
            guiaRequest.setM_nIdGuia(nIdGuia);
            String query = "SELECT Tracking,FolioGuia FROM ProGuiaPQ WHERE IdGuia = "+ guiaRequest.getM_nIdGuia();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                guiaRequest.setM_nTracking(rs.getString("Tracking"));
                guiaRequest.setM_sFolioGuia(rs.getString("FolioGuia"));
            }
            query = "EXEC usp_ProIdUltimaMillaGetPaquetesEsRecoleccionPQ false, '"+guiaRequest.getM_nTracking()+"'";
            rs = statement.executeQuery(query);
            JSONObject jsonGuia = UtilFuctions.convertObject(rs);
            if (jsonGuia != null){
                guiaRequest.setM_sCorreoDestinatario(jsonGuia.getString("m_sCorreoDestinatario"));
                guiaRequest.setM_sDomicilioDestinatario(
                        jsonGuia.getBoolean("m_bEntregaDiferenteDomicilio") ?
                                jsonGuia.getString("m_sDomicilioDetalleEntrega") :
                                rs.getString("m_sDomicilioDestinatario"));
                guiaRequest.setM_dFecha(jsonGuia.getString("m_dFechaRegistro"));
                guiaRequest.setM_sTipoServicio(jsonGuia.getString("m_sTipoServicio"));
                guiaRequest.setM_arrPaquetes(getPaquetesByGuia(statement, guiaRequest.getM_nIdGuia()));

            }
            query = "SELECT NombreFiscal,Logo FROM SisParametros";
            rs = statement.executeQuery(query);
            jsonGuia = UtilFuctions.convertObject(rs);
            String logo=jsonGuia.getString("Logo").replace("\r\n","");
            logo=logo.replace("=","");
            guiaRequest.setM_sNombreCliente(jsonGuia.getString("NombreFiscal"));
            query = "SELECT top 1 FormatoCorreoResumen FROM SisParametrosPQ";
            rs = statement.executeQuery(query);
            String plantillaCorreo = "";

            while(rs.next()){
                plantillaCorreo = rs.getString("FormatoCorreoResumen");
            }
            if(mensaje=="El trayecto ha sido cancelado")
            {

            }
            plantillaCorreo = plantillaCorreo.replace("#LINK_TRACKING", DBConection.getEnviromentURL()
                    +"app/applications/"+sRFC+"/"+guiaRequest.getM_nTracking()+"/tracking");
            plantillaCorreo = plantillaCorreo.replace("#TRACKING", guiaRequest.getM_nTracking());
            plantillaCorreo = plantillaCorreo.replace("#IMAGEN", "cid:logoImage");
            plantillaCorreo = plantillaCorreo.replace("#CLIENTE", guiaRequest.getM_sNombreCliente());
            //  plantillaCorreo = plantillaCorreo.replace("<p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">", "<p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">");
            plantillaCorreo = plantillaCorreo.replace("<span style=\"font-size: 20px;\">" +
                    "¡Tu orden ha sido registrada! </span>", "<span style=\"font-size: 20px;" +
                    "text-align:center\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+mensaje+"</span>");

            plantillaCorreo = plantillaCorreo.replace( "#DIRECCION_ENTREGA",
                    guiaRequest.getM_sDomicilioDestinatario() );
            plantillaCorreo = plantillaCorreo.replace( "#FOLIO",  guiaRequest.getM_sFolioGuia() );
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            plantillaCorreo = plantillaCorreo.replace( "#FECHA",  guiaRequest.getM_dFecha());
            plantillaCorreo = plantillaCorreo.replace( "#TIPO_SERVICIO", guiaRequest.getM_sTipoServicio() );

            int contador = 1;
            String paquetesConcat = "";

            EmailService emailService = new EmailService(jdbcConnection);
            String[] correo = new String[1];
            correo[0] = guiaRequest.getM_sCorreoDestinatario();

            if(correoDefault){
                emailService.sendMail(plantillaCorreo, "Seguimiento de la guia "
                        + guiaRequest.getM_sFolioGuia(), correo, Base64.getDecoder().decode(logo));
            }else{
                emailService.sendMail(plantillaCorreo, "Seguimiento de la guia "
                        + guiaRequest.getM_sFolioGuia(), correo, Base64.getDecoder().decode(logo));
            }
            //emailService.sendMail(plantillaCorreo, "Seguimiento de la guia " + guiaRequest.getM_sFolioGuia(), correo);

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Hubo un error al tratar de mandar el correo electrónico al destinatario.");
        }
    }
    public void enviarCorreoCancelarGuia(Connection jdbcConnection, Statement statement, int nIdGuia,
                                         GuiaRequest guia,Boolean correoDefault,String sRFC, String mensaje)
            throws Exception {
        try{
            GuiaRequest guiaRequest = new GuiaRequest();
            guiaRequest.setM_nIdGuia(nIdGuia);
            String query = "SELECT Tracking,FolioGuia FROM ProGuiaPQ WHERE IdGuia = "+ guiaRequest.getM_nIdGuia();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                guiaRequest.setM_nTracking(rs.getString("Tracking"));
                guiaRequest.setM_sFolioGuia(rs.getString("FolioGuia"));
            }
            query = "EXEC usp_ProIdUltimaMillaGetPaquetesEsRecoleccionPQ false, '"+guiaRequest.getM_nTracking()+"'";
            rs = statement.executeQuery(query);
            JSONObject jsonGuia = UtilFuctions.convertObject(rs);
            if (jsonGuia != null){
                guiaRequest.setM_sCorreoDestinatario(jsonGuia.getString("m_sCorreoDestinatario"));
                guiaRequest.setM_sDomicilioDestinatario(
                        jsonGuia.getBoolean("m_bEntregaDiferenteDomicilio") ?
                                jsonGuia.getString("m_sDomicilioDetalleEntrega") :
                                rs.getString("m_sDomicilioDestinatario"));
                guiaRequest.setM_dFecha(jsonGuia.getString("m_dFechaRegistro"));
                guiaRequest.setM_sTipoServicio(jsonGuia.getString("m_sTipoServicio"));
                guiaRequest.setM_arrPaquetes(getPaquetesByGuia(statement, guiaRequest.getM_nIdGuia()));

            }
            query = "SELECT NombreFiscal,Logo FROM SisParametros";
            rs = statement.executeQuery(query);
            jsonGuia = UtilFuctions.convertObject(rs);
            String logo=jsonGuia.getString("Logo").replace("\r\n","");
            logo=logo.replace("=","");
            guiaRequest.setM_sNombreCliente(jsonGuia.getString("NombreFiscal"));
            query = "SELECT top 1 FormatoCorreoResumen FROM SisParametrosPQ";
            rs = statement.executeQuery(query);
            String plantillaCorreo = "";

            while(rs.next()){
                plantillaCorreo = rs.getString("FormatoCorreoResumen");
            }
            if(mensaje=="El trayecto ha sido cancelado")
            {

            }
            plantillaCorreo = plantillaCorreo.replace("#LINK_TRACKING", DBConection.getEnviromentURL()
                    +"app/applications/"+sRFC+"/"+guiaRequest.getM_nTracking()+"/tracking");
            plantillaCorreo = plantillaCorreo.replace("#TRACKING", guiaRequest.getM_nTracking());
            plantillaCorreo = plantillaCorreo.replace("#IMAGEN", "cid:logoImage");
            plantillaCorreo = plantillaCorreo.replace("#CLIENTE", guia.getM_sNombreCliente());
            //  plantillaCorreo = plantillaCorreo.replace("<p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">", "<p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">");
            plantillaCorreo = plantillaCorreo.replace("<span style=\"font-size: 20px;\">" +
                    "¡Tu orden ha sido registrada! </span>", "<span style=\"font-size: 20px;" +
                    "text-align:center\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+mensaje+"</span>");

            plantillaCorreo = plantillaCorreo.replace( "#DIRECCION_ENTREGA",  guia.getM_sDomicilioDestinatario() );
            plantillaCorreo = plantillaCorreo.replace( "#FOLIO",  guiaRequest.getM_sFolioGuia() );
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date=new Date();
            plantillaCorreo = plantillaCorreo.replace( "#FECHA",  df.format(date));
            plantillaCorreo = plantillaCorreo.replace( "#TIPO_SERVICIO", guia.getM_sTipoServicio() );

            int contador = 1;
            String paquetesConcat = "";

            EmailService emailService = new EmailService(jdbcConnection);
            String[] correo = new String[1];
            correo[0] = guia.getM_sCorreoDestinatario();

            if(correoDefault){
                emailService.sendMail(plantillaCorreo, "Seguimiento de la guia " + guiaRequest.getM_sFolioGuia(),
                        correo, Base64.getDecoder().decode(logo));
            }else{
                emailService.sendMail(plantillaCorreo, "Seguimiento de la guia " + guiaRequest.getM_sFolioGuia(),
                        correo, Base64.getDecoder().decode(logo));
            }
            //emailService.sendMail(plantillaCorreo, "Seguimiento de la guia " + guiaRequest.getM_sFolioGuia(), correo);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Hubo un error al tratar de mandar el correo electrónico al destinatario.");
        }
    }
    public ArrayList<PaqueteRequest> getPaquetesByGuia(Statement statement, int nIdGuia) throws SQLException {
        String query = "SELECT\n" +
                "\t\tpe.IdEmbarqueDetalle\n" +
                "      ,pe.IdEmbarque,\n" +
                "\t  (select Nombre from CatEmbalajesPQ ce where pe.IdTipoEmpaque = ce.IdEmbalaje ) as Embalaje\n" +
                "      ,Tipo\n" +
                "      ,Descripcion\n" +
                "      ,Peso\n" +
                "      ,Largo\n" +
                "      ,Ancho\n" +
                "      ,Alto\n" +
                "      ,Volumen\n" +
                "      ,IdTipoEmpaque\n" +
                "      ,ValorDeclarado\n" +
                "      ,Observaciones\n" +
                "      ,Activo\n" +
                "      ,ctd\n" +
                "      ,IdProducto\n" +
                "      ,(select Descripcion from CatProductosPQ p WHERE p.IdProducto = pe.IdProducto ) as Producto\n" +
                "    FROM ProEmbarqueDetallePQ pe\n" +
                "\tWHERE IdEmbarque = (select pg.IdEmbarque from ProGuiaPQ pg where pg.IdGuia = "+nIdGuia+") " +
                "AND Tipo = 2";

        ResultSet rs = statement.executeQuery(query);
        ArrayList<PaqueteRequest> listPaquetes = new ArrayList<>();
        while(rs.next()){
            PaqueteRequest paqueteRequest = new PaqueteRequest();
            paqueteRequest.setM_xPeso(rs.getInt("Peso"));
            paqueteRequest.setM_xLargo(rs.getInt("Largo"));
            paqueteRequest.setM_xAncho(rs.getInt("Ancho"));
            paqueteRequest.setM_xAlto(rs.getInt("Alto"));
            paqueteRequest.setCtd(rs.getInt("ctd"));
            paqueteRequest.setM_sDescripcion(rs.getString("Descripcion"));
            paqueteRequest.setM_sObservaciones(rs.getString("Observaciones"));

            listPaquetes.add(paqueteRequest);
        }
        return listPaquetes;

    }
    public JSONArray getInformesAsignadosByIdViaje(int id, Connection jdbcConnection) throws Exception {
        String query = "SELECT \n" +
                "            i.IdInforme as m_nIdInforme\n" +
                "                 ,FORMAT(i.Fecha, 'dd-MM-yyyy') + ' ' +  " +
                "convert(char(5),convert(time(0),i.Hora)) as m_sFechaHora\n" +
                "                 ,i.IdViaje as m_nIdViaje\n" +
                "                 ,i.FolioInforme as m_sFolioInforme\n" +
                "                 ,i.IdRuta as m_nIdRuta\n" +
                "                 ,(Select(Nombres + ' ' + ApellidoPaterno + ' ' + ApellidoPaterno) as NombreCompleto " +
                "from CatOperadores where IdOperador= i.IdOperador) as m_sNombreCompleto\n" +
                "                 ,(Select OrigenDestino from CatOrigenesDestinos " +
                "where IdOrigenDestino=i.IdCiudadOrigen) as m_sCiudadOrigen\n" +
                "                ,(Select OrigenDestino from CatOrigenesDestinos " +
                "where IdOrigenDestino=i.IdCiudadDestino) as m_sCiudadDestino\n" +
                "                 ,(Select OrigenDestino from CatOrigenesDestinos " +
                "where IdOrigenDestino=PVDPP.IdUbicacionDestino) as m_sDestinoSeleccionado\n" +
                "                , PVDPP.IdUbicacionDestino AS m_nDestinoSeleccionado\n" +
                "                 ,case when PVDPP.IdLlegada is null then 1 else 0 end as m_bSePuedeBorrar  \n" +
                "                 ,i.IdOperador as m_nIdOperador\n" +
                "                ,i.IdDolly as m_nIdDolly\n" +
                "                ,i.IdRemolque1 as m_nIdRemolque1\n" +
                "                ,i.IdRemolque2 as m_nIdRemolque2\n" +
                "                ,(Select Descripcion from CatUnidades where IdUnidad=i.IdDolly) as m_sDolly\n" +
                "                ,(Select Descripcion from CatUnidades where IdUnidad=i.IdRemolque1) as m_sRemolque1\n" +
                "                ,(Select Descripcion from CatUnidades where IdUnidad=i.IdRemolque2) as m_sRemolque2 \n" +
                "                FROM ProInformePQ i " +
                "inner join ProViajeDetalleParadasPQ PVDPP on i.IdInforme = PVDPP.IdInforme\n" +
                "                WHERE PVDPP.IdViaje =" + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);


    }

    public JSONArray getIdRutasByIdViaje(int id, Connection jdbcConnection) throws Exception{
        String query = "select pvt.IdRuta,\n" +
                "       case when pvt.IdLlegada is null then 0 else 1 end as 'Terminado',\n" +
                "       case when pvt.IdSalida is null or PVSP.Cancelado = 1 then 0 else 1 end as 'Iniciado' ,\n" +
                "       pvt.IdOrigen,pvt.IdDestino from ProViajeTrayectosPQ pvt " +
                "left join ProViajeSalidaPQ PVSP on pvt.IdSalida = PVSP.IdViajeSalida WHERE pvt.IdViaje =" + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getTrayectosViaje(int id, Connection jdbcConnection) throws Exception {
        String query = "SELECT IdViajeTrayecto as m_nIdViajeTrayecto,vt.IdViaje as m_nIdViaje,r.IdRuta as m_nIdRuta, " +
                "vt.IdOrigen as m_nIdOrigen, \n" +
                "                       vt.IdDestino as m_nIdDestino ,o.OrigenDestino as m_sOrigen, \n" +
                "                       d.OrigenDestino as m_sDestino,o.OrigenDestino + '-' + d.OrigenDestino " +
                "as m_sRuta,\n" +
                "       vt.IdLlegada as m_nIdLlegada, vt.IdSalida as m_nIdSalida,\n" +
                "       s.FechaSalida as m_dFechaSalida, LEFT(s.HoraSalida,5) as m_tHoraSalida, s.Cancelado " +
                "as m_bSalidaCancelada\n" +
                "                from ProViajeTrayectosPQ vt \n" +
                "                    inner join CatOrigenesDestinos o on o.IdOrigenDestino = vt.IdOrigen \n" +
                "                    inner join CatOrigenesDestinos d on d.IdOrigenDestino = vt.IdDestino\n" +
                "                        left join ProViajeSalidaPQ s on s.IdViajeSalida = vt.IdSalida\n" +
                "                    left join ProViajeLlegadaPQ l on l.IdViajeLlegada = vt.IdLlegada\n" +
                "                   inner join CatRutasTrayectos r on r.IdRutaTrayecto = vt.IdRuta " +
                "                WHERE vt.IdViaje = " + id +
                "                order by r.Secuencia";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public double getTotalCalculoLiquidacionPorcentajeSobreImpFlete(int id, Connection jdbcConnection)
            throws Exception {
        String query = "select ISNULL(SUM(PGCP.Importe), 0.0) as TotalCalculoLiquidacionPorcentajeSobreImpFlete\n" +
                "from ProGuiaConceptoPQ PGCP\n" +
                "         inner join CatConceptosFacturacion CCF on CCF.IdConceptoFacturacion = PGCP.IdConceptoFacturacion\n" +
                "         inner join ProInformeGuiaPQ PIGP on PIGP.IdGuia = PGCP.IdGuia\n" +
                "         inner join ProViajeDetalleParadasPQ PVDPP on PVDPP.IdInforme = PIGP.IdInforme\n" +
                "where CCF.IncluirCalculoLiquidacionPorcentajeSobreImpFlete = 1\n" +
                "  and PVDPP.IdViaje = " + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONObject jsonObject = UtilFuctions.convertObject(rs);
        if (jsonObject == null){
            jsonObject = new JSONObject();
            jsonObject.put("TotalCalculoLiquidacionPorcentajeSobreImpFlete", 0.0);
        }
        return jsonObject.getDouble("TotalCalculoLiquidacionPorcentajeSobreImpFlete");
    }

    public  JSONArray convertViaje(ResultSet resultSet, Connection jdbcConnection) throws Exception {
        JSONArray jsonArray = new JSONArray();

        while (resultSet.next()) {

            int columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < columns; i++)
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));


            obj.put("m_arrTrayectos",getTrayectosViaje(obj.getInt("m_nIdViaje"), jdbcConnection) );
            obj.put("m_nTotalCalculoLiquidacionPorcentajeSobreImpFlete",
                    getTotalCalculoLiquidacionPorcentajeSobreImpFlete(obj.getInt("m_nIdViaje"), jdbcConnection) );
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public void insertarCartaPorte(int idGuia,String rfc,Connection jdbcConnection)throws Exception {

        try {
            String query = "SELECT\n" +
                    "e.IdEmbarque,\n" +
                    "g.IdSucursal,\n" +
                    "g.IdMoneda\n" +
                    "FROM ProGuiaPQ g,ProEmbarquePQ e\n" +
                    "WHERE g.IdGuia = "+idGuia+" and e.IdEmbarque = g.IdEmbarque";

            Statement statement = jdbcConnection.createStatement();
            Statement statement2 = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            int idEmbarque=0;
            int idSucursal=0;
            int idMoneda=0;
            while(rs.next()){
                idEmbarque = rs.getInt("IdEmbarque");
                idSucursal = rs.getInt("IdSucursal");
                idMoneda = rs.getInt("IdMoneda");
            }

            String query2 = "SELECT\n" +
                    "      IdCliente\n" +
                    "      ,IdRemitente\n" +
                    "      ,IdDestinatario\n" +
                    "      ,IdCiudadOrigen\n" +
                    "      ,IdCiudadDestino\n" +
                    "    FROM ProEmbarquePQ e\n" +
                    "    WHERE IdEmbarque = "+idEmbarque;

            ResultSet rs2 = statement2.executeQuery(query2);
            int idCliente=0;
            int idRemitente=0;
            int idDestinatario=0;
            int idCiudadOrigen=0;
            int idCiudadDestino=0;
            while(rs2.next()){
                idCliente = rs2.getInt("IdCliente");
                idRemitente = rs2.getInt("IdRemitente");
                idDestinatario =  rs2.getInt("IdDestinatario");
                idCiudadOrigen =  rs2.getInt("IdCiudadOrigen");
                idCiudadDestino =  rs2.getInt("IdCiudadDestino");
            }

            ViajeRequest viaje = new ViajeRequest();

            viaje.setM_nIdRuta(0);
            viaje.setM_nIdRutaTrayecto(0);
            viaje.setM_nTipoCobroPorViaje(1);
            viaje.setM_nIdCliente(idCliente);
            viaje.setM_nIdSucursal(idSucursal);
            viaje.setM_nIdRemitente(idRemitente);
            viaje.setM_nIdDestinatario(idDestinatario);
            viaje.setM_nIdMoneda(idMoneda);
            viaje.setM_nGeneradoDesde(1);
            viaje.setM_nIdEstatusViaje(10);
            viaje.setM_nSecuencia(1);
            viaje.setM_nOrigen(idCiudadOrigen);
            viaje.setM_nDestino(idCiudadDestino);
            viaje.setM_nKilometros(1);
            viaje.setM_xHoras(1);

            agregarAPI(idGuia,rfc,viaje,jdbcConnection);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void agregarAPI(int idGuia,String rfc,ViajeRequest viaje,Connection jdbcConnection)
            throws Exception {
        try{
            Statement statement = jdbcConnection.createStatement();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            Date today = Calendar.getInstance().getTime();
            String fecha = df.format(today);

            String query2 = "EXEC usp_ProViajeCartaPorteAgregarPQ "+
                    viaje.getM_nTipoCobroPorViaje()+",'"+fecha
                    +"','"+01+"'," +viaje.getM_nIdCliente()+","+viaje.getM_nIdSucursal()+","+viaje.getM_nIdRemitente()+","
                    +viaje.getM_nIdDestinatario()+","+viaje.getM_nIdMoneda()+","+viaje.getM_nGeneradoDesde()+",'"
                    +fecha+"'," +viaje.getM_nIdEstatusViaje()+","+viaje.getM_nSecuencia()+","+viaje.getM_nOrigen()+","
                    +viaje.getM_nDestino()+","+viaje.getM_nKilometros()+","+viaje.getM_xHoras()+","+0+"," +0 +"," +
                    "0,"+idGuia+",'"+rfc+"'";
            statement.executeUpdate(query2);
        }catch (SQLException sqle){
            throw sqle;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public int getCantidadAnticiposViaje(int idViajePQ, Connection jdbcConnection) {
        String query = "Select count(*) cantidadAnticipos\n" +
                "from ProAnticipos anticipo\n" +
                "         left join ProViajesTrayectos trayecto on anticipo.IdViajeTrayecto = trayecto.IdViajeTrayecto\n" +
                "         left join ProViajes viaje on trayecto.IdViaje = viaje.IdViaje\n" +
                "where viaje.IdGuia in (Select IdGuia\n" +
                "                       from ProGuiaPQ\n" +
                "                       where IdInforme in (Select IdInforme\n" +
                "                                           from ProViajeDetalleParadasPQ\n" +
                "                                           where IdViaje = ?))";
        try (PreparedStatement preparedStatement = jdbcConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, idViajePQ);
            ResultSet resultSet = preparedStatement.executeQuery();
            JSONObject jsonResult = UtilFuctions.convertObject(resultSet);
            assert jsonResult != null;
            return jsonResult.getInt("cantidadAnticipos");
        } catch (SQLException exception) {
            exception.printStackTrace();
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
