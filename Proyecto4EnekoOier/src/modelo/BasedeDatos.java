package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class BasedeDatos {

	final static String url = "jdbc:mysql://reto4grupo5.duckdns.org:3306/reto4grupo5_m";
	final static String contrasenabdd = "Elorrieta2024+";
	final static String usuariobdd = "grupo05";
	final static String clienteUsuario = "Usuario", clienteNombre = "Nombre", clienteApellidos = "Apellido",
			clienteFechaNacimiento = "FechaNacimiento", clienteFechaRegistro = "FechaRegistro",
			clienteContrasena = "Contrasena", clienteTipo = "TipoCliente";

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
			PreparedStatement preparedStatement = (PreparedStatement) conexion
					.prepareStatement("SELECT * from Cliente where usuario=? and contrasena=?");

			preparedStatement.setString(1, usuario);
			preparedStatement.setString(2, contrasena);
			ResultSet rs = preparedStatement.executeQuery();

			// Condición para comprobar si el usuario y la contraseña son correctos
			if (rs.next()) {
				ArrayList<PlayList> playlists = new ArrayList<PlayList>();
				String consulta2 = "Select * FROM Playlist where IDCliente =" + rs.getInt("IDCliente") + ";";
				PreparedStatement preparedStatement2 = conexion.prepareStatement(consulta2);
				ResultSet rs2 = preparedStatement2.executeQuery();
				while (rs2.next()) {
					ArrayList<Cancion> canciones = new ArrayList<Cancion>();
					String consulta3 = "SELECT Audio.* FROM Playlist_Canciones "
							+ "JOIN Audio ON Playlist_Canciones.IDAudio = Audio.IDAudio "
							+ "WHERE Playlist_Canciones.IDList =" + rs2.getInt("IDList") + ";";
					PreparedStatement preparedStatement3 = conexion.prepareStatement(consulta3);
					ResultSet rs3 = preparedStatement3.executeQuery();
					while (rs3.next()) {
						Cancion cancion = new Cancion(rs3.getString("Nombre"), rs3.getInt("IDAudio"),
								rs3.getString("Duracion"), rs3.getString("Audio"));
						canciones.add(cancion);
					}
					PlayList playlist = new PlayList(rs2.getString("Titulo"), rs2.getString("FechaCreacion"), canciones,
							rs2.getInt("idList"));
					playlists.add(playlist);
				}
				usuarioIniciado = new Usuarios(rs.getInt("IDCliente"), rs.getString(clienteNombre),
						rs.getString(clienteApellidos), rs.getString(clienteUsuario), rs.getString(clienteContrasena),
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
	 * inserta un usuario nuevo a la base de datos
	 * 
	 * @param usuarionuevo objeto usuario con todos los datos del mismo
	 * @param fecPremium   fecha en la que se acaba el premium
	 */
	public void insertarUsuario(Usuarios usuarionuevo, String fecPremium) {
		// TODO Auto-generated method stub

		try {

			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);

			String sql = "INSERT INTO Cliente (Nombre,Apellido,Contrasena,Usuario, FechaNacimiento, FechaRegistro, TipoCliente) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = conexion.prepareStatement(sql);

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
			PreparedStatement preparedStatement = (PreparedStatement) conexion
					.prepareStatement("SELECT IDCliente from Cliente where usuario= '" + usuario + "'");

			ResultSet rs = preparedStatement.executeQuery();

			// Condición para comprobar si el usuario y la contraseña son correctos
			if (rs.next()) {
				id = rs.getInt("IDCliente");
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String sql = "INSERT INTO Premium (IDCliente,FechaCaducidad) VALUES (?, ?)";
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
			PreparedStatement sentencia = (PreparedStatement) conexion.prepareStatement("SELECT usuario from Cliente");
			ResultSet rs = sentencia.executeQuery();

			// Añade los Usuarios al ArrayList
			if (rs.next()) {
				usuarios.add(rs.getString("Usuario"));
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
	 * comprueba si un usuario es premium o no en función de su nombre de usuario.
	 *
	 * @param usuario nombre de usuario del cliente a verificar.
	 * @return true si el usuario es premium, false si es cliente estandar.
	 */
	public boolean esPremium(String usuario) {
		// TODO Auto-generated method stub
		boolean premium = false;
		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			// Crear la consulta SQL
			String consulta = "SELECT tipoCliente FROM cliente WHERE USUARIO ='" + usuario + "';";
			// Crea el PreparedStatement
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			// Ejecutar la consulta y obtener el resultado
			ResultSet rs = preparedStatement.executeQuery();
			// Leer el resultado y agregarlo al modelo de lista
			if (rs.next()) {
				if (rs.getString("tipoCliente").equals("premium")) {
					premium = true;
				}
			}
			// Cierra la conexión
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return premium;
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

			String sql = "UPDATE Cliente " + "SET Nombre = ?, " + "    Apellido = ?, " + "    FechaNacimiento = ?, "
					+ "    Contrasena = ? " + "WHERE Usuario = ?";
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
			String consulta1 = "SELECT * FROM Musico";
			PreparedStatement preparedStatement1 = conexion.prepareStatement(consulta1);
			ResultSet rs1 = preparedStatement1.executeQuery();

			while (rs1.next()) {
				ArrayList<Album> albumes = new ArrayList<Album>(); // Nueva lista de álbumes para cada músico
				Caracteristica caracteristica = null;

				String consulta2 = "SELECT * FROM Album WHERE idMusico =" + rs1.getInt("idMusico") + ";";
				PreparedStatement preparedStatement2 = conexion.prepareStatement(consulta2);
				ResultSet rs2 = preparedStatement2.executeQuery();

				while (rs2.next()) {
					ArrayList<Cancion> canciones = new ArrayList<Cancion>(); // Nueva lista de canciones para cada álbum

					String consulta3 = "SELECT Audio.* FROM Audio " + "JOIN Cancion ON Audio.IDAudio = Cancion.IDAudio "
							+ "JOIN Album ON Cancion.IDAlbum = Album.IDAlbum " + "WHERE Album.IDAlbum ="
							+ rs2.getInt("idAlbum") + ";";
					PreparedStatement preparedStatement3 = conexion.prepareStatement(consulta3);
					ResultSet rs3 = preparedStatement3.executeQuery();

					while (rs3.next()) {
						Cancion cancion = new Cancion(rs3.getString("Nombre"), rs3.getInt("IDAudio"),
								rs3.getString("Duracion"), rs3.getString("Audio"));
						canciones.add(cancion); // Agregar la canción a la lista de canciones del álbum
					}

					Album album = new Album(rs2.getString("Titulo"), rs2.getInt("Año"), rs2.getString("Genero"),
							rs2.getString("Imagen"), canciones);
					albumes.add(album); // Agregar el álbum a la lista de álbumes del músico
				}

				if (rs1.getString("caracteristica").equals("solista")) {
					caracteristica = Caracteristica.solista;
				} else {
					caracteristica = Caracteristica.grupo;
				}

				Musico musico = new Musico(rs1.getString("nombreArtistico"), rs1.getString("Descripcion"),
						rs1.getString("Imagen"), caracteristica, albumes);
				musicos.add(musico); // Agregar el músico a la lista de músicos
			}
			conexion.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return musicos;
	}

	public ArrayList<Podcaster> conseguirPodcasters() {
		// TODO Auto-generated method stub
		ArrayList<Podcaster> podcasters = new ArrayList<Podcaster>();
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta1 = "SELECT * FROM Podcaster";
			PreparedStatement preparedStatement1 = conexion.prepareStatement(consulta1);
			ResultSet rs1 = preparedStatement1.executeQuery();

			while (rs1.next()) {
				ArrayList<Podcast> podcasts = new ArrayList<Podcast>(); // Nueva lista de álbumes para cada podcast

				String consulta2 = "SELECT Audio.*, Podcast.Colaboradores " + "FROM Podcaster "
						+ "JOIN Podcast ON Podcaster.IDPodcaster = Podcast.IdPodcaster "
						+ "JOIN Audio ON Podcast.IDAudio = Audio.IDAudio " + "WHERE Podcaster.IDPodcaster ="
						+ rs1.getInt("IDPodcaster") + ";";
				PreparedStatement preparedStatement2 = conexion.prepareStatement(consulta2);
				ResultSet rs2 = preparedStatement2.executeQuery();

				while (rs2.next()) {

					Podcast podcast = new Podcast(rs2.getString("Nombre"), rs2.getInt("idAudio"),
							rs2.getString("Duracion"), rs2.getString("Audio"), rs2.getString("Colaboradores"));
					podcasts.add(podcast); // Agregar el álbum a la lista de álbumes del músico
				}
				Podcaster podcaster = new Podcaster(rs1.getString("NombreArtistico"), rs1.getString("Descripcion"),
						rs1.getString("Imagen"), rs1.getString("Genero"), podcasts);
				podcasters.add(podcaster); // Agregar el músico a la lista de músicos
			}
			conexion.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return podcasters;
	}

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
			String consulta = "INSERT INTO Playlist_Canciones (IDList, IDAudio, fechaPlaylist_Cancion) " + "VALUES ("
					+ idLista + "," + idAudio + ", CURDATE());";
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

	public void obtenerYActualizarPlaylist(String usuario, JList<String> listaPlaylist) {
		DefaultListModel<String> listModel = new DefaultListModel<>();
		listaPlaylist.setModel(listModel); // Establecer el modelo de lista en el JList

		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			// Crear la consulta SQL
			String consulta1 = "SELECT p.Titulo FROM Playlist AS p "
					+ "INNER JOIN Cliente AS c ON p.IDCliente = c.IDCliente " + "WHERE c.Usuario = '" + usuario + "'";
			// Crea el PreparedStatement
			PreparedStatement preparedStatement1 = conexion.prepareStatement(consulta1);
			// Ejecutar la consulta y obtener el resultado
			ResultSet rs1 = preparedStatement1.executeQuery();
			// Leer el resultado y agregarlo al modelo de lista
			while (rs1.next()) {
				String titulo = rs1.getString("Titulo");
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

	public void borrarPlayList(JList<String> listaPlaylist, String usuario) {
		// TODO Auto-generated method stub
		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			// Crear la consulta SQL
			String consulta1 = "DELETE FROM Playlist WHERE IDCliente IN (SELECT IDCliente FROM Cliente WHERE Usuario = '"
					+ usuario + "') AND titulo = '" + listaPlaylist.getSelectedValue() + "';";

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

	public void obtenerYActualizarLista(JList<String> lista) {
		DefaultListModel<String> listModel = new DefaultListModel<>();
		lista.setModel(listModel); // Establecer el modelo de lista en el JList

		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta1 = "SELECT NombreArtistico FROM Musico";
			PreparedStatement preparedStatement1 = conexion.prepareStatement(consulta1);
			ResultSet rs1 = preparedStatement1.executeQuery();
			while (rs1.next()) {
				// Obtener el nombre del artista de la columna "NombreArtistico"
				String artista = rs1.getString("NombreArtistico");
				listModel.addElement(artista);
			}
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void borrarElementosLista(JList<String> lista) {
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);

			String artistaSeleccionado = lista.getSelectedValue();
			String consulta = "DELETE FROM Musico WHERE NombreArtistico = ?";

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

	public void modificarElementosLista(JList<String> lista, String nuevoNombre) {
		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);

			// Obtener el artista seleccionado en el JList
			String artistaSeleccionado = lista.getSelectedValue();
			String consulta = "UPDATE Musico SET NombreArtistico = ? WHERE NombreArtistico = ?";

			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);

			// Establecer los parámetros para el nuevo nombre y el nombre del artista
			// seleccionado
			preparedStatement.setString(1, nuevoNombre);
			preparedStatement.setString(2, artistaSeleccionado);

			// Ejecutar la consulta para modificar el nombre del artista seleccionado
			preparedStatement.executeUpdate();
			conexion.close();

			DefaultListModel<String> model = (DefaultListModel<String>) lista.getModel();
			// Modificar el elemento seleccionado en el modelo de lista
			int index = lista.getSelectedIndex();
			model.set(index, nuevoNombre);

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void agregarArtista(String nombreArtistico, String nombreImagen, String descripcion, String caracteristica) {
		try {
			// Conexión con la base de datos
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);

			// Crear la consulta SQL
			String consulta = "INSERT INTO Musico (NombreArtistico, Imagen, Descripción, Caracteristica) VALUES (?, ?, ?, ?)";

			// Crear el PreparedStatement
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.setString(1, nombreArtistico);
			preparedStatement.setString(2, nombreImagen);
			preparedStatement.setString(3, descripcion);
			preparedStatement.setString(4, caracteristica);

			// Ejecutar la consulta para agregar el nuevo artista
			preparedStatement.executeUpdate();

			// Cerrar la conexión
			conexion.close();

			System.out.println("Artista agregado correctamente a la base de datos.");
		} catch (SQLException ex) {
			System.out.println("Error al agregar el artista a la base de datos: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void insertarNuevaPlayList(Usuarios usuarioIniciado, String nombreLista, boolean importar, int[] numeros) {
		// TODO Auto-generated method stub
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "INSERT INTO Playlist (Titulo, FechaCreacion, IDCliente) VALUES ('" + nombreLista
					+ "', CURRENT_DATE(), " + usuarioIniciado.getIdUsuario() + ")";
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

	private int conseguirIDLista(String nombreLista, int idUsuario) {
		int idLista = 0;
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consultaridLista = "SELECT idList FROM PlayList Where idCliente =" + idUsuario + " and Titulo = '"
					+ nombreLista + "';";
			PreparedStatement preparedStatement = conexion.prepareStatement(consultaridLista);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				idLista = rs.getInt("idList");
			}
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return idLista;
	}

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
					String consulta = "INSERT INTO Playlist_Canciones (IDList, IDAudio, fechaPlaylist_Cancion) VALUES ("
							+ idLista + ", " + numeros[i] + ", CURRENT_DATE())";

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

	private int sacarUltimoIdAudio() {
		int ultimo = 0;
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "SELECT MAX(IDAudio) AS UltimoID FROM Audio;";
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

	public void audioReproducido(int idAudio) {
		// TODO Auto-generated method stub
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "UPDATE Estadisticas SET MasEscuchados = MasEscuchados + 1 WHERE IDAudio =" + idAudio
					+ ";";
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.executeUpdate();
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void anadirEstadisticasLike(int idAudio, int opcionEscogida) {
		// TODO Auto-generated method stub
		String consulta = "";
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			if (opcionEscogida == 0) {
				consulta = "UPDATE Estadisticas SET TopCanciones = TopCanciones + 1 WHERE IDAudio =" + idAudio + ";";
			} else {
				consulta = "UPDATE Estadisticas SET TopPodcast = TopPodcast + 1 WHERE IDAudio =" + idAudio + ";";
			}
			PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
			preparedStatement.executeUpdate();
			conexion.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void anadirCancionPlaylist(int idAudio, JList<String> listaMenu, int idLista) {
		// TODO Auto-generated method stub
		try {
			Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
			String consulta = "INSERT INTO Playlist_Canciones (IDList, IDAudio, fechaPlaylist_Cancion) VALUES ("
					+ idLista + ", " + idAudio + ", CURRENT_DATE())";
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

	public void añadirArtista(String nombreArtista, String descripcionArtista, Object selectedItem, JList<String> lista) {
	    try {
	        Connection conexion = DriverManager.getConnection(url, usuariobdd, contrasenabdd);
	        String consulta = "INSERT INTO Musico (NombreArtistico, Imagen, Descripcion, Caracteristica) VALUES (?, ?, ?, ?)";
	        PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
	        preparedStatement.setString(1, nombreArtista);
	        preparedStatement.setString(2, nombreArtista.toLowerCase() + ".jpg");
	        preparedStatement.setString(3, descripcionArtista);
	        preparedStatement.setString(4, selectedItem.toString()); // Convertimos selectedItem a String
	        DefaultListModel<String> model = (DefaultListModel<String>) lista.getModel();
			model.addElement(nombreArtista);
	        preparedStatement.executeUpdate();
	        conexion.close();
	    } catch (SQLException ex) {
	        System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
	    }
	}
}
