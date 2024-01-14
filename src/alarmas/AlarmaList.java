package alarmas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Funcionamiento.Utilidades;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class AlarmaList {

	private List<AlarmaItem> alarmaItems = new ArrayList<>();

	private Timeline animacionAlarmaTimeline = new Timeline();
	private Timeline animacionAlarmaOnlineTimeline = new Timeline();
	private Timeline animacionAlarmaTimelineInternet = new Timeline();
	private Timeline animacionAlarmaTimelineMySql = new Timeline();

	/**
	 * Etiqueta para mostrar el estado del fichero.
	 */
	private Label prontEstadoFichero;
	/**
	 * Línea de tiempo para la animación.
	 */
	private static Timeline timeline;

	private Label alarmaConexion = new Label("alarmaConexion");
	private Label alarmaConexionInternet = new Label("alarmaConexionInternet");
	private Label alarmaConexionSql = new Label("alarmaConexionSql");
	private Label iniciarAnimacionEspera = new Label("prontEstadoConexion");

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

	public void iniciarThreadChecker() {
		Thread checkerThread = new Thread(() -> {
			try {
				iniciarAnimacionAlarma(alarmaConexion);
				while (true) {
					boolean estadoInternet = Utilidades.isInternetAvailable();
					Platform.runLater(() -> {

						Map<String, String> datosConfiguracion = Utilidades.devolverDatosConfig();

						String port = datosConfiguracion.get("Puerto");
						String host = datosConfiguracion.get("Hosting");
						iniciarAnimacionEspera(iniciarAnimacionEspera);
						if (estadoInternet) {

							detenerAnimacion(animacionAlarmaTimelineInternet);
							asignarTooltip(alarmaConexionInternet, "Tienes conexión a internet");
							alarmaConexionInternet.setStyle("-fx-background-color: blue;");

						} else {
							asignarTooltip(alarmaConexionInternet, "No tienes conexión a internet");
							iniciarAnimacionInternet(alarmaConexionInternet);
						}

						if (Utilidades.isMySQLServiceRunning(host, port)) {

							if (animacionAlarmaTimelineMySql != null
									&& animacionAlarmaTimelineMySql.getStatus() == Animation.Status.RUNNING) {
								animacionAlarmaTimelineMySql.stop();
							}
							iniciarAnimacionSql(alarmaConexionSql);
							detenerAnimacion(animacionAlarmaTimelineMySql);
							asignarTooltip(alarmaConexionSql, "Servicio de MySQL activado");
							alarmaConexionSql.setStyle("-fx-background-color: green;");
//						iniciarAnimacionSql();
						} else {
							asignarTooltip(alarmaConexionSql, "Servicio de MySQL desactivado");
							iniciarAnimacionSql(alarmaConexionSql);
						}

						asignarTooltip(alarmaConexion,
								"Esperando guardado/modificación de datos de la base de datos local");

					});
					Thread.sleep(5000); // Espera 15 segundos
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		checkerThread.setDaemon(true); // Marcar el hilo como daemon
		checkerThread.start();

		Utilidades.crearEstructura();
		detenerAnimacion();

	}

	public void detenerAnimacion(Timeline animacion) {
		if (animacion != null && animacion.getStatus() == Animation.Status.RUNNING) {
			animacion.stop();
		}
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public void iniciarAnimacionEspera(Label prontEstadoConexion) {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

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
		timeline.getKeyFrames().addAll(mostrarEsperando, mostrarPunto, mostrarDosPuntos, mostrarTresPuntos,
				ocultarTexto);

		// Iniciar la animación
		timeline.play();
	}

	private void iniciarAnimacionInternet(Label alarmaConexionInternet) {
		animacionAlarmaTimelineInternet = new Timeline();
		animacionAlarmaTimelineInternet.setCycleCount(Timeline.INDEFINITE);

		KeyFrame mostrarAmarillo1 = new KeyFrame(Duration.ZERO,
				new KeyValue(alarmaConexionInternet.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostratRojo1 = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(alarmaConexionInternet.styleProperty(), "-fx-background-color: red;"));
		KeyFrame mostrarAmarillo2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(alarmaConexionInternet.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostratRojo2 = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(alarmaConexionInternet.styleProperty(), "-fx-background-color: red;"));
		KeyFrame mostrarAmarillo3 = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(alarmaConexionInternet.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostratRojo3 = new KeyFrame(Duration.seconds(2.0),
				new KeyValue(alarmaConexionInternet.styleProperty(), "-fx-background-color: red;"));

		animacionAlarmaTimelineInternet.getKeyFrames().addAll(mostrarAmarillo1, mostratRojo1, mostrarAmarillo2,
				mostratRojo2, mostrarAmarillo3, mostratRojo3);
		animacionAlarmaTimelineInternet.play();
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

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "La base de datos no existe"));
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
	public void iniciarAnimacionDatosError(String puerto) {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

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

	/**
	 * Inicia la animación de espera en la interfaz.
	 */
	public void iniciarAnimacionSql(Label alarmaConexionSql) {
		animacionAlarmaTimelineMySql = new Timeline();
		animacionAlarmaTimelineMySql.setCycleCount(Timeline.INDEFINITE);

		KeyFrame mostrarAmarillo1 = new KeyFrame(Duration.ZERO,
				new KeyValue(alarmaConexionSql.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostratRojo1 = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(alarmaConexionSql.styleProperty(), "-fx-background-color: yellow;"));
		KeyFrame mostrarAmarillo2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(alarmaConexionSql.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostratRojo2 = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(alarmaConexionSql.styleProperty(), "-fx-background-color: yellow;"));
		KeyFrame mostrarAmarillo3 = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(alarmaConexionSql.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostratRojo3 = new KeyFrame(Duration.seconds(2.0),
				new KeyValue(alarmaConexionSql.styleProperty(), "-fx-background-color: yellow;"));

		animacionAlarmaTimelineMySql.getKeyFrames().addAll(mostrarAmarillo1, mostratRojo1, mostrarAmarillo2,
				mostratRojo2, mostrarAmarillo3, mostratRojo3);
		animacionAlarmaTimelineMySql.play();
	}

	private void iniciarAnimacionError(Label prontEstadoConexion) {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoConexion.textProperty(), "ERROR. Activa MySql"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoConexion.textProperty(), ""));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoConexion.textProperty(), "ERROR. Activa MySql"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
	}

	public void iniciarAnimacionAlarmaOnline(Label alarmaConexion) {
		animacionAlarmaOnlineTimeline = new Timeline();
		animacionAlarmaOnlineTimeline.setCycleCount(Timeline.INDEFINITE);

		KeyFrame mostrarVerde = new KeyFrame(Duration.ZERO,
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: green;"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));
		KeyFrame mostrarTransparente = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: green;"));
		KeyFrame mostrarVerdeNuevamente = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));

		animacionAlarmaOnlineTimeline.getKeyFrames().addAll(mostrarVerde, ocultarTexto, mostrarTransparente,
				mostrarVerdeNuevamente);
		animacionAlarmaOnlineTimeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionConexion(Label prontEstadoConexion) {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoConexion.textProperty(), "ERROR. Conectate primero"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoConexion.textProperty(), ""));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoConexion.textProperty(), "ERROR. Conectate primero"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	public static void iniciarAnimacionConectado(Label prontEstadoConexion) {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarConectado = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoConexion.textProperty(), "Conectado"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoConexion.textProperty(), ""));
		KeyFrame mostrarConectado2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoConexion.textProperty(), "Conectado"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarConectado, ocultarTexto, mostrarConectado2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Maneja las acciones después de una conexión exitosa.
	 */
	public void manejarConexionExitosa(Label prontEstadoConexion) {
		Utilidades.crearCarpetasBackup();
		detenerAnimacion();
		prontEstadoConexion.setStyle("-fx-background-color: #A0F52D");
		iniciarAnimacionConectado(prontEstadoConexion);

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
		prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
		iniciarAnimacionError(prontEstadoConexion);
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
	}

	/**
	 * Inicia la animación de alarma, que cambia el color de fondo de la alarma
	 * entre amarillo y transparente.
	 */
	public void iniciarAnimacionAlarma(Label alarmaConexion) {
		animacionAlarmaTimeline = new Timeline();
		animacionAlarmaTimeline.setCycleCount(Timeline.INDEFINITE);

		KeyFrame mostrarAmarillo = new KeyFrame(Duration.ZERO,
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: yellow;"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));
		KeyFrame mostrarTransparente = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: yellow;"));
		KeyFrame mostrarAmarilloNuevamente = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));

		animacionAlarmaTimeline.getKeyFrames().addAll(mostrarAmarillo, ocultarTexto, mostrarTransparente,
				mostrarAmarilloNuevamente);

		animacionAlarmaTimeline.play();
	}

	/**
	 * Inicia la animación de conexión exitosa en la interfaz.
	 */
	public static void iniciarAnimacionGuardado(Label prontEstadoFichero) {
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
}