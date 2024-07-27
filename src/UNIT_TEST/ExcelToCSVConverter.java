package UNIT_TEST;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class ExcelToCSVConverter extends Application {

    @Override
    public void start(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            try {
                convertExcelToCSV(selectedFile);
                System.out.println("Conversion successful!");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("An error occurred during conversion.");
            }
        } else {
            System.out.println("No file selected.");
        }
    }

    private void convertExcelToCSV(File excelFile) throws IOException {
        FileInputStream fis = new FileInputStream(excelFile);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        // Determine CSV file name
        String csvFileName = Paths.get(excelFile.getParent(), excelFile.getName().replace(".xlsx", "_nuevo.csv")).toString();
        BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName));

        // Write header with ; at the end of each field
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            StringBuilder headerString = new StringBuilder();
            boolean firstCell = true;
            for (Cell cell : headerRow) {
                if (!firstCell) {
                    headerString.append(';');
                }
                String cellValue = (cell == null) ? "" : cell.toString();
                headerString.append(cellValue);
                firstCell = false;
            }
            // Ensure header ends with ;
            headerString.append(';');
            writer.write(headerString.toString());
            writer.newLine();
        }

        // Process the rest of the rows
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                StringBuilder rowString = new StringBuilder(";");
                boolean firstCell = true;

                // Add a ; for the idComic column, which is always empty
                rowString.append(';');

                for (Cell cell : row) {
                    if (!firstCell) {
                        rowString.append(';');
                    }
                    switch (cell.getCellType()) {
                        case STRING:
                            rowString.append(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            rowString.append(cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            rowString.append(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            rowString.append(cell.getCellFormula());
                            break;
                        default:
                            rowString.append("");
                    }
                    firstCell = false;
                }

                // Ensure each row ends with ;
                rowString.append(';');
                writer.write(rowString.toString());
                writer.newLine();
            }
        }

        writer.close();
        workbook.close();
        fis.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
