package comicManagement;

/**
 * Esta clase se encargará de la lógica de negocio, como la validación de
 * campos, la generación de códigos únicos
 */
public class ComicBusinessLogic {
	
	/**
	 * Verifica si se ha encontrado información válida para el cómic.
	 *
	 * @param comicInfo Un arreglo de strings con información del cómic.
	 * @return True si la información es válida y existe; de lo contrario, False.
	 */
	private boolean comprobarCodigo(String[] comicInfo) {
		boolean existe = true;
		if (comicInfo == null || comicInfo.length <= 0) {
			existe = false;
		}
		return existe;
	}

}
