package Programa;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC
 * con mySql Las ventanas graficas se realizan con JavaFX. El programa permite:
 * - Conectarse a la base de datos. - Ver la base de datos completa o parcial
 * segun parametros introducidos. - Guardar el contenido de la base de datos en
 * un fichero .txt y .xlsx,CSV - Copia de seguridad de la base de datos en
 * formato .sql - Introducir comics a la base de datos. - Modificar comics de la
 * base de datos. - Eliminar comics de la base de datos(Solamente cambia el
 * estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos
 * no los muestran el programa - Ver frases de personajes de comics - Opcion de
 * escoger algo para leer de forma aleatoria.
 *
 * Esta clase permite acceder a la base de datos introduciendo los diferentes
 * datos que nos pide.
 *
 * Version Final
 *
 * Por Alejandro Rodriguez
 *
 * Twitter: @silverAlox
 */

public class Principal {

	public static void main(String[] args) {

		Programa.main(args);

	}

}
