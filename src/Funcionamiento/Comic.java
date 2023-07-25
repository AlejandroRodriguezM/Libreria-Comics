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
 *  - Puntuar comics que se encuentren dentro de la base de datos.
 *  Esta clase permite acceder al menu principal donde se puede viajar a diferentes ventanas, etc.
 *
 *  Version 5.3
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.util.Random;

/**
 * Esta clase objeto sirve para dar forma al comic que estara dentro de la base
 * de datos.
 *
 * @author Alejandro Rodriguez
 */
public class Comic {

	protected String ID;
	protected String nombre;
	protected String numCaja;
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
	protected String puntuacion;
	protected String imagen;
	

	// Constructor
	public Comic(String ID, String nombre,String numCaja, String numero, String variante, String firma, String editorial,
			String formato, String procedencia, String fecha, String guionista, String dibujante, String estado,
			String puntuacion, String imagen) {
		this.ID = ID;
		this.nombre = nombre;
		this.numCaja = numCaja;
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
		this.puntuacion = puntuacion;
		this.imagen = imagen;
		
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
		this.puntuacion = "";
		this.imagen = null;
		this.numCaja = "";
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

	public String getPuntuacion() {
		return puntuacion;
	}
	
	public String getNumCaja() {
		return numCaja;
	}

	public void setPuntuacion(String puntuacion) {
		this.puntuacion = puntuacion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
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
	
	public void setNumCaja(String numCaja) {
		this.numCaja = numCaja;
	}

	/**
	 * Frases de personajes de comics
	 *
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
				"Cualquier sueno que merezca ser vivido es un sue�o por el que merece la pena luchar \n-Charles Xavier",
				"Tuviste una vida dura,  sabes quien mas tuvo una vida dura? Todo el mundo! \n-DeadPool",
				"Planeamos dias y dias y cuando llega el momento, procedemos a improvisar \n-QuickSilver",
				"No es quién soy debajo, sino lo que hago lo que me define. \n-Batman",
			    "Un gran poder conlleva una gran responsabilidad. \n-Tío Ben, Spider\n-Man",
			    "¡Hulk aplasta! \n-Hulk",
			    "¡Yo soy el mejor en lo que hago, y lo que hago no es agradable! \n-Wolverine",
			    "La verdad, la justicia y el estilo de vida americano. \n-Superman",
			    "Soy el vengador más poderoso del mundo. \n-Thor",
			    "Soy el murciélago de la noche, no el murciélago de la luz del día. \n-Batman",
			    "Con grandes poderes, vienen grandes responsabilidades. \n-Spider\n-Man",
			    "¡Vengadores, uníos! \n-Capitán América",
			    "La única manera de hacer lo imposible posible es creer que es posible. \n-Alicia en el País de las Maravillas",
			    "Yo soy Iron Man. \n-Iron Man",
			    "La noche es más oscura justo antes del amanecer. \n-Batman",
			    "¡Es hora de cortar la cabeza de la serpiente! \n-Wonder Woman",
			    "¡Por Asgard! \n-Thor",
			    "Nunca subestimes el poder de la fuerza de voluntad de un Jedi. \n-Luke Skywalker",
			    "Con gran poder viene una gran responsabilidad, y una gran necesidad de una gran pizza. \n-Deadpool",
			    "¡Nunca te rindas, nunca te rindas, nunca te rindas! \n-Capitán América",
			    "La esperanza nunca muere. \n-Green Lantern",
			    "Puedo hacer esto todo el día. \n-Capitán América",
			    "El crimen no paga, a menos que seas bueno en ello. \n-Batman",
			    "Tengo una misión que cumplir, y lo haré con o sin su ayuda. \n-Megamente",
			    "¡Yo soy el mejor en lo que hago! \n-X\n-Men",
			    "La justicia es ciega, pero yo no lo soy. \n-Daredevil",
			    "Mi sentido arácnido está zumbando. \n-Spider\n-Man",
			    "No me digas lo que no puedo hacer. \n-Lost",
			    "¡Seré el héroe que necesitan! \n-Batman",
			    "Con la gran capacidad, viene una gran responsabilidad. \n-Storm",
			    "¿Soy tu amigo? ¡Soy tu padre! \n-Star Wars",
			    "La grandeza de una civilización se mide por la forma en que trata a sus miembros más débiles. \n-X\n-Men",
			    "No se trata de quién soy debajo, sino de lo que hago lo que me define. \n-Iron Man",
			    "El poder sin control es peligroso. \n-Green Lantern",
			    "¡El hombre de acero no se rinde! \n-Superman",
			    "Hasta el más mínimo gesto de valentía es capaz de hacer la diferencia. \n-Peter Parker, Spider\n-Man",
			    "¿Quién necesita una identidad secreta cuando puedes tener una buena mascara? \n-Iron Man",
			    "El valor es la magia que convierte los sueños en realidad. \n-Walt Disney",
			    "La lucha es lo que te define. \n-Green Arrow"};
		n = (int) (Math.random() * r.nextInt(frases.length));

		return frases[n];
	}
	
	private void appendIfNotEmpty(StringBuilder builder, String label, String value) {
	    if (value != null && !value.isEmpty()) {
	        builder.append(label).append(": ").append(value).append("\n");
	    }
	}

	@Override
	public String toString() {
	    StringBuilder contenidoComic = new StringBuilder();

	    appendIfNotEmpty(contenidoComic, "ID", ID);
	    appendIfNotEmpty(contenidoComic, "Nombre", nombre);
	    appendIfNotEmpty(contenidoComic, "Numero", numero);
	    appendIfNotEmpty(contenidoComic, "Variante", variante);
	    appendIfNotEmpty(contenidoComic, "Firma", firma);
	    appendIfNotEmpty(contenidoComic, "Editorial", editorial);
	    appendIfNotEmpty(contenidoComic, "Formato", formato);
	    appendIfNotEmpty(contenidoComic, "Procedencia", procedencia);
	    appendIfNotEmpty(contenidoComic, "Fecha", fecha);
	    appendIfNotEmpty(contenidoComic, "Guionista", guionista);
	    appendIfNotEmpty(contenidoComic, "Dibujante", dibujante);
	    appendIfNotEmpty(contenidoComic, "Puntuacion", puntuacion);
	    appendIfNotEmpty(contenidoComic, "Estado", estado);
	    appendIfNotEmpty(contenidoComic, "Caja", numCaja);

	    return contenidoComic.toString();
	}


}
