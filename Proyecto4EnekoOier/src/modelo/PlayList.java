package modelo;

import java.util.ArrayList;

/**
 * Clase que representa una lista de reproducción (PlayList). Contiene
 * información sobre el título, fecha de registro, una lista de canciones y un
 * ID de la lista de reproducción.
 */
public class PlayList {

	private String titulo;
	private String fec_registro;
	private ArrayList<Cancion> canciones = new ArrayList<Cancion>();
	private int idPlayList;

	/**
	 * Obtiene el título de la lista de reproducción.
	 * 
	 * @return el título de la lista de reproducción.
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * Establece el título de la lista de reproducción.
	 * 
	 * @param titulo el título de la lista de reproducción.
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * Obtiene la fecha de registro de la lista de reproducción.
	 * 
	 * @return la fecha de registro de la lista de reproducción.
	 */
	public String getFec_registro() {
		return fec_registro;
	}

	/**
	 * Establece la fecha de registro de la lista de reproducción.
	 * 
	 * @param fec_registro fecha de registro de la lista de reproducción.
	 */
	public void setFec_registro(String fec_registro) {
		this.fec_registro = fec_registro;
	}

	/**
	 * Obtiene la lista de canciones de la lista de reproducción.
	 * 
	 * @return lista de canciones.
	 */
	public ArrayList<Cancion> getCanciones() {
		return canciones;
	}

	/**
	 * Establece la lista de canciones de la lista de reproducción.
	 * 
	 * @param canciones lista de canciones.
	 */
	public void setCanciones(ArrayList<Cancion> canciones) {
		this.canciones = canciones;
	}

	/**
	 * Obtiene el ID de la lista de reproducción.
	 * 
	 * @return ID de la lista de reproducción.
	 */
	public int getIdPlayList() {
		return idPlayList;
	}

	/**
	 * Establece el ID de la lista de reproducción.
	 * 
	 * @param idPlayList ID de la lista de reproducción.
	 */
	public void setIdPlayList(int idPlayList) {
		this.idPlayList = idPlayList;
	}

	/**
	 * Constructor que inicializa una nueva lista de reproducción con los detalles
	 * especificados.
	 * 
	 * @param titulo       título de la lista de reproducción.
	 * @param fec_registro fecha de registro de la lista de reproducción.
	 * @param canciones    lista de canciones de la lista de reproducción.
	 * @param idPlayList   ID de la lista de reproducción.
	 */
	public PlayList(String titulo, String fec_registro, ArrayList<Cancion> canciones, int idPlayList) {
		super();
		this.titulo = titulo;
		this.fec_registro = fec_registro;
		this.canciones = canciones;
		this.idPlayList = idPlayList;
	}

	/**
	 * Constructor que inicializa una nueva lista de reproducción con los detalles
	 * especificados, excluyendo la lista de canciones y el ID.
	 * 
	 * @param titulo       título de la lista de reproducción.
	 * @param fec_registro fecha de registro de la lista de reproducción.
	 */
	public PlayList(String titulo, String fec_registro) {
		this.titulo = titulo;
		this.fec_registro = fec_registro;
	}

	/**
	 * Constructor por defecto.
	 */
	public PlayList() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Devuelve una representación en formato cadena de la lista de reproducción.
	 * 
	 * @return una cadena que representa la lista de reproducción.
	 */
	@Override
	public String toString() {
		return "PlayList [titulo=" + titulo + ", fec_registro=" + fec_registro + ", canciones=" + canciones
				+ ", idPlayList=" + idPlayList + "]";
	}
}
