package Controladores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Esta clase sirve para acceder a la base de datos y poder realizar diferentes
 * funciones..
 *
 * @author Alejandro Rodriguez
 */
public class AccesoBBDDController implements Initializable{

    @FXML
    private Button botonAccesobbdd;

    @FXML
    private Button botonDescargaBBDD;

    @FXML
    private Button botonEnviar;

    @FXML
    private Button botonOpciones;

    @FXML
    private Button botonSalir;

    @FXML
    private Button botonSobreMi;

	@FXML
	private Label prontEstadoConexion;

	
	private static Ventanas nav = new Ventanas();
	private static CrearBBDDController cbd = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    
	    crearEstructura();

	}

	/**
	 * Funcion para abrir el navegador y acceder a la URL
	 *
	 * @param event
	 */
	@FXML
	void accesoMySqlWorkbench(ActionEvent event) {
		String url1 = "https://dev.mysql.com/downloads/windows/installer/8.0.html";
		String url2 = "https://www.youtube.com/watch?v=FvXQBKsp0OI&ab_channel=MisterioRojo";

		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url1); // Llamada a funcion
			Utilidades.accesoWebWindows(url2); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url1); // Llamada a funcion
				Utilidades.accesoWebLinux(url2); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url1); // Llamada a funcion
				Utilidades.accesoWebMac(url2); // Llamada a funcion
			}
		}
	}

	/**
	 * Funcion que permite el acceso a la ventana de menuPrincipal
	 *
	 * @param event
	 */
	@FXML
	void entrarMenu(ActionEvent event) {

		if (JDBC.DBManager.isConnected()) { // Siempre que el metodo de la clase DBManager sea true, permitira acceder
											// al menu principal

			nav.verMenuPrincipal(); // Llamada a metodo de la clase NavegacionVentanas. Permite cargar y mostrar el
									// menu principal
			Stage myStage = (Stage) this.botonAccesobbdd.getScene().getWindow();
			myStage.close();
		} else { // En caso contrario mostrara el siguiente mensaje.
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			prontEstadoConexion.setFont(new Font("Arial", 25));
			prontEstadoConexion.setText("Conectate a la bbdd antes de continuar");
		}
	}

	/**
	 * Metodo que permite abrir la ventana "sobreMiController"
	 *
	 * @param event
	 */
	@FXML
	void verSobreMi(ActionEvent event) {

		nav.verSobreMi();

		Stage myStage = (Stage) this.botonSobreMi.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Envia los datos a la clase DBManager y permite conectarse a esta.
	 *
	 * @param event
	 */
	@FXML
	void enviarDatos(ActionEvent event) {

		JDBC.DBManager.loadDriver(); // Llamada a metodo que permite comprobar que el driver de conexion a la
		// base de datos sea correcto y funcione
		envioDatosBBDD(); // Llamada a metodo que manda los datos de los textField de la ventana hacia la
		// clase DBManager.
		DBManager.conexion(); // Llamada a metodo que permite conectar con la base de datos.
		cbd = new CrearBBDDController();

		if (JDBC.DBManager.isConnected()) {

			if (cbd.chechTables()) {
				
				String userHome = System.getProperty("user.home");
				String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
				String carpetaLibreria = ubicacion + File.separator + "libreria";
				String carpetaBackup = carpetaLibreria + File.separator + obtenerDatoDespuesDeDosPuntos("Database") + File.separator + "backups";

				try {
				    File carpeta_backupsFile = new File(carpetaBackup);
				    if (!carpeta_backupsFile.exists()) {
				        if (carpeta_backupsFile.mkdirs()) {
				            System.out.println("Carpeta de backups creada: " + carpetaBackup);
				        } else {
				            System.out.println("No se pudo crear la carpeta de backups");
				        }
				    } else {
				        System.out.println("La carpeta de backups ya existe: " + carpetaBackup);
				    }
				} catch (Exception e) {
				    e.printStackTrace();
				}
				prontEstadoConexion.setStyle("-fx-background-color: #A0F52D");
				prontEstadoConexion.setText("Conectado");
			}
		} else { // En caso contrario mostrara el siguiente mensaje
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			prontEstadoConexion.setText("ERROR. Los datos son incorrectos. Revise los datos.");
		}

	}

	/**
	 * Funcion que permite mandar los datos a la clase DBManager
	 *
	 * @return
	 */
	public void envioDatosBBDD() {
	    cbd = new CrearBBDDController();
	    String datos[] = new String[5];
	    datos[0] = obtenerDatoDespuesDeDosPuntos("Puerto");
	    datos[1] = obtenerDatoDespuesDeDosPuntos("Database");
	    datos[2] = obtenerDatoDespuesDeDosPuntos("Usuario");
	    datos[3] = obtenerDatoDespuesDeDosPuntos("Password");
	    datos[4] = obtenerDatoDespuesDeDosPuntos("Hosting");

	    DBManager.datosBBDD(datos);
	}

	private String obtenerDatoDespuesDeDosPuntos(String linea) {
	    String userHome = System.getProperty("user.home");
	    String ubicacion = userHome + "\\AppData\\Roaming";
	    String carpetaLibreria = ubicacion + "\\libreria";
	    String archivoConfiguracion = carpetaLibreria + "\\configuracion.conf";

	    try (BufferedReader reader = new BufferedReader(new FileReader(archivoConfiguracion))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.startsWith(linea + ": ")) {
	                return line.substring(linea.length() + 2).trim();
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return "";
	}

	/**
	 * Permite salir completamente del programa.
	 *
	 * @param event
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {

		if (nav.salirPrograma(event)) { // Llamada a metodo que permite salir completamente del programa
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close();
		}
	}
	
    @FXML
    void opcionesPrograma(ActionEvent event) {
		nav.verOpciones();

		Stage myStage = (Stage) this.botonOpciones.getScene().getWindow();
		myStage.close();
    }

	/**
	 * Cierra el programa a la fuerza correctamente.
	 */
	public void closeWindows() { // Metodo que permite cerrar completamente el programa en caso de cerrar a la //
		// fuerza.
		Stage myStage = (Stage) this.botonEnviar.getScene().getWindow();
		myStage.close();
	}
	
    public void crearEstructura() {
        String userHome = System.getProperty("user.home");
        String ubicacion = userHome + "\\AppData\\Roaming";
        String carpetaLibreria = ubicacion + "\\libreria";
        
        String archivoConfiguracion = carpetaLibreria + "\\configuracion.conf";


        
        // Verificar y crear la carpeta "libreria" si no existe
        File carpetaLibreriaFile = new File(carpetaLibreria);
        if (!carpetaLibreriaFile.exists()) {
            carpetaLibreriaFile.mkdir();
            carpetaLibreriaFile.setWritable(true);
        }
        


        // Verificar y crear el archivo "configuracion.conf" si no existe
        File archivoConfiguracionFile = new File(archivoConfiguracion);
        if (!archivoConfiguracionFile.exists()) {
            try {
                archivoConfiguracionFile.createNewFile();

                // Escribir líneas en el archivo
                FileWriter fileWriter = new FileWriter(archivoConfiguracionFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("###############################");
                bufferedWriter.newLine();
                bufferedWriter.write("Fichero de configuracion de la libreria");
                bufferedWriter.newLine();
                bufferedWriter.write("###############################");
                bufferedWriter.newLine();
                bufferedWriter.write("Usuario:");
                bufferedWriter.newLine();
                bufferedWriter.write("Password:");
                bufferedWriter.newLine();
                bufferedWriter.write("Puerto:");
                bufferedWriter.newLine();
                bufferedWriter.write("Database:");
                bufferedWriter.newLine();
                bufferedWriter.write("Hosting:");
                bufferedWriter.newLine();

                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
