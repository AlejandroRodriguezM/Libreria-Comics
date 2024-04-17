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

import Controladores.managment.AccionFuncionesComunes;
import Controladores.managment.AccionModificar;
import Controladores.managment.AccionReferencias;
import alarmas.AlarmaList;
import controlUI.FuncionesComboBox;
import controlUI.FuncionesManejoFront;
import dbmanager.ConectManager;
import dbmanager.DatabaseManagerDAO;
import dbmanager.ListaComicsDAO;
import funciones_auxiliares.Utilidades;
import funciones_auxiliares.Ventanas;
import funciones_auxiliares.VersionService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.FileChooser;
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

	public static ObservableList<String> urlActualizados = FXCollections.observableArrayList();

	public static AccionReferencias referenciaVentana = new AccionReferencias();

	/**
	 * Instancia de la clase FuncionesComboBox para el manejo de ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	/**
	 * Referencia a la ventana (stage).
	 */
	private Scene ventanaOpciones;

	public static boolean estaActualizado = true;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	public static AtomicBoolean actualizarFima = new AtomicBoolean(false);

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
		referenciaVentana.setStage(estadoStage());

		referenciaVentana.setBotonComprimirPortadas(botonComprimirPortadas);
		referenciaVentana.setBotonReCopiarPortadas(botonReCopiarPortadas);

		return referenciaVentana;
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
			AccionModificar.referenciaVentana = guardarReferencia();
			AccionFuncionesComunes.referenciaVentana = guardarReferencia();
			rellenarComboboxPreviews();
			FuncionesManejoFront.stageVentanas.add(estadoStage());
		});
		ventanaOpciones = miStageVentana();

		obtenerVersionDesdeOtraClase();
		AlarmaList.iniciarAnimacionEspera(prontInfo);
		AlarmaList.iniciarAnimacionEspera(prontInfoEspecial);
		AlarmaList.iniciarAnimacionEspera(prontInfoPreviews);
		AlarmaList.iniciarAnimacionEspera(prontInfoPortadas);

		checkFirmas.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
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

		if (!ConectManager.conexionActiva()) {
			return;
		}

		AlarmaList.detenerAnimacionEspera();
		DatabaseManagerDAO.makeSQL(prontInfo);
		Utilidades.borrarArchivosNoEnLista(ListaComicsDAO.listaImagenes);

	}

	@FXML
	void comprobarVersion(ActionEvent event) {

		String versionSW = VersionService.obtenerVersion();
		String versionLocal = VersionService.leerVersionDelArchivo();
		AlarmaList.detenerAnimacionEspera();
		labelComprobar.setStyle("-fx-text-fill: white;");

		String cadena = "";
		if (compareVersions(versionSW, versionLocal) > 0) {
			estaActualizado = false;
			labelComprobar.setStyle("-fx-text-fill: red;");
			labelVersionPreviews.setStyle("-fx-text-fill: red;");
			labelVersionPortadas.setStyle("-fx-text-fill: red;");
			labelVersionEspecial.setStyle("-fx-text-fill: red;");
			cadena = "Versión desactualizada";
		} else {
			estaActualizado = true;
			cadena = "Versión actualizada";
		}

		AlarmaList.iniciarAnimacionAvanzado(prontInfo, cadena);
		AlarmaList.iniciarAnimacionAvanzado(prontInfoPreviews, cadena);
		AlarmaList.iniciarAnimacionAvanzado(prontInfoEspecial, cadena);
		AlarmaList.iniciarAnimacionAvanzado(prontInfoPortadas, cadena);

		if (!estaActualizado) {
			botonActualizarSoftware.setVisible(true);
		}
	}

	@FXML
	void normalizarDataBase(ActionEvent event) {
		AlarmaList.detenerAnimacionEspera();
		DatabaseManagerDAO.comprobarNormalizado("nomGuionista", prontInfo);
		DatabaseManagerDAO.comprobarNormalizado("nomDibujante", prontInfo);
		DatabaseManagerDAO.comprobarNormalizado("nomVariante", prontInfo);

		ListaComicsDAO.reiniciarListaComics();
		ListaComicsDAO.listasAutoCompletado();
		List<ComboBox<String>> comboboxes = referenciaVentana.getComboboxes();
		referenciaVentana.getTablaBBDD().refresh();
		if (comboboxes != null) {
			funcionesCombo.rellenarComboBox(comboboxes);
		}

	}

	public void obtenerVersionDesdeOtraClase() {
		VersionService versionService = new VersionService();

		versionService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				String version = VersionService.leerVersionDelArchivo();
				labelVersion.setText(version);
				labelVersionPreviews.setText(version);
				labelVersionPortadas.setText(version);
				labelVersionEspecial.setText(version);
			}
		});

		versionService.start();
	}

	private static int compareVersions(String version1, String version2) {
		String[] parts1 = version1.split("\\.");
		String[] parts2 = version2.split("\\.");

		int minLength = Math.min(parts1.length, parts2.length);

		for (int i = 0; i < minLength; i++) {
			int part1 = Integer.parseInt(parts1[i]);
			int part2 = Integer.parseInt(parts2[i]);

			if (part1 != part2) {
				return Integer.compare(part1, part2);
			}
		}

		return Integer.compare(parts1.length, parts2.length);
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

				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
				File file = fileChooser.showSaveDialog(new Stage());

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
		AccionModificar.referenciaVentana = guardarReferencia();
		AccionFuncionesComunes.referenciaVentana = guardarReferencia();
		AccionModificar.actualizarDatabase("modificar", actualizarFima.get(), estadoStage());

	}

	@FXML
	void actualizarDatosComic(ActionEvent event) {
		AccionModificar.referenciaVentana = guardarReferencia();
		AccionFuncionesComunes.referenciaVentana = guardarReferencia();
		AccionModificar.actualizarDatabase("actualizar datos", actualizarFima.get(), estadoStage());
	}

	@FXML
	void actualizarPortadaComic(ActionEvent event) {
		AccionModificar.referenciaVentana = guardarReferencia();
		AccionFuncionesComunes.referenciaVentana = guardarReferencia();
		AccionModificar.actualizarDatabase("actualizar portadas", actualizarFima.get(), estadoStage());
	}

	@FXML
	void comprimirPortadas(ActionEvent event) {
		// Constantes
		final String DOCUMENTS_PATH = Utilidades.DOCUMENTS_PATH;
		final String DB_NAME = ConectManager.DB_NAME;
		final String directorioComun = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator + DB_NAME
				+ File.separator;
		final String directorioOriginal = directorioComun + "portadas" + File.separator;
		final String directorioNuevo = directorioComun + "portadas_originales";

		List<String> inputPaths = ListaComicsDAO.listaImagenes;
		AtomicReference<CargaComicsController> cargaComicsControllerRef = new AtomicReference<>();
		AtomicInteger portadasProcesados = new AtomicInteger(0);
		AtomicInteger mensajeIdCounter = new AtomicInteger(0); // Contador para generar IDs únicos
		// Crear y ejecutar tarea para comprimir las portadas
		Task<Void> task = new Task<>() {
			@Override
			protected Void call() throws Exception {
				HashSet<String> mensajesUnicos = new HashSet<>(); // Para almacenar mensajes únicos

				// Copiar directorio original a uno nuevo
				Utilidades.copiarDirectorio(directorioNuevo, directorioOriginal);
				nav.verCargaComics(cargaComicsControllerRef);
				int numEntries = inputPaths.size();
				inputPaths.forEach(codigo -> {
					portadasProcesados.getAndIncrement();
					if (isCancelled() || !referenciaVentana.getStage().isShowing()) {
						return; // Sale del método call() si la tarea ha sido cancelada
					}

					StringBuilder textoBuilder = new StringBuilder();
					// Actualizar texto de progreso
					String mensajeId = String.valueOf(mensajeIdCounter.getAndIncrement()); // Generar un ID único
					textoBuilder.append("Comprimiendo: ").append(portadasProcesados.get()).append(" de ")
							.append(numEntries).append("\n");
					mensajesUnicos.add(mensajeId + ": " + textoBuilder.toString());
					mensajesUnicos.add(textoBuilder.toString());
					try {
						File inputFile = new File(codigo);
						if (!inputFile.exists()) {
							return; // O manejar el caso de que la imagen no exista de otra manera
						}

						BufferedImage image = ImageIO.read(inputFile);
						if (image == null) {
							return; // O manejar el caso de que la imagen no se cargue correctamente de otra manera
						}

						// Comprimir imagen en un nuevo hilo
						Thread compressionThread = new Thread(() -> {
							try {
								Thumbnails.of(image).scale(1).outputQuality(0.5).toFile(codigo);
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
						compressionThread.start();
						try {
							compressionThread.join(); // Espera a que termine la compresión
						} catch (InterruptedException e) {
							compressionThread.interrupt(); // Envía una señal de interrupción al hilo
						}

						// Actualizar el progreso y la interfaz de usuario
						double progress = (double) portadasProcesados.get() / numEntries;
						String porcentaje = String.format("%.2f%%", progress * 100);

						if (nav.isVentanaCerrada()) {
							nav.verCargaComics(cargaComicsControllerRef);
							StringBuilder textoFiltrado = new StringBuilder();

							List<String> mensajesOrdenados = new ArrayList<>(mensajesUnicos); // Convertir el conjunto a
																								// lista
							Collections.sort(mensajesOrdenados,
									Comparator.comparingInt(m -> Integer.parseInt(m.split(":")[0]))); // Ordenar por ID

							for (String mensaje : mensajesOrdenados) {
								if (!mensaje.equalsIgnoreCase(textoBuilder.toString())) {
									textoFiltrado.append(mensaje.substring(mensaje.indexOf(":") + 2)); // Añadir mensaje
																										// sin el ID
								}
							}

							Platform.runLater(() -> {
								cargaComicsControllerRef.get().cargarDatosEnCargaComics(textoFiltrado.toString(),
										porcentaje, progress);
							});
						}

						Platform.runLater(() -> cargaComicsControllerRef.get()
								.cargarDatosEnCargaComics(textoBuilder.toString(), porcentaje, progress));
					} catch (IOException e) {
						e.printStackTrace();
						// Manejar la excepción adecuadamente según tus requisitos
					}
				});

				return null;
			}
		};

		// Manejar eventos de la tarea
		task.setOnRunning(ev -> {
			String mensaje = "Comprimiendo portadas";
			AlarmaList.iniciarAnimacionAvanzado(prontInfoPortadas, mensaje);
			botonCancelarSubidaPortadas.setVisible(true);
			FuncionesManejoFront.cambiarEstadoMenuBar(true);
			FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(true, referenciaVentana);
			FuncionesManejoFront.manejarMensajeTextArea(mensaje);
		});

		task.setOnSucceeded(ev -> {
			String mensaje = "Portadas comprimidas";
			AlarmaList.iniciarAnimacionAvanzado(prontInfoPortadas, mensaje);
			Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics("", "100%", 100.0));
			botonCancelarSubidaPortadas.setVisible(false);
			FuncionesManejoFront.cambiarEstadoMenuBar(false);
			FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, referenciaVentana);
			FuncionesManejoFront.manejarMensajeTextArea(mensaje);
		});

		task.setOnCancelled(ev -> {
			String mensaje = "Cancelada la actualizacion de la base de datos.";
			AlarmaList.iniciarAnimacionAvanzado(prontInfoPortadas, mensaje);
			FuncionesManejoFront.cambiarEstadoMenuBar(false);
			FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, referenciaVentana);
			FuncionesManejoFront.manejarMensajeTextArea(mensaje);
			
			Platform.runLater(() -> {
				cargaComicsControllerRef.get().cargarDatosEnCargaComics("", "100%", 100.0);
			});
		});

		// Iniciar tarea en un nuevo hilo
		Thread thread = new Thread(task);

		// Manejar la cancelación
		botonCancelarSubidaPortadas.setOnAction(ev -> {
			botonCancelarSubidaPortadas.setVisible(false);

			task.cancel();
		});

		thread.setDaemon(true);
		thread.start();
	}

	@FXML
	void reCopiarPortadas(ActionEvent event) {

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Seleccionar carpeta");

		// Mostrar el diálogo de selección de carpeta
		File selectedDirectory = directoryChooser.showDialog(null);

		String directorioNuevo = Utilidades.DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
				+ ConectManager.DB_NAME + File.separator + "portadas";

		Utilidades.copiarDirectorio(directorioNuevo, selectedDirectory.getAbsolutePath());

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

			if (FuncionesManejoFront.stageVentanas.contains(estadoStage())) {
				FuncionesManejoFront.stageVentanas.remove(estadoStage());
			}
			nav.cerrarCargaComics();
			Stage stage = (Stage) ventanaOpciones.getWindow();
			stage.close();
		}
	}
}
