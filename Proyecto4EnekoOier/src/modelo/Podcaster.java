package modelo;

import java.util.ArrayList;

/**
 * Clase que representa a un Podcaster, que es un tipo de Artista. Contiene
 * información sobre el género del Podcaster y una lista de podcasts.
 */
public class Podcaster extends Artista {

	private String genero;
	private ArrayList<Podcast> podcasts = new ArrayList<Podcast>();

	/**
	 * Constructor que inicializa un nuevo Podcaster con los detalles especificados.
	 * 
	 * @param nombreArtista nombre del artista.
	 * @param descripcion   descripción del artista.
	 * @param imagen        imagen asociada al artista.
	 * @param genero        género del Podcaster.
	 * @param podcasts      lista de podcasts del Podcaster.
	 */
	public Podcaster(String nombreArtista, String descripcion, String imagen, String genero,
			ArrayList<Podcast> podcasts) {
		super(nombreArtista, descripcion, imagen);
		this.genero = genero;
		this.podcasts = podcasts;
	}

	/**
	 * Constructor por defecto.
	 */
	public Podcaster() {
	}

	/**
	 * Obtiene el género del Podcaster.
	 * 
	 * @return género del Podcaster.
	 */
	public String getGenero() {
		return genero;
	}

	/**
	 * Establece el género del Podcaster.
	 * 
	 * @param genero género del Podcaster.
	 */
	public void setGenero(String genero) {
		this.genero = genero;
	}

	/**
	 * Obtiene la lista de podcasts del Podcaster.
	 * 
	 * @return la lista de podcasts.
	 */
	public ArrayList<Podcast> getPodcasts() {
		return podcasts;
	}

	/**
	 * Establece la lista de podcasts del Podcaster.
	 * 
	 * @param podcasts la lista de podcasts.
	 */
	public void setPodcasts(ArrayList<Podcast> podcasts) {
		this.podcasts = podcasts;
	}

	/**
	 * Devuelve una representación en formato cadena del Podcaster.
	 * 
	 * @return una cadena que representa al Podcaster.
	 */
	@Override
	public String toString() {
		return "Podcaster [genero=" + genero + ", podcasts=" + podcasts + ", getNombreArtista()=" + getNombreArtista()
				+ ", getDescripcion()=" + getDescripcion() + "]";
	}
}
