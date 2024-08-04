package UNIT_TEST;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RemoveQuotes {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:\\Users\\mrmis\\AppData\\Roaming\\libreria\\edeee.db"; // Cambia esto al nombre de tu base de datos

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                
                // Obtener todas las tablas
                try (ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
                    while (tables.next()) {
                        String tableName = tables.getString("TABLE_NAME");
                        System.out.println("Procesando tabla: " + tableName);
                        
                        try (Statement stmt = conn.createStatement()) {
                            // Obtener columnas de la tabla
                            String pragmaQuery = "PRAGMA table_info(" + tableName + ")";
                            try (ResultSet columns = stmt.executeQuery(pragmaQuery)) {
                                boolean foundTextColumns = false;
                                while (columns.next()) {
                                    String columnName = columns.getString("name");
                                    String columnType = columns.getString("type");
                                    System.out.println("Columna encontrada: " + columnName + " Tipo: " + columnType);

                                    if ("TEXT".equalsIgnoreCase(columnType)) {
                                        foundTextColumns = true;
                                        String updateSQL = "UPDATE " + tableName + " SET " + columnName +
                                                " = REPLACE(REPLACE(" + columnName + ", '\"', ''), '''', '')";
                                        System.out.println("Ejecutando: " + updateSQL); // Ver las consultas generadas
                                        try {
                                            stmt.executeUpdate(updateSQL);
                                        } catch (SQLException e) {
                                            System.out.println("Error al ejecutar la actualización: " + e.getMessage());
                                        }
                                    }
                                }
                                if (!foundTextColumns) {
                                    System.out.println("No se encontraron columnas de tipo TEXT en la tabla: " + tableName);
                                }
                            } catch (SQLException e) {
                                System.out.println("Error al obtener columnas para la tabla " + tableName + ": " + e.getMessage());
                            }
                        } catch (SQLException e) {
                            System.out.println("Error al crear Statement para la tabla " + tableName + ": " + e.getMessage());
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Error al obtener tablas: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        }
    }
}

