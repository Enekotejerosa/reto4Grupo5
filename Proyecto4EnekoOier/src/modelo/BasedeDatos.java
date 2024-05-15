package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BasedeDatos {
	// Conexion Base de datos
	final static String url = "jdbc:mysql://localhost:3306/reto4grupo5_m";
	final static String contrasenabdd = "eneko";
	final static String usuariobdd = "root";

	// Tabla Cliente
	final static String clienteUsuario = "Usuario", clienteNombre = "Nombre", clienteApellido = "Apellido",
			clienteFechaNacimiento = "FechaNacimiento", clienteFechaRegistro = "FechaRegistro",
			clienteContrasena = "Contrasena", clienteTipo = "TipoCliente", clienteId = "IDCliente",
			tablaCliente = "Cliente";
	// Tabla PlayList
	final static String playlistId = "IDList", playlistTitulo = "Titulo", playlistFecha = "FechaCreacion",
			tablaPlaylist = "Playlist";
	// Tabla Audio
	final static String audioId = "IDAudio", audioNombre = "Nombre", audioDuracion = "Duracion", audioAudio = "Audio",
			audioTipo = "Tipo", tablaAudio = "Audio";
	// Tabla PlayListCancione
	final static String tablaPlaylistCanciones = "Playlist_Canciones", playlistCancionFecha = "fechaPlaylist_Cancion",
			tablaPremium = "Premium", premiumFechaCaducidad = "Fecha Caducidad", tablaCancion = "Cancion";
	// Tabla Musico
	final static String musicoId = "IDMusico", musicoNombre = "NombreArtistico", musicoImagen = "Imagen",
			musicoDescripcion = "Descripcion", musicoCaracteristica = "Caracteristica", tablaMusico = "Musico";
	// Tabla Album
	final static String albumId = "IDAlbum", albumTitulo = "Titulo", albumAnyo = "Año", albumGenero = "Genero",
			albumImagen = "Imagen", tablaAlbum = "Album";
	// Tabla Podcaster
	final static String podcasterId = "IDPodcaster", podcasterNombre = "NombreArtistico", podcasterImagen = "Imagen",
			podcasterGenero = "Genero", podcasterDescripcion = "Descripcion", tablaPodcaster = "Podcaster";
	// Tabla Podcast
	final static String podcastColaboradores = "Colaboradores", tablaPodcast = "Podcast";
	// Tabla Estadisticas
	final static String estadisticasTopCanciones = "TopCanciones", estadisticasTopPodcast = "TopPodcast",
			estadisticasMasEscuchados = "MasEscuchados", estadisticasTopPlaylist = "TopPlaylist",
			tablaEstadisticas = "Estadisticas";
	// Consultas
	final static String consultaObtenerUsuario = "SELECT * from " + tablaCliente + " where " + clienteUsuario
			+ "=? and " + clienteContrasena + "=?",
			consultaInsertarUsaurio = "INSERT INTO " + tablaCliente + " (" + clienteNombre + "," + clienteApellido + ","
					+ clienteContrasena + "," + clienteUsuario + "," + clienteFechaNacimiento + ","
					+ clienteFechaRegistro + "," + clienteTipo + ") VALUES (?, ?, ?, ?, ?, ?, ?)";

	/**
	 * Comprueba si el usuario y la contraseña son correctos y si ambos son del
	 * mismo usuario
	 * 
	 * @param usuario    el texto introducido en el JTextField de usuario
	 * @param contrasena usuario el texto introducido en el JPasswordField de
	 *                   contraseña
	 * @return devuelve true si el inicio de sesion es correcto o false si hay un
	 *         error o el inicio de sesion es incorrecto
	 */
	public Usuarios inicioSesion(String usuario, String contrasena) {
		Usuarios usuarioIniciado = null;
		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);

			// Consulta
			PreparedStatement preparedStatement = (PreparedStatement) conexion.prepareStatement(consultaObtenerUsuario);

			preparedStatement.setString(1, usuario);
			preparedStatement.setString(2, contrasena);
			ResultSet rs = preparedStatement.executeQuery();

			// Condición para comprobar si el usuario y la contraseña son correctos
			if (rs.next()) {
				ArrayList<PlayList> playlists = new ArrayList<PlayList>();
				String consulta2 = "Select * FROM " + tablaPlaylist + " where " + clienteId + "=" + rs.getInt(clienteId)
						+ ";";
				PreparedStatement preparedStatement2 = conexion.prepareStatement(consulta2);
				ResultSet rs2 = preparedStatement2.executeQuery();
				while (rs2.next()) {
					ArrayList<Cancion> canciones = new ArrayList<Cancion>();
					String consulta3 = "SELECT " + tablaAudio + ".* FROM " + tablaPlaylistCanciones + " " + "JOIN "
							+ tablaAudio + " ON " + tablaPlaylistCanciones + "." + audioId + " = " + tablaAudio + "."
							+ audioId + " WHERE " + tablaPlaylistCanciones + "." + playlistId + " ="
							+ rs2.getInt(playlistId) + ";";
					PreparedStatement preparedStatement3 = conexion.prepareStatement(consulta3);
					ResultSet rs3 = preparedStatement3.executeQuery();
					while (rs3.next()) {
						Cancion cancion = new Cancion(rs3.getString(audioNombre), rs3.getInt(audioId),
								rs3.getString(audioDuracion), rs3.getString(audioAudio));
						canciones.add(cancion);
					}
					PlayList playlist = new PlayList(rs2.getString(playlistTitulo), rs2.getString(playlistFecha),
							canciones, rs2.getInt(playlistId));
					playlists.add(playlist);
				}
				usuarioIniciado = new Usuarios(rs.getInt(clienteId), rs.getString(clienteNombre),
						rs.getString(clienteApellido), rs.getString(clienteUsuario), rs.getString(clienteContrasena),
						rs.getString(clienteFechaNacimiento), rs.getString(clienteFechaRegistro),
						rs.getString(clienteTipo), playlists);

			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
		return usuarioIniciado;
	}

	/**
	 * Inserta un usuario nuevo a la base de datos
	 * 
	 * @param usuarionuevo objeto usuario con todos los datos del mismo
	 * @param fecPremium   fecha en la que se acaba el premium
	 */
	public void insertarUsuario(Usuarios usuarionuevo, String fecPremium) {
		// TODO Auto-generated method stub

		try {

			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);

			PreparedStatement preparedStatement = conexion.prepareStatement(consultaInsertarUsaurio);

			preparedStatement.setString(1, usuarionuevo.getNombre());
			preparedStatement.setString(2, usuarionuevo.getApellido());
			preparedStatement.setString(3, usuarionuevo.getContrasena());
			preparedStatement.setString(4, usuarionuevo.getUsuario());
			preparedStatement.setString(5, usuarionuevo.getFec_nacimiento());
			preparedStatement.setString(6, usuarionuevo.getFec_registro());
			preparedStatement.setNString(7, usuarionuevo.getTipoCliente());

			preparedStatement.executeUpdate();
			preparedStatement.close();

			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		if (usuarionuevo.getTipoCliente().equals("premium")) {
			registrarPremium(usuarionuevo.getUsuario(), fecPremium);
		}

	}

	/**
	 * Registra al usuario y su respectiva fecha limite de premium en la base de
	 * datos
	 * 
	 * @param usuario
	 * @param fecPremium
	 */
	public void registrarPremium(String usuario, String fecPremium) {
		// TODO Auto-generated method stub
		int id = 0;

		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);

			// Consulta
			PreparedStatement preparedStatement = (PreparedStatement) conexion.prepareStatement("SELECT " + clienteId
					+ " from " + tablaCliente + " where " + clienteUsuario + "= '" + usuario + "'");

			ResultSet rs = preparedStatement.executeQuery();

			// Condición para comprobar si el usuario y la contraseña son correctos
			if (rs.next()) {
				id = rs.getInt(clienteId);
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String sql = "INSERT INTO " + tablaPremium + " (" + clienteId + "," + premiumFechaCaducidad
					+ ") VALUES (?, ?)";
			PreparedStatement preparedStatement = conexion.prepareStatement(sql);

			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, fecPremium);

			preparedStatement.executeUpdate();
			preparedStatement.close();

			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
	}

	/**
	 * Comprueba si el usuario introducido al registrarse existe o no
	 * 
	 * @param usuario
	 * @return devuelve true si no existe y false si existe
	 */
	public boolean comprobarUsuarios(String usuario) {
		// TODO Auto-generated method stub
		ArrayList<String> usuarios = new ArrayList<String>();
		boolean error = false;
		int cont = 0;
		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);

			// Consulta
			PreparedStatement sentencia = (PreparedStatement) conexion
					.prepareStatement("SELECT " + clienteUsuario + " from " + tablaCliente);
			ResultSet rs = sentencia.executeQuery();

			// Añade los Usuarios al ArrayList
			while (rs.next()) {
				usuarios.add(rs.getString(clienteUsuario));
			}

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
		do {
			if (usuario.equals(usuarios.get(cont))) {
				error = true;
			} else {
				error = false;
				cont++;
			}
		} while (cont != usuarios.size() && !error);
		return error;
	}

	/**
	 * Actualiza los datos del cliente al darle al perfil
	 * 
	 * @param nombre
	 * @param apellido
	 * @param contrasena
	 * @param fechaNacimiento
	 * @param usuario
	 */
	public void cambiarDatos(String nombre, String apellido, String contrasena, String fechaNacimiento,
			String usuario) {
		// TODO Auto-generated method stub
		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);

			String sql = "UPDATE " + tablaCliente + " " + "SET " + clienteNombre + " = ?, " + clienteApellido + " = ?, "
					+ "    " + clienteFechaNacimiento + " = ?, " + clienteContrasena + " = ? " + "WHERE "
					+ clienteUsuario + " = ?";
			// Consulta
			PreparedStatement declaracion = conexion.prepareStatement(sql);
			declaracion.setString(1, nombre);
			declaracion.setString(2, apellido);
			declaracion.setString(3, fechaNacimiento);
			declaracion.setString(4, contrasena);
			declaracion.setString(5, usuario);
			declaracion.executeUpdate();
			declaracion.close();

			conexion.close();

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
	}

	/**
	 * Recoge de la base de datos los nombres de los artistas y cuantos hay
	 * 
	 * @return devuelve el numero de artistas que hay en la base de datos
	 */
	public ArrayList<Musico> conseguirArtistas() {
		ArrayList<Musico> musicos = new ArrayList<Musico>();
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta1 = "SELECT * FROM " + tablaMusico;
			PreparedStatement preparedStatement1 = conexion.prepareStatement(consulta1);
			ResultSet rs1 = preparedStatement1.executeQuery();

			while (rs1.next()) {
				ArrayList<Album> albumes = new ArrayList<Album>(); // Nueva lista de álbumes para cada músico
				Caracteristica caracteristica = null;

				String consulta2 = "SELECT * FROM " + tablaAlbum + " WHERE " + musicoId + " =" + rs1.getInt(musicoId)
						+ ";";
				PreparedStatement preparedStatement2 = conexion.prepareStatement(consulta2);
				ResultSet rs2 = preparedStatement2.executeQuery();

				while (rs2.next()) {
					ArrayList<Cancion> canciones = new ArrayList<Cancion>(); // Nueva lista de canciones para cada álbum

					String consulta3 = "SELECT " + tablaAudio + ".* FROM " + tablaAudio + " " + "JOIN " + tablaCancion
							+ " ON " + tablaAudio + "." + audioId + " = " + tablaCancion + "." + audioId + " " + "JOIN "
							+ tablaAlbum + " ON " + tablaCancion + "." + albumId + " = " + tablaAlbum + "." + albumId
							+ " " + "WHERE " + tablaAlbum + "." + albumId + " =" + rs2.getInt(albumId) + ";";
					PreparedStatement preparedStatement3 = conexion.prepareStatement(consulta3);
					ResultSet rs3 = preparedStatement3.executeQuery();

					while (rs3.next()) {
						Cancion cancion = new Cancion(rs3.getString(audioNombre), rs3.getInt(audioId),
								rs3.getString(audioDuracion), rs3.getString(audioAudio));
						canciones.add(cancion); // Agregar la canción a la lista de canciones del álbum
					}

					Album album = new Album(rs2.getString(albumTitulo), rs2.getInt(albumAnyo),
							rs2.getString(albumGenero), rs2.getString(albumImagen), canciones, rs2.getInt(albumId));
					albumes.add(album); // Agregar el álbum a la lista de álbumes del músico
				}

				if (rs1.getString(musicoCaracteristica).equals("solista")) {
					caracteristica = Caracteristica.solista;
				} else {
					caracteristica = Caracteristica.grupo;
				}

				Musico musico = new Musico(rs1.getString(musicoNombre), rs1.getString(musicoDescripcion),
						rs1.getString(musicoImagen), caracteristica, albumes, rs1.getInt(musicoId));
				musicos.add(musico); // Agregar el músico a la lista de músicos
			}
			conexion.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return musicos;
	}

	/**
	 * Recoge una lista de todos los podcasters con sus respectivos podcasts.
	 *
	 * @return lista de objetos Podcaster, cada uno con sus podcasts asociados.
	 */
	public ArrayList<Podcaster> conseguirPodcasters() {
		// TODO Auto-generated method stub
		ArrayList<Podcaster> podcasters = new ArrayList<Podcaster>();
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta1 = "SELECT * FROM " + tablaPodcaster;
			PreparedStatement preparedStatement1 = conexion.prepareStatement(consulta1);
			ResultSet rs1 = preparedStatement1.executeQuery();

			while (rs1.next()) {
				ArrayList<Podcast> podcasts = new ArrayList<Podcast>(); // Nueva lista de álbumes para cada podcast

				String consulta2 = "SELECT " + tablaAudio + ".*, " + tablaPodcast + "." + podcastColaboradores + " "
						+ "FROM " + tablaPodcaster + " " + "JOIN " + tablaPodcast + " ON " + tablaPodcaster + "."
						+ podcasterId + " = " + tablaPodcast + "." + podcasterId + " " + "JOIN " + tablaAudio + " ON "
						+ tablaPodcast + "." + audioId + " = " + tablaAudio + "." + audioId + " " + "WHERE "
						+ tablaPodcaster + "." + podcasterId + " =" + rs1.getInt(podcasterId) + ";";
				PreparedStatement preparedStatement2 = conexion.prepareStatement(consulta2);
				ResultSet rs2 = preparedStatement2.executeQuery();

				while (rs2.next()) {

					Podcast podcast = new Podcast(rs2.getString(audioNombre), rs2.getInt(audioId),
							rs2.getString(audioDuracion), rs2.getString(audioAudio),
							rs2.getString(podcastColaboradores));
					podcasts.add(podcast); // Agregar el álbum a la lista de álbumes del músico
				}
				Podcaster podcaster = new Podcaster(rs1.getString(podcasterNombre), rs1.getString(podcasterDescripcion),
						rs1.getString(podcasterImagen), rs1.getString(podcasterGenero), podcasts);
				podcasters.add(podcaster); // Agregar el músico a la lista de músicos
			}
			conexion.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return podcasters;
	}

	/**
	 * Añade una canción a la lista de "Me gusta" de un usuario.
	 *
	 * @param usuarioIniciado usuario que ha iniciado sesión
	 * @param idAudio         ID de la canción que se va a añadir a la lista de "Me
	 *                        gusta"
	 */
	public void anadirCancionLike(Usuarios usuarioIniciado, int idAudio) {
		// TODO Auto-generated method stub
		int idLista = 0;
		for (int i = 0; i != usuarioIniciado.getPlaylists().size(); i++) {
			if (usuarioIniciado.getPlaylists().get(i).getTitulo().equals("Me gusta")) {
				idLista = usuarioIniciado.getPlaylists().get(i).getIdPlayList();
			}
		}
		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			// Crear la consulta SQL para insertar en Playlist_Canciones
			String consulta = "INSERT INTO " + tablaPlaylistCanciones + " (" + playlistId + ", " + audioId + ", "
					+ playlistCancionFecha + ") " + "VALUES (" + idLista + "," + idAudio + ", CURDATE());";
			// Crea el PreparedStatement
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			// Ejecutar la consulta y obtener el resultado
			preparedStatement.executeUpdate();

			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Añade una canción a la lista de "Me gusta" de un usuario.
	 *
	 * @param usuarioIniciado usuario que ha iniciado sesión.
	 * @param idAudio         ID de la canción que se va a añadir a la lista de "Me
	 *                        gusta".
	 */
	public void obtenerYActualizarPlaylist(String usuario, JList<String> listaPlaylist) {
		DefaultListModel<String> listModel = new DefaultListModel<>();
		listaPlaylist.setModel(listModel); // Establecer el modelo de lista en el JList

		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			// Crear la consulta SQL
			String consulta1 = "SELECT p." + playlistTitulo + " FROM " + tablaPlaylist + " AS p " + "INNER JOIN "
					+ tablaCliente + " AS c ON p." + clienteId + " = c." + clienteId + " " + "WHERE c." + clienteUsuario
					+ " = '" + usuario + "'";
			// Crea el PreparedStatement
			PreparedStatement preparedStatement1 = conexion.prepareStatement(consulta1);
			// Ejecutar la consulta y obtener el resultado
			ResultSet rs1 = preparedStatement1.executeQuery();
			// Leer el resultado y agregarlo al modelo de lista
			while (rs1.next()) {
				String titulo = rs1.getString(playlistTitulo);
				listModel.addElement(titulo);
			}
			// Cierra la conexión
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Borra una playlist de un usuario.
	 *
	 * @param listaPlaylist JList que contiene las playlists del usuario.
	 * @param usuario       nombre de usuario del usuario.
	 */
	public void borrarPlayList(JList<String> listaPlaylist, String usuario) {
		// TODO Auto-generated method stub
		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			// Crear la consulta SQL
			String consulta1 = "DELETE FROM " + tablaPlaylist + " WHERE " + clienteId + " IN (SELECT " + clienteId
					+ " FROM " + tablaCliente + " WHERE " + clienteUsuario + " = '" + usuario + "') AND "
					+ playlistTitulo + " = '" + listaPlaylist.getSelectedValue() + "';";

			// Crea el PreparedStatement
			PreparedStatement preparedStatement1 = conexion.prepareStatement(consulta1);
			// Ejecutar la consulta y obtener el resultado
			preparedStatement1.executeUpdate();
			// Cierra la conexión
			conexion.close();
			// Eliminar el elemento seleccionado del JList
			DefaultListModel<String> model = (DefaultListModel<String>) listaPlaylist.getModel();
			model.removeElementAt(listaPlaylist.getSelectedIndex());
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Inserta una nueva lista de reproducción en la base de datos.
	 * 
	 * @param usuarioIniciado El usuario que está iniciado en el sistema.
	 * @param nombreLista     El nombre de la nueva lista de reproducción.
	 * @param importar        Booleano que indica si se van a importar canciones a
	 *                        la lista de reproducción.
	 * @param numeros         Un array de enteros que contiene los IDs de las
	 *                        canciones a importar.
	 */
	public void insertarNuevaPlayList(Usuarios usuarioIniciado, String nombreLista, boolean importar, int[] numeros) {
		// TODO Auto-generated method stub
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "INSERT INTO " + tablaPlaylist + " (" + playlistTitulo + ", " + playlistFecha + ", "
					+ clienteId + ") VALUES ('" + nombreLista + "', CURRENT_DATE(), " + usuarioIniciado.getIdUsuario()
					+ ")";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.executeUpdate();
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		if (importar) {

			int idLista = conseguirIDLista(nombreLista, usuarioIniciado.getIdUsuario());
			insertarCancionesaPlaylist(numeros, idLista);
		}
	}

	/**
	 * Inserta una nueva playlist en la base de datos y, opcionalmente, importar
	 * canciones a la misma.
	 *
	 * @param usuarioIniciado usuario que ha iniciado sesión.
	 * @param nombreLista     nombre de la nueva playlist.
	 * @param importar        indicador booleano que especifica si se deben importar
	 *                        canciones a la playlist.
	 * @param numeros         array de enteros que contiene los IDs de las canciones
	 *                        que se importarán a la playlist.
	 */
	private int conseguirIDLista(String nombreLista, int idUsuario) {
		int idLista = 0;
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consultaridLista = "SELECT " + playlistId + " FROM " + tablaPlaylist + " Where " + clienteId + " ="
					+ idUsuario + " and " + playlistTitulo + " = '" + nombreLista + "';";
			PreparedStatement preparedStatement = conexion.prepareStatement(consultaridLista);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				idLista = rs.getInt(playlistId);
			}
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return idLista;
	}

	/**
	 * Inserta canciones en una playlist en la base de datos.
	 *
	 * @param numeros array de enteros que contiene los IDs de las canciones a
	 *                insertar.
	 * @param idLista ID de la playlist donde se insertarán las canciones.
	 */
	private void insertarCancionesaPlaylist(int[] numeros, int idLista) {
		int i = 0;
		int ultimoAudio = sacarUltimoIdAudio();
		boolean repetido = false;
		ArrayList<Integer> numerosInsertados = new ArrayList<Integer>();
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			while (i != numeros.length) {
				for (int o = 0; o != numerosInsertados.size(); o++) {
					if (numerosInsertados.get(o) == numeros[i]) {
						repetido = true;
					}
				}
				if (numeros[i] <= ultimoAudio && numeros[i] > 0 && repetido == false) {
					String consulta = "INSERT INTO " + tablaPlaylistCanciones + " (" + playlistId + ", " + audioId
							+ ", " + playlistCancionFecha + ") VALUES (" + idLista + ", " + numeros[i]
							+ ", CURRENT_DATE())";

					PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
					preparedStatement.executeUpdate();
					numerosInsertados.add(numeros[i]);
				} else {
					JOptionPane.showMessageDialog(null, "No se ha podido insertar la cancion con el id: " + numeros[i],
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				i++;
				repetido = false;
			}
			conexion.close();

		} catch (

		SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Recoge el último ID de audio de la base de datos.
	 *
	 * @return último ID de audio.
	 */
	private int sacarUltimoIdAudio() {
		int ultimo = 0;
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "SELECT MAX(" + audioId + ") AS UltimoID FROM " + tablaAudio + ";";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				ultimo = rs.getInt("UltimoID");
			}
			conexion.close();

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return ultimo;
	}

	/**
	 * Método para registrar que un audio ha sido reproducido.
	 *
	 * @param idAudio ID del audio que ha sido reproducido.
	 */
	public void audioReproducido(int idAudio) {
		// TODO Auto-generated method stub
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "UPDATE " + tablaEstadisticas + " SET " + estadisticasMasEscuchados + " = "
					+ estadisticasMasEscuchados + " + 1 WHERE " + audioId + " =" + idAudio + ";";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.executeUpdate();
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Registra que a un audio se le ha dado "Me gusta".
	 *
	 * @param idAudio        ID del audio al que se le ha dado "Me gusta".
	 * @param opcionEscogida La opción seleccionada (0 para canción, 1 para
	 *                       podcast).
	 */
	public void anadirEstadisticasLike(int idAudio, int opcionEscogida) {
		// TODO Auto-generated method stub

		String atributoEscogido = "";
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			if (opcionEscogida == 0) {
				atributoEscogido = "TopCanciones";
			} else {
				atributoEscogido = "TopPodcast";
			}
			String consulta = "UPDATE " + tablaEstadisticas + " SET " + atributoEscogido + " = " + atributoEscogido
					+ " + 1 WHERE " + audioId + " =" + idAudio + ";";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.executeUpdate();
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Añade una canción a una playlist en la base de datos.
	 *
	 * @param idAudio   ID de la canción que se va a añadir a la playlist.
	 * @param listaMenu JList que contiene las playlists disponibles.
	 * @param idLista   ID de la playlist a la que se va a añadir la canción.
	 */
	public void anadirCancionPlaylist(int idAudio, JList<String> listaMenu, int idLista) {
		// TODO Auto-generated method stub
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "INSERT INTO " + tablaPlaylistCanciones + " (" + playlistId + ", " + audioId + ", "
					+ playlistCancionFecha + ") VALUES (" + idLista + ", " + idAudio + ", CURRENT_DATE())";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			System.out.println("se ha metiu");
			preparedStatement.executeUpdate();
			conexion.close();
		} catch (

		SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	/**
	 * Recoge y actualiza la lista de artistas en un JList.
	 *
	 * @param lista JList que se actualizará con la lista de artistas.
	 */
	public void obtenerYActualizarLista(JList<String> lista, int elementoModificar) {
		DefaultListModel<String> listModel = new DefaultListModel<>();
		lista.setModel(listModel); // Establecer el modelo de lista en el JList
		String elemento = "";
		String tablaElemento = "";
		if (elementoModificar == 1) {
			elemento = musicoNombre;
			tablaElemento = tablaMusico;
		} else if (elementoModificar == 2) {
			elemento = albumTitulo;
			tablaElemento = tablaAlbum;
		} else {
			elemento = audioNombre;
			tablaElemento = tablaAudio;
		}

		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta1 = "SELECT " + elemento + " FROM " + tablaElemento;
			PreparedStatement preparedStatement1 = conexion.prepareStatement(consulta1);
			ResultSet rs1 = preparedStatement1.executeQuery();
			while (rs1.next()) {
				String elementanyadir = rs1.getString(elemento);
				listModel.addElement(elementanyadir);
			}
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Añade un nuevo artista a la base de datos.
	 *
	 * @param nombreArtista      nombre del nuevo artista.
	 * @param descripcionArtista descripción del nuevo artista.
	 * @param cmbxCrudTipo
	 * @param selectedItem       tipo de artista seleccionado.
	 * @param lista              JList que muestra la lista de artistas.
	 * @param cmbxArtista
	 * @param elementoGestionado
	 * @param musicos
	 */
	public void añadirElementoLista(String nombreArtista, String descripcionArtista, JComboBox<String> cmbxCrudTipo,
			JList<String> lista, int elementoGestionado, JComboBox<String> cmbxArtista, ArrayList<Musico> musicos) {
		PreparedStatement preparedStatement = null;
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			if (elementoGestionado == 1) {
				String consulta = "INSERT INTO " + tablaMusico + " (" + musicoNombre + ", " + musicoImagen + ", "
						+ musicoDescripcion + ", " + musicoCaracteristica + ") VALUES (?, ?, ?, ?)";
				preparedStatement = conexion.prepareStatement(consulta);
				preparedStatement.setString(1, nombreArtista);
				preparedStatement.setString(2, nombreArtista.toLowerCase() + ".jpg");
				preparedStatement.setString(3, descripcionArtista);
				preparedStatement.setString(4, cmbxCrudTipo.getSelectedItem().toString().toLowerCase());

			} else if (elementoGestionado == 2) {
				String consulta = "INSERT INTO " + tablaAlbum + " (" + albumTitulo + ", " + albumImagen + ", "
						+ albumAnyo + ", " + albumGenero + ", " + musicoId + ") VALUES (?, ?, ?, ?, ?)";
				preparedStatement = conexion.prepareStatement(consulta);
				preparedStatement.setString(1, nombreArtista);
				preparedStatement.setString(2, nombreArtista.toLowerCase() + ".jpg");
				preparedStatement.setInt(3, Integer.parseInt(cmbxCrudTipo.getSelectedItem().toString()));
				preparedStatement.setString(4, descripcionArtista);
				preparedStatement.setInt(5, musicos.get(cmbxArtista.getSelectedIndex()).getId());
			} else if (elementoGestionado == 3) {
				String consulta = "INSERT INTO " + tablaAudio + " (" + audioNombre + ", " + audioAudio + ", "
						+ audioDuracion + ", " + audioTipo + ") VALUES (?, ?, ?, ?)";
				preparedStatement = conexion.prepareStatement(consulta);
				preparedStatement.setString(1, nombreArtista);
				preparedStatement.setString(2, nombreArtista.toLowerCase() + ".wav");
				preparedStatement.setString(3, descripcionArtista);
				preparedStatement.setString(4, "cancion");
			}
			DefaultListModel<String> model = (DefaultListModel<String>) lista.getModel();
			model.addElement(nombreArtista);
			preparedStatement.executeUpdate();
			if ((elementoGestionado == 3)) {
				int idCancion = obteneridAudio(nombreArtista, descripcionArtista);
				insertarCancion(musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().get(cmbxCrudTipo.getSelectedIndex()).getId(),
						idCancion);
				Cancion nuevaCancion = new Cancion(nombreArtista, idCancion, descripcionArtista, nombreArtista+".wav");
				musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().get(cmbxCrudTipo.getSelectedIndex()).getCanciones().add(nuevaCancion);
				
				lista.setModel(new DefaultComboBoxModel<String>());
			}
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	private void insertarCancion(int idAlbum, int idAudio) {
		// TODO Auto-generated method stub
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "INSERT INTO " + tablaCancion + " (" + audioId + ", " + albumId + ") VALUES (?, ?)";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.setInt(1, idAudio);
			preparedStatement.setInt(2, idAlbum);
			preparedStatement.executeUpdate();
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	private int obteneridAudio(String nombreArtista, String duracion) {
		// TODO Auto-generated method stub
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "Select " + audioId + " from " + tablaAudio + " where " + audioNombre + " ='"
					+ nombreArtista + "' and " + audioDuracion + " = '" + duracion + "';";

			PreparedStatement statement = conexion.prepareStatement(consulta);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt(audioId);
			}
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		return 0;
	}

	/**
	 * Modifica los datos de un artista en la base de datos.
	 *
	 * @param nombreArtista nombre del artista a modificar.
	 * @param selectedItem  nuevo tipo de artista seleccionado.
	 * @param lista         JList que muestra la lista de artistas.
	 */
	public void modificarElementoLista(String nombreArtistaAntiguo, String nombreArtistaNuevo,
			String descripcionArtista, Object selectedItem, JList<String> lista) {
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "UPDATE " + tablaMusico + " SET " + musicoNombre + "=?, " + musicoDescripcion + "=?, "
					+ musicoCaracteristica + "=? WHERE " + musicoNombre + "=?";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.setString(1, nombreArtistaNuevo); // Nuevo nombre del artista
			preparedStatement.setString(2, descripcionArtista);
			preparedStatement.setString(3, selectedItem.toString());
			preparedStatement.setString(4, nombreArtistaAntiguo); // Nombre antiguo del artista
			preparedStatement.executeUpdate();
			conexion.close();

			// Actualizar el nombre en el JList
			DefaultListModel<String> model = (DefaultListModel<String>) lista.getModel();
			int index = lista.getSelectedIndex();
			model.set(index, nombreArtistaNuevo);
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Recoge un artista de la base de datos según su nombre.
	 *
	 * @param nombreArtista nombre del artista a buscar.
	 * @return objeto Musico que representa al artista encontrado, o null si no se
	 *         encuentra.
	 */
	public Musico obtenerArtista(String nombreArtista) {
		Musico artista = null;
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "SELECT * FROM " + tablaMusico + " WHERE " + musicoNombre + "=?";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.setString(1, nombreArtista);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Caracteristica caracteristica = Caracteristica.valueOf(resultSet.getString(musicoCaracteristica));
				artista = new Musico(resultSet.getString(musicoNombre), resultSet.getString(musicoDescripcion),
						resultSet.getString(musicoImagen), caracteristica, null, resultSet.getInt(musicoId));
			}
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return artista;
	}

	/**
	 * Borra un elemento de una lista y de la base de datos.
	 *
	 * @param lista             JList que contiene los elementos.
	 * @param elementoModificar tipo de elemento a borrar (1 para músico...).
	 */
	public void borrarElementosLista(JList<String> lista, int elementoModificar) {
		String elemento = "";
		String tablaElemento = "";
		if (elementoModificar == 1) {
			elemento = musicoNombre;
			tablaElemento = tablaMusico;
		} else if (elementoModificar == 2) {
			elemento = albumTitulo;
			tablaElemento = tablaAlbum;
		} else {
			elemento = audioNombre;
			tablaElemento = tablaAudio;
		}

		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);

			String artistaSeleccionado = lista.getSelectedValue();
			String consulta = "DELETE FROM " + tablaElemento + " WHERE " + elemento + " = ?";

			// Crea el PreparedStatement
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.setString(1, artistaSeleccionado);

			// Ejecutar la consulta para borrar el artista seleccionado
			preparedStatement.executeUpdate();

			// Cierra la conexión
			conexion.close();

			// Eliminar el elemento seleccionado del JList
			DefaultListModel<String> model = (DefaultListModel<String>) lista.getModel();
			model.removeElement(artistaSeleccionado);
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Recoge y muestra las canciones más escuchadas en una tabla.
	 *
	 * @param tablaEstadisticasPanel La tabla donde se mostrarán las canciones más
	 *                               escuchadas.
	 */
	public void obtenerTopCanciones(JTable tablaEstadisticasPanel) {
		// TODO Auto-generated method stub

		int cont = 1;
		if (tablaEstadisticasPanel.getModel() instanceof DefaultTableModel) {
			((DefaultTableModel) tablaEstadisticasPanel.getModel()).setRowCount(0);
		}

		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Posicion");
		model.addColumn("Nombre Artista");
		model.addColumn("Nombre Álbum");
		model.addColumn("Nombre Audio");
		model.addColumn("Top Canciones");

		// Establecer el modelo de datos en la tabla
		tablaEstadisticasPanel.setModel(model);
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "SELECT m." + musicoNombre + " AS NombreArtista, a." + albumTitulo
					+ " AS NombreAlbum, au." + audioNombre + " AS NombreAudio, e." + estadisticasTopCanciones + " "
					+ "FROM " + tablaEstadisticas + " e " + "JOIN " + tablaAudio + " au ON e." + audioId + " = au."
					+ audioId + " " + "JOIN " + tablaCancion + " c ON au." + audioId + " = c." + audioId + " " + "JOIN "
					+ tablaAlbum + " a ON c." + albumId + " = a." + albumId + " " + "JOIN " + tablaMusico + " m ON a."
					+ musicoId + " = m." + musicoId + " " + "WHERE e." + estadisticasTopCanciones + " > 0 "
					+ "ORDER BY e." + estadisticasTopCanciones + " DESC";
			PreparedStatement statement = conexion.prepareStatement(consulta);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Object[] row = { cont, resultSet.getString("NombreArtista"), resultSet.getString("NombreAlbum"),
						resultSet.getString("NombreAudio"), resultSet.getInt(estadisticasTopCanciones) };
				model.addRow(row);
				cont++;
			}

			conexion.close();
		} catch (

		SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	/**
	 * Recoge la lista de las canciones más escuchadas y mostrarlas en una tabla.
	 *
	 * @param tablaEstadisticasPanel tabla donde se mostrarán las canciones más
	 *                               escuchadas.
	 */
	public void obtenerTopPodcasts(JTable tablaEstadisticasPanel) {
		// TODO Auto-generated method stub
		int cont = 1;
		if (tablaEstadisticasPanel.getModel() instanceof DefaultTableModel) {
			((DefaultTableModel) tablaEstadisticasPanel.getModel()).setRowCount(0);
		}

		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Posicion");
		model.addColumn("Podcast");
		model.addColumn("Capitulo");
		model.addColumn("Top Podcast");

		// Establecer el modelo de datos en la tabla
		tablaEstadisticasPanel.setModel(model);
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "SELECT p." + podcasterNombre + " AS NombrePodcaster, a." + audioNombre
					+ " AS NombrePodcast, e." + estadisticasTopPodcast + " " + "FROM " + tablaEstadisticas + " e "
					+ "JOIN " + tablaAudio + " a ON e." + audioId + " = a." + audioId + " " + "JOIN " + tablaPodcast
					+ " pc ON e." + audioId + " = pc." + audioId + " " + "JOIN " + tablaPodcaster + " p ON pc."
					+ podcasterId + " = p." + podcasterId + " " + "WHERE e." + estadisticasTopPodcast + " > 0 "
					+ "ORDER BY e." + estadisticasTopPodcast + " DESC";
			PreparedStatement statement = conexion.prepareStatement(consulta);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Object[] row = { cont, resultSet.getString("NombrePodcaster"), resultSet.getString("NombrePodcast"),
						resultSet.getInt(estadisticasTopPodcast) };
				model.addRow(row);
				cont++;
			}

			conexion.close();
		} catch (

		SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void obtenerTopPlaylist(JTable tablaEstadisticasPanel) {
		int cont = 1;
		if (tablaEstadisticasPanel.getModel() instanceof DefaultTableModel) {
			((DefaultTableModel) tablaEstadisticasPanel.getModel()).setRowCount(0);
		}
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Posicion");
		model.addColumn("Cancion / Podcast");
		model.addColumn("Mas Reproducidos");
		// Establecer el modelo de datos en la tabla
		tablaEstadisticasPanel.setModel(model);
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "SELECT au." + audioNombre + " AS NombreAudio, e." + estadisticasTopPlaylist + " "
					+ "FROM " + tablaEstadisticas + " e " + "JOIN " + tablaAudio + " au ON e." + audioId + " = au."
					+ audioId + " " + "WHERE e." + estadisticasTopPlaylist + " > 0 " + "ORDER BY e."
					+ estadisticasTopPlaylist + " DESC";
			PreparedStatement statement = conexion.prepareStatement(consulta);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Object[] row = { cont, resultSet.getString("NombreAudio"), resultSet.getInt(estadisticasTopPlaylist) };
				model.addRow(row);
				cont++;
			}
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void obtenerMasEscuchadas(JTable tablaEstadisticasPanel) {
		int cont = 1;
		if (tablaEstadisticasPanel.getModel() instanceof DefaultTableModel) {
			((DefaultTableModel) tablaEstadisticasPanel.getModel()).setRowCount(0);
		}
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Posicion");
		model.addColumn("Cancion / Podcast");
		model.addColumn("Mas Reproducidos");
		// Establecer el modelo de datos en la tabla
		tablaEstadisticasPanel.setModel(model);
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "SELECT au." + audioNombre + " AS NombreAudio, e." + estadisticasMasEscuchados + " "
					+ "FROM " + tablaEstadisticas + " e " + "JOIN " + tablaAudio + " au ON e." + audioId + " = au."
					+ audioId + " " + "WHERE e." + estadisticasMasEscuchados + " > 0 " + "ORDER BY e."
					+ estadisticasMasEscuchados + " DESC";
			PreparedStatement statement = conexion.prepareStatement(consulta);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Object[] row = { cont, resultSet.getString("NombreAudio"),
						resultSet.getInt(estadisticasMasEscuchados) };
				model.addRow(row);
				cont++;
			}
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void mostrarInfoPlaylist(JTable tablaPlaylists, int idCliente, String nombreLista) {
		int cont = 1;
		if (tablaPlaylists.getModel() instanceof DefaultTableModel) {
			((DefaultTableModel) tablaPlaylists.getModel()).setRowCount(0);
		}

		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Posicion");
		model.addColumn("Cancion / Podcast");

		// Establecer el modelo de datos en la tabla
		tablaPlaylists.setModel(model);
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "SELECT au." + audioNombre + " AS NombreAudio " + "FROM " + tablaPlaylist + " p "
					+ "JOIN " + tablaPlaylistCanciones + " pc on p." + playlistId + " = pc." + playlistId + " "
					+ "JOIN " + tablaAudio + " au ON pc." + audioId + " = au." + audioId + " " + "WHERE p." + clienteId
					+ " = " + idCliente + " And " + playlistTitulo + " = '" + nombreLista + "';";

			PreparedStatement statement = conexion.prepareStatement(consulta);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Object[] row = { cont, resultSet.getString("NombreAudio") };
				model.addRow(row);
				cont++;
			}
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public void modificarAlbum(String nombre, String genero, Object anyo, JList<String> listaCrudMusica, Album album) {
		// TODO Auto-generated method stub
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "UPDATE " + tablaAlbum + " SET " + albumTitulo + "=?, " + albumImagen + "=?, "
					+ albumGenero + "=?, " + albumAnyo + "=? WHERE " + albumId + "=?";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.setString(1, nombre); // Nuevo nombre del artista
			preparedStatement.setString(2, nombre + ".jpg");
			preparedStatement.setString(3, genero);
			preparedStatement.setInt(4, Integer.parseInt(anyo.toString())); // Nombre antiguo del artista
			preparedStatement.setInt(5, album.getId());
			preparedStatement.executeUpdate();
			conexion.close();

			// Actualizar el nombre en el JList
			DefaultListModel<String> model = (DefaultListModel<String>) listaCrudMusica.getModel();
			int index = listaCrudMusica.getSelectedIndex();
			model.set(index, nombre);
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

}
