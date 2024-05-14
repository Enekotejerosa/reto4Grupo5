package modelo;

/**
 * Clase abstracta que representa a un artista. Contiene información sobre el
 * nombre del artista, una descripción y una imagen asociada.
 */
public abstract class Artista {

	private String nombreArtista;
	private String descripcion;
	private String imagen;

	/**
	 * Constructor que inicializa un nuevo artista con los detalles especificados.
	 * 
	 * @param nombreArtista el nombre del artista.
	 * @param descripcion   una descripción del artista.
	 * @param imagen        una imagen asociada al artista.
	 */
	public Artista(String nombreArtista, String descripcion, String imagen) {
		this.nombreArtista = nombreArtista;
		this.descripcion = descripcion;
		this.imagen = imagen;
	}

	/**
	 * Obtiene el nombre del artista.
	 * 
	 * @return nombre del artista.
	 */
	public String getNombreArtista() {
		return nombreArtista;
	}

	/**
	 * Establece el nombre del artista.
	 * 
	 * @param nombreArtista nombre del artista.
	 */
	public void setNombreArtista(String nombreArtista) {
		this.nombreArtista = nombreArtista;
	}

	/**
	 * Obtiene la descripción del artista.
	 * 
	 * @return descripción del artista.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Establece la descripción del artista.
	 * 
	 * @param descripcion descripción del artista.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Obtiene la imagen asociada al artista.
	 * 
	 * @return imagen del artista.
	 */
	public String getImagen() {
		return imagen;
	}

	/**
	 * Establece la imagen asociada al artista.
	 * 
	 * @param imagen imagen del artista.
	 */
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	/**
	 * Constructor por defecto.
	 */
	public Artista() {
	}
}
