package modelo;

import java.util.ArrayList;

/**
 * Clase que representa un álbum musical.
 * Contiene información sobre el título, año de lanzamiento, género,
 * imagen asociada y una lista de canciones.
 */
public class Album {
    private String titulo;
    private int anyo;
    private String Genero;
    private String imagen;
    private ArrayList<Cancion> canciones = new ArrayList<Cancion>();
    private int id;
    /**
     * Obtiene el título del álbum.
     * 
     * @return el título del álbum.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del álbum.
     * 
     * @param titulo el título del álbum.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el año de lanzamiento del álbum.
     * 
     * @return el año de lanzamiento del álbum.
     */
    public int getAnyo() {
        return anyo;
    }

    /**
     * Establece el año de lanzamiento del álbum.
     * 
     * @param anyo el año de lanzamiento del álbum.
     */
    public void setAnyo(int anyo) {
        this.anyo = anyo;
    }

    /**
     * Obtiene el género del álbum.
     * 
     * @return el género del álbum.
     */
    public String getGenero() {
        return Genero;
    }

    /**
     * Establece el género del álbum.
     * 
     * @param genero el género del álbum.
     */
    public void setGenero(String genero) {
        Genero = genero;
    }

    /**
     * Obtiene la imagen asociada al álbum.
     * 
     * @return la imagen del álbum.
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen asociada al álbum.
     * 
     * @param imagen la imagen del álbum.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Obtiene la lista de canciones del álbum.
     * 
     * @return la lista de canciones.
     */
    public ArrayList<Cancion> getCanciones() {
        return canciones;
    }

    /**
     * Establece la lista de canciones del álbum.
     * 
     * @param canciones la lista de canciones.
     */
    public void setCanciones(ArrayList<Cancion> canciones) {
        this.canciones = canciones;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
     * Constructor que inicializa un nuevo álbum con los detalles especificados
     * 
     * @param titulo     título del álbum.
     * @param anyo       año de lanzamiento del álbum.
     * @param genero     género del álbum.
     * @param imagen     imagen del álbum.
     * @param canciones  lista de canciones del álbum.
     */
    public Album(String titulo, int anyo, String genero, String imagen, ArrayList<Cancion> canciones, int id) {
		super();
		this.titulo = titulo;
		this.anyo = anyo;
		Genero = genero;
		this.imagen = imagen;
		this.canciones = canciones;
		this.id = id;
	}

	/**
     * Constructor por defecto.
     */
    public Album() {
    }
    
    public Album(String titulo, int anyo, String genero) {
		this.titulo = titulo;
		this.anyo = anyo;
		Genero = genero;
	}
	/**
     * Devuelve una representación en formato cadena del álbum.
     * 
     * @return una cadena que representa el álbum.
     */
	@Override
	public String toString() {
		return "Album [titulo=" + titulo + ", anyo=" + anyo + ", Genero=" + Genero + ", imagen=" + imagen
				+ ", canciones=" + canciones + ", id=" + id + "]";
	}

  
}
