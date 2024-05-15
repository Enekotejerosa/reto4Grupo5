package modelo;

/**
 * Clase abstracta que representa un elemento multimedia. Contiene información
 * sobre el nombre, ID de audio, duración y archivo de audio del elemento.
 */
public abstract class Multimedia {
	private String nombre;
	private int idAudio;
	private String duracion;
	private String audio;

	/**
	 * Constructor que inicializa un nuevo elemento multimedia con los detalles
	 * especificados.
	 * 
	 * @param nombre   nombre del elemento multimedia.
	 * @param idAudio  ID del audio.
	 * @param duracion duración del elemento multimedia.
	 * @param audio    archivo de audio del elemento multimedia.
	 */
	public Multimedia(String nombre, int idAudio, String duracion, String audio) {
		this.nombre = nombre;
		this.idAudio = idAudio;
		this.duracion = duracion;
		this.audio = audio;
	}

	/**
	 * Obtiene el nombre del elemento multimedia.
	 * 
	 * @return nombre del elemento multimedia.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre del elemento multimedia.
	 * 
	 * @param nombre nombre del elemento multimedia.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Obtiene la duración del elemento multimedia.
	 * 
	 * @return duración del elemento multimedia.
	 */
	public String getDuracion() {
		return duracion;
	}

	/**
	 * Establece la duración del elemento multimedia
	 * 
	 * @param duracion duración del elemento multimedia
	 */
	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	/**
	 * Obtiene el archivo de audio del elemento multimedia.
	 * 
	 * @return archivo de audio del elemento multimedia.
	 */
	public String getAudio() {
		return audio;
	}

	/**
	 * Establece el archivo de audio del elemento multimedia.
	 * 
	 * @param audio archivo de audio del elemento multimedia.
	 */
	public void setAudio(String audio) {
		this.audio = audio;
	}

	/**
	 * Obtiene ID del audio.
	 * 
	 * @return ID del audio.
	 */
	public int getIdAudio() {
		return idAudio;
	}

	/**
	 * Establece el ID del audio.
	 * 
	 * @param idAudio el ID del audio.
	 */
	public void setIdAudio(int idAudio) {
		this.idAudio = idAudio;
	}

	/**
	 * Devuelve una representación en formato cadena del elemento multimedia.
	 * 
	 * @return una cadena que representa el elemento multimedia.
	 */
	@Override
	public String toString() {
		return "Multimedia [nombre=" + nombre + ", idAudio=" + idAudio + ", duracion=" + duracion + ", audio=" + audio
				+ "]";
	}
}
