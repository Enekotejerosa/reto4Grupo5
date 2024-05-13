package modelo;

import java.io.File;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Cancion extends Multimedia implements Reproducible {
	BasedeDatos basededatos = new BasedeDatos();

	public Cancion(String nombre, int idAudio, String duracion, String audio) {
		super(nombre, idAudio, duracion, audio);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Cancion [getNombre()=" + getNombre() + ", getDuracion()=" + getDuracion() + ", getAudio()=" + getAudio()
				+ "]";
	}

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
