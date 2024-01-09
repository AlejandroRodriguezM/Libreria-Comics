package comicManagement;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Apis.ApiISBNGeneral;
import Apis.ApiMarvel;
import Controladores.VentanaAccionController;
import Funcionamiento.Utilidades;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import webScrap.WebScraperPreviewsWorld;

public class ComicImportList {

	@FXML
	void eliminarComicSeleccionado(ActionEvent event) {
		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (nav.alertaEliminar()) {

			if (idRow != null) {

				String id_comic = idRow.getID();
				comicsImportados.removeIf(c -> c.getID().equals(id_comic));

				tablaBBDD.getItems().clear();
				funcionesTabla.tablaBBDD(comicsImportados, tablaBBDD, columnList); // Llamada a funcion

				id_comic_selecionado = "";

				nombreComic.setText("");
				varianteComic.setText("");
				firmaComic.setText("");
				editorialComic.setText("");
				fechaComic.setValue(null);
				guionistaComic.setText("");
				dibujanteComic.setText("");
				prontInfo.setText(null);
				prontInfo.setOpacity(0);
				nombreKeyIssue.setText("");
				numeroComic.getEditor().clear(); // Limpiar el texto en el ComboBox
				formatoComic.getEditor().clear(); // Limpiar el texto en el ComboBox
				procedenciaComic.getEditor().clear(); // Limpiar el texto en el ComboBox
				estadoComic.getEditor().clear(); // Limpiar el texto en el ComboBox
				urlReferencia.setText("");
				precioComic.setText("");
				direccionImagen.setText("");
				imagencomic.setImage(null);
				numeroCajaComic.getEditor().clear();
				codigoComicTratar.setText("");
			}
		}
	}
	
	/**
	 * Realiza una búsqueda utilizando un código de importación y muestra los
	 * resultados en la interfaz gráfica.
	 *
	 * @param valorCodigo El código de importación a buscar.
	 */
	private void busquedaPorCodigoImportacion(String valorCodigo) {
		limpiarDatosPantalla();
		ApiISBNGeneral isbnGeneral = new ApiISBNGeneral();
		WebScraperPreviewsWorld previewsScraper = new WebScraperPreviewsWorld();
		botonGuardarCambioComic.setVisible(true);
		botonGuardarComic.setVisible(true);
		botonEliminarImportadoComic.setVisible(true);
		final int contadorFaltas[] = { 0 };
		final String codigosFaltantes[] = { "" };

		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + DBManager.DB_NAME;

		// Crear una tarea que se ejecutará en segundo plano
		Task<Boolean> tarea = new Task<Boolean>() {

			@Override
			protected Boolean call() throws Exception {

				final String finalValorCodigo = Utilidades.eliminarEspacios(valorCodigo).replace("-", "");

				String[] comicInfo = null;

				if (!finalValorCodigo.isEmpty()) {

					if (finalValorCodigo.length() == 9) {
						comicInfo = previewsScraper.displayComicInfo(finalValorCodigo.trim(), prontInfo);

					} else {
						comicInfo = ApiMarvel.infoComicCode(finalValorCodigo.trim(), prontInfo);
						contadorFaltas[0]++;

						if (comicInfo == null) {
							comicInfo = isbnGeneral.getBookInfo(finalValorCodigo.trim(), prontInfo);

							if (comicInfo == null) {
								contadorFaltas[0]++;
							}

						}
					}

					if (contadorFaltas[0] > 1) {
						codigosFaltantes[0] += "Falta comic con codigo: " + valorCodigo;
					}
					contadorFaltas[0] = 0;
					if (comprobarCodigo(comicInfo)) {
						rellenarTablaImport(comicInfo, finalValorCodigo.trim());
						return true;
					}
				}
				return false;
			}
		};

		iniciarAnimacionCargaImagen();

		// Configurar un manejador de eventos para actualizar la interfaz de usuario
		// cuando la tarea esté completa
		tarea.setOnSucceeded(ev -> {
			Platform.runLater(() -> {

				if (codigosFaltantes[0].length() > 0) {
					Utilidades.imprimirEnArchivo(codigosFaltantes[0], sourcePath);
				}
				prontInfo.setOpacity(0);
				prontInfo.setText("");
				detenerAnimacionCargaImagen();
			});
		});

		tarea.setOnFailed(ev -> {
			Platform.runLater(() -> {
				detenerAnimacionCargaImagen();
			});
		});

		// Iniciar la tarea en un nuevo hilo
		Thread thread = new Thread(tarea);
		thread.setDaemon(true); // Hacer que el hilo sea demonio para que se cierre al
								// salir de la aplicación
		// Iniciar la tarea
		thread.start();
	}
	
	/**
	 * Método que maneja el evento de guardar los datos de un cómic.
	 * 
	 * @param event El evento de acción que desencadena la llamada al método.
	 */
	@FXML
	void guardarDatos(ActionEvent event) {

		LocalDate fecha_comic;

		if (id_comic_selecionado != null) {
			String id_comic = id_comic_selecionado;

			String datos[] = camposComic();

			String nombre = (datos[0] != null || !datos[0].isEmpty()) ? datos[0] : "Vacio";

			String numero = (datos[1] != null || !datos[1].isEmpty()) ? datos[1] : "0";

			String variante = (datos[2] != null || !datos[2].isEmpty()) ? datos[2] : "Vacio";

			String firma = (datos[3] != null || !datos[3].isEmpty()) ? datos[3] : "";

			String editorial = (datos[4] != null || !datos[4].isEmpty()) ? datos[4] : "Vacio";

			String formato = (datos[5] != null || !datos[5].isEmpty()) ? datos[5] : "Grapa (Issue individual)";

			String procedencia = (datos[6] != null || !datos[6].isEmpty()) ? datos[6]
					: "Estados Unidos (United States)";

			if (datos[7] == null) {
				datos[7] = "2000-01-01";
				fecha_comic = LocalDate.parse(datos[7]);
			} else {
				fecha_comic = LocalDate.parse(datos[7]);
			}

			String guionista = (datos[8] != null || !datos[8].isEmpty()) ? datos[8] : "Vacio";

			String dibujante = (datos[9] != null || !datos[9].isEmpty()) ? datos[9] : "Vacio";

			String portada = (datos[10] != null || !datos[10].isEmpty()) ? datos[10] : "";

			String estado = (datos[11] != null || !datos[11].isEmpty()) ? datos[11] : "Comprado";

			String numCaja = datos[12];

			if (numCaja.isEmpty()) {
				numCaja = "0";
			}

			String key_issue = "Vacio";
			String key_issue_sinEspacios = datos[13].trim();

			Pattern pattern = Pattern.compile(".*\\w+.*");
			Matcher matcher = pattern.matcher(key_issue_sinEspacios);

			if (!key_issue_sinEspacios.isEmpty() && matcher.matches()) {
				key_issue = key_issue_sinEspacios;
			}

			String url_referencia = (datos[14] != null || !datos[14].isEmpty()) ? datos[14] : "";
			String precio_comic = (datos[15] != null && !datos[15].isEmpty()) ? datos[15] : "0";

			double valor_comic = Double.parseDouble(precio_comic);

			precio_comic = String.valueOf(Utilidades.convertirMonedaADolar(procedencia, valor_comic));

			if (url_referencia.isEmpty()) {
				url_referencia = "Sin referencia";
			}

			String codigo_comic = datos[16];

			Comic comic = new Comic(id_comic, nombre, numCaja, numero, variante, firma, editorial, formato, procedencia,
					fecha_comic.toString(), guionista, dibujante, estado, key_issue, "Sin puntuar", portada,
					url_referencia, precio_comic, codigo_comic);

			for (Comic c : comicsImportados) {
				if (c.getID().equals(id_comic)) {
					comicsImportados.set(comicsImportados.indexOf(c), comic);
					break;
				}
			}

			tablaBBDD.getItems().clear();
			funcionesTabla.tablaBBDD(comicsImportados, tablaBBDD, columnList); // Llamada a funcion
		}

	}
	
	/**
	 * Método que maneja el evento de guardar la lista de cómics importados.
	 * 
	 * @param event El evento de acción que desencadena la llamada al método.
	 * @throws IOException        Si ocurre un error de entrada/salida.
	 * @throws SQLException       Si ocurre un error de base de datos.
	 * @throws URISyntaxException
	 */
	@FXML
	void guardarListaImportados(ActionEvent event) throws IOException, SQLException, URISyntaxException {
		if (comicsImportados.size() > 0) {
			if (nav.alertaInsertar()) {
				libreria = new DBLibreriaManager();
				String mensajePront = "";
				detenerAnimacionPront();
				iniciarAnimacionCambioImagen();
				utilidad = new Utilidades();

				Collections.sort(comicsImportados, Comparator.comparing(Comic::getNombre));

				for (Comic c : comicsImportados) {
					c.setID("");
					libreria.insertarDatos(c);

					mensajePront += "Comic " + c.getNombre() + " con codigo " + c.getCodigo_comic()
							+ " introducido correctamente\n";

					Image imagenDeseo = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
					imagenFondo.setImage(imagenDeseo);

				}
				libreria.listasAutoCompletado();
				List<ComboBox<String>> comboboxes = VentanaAccionController.getComboBoxes();
				funcionesCombo.rellenarComboBox(comboboxes);

				comicsImportados.clear();
				tablaBBDD.getItems().clear();
				funcionesTabla.tablaBBDD(comicsImportados, tablaBBDD, columnList); // Llamada a funcion
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo.setText(mensajePront);
				detenerAnimacionPront();
			}
		}
	}
	
}
