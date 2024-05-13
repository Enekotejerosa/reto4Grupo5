package modelo;

import java.io.File;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Podcast extends Multimedia implements Reproducible {
	private String colaboradores;
	BasedeDatos basededatos = new BasedeDatos();

	public Podcast(String nombre, int idAudio, String duracion, String audio, String colaboradores) {
		super(nombre, idAudio, duracion, audio);
		// TODO Auto-generated constructor stub
		this.colaboradores = colaboradores;
	}

	public String getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(String colaboradores) {
		this.colaboradores = colaboradores;
	}

	@Override
	public String toString() {
		return "Podcast [nombre=" + getNombre() + ",Duracion=" + getDuracion() + ", Audio=" + getAudio()
				+ ", colaboradores = " + colaboradores + "]";
	}

	@Override
	public File reproducir(JLabel lblDuracionSelec, JLabel lblReproduciendoSelec, JButton btnX05, JButton btnX1,
			JButton btnX15) {
		// TODO Auto-generated method stub
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
