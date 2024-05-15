package modelo;

import java.io.File;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Clase que representa un podcast. Extiende la clase Multimedia e implementa la
 * interfaz Reproducible.
 */
public class Podcast extends Multimedia implements Reproducible {
	/** Colaboradores del podcast */
	private String colaboradores;
	/** Objeto para acceder a la base de datos */
	BasedeDatos basededatos = new BasedeDatos();

	/**
	 * Constructor de la clase Podcast.
	 * 
	 * @param nombre        Nombre del podcast.
	 * @param idAudio       Identificador del audio.
	 * @param duracion      Duración del podcast.
	 * @param audio         Archivo de audio del podcast.
	 * @param colaboradores Colaboradores del podcast.
	 */
	public Podcast(String nombre, int idAudio, String duracion, String audio, String colaboradores) {
		super(nombre, idAudio, duracion, audio);
		this.colaboradores = colaboradores;
	}

	/**
	 * Método para obtener los colaboradores del podcast.
	 * 
	 * @return colaboradores del podcast.
	 */
	public String getColaboradores() {
		return colaboradores;
	}

	/**
	 * Método para establecer los colaboradores del podcast.
	 * 
	 * @param colaboradores Los colaboradores del podcast.
	 */
	public void setColaboradores(String colaboradores) {
		this.colaboradores = colaboradores;
	}

	/**
	 * Método toString que devuelve la representación en forma de cadena del objeto
	 * Podcast.
	 * 
	 * @return Representación en forma de cadena del objeto Podcast.
	 */
	@Override
	public String toString() {
		return "Podcast [nombre=" + getNombre() + ", Duracion=" + getDuracion() + ", Audio=" + getAudio()
				+ ", colaboradores=" + colaboradores + "]";
	}

	/**
	 * Método para reproducir el podcast
	 * 
	 * @param lblDuracionSelec      Etiqueta para mostrar la duración seleccionada.
	 * @param lblReproduciendoSelec Etiqueta para mostrar el podcast que se está
	 *                              reproduciendo.
	 * @param btnX05                Botón para reproducir a 0.5x la velocidad
	 *                              normal.
	 * @param btnX1                 Botón para reproducir a 1x la velocidad normal.
	 * @param btnX15                Botón para reproducir a 1.5x la velocidad
	 *                              normal.
	 * @return Archivo de audio del podcast
	 */
	@Override
	public File reproducir(JLabel lblDuracionSelec, JLabel lblReproduciendoSelec, JButton btnX05, JButton btnX1,
			JButton btnX15) {
		lblDuracionSelec.setText(getDuracion());
		lblReproduciendoSelec.setText(getNombre());
		btnX05.setVisible(true);
		btnX1.setVisible(true);
		btnX15.setVisible(true);
		File file = new File(Paths.get("").toAbsolutePath().toString() + "\\musica\\" + getAudio());
		basededatos.audioReproducido(getIdAudio());
		return file;
	}

}
