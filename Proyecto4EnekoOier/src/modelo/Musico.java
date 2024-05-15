package modelo;

import java.util.ArrayList;

/**
 * Clase que representa un músico, que es un tipo de artista. Contiene
 * información sobre las características del músico y una lista de álbumes.
 */
public class Musico extends Artista {
	private Caracteristica caracteristica;
	private ArrayList<Album> albumes = new ArrayList<Album>();

	/**
	 * Constructor que inicializa un nuevo músico con los detalles especificados.
	 * 
	 * @param nombreArtista  nombre del artista.
	 * @param descripcion    descripción del artista.
	 * @param imagen         imagen asociada al artista.
	 * @param caracteristica característica del músico.
	 * @param albumes        lista de álbumes del músico.
	 */
	public Musico(String nombreArtista, String descripcion, String imagen, Caracteristica caracteristica,
			ArrayList<Album> albumes, int id) {
		super(nombreArtista, descripcion, imagen, id);
		this.caracteristica = caracteristica;
		this.albumes = albumes;
	}

	/**
	 * Constructor por defecto.
	 */
	public Musico() {
	}

	/**
	 * Obtiene la lista de álbumes del músico.
	 * 
	 * @return lista de álbumes.
	 */
	public ArrayList<Album> getAlbumes() {
		return albumes;
	}

	/**
	 * Establece la lista de álbumes del músico.
	 * 
	 * @param albumes lista de álbumes.
	 */
	public void setAlbumes(ArrayList<Album> albumes) {
		this.albumes = albumes;
	}

	/**
	 * Obtiene la característica del músico.
	 * 
	 * @return característica del músico
	 */
	public Caracteristica getCaracteristica() {
		return caracteristica;
	}

	/**
	 * Establece la característica del músico.
	 * 
	 * @param caracteristica característica del músico.
	 */
	public void setCaracteristica(Caracteristica caracteristica) {
		this.caracteristica = caracteristica;
	}

	/**
	 * Devuelve una representación en formato cadena del músico.
	 * 
	 * @return una cadena que representa al músico
	 */
	@Override
	public String toString() {
		return "Musico [caracteristica=" + caracteristica + ", albumes=" + albumes + ", getId()=" + getId()
				+ ", getNombreArtista()=" + getNombreArtista() + ", getDescripcion()=" + getDescripcion() + "]";
	}

}
