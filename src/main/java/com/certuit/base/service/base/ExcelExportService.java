package com.certuit.base.service.base;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.certuit.base.util.UtilFuctions.convertArray;
import static com.certuit.base.util.UtilFuctions.convertObject;

public class ExcelExportService {
    private XSSFWorkbook workbook;
    private XSSFSheet sheetGuias,sheetPaquete,sheetModeloPaquete;
    private Connection jdbcConnection;

    public ExcelExportService(Connection jdbcConnection, int idCliente) {
        try {
            JSONObject base64File = getFileBase64(jdbcConnection,idCliente);
            File file = convertBase64ToFile(base64File.getString("ArchivoBase64"),base64File.getString("ArchivoNombre"));
            if (file == null){
                System.out.println("No se pudo generar el archivo");
            }else {
                workbook = new XSSFWorkbook(file);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public File convertBase64ToFile(String b64,String filename) {
        try {
            // Path of a file
            String home = "/home/administrador/";
            String FILEPATH = home+filename+".xlsx";
            File file = new File(FILEPATH);
            byte[] decoder = Base64.getDecoder().decode(b64);
            // Try block to check for exceptions
            // Initialize a pointer in file
            // using OutputStream
            OutputStream os = new FileOutputStream(file);

            // Starting writing the bytes in it
            os.write(decoder);

            // Display message onconsole for successful
            // execution

            // Close the file connections
            os.close();
            return file;
        }

        // Catch block to handle the exceptions
        catch (Exception e) {

            // Display exception on console
            return null;
        }
    }

    private void writeHeaderLine() {
        sheetGuias = workbook.createSheet("Guiass");
        sheetPaquete = workbook.createSheet("Paquetess");
        sheetModeloPaquete = workbook.getSheet("Productos");
        workbook.setSheetHidden(2, true);
        Row row = sheetGuias.createRow(0);
        Row row2 = sheetPaquete.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        //HOJA GUIAS
        createCellGuias(row, 0, "Numero guia", style);
        createCellGuias(row, 1, "Sucursal", style);
        createCellGuias(row, 2, "Cliente", style);
        createCellGuias(row, 3, "Nombre Remitente", style);
        createCellGuias(row, 4, "RFC Remitente", style);

        createCellGuias(row, 5, "Calle Remitente", style);
        createCellGuias(row, 6, "Numero Interior Remitente", style);
        createCellGuias(row, 7, "Numero Exterior Remitente", style);
        createCellGuias(row, 8, "Codigo Postal Remitente", style);
        createCellGuias(row, 9, "Colonia Remitente", style);
        createCellGuias(row, 10, "Correo Remitente", style);
        createCellGuias(row, 11, "Telefono Remitente", style);
        createCellGuias(row, 12, "Contacto Remitente", style);
        createCellGuias(row, 13, "Nombre Destinatario", style);
        createCellGuias(row, 14, "RFC Destinatario", style);
        createCellGuias(row, 15, "Calle Destinatario", style);

        createCellGuias(row, 16, "Numero Interior Destinatario", style);
        createCellGuias(row, 17, "Numero Exterior Destinatario", style);
        createCellGuias(row, 18, "Codigo Postal Destinatario", style);
        createCellGuias(row, 19, "Colonia Destinatario", style);
        createCellGuias(row, 20, "Correo Destinatario", style);

        createCellGuias(row, 21, "Telefono Destinatario", style);
        createCellGuias(row, 22, "Contacto Destinatario", style);
        createCellGuias(row, 23, "Latitud", style);
        createCellGuias(row, 24, "Longitud", style);
        createCellGuias(row, 25, "Con Cita", style);

        createCellGuias(row, 26, "Fecha Cita", style);
        createCellGuias(row, 27, "Hora Cita Minima", style);
        createCellGuias(row, 28, "Hora Cita Maxima", style);
        createCellGuias(row, 29, "Cita Pendiente", style);

        //HOJA PAQUETES
        createCellPaquetes(row2, 0, "Numero Guia", style);
        createCellPaquetes(row2, 1, "Descripcion", style);
        createCellPaquetes(row2, 2, "Embalaje", style);
        createCellPaquetes(row2, 3, "Cantidad", style);

        createCellPaquetes(row2, 4, "Largo", style);
        createCellPaquetes(row2, 5, "Ancho", style);
        createCellPaquetes(row2, 6, "Alto", style);
        createCellPaquetes(row2, 7, "Peso", style);
        createCellPaquetes(row2, 8, "Observaciones", style);
    }

    private void createCellGuias(Row row, int columnCount, Object value, CellStyle style) {
        sheetGuias.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void createCellPaquetes(Row row, int columnCount, Object value, CellStyle style){
        sheetPaquete.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void createCellModeloPaquetes(Row row, int columnCount, Object value, CellStyle style){
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    /*private void writeDataValidationLinesGuia()  {
        try {

        String[] sucursales = getSucursales();
        String[] clientes = getClientes();

        //Sucursal
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetGuias);//para crear validacion
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)//rango de valores del datavalidation
        dvHelper.createExplicitListConstraint(sucursales);

        //int lastRow = workbook.getSpreadsheetVersion().getLastRowIndex();//ultima fila
        CellRangeAddressList addressList = new CellRangeAddressList(1, 1000, 1, 1);
        XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressList);
        validation.setShowErrorBox(true);
        sheetGuias.addValidationData(validation);

        //Cliente
        XSSFDataValidationHelper dvHelper2 = new XSSFDataValidationHelper(sheetGuias);//para crear validacion
         dvConstraint = (XSSFDataValidationConstraint)//rango de valores del datavalidation
         dvHelper2.createExplicitListConstraint(clientes);

        //int lastRow = workbook.getSpreadsheetVersion().getLastRowIndex();//ultima fila
        CellRangeAddressList addressList2 = new CellRangeAddressList(1, 1000, 2, 2);
        validation = (XSSFDataValidation)dvHelper2.createValidation(dvConstraint, addressList2);
        validation.setShowErrorBox(true);
        sheetGuias.addValidationData(validation);

    } catch (Exception e) {
        e.printStackTrace();
    }
    }*/

    /*private void writeDataValidationLinesPaquetes(){
        try {

            String sname = "ModeloPaquete";
            //descripcion y embalaje
            String[] descripciones = getDescripciones();
            String[] embalajes = getEmbalaje();

            //EXCEL ESTA LIMITADO EN CUANTO AL TAMANO DE LA LONGITUD QUE SE AGREGA EN DATA VALIDATION
            //POR ESO ES MEJOR SETEAR A TRAVES DE UNA HOJA ESCONDIDA
            Name namedArea = workbook.createName();
            namedArea.setNameName("myDataArea");
            namedArea.setRefersToFormula(sname+"!$A$1:$A$"+descripciones.length);

            //Producto
            DataValidationHelper dvHelper = sheetPaquete.getDataValidationHelper();
            DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("myDataArea");

            CellRangeAddressList addressList = new CellRangeAddressList(1, 1000, 1, 1);
            DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
            sheetPaquete.addValidationData(validation);

            //Embalaje
            XSSFDataValidationHelper dvHelper2 = new XSSFDataValidationHelper(sheetPaquete);//para crear validacion
            dvConstraint = (XSSFDataValidationConstraint)//rango de valores del datavalidation
            dvHelper2.createExplicitListConstraint(embalajes);

            //int lastRow = workbook.getSpreadsheetVersion().getLastRowIndex();//ultima fila
            CellRangeAddressList addressList2 = new CellRangeAddressList(1, 1000, 2, 2);
            validation = (XSSFDataValidation)dvHelper2.createValidation(dvConstraint, addressList2);
            validation.setShowErrorBox(true);
            sheetPaquete.addValidationData(validation);

            //TOCA LLENAR 100 COLUMNAS CON LA FUNCION DE VLOOKUP
            for (int i = 0; i < 1000; i++) {

                Row row = sheetPaquete.createRow(i+1);//variable ya que rondara de 1 a 100

                //EMBALAJE
                String strFormulaEmbalaje = "IFERROR(VLOOKUP(B"+(i+2)+",ModeloPaquete!$A$1:$F$1000,2,FALSE()),\"\")";
                Cell cell0 = row.createCell(2);//variable de 1 a 100
                cell0.setCellFormula(strFormulaEmbalaje);

                //lARGO
                String strFormulaLargo = "IFERROR(VLOOKUP(B"+(i+2)+",ModeloPaquete!$A$1:$F$1000,3,FALSE()),\"\")";
                Cell cell = row.createCell(4);//variable de 1 a 100
                cell.setCellFormula(strFormulaLargo);

                //ANCHO
                String strFormulaAncho = "IFERROR(VLOOKUP(B"+(i+2)+",ModeloPaquete!$A$1:$F$1000,4,FALSE()),\"\")";
                Cell cell2 = row.createCell(5);//variable de 1 a 100
                cell2.setCellFormula(strFormulaAncho);

                //ALTO
                String strFormulaAlto = "IFERROR(VLOOKUP(B"+(i+2)+",ModeloPaquete!$A$1:$F$1000,5,FALSE()),\"\")";
                Cell cell3 = row.createCell(6);//variable de 1 a 100
                cell3.setCellFormula(strFormulaAlto);

                //PESO
                String strFormulaPeso = "IFERROR(VLOOKUP(B"+(i+2)+",ModeloPaquete!$A$1:$F$1000,6,FALSE()),\"\")";
                Cell cell4 = row.createCell(7);//variable de 1 a 100
                cell4.setCellFormula(strFormulaPeso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    private void addMonedasToFile() {
        try {
            CellStyle style = workbook.createCellStyle();
            JSONArray modeloListado = getMonedasList();
            for (int i = 0; i < modeloListado.length(); i++) {
                JSONObject modelo = modeloListado.getJSONObject(i);
                Row row = workbook.getSheet("Monedas").createRow(i);
                createCellModeloPaquetes(row, 0, modelo.getString("Moneda"), style);
                createCellModeloPaquetes(row, 1, modelo.getInt("IdMoneda"), style);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addTiposCambioToFile() {
        try {
            CellStyle style = workbook.createCellStyle();
            JSONArray modeloListado = getTiposCambioList();
            for (int i = 0; i < modeloListado.length(); i++) {
                JSONObject modelo = modeloListado.getJSONObject(i);
                Row row = workbook.getSheet("Tipos de cambio").createRow(i);
                createCellModeloPaquetes(row, 0, modelo.getString("TipoCambio"), style);
                createCellModeloPaquetes(row, 1, modelo.getInt("IdTipoCambio"), style);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addTiposCobroToFile() {
        try {
            CellStyle style = workbook.createCellStyle();
            JSONArray modeloListado = getTiposCobroList();
            for (int i = 0; i < modeloListado.length(); i++) {
                JSONObject modelo = modeloListado.getJSONObject(i);
                Row row = workbook.getSheet("Tipos de cobro").createRow(i);
                createCellModeloPaquetes(row, 0, modelo.getString("Descripcion"), style);
                createCellModeloPaquetes(row, 1, modelo.getInt("IdTipoCobro"), style);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addClientesToFile() {
        try {
            String query = "SELECT NumeroCliente, IdCliente, NombreFiscal\n" +
                    "FROM CatClientes";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray modeloListado = convertArray(rs);
            CellStyle style = workbook.createCellStyle();
            for (int i = 0; i < modeloListado.length(); i++) {
                JSONObject modelo = modeloListado.getJSONObject(i);
                Row row = workbook.getSheet("Clientes").createRow(i);
                createCellModeloPaquetes(row, 0, modelo.getInt("NumeroCliente"), style);
                createCellModeloPaquetes(row, 1, modelo.getInt("IdCliente"), style);
                createCellModeloPaquetes(row, 2, modelo.getString("NombreFiscal"), style);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addTiposSeguroToFile() {
        try {
            String query = "SELECT " +
                    "IdTipoSeguro," +
                    "Descripcion," +
                    "Porcentaje, " +
                    "AplicaTarifa " +
                    "FROM CatTipoSeguro";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray modeloListado = convertArray(rs);
            CellStyle style = workbook.createCellStyle();
            for (int i = 0; i < modeloListado.length(); i++) {
                JSONObject modelo = modeloListado.getJSONObject(i);
                Row row = workbook.getSheet("Tipos de seguro").createRow(i);
                createCellModeloPaquetes(row, 0, modelo.getString("Descripcion"), style);
                createCellModeloPaquetes(row, 1, modelo.getInt("IdTipoSeguro"), style);
                createCellModeloPaquetes(row, 2, modelo.getString("AplicaTarifa"), style);
                createCellModeloPaquetes(row, 3, modelo.getInt("Porcentaje"), style);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addRemitentesDestinatariosToFile(int idCliente) {
        try {
            String query = "SELECT Numero, IdRemitenteDestinatario, Nombre\n" +
                    "FROM CatRemitentesDestinatarios where IdCliente =  "+idCliente;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray modeloListado = convertArray(rs);
            CellStyle style = workbook.createCellStyle();
            for (int i = 0; i < modeloListado.length(); i++) {
                JSONObject modelo = modeloListado.getJSONObject(i);
                Row row = workbook.getSheet("RemitentesDestinatarios").createRow(i);
                createCellModeloPaquetes(row, 0, modelo.getInt("Numero"), style);
                createCellModeloPaquetes(row, 1, modelo.getInt("IdRemitenteDestinatario"), style);
                createCellModeloPaquetes(row, 2, modelo.getString("Nombre"), style);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addSucursalesToFile() {
        try {
            String query = "SELECT Sucursal, IdSucursal\n" +
                    "FROM CatSucursales";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray modeloListado = convertArray(rs);
            CellStyle style = workbook.createCellStyle();
            for (int i = 0; i < modeloListado.length(); i++) {
                JSONObject modelo = modeloListado.getJSONObject(i);
                Row row = workbook.getSheet("Sucursales").createRow(i);
                createCellModeloPaquetes(row, 0, modelo.getString("Sucursal"), style);
                createCellModeloPaquetes(row, 1, modelo.getInt("IdSucursal"), style);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addProductsToFile() {
        try {
            CellStyle style = workbook.createCellStyle();
            JSONArray modeloPaquetes = getProductosList();
            for (int i = 0; i < modeloPaquetes.length(); i++) {
                JSONObject modelo = modeloPaquetes.getJSONObject(i);
                Row row = workbook.getSheet("Productos").createRow(i);
                createCellModeloPaquetes(row, 0, modelo.getInt("NoProducto"), style);
                createCellModeloPaquetes(row, 1, modelo.getInt("IdProducto"), style);
                createCellModeloPaquetes(row, 2, modelo.getString("Descripcion"), style);
                createCellModeloPaquetes(row, 3, modelo.getString("Embalaje"), style);
                createCellModeloPaquetes(row, 4, modelo.getInt("IdEmbalaje"), style);
                createCellModeloPaquetes(row, 5, modelo.getDouble("Largo"), style);
                createCellModeloPaquetes(row, 6, modelo.getDouble("Alto"), style);
                createCellModeloPaquetes(row, 7, modelo.getDouble("Ancho"), style);
                createCellModeloPaquetes(row, 8, modelo.getDouble("Peso"), style);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

            Row row = sheetGuias.createRow(rowCount++);
            int columnCount = 0;

        createCellGuias(row, columnCount++,99, style);
        createCellGuias(row, columnCount++, "erick@gmail.com", style);
        createCellGuias(row, columnCount++, "Erick Sael Castillo", style);
        createCellGuias(row, columnCount++, "Desarrollador", style);
        createCellGuias(row, columnCount++, true, style);

        }*/

    //EXPORTAR ARCHIVO
    public void export(HttpServletResponse response) throws IOException {
        if (workbook != null) {
//            writeHeaderLine();
            // writeDataLines();
//            writeDataValidationLinesGuia();
//            writeDataValidationLinesPaquetes();
            /*addMonedasToFile();
            addTiposCambioToFile();
            addTiposCobroToFile();
            addClientesToFile();
            addTiposSeguroToFile();
            addRemitentesDestinatariosToFile(idCliente);
            addSucursalesToFile();
            addProductsToFile();*/
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        }
    }

    //DATOS A CARGAR EN HOJAS AUXILIARES Y DATA VALIDATIONS

    /*public String[] getSucursales() throws SQLException {
        String[] sucursales = {};
        Statement statement = jdbcConnection.createStatement();
        String SucursalesQuery = "select Sucursal from CatSucursalesUM";
        ResultSet sucursalesResult = statement.executeQuery(SucursalesQuery);

        List<String> auxSucursales = new ArrayList<String>(Arrays.asList(sucursales));
        while (sucursalesResult.next()) {
            auxSucursales.add(sucursalesResult.getString("Sucursal"));
        }
        return auxSucursales.toArray(sucursales);
    }

    public String[] getClientes() throws SQLException {
        String[] clientes = {};

        Statement statement = jdbcConnection.createStatement();
        String clientesQuery = "select NombreFiscal from CatClientesUM";
        ResultSet clientesResult = statement.executeQuery(clientesQuery);


        List<String> auxSucursales = new ArrayList<String>(Arrays.asList(clientes));
        while (clientesResult.next()) {
            auxSucursales.add(clientesResult.getString("NombreFiscal"));
        }
        return auxSucursales.toArray(clientes);
    }

    public String[] getDescripciones() throws SQLException {
        String[] descripciones = {};

        Statement statement = jdbcConnection.createStatement();
        String descripcionesQuery = "select Descripcion as Nombre from CatProductosUM";
        ResultSet descripcionesResult = statement.executeQuery(descripcionesQuery);

        List<String> auxSucursales = new ArrayList<String>(Arrays.asList(descripciones));
        while (descripcionesResult.next()) {
            auxSucursales.add(descripcionesResult.getString("Nombre"));
        }
        return auxSucursales.toArray(descripciones);
    }*/

    public String[] getEmbalaje() throws SQLException {
        String[] embalajes = {};

        Statement statement = jdbcConnection.createStatement();
        String embalajesQuery = "SELECT Nombre FROM CatEmbalajesPQ";
        ResultSet embalajesResult = statement.executeQuery(embalajesQuery);

        List<String> auxSucursales = new ArrayList<String>(Arrays.asList(embalajes));
        while (embalajesResult.next()) {
            auxSucursales.add(embalajesResult.getString("Nombre"));
        }
        return auxSucursales.toArray(embalajes);
    }

    /**Obtiene listado de monedas*/
    public JSONArray getMonedasList() throws SQLException {
        String query = "SELECT IdMoneda, Moneda FROM CatMonedasPQ";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray array = new JSONArray();
        JSONObject json;
        while(rs.next()){
            json = new JSONObject();
            json.put("IdMoneda", rs.getInt("IdMoneda"));
            json.put("Moneda", rs.getString("Moneda"));
            array.put(json);
        }
        return array;
    }

    /**Obtiene listado de de tipos de cambio*/
    public JSONArray getTiposCambioList() throws SQLException {
        String query = "SELECT " +
                "IdTipoCambio," +
                "TipoCambio " +
                "FROM CatTiposCambio ORDER BY Fecha Desc";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray array = new JSONArray();
        JSONObject json;
        while(rs.next()){
            json = new JSONObject();
            json.put("IdTipoCambio", rs.getInt("IdTipoCambio"));
            json.put("TipoCambio", rs.getString("TipoCambio"));
            array.put(json);
        }
        return array;
    }

    /**Obtiene listado de de tipos de cobro*/
    public JSONArray getTiposCobroList() throws SQLException {
        String query = "SELECT" +
                "  IdTipoCobro," +
                "  Descripcion" +
                " FROM CatTipoCobroPQ";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray array = new JSONArray();
        JSONObject json;
        while(rs.next()){
            json = new JSONObject();
            json.put("IdTipoCobro", rs.getInt("IdTipoCobro"));
            json.put("Descripcion", rs.getString("Descripcion"));
            array.put(json);
        }
        return array;
    }

    /**Obtiene listado de productos*/
    public JSONArray getProductosList() throws SQLException {
        String query = "SELECT CP.NoProducto,\n" +
                "       CP.IdProducto,\n" +
                "       CP.Descripcion,\n" +
                "       ISNULL(CE.Nombre, '')    as Embalaje,\n" +
                "       ISNULL(CE.IdEmbalaje, 0) as IdEmbalaje,\n" +
                "       CP.Largo,\n" +
                "       CP.Alto,\n" +
                "       CP.Ancho,\n" +
                "       CP.Peso\n" +
                "FROM CatProductosPQ CP\n" +
                "         left join CatEmbalajesPQ as CE\n" +
                "                   on CE.IdEmbalaje = CP.IdEmbalaje";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray array = new JSONArray();
        JSONObject json;
        while(rs.next()){
            json = new JSONObject();
            json.put("NoProducto", rs.getInt("NoProducto"));
            json.put("IdProducto", rs.getInt("IdProducto"));
            json.put("Descripcion", rs.getString("Descripcion"));
            json.put("Embalaje", rs.getString("Embalaje"));
            json.put("IdEmbalaje", rs.getInt("IdEmbalaje"));
            json.put("Largo", rs.getFloat("Largo"));
            json.put("Ancho", rs.getFloat("Ancho"));
            json.put("Alto", rs.getFloat("Alto"));
            json.put("Peso", rs.getFloat("Peso"));
            array.put(json);
        }
        return array;
    }

    /*public String getFileBase64(Connection jdbcConnection,int idCliente) throws SQLException {
        try {
            String query = "select ArchivoBase64 From CatPlantillasImportacionPQ where IdCliente = " + idCliente;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject json = convertObject(rs);
            if (json == null){
                return "";
            }
            return json.getString("ArchivoBase64");
        } catch (Exception e) {
            return "";
        }

    }*/

    public JSONObject getFileBase64(Connection jdbcConnection,int idCliente) throws SQLException {
        try {
            String query = "select ArchivoBase64, ArchivoNombre From CatPlantillasImportacionPQ where IdCliente = " + idCliente;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject json = convertObject(rs);
            if (json == null){
                return null;
            }
            return json;
        } catch (Exception e) {
            return null;
        }
    }

}
