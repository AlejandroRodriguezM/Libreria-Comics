package funcionesManagment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import Apis.ApiISBNGeneral;
import Apis.ApiMarvel;
import Controladores.CargaComicsController;
import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.DatabaseManagerDAO;
import dbmanager.ListaComicsDAO;
import dbmanager.UpdateManager;
import ficherosFunciones.FuncionesFicheros;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesInterfaz.FuncionesTableView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import webScrap.WebScrapGoogleLeagueOfComics;
import webScrap.WebScraperPreviewsWorld;

public class AccionFuncionesComunes {

	private static File fichero;
	private static AtomicInteger contadorErrores;
	private static AtomicInteger comicsProcesados;
	private static AtomicInteger mensajeIdCounter;
	private static AtomicInteger numLineas;
	private static AtomicReference<CargaComicsController> cargaComicsControllerRef;
	private static StringBuilder codigoFaltante;
	private static HashSet<String> mensajesUnicos = new HashSet<>();

	/**
	 * Obtenemos el directorio de inicio del usuario
	 */
	private static final String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	private static final String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de la clase FuncionesComboBox para el manejo de ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	public static String TIPO_ACCION = getTipoAccion();

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	private static AccionReferencias referenciaVentanaPrincipal = getReferenciaVentanaPrincipal();

	private static AccionControlUI accionRellenoDatos = new AccionControlUI();

	/**
	 * Procesa la información de un cómic, ya sea para realizar una modificación o
	 * una inserción en la base de datos.
	 *
	 * @param comic          El cómic con la información a procesar.
	 * @param esModificacion Indica si se está realizando una modificación (true) o
	 *                       una inserción (false).
	 * @throws Exception
	 */
	public void procesarComic(Comic comic, boolean esModificacion) {
		final List<Comic> listaComics;
		final List<Comic> comicsFinal;

		getReferenciaVentana().getProntInfo().setOpacity(1);
		if (!accionRellenoDatos.camposComicSonValidos()) {
			String mensaje = "Error. Debes de introducir los datos correctos";
			AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfo());
			comicsFinal = ListaComicsDAO.comicsImportados;
			Platform.runLater(() -> FuncionesTableView.tablaBBDD(comicsFinal));

			return;
		}

		String codigoImagen = Utilidades.generarCodigoUnico(carpetaPortadas(Utilidades.nombreDB()) + File.separator);
		String mensaje = "";
		try {
			Utilidades.redimensionarYGuardarImagen(comic.getImagen(), codigoImagen);
			comic.setImagen(carpetaPortadas(Utilidades.nombreDB()) + File.separator + codigoImagen + ".jpg");
			if (esModificacion) {
				ComicManagerDAO.actualizarComicBBDD(comic, "modificar");
				mensaje = "Has modificado correctamente el cómic";
			} else {

				ComicManagerDAO.insertarDatos(comic, true);
				mensaje = " Has introducido correctamente el cómic";
				Comic newSelection = getReferenciaVentana().getTablaBBDD().getSelectionModel().getSelectedItem();

				if (newSelection != null) {
					listaComics = ListaComicsDAO.comicsImportados;
					String idComic = newSelection.getid();
					ListaComicsDAO.comicsImportados.removeIf(c -> c.getid().equals(idComic));
					getReferenciaVentana().getTablaBBDD().getItems().clear();
				} else {
					listaComics = null; // Inicializar listaComics en caso de que no haya ningún cómic seleccionado
				}

				comicsFinal = listaComics; // Declarar otra variable final para listaComics

				Platform.runLater(() -> FuncionesTableView.tablaBBDD(comicsFinal));
				getReferenciaVentana().getTablaBBDD().refresh();
			}

			AlarmaList.mostrarMensajePront(mensaje, esModificacion, getReferenciaVentana().getProntInfo());
			procesarBloqueComun(comic);
		} catch (IOException | SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

	}

	/**
	 * Procesa el bloque común utilizado en la función procesarComic para actualizar
	 * la interfaz gráfica y realizar operaciones relacionadas con la manipulación
	 * de imágenes y la actualización de listas y combos.
	 *
	 * @param comic El objeto Comic que se está procesando.
	 * @throws SQLException Si ocurre un error al interactuar con la base de datos.
	 */
	private void procesarBloqueComun(Comic comic) {
		File file = new File(comic.getImagen());
		Image imagen = new Image(file.toURI().toString(), 250, 0, true, true);
		getReferenciaVentana().getImagencomic().setImage(imagen);
		getReferenciaVentana().getImagencomic().setImage(imagen);

		ListaComicsDAO.listasAutoCompletado();
		FuncionesTableView.nombreColumnas();
		FuncionesTableView.actualizarBusquedaRaw();
	}

	public static boolean procesarComicPorCodigo(String finalValorCodigo) {

		Comic comicInfo = obtenerComicInfo(finalValorCodigo);

		if (comprobarCodigo(comicInfo)) {
			rellenarTablaImport(comicInfo);
			return true;
		} else {
			return false;
		}
	}

	public static void actualizarComicsDatabase(Comic comicOriginal, String tipoUpdate, boolean confirmarFirma) {

		// Verificar si la firma ya está establecida en comicOriginal
		if (!confirmarFirma && !comicOriginal.getFirma().isEmpty()) {
			System.out.println("Comic: " + comicOriginal.getid() + " evitado");
			return; // Si ya tiene firma, salir de la función
		}

		if (!comprobarCodigo(comicOriginal)) {
			return;
		}

		String codigoComic = comicOriginal.getcodigoComic();
		Comic comicInfo = obtenerComicInfo(codigoComic);

		if (comicInfo == null) {
			return;
		}

		String codigo_imagen = Utilidades.generarCodigoUnico(carpetaPortadas(Utilidades.nombreDB()) + File.separator);
		String urlImagen = comicInfo.getImagen();
		String urlFinal = carpetaPortadas(Utilidades.nombreDB()) + File.separator + codigo_imagen + ".jpg";
		String correctedUrl = urlImagen.replace("\\", "/").replaceFirst("^http:", "https:");
		comicOriginal.setID(comicOriginal.getid());
		if (tipoUpdate.equalsIgnoreCase("modificar") || tipoUpdate.equalsIgnoreCase("actualizar datos")) {

			String numComic = Utilidades.extraerNumeroLimpio(comicOriginal.getNombre());
			String nombreCorregido = Utilidades.eliminarParentesis(comicOriginal.getNombre());
			String nombreLimpio = Utilidades.extraerNombreLimpio(nombreCorregido);
			nombreLimpio = DatabaseManagerDAO.corregirPatrones(nombreLimpio);
			String formatoLimpio = Utilidades.devolverPalabrasClave(comicOriginal.getNombre());
			String editorial = DatabaseManagerDAO.getEditorial(comicOriginal.getEditorial());

			comicOriginal.setNombre(nombreLimpio);
			comicOriginal.setNumero(numComic);
			comicOriginal.setFormato(formatoLimpio);
			comicOriginal.setEditorial(editorial);

			completarInformacionFaltante(comicOriginal, comicOriginal);
			if (tipoUpdate.equalsIgnoreCase("modificar")) {
				Utilidades.deleteFile(comicOriginal.getImagen());
				comicOriginal.setImagen(urlFinal);
			} else {
				comicOriginal.setImagen(comicOriginal.getImagen());
			}

		}

		if (tipoUpdate.equalsIgnoreCase("modificar") || tipoUpdate.equalsIgnoreCase("actualizar portadas")) {
			comicOriginal.setImagen(urlFinal);
			// Asynchronously download and convert image
			Platform.runLater(() -> {
				URI uri = null;
				try {
					uri = new URI(correctedUrl);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				Utilidades.descargarYConvertirImagenAsync(uri, carpetaPortadas(Utilidades.nombreDB()),
						codigo_imagen + ".jpg");
			});
		}

		if (tipoUpdate.equalsIgnoreCase("modificar") || tipoUpdate.equalsIgnoreCase("actualizar datos")
				|| tipoUpdate.equalsIgnoreCase("actualizar portadas")) {
			UpdateManager.actualizarComicBBDD(comicOriginal, "modificar");
		}
	}

	private static void completarInformacionFaltante(Comic comicInfo, Comic comicOriginal) {
		// Completar información faltante con la información original si está vacía
		if (comicInfo.getVariante() == null || comicInfo.getVariante().isEmpty()) {
			comicInfo.setVariante(comicOriginal.getVariante());
		}
		if (comicInfo.getGuionista() == null || comicInfo.getGuionista().isEmpty()) {
			comicInfo.setGuionista(comicOriginal.getGuionista());
		}
		if (comicInfo.getDibujante() == null || comicInfo.getDibujante().isEmpty()) {
			comicInfo.setDibujante(comicOriginal.getDibujante());
		}
		if (comicInfo.getprecioComic() == null || comicInfo.getprecioComic().isEmpty()) {
			comicInfo.setprecioComic(comicOriginal.getprecioComic());
		}
		comicInfo.setFormato(comicOriginal.getFormato());

		if (!comicInfo.getFormato().equalsIgnoreCase("Grapa (Issue individual)")) {
			comicInfo.setDibujante(comicOriginal.getDibujante());
			comicInfo.setGuionista(comicOriginal.getGuionista());
		}

		comicInfo.setImagen(comicOriginal.getImagen());

	}

	/**
	 * Funcion que escribe en el TextField de "Direccion de imagen" la dirrecion de
	 * la imagen
	 */
	public static void subirPortada() {

		String frase = "Fichero JPG";

		String formato = "*.jpg";

		File fichero = Utilidades.tratarFichero(frase, formato, false);

		// Verificar si se obtuvo un objeto FileChooser válido
		if (fichero != null) {
			String nuevoNombreArchivo = Utilidades.generarCodigoUnico(carpetaRaizPortadas(Utilidades.nombreDB()));

			try {
				Utilidades.redimensionarYGuardarImagen(fichero.getAbsolutePath(), nuevoNombreArchivo);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String portada = carpetaRaizPortadas(Utilidades.nombreDB()) + "portadas" + File.separator
					+ nuevoNombreArchivo + ".jpg";

			getReferenciaVentana().getDireccionImagen().setText(portada);

			String mensaje = "Portada subida correctamente.";

			AlarmaList.mostrarMensajePront(mensaje, true, getReferenciaVentana().getProntInfo());

			Utilidades.cargarImagenAsync(portada, getReferenciaVentana().getImagencomic());

		} else {
			String mensaje = "Has cancelado la subida de portada.";

			AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfo());
		}
	}

	/**
	 * Limpia y restablece todos los campos de datos en la sección de animaciones a
	 * sus valores predeterminados. Además, restablece la imagen de fondo y oculta
	 * cualquier mensaje de error o información.
	 */
	public static void limpiarDatosPantallaAccion() {
		// Restablecer los campos de datos

		if (ListaComicsDAO.comicsImportados.size() > 0) {
			if (nav.alertaBorradoLista()) {
				getReferenciaVentana().getBotonGuardarComic().setVisible(false);
				getReferenciaVentana().getBotonEliminarImportadoComic().setVisible(false);

				ListaComicsDAO.comicsImportados.clear();
				getReferenciaVentana().getTablaBBDD().getItems().clear();
			}
		}

		getReferenciaVentana().getNombreComic().setText("");
		getReferenciaVentana().getVarianteComic().setText("");
		getReferenciaVentana().getFirmaComic().setText("");
		getReferenciaVentana().getEditorialComic().setText("");
		getReferenciaVentana().getFechaComic().setValue(null);
		getReferenciaVentana().getGuionistaComic().setText("");
		getReferenciaVentana().getDibujanteComic().setText("");
		getReferenciaVentana().getProntInfo().setText(null);
		getReferenciaVentana().getProntInfo().setOpacity(0);
		getReferenciaVentana().getNombreKeyIssue().setText("");
		getReferenciaVentana().getNumeroComic().getEditor().clear(); // Limpiar el texto en el ComboBox
		getReferenciaVentana().getFormatoComic().getEditor().clear(); // Limpiar el texto en el ComboBox
		getReferenciaVentana().getProcedenciaComic().getEditor().clear(); // Limpiar el texto en el ComboBox
		getReferenciaVentana().getEstadoComic().getEditor().clear(); // Limpiar el texto en el ComboBox
		getReferenciaVentana().getUrlReferencia().setText("");
		getReferenciaVentana().getPrecioComic().setText("");
		getReferenciaVentana().getDireccionImagen().setText("");
		getReferenciaVentana().getImagencomic().setImage(null);
		getReferenciaVentana().getGradeoComic().getEditor().clear();
		getReferenciaVentana().getCodigoComicTratar().setText("");
		getReferenciaVentana().getIdComicTratar().setText("");
		if ("modificar".equals(TIPO_ACCION)) {
			AccionControlUI.mostrarOpcion(TIPO_ACCION);
		}
		// Borrar cualquier mensaje de error presente
		borrarErrores();
		AccionControlUI.validarCamposClave(true);
		AccionControlUI.borrarDatosGraficos();
	}

	/**
	 * Comprueba la existencia de un cómic en la base de datos y realiza acciones
	 * dependiendo del resultado.
	 *
	 * @param ID El identificador del cómic a verificar.
	 * @return true si el cómic existe en la base de datos y se realizan las
	 *         acciones correspondientes, false de lo contrario.
	 * @throws SQLException Si ocurre un error al interactuar con la base de datos.
	 */
	public boolean comprobarExistenciaComic(String idComic) {

		// Si el cómic existe en la base de datos
		if (ComicManagerDAO.comprobarIdentificadorComic(idComic)) {
			FuncionesTableView.actualizarBusquedaRaw();
			return true;
		} else { // Si el cómic no existe en la base de datos
			String mensaje = "ERROR. ID desconocido.";
			AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfo());
			return false;
		}
	}

	/**
	 * Verifica si se ha encontrado información válida para el cómic.
	 *
	 * @param comicInfo Un objeto Comic con la información del cómic.
	 * @return True si la información es válida y existe; de lo contrario, False.
	 */
	private static boolean comprobarCodigo(Comic comicInfo) {
		return comicInfo != null;
	}

	/**
	 * Rellena los campos de la interfaz gráfica con la información del cómic
	 * proporcionada.
	 *
	 * @param comicInfo Un arreglo de strings con información del cómic.
	 * @throws IOException
	 */
	private static void rellenarTablaImport(Comic comic) {
		Platform.runLater(() -> {

			String numComic = Utilidades.extraerNumeroLimpio(comic.getNombre());
			String nombreCorregido = Utilidades.eliminarParentesis(comic.getNombre());

			System.out.println(comic.toString());

			String nombreLimpio = Utilidades.extraerNombreLimpio(nombreCorregido);

			// Variables relacionadas con la importación de cómics
			String id = "A" + 0 + "" + (ListaComicsDAO.comicsImportados.size() + 1);
			String titulo = Utilidades.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirPatrones(nombreLimpio), "Vacio");
			String keyIssue = Utilidades.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirNombre(comic.getkeyIssue()),
					"Vacio");
			String numero = Utilidades.defaultIfNullOrEmpty(numComic, "0");
			String variante = Utilidades.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirNombre(comic.getVariante()),
					"Vacio");
			String precio = Utilidades.defaultIfNullOrEmpty(comic.getprecioComic(), "0");
			String dibujantes = Utilidades.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirNombre(comic.getDibujante()),
					"Vacio");
			String escritores = Utilidades.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirNombre(comic.getGuionista()),
					"Vacio");
			String fechaVenta = comic.getFecha();
			LocalDate fecha = Utilidades.parseFecha(fechaVenta);

			// Variables relacionadas con la imagen del cómic
			String urlReferencia = Utilidades.defaultIfNullOrEmpty(comic.getUrlReferencia(), "Vacio");
			String urlImagen = comic.getImagen();
			String editorial = Utilidades
					.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirPatrones(comic.getEditorial()), "Vacio");
			File file = new File(urlImagen);

			// Manejo de la ruta de la imagen
			if (comic.getImagen() == null || comic.getImagen().isEmpty()) {
				String rutaImagen = "/imagenes/sinPortada.jpg";
				URL url = Utilidades.class.getClass().getResource(rutaImagen);
				if (url != null) {
					urlImagen = url.toExternalForm();
				}
			} else {
				file = new File(urlImagen);
				urlImagen = file.toString();
			}

			String formatoLimpio = Utilidades.devolverPalabrasClave(comic.getNombre());
			String formato = Utilidades.defaultIfNullOrEmpty(formatoLimpio, "Grapa (Issue individual)");
			String procedencia = Utilidades.defaultIfNullOrEmpty(comic.getProcedencia(),
					"Estados Unidos (United States)");

			// Corrección y generación de la URL final de la imagen
			String correctedUrl = urlImagen.replace("\\", "/").replace("http:", "https:").replace("https:", "https:/");
			String codigoImagen = Utilidades
					.generarCodigoUnico(carpetaPortadas(Utilidades.nombreDB()) + File.separator);
			URI uri = null;
			try {
				uri = new URI(correctedUrl);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			String imagen = carpetaPortadas(Utilidades.nombreDB()) + File.separator + codigoImagen + ".jpg";
			String codigoComic = Utilidades.defaultIfNullOrEmpty(comic.getcodigoComic(), "0");
			// Descarga y conversión asíncrona de la imagen
			Utilidades.descargarYConvertirImagenAsync(uri, carpetaPortadas(Utilidades.nombreDB()),
					codigoImagen + ".jpg");

			Comic comicImport = new Comic.ComicBuilder(id, titulo).valorGradeo("NM (Noir Medium)").numero(numero)
					.variante(variante).firma("").editorial(editorial).formato(formato).procedencia(procedencia)
					.fecha(fecha.toString()).guionista(escritores).dibujante(dibujantes).estado("Comprado")
					.keyIssue(keyIssue).puntuacion("Sin puntuar").imagen(imagen).referenciaComic(urlReferencia)
					.precioComic(precio).codigoComic(codigoComic).build();

			ListaComicsDAO.comicsImportados.add(comicImport);
//
			FuncionesTableView.nombreColumnas();
			FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados);
		});
	}

	private static Comic obtenerComicInfo(String finalValorCodigo) {
		try {

			// Obtener información del cómic según la longitud del código
			if (finalValorCodigo.matches("[A-Z]{3}\\d{6}")) {

				return WebScraperPreviewsWorld.displayComicInfo(finalValorCodigo.trim(),
						getReferenciaVentana().getProntInfo());
			} else {
				// Si no, intentar obtener la información del cómic de diferentes fuentes
				Comic comicInfo = ApiMarvel.infoComicCode(finalValorCodigo.trim(),
						getReferenciaVentana().getProntInfo());
				if (comicInfo == null) {
					comicInfo = WebScrapGoogleLeagueOfComics.obtenerDatosDiv(finalValorCodigo.trim());
				}
				if (comicInfo == null) {
					ApiISBNGeneral isbnGeneral = new ApiISBNGeneral();
					comicInfo = isbnGeneral.getBookInfo(finalValorCodigo.trim(), getReferenciaVentana().getProntInfo());
				}

				if (comicInfo == null) {
					return null;
				}

				Comic.limpiarCamposComic(comicInfo);
				return comicInfo;
			}
		} catch (URISyntaxException e) {
			// Manejar excepciones
			System.err.println("Error al obtener información del cómic: " + e.getMessage());
			return null;
		}
	}

	private static void actualizarInterfaz(AtomicInteger contadorErrores, String carpetaDatabase,
			AtomicInteger contadorTotal) {
		Platform.runLater(() -> {
			String mensaje = "";

			if (!carpetaDatabase.isEmpty()) {
				mensaje = "Se han procesado: " + (contadorTotal.get() - contadorErrores.get()) + " de "
						+ contadorTotal.get();
			} else {
				mensaje = "Se han procesado: " + (contadorErrores.get()) + " de " + contadorTotal.get();

			}

			AlarmaList.mostrarMensajePront(mensaje, true, getReferenciaVentana().getProntInfo());
		});
	}

	// ES OPCIONES
	public static void busquedaPorListaDatabase(List<Comic> listaComicsDatabase, String tipoUpdate,
			boolean actualizarFirma) {

		codigoFaltante = new StringBuilder();
		codigoFaltante.setLength(0);
		contadorErrores = new AtomicInteger(0);
		comicsProcesados = new AtomicInteger(0);
		mensajeIdCounter = new AtomicInteger(0); // Contador para generar IDs únicos
		numLineas = new AtomicInteger(listaComicsDatabase.size()); // Obtener el tamaño de la lista
		cargaComicsControllerRef = new AtomicReference<>();
		mensajesUnicos = new HashSet<>();
		mensajesUnicos.clear();

		Task<Void> tarea = createSearchTask(tipoUpdate, actualizarFirma, listaComicsDatabase);

		handleTaskEvents(tarea, tipoUpdate);

		Thread thread = new Thread(tarea);
		thread.setDaemon(true);
		thread.start();
	}

	// ES ACCION
	public static void busquedaPorCodigoImportacion(File file) {

		fichero = file;
		contadorErrores = new AtomicInteger(0);
		comicsProcesados = new AtomicInteger(0);
		mensajeIdCounter = new AtomicInteger(0);
		numLineas = new AtomicInteger(0);
		numLineas.set(Utilidades.contarLineasFichero(fichero));
		cargaComicsControllerRef = new AtomicReference<>();
		codigoFaltante = new StringBuilder();
		codigoFaltante.setLength(0);
		mensajesUnicos = new HashSet<>();
		mensajesUnicos.clear();

		Task<Void> tarea = createSearchTask("", false, null);

		handleTaskEvents(tarea, "");

		Thread thread = new Thread(tarea);
		thread.setDaemon(true);
		thread.start();
	}

	private static Task<Void> createSearchTask(String tipoUpdate, boolean actualizarFirma,
			List<Comic> listaComicsDatabase) {
		return new Task<>() {
			@Override
			protected Void call() {
				nav.verCargaComics(cargaComicsControllerRef);

				if (tipoUpdate.isEmpty()) {
					try (BufferedReader reader = new BufferedReader(new FileReader(fichero))) {
						reader.lines().forEach(linea -> {
							if (isCancelled() || !getReferenciaVentana().getStage().isShowing()) {
								return;
							}
							String finalValorCodigo = Utilidades.eliminarEspacios(linea).replace("-", "");
							Comic comicInfo = obtenerComicInfo(finalValorCodigo);
							processComic(comicInfo, "", false);
						});
					} catch (IOException e) {
						Utilidades.manejarExcepcion(e);
					}
				} else {
					listaComicsDatabase.forEach(codigo -> {
						String finalValorCodigo = Utilidades.eliminarEspacios(codigo.getcodigoComic()).replace("-", "");
						codigo.setcodigoComic(finalValorCodigo);
						processComic(codigo, tipoUpdate, actualizarFirma);

					});
				}
				return null;
			}
		};
	}

	private static void processComic(Comic comic, String tipoUpdate, boolean actualizarFirma) {
		StringBuilder textoBuilder = new StringBuilder();

		if (comic.getcodigoComic().isEmpty()) {

			if (tipoUpdate.isEmpty()) {
				codigoFaltante.append("Falta cómic con código: ").append(comic.getcodigoComic()).append("\n");
				textoBuilder.append("Cómic no capturado: ").append(comic.getcodigoComic()).append("\n");
			} else {
				codigoFaltante.append("ID no procesado: ").append(comic.getid()).append("\n");
				textoBuilder.append("ID no procesado: ").append(comic.getid()).append("\n");
			}

			contadorErrores.getAndIncrement();

		} else {

			if (tipoUpdate.isEmpty()) {
				textoBuilder.append("Código: ").append(comic.getcodigoComic()).append(" procesado.").append("\n");
				AccionFuncionesComunes.procesarComicPorCodigo(comic.getcodigoComic());
			} else {
				textoBuilder.append("ID: ").append(comic.getid()).append(" actualizado.").append("\n");
				AccionFuncionesComunes.actualizarComicsDatabase(comic, tipoUpdate, actualizarFirma);
			}
		}
		updateGUI(textoBuilder);
	}

	private static void updateGUI(StringBuilder textoBuilder) {
		String mensajeId = String.valueOf(mensajeIdCounter.getAndIncrement());

		String mensaje = mensajeId + ": " + textoBuilder.toString();
		mensajesUnicos.add(mensaje);
		comicsProcesados.getAndIncrement();

		long finalProcessedItems = comicsProcesados.get();
		double progress = (double) finalProcessedItems / (numLineas.get());
		String porcentaje = String.format("%.2f%%", progress * 100);

		if (nav.isVentanaCerrada()) {

			nav.verCargaComics(cargaComicsControllerRef);

			StringBuilder textoFiltrado = new StringBuilder();
			List<String> mensajesOrdenados = new ArrayList<>(mensajesUnicos);

			Collections.sort(mensajesOrdenados, Comparator.comparingInt(m -> Integer.parseInt(m.split(":")[0])));
			for (String mensajeUnico : mensajesOrdenados) {
				if (!mensajeUnico.equalsIgnoreCase(mensaje)) {
					textoFiltrado.append(mensajeUnico.substring(mensajeUnico.indexOf(":") + 2));
				}
			}

			Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics(textoFiltrado.toString(),
					porcentaje, progress));
		}
		Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics(textoBuilder.toString(),
				porcentaje, progress));

	}

	private static void handleTaskEvents(Task<Void> tarea, String tipoUpdate) {

		tarea.setOnRunning(ev -> {
			if (tipoUpdate.isEmpty()) {
				AccionControlUI.limpiarAutorellenos(false);

				cambiarEstadoBotones(true);
				getReferenciaVentana().getImagencomic().setImage(null);
				getReferenciaVentana().getImagencomic().setVisible(true);

				AlarmaList.iniciarAnimacionCargaImagen(getReferenciaVentana().getCargaImagen());
				FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentana);
				FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentanaPrincipal);

				getReferenciaVentana().getMenu_Importar_Fichero_CodigoBarras().setDisable(true);
				getReferenciaVentana().getBotonSubidaPortada().setDisable(true);

				AlarmaList.mostrarMensajePront("Se estan cargando los datos", true,
						getReferenciaVentana().getProntInfo());
				AlarmaList.iniciarAnimacionCarga(getReferenciaVentana().getProgresoCarga());
			} else {
				String cadenaAfirmativo = getAffirmativeMessage(tipoUpdate);
				List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();

				for (Stage stage : stageVentanas) {
					if (stage.getTitle().equalsIgnoreCase("Menu principal")
							|| stage.getTitle().equalsIgnoreCase("Opciones avanzadas")) {
						stage.setOnCloseRequest(closeEvent -> {

							tarea.cancel(true);
							Utilidades.cerrarCargaComics();
							FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
							FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
							FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, referenciaVentana);
							nav.cerrarMenuOpciones();
							nav.cerrarVentanaAccion();

							Utilidades.cerrarOpcionesAvanzadas();
						});
					}
				}

				getReferenciaVentanaPrincipal().getProntInfo().setDisable(false);
				getReferenciaVentanaPrincipal().getProntInfo().setOpacity(1);

				AlarmaList.mostrarMensajePront("Se estan cargando los datos", true,
						getReferenciaVentanaPrincipal().getProntInfo());

				AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(), cadenaAfirmativo);
				getReferenciaVentana().getBotonCancelarSubida().setVisible(true);
				actualizarCombobox();
				FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentana);
				FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentanaPrincipal);
				FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(true, getReferenciaVentana());
			}
		});

		tarea.setOnSucceeded(ev -> {
			if (tipoUpdate.isEmpty()) {
				AlarmaList.detenerAnimacionCargaImagen(getReferenciaVentana().getCargaImagen());
				cambiarEstadoBotones(false);

				actualizarInterfaz(contadorErrores, carpetaRaizPortadas(Utilidades.nombreDB()), numLineas);

				getReferenciaVentana().getMenu_Importar_Fichero_CodigoBarras().setDisable(false);

				Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics("", "100%", 100.0));
				AlarmaList.detenerAnimacionCarga(getReferenciaVentana().getProgresoCarga());
			} else {

				AlarmaList.mostrarMensajePront("Datos cargados correctamente", true,
						getReferenciaVentanaPrincipal().getProntInfo());

				Platform.runLater(() -> getReferenciaVentana().getBotonCancelarSubida().setVisible(false));
				String cadenaAfirmativo = getUpdateTypeString(tipoUpdate);
				AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(), cadenaAfirmativo);
				actualizarCombobox();

				FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, getReferenciaVentana());
			}

			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
		});

		tarea.setOnCancelled(ev -> {

			if (tipoUpdate.isEmpty()) {
				Thread.currentThread().interrupt();
				cambiarEstadoBotones(false);

				Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics("", "100%", 100.0));

				AlarmaList.mostrarMensajePront("Se ha cancelado la importacion", false,
						getReferenciaVentana().getProntInfo());

				AlarmaList.detenerAnimacionCarga(getReferenciaVentana().getProgresoCarga());
				AlarmaList.detenerAnimacionCargaImagen(getReferenciaVentana().getCargaImagen());
			} else {
				String cadenaAfirmativo = "Cancelada la actualización de la base de datos.";
				AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(), cadenaAfirmativo);
				actualizarCombobox();
				FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, getReferenciaVentana());
				Platform.runLater(() -> getReferenciaVentana().getBotonCancelarSubida().setVisible(false));
			}
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
		});

		// Configurar el evento para cancelar la tarea cuando se presiona el botón de
		// cancelar subida
		getReferenciaVentana().getBotonCancelarSubida().setOnAction(ev -> {
			if (tipoUpdate.isEmpty()) {
				actualizarInterfaz(comicsProcesados, "", numLineas);
				cambiarEstadoBotones(false);
				getReferenciaVentana().getMenu_Importar_Fichero_CodigoBarras().setDisable(false);
			} else {

				referenciaVentanaPrincipal.getProntInfo().clear();
				referenciaVentanaPrincipal.getProntInfo().setText(null);
				referenciaVentanaPrincipal.getProntInfo().setOpacity(0);
				Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics("", "100%", 100.0));
				String cadenaAfirmativo = "Cancelada la actualización de la base de datos.";
				AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(), cadenaAfirmativo);
				actualizarCombobox();
				FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
				FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
				FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, getReferenciaVentana());
				Platform.runLater(() -> getReferenciaVentana().getBotonCancelarSubida().setVisible(false));
				getReferenciaVentana().getBotonCancelarSubida().setVisible(false);

			}
			tarea.cancel(true);
		});
	}

	private static String getUpdateTypeString(String tipoUpdate) {
		if (tipoUpdate.equalsIgnoreCase("actualizar datos")) {
			return "Datos actualizados";
		} else if (tipoUpdate.equalsIgnoreCase("actualizar portadas")) {
			return "Portadas actualizadas";
		} else {
			return "Base de datos actualizada";
		}
	}

	private static String getAffirmativeMessage(String tipoUpdate) {
		if (tipoUpdate.equalsIgnoreCase("actualizar datos")) {
			return "Actualizando datos";
		} else if (tipoUpdate.equalsIgnoreCase("actualizar portadas")) {
			return "Actualizando portadas";
		} else {
			return "Actualizando base de datos";
		}
	}

	public static void actualizarCombobox() {
		ListaComicsDAO.reiniciarListaComics();
		ListaComicsDAO.listasAutoCompletado();

		List<ComboBox<String>> comboboxes = getReferenciaVentana().getComboboxes();

		if (comboboxes != null) {
			funcionesCombo.rellenarComboBox(comboboxes);
		}
	}

	/**
	 * Elimina cualquier resaltado de campos en rojo que indique errores.
	 */
	public static void borrarErrores() {

		getReferenciaVentana().getNombreComic().setStyle("");
		getReferenciaVentana().getNumeroComic().setStyle("");
		getReferenciaVentana().getEditorialComic().setStyle("");
		getReferenciaVentana().getGuionistaComic().setStyle("");
		getReferenciaVentana().getDibujanteComic().setStyle("");
	}

	/**
	 * Modifica la visibilidad y el estado de los elementos de búsqueda en la
	 * interfaz de usuario.
	 *
	 * @param mostrar True para mostrar los elementos de búsqueda, False para
	 *                ocultarlos.
	 */
	public static void cambiarVisibilidadAvanzada() {

		List<Node> elementos = Arrays.asList(getReferenciaVentana().getBotonBusquedaCodigo(),
				getReferenciaVentana().getBusquedaCodigo());

		if (getReferenciaVentana().getBotonBusquedaCodigo().isVisible()) {
			Utilidades.cambiarVisibilidad(elementos, true);
		} else {
			Utilidades.cambiarVisibilidad(elementos, false);
		}
	}

	public static void cambiarEstadoBotones(boolean esCancelado) {

		List<Node> elementos = Arrays.asList(getReferenciaVentana().getBotonEliminarImportadoComic(),
				getReferenciaVentana().getBotonSubidaPortada(), getReferenciaVentana().getBotonGuardarComic());

		if (!TIPO_ACCION.equals("aniadir")) {
			elementos.add(getReferenciaVentana().getBotonBusquedaCodigo());
			elementos.add(getReferenciaVentana().getBusquedaCodigo());
			elementos.add(getReferenciaVentana().getBotonCancelarSubida());
			elementos.add(getReferenciaVentana().getBotonBusquedaCodigo());
			elementos.add(getReferenciaVentana().getBotonSubidaPortada());
		}

		getReferenciaVentana().getBotonCancelarSubida().setVisible(esCancelado);
		getReferenciaVentana().getBotonLimpiar().setDisable(esCancelado);
		getReferenciaVentana().getBotonBusquedaAvanzada().setDisable(esCancelado);
		getReferenciaVentana().getBotonGuardarCambioComic().setDisable(esCancelado);

		Utilidades.cambiarVisibilidad(elementos, esCancelado);
	}

	public static String carpetaRaizPortadas(String nombreDatabase) {
		return DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator + nombreDatabase + File.separator;
	}

	public static String carpetaPortadas(String nombreDatabase) {
		return DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator + nombreDatabase + File.separator
				+ "portadas";
	}

	public static void setTipoAccion(String tipoAccion) {
		TIPO_ACCION = tipoAccion;
	}

	public static String getTipoAccion() {
		return TIPO_ACCION;
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static AccionReferencias getReferenciaVentanaPrincipal() {
		return referenciaVentanaPrincipal;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionFuncionesComunes.referenciaVentana = referenciaVentana;
	}

	public static void setReferenciaVentanaPrincipal(AccionReferencias referenciaVentana) {
		AccionFuncionesComunes.referenciaVentanaPrincipal = referenciaVentana;
	}

}
