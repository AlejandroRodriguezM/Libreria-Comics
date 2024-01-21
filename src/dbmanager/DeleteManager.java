package dbmanager;

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

					String[] sentencia = { "delete from comicsbbdd", "alter table comicsbbdd AUTO_INCREMENT = 1;" };

					Utilidades.copia_seguridad();
					Utilidades.eliminarArchivosEnCarpeta();

					DBLibreriaManager.limpiarListasPrincipales();

					CompletableFuture<Boolean> ejecucionResult = CommonFunctions.ejecutarPreparedStatementAsync(sentencia);
					boolean ejecucionExitosa = ejecucionResult.join();

					futureResult.complete(ejecucionExitosa);
				} catch (Exception e) {
					futureResult.completeExceptionally(e);
				}
				return null;
			}
		};

		task.setOnFailed(e -> futureResult.completeExceptionally(task.getException()));

		Thread thread = new Thread(task);
		thread.start();

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
