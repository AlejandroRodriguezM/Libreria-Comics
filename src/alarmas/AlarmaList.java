package alarmas;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import dbmanager.ConectManager;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class AlarmaList {

	private List<AlarmaItem> alarmaItems = new ArrayList<>();

	private static Timeline animacionAlarmaTimeline = new Timeline();
	private static Timeline animacionAlarmaOnlineTimeline = new Timeline();
	private static Timeline animacionAlarmaTimelineInternet = new Timeline();
	private static Timeline animacionAlarmaTimelineMySql = new Timeline();

	private static final Object timelineLock = new Object();
	private static final Object timelineGifLock = new Object();

	/**
	 * Etiqueta para mostrar el estado del fichero.
	 */
	private Label prontEstadoFichero;

	/**
	 * Línea de tiempo para la animación.
	 */
	private static Timeline timeline;

	/**
	 * Línea de tiempo para la animación.
	 */
	private static Timeline timelineError;

	/**
	 * Línea de tiempo para la animación.
	 */
	private static Timeline timelineGif;

	private boolean ejecutando = true;

	private Label alarmaConexion = new Label("alarmaConexion");
	private Label alarmaConexionInternet = new Label("alarmaConexionInternet");
	private Label alarmaConexionSql = new Label("alarmaConexionSql");
	private Label iniciarAnimacionEspera = new Label("prontEstadoConexion");
	private Label iniciarAnimacionEsperaInternet = new Label("prontEstadoConexionInternet");

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	private static final String GIF_PATH = "/imagenes/cargaImagen.gif";

	private static Image eyeOpenImage;
	private static Image eyeClosedImage;

	public AlarmaList() {
		// Agregar instancias de AlarmaItem a la lista alarmaItems
		alarmaItems.add(new AlarmaItem(alarmaConexion, animacionAlarmaOnlineTimeline));
		alarmaItems.add(new AlarmaItem(alarmaConexionInternet, animacionAlarmaTimelineInternet));
		alarmaItems.add(new AlarmaItem(alarmaConexionSql, animacionAlarmaTimelineMySql));
		alarmaItems.add(new AlarmaItem(iniciarAnimacionEspera, animacionAlarmaTimeline));
	}

	public List<AlarmaItem> getAlarmaItems() {
		return alarmaItems;
	}

	public void setAlarmaConexion(Label alarmaConexion) {
		this.alarmaConexion = alarmaConexion;
		// Actualizar el Label en el primer elemento de la lista
		if (!alarmaItems.isEmpty()) {
			alarmaItems.get(0).setLabel(alarmaConexion);
		}
	}

	public void setAlarmaConexionInternet(Label alarmaConexionInternet) {
		this.alarmaConexionInternet = alarmaConexionInternet;
		// Actualizar el Label en el segundo elemento de la lista
		if (alarmaItems.size() > 1) {
			alarmaItems.get(1).setLabel(alarmaConexionInternet);
		}
	}

	public void setAlarmaConexionSql(Label alarmaConexionSql) {
		this.alarmaConexionSql = alarmaConexionSql;
		// Actualizar el Label en el tercer elemento de la lista
		if (alarmaItems.size() > 2) {
			alarmaItems.get(2).setLabel(alarmaConexionSql);
		}
	}

	public void setAlarmaConexionPrincipal(Label iniciarAnimacionEspera) {
		this.iniciarAnimacionEspera = iniciarAnimacionEspera;
		// Actualizar el Label en el tercer elemento de la lista
		if (alarmaItems.size() > 2) {
			alarmaItems.get(3).setLabel(iniciarAnimacionEspera);
		}
	}

	public void setAlarmaInternetLabel(Label iniciarAnimacionEsperaInternet) {
		this.iniciarAnimacionEsperaInternet = iniciarAnimacionEsperaInternet;
		// Actualizar el Label en el tercer elemento de la lista
		if (alarmaItems.size() > 2) {
			alarmaItems.get(3).setLabel(iniciarAnimacionEsperaInternet);
		}
	}

	public void iniciarThreadChecker() {
		ejecutando = true;
		ForkJoinPool.commonPool().execute(() -> {
			while (ejecutando) {
				try {
					procesarEstadoConexion();
					Thread.sleep(5000); // Espera 5 segundos antes de la próxima ejecución
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt(); // Restaura la bandera de interrupción
				} catch (Exception e) {
					Utilidades.manejarExcepcion(e);
				}
			}
		});
	}

	public void detenerThreadChecker() {
		ejecutando = false;
	}

	private void procesarEstadoConexion() {

		boolean estadoInternet = Utilidades.isInternetAvailable();
		Platform.runLater(() -> {
			manejarConexionPrincipal(estadoInternet);
			manejarConexionInternet(estadoInternet);

			asignarTooltip(alarmaConexion,
					"Esperando guardado/modificación/conexion de datos de la base de datos local");
		});
	}

	private void manejarConexionInternet(boolean esComprobarConexion) {
		if (esComprobarConexion) {
			iniciarAnimacionEspera(iniciarAnimacionEsperaInternet);
			detenerAnimacion(animacionAlarmaTimelineInternet);
			asignarTooltip(alarmaConexionInternet, "Tienes conexión a internet");
			iniciarAnimacionAlarmaOnline(alarmaConexionInternet);
		} else {
			asignarTooltip(alarmaConexionInternet, "No tienes conexión a internet");
			iniciarAnimacionAlarmaError(alarmaConexionInternet);
			iniciarAnimacionEspera(iniciarAnimacionEspera);
		}
	}

	private void manejarConexionPrincipal(boolean esComprobarConexion) {

		if (esComprobarConexion) {

			if (ConectManager.estadoConexion) {
				detenerAnimacionEspera();
				iniciarAnimacionConectado(iniciarAnimacionEspera);
				iniciarAnimacionConectado(iniciarAnimacionEsperaInternet);
			} else {
				iniciarAnimacionEspera(iniciarAnimacionEspera);
				iniciarAnimacionEspera(iniciarAnimacionEsperaInternet);
			}
		} else {
			iniciarAnimacionAlarma(alarmaConexion);
			iniciarAnimacionDesconectado(iniciarAnimacionEspera);
		}
		asignarTooltip(alarmaConexion, "Esperando guardado/modificación/conexion de datos de la base de datos local");
	}

	public void detenerAnimacion(Timeline animacion) {
		if (animacion != null && animacion.getStatus() == Animation.Status.RUNNING) {
			animacion.stop();
		}
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionEspera(Label prontEstadoConexion) {

		timelineError = new Timeline();
		timelineError.setCycleCount(Animation.INDEFINITE);
		prontEstadoConexion.setStyle("-fx-background-color: #29B6CC;");

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarEsperando = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoConexion.textProperty(), "Esperando"));
		KeyFrame mostrarPunto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoConexion.textProperty(), "Esperando."));
		KeyFrame mostrarDosPuntos = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoConexion.textProperty(), "Esperando.."));
		KeyFrame mostrarTresPuntos = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontEstadoConexion.textProperty(), "Esperando..."));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(2), new KeyValue(prontEstadoConexion.textProperty(), ""));

		// Agregar los keyframes al timeline
		timelineError.getKeyFrames().addAll(mostrarEsperando, mostrarPunto, mostrarDosPuntos, mostrarTresPuntos,
				ocultarTexto);

		timelineError.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionAvanzado(Label prontEstadoConexion, String cadena) {

//		detenerAnimacionEspera();

		timelineError = new Timeline();
		timelineError.setCycleCount(Animation.INDEFINITE);
		prontEstadoConexion.setStyle("-fx-background-color: #29B6CC;");

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarEsperando = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoConexion.textProperty(), cadena));
		KeyFrame mostrarPunto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoConexion.textProperty(), ""));
		KeyFrame mostrarDosPuntos = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoConexion.textProperty(), cadena));
		KeyFrame mostrarTresPuntos = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontEstadoConexion.textProperty(), ""));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(2), new KeyValue(prontEstadoConexion.textProperty(), ""));

		// Agregar los keyframes al timeline
		timelineError.getKeyFrames().addAll(mostrarEsperando, mostrarPunto, mostrarDosPuntos, mostrarTresPuntos,
				ocultarTexto);

		// Iniciar la animación
		timelineError.play();
	}

	public static void iniciarAnimacionConectado(Label prontEstadoConexion) {

		timelineError = new Timeline();
		timelineError.setCycleCount(Animation.INDEFINITE);

//		prontEstadoConexion.setStyle("-fx-background-color: green;");
		prontEstadoConexion.setStyle("-fx-background-color: #A0F52D");

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarEsperando = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoConexion.textProperty(), "Conectado"));
		KeyFrame mostrarPunto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoConexion.textProperty(), "Conectado."));
		KeyFrame mostrarDosPuntos = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoConexion.textProperty(), "Conectado.."));
		KeyFrame mostrarTresPuntos = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontEstadoConexion.textProperty(), "Conectado..."));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(2), new KeyValue(prontEstadoConexion.textProperty(), ""));

		// Agregar los keyframes al timeline
		timelineError.getKeyFrames().addAll(mostrarEsperando, mostrarPunto, mostrarDosPuntos, mostrarTresPuntos,
				ocultarTexto);

		// Iniciar la animación
		timelineError.play();

	}

	public static void detenerAnimacionEspera() {
		if (timelineError != null) {
			timelineError.stop();

		}
	}

	/**
	 * Inicia la animación de espera en la interfaz.
	 */
	public void iniciarAnimacionConexionRed(Label alarmaConexion) {
		animacionAlarmaTimeline = new Timeline();
		animacionAlarmaTimeline.setCycleCount(Animation.INDEFINITE);

		KeyFrame mostrarAmarillo1 = new KeyFrame(Duration.ZERO,
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: yellow;"));
		KeyFrame mostratRojo1 = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostrarAmarillo2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: yellow;"));
		KeyFrame mostratRojo2 = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostrarAmarillo3 = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: yellow;"));
		KeyFrame mostratRojo3 = new KeyFrame(Duration.seconds(2.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: orange;"));

		animacionAlarmaTimeline.getKeyFrames().addAll(mostrarAmarillo1, mostratRojo1, mostrarAmarillo2, mostratRojo2,
				mostrarAmarillo3, mostratRojo3);
		animacionAlarmaTimeline.play();
	}

	/**
	 * Inicia la animación de restauración con error en la interfaz.
	 */
	public void iniciarAnimacionRestauradoError(Label prontEstadoFichero) {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "No se ha podido restaurar correctamente"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoFichero.textProperty(), ""));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoFichero.textProperty(), "ERROR"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Inicia la animación de restauración con error en la interfaz.
	 */
	public void iniciarAnimacionBBDDError(Label prontEstadoFichero) {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		prontEstadoFichero.setStyle("-fx-background-color: red;");

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO, new KeyValue(prontEstadoFichero.textProperty(), "ERROR"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoFichero.textProperty(), "La base de datos no existe"));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1), new KeyValue(prontEstadoFichero.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Inicia la animación de restauración con error en la interfaz.
	 */
	public void iniciarAnimacionDatosError(Label prontEstadoConexion, String puerto) {

		if (prontEstadoConexion != null) {

			timeline = new Timeline();
			timeline.setCycleCount(Timeline.INDEFINITE);

			prontEstadoFichero.setStyle("-fx-background-color: red;");

			// Agregar los keyframes para cambiar el texto
			KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
					new KeyValue(prontEstadoFichero.textProperty(), "Los datos recibidos estan incorrectos."));
			KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
					new KeyValue(prontEstadoFichero.textProperty(), ""));
			KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
					new KeyValue(prontEstadoFichero.textProperty(), "ERROR"));

			// Agregar los keyframes al timeline
			timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

			// Iniciar la animación
			timeline.play();
		}
	}

	public static void iniciarAnimacionErrorMySql(Label prontEstadoConexion) {

		detenerAnimacion();

		if (timeline == null) {

			timelineError = new Timeline();
			timelineError.setCycleCount(Timeline.INDEFINITE);

			prontEstadoConexion.setStyle("-fx-background-color: red;");
			// Agregar los keyframes para cambiar el texto
			KeyFrame mostrarError = new KeyFrame(Duration.ZERO, new KeyValue(prontEstadoConexion.textProperty(), ""));
			KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
					new KeyValue(prontEstadoConexion.textProperty(), "ERROR. Activa MySql"));
			KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
					new KeyValue(prontEstadoConexion.textProperty(), ""));

			// Agregar los keyframes al timeline
			timelineError.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

			// Iniciar la animación
			timelineError.play();
		}
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionConexionError(Label prontEstadoConexion, String mensaje) {
		timelineError = new Timeline();
		timelineError.setCycleCount(Animation.INDEFINITE);
		prontEstadoConexion.setStyle("-fx-background-color: red;");

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO, new KeyValue(prontEstadoConexion.textProperty(), ""));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoConexion.textProperty(), mensaje));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoConexion.textProperty(), ""));

		// Agregar los keyframes al timeline
		timelineError.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timelineError.play();

	}

	public static void iniciarAnimacionDesconectado(Label prontEstadoConexion) {
		timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);

		prontEstadoConexion.setStyle("-fx-background-color: red; -fx-text-fill: white;");

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarConectado = new KeyFrame(Duration.ZERO, new KeyValue(prontEstadoConexion.textProperty(), ""));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(prontEstadoConexion.textProperty(), "Desconectado"));
		KeyFrame mostrarConectado2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoConexion.textProperty(), ""));
		KeyFrame ocultarTextoDeNuevo = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(prontEstadoConexion.textProperty(), "Desconectado"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarConectado, ocultarTexto, mostrarConectado2, ocultarTextoDeNuevo);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Inicia la animación de alarma, que cambia el color de fondo de la alarma
	 * entre amarillo y transparente.
	 */
	public static void iniciarAnimacionAlarma(Label alarmaConexion) {
		animacionAlarmaTimeline = new Timeline();
		animacionAlarmaTimeline.setCycleCount(Timeline.INDEFINITE);

		KeyFrame mostrarAmarillo = new KeyFrame(Duration.ZERO,
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: yellow;"));
		KeyFrame mostrarTransparente = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));
		KeyFrame mostrarAmarilloNuevamente = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: yellow;"));

		animacionAlarmaTimeline.getKeyFrames().addAll(mostrarAmarillo, ocultarTexto, mostrarTransparente,
				mostrarAmarilloNuevamente);

		animacionAlarmaTimeline.play();
	}

	public static void iniciarAnimacionAlarmaOnline(Label alarmaConexion) {
		animacionAlarmaOnlineTimeline = new Timeline();
		animacionAlarmaOnlineTimeline.setCycleCount(Timeline.INDEFINITE);

		KeyFrame mostrarVerde = new KeyFrame(Duration.ZERO,
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: green;"));
		KeyFrame mostrarTransparente = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));
		KeyFrame mostrarVerdeNuevamente = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: green;"));

		animacionAlarmaOnlineTimeline.getKeyFrames().addAll(mostrarVerde, ocultarTexto, mostrarTransparente,
				mostrarVerdeNuevamente);
		animacionAlarmaOnlineTimeline.play();
	}

	public static void iniciarAnimacionAlarmaError(Label alarmaConexion) {
		animacionAlarmaOnlineTimeline = new Timeline();
		animacionAlarmaOnlineTimeline.setCycleCount(Timeline.INDEFINITE);

		KeyFrame mostrarVerde = new KeyFrame(Duration.ZERO,
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: red;"));
		KeyFrame mostrarTransparente = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));
		KeyFrame mostrarVerdeNuevamente = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: red;"));

		animacionAlarmaOnlineTimeline.getKeyFrames().addAll(mostrarVerde, ocultarTexto, mostrarTransparente,
				mostrarVerdeNuevamente);
		animacionAlarmaOnlineTimeline.play();
	}

	/**
	 * Maneja las acciones después de una conexión exitosa.
	 */
	public void manejarConexionExitosa(Label prontEstadoConexion) {
		Utilidades.crearCarpetasBackup();
		detenerAnimacion();
		prontEstadoConexion.setStyle("-fx-background-color: #A0F52D");

		alarmaConexion.setStyle("-fx-background-color: blue;");
		asignarTooltip(alarmaConexion, "Estás conectado a la base de datos.");
		iniciarAnimacionAlarmaOnline(alarmaConexion);
	}

	/**
	 * Maneja las acciones después de un error de conexión.
	 * 
	 * @param mensaje Mensaje de error.
	 */
	public void manejarErrorConexion(String mensaje, Label prontEstadoConexion) {
		asignarTooltip(alarmaConexion, mensaje);
		detenerAnimacion();

		if (prontEstadoConexion != null) {
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			prontEstadoConexion.setText(mensaje);
		}
	}

	/**
	 * Detiene la animación actual en la interfaz.
	 */
	public static void detenerAnimacion() {
		if (timeline != null) {
			timeline.stop();
			timeline.getKeyFrames().clear(); // Eliminar los KeyFrames del Timeline
			timeline = null; // Destruir el objeto timeline
		}

		if (timelineError != null) {
			timelineError.stop();
			timelineError.getKeyFrames().clear(); // Eliminar los KeyFrames del Timeline
			timelineError = null; // Destruir el objeto timeline
		}
	}

	/**
	 * Inicia la animación de conexión exitosa en la interfaz.
	 */
	public static void iniciarAnimacionGuardado(Label prontEstadoFichero) {
		prontEstadoFichero.setStyle("-fx-background-color: #A0F52D");
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarConectado = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "Fichero guardado"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.6),
				new KeyValue(prontEstadoFichero.textProperty(), ""));
		KeyFrame mostrarConectado2 = new KeyFrame(Duration.seconds(1.1),
				new KeyValue(prontEstadoFichero.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarConectado, ocultarTexto, mostrarConectado2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Inicia la animación de restauración exitosa en la interfaz.
	 */
	public void iniciarAnimacionRestaurado(Label prontEstadoFichero) {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "Fichero restaurado correctamente"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoFichero.textProperty(), ""));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1), new KeyValue(prontEstadoFichero.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();

		iniciarAnimacionAlarma(alarmaConexion);
	}

	public void mensajeRespuestaGuardado(Label prontEstadoFichero, Label alarmaConexion) {
		detenerAnimacion();

		if (prontEstadoFichero != null) {
			prontEstadoFichero.setStyle("-fx-background-color: #A0F52D");
			iniciarAnimacionGuardado(prontEstadoFichero);
		}

		if (alarmaConexion != null) {
			alarmaConexion.setStyle("-fx-background-color: green;");
			iniciarAnimacionAlarmaOnline(alarmaConexion);
			asignarTooltip(alarmaConexion, "Datos guardados correctamente.");
		}
	}

	public void mensajeRespuestaError(Label prontEstadoFichero, Label alarmaConexion) {
		if (alarmaConexion != null) {
			AlarmaList.asignarTooltip(alarmaConexion, "No hay datos para poder guardar");
		}

		detenerAnimacion();

		if (prontEstadoFichero != null) {
			prontEstadoFichero.setStyle("-fx-background-color: #DD370F");
			iniciarAnimacionBBDDError(prontEstadoFichero);
		}

		if (alarmaConexion != null) {
			if (prontEstadoFichero == null) {
				// Si prontEstadoFichero también es nulo, mostrar un mensaje de error o manejar
				// de otra manera.
			}
			alarmaConexion.setStyle("-fx-background-color: yellow;");
			iniciarAnimacionAlarma(alarmaConexion);
		}
	}

	/**
	 * Asigna un tooltip a un label.
	 *
	 * @param label   El label al que se asignará el tooltip.
	 * @param mensaje El mensaje del tooltip.
	 */
	public static void asignarTooltip(Label label, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
		label.setTooltip(tooltip);
	}

	// Alarmas de pront en MenuPrincipal

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionEspera(TextArea prontInfo) {
		prontInfo.setOpacity(1);
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarSubida1 = new KeyFrame(Duration.ZERO, new KeyValue(prontInfo.textProperty(), "Esperando"));
		KeyFrame mostrarSubida2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontInfo.textProperty(), "Esperando."));
		KeyFrame mostrarSubida3 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontInfo.textProperty(), "Esperando.."));
		KeyFrame mostrarSubida4 = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontInfo.textProperty(), "Esperando..."));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarSubida1, mostrarSubida2, mostrarSubida3, mostrarSubida4);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionTextArea(TextArea prontInfo, String mensaje) {
		prontInfo.setOpacity(1);
//		Timeline timeline = new Timeline();
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarSubida1 = new KeyFrame(Duration.ZERO, new KeyValue(prontInfo.textProperty(), mensaje));
		KeyFrame mostrarSubida2 = new KeyFrame(Duration.seconds(0.5), new KeyValue(prontInfo.textProperty(), ""));
		KeyFrame mostrarSubida3 = new KeyFrame(Duration.seconds(1), new KeyValue(prontInfo.textProperty(), mensaje));
		KeyFrame mostrarSubida4 = new KeyFrame(Duration.seconds(1.5), new KeyValue(prontInfo.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarSubida1, mostrarSubida2, mostrarSubida3, mostrarSubida4);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionGuardado(TextArea prontInfo) {
		prontInfo.setOpacity(1);
		prontInfo.setStyle("-fx-background-color: green;");

		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarSubida1 = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInfo.textProperty(), "Fichero guardado correctamente"));
		KeyFrame mostrarSubida2 = new KeyFrame(Duration.seconds(0.5), new KeyValue(prontInfo.textProperty(), ""));
		KeyFrame mostrarSubida3 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontInfo.textProperty(), "Fichero guardado correctamente"));
		KeyFrame mostrarSubida4 = new KeyFrame(Duration.seconds(1.5), new KeyValue(prontInfo.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarSubida1, mostrarSubida2, mostrarSubida3, mostrarSubida4);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionError(Label prontInfo) {
		prontInfo.setOpacity(1);
		prontInfo.setStyle("-fx-background-color: #FF6347;");

		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarSubida1 = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInfo.textProperty(), "ERROR. No se ha podido guardar el fichero"));
		KeyFrame mostrarSubida2 = new KeyFrame(Duration.seconds(0.5), new KeyValue(prontInfo.textProperty(), ""));
		KeyFrame mostrarSubida3 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontInfo.textProperty(), "ERROR. No se ha podido guardar el fichero"));
		KeyFrame mostrarSubida4 = new KeyFrame(Duration.seconds(1.5), new KeyValue(prontInfo.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarSubida1, mostrarSubida2, mostrarSubida3, mostrarSubida4);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionReEstablecido(Label prontInfo) {
		prontInfo.setOpacity(1);
		prontInfo.setStyle("-fx-background-color: yellow;");

		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarSubida1 = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInfo.textProperty(), "Fichero restablecido"));
		KeyFrame mostrarSubida2 = new KeyFrame(Duration.seconds(0.5), new KeyValue(prontInfo.textProperty(), ""));
		KeyFrame mostrarSubida3 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontInfo.textProperty(), "Fichero restablecido"));
		KeyFrame mostrarSubida4 = new KeyFrame(Duration.seconds(1.5), new KeyValue(prontInfo.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarSubida1, mostrarSubida2, mostrarSubida3, mostrarSubida4);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionSubida(TextArea prontInfo) {
		prontInfo.setOpacity(1);

		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarSubida1 = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInfo.textProperty(), "Subido datos a la " + ConectManager.DB_NAME + " ."));
		KeyFrame mostrarSubida2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontInfo.textProperty(), "Subido datos a la " + ConectManager.DB_NAME + " .."));
		KeyFrame mostrarSubida3 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontInfo.textProperty(), "Subido datos a la " + ConectManager.DB_NAME + " ..."));
		KeyFrame mostrarSubida4 = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontInfo.textProperty(), "Subido datos a la " + ConectManager.DB_NAME + " ...."));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarSubida1, mostrarSubida2, mostrarSubida3, mostrarSubida4);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionBajada(TextArea prontInfo) {
		prontInfo.setOpacity(1);
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarBajada1 = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInfo.textProperty(), "Eliminando base de datos ."));
		KeyFrame mostrarBajada2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontInfo.textProperty(), "Eliminando base de datos .."));
		KeyFrame mostrarBajada3 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontInfo.textProperty(), "Eliminando base de datos ..."));
		KeyFrame mostrarBajada4 = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontInfo.textProperty(), "Eliminando base de datos ...."));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarBajada1, mostrarBajada2, mostrarBajada3, mostrarBajada4);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionEstadistica(TextArea prontInfo) {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarDescarga1 = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInfo.textProperty(), "Generando fichero de estadisticas ."));
		KeyFrame mostrarDescarga2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontInfo.textProperty(), "Generando fichero de estadisticas .."));
		KeyFrame mostrarDescarga3 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontInfo.textProperty(), "Generando fichero de estadisticas ..."));
		KeyFrame mostrarDescarga4 = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontInfo.textProperty(), "Generando fichero de estadisticas ...."));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarDescarga1, mostrarDescarga2, mostrarDescarga3, mostrarDescarga4);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Maneja el fallo de la tarea, mostrando un mensaje de error y deteniendo las
	 * animaciones.
	 */
	public void manejarFallo(String mensaje, TextArea prontInfo) {
		prontInfo.clear();
		mostrarMensajePront(mensaje, false, prontInfo);
		detenerAnimacionPront(prontInfo);
		detenerAnimacion();
	}

	public static void mostrarMensajePront(String mensaje, boolean exito, TextArea prontInfo) {

		if (prontInfo != null) {
			detenerAnimacion();
			prontInfo.setOpacity(1);
			prontInfo.setVisible(true);
			if (exito) {
				prontInfo.setStyle("-fx-border-color: green;");
			} else {
				prontInfo.setStyle("-fx-border-color: red;");
			}
			prontInfo.setText(mensaje);
		}
	}

	public static void mostrarMensajeProntLabel(String mensaje, boolean exito, Label prontInfo) {

		if (prontInfo != null) {
			detenerAnimacion();
			prontInfo.setOpacity(1);
			prontInfo.setVisible(true);
			if (exito) {
				prontInfo.setStyle("-fx-border-color: green;");
			} else {
				prontInfo.setStyle("-fx-border-color: red;");
			}
			prontInfo.setText(mensaje);

			detenerAnimacion();
		}
	}

	/**
	 * Metodo que permite detener una animacion
	 */
	public static void detenerAnimacionPront(TextArea prontInfo) {
		if (timeline != null) {
			timeline.stop();
			timeline = null; // Destruir el objeto timeline
		}
	}

	/**
	 * Inicia la animación del progreso de carga.
	 */
	public static void iniciarAnimacionCarga(ProgressIndicator progresoCarga) {
		Platform.runLater(() -> {
			progresoCarga.setVisible(true);
			progresoCarga.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		});
	}

	public static void detenerAnimacionCarga(ProgressIndicator progresoCarga) {
		Platform.runLater(() -> {

			if (progresoCarga != null) {
				progresoCarga.setVisible(false);
				progresoCarga.setProgress(0); // Establece el progreso en 0 para detener la animación
			}

		});
	}

	public static void manejarFalloGuardadoBD(Throwable exception, TextArea prontInfo) {
		if (exception != null) {
			exception.printStackTrace();
			Platform.runLater(
					() -> nav.alertaException("Error al guardar datos en la base de datos: " + exception.getMessage()));
		}
		String mensaje = "ERROR. No se ha podido guardar correctamente en la base de datos.";

		mostrarMensajePront(mensaje, false, prontInfo);
	}

	// Funciones CrearBBDDController

	/**
	 * Metodo que permite crear una animacion
	 * 
	 */
	public static void iniciarAnimacionBaseCreada(Label prontInformativo, String DB_NAME) {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		prontInformativo.setStyle("-fx-background-color: #A0F52D");
		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarConectado = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInformativo.textProperty(), "Base de datos: " + DB_NAME + " creada correctamente"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.6), new KeyValue(prontInformativo.textProperty(), ""));
		KeyFrame mostrarConectado2 = new KeyFrame(Duration.seconds(1.1),
				new KeyValue(prontInformativo.textProperty(), "Base de datos: " + DB_NAME + " creada correctamente"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarConectado, ocultarTexto, mostrarConectado2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 * 
	 */
	public static void iniciarAnimacionBaseExiste(Label prontInformativo, String dbNombre) {
		timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);
		prontInformativo.setStyle("-fx-background-color: #DD370F");

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarConectado = new KeyFrame(Duration.ZERO, new KeyValue(prontInformativo.textProperty(),
				"ERROR. Ya existe una base de datos llamada: " + dbNombre));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.6), new KeyValue(prontInformativo.textProperty(), ""));
		KeyFrame mostrarConectado2 = new KeyFrame(Duration.seconds(1.1), new KeyValue(prontInformativo.textProperty(),
				"ERROR. Ya existe una base de datos llamada: " + dbNombre));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarConectado, ocultarTexto, mostrarConectado2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 * 
	 */
	public static void iniciarAnimacionBaseError(String error, Label prontInformativo) {
		timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarConectado = new KeyFrame(Duration.ZERO, new KeyValue(prontInformativo.textProperty(), error));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.6), new KeyValue(prontInformativo.textProperty(), ""));
		KeyFrame mostrarConectado2 = new KeyFrame(Duration.seconds(1.1),
				new KeyValue(prontInformativo.textProperty(), error));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarConectado, ocultarTexto, mostrarConectado2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 * 
	 */
	public static void iniciarAnimacionEsperaCreacion(Label prontInformativo) {
		timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarEsperando = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInformativo.textProperty(), "Esperando entrada de datos"));
		KeyFrame mostrarPunto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontInformativo.textProperty(), "Esperando entrada de datos."));
		KeyFrame mostrarDosPuntos = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontInformativo.textProperty(), "Esperando entrada de datos.."));
		KeyFrame mostrarTresPuntos = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontInformativo.textProperty(), "Esperando entrada de datos..."));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(2), new KeyValue(prontInformativo.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarEsperando, mostrarPunto, mostrarDosPuntos, mostrarTresPuntos,
				ocultarTexto);

		// Iniciar la animación
		timeline.play();
	}

	// Funciones de VentanaAccionController

	/**
	 * Inicia una animación que alterna entre dos imágenes en un ImageView para
	 * lograr un efecto visual llamativo. La animación se ejecuta de forma
	 * indefinida y cambia las imágenes cada 0.1 segundos.
	 */
	public static void iniciarAnimacionCambioImagen(ImageView imagenFondo) {

		// Agrega las imágenes que deseas mostrar en cada KeyFrame
		InputStream imagenStream1 = Utilidades.class.getResourceAsStream("/imagenes/accionComicDeseo.jpg");
		InputStream imagenStream2 = Utilidades.class.getResourceAsStream("/imagenes/accionComic.jpg");

		// Convierte las corrientes de entrada en objetos Image
		Image imagen1 = new Image(imagenStream1);
		Image imagen2 = new Image(imagenStream2);

		// Establece la imagen inicial en el ImageView
		imagenFondo.setImage(imagen1);

		// Configura la opacidad inicial
		imagenFondo.setOpacity(1);

		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar la imagen
		KeyFrame cambiarImagen1 = new KeyFrame(Duration.ZERO, new KeyValue(imagenFondo.imageProperty(), imagen1));
		KeyFrame cambiarImagen2 = new KeyFrame(Duration.seconds(0.1),
				new KeyValue(imagenFondo.imageProperty(), imagen2));
		KeyFrame cambiarImagen3 = new KeyFrame(Duration.seconds(0.2),
				new KeyValue(imagenFondo.imageProperty(), imagen1));
		KeyFrame cambiarImagen4 = new KeyFrame(Duration.seconds(0.3),
				new KeyValue(imagenFondo.imageProperty(), imagen2));
		KeyFrame cambiarImagen5 = new KeyFrame(Duration.seconds(0.4),
				new KeyValue(imagenFondo.imageProperty(), imagen1));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(cambiarImagen1, cambiarImagen2, cambiarImagen3, cambiarImagen4, cambiarImagen5);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Inicia la animación de carga de imagen, mostrando un GIF animado en un bucle
	 * continuo.
	 */
	public static void iniciarAnimacionCargaImagen(ImageView cargaImagen) {
//	    detenerAnimacionCargaImagen(cargaImagen); // Detener animación anterior

		Image gif = cargarGif(GIF_PATH);

		// Establecer la imagen inicial en el ImageView
		cargaImagen.setImage(gif);

		// Configurar la opacidad inicial
		cargaImagen.setOpacity(1);

		timelineGif = new Timeline();
		cargaImagen.setVisible(true);
		timelineGif.setCycleCount(Timeline.INDEFINITE);

		// Agregar el keyframe para cambiar la imagen
		KeyFrame cambiarGif = new KeyFrame(Duration.ZERO, new KeyValue(cargaImagen.imageProperty(), gif));

		// Agregar el keyframe al timeline
		timelineGif.getKeyFrames().add(cambiarGif);

		// Iniciar la animación
		timelineGif.play();
	}

	public static void detenerAnimacionCargaImagen(ImageView cargaImagen) {
		synchronized (timelineGifLock) {
			if (timelineGif != null) {
				timelineGif.stop();
				timelineGif = null; // Destruir el objeto timeline

				Platform.runLater(() -> {
					cargaImagen.setImage(null);
					cargaImagen.setVisible(false);
				});
			}
		}
	}

	/**
	 * Carga un GIF desde la ruta proporcionada.
	 *
	 * @param path Ruta del archivo GIF.
	 * @return La imagen del GIF cargado.
	 */
	private static Image cargarGif(String path) {
		InputStream gifStream = Utilidades.class.getResourceAsStream(path);
		return new Image(gifStream);
	}

	/**
	 * Metodo que permite detener una animacion
	 */
	public static void detenerAnimacionProntAccion(ImageView imagenFondo) {
		synchronized (timelineLock) {
			if (timeline != null) {
				timeline.stop();
				timeline = null; // Destruir el objeto timeline

				Platform.runLater(() -> {
					InputStream imagenStream = Utilidades.class.getResourceAsStream("/imagenes/accionComic.jpg");
					Image imagen = new Image(imagenStream);
					imagenFondo.setImage(imagen);
				});
			}
		}
	}

	public static void configureEyeToggle(ImageView toggleEyeImageView, TextField passUsuarioText,
			PasswordField passBBDD) {
		eyeOpenImage = new Image(Utilidades.class.getResourceAsStream("/imagenes/visible.png"), 20, 20, true, true);
		eyeClosedImage = new Image(Utilidades.class.getResourceAsStream("/imagenes/hide.png"), 20, 20, true, true);

		// Configurar el ImageView con la imagen de ojo abierto inicialmente
		toggleEyeImageView.setImage(eyeClosedImage);

		// Establecer el manejador de eventos para el ImageView
		toggleEyeImageView.setOnMouseClicked(event -> toggleEye(toggleEyeImageView, passUsuarioText, passBBDD));
	}

	private static void toggleEye(ImageView toggleEyeImageView, TextField passUsuarioText, PasswordField passBBDD) {
		if (toggleEyeImageView.getImage() == eyeOpenImage) {
			passUsuarioText.setVisible(false);
			passUsuarioText.setDisable(true);
			passBBDD.setVisible(true);
			passBBDD.setDisable(false);

			passBBDD.setPromptText(passUsuarioText.getPromptText());
			passUsuarioText.setText(passBBDD.getText());
			toggleEyeImageView.setImage(eyeClosedImage); // Cambiar a la imagen de ojo cerrado
		} else {
			passUsuarioText.setVisible(true);
			passUsuarioText.setDisable(false);
			passBBDD.setVisible(false);
			passBBDD.setDisable(true);

			passUsuarioText.setText(passBBDD.getText());
			passBBDD.setPromptText(passUsuarioText.getPromptText());
			toggleEyeImageView.setImage(eyeOpenImage); // Cambiar a la imagen de ojo abierto
		}
	}

}