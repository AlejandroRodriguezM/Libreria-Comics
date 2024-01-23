package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import Funcionamiento.Utilidades;
import javafx.concurrent.Task;

public class DeleteManager {

	/**
	 * Borra el contenido de la base de datos.
	 *
	 * @return
	 * @throws SQLException
	 */
	public static CompletableFuture<Boolean> deleteTable() {
		CompletableFuture<Boolean> futureResult = new CompletableFuture<>();

		Task<Void> task = new Task<>() {
			@Override
			protected Void call() throws Exception {
				try {
					if (DBLibreriaManager.listaNombre.isEmpty()) {
						futureResult.complete(false);
						return null;
					}


					Utilidades.copiaSeguridad();
					Utilidades.eliminarArchivosEnCarpeta();

					DBLibreriaManager.limpiarListasPrincipales();

					CompletableFuture<Boolean> ejecucionResult = reiniciarBaseDatosAsync();

					// Manejar resultado de la ejecución de la sentencia de borrado
					ejecucionResult.whenComplete((result, exception) -> {
						if (exception != null) {
							futureResult.completeExceptionally(exception);
						} else {
							futureResult.complete(result);
						}
					});

				} catch (Exception e) {
					futureResult.completeExceptionally(e);
				}
				return null;
			}
		};

		task.setOnFailed(e -> futureResult.completeExceptionally(task.getException()));

		// Iniciar el hilo en el mismo momento que se crea
		new Thread(task).start();

		return futureResult;
	}

    public static CompletableFuture<Boolean> reiniciarBaseDatosAsync() {
        CompletableFuture<Boolean> futureResult = new CompletableFuture<>();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try (Connection conn = ConectManager.conexion();
                     PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM comicsbbdd");
                     PreparedStatement resetAutoIncrementStatement = conn.prepareStatement("ALTER TABLE comicsbbdd AUTO_INCREMENT = 1")) {

                    int deletedRows = deleteStatement.executeUpdate();
                    resetAutoIncrementStatement.executeUpdate();

                    System.out.println("Número de filas eliminadas: " + deletedRows);

                    // Completa el futuro con true si todo se realiza correctamente
                    futureResult.complete(true);
                } catch (Exception e) {
                    // Completa el futuro con la excepción si hay un error
                    futureResult.completeExceptionally(e);
                }
                return null;
            }
        };

        // Maneja cualquier excepción que pueda ocurrir durante la ejecución del task
        task.setOnFailed(e -> futureResult.completeExceptionally(task.getException()));

        // Ejecuta el task en un hilo de fondo gestionado por JavaFX
        new Thread(task).start();

        return futureResult;
    }

	/**
	 * Realiza acciones específicas en la base de datos para un comic según la
	 * operación indicada.
	 *
	 * @param id        El ID del comic a modificar.
	 * @param operacion La operación a realizar: "Vender", "En venta" o "Eliminar".
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static void eliminarComicBBDD(String id) throws SQLException {
		String sentenciaSQL = null;

		sentenciaSQL = "DELETE FROM comicsbbdd WHERE ID = ?";

		CommonFunctions.modificarDatos(id, sentenciaSQL);
	}

}
