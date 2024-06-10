package Controladores;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;

import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.DatabaseManagerDAO;
import dbmanager.ListaComicsDAO;
import ficherosFunciones.FuncionesExcel;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesAuxiliares.VersionService;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesManagment.AccionFuncionesComunes;
import funcionesManagment.AccionModificar;
import funcionesManagment.AccionReferencias;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.coobird.thumbnailator.Thumbnails;

public class OpcionesAvanzadasController implements Initializable {

	@FXML
	private Button botonActualizarDatos;

	@FXML
	private Button botonActualizarPortadas;

	@FXML
	private Button botonActualizarSoftware;

	@FXML
	private Button botonActualizarTodo;

	@FXML
	private Button botonCancelarSubida;

	@FXML
	private Button botonCancelarSubidaPortadas;

	@FXML
	private Button botonComprimirPortadas;

	@FXML
	private Button botonDescargarPdf;

	@FXML
	private Button botonDescargarSQL;

	@FXML
	private Button botonNormalizarDB;

	@FXML
	private Button botonReCopiarPortadas;

	@FXML
	private Button botonRecomponerPortadas;

	@FXML
	private CheckBox checkFirmas;

	@FXML
	private ComboBox<String> comboPreviews;

	@FXML
	private Label labelComprobar;

	@FXML
	private Label labelVersion;

	@FXML
	private Label labelVersionEspecial;

	@FXML
	private Label labelVersionPortadas;

	@FXML
	private Label labelVersionPreviews;

	@FXML
	private Label prontInfo;

	@FXML
	private Label prontInfoEspecial;

	@FXML
	private Label prontInfoPortadas;

	@FXML
	private Label prontInfoPreviews;

	public static final ObservableList<String> urlActualizados = FXCollections.observableArrayList();

	public static final AccionReferencias referenciaVentana = new AccionReferencias();

	private static AccionReferencias referenciaVentanaPrincipal = getReferenciaVentanaPrincipal();

	/**
	 * Instancia de la clase FuncionesComboBox para el manejo de ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	/**
	 * Referencia a la ventana (stage).
	 */
	private Scene ventanaOpciones;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	public static AtomicBoolean actualizarFima = new AtomicBoolean(false);

	private static AtomicInteger comicsProcesados;
	private static AtomicInteger mensajeIdCounter;
	private static AtomicInteger numLineas;
	private static AtomicReference<CargaComicsController> cargaComicsControllerRef;
	private static StringBuilder codigoFaltante;
	private static HashSet<String> mensajesUnicos = new HashSet<>();

	public AccionReferencias guardarReferencia() {

		referenciaVentana.setBotonCancelarSubida(botonCancelarSubida);
		referenciaVentana.setBotonActualizarDatos(botonActualizarDatos);
		referenciaVentana.setBotonActualizarPortadas(botonActualizarPortadas);
		referenciaVentana.setBotonActualizarSoftware(botonActualizarSoftware);
		referenciaVentana.setBotonActualizarTodo(botonActualizarTodo);
		referenciaVentana.setBotonDescargarPdf(botonDescargarPdf);
		referenciaVentana.setBotonDescargarSQL(botonDescargarSQL);
		referenciaVentana.setBotonNormalizarDB(botonNormalizarDB);
		referenciaVentana.setCheckFirmas(checkFirmas);
		referenciaVentana.setComboPreviews(comboPreviews);
		referenciaVentana.setLabelComprobar(labelComprobar);
		referenciaVentana.setLabelVersion(labelVersion);
		referenciaVentana.setProntInfoLabel(prontInfo);
		referenciaVentana.setProntInfoEspecial(prontInfoEspecial);
		referenciaVentana.setProntInfoPreviews(prontInfoPreviews);
		referenciaVentana.setProntInfoPortadas(prontInfoPortadas);
		referenciaVentana.setStage(estadoStage());

		referenciaVentana.setBotonComprimirPortadas(botonComprimirPortadas);
		referenciaVentana.setBotonReCopiarPortadas(botonReCopiarPortadas);

		return referenciaVentana;
	}

	public void enviarReferencias() {
		AccionFuncionesComunes.setReferenciaVentana(guardarReferencia());
		AccionFuncionesComunes.setReferenciaVentanaPrincipal(getReferenciaVentanaPrincipal());
		FuncionesManejoFront.setReferenciaVentana(guardarReferencia());
		AccionModificar.setReferenciaVentana(guardarReferencia());

	}

	/**
	 * Inicializa la interfaz de usuario y configura el comportamiento de los
	 * elementos al cargar la vista.
	 *
	 * @param location  La ubicación relativa del archivo FXML.
	 * @param resources Los recursos que pueden ser utilizados por el controlador.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(() -> {
			rellenarComboboxPreviews();
			enviarReferencias();

			obtenerVersionDesdeOtraClase();

			FuncionesManejoFront.getStageVentanas().add(estadoStage());

		});
		ventanaOpciones = miStageVentana();

		AlarmaList.iniciarAnimacionEspera(prontInfo);
		AlarmaList.iniciarAnimacionEspera(prontInfoEspecial);
		AlarmaList.iniciarAnimacionEspera(prontInfoPreviews);
		AlarmaList.iniciarAnimacionEspera(prontInfoPortadas);

		checkFirmas.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (Boolean.TRUE.equals(newValue)) {
				if (nav.alertaFirmaActivada()) {
					actualizarFima.set(true);
				} else {
					// Si la alerta no está activada, desmarcar el CheckBox
					checkFirmas.setSelected(false);
				}
			} else {
				// Cuando el CheckBox se desmarca, actualiza actualizarFima a false
				actualizarFima.set(false);
			}
		});
	}

	@FXML
	void descargarActualizacion(ActionEvent event) {
		Utilidades.descargarYAbrirEjecutableDesdeGitHub();
	}

	@FXML
	void descargarSQL(ActionEvent event) {

		AlarmaList.detenerAnimacionEspera();
		DatabaseManagerDAO.makeSQL(prontInfo, estadoStage());
		Utilidades.borrarArchivosNoEnLista(ListaComicsDAO.listaImagenes);

	}

	@FXML
	void comprobarVersion(ActionEvent event) {

		String versionSW = VersionService.obtenerVersion();
		String versionLocal = VersionService.leerVersionDelArchivo();
		boolean estaActualizado = true;
		String cadena = "";
		if (Utilidades.compareVersions(versionSW, versionLocal) > 0) {

			labelComprobar.setStyle("-fx-text-fill: red;");
			labelVersionPreviews.setStyle("-fx-text-fill: red;");
			labelVersionPortadas.setStyle("-fx-text-fill: red;");
			labelVersionEspecial.setStyle("-fx-text-fill: red;");
			cadena = "Versión desactualizada";
			estaActualizado = false;
		} else {
			labelComprobar.setStyle("-fx-text-fill: white;");
			cadena = "Versión actualizada";
			estaActualizado = true;
		}

		AlarmaList.iniciarAnimacionAvanzado(prontInfo, cadena);

		if (!estaActualizado) {
			botonActualizarSoftware.setVisible(true);
		}
	}

	@FXML
	void normalizarDataBase(ActionEvent event) {

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				DatabaseManagerDAO.comprobarNormalizado("nomComic", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("nivel_gradeo", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("precio_comic", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("codigo_comic", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("numComic", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("firma", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("nomEditorial", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("formato", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("procedencia", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("puntuacion", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("key_issue", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("estado", prontInfo);

				DatabaseManagerDAO.comprobarNormalizado("nomGuionista", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("nomDibujante", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("nomVariante", prontInfo);
				DatabaseManagerDAO.comprobarNormalizado("", prontInfo);

				ListaComicsDAO.reiniciarListaComics();
				ListaComicsDAO.listasAutoCompletado();
				List<String> inputPaths = DBUtilidades.obtenerValoresColumna("portada");

				Utilidades.borrarArchivosNoEnLista(inputPaths);

				List<ComboBox<String>> comboboxes = referenciaVentana.getComboboxes();
				if (comboboxes != null) {
					Platform.runLater(() -> funcionesCombo.rellenarComboBox(comboboxes));
				}
				return null;
			}
		};

		Thread thread = new Thread(task);
		thread.setDaemon(true); // Hacer que el hilo sea demonio para que termine cuando la aplicación se cierre

		task.setOnRunning(e -> estadoStage().setOnCloseRequest(closeEvent -> task.cancel(true)));

		task.setOnSucceeded(e -> System.out.println("Terminado"));

		thread.start();
	}

	public void obtenerVersionDesdeOtraClase() {
		VersionService versionService = new VersionService();

		versionService.setOnSucceeded(event -> {
			String version = VersionService.leerVersionDelArchivo();
			labelVersion.setText(version);
			labelVersionPreviews.setText(version);
			labelVersionPortadas.setText(version);
			labelVersionEspecial.setText(version);
		});

		versionService.start();
	}

	@FXML
	void descargarPreview(ActionEvent evento) {
		if (Utilidades.isInternetAvailable()) {
			Platform.runLater(() -> {

				int indiceSeleccionado = comboPreviews.getSelectionModel().getSelectedIndex();
				String urlSeleccionada = OpcionesAvanzadasController.urlActualizados.get(indiceSeleccionado);
				if (urlSeleccionada.equals("No hay previews")) {
					String cadenaError = "No existe PDF que descargar";
					AlarmaList.iniciarAnimacionAvanzado(prontInfoPreviews, cadenaError);
					return;
				}

				String formato = "*.pdf";
				String frase = "Archivos PDF";

				// Mostrar el cuadro de diálogo de guardado y obtener la ubicación seleccionada
				File file = Utilidades.abrirFileChooser(frase, formato, true);

				if (file != null) {
					Utilidades.descargarPDFAsync(file, comboPreviews);
					String cadenaAfirmativo = "PDF descargado exitosamente.";
					AlarmaList.iniciarAnimacionAvanzado(prontInfoPreviews, cadenaAfirmativo);
					FuncionesManejoFront.manejarMensajeTextArea(cadenaAfirmativo);
				} else {
					String cadenaCancelado = "Has cancelado la descarga del PDF.";
					AlarmaList.iniciarAnimacionAvanzado(prontInfoPreviews, cadenaCancelado);
					FuncionesManejoFront.manejarMensajeTextArea(cadenaCancelado);
				}
			});
		} else {
			String cadenaCancelado = "No se puede descargar, no hay internet";
			AlarmaList.iniciarAnimacionAvanzado(prontInfoPreviews, cadenaCancelado);
			FuncionesManejoFront.manejarMensajeTextArea(cadenaCancelado);
		}
	}

	private void rellenarComboboxPreviews() {
		ObservableList<String> meses = FXCollections.observableArrayList();

		if (Utilidades.isInternetAvailable()) {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					List<Map.Entry<String, String>> previews = MenuPrincipalController.urlPreviews.get();
					ObservableList<String> mesesActualizados = FXCollections.observableArrayList();
					for (Map.Entry<String, String> entry : previews) {
						urlActualizados.add(entry.getValue()); // Agregar la URL a la lista global
						mesesActualizados.add(entry.getKey());
					}

					Platform.runLater(() -> {
						comboPreviews.setItems(mesesActualizados);
						comboPreviews.getSelectionModel().selectFirst();
					});

					return null;
				}
			};

			new Thread(task).start();
		} else {
			meses.add("No hay previews");
			comboPreviews.setItems(meses);
		}
	}

	@FXML
	void actualizarCompletoComic(ActionEvent event) {
		Utilidades.crearCarpeta();
		AccionModificar.setReferenciaVentana(guardarReferencia());
		AccionFuncionesComunes.setReferenciaVentana(guardarReferencia());
		AccionModificar.actualizarDatabase("modificar", actualizarFima.get(), estadoStage());

	}

	@FXML
	void actualizarDatosComic(ActionEvent event) {
		AccionModificar.setReferenciaVentana(guardarReferencia());
		AccionFuncionesComunes.setReferenciaVentana(guardarReferencia());
		AccionModificar.actualizarDatabase("actualizar datos", actualizarFima.get(), estadoStage());
	}

	@FXML
	void actualizarPortadaComic(ActionEvent event) {
		Utilidades.crearCarpeta();
		AccionModificar.setReferenciaVentana(guardarReferencia());
		AccionFuncionesComunes.setReferenciaVentana(guardarReferencia());
		AccionModificar.actualizarDatabase("actualizar portadas", actualizarFima.get(), estadoStage());

	}

	@FXML
	void comprimirPortadas(ActionEvent event) {
		List<String> inputPortadas = DBUtilidades.obtenerValoresColumna("portada");
		Utilidades.borrarArchivosNoEnLista(inputPortadas);
		List<String> inputPaths = ListaComicsDAO.listaImagenes;
		comicsProcesados = new AtomicInteger(0);
		mensajeIdCounter = new AtomicInteger(0);
		numLineas = new AtomicInteger(0);
		numLineas.set(inputPaths.size());
		cargaComicsControllerRef = new AtomicReference<>();
		codigoFaltante = new StringBuilder();
		codigoFaltante.setLength(0);
		mensajesUnicos = new HashSet<>();
		mensajesUnicos.clear();

		final String DOCUMENTS_PATH = Utilidades.DOCUMENTS_PATH;
		final String DB_NAME = Utilidades.nombreDB();
		final String directorioComun = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator + DB_NAME
				+ File.separator;
		final String directorioOriginal = directorioComun + "portadas" + File.separator;
		final String directorioNuevo = directorioComun + "portadas_originales";

		Task<Void> task = createCompressionTask(inputPaths, directorioOriginal, directorioNuevo);

		setupTaskEventHandlers(task);

		Thread thread = new Thread(task);

		thread.setDaemon(true);
		thread.start();
	}

	private static void processComic(String codigo) {
		StringBuilder textoBuilder = new StringBuilder();

		codigoFaltante.append("Comprimiendo: ").append(comicsProcesados.get()).append(" de ").append(numLineas)
				.append("\n");
		textoBuilder.append("Comprimiendo: ").append(comicsProcesados.get()).append(" de ").append(numLineas)
				.append("\n");

		updateUI(textoBuilder);
	}

	private Task<Void> createCompressionTask(List<String> inputPaths, String directorioOriginal,
			String directorioNuevo) {
		return new Task<>() {
			@Override
			protected Void call() throws Exception {
				Utilidades.copiarDirectorio(directorioNuevo, directorioOriginal);
				nav.verCargaComics(cargaComicsControllerRef);
				boolean estaBaseLlena = ListaComicsDAO.comprobarLista();

				if (!estaBaseLlena) {
					String cadenaCancelado = "La base de datos esta vacia";
					AlarmaList.iniciarAnimacionAvanzado(prontInfoPortadas, cadenaCancelado);
					cancel();
					return null;
				}
				inputPaths.forEach(codigo -> {

					if (isCancelled() || !estadoStage().isShowing()) {
						return;
					}

					processComic(codigo);
					comprimirImagenes(codigo);
				});

				return null;
			}
		};
	}

	private static void comprimirImagenes(String codigo) {

		try {
			File inputFile = new File(codigo);
			if (!inputFile.exists()) {
				return;
			}

			BufferedImage image = ImageIO.read(inputFile);
			if (image == null) {
				return;
			}

			Thread compressionThread = new Thread(() -> {
				try {
					Thumbnails.of(image).scale(1).outputQuality(0.5).toFile(codigo);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			compressionThread.start();
			try {
				compressionThread.join();
			} catch (InterruptedException e) {
				compressionThread.interrupt();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void updateUI(StringBuilder textoBuilder) {

		String mensajeId = String.valueOf(mensajeIdCounter.getAndIncrement());

		String mensaje = mensajeId + ": " + textoBuilder.toString();
		mensajesUnicos.add(mensaje);
		comicsProcesados.getAndIncrement();

		long finalProcessedItems = comicsProcesados.get();
		double progress = (double) finalProcessedItems / (numLineas.get());
		String porcentaje = String.format("%.2f%%", progress * 100);

		if (nav.isVentanaCerrada()) {
			// Código para actualizar la interfaz de usuario cuando la ventana está cerrada
			nav.verCargaComics(cargaComicsControllerRef);

			StringBuilder textoFiltrado = new StringBuilder();
			List<String> mensajesOrdenados = new ArrayList<>(mensajesUnicos);
			Collections.sort(mensajesOrdenados, Comparator.comparingInt(m -> Integer.parseInt(m.split(":")[0])));

			for (String mensajeUnico : mensajesOrdenados) {
				if (!mensajeUnico.equalsIgnoreCase(textoBuilder.toString())) {
					int colonIndex = mensajeUnico.indexOf(":");
					if (colonIndex != -1) {
						textoFiltrado.append(mensajeUnico.substring(colonIndex + 2));
					}
				}
			}

			Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics(textoFiltrado.toString(),
					porcentaje, progress));
		}

		Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics(textoBuilder.toString(),
				porcentaje, progress));
	}

	private void setupTaskEventHandlers(Task<Void> task) {
		task.setOnRunning(ev -> {
			estadoStage().setOnCloseRequest(closeEvent -> {
				task.cancel(true);
				Utilidades.cerrarCargaComics();
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
				FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, referenciaVentana);
			});

			String mensaje = "Comprimiendo portadas";
			AlarmaList.iniciarAnimacionAvanzado(prontInfoPortadas, mensaje);
			botonCancelarSubidaPortadas.setVisible(true);
			FuncionesManejoFront.cambiarEstadoMenuBar(true, guardarReferencia());
			FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentanaPrincipal);
			FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(true, referenciaVentana);
		});

		task.setOnSucceeded(ev -> {
			String mensaje = "Portadas comprimidas";
			AlarmaList.iniciarAnimacionAvanzado(prontInfoPortadas, mensaje);
			Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics("", "100%", 100.0));
			botonCancelarSubidaPortadas.setVisible(false);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
			FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, referenciaVentana);
		});

		task.setOnCancelled(ev -> {
			String mensaje = "Cancelada la actualizacion de la base de datos.";
			botonCancelarSubidaPortadas.setVisible(false);
			AlarmaList.iniciarAnimacionAvanzado(prontInfoPortadas, mensaje);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
			FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, referenciaVentana);

			Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics("", "100%", 100.0));

			task.cancel(true);
		});

		botonCancelarSubidaPortadas.setOnAction(ev -> {
			botonCancelarSubidaPortadas.setVisible(false);
			task.cancel(true);
		});

	}

	@FXML
	void reCopiarPortadas(ActionEvent event) {
		accionPortadas(true);
	}

	@FXML
	void recomponerPortadas(ActionEvent event) {
		accionPortadas(false);
	}

	public void accionPortadas(boolean esCopia) {

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Seleccionar carpeta");
		File selectedDirectory = directoryChooser.showDialog(null);

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					boolean estaBaseLlena = ListaComicsDAO.comprobarLista();
					if (!estaBaseLlena) {
						String cadenaCancelado = "La base de datos esta vacia";
						AlarmaList.iniciarAnimacionAvanzado(prontInfoPortadas, cadenaCancelado);
						cancel(); // Cancelar el Task si la base de datos está vacía
						return null; // Salir del método call() para finalizar el Task
					}

					List<String> listaID = ListaComicsDAO.listaID;
					// Mostrar el diálogo de selección de carpeta

					if (esCopia) {
						Utilidades.copyDirectory(FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH,
								selectedDirectory.getAbsolutePath());
					} else {
						Utilidades.copyDirectory(selectedDirectory.getAbsolutePath(),
								FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH);
					}

					for (String idComic : listaID) {
						Comic comicNuevo = ComicManagerDAO.comicDatos(idComic);

						String nombre_portada = Utilidades.obtenerNombrePortada(false, comicNuevo.getImagen());
						String nombre_modificado = Utilidades.convertirNombreArchivo(nombre_portada);
						if (!Utilidades.existeArchivo(FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH, nombre_portada)) {
							FuncionesExcel.copiarPortadaPredeterminada(selectedDirectory.getAbsolutePath(),
									nombre_modificado);
						}
					}

				} catch (Exception e) {
					e.printStackTrace(); // Maneja la excepción de acuerdo a tu lógica
				}
				return null;
			}
		};

		Thread thread = new Thread(task);

		task.setOnRunning(e -> {

			estadoStage().setOnCloseRequest(closeEvent -> {
				task.cancel(true);
				Utilidades.cerrarCargaComics();
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
			});

			FuncionesManejoFront.cambiarEstadoMenuBar(true, guardarReferencia());
			FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(true, referenciaVentana);

			String cadenaCancelado = "Copiando portadas";
			AlarmaList.iniciarAnimacionAvanzado(prontInfoPortadas, cadenaCancelado);
		});

		task.setOnSucceeded(e -> {
			FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
			FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, referenciaVentana);

			String cadenaCancelado = "Portadas copiadas";
			AlarmaList.iniciarAnimacionAvanzado(prontInfoPortadas, cadenaCancelado);
		});

		task.setOnFailed(e -> {
			FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
			FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, referenciaVentana);

			String cadenaCancelado = "ERROR. No se han podido copiar las portadas";
			AlarmaList.iniciarAnimacionAvanzado(prontInfoPortadas, cadenaCancelado);
		});

		thread.setDaemon(true);
		thread.start();

	}

	public Scene miStageVentana() {
		Node rootNode = botonActualizarSoftware;
		while (rootNode.getParent() != null) {
			rootNode = rootNode.getParent();
		}

		if (rootNode instanceof Parent) {
			Scene scene = ((Parent) rootNode).getScene();

			ConectManager.activeScenes.add(scene);

			return scene;
		} else {
			// Manejar el caso en el que no se pueda encontrar un nodo raíz adecuado
			return null;
		}
	}

	public Stage estadoStage() {

		return (Stage) botonActualizarDatos.getScene().getWindow();
	}

	/**
	 * Cierra la ventana asociada a este controlador, si está disponible. Si no se
	 * ha establecido una instancia de ventana (Scene), este método no realiza
	 * ninguna acción.
	 */
	public void closeWindow() {

		if (ventanaOpciones != null && ventanaOpciones.getWindow() instanceof Stage) {

			if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
				FuncionesManejoFront.getStageVentanas().remove(estadoStage());
			}
			Utilidades.cerrarOpcionesAvanzadas();
			nav.cerrarCargaComics();
			estadoStage().close();
		}
	}

	public static AccionReferencias getReferenciaVentanaPrincipal() {
		return referenciaVentanaPrincipal;
	}

	public static void setReferenciaVentanaPrincipal(AccionReferencias referenciaVentana) {
		OpcionesAvanzadasController.referenciaVentanaPrincipal = referenciaVentana;
	}
}
