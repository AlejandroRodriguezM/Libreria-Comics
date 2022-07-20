package Funcionamiento;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xlsx,CSV
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Introducir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *
 *  Esta clase permite darle forma a un comic
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.util.Random;

public class Comic {

	protected String ID;
	protected String nombre;
	protected String numero;
	protected String variante;
	protected String firma;
	protected String editorial;
	protected String formato;
	protected String procedencia;
	protected String fecha;
	protected String guionista;
	protected String dibujante;
	protected String estado;

	// Constructor
	public Comic(String ID, String nombre, String numero, String variante, String firma, String editorial,
			String formato, String procedencia, String fecha, String guionista, String dibujante, String estado) {
		this.ID = ID;
		this.nombre = nombre;
		this.numero = numero;
		this.variante = variante;
		this.firma = firma;
		this.editorial = editorial;
		this.formato = formato;
		this.procedencia = procedencia;
		this.fecha = fecha;
		this.guionista = guionista;
		this.dibujante = dibujante;
		this.estado = estado;
	}

	// Constructor
	public Comic() {
		this.ID = "";
		this.nombre = "";
		this.numero = "";
		this.variante = "";
		this.firma = "";
		this.editorial = "";
		this.formato = "";
		this.procedencia = "";
		this.fecha = "";
		this.guionista = "";
		this.dibujante = "";
		this.estado = "";
	}

	// Getters y setters

	public String getID() {
		return ID;
	}

	public String getNombre() {
		return nombre;
	}

	public String getNumero() {
		return numero;
	}

	public String getVariante() {
		return variante;
	}

	public String getFirma() {
		return firma;
	}

	public String getEditorial() {
		return editorial;
	}

	public String getFormato() {
		return formato;
	}

	public String getProcedencia() {
		return procedencia;
	}

	public String getFecha() {
		return fecha;
	}

	public String getGuionista() {
		return guionista;
	}

	public String getDibujante() {
		return dibujante;
	}

	public String getEstado() {
		return estado;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setVariante(String variante) {
		this.variante = variante;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public void setGuionista(String guionista) {
		this.guionista = guionista;
	}

	public void setDibujante(String dibujante) {
		this.dibujante = dibujante;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Frases de personajes de comics
	 * @return
	 */
	public static String frasesComics() {

		Random r = new Random();
		int n;

		String frases[] = { "O sostienes el latigo, o recibes los latigazos.\n-Magneto",
				"Hay belleza en todo, hast en la muerte. Pero no todos son capaces de verlo.\n-Vision",
				"Blasfemar es el intento de una mente fragil por expresarse violentamente.\n-El Acertijo",
				"Esto es la mesa de operaciones y yo soy el cirujano.\n-Batman",
				"Afrontalo tigre, te acaba de tocar la loteria.\n-Mary Jane Watson",
				"Si he de tener un pasado, prefiero que sea de opcion multiple.\n-Joker",
				"Si eres culpable, estais muerto.\n-Punisher",
				"Soy el mejor en lo que hago... Y lo que hago no es agradable.\n-Wolverine",
				"El poder absoluto corrompe de manera absoluta.\n-Charles Xavier",
				"Cuando ellos griten, salvanos, yo susurrare, no.\n-Rorschac",
				"La existencia de la vida es un fenomeno altamente sobrevalorado.\n-Dr. Manhatan",
				"No son los dioses los que deciden si el hombre existe; son los hombres los que deciden si los dioses existen.\n-Thor",
				"Un hombre sin esperanza es un hombre sin miedo.\n-Wilson Fisk",
				"No tiene nada de malo sentir miedo, siempre y cuando no te dejes vencer\n-Capitan America",
				"Cualquier sueño que merezca ser vivido es un sueño por el que merece la pena luchar \n-Charles Xavier",
				"Tuviste una vida dura,  sabes quién más tuvo una vida dura? ¡Todo el mundo! \n-DeadPool",
				"Planeamos días y días y cuando llega el momento, procedemos a improvisar \n-QuickSilver" };
		n = (int) (Math.random() * r.nextInt(frases.length));

		return frases[n];
	}

	@Override
	public String toString() {
		return "\nNombre: " + nombre + "\nNumero: " + numero + "\nVariante: " + variante + "\nFirma: " + firma
				+ "\nEditorial: " + editorial + "\nFormato: " + formato + "\nProcedencia: " + procedencia + "\nFecha: "
				+ fecha + "\nGuionista: " + guionista + "\nDibujante: " + dibujante + "\nEstado: " + estado;
	}

}
