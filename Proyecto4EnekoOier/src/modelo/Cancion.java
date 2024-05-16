package modelo;

import java.io.File;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Clase que representa una canción, heredada de Multimedia e implementa la
 * interfaz Reproducible. Contiene información sobre el nombre, ID de audio,
 * duración y archivo de audio de la canción
 */
public class Cancion extends Multimedia implements Reproducible {
	BasedeDatos basededatos = new BasedeDatos();

	/**
	 * Constructor que inicializa una nueva canción con los detalles especificados.
	 * 
	 * @param nombre   nombre de la canción.
	 * @param idAudio  ID del audio.
	 * @param duracion duración de la canción.
	 * @param audio    archivo de audio de la canción.
	 */
	public Cancion(String nombre, int idAudio, String duracion, String audio) {
		super(nombre, idAudio, duracion, audio);
		// TODO Auto-generated constructor stub
	}

	public Cancion() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Devuelve una representación en formato cadena de la canción.
	 * 
	 * @return una cadena que representa la canción
	 */
	@Override
	public String toString() {
		return "Cancion [getNombre()=" + getNombre() + ", getDuracion()=" + getDuracion() + ", getAudio()=" + getAudio()
				+ "]";
	}

	/**
	 * Método que permite reproducir la canción. Actualiza las etiquetas de duración
	 * y reproducción, oculta los botones de velocidad y devuelve el archivo de
	 * audio de la canción.
	 * 
	 * @param lblDuracionSelec      etiqueta para mostrar la duración de la canción.
	 * @param lblReproduciendoSelec etiqueta para mostrar el nombre de la canción
	 *                              que se está reproduciendo.
	 * @param btnX05                botón para ajustar la velocidad de reproducción
	 *                              a 0.5x.
	 * @param btnX1                 botón para ajustar la velocidad de reproducción
	 *                              a 1x.
	 * @param btnX15                botón para ajustar la velocidad de reproducción
	 *                              a 1.5x.
	 * @return archivo de audio de la canción.
	 */
	@Override
	public File reproducir(JLabel lblDuracionSelec, JLabel lblReproduciendoSelec, JButton btnX05, JButton btnX1,
			JButton btnX15) {
		// TODO Auto-generated method stub
		lblDuracionSelec.setText(getDuracion());
		lblReproduciendoSelec.setText(getNombre());
		btnX05.setVisible(false);
		btnX1.setVisible(false);
		btnX15.setVisible(false);
		File file = new File(Paths.get("").toAbsolutePath().toString() + "\\musica\\" + getAudio());
		basededatos.audioReproducido(getIdAudio());
		return file;
	}
}
