package com.certuit.base.service.base;

import com.certuit.base.domain.request.base.InformeGuiaRequest;
import com.certuit.base.util.UtilFuctions;
import org.apache.commons.lang.time.DateFormatUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class InformesService {
    @Autowired
    GuiaService guiaService;
    @Autowired
    ReportServices reportServices;
    @Autowired
    EmbarqueService embarqueService;

    public JSONArray getGuiasInforme(int id, Connection jdbcConnection) throws Exception {
        String query = "SELECT " +
                " g.IdGuia as m_nIdGuia, " +
                "ISNULL(g.Timbrado,0) as m_sTimbrado,"+
                "g.IdEstatusGuia as m_nIdEstatusGuia, " +
                " (select Estatus from CatEstatusGuiasPQ eg where eg.IdEstatusGuias = g.IdEstatusGuia ) as m_sEstatusGuia " +
                " ,g.FolioGuia as m_nFolioGuia, " +
                " (select OrigenDestino from CatOrigenesDestinos c where c.IdOrigenDestino = e.IdCiudadDestino) as m_sCiudadDestino, " +
                " (select Descripcion from CatTipoServicioPQ ts where g.IdTipoServicio = ts.IdTipoServicio) as m_sTipoServicio, " +
                " ISNULL((select Sum((pgc.Importe + pgc.ImporteIva) - pgc.ImporteRetiene) from ProGuiaConceptoPQ pgc " +
                "where pgc.IdGuia = g.IdGuia and pgc.IdConceptoFacturacion = (SELECT IdConceptoFlete FROM CatParametrosConfiguracion2PQ)),0) as m_xTotal, " +
                " e.Observaciones as m_sObservaciones, " +
                "(select sum(ed.Peso * ed.ctd) from ProEmbarqueDetallePQ ed where e.IdEmbarque = ed.IdEmbarque) as m_xPeso " +
                " FROM ProGuiaPQ g inner join ProEmbarquePQ e on g.IdEmbarque = e.IdEmbarque " +
                " WHERE g.IdInforme = " +  id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getGuiasPaquetesInforme(int id, Connection jdbcConnection) throws Exception {
        String query = "SELECT " +
                " g.IdGuia as m_nIdGuia, " +
                " (select Estatus from CatEstatusGuiasPQ eg where eg.IdEstatusGuias = g.IdEstatusGuia ) as m_sEstatusGuia " +
                " ,g.IdEstatusGuia as m_nIdEstatusGuia" +
                " ,g.FolioGuia as m_nFolioGuia," +
                "g.IdEmbarque as m_nIdEmbarque, " +
                " e.EntregaEnSucursal as m_bEntregaEnSucursal, " +
                " (select OrigenDestino from CatOrigenesDestinos c where c.IdOrigenDestino = e.IdCiudadDestino) as m_sCiudadDestino, " +
                " (select Descripcion from CatTipoServicioPQ ts where g.IdTipoServicio = ts.IdTipoServicio) as m_sTipoServicio, " +
                " ISNULL((select SUM(ped.Peso * ped.ctd ) from ProEmbarqueDetallePQ ped " +
                "where g.IdEmbarque = ped.IdEmbarque), 0) as m_xPeso, " +
                " ISNULL((select Sum((pgc.Importe + pgc.ImporteIva) - pgc.ImporteRetiene) " +
                "from ProGuiaConceptoPQ pgc where pgc.IdGuia = g.IdGuia " +
                "and pgc.IdConceptoFacturacion = 1),0) as m_xTotal, " +
                " g.Observaciones as m_sObservaciones " +
                " FROM ProGuiaPQ g inner join ProEmbarquePQ e on g.IdEmbarque = e.IdEmbarque " +
                " WHERE g.IdInforme = " +  id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return guiaService.convertGuia(rs,jdbcConnection);
    }

    public JSONObject getInformeId(int id, Connection jdbcConnection) throws Exception {
        String query = "SELECT cin.IdInforme                                                                                       as m_nIdInforme, \n" +
                "           cin.FolioInforme                                                                                    as m_sFolioInforme, \n" +
                "           cin.Fecha                                                                                           as m_dFecha, \n" +
                "           cin.Hora                                                                                            as m_tHora, \n" +
                "           cin.IdEstatusInforme                                                                                as m_nIdEstatusInforme, \n" +
                "           ev.Estatus                                                                                          as m_sEstatusInforme, \n" +
                "           cin.IdViaje                                                                                         as m_nIdViaje, \n" +
                "           cin.IdRuta                                                                                          as m_nIdRuta, \n" +
                "           viaje.FolioViaje                                                                                    as m_sFolioViaje, \n" +
                "           cin.IdSucursalEmisora                                                                               as m_nIdSucursalEmisora, \n" +
                "           sucEmi.Sucursal                                                                                     as m_sSucursalEmisora, \n" +
                "           cin.IdSucursalReceptora                                                                             as m_nIdSucursalReceptora, \n" +
                "           sucRec.Sucursal                                                                                     as m_sSucursalReceptora, \n" +
                "           cin.IdOperador                                                                                      as m_nIdOperador, \n" +
                "           CONCAT(operador.Nombres ,' ' , operador.ApellidoPaterno , ' ' , operador.ApellidoMaterno)           as m_sNombreCompleto, \n" +
                "           operador.NumeroOperador                                                                             as m_sNumeroOperador, \n" +
                "           unidad.Descripcion                                                                                  as m_sUnidad, \n" +
                "           unidad.Codigo                                                                                       as m_sUnidadIdentificador,\n" +
                "           unidad.Placas                                                                                       as m_sPlacasUnidad,\n" +
                "           cin.IdUnidad                                                                                        as m_nIdUnidad,\n" +
                "           cin.IdRemolque1                                                                                     as m_nIdRemolque1, \n" +
                "           cin.IdRemolque2                                                                                     as m_nIdRemolque2, \n" +
                "           cin.IdDolly                                                                                         as m_nIdDolly, \n" +
                "           CONCAT(remolque1.Codigo, ' - ',remolque1.Descripcion)                                               as m_sRemolque1,\n" +
                "           CONCAT(remolque2.Codigo, ' - ',remolque2.Descripcion)                                               as m_sRemolque2,\n" +
                "           remolque2.Placas                                                                                    as m_sPlacasRemolque2, \n" +
                "           remolque1.Placas                                                                                    as m_sPlacasRemolque1, \n" +
                "           dolly.Placas                                                                                        as m_sPlacasDolly, \n" +
                "           dolly.Descripcion                                                                                   as m_sDolly, \n" +
                "           origen.OrigenDestino                                                                                as m_sCiudadOrigen, \n" +
                "           destino.OrigenDestino                                                                               as m_sCiudadDestino, \n" +
                "           ruta.Descripcion                                                                                    as m_sRuta, \n" +
                "           cin.IdCiudadDestino                                                                                 as m_nIdCiudadDestino, \n" +
                "           cin.IdCiudadOrigen                                                                                  as m_nIdCiudadOrigen, \n" +
                "           cin.FechaCancelacion                                                                                as m_dtFechaCancelacion, \n" +
                "           cin.UsuarioCancelacion                                                                              as m_nIdUsuarioCancelacion, \n" +
                "           cin.MotivoCancelacion                                                                               as m_sMotivoCancelacion, \n" +
                "           uCancel.Nombre                                                                                      as m_sUsuarioCancelacion, \n" +
                "           cin.TipoTimbrado                                                                                      as m_nTipoTimbrado, \n" +
                "           (SELECT COUNT(*) FROM ProGuiaPQ where IdInforme="+id+") as countGuias,    "+
                "           case when (cin.IdEstatusInforme = 9 or cin.IdViaje > 0) then CAST(0 AS BIT) else CAST(1 AS BIT) END as m_bSePuedeCancelar, \n" +
                "           ISNULL(cin.FolioFiscalUUID, '')                                                                     as m_sFolioFiscalUUID, \n" +
                "           ISNULL(cin.Timbrado, 0)                                                                             as m_bTimbrado, \n" +
                "           ISNULL(cin.XMLTraslada, '')                                                                         as m_sXMLTraslada, \n" +
                "           ISNULL(cin.FolioFiscalUUIDSustituido, '')                                                           as m_sFolioFiscalUUIDSustituido, \n" +
                "           ISNULL(cin.UltimoFolioFiscalUUID, '')                                                               as m_sUltimoFolioFiscalUUIDSustituido, \n" +
                "           ISNULL(cin.MotivoCancelacionTimbrado, '')                                                           as m_sMotivoCancelacionTimbrado, \n" +
                "           ISNULL(cin.MotivoCancelacionSAT, '')                                                                as m_sMotivoCancelacionSAT \n" +
                "    FROM ProInformePQ cin \n" +
                "             inner join CatEstatusInformePQ ev on cin.IdEstatusInforme = ev.IdEstatusInforme \n" +
                "            left join ProViajesPQ viaje on viaje.IdViaje = cin.IdViaje \n" +
                "            left join CatUnidades dolly on dolly.IdUnidad = cin.IdDolly \n" +
                "            left join CatUnidades unidad on unidad.IdUnidad = cin.IdUnidad \n" +
                "            left join CatUnidades remolque1 on remolque1.IdUnidad = cin.IdRemolque1 \n" +
                "            left join CatUnidades remolque2 on remolque2.IdUnidad = cin.IdRemolque2 \n" +
                "            left join CatOperadores operador on operador.IdOperador = cin.IdOperador \n" +
                "            left join CatSucursales sucEmi on sucEmi.IdSucursal = cin.IdSucursalEmisora \n" +
                "            left join CatSucursales sucRec on cin.IdSucursalReceptora = sucRec.IdSucursal \n" +
                "            left join CatUsuarios uCancel on uCancel.IdUsuario = cin.UsuarioCancelacion \n" +
                "            left join CatOrigenesDestinos origen on cin.IdCiudadOrigen = origen.IdOrigenDestino \n" +
                "            left join CatOrigenesDestinos destino on cin.IdCiudadDestino = destino.IdOrigenDestino \n" +
                "            left join CatDetalleRutasPQ ruta on cin.IdRuta = ruta.IdDetalleRuta \n" +
                "            where cin.IdInforme =" + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONObject jsonObject = UtilFuctions.convertObject(rs);
        jsonObject.put("m_arrClsProGuia", getGuiasInforme(id, jdbcConnection));
        return jsonObject;
    }

    public JSONObject getInformeIdEscaner(int id, Connection jdbcConnection) throws Exception {
        String query = "SELECT " +
                "    cin.IdInforme as m_nIdInforme, " +
                "    cin.FolioInforme as m_sFolioInforme, " +
                "    cin.Fecha as m_dFecha, " +
                "    cin.Hora as m_tHora, " +
                "    cin.Fecha as m_sFechayHora, " +
                "    cin.IdEstatusInforme as m_nIdEstatusInforme, " +
                "    cin.IdViaje as m_nIdViaje, " +
                "    cin.IdRuta as m_nIdRuta, " +
                "    (CAST(op.NumeroOperador as varchar) + ' ' + op.Nombre) as m_sNumeroNombreOperador, " +
                "    (select FolioViaje from ProViajesPQ v where cin.IdViaje = v.IdViaje) as m_sFolioViaje, " +
                "    cin.IdSucursalEmisora as m_nIdSucursalEmisora , " +
                "    (select Sucursal from CatSucursales cs where cin.IdSucursalEmisora = cs.IdSucursal) as m_sSucursalEmisora, " +
                "    cin.IdSucursalReceptora as m_nIdSucursalReceptora, " +
                " " +
                "    (select Sucursal from CatSucursales cs where cin.IdSucursalReceptora = cs.IdSucursal) as m_sSucursalReceptora, " +
                "    cin.IdOperador as m_nIdOperador, " +
                " " +
                "    (op.Nombres + ' ' + op.ApellidoPaterno + ' ' + op.ApellidoMaterno)  as m_sNombreOperador, " +
                "    op.NumeroOperador  as m_sNumeroOperador, " +
                "    (select Descripcion from CatUnidades cu where cin.IdDolly = cu.IdUnidad) as Dolly, " +
                "    (select Codigo from CatUnidades cu where cin.IdRemolque1 = cu.IdUnidad) as m_nIdentificador, " +
                "    cin.IdRemolque1 as m_nIdRemolque1, " +
                "    cin.IdRemolque2 as m_nIdRemolque2, " +
                "    cin.IdDolly as m_nIdDolly, " +
                " " +
                "    (select Descripcion from CatUnidades cu where cin.IdRemolque1 = cu.IdUnidad) as m_sRemolque1, " +
                "    (select Descripcion from CatUnidades cu where cin.IdRemolque2 = cu.IdUnidad) as m_sRemolque2, " +
                "    cin.PlacasRemolque1 as m_sPlacasRemolque1, " +
                "    cin.PlacasRemolque2 as m_sPlacasRemolque2, " +
                "    cin.PlacasDolly as m_sDolly, " +
                " " +
                "    (select OrigenDestino from CatOrigenesDestinos cc where cin.IdCiudadOrigen = cc.IdOrigenDestino) as m_sCiudadOrigen, " +
                "    (select OrigenDestino from CatOrigenesDestinos cc where cin.IdCiudadDestino = cc.IdOrigenDestino) as m_sCiudadDestino, " +
                "    (select Descripcion from CatDetalleRutasPQ cr where cin.IdRuta = cr.IdDetalleRuta) as m_sRuta, " +
                "    cin.IdCiudadDestino as m_nIdCiudadDestino, " +
                "    cin.IdCiudadOrigen as m_nIdCiudadOrigen, " +
                "    cin.FechaCancelacion as m_dtFechaCancelacion, " +
                "    cin.UsuarioCancelacion as m_nIdUsuarioCancelacion, " +
                "    cus.Nombre as m_sNombre " +
                "FROM ProInformePQ cin left JOIN CatUsuarios cus on cin.UsuarioCancelacion= cus.IdUsuario " +
                "left join CatOperadores op on cin.IdOperador = op.IdOperador " +
                "where cin.IdInforme = " + id + " and (cin.IdEstatusInforme = 7 OR cin.IdEstatusInforme = 5)";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        JSONObject jsonObject = UtilFuctions.convertObject(rs);
        if (jsonObject == null){
            return null;
        }
        jsonObject.put("m_arrClsProGuia", getGuiasPaquetesInforme(id, jdbcConnection));

        return jsonObject;
    }

    public JSONObject getInformeIdEscanerValidar(int id, Connection jdbcConnection) throws Exception {
        String query = "SELECT " +
                "    cin.IdInforme as m_nIdInforme, " +
                "    cin.FolioInforme as m_sFolioInforme, " +
                "    cin.Fecha as m_dFecha, " +
                "    cin.Hora as m_tHora, " +
                "    cin.Fecha as m_sFechayHora, " +
                "    cin.IdEstatusInforme as m_nIdEstatusInforme, " +
                "    cin.IdViaje as m_nIdViaje, " +
                "    cin.IdRuta as m_nIdRuta, " +
                "    (CAST(op.NumeroOperador as varchar) + ' ' + op.Nombre) as m_sNumeroNombreOperador, " +
                "    (select FolioViaje from ProViajesPQ v where cin.IdViaje = v.IdViaje) as m_sFolioViaje, " +
                "    cin.IdSucursalEmisora as m_nIdSucursalEmisora , " +
                "    (select Sucursal from CatSucursales cs where cin.IdSucursalEmisora = cs.IdSucursal) as m_sSucursalEmisora, " +
                "    cin.IdSucursalReceptora as m_nIdSucursalReceptora, " +
                " " +
                "    (select Sucursal from CatSucursales cs where cin.IdSucursalReceptora = cs.IdSucursal) as m_sSucursalReceptora, " +
                "    cin.IdOperador as m_nIdOperador, " +
                " " +
                "    (op.Nombres + ' ' + op.ApellidoPaterno + ' ' + op.ApellidoMaterno)  as m_sNombreOperador, " +
                "    op.NumeroOperador  as m_sNumeroOperador, " +
                "    (select Descripcion from CatUnidades cu where cin.IdDolly = cu.IdUnidad) as Dolly, " +
                "    (select Codigo from CatUnidades cu where cin.IdRemolque1 = cu.IdUnidad) as m_nIdentificador, " +
                "    cin.IdRemolque1 as m_nIdRemolque1, " +
                "    cin.IdRemolque2 as m_nIdRemolque2, " +
                "    cin.IdDolly as m_nIdDolly, " +
                " " +
                "    (select Descripcion from CatUnidades cu where cin.IdRemolque1 = cu.IdUnidad) as m_sRemolque1, " +
                "    (select Descripcion from CatUnidades cu where cin.IdRemolque2 = cu.IdUnidad) as m_sRemolque2, " +
                "    cin.PlacasRemolque1 as m_sPlacasRemolque1, " +
                "    cin.PlacasRemolque2 as m_sPlacasRemolque2, " +
                "    cin.PlacasDolly as m_sDolly, " +
                " " +
                "    (select OrigenDestino from CatOrigenesDestinos cc where cin.IdCiudadOrigen = cc.IdOrigenDestino) as m_sCiudadOrigen, " +
                "    (select OrigenDestino from CatOrigenesDestinos cc where cin.IdCiudadDestino = cc.IdOrigenDestino) as m_sCiudadDestino, " +
                "    (select Descripcion from CatDetalleRutasPQ cr where cin.IdRuta = cr.IdDetalleRuta) as m_sRuta, " +
                "    cin.IdCiudadDestino as m_nIdCiudadDestino, " +
                "    cin.IdCiudadOrigen as m_nIdCiudadOrigen, " +
                "    cin.FechaCancelacion as m_dtFechaCancelacion, " +
                "    cin.UsuarioCancelacion as m_nIdUsuarioCancelacion, " +
                "    cus.Nombre as m_sNombre " +
                "FROM ProInformePQ cin left JOIN CatUsuarios cus on cin.UsuarioCancelacion= cus.IdUsuario " +
                "left join CatOperadores op on cin.IdOperador = op.IdOperador " +
                "where cin.IdInforme =" + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        JSONObject jsonObject = UtilFuctions.convertObject(rs);
        if (jsonObject == null){
            return null;
        }
        jsonObject.put("m_arrClsProGuia", getGuiasPaquetesInforme(id, jdbcConnection));

        return jsonObject;
    }

    public  JSONArray convertParada(ResultSet resultSet, Connection jdbcConnection) throws Exception {

        JSONArray jsonArray = new JSONArray();

        while (resultSet.next()) {
            int columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < columns; i++)
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));

            obj.put("m_clsInforme",getInformeId(obj.getInt("m_nIdInforme"), jdbcConnection) );
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public  JSONArray convertInformes(ResultSet resultSet, Connection jdbcConnection) throws Exception {

        JSONArray jsonArray = new JSONArray();

        while (resultSet.next()) {
            int columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < columns; i++)
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));

            obj.put("m_arrClsProGuia",getGuiasInforme(obj.getInt("m_nIdInforme"), jdbcConnection) );
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public void enviarCorreoFacturacion(String emailTemplate, int id,Connection jdbcConnection,
                                        String[] correos,Boolean correoDefault,String rfc, int idViaje)  {
        try {
            EmailService emailService = new EmailService(jdbcConnection);
            String query = "select  '' AS 'ClientePaga', CO.CorreoOperador," +
                    "FORMAT(PIP.FechaTimbrado,'dd/MMM/yyyy HH:mm') as FechaTimbrado, PIP.FolioInforme as 'Folio'," +
                    "PIP.XMLTraslada as 'XMLTraslada',PIP.FolioFiscalUUID  " +
                    "from ProInformePQ PIP left join CatOperadores CO on PIP.IdOperador = CO.IdOperador " +
                    "where PIP.IdInforme = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<File> fileList = new ArrayList<>();
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            String emailFormat = UtilFuctions.converTemplate(jsonObject, emailTemplate);
            fileList.add(reportServices.generateReporCFDIViaje(1,idViaje, id,rfc,
                    jsonObject.getString("FolioFiscalUUID")));
            fileList.add(UtilFuctions.convertXMLToFile(jsonObject.getString("XMLTraslada"),
                    jsonObject.getString("FolioFiscalUUID") ));

            if (correoDefault){
                emailService.sendMail(emailFormat, "CFDI " + jsonObject.getString("FolioFiscalUUID"),
                        jsonObject.getString("CorreoElectronico"), fileList);
                Thread.sleep(30000);
            }

            for (String correo: correos) {
                emailService.sendMail(emailFormat, "CFDI " + jsonObject.getString("FolioFiscalUUID"),
                        correo, fileList);
                Thread.sleep(30000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public JSONArray getListadoPorInformes(int id, Connection jdbcConnection) throws Exception {
        String query = "SELECT\n" +
                "\t\tg.IdEmbarque\n" +
                "\t\t,(select FolioEmbarque from ProEmbarquePQ e where g.IdEmbarque = e.IdEmbarque) as FolioEmbarque\n" +
                "\t\t,(select IdRecoleccion from dbo.ProRecoleccionPQ r where r.IdRecoleccion =  e.IdRecoleccion) as foliorecoleccion\n" +
                "\t\t,g.FolioGuia\n" +
                "\t\t,(select FolioInforme from dbo.ProInformePQ r where r.IdInforme =  g.IdInforme) as FolioInforme\n" +
                "\t\t,e.Fecha\n" +
                "\t\t,g.Hora\n" +
                "\t\t,g.IdEstatusGuia\n" +
                "\t\t,e.EntregaEnSucursal\n" +
                "\t\t,e.TipoCambio\n" +
                "\t\t,e.IdTipoCobro\n" +
                "\t\t,e.NombreRemitente\n" +
                "\t\t,e.RFCRemitente\n" +
                "\t\t,e.DomicilioRemitente\n" +
                "\t\t,e.IdCodigoPostalRemitente\n" +
                "\t\t,e.IdCiudadRemitente\n" +
                "\t\t,e.CorreoRemitente\n" +
                "\t\t,e.TelefonoRemitente\n" +
                "\t\t,e.ContactoRemitente\n" +
                "\t\t,e.IdCiudadOrigen\n" +
                "\t\t,e.NombreDestinatario\n" +
                "\t\t,e.RFCDestinatario\n" +
                "\t\t,e.DomicilioDestinatario\n" +
                "\t\t,e.IdCodigoPostalDestinatario\n" +
                "\t\t,e.IdCiudadDestinatario\n" +
                "\t\t,e.CorreoDestinatario\n" +
                "\t\t,e.TelefonoDestinatario\n" +
                "\t\t,e.ContactoDestinatario\n" +
                "\t\t,e.IdCiudadDestino\n" +
                "\t\t,e.FechaEntrega\n" +
                "\t\t,e.HoraEntrega\n" +
                "\t\t,e.NoPaquetes\n" +
                "\t\t,e.NoSobres\n" +
                "\t\t,e.IdOperador\n" +
                "\t\t,e.IdUnidad\n" +
                "\t\t,e.FechaSalida\n" +
                "\t\t,e.HoraSalida\n" +
                "\t\t,g.FechaCancelacion\n" +
                "\t\t,g.UsuarioCancelacion\n" +
                "\t\t,e.MotivoCancelacion\n" +
                "\t\t,e.EntregarMismoDomicilio \n" +
                "\t\t,e.FechaLlegada \n" +
                "\t\t,e.HoraLlegada  \n" +
                "\t\t,e.CodigoPostalEntrega  \n" +
                "\t\t,e.IdCiudadEntrega \n" +
                "\t\t,e.IdZonaEntrega \n" +
                "\t\t,e.DomicilioEntrega  \n" +
                "\t\t,e.EntregarEn  \n" +
                "\t\t,DatosAdicionales \n" +
                "\t\t,g.Tracking,g.IdMoneda,g.CreadoPor,g.CreadoEl,g.ModificadoPor,g.ModificadoEl,g.IdGuia,g.IdSucursal,\n" +
                "\t\t(select Sucursal from CatSucursales s where s.IdSucursal = g.IdSucursal ) as Sucursal,\t\n" +
                "\t\t(select Estatus from CatEstatusGuiasPQ eg where eg.IdEstatusGuias = g.IdEstatusGuia ) as estatusguia,\n" +
                "\t\t(select OrigenDestino from CatOrigenesDestinos c where c.IdOrigenDestino = e.IdCiudadOrigen) as CiudadOrigen,\n" +
                "\t\t(select OrigenDestino from CatOrigenesDestinos c where c.IdOrigenDestino = e.IdCiudadDestino) as CiudadDestino\n" +
                "\t\t,g.ValorDeclarado,g.IdTipoServicio,\n" +
                "\t\t(select cs.Descripcion from CatTipoServicioPQ cs where cs.IdTipoServicio = g.IdTipoServicio) as TipoServicio\n" +
                "\n" +
                "\t\t\n" +
                "\t\tFROM ProGuiaPQ g inner join  ProEmbarquePQ e on g.IdGuia= e.IdGuia\n" +
                "\t\tWHERE g.IdGuia = (select i.IdGuia from ProInformeGuiaPQ i where i.IdInforme = "+id+" and i.IdGuia = g.IdGuia)";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray array = new JSONArray();
        JSONObject json;
        int idEmbarque,idGuia;
        Date fecha;
        String hora;
        Time horaSalida;
        java.util.Date horaFormateada;
        while(rs.next()) {
            json = new JSONObject();
            idEmbarque = 0;
            idGuia = 0;
            fecha = null;
            hora = null;
            horaSalida = rs.getTime("horaSalida");
            idEmbarque = rs.getInt("IdEmbarque");
            json.put("m_nIdEmbarque", idEmbarque);
            json.put("m_nTracking", rs.getString("Tracking"));
            json.put("m_nFolioGuia", rs.getString("FolioGuia"));
            fecha =  rs.getDate("Fecha");
            hora =  rs.getString("Hora");
            horaFormateada = new SimpleDateFormat("HH:mm:ss.S").parse(hora);
            json.put("m_dFecha", fecha);
            json.put("m_sHora", hora);
            json.put("m_sFechaHora", DateFormatUtils.format(fecha, "yyyy-MM-dd") +" "
                    + new SimpleDateFormat("yyyy-MM-dd").format(horaFormateada));
            idGuia = rs.getInt("IdGuia");
            json.put("m_nIdGuia", idGuia);
            json.put("m_nIdEstatusGuia", rs.getInt("IdEstatusGuia"));
            json.put("m_sFolioEmbarque", rs.getString("FolioEmbarque"));
            json.put("m_nFolioRecoleccion", rs.getInt("FolioRecoleccion"));
            json.put("m_sFolioInforme", rs.getString("FolioInforme"));
            json.put("m_sNOmbreRemitente", rs.getString("NOmbreRemitente"));
            json.put("m_sRFCRemitente", rs.getString("RFCRemitente"));
            json.put("m_sDomicilioRemitente", rs.getString("DomicilioRemitente"));
            json.put("m_nIdCodigoPostalRemitente", rs.getInt("IdCodigoPostalRemitente"));
            json.put("m_nCiudadRemitente", rs.getInt("IdCiudadRemitente"));
            json.put("m_sCorreoRemitente", rs.getString("CorreoRemitente"));
            json.put("m_sTelefonoRemitente", rs.getString("TelefonoRemitente"));
            json.put("m_bEntregaEnSucursal", rs.getBoolean("EntregaEnSucursal"));
            json.put("m_sContactoRemitente", rs.getString("ContactoRemitente"));
            json.put("m_nIdCiudadOrigen", rs.getInt("IdCiudadOrigen"));
            json.put("m_sNombreDestinatario", rs.getString("NombreDestinatario"));
            json.put("m_sRFCDestinatario", rs.getString("RFCDestinatario"));
            json.put("m_sDomicilioDestinatario", rs.getString("DomicilioDestinatario"));
            json.put("m_nIdCodigoPostalDestinatario", rs.getInt("IdCodigoPostalDestinatario"));
            json.put("m_nIdCIudadDestinatario", rs.getInt("IdCIudadDestinatario"));
            json.put("m_sCorreoDestinatario", rs.getString("CorreoDestinatario"));
            json.put("m_sTelefonoDestinatario", rs.getString("TelefonoDestinatario"));
            json.put("m_sContactoDestinatario", rs.getString("ContactoDestinatario"));
            json.put("m_nIdCiudadDestino", rs.getInt("IdCiudadDestino"));
            json.put("m_dFechaEntrega", rs.getDate("FechaEntrega"));
            json.put("m_tHoraEntrega",  rs.getTime("HoraEntrega"));
            json.put("m_nNoPaquetes", rs.getInt("NoPaquetes"));
            json.put("m_nNoSobres", rs.getInt("NoSobres"));
            json.put("m_nIdOperador", rs.getInt("IdOperador"));
            json.put("m_nIdUnidad", rs.getInt("IdUnidad"));
            json.put("m_dFechaSalida",  rs.getDate("FechaSalida"));
            json.put("m_tHoraSalida",  rs.getTime("HoraSalida"));
            json.put("IdSucursal", rs.getInt("IdSucursal"));
            json.put("m_sSucursal", rs.getString("Sucursal"));
            json.put("m_sTipoServicio", rs.getString("TipoServicio"));
            json.put("m_sEstatusGuia", rs.getString("EstatusGuia"));
            json.put("m_sCiudadOrigen", rs.getString("CiudadOrigen"));
            json.put("m_sCiudadDestino", rs.getString("CiudadDestino"));
            json.put("m_cValorDeclarado", rs.getFloat("ValorDeclarado"));
            json.put("m_nIdTipoServicio", rs.getInt("idTipoServicio"));
            json.put("m_arrClsDetalle", embarqueService.getListadoByIdEmabrque(idEmbarque,jdbcConnection));
            json.put("m_arClsGuiaConceptos", guiaService.getListadoByGuiaId(idGuia,jdbcConnection));
            array.put(json);
        }
        return array;
    }

    public void agregarInformeGuia(InformeGuiaRequest i, int id, int creadoPor, Connection jdbcConnection) throws Exception {
        try {
            String query = "insert into ProInformeGuiaPQ (IdInforme,IdGuia, CreadoPor) " +
                    "values (" + id + "," + i.getM_nIdGuia() + "," + creadoPor + ")";
            Statement statement = jdbcConnection.createStatement();
            statement.executeUpdate(query);

            String query2 = "update ProGuiaPQ set IdInforme = " + id + ",IdEstatusGuia=5 " +
                    "where IdGuia = " + i.getM_nIdGuia();
            Statement statement2 = jdbcConnection.createStatement();
            statement2.executeUpdate(query2);

            String query3 = "update ProEmbarquePQ set IdInforme = " + id + ", IdEstatusEmbarque = 18 " +
                    "where IdGuia = " + i.getM_nIdGuia();
            Statement statement3 = jdbcConnection.createStatement();
            statement3.executeUpdate(query3);
        }catch (Exception e){
            throw e;
        }
    }

}
