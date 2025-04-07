package com.certuit.base.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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

        JSONArray jsonArray = new JSONArray();

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
        return "" +
                "<!-- inicio de paquetes-->\n" +
                "    <div>\n" +
                "\n" +
                "    <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;\">\n" +
                "\n" +
                "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
                "               style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
                "            <tbody>\n" +
                "            <tr>\n" +
                "                <td style=\"direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;\">\n" +
                "                    <!--[if mso | IE]>\n" +
                "                    <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "\n" +
                "                        <tr>\n" +
                "\n" +
                "                            <td\n" +
                "                                    class=\"\" style=\"vertical-align:top;width:600px;\"\n" +
                "                            >\n" +
                "                    <![endif]-->\n" +
                "\n" +
                "                    <div class=\"mj-column-per-100 outlook-group-fix\"\n" +
                "                         style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;\">\n" +
                "\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
                "                               style=\"vertical-align:top;\" width=\"100%\">\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\"\n" +
                "                                    style=\"font-size:0px;padding:15px 15px 15px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px;\"><strong><span\n" +
                "                                                style=\"font-size: 16px;\">Paquete #NUM_PAQUETE</span></strong></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                        </table>\n" +
                "\n" +
                "                    </div>\n" +
                "\n" +
                "                    <!--[if mso | IE]>\n" +
                "                    </td>\n" +
                "\n" +
                "                    </tr>\n" +
                "\n" +
                "                    </table>\n" +
                "                    <![endif]-->\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "\n" +
                "    </div>\n" +
                "\n" +
                "\n" +
                "    <!--[if mso | IE]>\n" +
                "    </td>\n" +
                "    </tr>\n" +
                "    </table>\n" +
                "\n" +
                "    <table\n" +
                "            align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
                "    >\n" +
                "        <tr>\n" +
                "            <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
                "    <![endif]-->\n" +
                "\n" +
                "\n" +
                "    <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;\">\n" +
                "\n" +
                "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
                "               style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
                "            <tbody>\n" +
                "            <tr>\n" +
                "                <td style=\"direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;\">\n" +
                "                    <!--[if mso | IE]>\n" +
                "                    <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "\n" +
                "                        <tr>\n" +
                "\n" +
                "                            <td\n" +
                "                                    class=\"\" style=\"vertical-align:top;width:150px;\"\n" +
                "                            >\n" +
                "                    <![endif]-->\n" +
                "\n" +
                "                    <div class=\"mj-column-per-25 outlook-group-fix\"\n" +
                "                         style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;\">\n" +
                "\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
                "                               style=\"vertical-align:top;\" width=\"100%\">\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
                "                                            <span style=\"font-size: 14px;\">Peso</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
                "                                            <span style=\"font-size: 14px;\">#PESO kg</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                        </table>\n" +
                "\n" +
                "                    </div>\n" +
                "\n" +
                "                    <!--[if mso | IE]>\n" +
                "                    </td>\n" +
                "\n" +
                "                    <td\n" +
                "                            class=\"\" style=\"vertical-align:top;width:150px;\"\n" +
                "                    >\n" +
                "                    <![endif]-->\n" +
                "\n" +
                "                    <div class=\"mj-column-per-25 outlook-group-fix\"\n" +
                "                         style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;\">\n" +
                "\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
                "                               style=\"vertical-align:top;\" width=\"100%\">\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
                "                                            <span style=\"font-size: 14px;\">Largo</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
                "                                            <span style=\"font-size: 14px;\">#LARGO cm</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                        </table>\n" +
                "\n" +
                "                    </div>\n" +
                "\n" +
                "                    <!--[if mso | IE]>\n" +
                "                    </td>\n" +
                "\n" +
                "                    <td\n" +
                "                            class=\"\" style=\"vertical-align:top;width:150px;\"\n" +
                "                    >\n" +
                "                    <![endif]-->\n" +
                "\n" +
                "                    <div class=\"mj-column-per-25 outlook-group-fix\"\n" +
                "                         style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;\">\n" +
                "\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
                "                               style=\"vertical-align:top;\" width=\"100%\">\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
                "                                            <span style=\"font-size: 14px;\">Ancho</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
                "                                            <span style=\"font-size: 14px;\">#ANCHO cm</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                        </table>\n" +
                "\n" +
                "                    </div>\n" +
                "\n" +
                "                    <!--[if mso | IE]>\n" +
                "                    </td>\n" +
                "\n" +
                "                    <td\n" +
                "                            class=\"\" style=\"vertical-align:top;width:150px;\"\n" +
                "                    >\n" +
                "                    <![endif]-->\n" +
                "\n" +
                "                    <div class=\"mj-column-per-25 outlook-group-fix\"\n" +
                "                         style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;\">\n" +
                "\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
                "                               style=\"vertical-align:top;\" width=\"100%\">\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
                "                                            <span style=\"font-size: 14px;\">Alto</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
                "                                            <span style=\"font-size: 14px;\">#ALTO cm</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                        </table>\n" +
                "\n" +
                "                    </div>\n" +
                "\n" +
                "                    <!--[if mso | IE]>\n" +
                "                    </td>\n" +
                "\n" +
                "                    </tr>\n" +
                "\n" +
                "                    </table>\n" +
                "                    <![endif]-->\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "\n" +
                "    </div>\n" +
                "\n" +
                "\n" +
                "    <!--[if mso | IE]>\n" +
                "    </td>\n" +
                "    </tr>\n" +
                "    </table>\n" +
                "\n" +
                "    <table\n" +
                "            align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
                "    >\n" +
                "        <tr>\n" +
                "            <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
                "    <![endif]-->\n" +
                "\n" +
                "\n" +
                "    <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;\">\n" +
                "\n" +
                "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
                "               style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
                "            <tbody>\n" +
                "            <tr>\n" +
                "                <td style=\"direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;\">\n" +
                "                    <!--[if mso | IE]>\n" +
                "                    <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "\n" +
                "                        <tr>\n" +
                "\n" +
                "                            <td\n" +
                "                                    class=\"\" style=\"vertical-align:top;width:300px;\"\n" +
                "                            >\n" +
                "                    <![endif]-->\n" +
                "\n" +
                "                    <div class=\"mj-column-per-50 outlook-group-fix\"\n" +
                "                         style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:50%;\">\n" +
                "\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
                "                               style=\"vertical-align:top;\" width=\"100%\">\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
                "                                            <span style=\"font-size: 14px;\">Descripci&oacute;n</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
                "                                            <span style=\"font-size: 14px;\">#DESCRIPCION</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                        </table>\n" +
                "\n" +
                "                    </div>\n" +
                "\n" +
                "                    <!--[if mso | IE]>\n" +
                "                    </td>\n" +
                "\n" +
                "                    <td\n" +
                "                            class=\"\" style=\"vertical-align:top;width:300px;\"\n" +
                "                    >\n" +
                "                    <![endif]-->\n" +
                "\n" +
                "                    <div class=\"mj-column-per-50 outlook-group-fix\"\n" +
                "                         style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:50%;\">\n" +
                "\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
                "                               style=\"vertical-align:top;\" width=\"100%\">\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: right;\">\n" +
                "                                            <span style=\"font-size: 14px;\">Ctd</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: right;\">\n" +
                "                                            <span style=\"font-size: 14px;\">#CTD</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                        </table>\n" +
                "\n" +
                "                    </div>\n" +
                "\n" +
                "                    <!--[if mso | IE]>\n" +
                "                    </td>\n" +
                "\n" +
                "                    </tr>\n" +
                "\n" +
                "                    </table>\n" +
                "                    <![endif]-->\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "\n" +
                "    </div>\n" +
                "\n" +
                "\n" +
                "    <!--[if mso | IE]>\n" +
                "    </td>\n" +
                "    </tr>\n" +
                "    </table>\n" +
                "\n" +
                "    <table\n" +
                "            align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
                "    >\n" +
                "        <tr>\n" +
                "            <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
                "    <![endif]-->\n" +
                "\n" +
                "\n" +
                "    <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;\">\n" +
                "\n" +
                "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
                "               style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
                "            <tbody>\n" +
                "            <tr>\n" +
                "                <td style=\"direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;\">\n" +
                "                    <!--[if mso | IE]>\n" +
                "                    <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "\n" +
                "                        <tr>\n" +
                "\n" +
                "                            <td\n" +
                "                                    class=\"\" style=\"vertical-align:top;width:600px;\"\n" +
                "                            >\n" +
                "                    <![endif]-->\n" +
                "\n" +
                "                    <div class=\"mj-column-per-100 outlook-group-fix\"\n" +
                "                         style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;\">\n" +
                "\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
                "                               style=\"vertical-align:top;\" width=\"100%\">\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
                "                                            <span style=\"font-size: 14px;\">Observaciones</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" style=\"font-size:0px;padding:4px 15px 4px 15px;word-break:break-word;\">\n" +
                "\n" +
                "                                    <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
                "                                        <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
                "                                            <span style=\"font-size: 14px;\">#OBSERVACIONES</span></p></div>\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "\n" +
                "                        </table>\n" +
                "\n" +
                "                    </div>\n" +
                "\n" +
                "                    <!--[if mso | IE]>\n" +
                "                    </td>\n" +
                "\n" +
                "                    </tr>\n" +
                "\n" +
                "                    </table>\n" +
                "                    <![endif]-->\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "\n" +
                "    </div>\n" +
                "\n" +
                "\n" +
                "    <!--[if mso | IE]>\n" +
                "    </td>\n" +
                "    </tr>\n" +
                "    </table>\n" +
                "\n" +
                "    <table\n" +
                "            align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
                "    >\n" +
                "        <tr>\n" +
                "            <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
                "    <![endif]-->\n" +
                "</div>\n" +
                "<!-- fin de paquetes -->";
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
        return BigDecimal.valueOf(d).setScale(decimalPlace,BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
