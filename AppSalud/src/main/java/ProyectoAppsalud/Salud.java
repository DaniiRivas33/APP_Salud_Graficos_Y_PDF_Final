/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoAppsalud;
/**
 *
 * @author danie
 */

import javax.swing.*;
import java.awt.*;
import java.io.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Salud extends JFrame {

    private JTextField txtPeso, txtAltura, txtIMC;
    private JButton btnCalcular, btnGuardar, btnLeer, btnInsertarFila;
    private JLabel lblTitulo;
    private final File archivoExcel = new File("src/main/java/ProyectoAppsalud/DatosSalud.xlsx");

    public Salud() {
        setTitle("Salud");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //titulo
        
        lblTitulo = new JLabel("Salud - IMC", JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);
        //campos peso, altura y IMC
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Peso (kg):"), gbc);
        gbc.gridx = 1;
        txtPeso = new JTextField();
        add(txtPeso, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Altura (m):"), gbc);
        gbc.gridx = 1;
        txtAltura = new JTextField();
        add(txtAltura, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("IMC:"), gbc);
        gbc.gridx = 1;
        txtIMC = new JTextField();
        txtIMC.setEditable(false);
        add(txtIMC, gbc);
        
        //Botones
        gbc.gridy++;
        gbc.gridx = 0;
        btnCalcular = new JButton("Calcular IMC");
        add(btnCalcular, gbc);
        
        gbc.gridx = 1;
        btnGuardar = new JButton("Guardar en Excel");
        add(btnGuardar, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        btnLeer = new JButton("Leer datos Excel");
        add(btnLeer, gbc);

        gbc.gridx = 1;
        btnInsertarFila = new JButton("Insertar fila");
        add(btnInsertarFila, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        JButton btnPDF = new JButton("Generar PDF");
        add(btnPDF, gbc);
        btnPDF.addActionListener(e -> generarPDF());

        // Acciones
        btnCalcular.addActionListener(e -> calcularIMC());
        btnGuardar.addActionListener(e -> guardarDatosEnExcel());
        btnLeer.addActionListener(e -> leerDatosExcel());
        btnInsertarFila.addActionListener(e -> insertarFila());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void calcularIMC() {
        try {
            double peso = Double.parseDouble(txtPeso.getText());
            double altura = Double.parseDouble(txtAltura.getText());
            double imc = peso / (altura * altura);
            txtIMC.setText(String.format("%.2f", imc));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Introduce valores válidos (recuerda usar . en vez de, no añadas kg ni m)");
        }
    }

private void guardarDatosEnExcel() {
    try {
        XSSFWorkbook workbook;
        XSSFSheet hoja;

        if (archivoExcel.exists()) {
            try (FileInputStream fis = new FileInputStream(archivoExcel)) {
                workbook = new XSSFWorkbook(fis);
            }
            hoja = workbook.getSheetAt(0);
        } else {
            workbook = new XSSFWorkbook();
            hoja = workbook.createSheet("Salud");
        }

        if (hoja.getPhysicalNumberOfRows() == 0) {
            Row encabezado = hoja.createRow(0);
            encabezado.createCell(0).setCellValue("Registro");
            encabezado.createCell(1).setCellValue("Peso (kg)");
            encabezado.createCell(2).setCellValue("Altura (m)");
            encabezado.createCell(3).setCellValue("IMC");
        }

        int filaNueva = hoja.getLastRowNum() + 1;
        Row fila = hoja.createRow(filaNueva);
        fila.createCell(0).setCellValue(filaNueva);
        fila.createCell(1).setCellValue(Double.parseDouble(txtPeso.getText().replace(",", ".")));
        fila.createCell(2).setCellValue(Double.parseDouble(txtAltura.getText().replace(",", ".")));
        fila.createCell(3).setCellValue(Double.parseDouble(txtIMC.getText().replace(",", ".")));

        for (int i = 0; i <= 3; i++) hoja.autoSizeColumn(i);

        int ultimaFila = hoja.getLastRowNum();
        if (ultimaFila < 1) {
            try (FileOutputStream out = new FileOutputStream(archivoExcel)) {
                workbook.write(out);
            }
            workbook.close();
            JOptionPane.showMessageDialog(this, "Datos guardados. No hay suficientes filas para crear el gráfico.");
            return;
        }

        if (hoja.getCTWorksheet().isSetDrawing()) {
            hoja.getCTWorksheet().unsetDrawing();
        }

        XSSFDrawing drawing = hoja.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 5, 1, 15, 20);
        XSSFChart chart = drawing.createChart(anchor); // 👈 Tipo concreto XSSFChart
        chart.setTitleText("Evolución del IMC");
        chart.setTitleOverlay(false);

        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Registro");
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("IMC");

        XDDFDataSource<String> categorias = XDDFDataSourcesFactory.fromStringCellRange(
                hoja, new CellRangeAddress(1, ultimaFila, 0, 0));
        XDDFNumericalDataSource<Double> valores = XDDFDataSourcesFactory.fromNumericCellRange(
                hoja, new CellRangeAddress(1, ultimaFila, 3, 3));

        XDDFBarChartData data = (XDDFBarChartData)
                chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        data.setBarDirection(BarDirection.COL);
        data.setBarGrouping(BarGrouping.CLUSTERED);
        XDDFChartData.Series series = data.addSeries(categorias, valores);
        series.setTitle("IMC", null);
        chart.plot(data);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.BOTTOM);

        try (FileOutputStream out = new FileOutputStream(archivoExcel)) {
            workbook.write(out);
        }
                JOptionPane.showMessageDialog(this, "Datos guardados correctamente en:" + archivoExcel.getAbsolutePath());

        generarGraficoJPG(hoja);

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

private void generarGraficoJPG(XSSFSheet hoja) {
    try {
        org.jfree.data.category.DefaultCategoryDataset dataset = new org.jfree.data.category.DefaultCategoryDataset();

        for (int i = 1; i <= hoja.getLastRowNum(); i++) {
            Row fila = hoja.getRow(i);
            if (fila != null && fila.getCell(3) != null) {
                double imc = fila.getCell(3).getNumericCellValue();
                dataset.addValue(imc, "IMC", "Registro " + i);
            }
        }

        org.jfree.chart.JFreeChart chart = org.jfree.chart.ChartFactory.createBarChart(
                "Evolución del IMC",
                "Registro",
                "IMC",
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true, true, false
        );

        File jpgFile = new File(archivoExcel.getParentFile(), "Grafico_IMC.jpg");
        org.jfree.chart.ChartUtils.saveChartAsJPEG(jpgFile, chart, 800, 600);

        System.out.println("Gráfico guardado como: " + jpgFile.getAbsolutePath());

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al generar gráfico JPG: " + e.getMessage());
    }
}

private void generarPDF() {
    try {
        File pdfFile = new File(archivoExcel.getParentFile(), "Informe_Salud.pdf");

        try (org.apache.pdfbox.pdmodel.PDDocument documento = new org.apache.pdfbox.pdmodel.PDDocument()) {
            org.apache.pdfbox.pdmodel.PDPage pagina = new org.apache.pdfbox.pdmodel.PDPage();
            documento.addPage(pagina);

            try (org.apache.pdfbox.pdmodel.PDPageContentStream contenido =
                         new org.apache.pdfbox.pdmodel.PDPageContentStream(documento, pagina)) {

              
                PDType1Font fuenteTitulo = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font fuenteTexto = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                contenido.beginText();
                contenido.setFont(fuenteTitulo, 14);
                contenido.newLineAtOffset(100, 700);
                contenido.showText("Informe de Salud - IMC");

                contenido.setFont(fuenteTexto, 12);
                contenido.newLineAtOffset(0, -25);
                contenido.showText("Peso (kg): " + txtPeso.getText());
                contenido.newLineAtOffset(0, -20);
                contenido.showText("Altura (m): " + txtAltura.getText());
                contenido.newLineAtOffset(0, -20);
                contenido.showText("IMC calculado: " + txtIMC.getText());
                contenido.endText();

                File imgFile = new File(archivoExcel.getParentFile(), "Grafico_IMC.jpg");
                if (imgFile.exists()) {
                    var imagen = org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
                            .createFromFile(imgFile.getAbsolutePath(), documento);

                    contenido.drawImage(imagen, 100, 350, imagen.getWidth() / 3f, imagen.getHeight() / 3f);
                } else {
                }
            }

            documento.save(pdfFile);
        }

        JOptionPane.showMessageDialog(this, "PDF generado correctamente en: " + pdfFile.getAbsolutePath());
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al generar el PDF: " + ex.getMessage());
    }
}



private void leerDatosExcel() {
        if (!archivoExcel.exists()) {
            JOptionPane.showMessageDialog(this, "El archivo no existe todavía");
            return;
        }

        try (FileInputStream fis = new FileInputStream(archivoExcel);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet hoja = workbook.getSheetAt(0);
            StringBuilder sb = new StringBuilder("Datos registrados:\n");

            for (Row fila : hoja) {
                for (Cell celda : fila) {
                    sb.append(String.format("%-25s", celda.toString())); //separa los numeros 25 caracteres para que no se junten todos
                }
                sb.append("\n");
            }

            JOptionPane.showMessageDialog(this, sb.toString());
        } catch (IOException ex) {
        }
    }

    private void insertarFila() {
        if (!archivoExcel.exists()) {
            JOptionPane.showMessageDialog(this, "Primero guarda algún dato en Excel.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(archivoExcel);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet hoja = workbook.getSheetAt(0);
            int filaInsertar = 1; 
            hoja.shiftRows(filaInsertar, hoja.getLastRowNum(), 1);

            Row nuevaFila = hoja.createRow(filaInsertar);
            nuevaFila.createCell(0).setCellValue("Nueva Fila");
            nuevaFila.createCell(1).setCellValue("");
            nuevaFila.createCell(2).setCellValue("");

            try (FileOutputStream out = new FileOutputStream(archivoExcel)) {
                workbook.write(out);
            }

            JOptionPane.showMessageDialog(this, "Fila insertada correctamente.");
        } catch (IOException ex) {
        }
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Salud::new);
    }
}
