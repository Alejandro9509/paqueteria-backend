package com.certuit.base.service.base;

import com.certuit.base.util.DBConection;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServices {

    @Autowired
    DBConection dbConection;
    @Value("${spring.report.location}")
    String reportLocation;

    public String generateReportGuia(int type, int id, String rfc) throws JRException {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/" + "ProGuia/RptProGuia.jrxml");
            reportParameters.put("Id_Guia", Math.toIntExact(id));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateReportInforme(int type, int id, String rfc) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProInforme/RptProInforme.jrxml");
            reportParameters.put("IdInforme", Math.toIntExact(id));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateReportCorteCaja(int type, int id, String rfc) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProCorteCaja/RptProCorteCaja.jrxml");
            reportParameters.put("IdCorte", Math.toIntExact(id));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateReportCorteCajaGeneral(int type, String fechaHora, String rfc) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            String[] partes = fechaHora.split(" ");
            String fecha = partes[0];
            String hora = partes[1];
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProCorteCaja/RptProCorteCajaGeneralMain.jrxml");
            reportParameters.put("FechaCorte", fecha);
            reportParameters.put("HoraCorte", hora);
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateReportUltimaMilla(int type, int id, String rfc) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProInformeUltimaMilla/RptProInformeUltimaMilla.jrxml");
            reportParameters.put("IdUltimaMilla", Math.toIntExact(id));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateReportRecoleccion(int type, int id, String rfc) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProRecoleccion/RptProRecoleccion.jrxml");
            reportParameters.put("IdRecoleccion", Math.toIntExact(id));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateReporEmbarque(int type, int id, String rfc) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProEmbarque/RptProEmbarque.jrxml");
            reportParameters.put("IdEmbarque", Math.toIntExact(id));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateReporCFDI(int type, int id, String rfc) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProFormatoTimbrado/ProFormatoCFDITrasladoCartaPorte20.jrxml");
            reportParameters.put("IdGuia", Math.toIntExact(id));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateReporCFDIViaje(int type, int id, int idInforme, String rfc) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProFormatoTimbradoViaje/ProFormatoCFDITrasladoCartaPorte20.jrxml");
            reportParameters.put("Id_Viaje", Math.toIntExact(id));
            reportParameters.put("IdInforme", Math.toIntExact(idInforme));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateReporCFDIGuia(int type, int id, String rfc) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProFormatoTimbradoUltimaMilla/ProFormatoCFDITrasladoCartaPorte20.jrxml");
            reportParameters.put("Id_Guia", Math.toIntExact(id));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateReporCFDIRecoleccion(int type, int id, String rfc) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProFormatoTimbradoPrimeraMilla/ProFormatoCFDITrasladoCartaPorte20.jrxml");
            reportParameters.put("Id_Guia", Math.toIntExact(id));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public File generateReporCFDIViaje(int type, int id, int idInforme, String rfc, String fileName) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProFormatoTimbradoViaje/ProFormatoCFDITrasladoCartaPorte20.jrxml");
            reportParameters.put("Id_Viaje", Math.toIntExact(id));
            reportParameters.put("IdInforme", Math.toIntExact(idInforme));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                File someFile = new File(fileName + ".pdf");
                FileOutputStream fos = new FileOutputStream(someFile);
                fos.write(pdf);
                fos.flush();
                fos.close();
                return someFile;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateReporEtiquetasGuia(int type, int id, String rfc) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProEtiqueta/RtpProGuiaEtiqueta.jrxml");
            reportParameters.put("ID", Math.toIntExact(id));
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateReporEtiquetasParcialesGuia(int type, int id, String rfc) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProEtiqueta/RtpProGuiaEtiquetaRangos.jrxml");
            reportParameters.put("ID", Math.toIntExact(id));
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                return Base64.getEncoder().encodeToString(pdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public File generateReporCFDIRecoleccion(int type, int id, String rfc, String fileName) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProFormatoTimbradoPrimeraMilla/ProFormatoCFDITrasladoCartaPorte20.jrxml");
            reportParameters.put("Id_Guia", Math.toIntExact(id));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                File someFile = new File(fileName + ".pdf");
                FileOutputStream fos = new FileOutputStream(someFile);
                fos.write(pdf);
                fos.flush();
                fos.close();

                return someFile;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public File generateReporCFDIGuia(int type, int id, String rfc, String fileName) {
        Map<String, Object> reportParameters = new HashMap<>();
        try {
            JasperReport jr = JasperCompileManager.compileReport(reportLocation + rfc + "/"
                    + "ProFormatoTimbradoPrimeraMilla/ProFormatoCFDITrasladoCartaPorte20.jrxml");
            reportParameters.put("Id_Guia", Math.toIntExact(id));
            reportParameters.put("BaseDir", reportLocation + rfc + "/");
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jr, reportParameters, jdbcConnection);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                File someFile = new File(fileName + ".pdf");
                FileOutputStream fos = new FileOutputStream(someFile);
                fos.write(pdf);
                fos.flush();
                fos.close();
                return someFile;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
