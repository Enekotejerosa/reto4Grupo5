
package controlador;

import java.awt.CardLayout;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import modelo.Usuarios;
import modelo.BasedeDatos;
import modelo.Cancion;
import modelo.PlayList;
import modelo.Podcast;

public class Metodos {
	BasedeDatos basededatos = new BasedeDatos();

	/**
	 * Cambia de capa de layeredPane
	 * 
	 * @param nombreLayeredPane
	 * @param identificadorCapa
	 */
	public void cambiardePantalla(JLayeredPane nombreLayeredPane, String identificadorCapa) {

		// CARDLAYOUT PARA MANEJAR LOS DISTINTOS CONTENIDOS
		CardLayout cardLayout = (CardLayout) nombreLayeredPane.getLayout();

		// CAMBIA LA VISIBILIDAD DE LAS CAPAS EN EL JLayeredPane
		cardLayout.show(nombreLayeredPane, identificadorCapa);
	}

	/**
	 * Cambia de imagen entre Artistas 1 y 2 en el login con un Timer
	 * 
	 * @param lblImagenesArtistas
	 * @param imagenArtistas1
	 * @param imagenArtistas2
	 */
	public void cambiarImagenArtistas(JLabel lblImagenesArtistas, String imagenArtistas1, String imagenArtistas2) {
		Timer timer = new Timer();

		TimerTask task = new TimerTask() {
			boolean primeraImagen = true;

			public void run() {
				// Alternamos entre las dos imágenes
				if (primeraImagen) {

					lblImagenesArtistas
							.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + imagenArtistas1));
				} else {
					lblImagenesArtistas
							.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + imagenArtistas2));
				}
				primeraImagen = !primeraImagen; // Cambiamos el estado para la próxima ejecución
			}
		};

		// Programamos la tarea para que se ejecute cada 2 segundos y se repita
		// continuamente
		timer.schedule(task, 0, 2000);
	}

	/**
	 * Crea un objeto usuario nuevo dependiendo de si es premium o no, comprueba que
	 * el usuario no exista todavia, si todos los campos esten rellenos y si las
	 * contraseñas coinciden, si todo va bien se registrara el nuevo usuario en la
	 * base de datos y si es premium o no, si hubiese algun fallo sacaria un
	 * JOptionpane
	 * 
	 * @param nombre          el nombre introducido en el JTextFiel del panel de
	 *                        registrar
	 * @param apellido        el apellido introducido en el JTextFiel del panel de
	 *                        registrar
	 * @param usuario         el usuario introducido en el JTextFiel del panel de
	 *                        registrar
	 * @param contrasena      La contraseña introducido en el JPasswordField del
	 *                        panel de registrar
	 * @param conficontrasena La contraseña introducida de nuevo en otro
	 *                        JPasswordField del panel de registrar
	 * @param fecNac          la fecha de nacimiento introducido en el JTextFiel del
	 *                        panel de registrar
	 * @param fecReg          La fecha en la que se realiza el registro
	 * @param fecPremium      La fecha en la cual se acaba el premium
	 * @param premium         da informacion sobre si ese usuario nuevo es premium o
	 *                        no
	 * @return true si el registro se ha completado correctamente o false si ha
	 *         habido algun error
	 */
	public boolean registrarUsuario(String nombre, String apellido, String usuario, String contrasena,
			String conficontrasena, String fecNac, String fecReg, String fecPremium, boolean premium) {
		// TODO Auto-generated method stub
		Usuarios usuarionuevo = new Usuarios();
		if (premium) {
			Usuarios nuevo_usuario = new Usuarios(nombre, apellido, usuario, contrasena, fecNac, fecReg, "premium");
			usuarionuevo = nuevo_usuario;
		} else {
			Usuarios nuevo_usuario = new Usuarios(nombre, apellido, usuario, contrasena, fecNac, fecReg, "free");
			usuarionuevo = nuevo_usuario;
		}
		boolean error = basededatos.comprobarUsuarios(usuarionuevo.getUsuario());
		if (error) {
			JOptionPane.showMessageDialog(null, "Usuario repetido, ponga otro");
		}
		if (!contrasena.equals(conficontrasena)) {
			error = true;
			JOptionPane.showMessageDialog(null, "Las contraseñas son distintas");
		}
		if (nombre.isBlank() || apellido.isBlank() || usuario.isBlank() || contrasena.isBlank() || fecNac.isBlank()) {
			error = true;
			JOptionPane.showMessageDialog(null, "Todos los campos deben estar rellenados");
		}
		if (premium && !error) {
			basededatos.insertarUsuario(usuarionuevo, fecPremium);
			JOptionPane.showMessageDialog(null, "Usuario premium creado correctamente");
			return true;
		} else if (!premium && !error) {
			basededatos.insertarUsuario(usuarionuevo, fecPremium);
			JOptionPane.showMessageDialog(null, "Usuario free creado correctamente");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Borra todos los elementos de un panel
	 * 
	 * @param panel es el panel enviado
	 */
	public void borrarPanel(JPanel panel) {
		// TODO Auto-generated method stub
		Component[] componentes = panel.getComponents();
		for (Component componente : componentes) {
			panel.remove(componente);
		}
		panel.revalidate();
		panel.repaint();

	}

	/**
	 * Crea una nueva lista de reproducción y la añade al usuario iniciado.
	 * 
	 * @param usuarioIniciado usuario que está iniciado en el sistema.
	 * @param listaPlaylist   lista de reproducción donde se añadirá el nombre de la
	 *                        nueva lista.
	 * @return El usuario actualizado con la nueva lista de reproducción.
	 */
	public Usuarios crearPlayList(Usuarios usuarioIniciado, JList<String> listaPlaylist) {
		// TODO Auto-generated method stub
		String nombreLista = JOptionPane.showInputDialog("Por favor, introduce un texto:");
		System.out.println(nombreLista);
		boolean error = false;
		for (int i = 0; (i != usuarioIniciado.getPlaylists().size()) && error == false; i++) {
			if (nombreLista.equalsIgnoreCase(usuarioIniciado.getPlaylists().get(i).getTitulo())) {
				JOptionPane.showMessageDialog(null, "Ya hay una PlayList creada con el nombre de " + nombreLista);
				error = true;
			}
		}
		if (!error) {
			basededatos.insertarNuevaPlayList(usuarioIniciado, nombreLista, false, null);
			DefaultListModel<String> model = (DefaultListModel<String>) listaPlaylist.getModel();
			model.addElement(nombreLista);
			LocalDate fecha_creacion = LocalDate.now();
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String diaString = fecha_creacion.format(formato);
			usuarioIniciado.getPlaylists().add(new PlayList(nombreLista, diaString));
		}
		return usuarioIniciado;
	}

	/**
	 * Exporta una lista de reproducción a un archivo CSV.
	 * 
	 * @param usuarioIniciado      usuario que está iniciado en el sistema.
	 * @param posicionSeleccionada posición de la lista de reproducción
	 *                             seleccionada.
	 */
	public void exportarPlayList(Usuarios usuarioIniciado, int posicionSeleccionada) {
		// TODO Auto-generated method stub
		PlayList playlist = usuarioIniciado.getPlaylists().get(posicionSeleccionada);
		File fichero = new File(
				Paths.get("").toAbsolutePath().toString() + "\\exportaciones\\" + playlist.getTitulo().replace(" ", "") + ".csv");

		try (FileWriter fic = new FileWriter(fichero)) {
			if (!fichero.exists()) {
				fichero.createNewFile();
			}
			// Escribir el título de la playlist en el archivo
			fic.write(playlist.getTitulo() + "\n");

			// Escribir los ID de las canciones en la playlist

			for (int i = 0; i != playlist.getCanciones().size(); i++) {
				fic.write(playlist.getCanciones().get(i).getIdAudio() + ";");
			}

			System.out.println("Se ha creado el archivo exitosamente: " + playlist.getTitulo());
		} catch (IOException e) {
			System.err.println("Error al escribir en el archivo: " + e.getMessage());
		}
	}

	/**
	 * Importa una lista de reproducción desde un archivo CSV.
	 *
	 * @param usuarioIniciado usuario que está iniciado en el sistema.
	 * @param listaPlaylist   lista de reproducción.
	 * @return usuario con la lista de reproducción importada.
	 */
	public Usuarios importarPlayList(Usuarios usuarioIniciado, JList<String> listaPlaylist) {
		// TODO Auto-generated method stub
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
		fileChooser.setDialogTitle("Seleccionar archivo de playlist");

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
			try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
				String tituloPlaylist = reader.readLine().trim();
				String segundaLinea = reader.readLine();
				int i = 0;
				boolean repetido = false;
				do {
					if (tituloPlaylist.equals(usuarioIniciado.getPlaylists().get(i).getTitulo())) {
						repetido = true;
					}
					i++;
				} while (i != usuarioIniciado.getPlaylists().size() && repetido == false);
				if (!repetido) {
					int[] numeros = validarSegundaLinea(segundaLinea);
					if (numeros.length != 0) {
						DefaultListModel<String> model = (DefaultListModel<String>) listaPlaylist.getModel();
						model.addElement(tituloPlaylist);

						basededatos.insertarNuevaPlayList(usuarioIniciado, tituloPlaylist, true, numeros);
						PlayList playlist = new PlayList(tituloPlaylist,
								String.valueOf(java.sql.Date.valueOf(LocalDate.now())));
						usuarioIniciado.getPlaylists().add(playlist);
					} else {
						JOptionPane.showMessageDialog(null, "los idAudio no estan bien posicionados o tienen letras",
								"Importar", JOptionPane.ERROR_MESSAGE, null);
					}
				} else {
					JOptionPane.showMessageDialog(null, "La playList tiene el mismo nombre que una ya existente",
							"Importar", JOptionPane.ERROR_MESSAGE, null);
				}
			} catch (IOException e) {
				System.out.println("Error al leer el archivo: " + e.getMessage());
				JOptionPane.showMessageDialog(null, "El archivo no se encontro", "Importar", JOptionPane.ERROR_MESSAGE,
						null);
			}

		} else {
			System.out.println("Operación cancelada por el usuario.");
		}
		return usuarioIniciado;
	}

	/**
	 * Valida la segunda línea del archivo CSV para obtener los IDs de las
	 * canciones.
	 *
	 * @param segundaLinea segunda línea del archivo CSV.
	 * @return arreglo de enteros que representa los IDs de las canciones.
	 */
	public static int[] validarSegundaLinea(String segundaLinea) {

		// Verificar si la segunda línea contiene números separados por ;
		String[] numerosString = segundaLinea.split(";");
		int[] numeros = new int[numerosString.length];
		try {
			for (int i = 0; i < numerosString.length; i++) {
				numeros[i] = Integer.parseInt(numerosString[i].trim());
			}
		} catch (NumberFormatException e) {
			// Devolver un array vacío si se produce una excepción
			return new int[0];
		}
		return numeros;
	}

	/**
	 * Exporta los detalles de una canción a un archivo de texto.
	 * 
	 * @param cancion canción que se exportará.
	 */
	public void exportarCancion(Cancion cancion) {
		// TODO Auto-generated method stub
		File fichero = new File(
				Paths.get("").toAbsolutePath().toString() + "\\exportaciones\\" + cancion.getNombre().replace(" ", "") + ".txt");

		try (FileWriter fic = new FileWriter(fichero)) {
			if (!fichero.exists()) {
				fichero.createNewFile();
			}
			// Escribir el título de la playlist en el archivo
			fic.write("Nombre: " + cancion.getNombre() + "\nDuracion: " + cancion.getDuracion() + "\nID: "
					+ cancion.getIdAudio() + "\nURL: " + cancion.getAudio());

			// Escribir los ID de las canciones en la playlist

			System.out.println("Se ha creado el archivo exitosamente: " + cancion.getNombre());
		} catch (IOException e) {
			System.err.println("Error al escribir en el archivo: " + e.getMessage());
		}
	}

	/**
	 * Exporta los detalles de un podcast a un archivo de texto.
	 * 
	 * @param podcast podcast que se exportará.
	 */
	public void exportarPodcast(Podcast podcast) {
		// TODO Auto-generated method stub
		File fichero = new File(
				Paths.get("").toAbsolutePath().toString() + "\\exportaciones\\" + podcast.getNombre().replace(" ", "") + ".txt");

		try (FileWriter fic = new FileWriter(fichero)) {
			if (!fichero.exists()) {
				fichero.createNewFile();
			}
			// Escribir el título de la playlist en el archivo
			fic.write("Nombre: " + podcast.getNombre() + "\nDuracion: " + podcast.getDuracion() + "\nID: "
					+ podcast.getIdAudio() + "\nURL: " + podcast.getAudio() + "\nColaboradores: "
					+ podcast.getColaboradores());

			// Escribir los ID de las canciones en la playlist

			System.out.println("Se ha creado el archivo exitosamente: " + podcast.getNombre());
		} catch (IOException e) {
			System.err.println("Error al escribir en el archivo: " + e.getMessage());
		}
	}

	/**
	 * Comprueba si la canción ya está en la playlist seleccionada y, de no estarlo,
	 * la añade.
	 * 
	 * @param idAudio         ID de la canción que se quiere añadir a la playlist.
	 * @param listaMenu       lista de playlists.
	 * @param usuarioIniciado usuario que inició sesión.
	 */
	public void comprobarInsertarCancionPlaylist(int idAudio, JList<String> listaMenu, Usuarios usuarioIniciado) {
		if (!listaMenu.isSelectionEmpty()) {
			int playListElegida = listaMenu.getSelectedIndex();
			boolean repetido = false;

			for (int i = 0; i != usuarioIniciado.getPlaylists().get(playListElegida).getCanciones().size()
					&& !repetido; i++) {

				if (usuarioIniciado.getPlaylists().get(playListElegida).getCanciones().get(i).getIdAudio() == idAudio) {
					repetido = true;

				}
			}
			if (!repetido) {
				basededatos.anadirCancionPlaylist(idAudio, listaMenu,
						usuarioIniciado.getPlaylists().get(playListElegida).getIdPlayList());
			} else {
				JOptionPane.showMessageDialog(null, "Esta cancion ya se encuentra en esta playList", "Añadir Cancion",
						JOptionPane.ERROR_MESSAGE, null);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Escoge una playList", "Añadir Cancion", JOptionPane.ERROR_MESSAGE,
					null);
		}
	}

	/**
	 * Muestra los componentes necesarios para añadir o modificar un artista.
	 * 
	 * @param lblNombreArtista
	 * @param lblDescripcionArtista
	 * @param lblCaracteristicaArtista
	 * @param txtFNombreArtista
	 * @param txtFDescripcionArtista
	 * @param comboBox
	 * @param btnArtistaAceptar
	 */
	public void mostrarComponentes(JLabel lblNombreArtista, JLabel lblDescripcionArtista,
			JLabel lblCaracteristicaArtista, JTextField txtFNombreArtista, JTextField txtFDescripcionArtista,
			JComboBox<String> comboBox, JButton btnArtistaAceptar) {
		// TODO Auto-generated method stub
		lblNombreArtista.setVisible(false);
		lblDescripcionArtista.setVisible(false);
		lblCaracteristicaArtista.setVisible(false);
		txtFNombreArtista.setVisible(false);
		txtFDescripcionArtista.setVisible(false);
		comboBox.setVisible(false);
		btnArtistaAceptar.setVisible(false);
	}

	/**
	 * Esconde los componentes necesarios para añadir o modificar un artista.
	 * 
	 * @param lblNombreArtista
	 * @param lblDescripcionArtista
	 * @param lblCaracteristicaArtista
	 * @param txtFNombreArtista
	 * @param txtFDescripcionArtista
	 * @param comboBox
	 * @param btnArtistaAceptar
	 * @param cmbxArtista
	 * @param lblCrudArtista
	 */
	public void ocultarComponentes(JLabel lblNombreArtista, JLabel lblDescripcionArtista,
			JLabel lblCaracteristicaArtista, JTextField txtFNombreArtista, JTextField txtFDescripcionArtista,
			JComboBox<String> comboBox, JButton btnArtistaAceptar, JLabel lblCrudArtista,
			JComboBox<String> cmbxArtista) {
		// TODO Auto-generated method stub
		lblNombreArtista.setVisible(false);
		lblDescripcionArtista.setVisible(false);
		lblCaracteristicaArtista.setVisible(false);
		txtFNombreArtista.setVisible(false);
		txtFDescripcionArtista.setVisible(false);
		comboBox.setVisible(false);
		btnArtistaAceptar.setVisible(false);
		lblCrudArtista.setVisible(false);
		cmbxArtista.setVisible(false);
	}

	public String[] crearModeloAnyos() {
		// TODO Auto-generated method stub
		int añoInicio = 1980;
		int añoFin = 2024;
		String[] años = new String[añoFin - añoInicio + 1];
		for (int i = 0; i < años.length; i++) {
			años[i] = String.valueOf(añoInicio + i);
		}
		return años;
	}

	public void cargarCrudArtistas(JLabel lblNombreCrud, JLabel lblInfo1Crud, JLabel lblInfo2Crud,
			JTextField txtFNombreCrud, JTextField txtFInfo1Crud, JComboBox<String> cmbxCrudTipo) {
		// TODO Auto-generated method stub
		lblNombreCrud.setText("Nombre Artista:");
		lblNombreCrud.setVisible(true);
		lblInfo1Crud.setText("Descripcion Artista");
		lblInfo1Crud.setVisible(true);
		lblInfo2Crud.setText("Caracteristica");
		lblInfo2Crud.setVisible(true);
		txtFNombreCrud.setVisible(true);
		txtFInfo1Crud.setVisible(true);
		cmbxCrudTipo.setModel(new DefaultComboBoxModel<String>(new String[] { "Solista", "Grupo" }));
		cmbxCrudTipo.setVisible(true);
	}

	public void cargarCrudAlbumesycancion(JLabel lblNombreCrud, JLabel lblInfo1Crud, JLabel lblInfo2Crud,
			JTextField txtFNombreCrud, JTextField txtFInfo1Crud, JComboBox<String> cmbxCrudTipo, JLabel lblCrudArtista,
			JComboBox<String> cmbxArtista, int accion) {
		// TODO Auto-generated method stub
		lblNombreCrud.setVisible(true);
		lblInfo1Crud.setVisible(true);
		lblInfo2Crud.setVisible(true);
		txtFNombreCrud.setVisible(true);
		txtFInfo1Crud.setVisible(true);
		cmbxCrudTipo.setVisible(true);
		lblCrudArtista.setVisible(true);
		cmbxArtista.setVisible(true);
		if (accion == 2) {
			lblNombreCrud.setText("Nombre Album:");
			lblInfo1Crud.setText("Genero");
			lblInfo2Crud.setText("Año");
			cmbxCrudTipo.setModel(new DefaultComboBoxModel<String>(crearModeloAnyos()));
		} else {
			lblNombreCrud.setText("Nombre Cancion:");
			lblInfo1Crud.setText("Duracion");
			lblInfo2Crud.setText("Album");
			cmbxCrudTipo.setModel(new DefaultComboBoxModel<String>());

		}
	}

	public boolean formatoDuracion(String duracion) {
		if (duracion == null) {
			return false;
		}
		String patron = "^\\d{2}:\\d{2}:\\d{2}$";
		if (!duracion.matches(patron)) {
			return false;
		}
		// Dividir el string en horas, minutos y segundos
		String[] partes = duracion.split(":");
		int horas = Integer.parseInt(partes[0]);
		int minutos = Integer.parseInt(partes[1]);
		int segundos = Integer.parseInt(partes[2]);

		// Comprobar rangos válidos
		return horas >= 0 && horas <= 23 && minutos >= 0 && minutos <= 59 && segundos >= 0 && segundos <= 59;
	}

}
