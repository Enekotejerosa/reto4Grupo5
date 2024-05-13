package modelo;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;

public interface Reproducible {
	
	File reproducir(JLabel lblAlbumSelec, JLabel lblDuracionSelec, JButton btnX05, JButton btnX1, JButton btnX15);
}
