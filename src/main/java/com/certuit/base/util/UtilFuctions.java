package com.certuit.base.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;

public class UtilFuctions {
    public static JSONArray convertArray(ResultSet resultSet) throws Exception {

        JSONArray jsonArray = new JSONArray();

        while (resultSet.next()) {
            int columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < columns; i++)
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));

            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public static JSONObject convertObject(ResultSet resultSet) throws Exception {

        if (resultSet.next()) {
            int columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < columns; i++)
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));
            return obj;
        }
        return null;
    }

    public static String converTemplate(JSONObject data, String template){
        String templateTemp = template;
        templateTemp = templateTemp.replaceAll("@Cliente", data.getString("ClientePaga"));
        templateTemp = templateTemp.replaceAll("@FechaTimbrado", data.getString("FechaTimbrado"));
        templateTemp = templateTemp.replaceAll("@Folio", data.getString("Folio"));

        return templateTemp;

    }

    public static File convertXMLToFile(String xmlText, String fileName) throws IOException {

        File someFile = new File( fileName+".xml");
        FileOutputStream fos = new FileOutputStream(someFile);
        fos.write(xmlText.getBytes());
        fos.flush();
        fos.close();
        return someFile;
    }

    public static String getPlantillaPaquetesCorreo(){
        return """
                <!-- inicio de paquetes-->
                    <div>
                
                    <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;">
                
                        <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation"
                               style="background:#ffffff;background-color:#ffffff;width:100%;">
                            <tbody>
                            <tr>
                                <td style="direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;">
                                    <!--[if mso | IE]>
                                    <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                
                                        <tr>
                
                                            <td
                                                    class="" style="vertical-align:top;width:600px;"
                                            >
                                    <![endif]-->
                
                                    <div class="mj-column-per-100 outlook-group-fix"
                                         style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;">
                
                                        <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                               style="vertical-align:top;" width="100%">
                
                                            <tr>
                                                <td align="left"
                                                    style="font-size:0px;padding:15px 15px 15px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px;"><strong><span
                                                                style="font-size: 16px;">Paquete #NUM_PAQUETE</span></strong></p></div>
                
                                                </td>
                                            </tr>
                
                                        </table>
                
                                    </div>
                
                                    <!--[if mso | IE]>
                                    </td>
                
                                    </tr>
                
                                    </table>
                                    <![endif]-->
                                </td>
                            </tr>
                            </tbody>
                        </table>
                
                    </div>
                
                
                    <!--[if mso | IE]>
                    </td>
                    </tr>
                    </table>
                
                    <table
                            align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                    >
                        <tr>
                            <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                    <![endif]-->
                
                
                    <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;">
                
                        <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation"
                               style="background:#ffffff;background-color:#ffffff;width:100%;">
                            <tbody>
                            <tr>
                                <td style="direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;">
                                    <!--[if mso | IE]>
                                    <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                
                                        <tr>
                
                                            <td
                                                    class="" style="vertical-align:top;width:150px;"
                                            >
                                    <![endif]-->
                
                                    <div class="mj-column-per-25 outlook-group-fix"
                                         style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;">
                
                                        <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                               style="vertical-align:top;" width="100%">
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                            <span style="font-size: 14px;">Peso</span></p></div>
                
                                                </td>
                                            </tr>
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                            <span style="font-size: 14px;">#PESO kg</span></p></div>
                
                                                </td>
                                            </tr>
                
                                        </table>
                
                                    </div>
                
                                    <!--[if mso | IE]>
                                    </td>
                
                                    <td
                                            class="" style="vertical-align:top;width:150px;"
                                    >
                                    <![endif]-->
                
                                    <div class="mj-column-per-25 outlook-group-fix"
                                         style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;">
                
                                        <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                               style="vertical-align:top;" width="100%">
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                            <span style="font-size: 14px;">Largo</span></p></div>
                
                                                </td>
                                            </tr>
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                            <span style="font-size: 14px;">#LARGO cm</span></p></div>
                
                                                </td>
                                            </tr>
                
                                        </table>
                
                                    </div>
                
                                    <!--[if mso | IE]>
                                    </td>
                
                                    <td
                                            class="" style="vertical-align:top;width:150px;"
                                    >
                                    <![endif]-->
                
                                    <div class="mj-column-per-25 outlook-group-fix"
                                         style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;">
                
                                        <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                               style="vertical-align:top;" width="100%">
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                            <span style="font-size: 14px;">Ancho</span></p></div>
                
                                                </td>
                                            </tr>
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                            <span style="font-size: 14px;">#ANCHO cm</span></p></div>
                
                                                </td>
                                            </tr>
                
                                        </table>
                
                                    </div>
                
                                    <!--[if mso | IE]>
                                    </td>
                
                                    <td
                                            class="" style="vertical-align:top;width:150px;"
                                    >
                                    <![endif]-->
                
                                    <div class="mj-column-per-25 outlook-group-fix"
                                         style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;">
                
                                        <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                               style="vertical-align:top;" width="100%">
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                            <span style="font-size: 14px;">Alto</span></p></div>
                
                                                </td>
                                            </tr>
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                            <span style="font-size: 14px;">#ALTO cm</span></p></div>
                
                                                </td>
                                            </tr>
                
                                        </table>
                
                                    </div>
                
                                    <!--[if mso | IE]>
                                    </td>
                
                                    </tr>
                
                                    </table>
                                    <![endif]-->
                                </td>
                            </tr>
                            </tbody>
                        </table>
                
                    </div>
                
                
                    <!--[if mso | IE]>
                    </td>
                    </tr>
                    </table>
                
                    <table
                            align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                    >
                        <tr>
                            <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                    <![endif]-->
                
                
                    <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;">
                
                        <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation"
                               style="background:#ffffff;background-color:#ffffff;width:100%;">
                            <tbody>
                            <tr>
                                <td style="direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;">
                                    <!--[if mso | IE]>
                                    <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                
                                        <tr>
                
                                            <td
                                                    class="" style="vertical-align:top;width:300px;"
                                            >
                                    <![endif]-->
                
                                    <div class="mj-column-per-50 outlook-group-fix"
                                         style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:50%;">
                
                                        <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                               style="vertical-align:top;" width="100%">
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                            <span style="font-size: 14px;">Descripci&oacute;n</span></p></div>
                
                                                </td>
                                            </tr>
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                            <span style="font-size: 14px;">#DESCRIPCION</span></p></div>
                
                                                </td>
                                            </tr>
                
                                        </table>
                
                                    </div>
                
                                    <!--[if mso | IE]>
                                    </td>
                
                                    <td
                                            class="" style="vertical-align:top;width:300px;"
                                    >
                                    <![endif]-->
                
                                    <div class="mj-column-per-50 outlook-group-fix"
                                         style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:50%;">
                
                                        <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                               style="vertical-align:top;" width="100%">
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: right;">
                                                            <span style="font-size: 14px;">Ctd</span></p></div>
                
                                                </td>
                                            </tr>
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: right;">
                                                            <span style="font-size: 14px;">#CTD</span></p></div>
                
                                                </td>
                                            </tr>
                
                                        </table>
                
                                    </div>
                
                                    <!--[if mso | IE]>
                                    </td>
                
                                    </tr>
                
                                    </table>
                                    <![endif]-->
                                </td>
                            </tr>
                            </tbody>
                        </table>
                
                    </div>
                
                
                    <!--[if mso | IE]>
                    </td>
                    </tr>
                    </table>
                
                    <table
                            align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                    >
                        <tr>
                            <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                    <![endif]-->
                
                
                    <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;">
                
                        <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation"
                               style="background:#ffffff;background-color:#ffffff;width:100%;">
                            <tbody>
                            <tr>
                                <td style="direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;">
                                    <!--[if mso | IE]>
                                    <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                
                                        <tr>
                
                                            <td
                                                    class="" style="vertical-align:top;width:600px;"
                                            >
                                    <![endif]-->
                
                                    <div class="mj-column-per-100 outlook-group-fix"
                                         style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;">
                
                                        <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                               style="vertical-align:top;" width="100%">
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                            <span style="font-size: 14px;">Observaciones</span></p></div>
                
                                                </td>
                                            </tr>
                
                                            <tr>
                                                <td align="left" style="font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;">
                
                                                    <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                        <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                            <span style="font-size: 14px;">#OBSERVACIONES</span></p></div>
                
                                                </td>
                                            </tr>
                
                                        </table>
                
                                    </div>
                
                                    <!--[if mso | IE]>
                                    </td>
                
                                    </tr>
                
                                    </table>
                                    <![endif]-->
                                </td>
                            </tr>
                            </tbody>
                        </table>
                
                    </div>
                
                
                    <!--[if mso | IE]>
                    </td>
                    </tr>
                    </table>
                
                    <table
                            align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                    >
                        <tr>
                            <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                    <![endif]-->
                </div>
                <!-- fin de paquetes -->""";
    }

    /**Concatena un string agregando una coma y comillas simples alrededor del string dado*/
    public static String addString(String variable){
        return ",'"+variable+ "'";
    }
    public static String addInt(int variable){
        return ","+variable;
    }
    public static String addFloat(float variable){
        return ","+variable;
    }
    public static String addBoolean(boolean variable){
        return ","+(variable ? 1:0);
    }

    public static float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, RoundingMode.HALF_UP).floatValue();
    }
}
