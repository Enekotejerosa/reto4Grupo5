package modelo;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Interfaz que define el comportamiento de los elementos que pueden ser reproducidos
 */
public interface Reproducible {
    
    /**
     * Reproduce el elemento, actualizando los componentes de la interfaz de usuario proporcionados.
     * 
     * @param lblAlbumSelec       etiqueta para mostrar el álbum o nombre del elemento que se está reproduciendo.
     * @param lblDuracionSelec    etiqueta para mostrar la duración del elemento.
     * @param btnX05              botón para la velocidad de reproducción 0.5x.
     * @param btnX1               botón para la velocidad de reproducción 1x.
     * @param btnX15              botón para la velocidad de reproducción 1.5x.
     * @return el archivo de audio del elemento que se está reproduciendo
     */
    File reproducir(JLabel lblAlbumSelec, JLabel lblDuracionSelec, JButton btnX05, JButton btnX1, JButton btnX15);
}

