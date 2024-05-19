package vista;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import controlador.Metodos;
import controlador.MiExcepcion;
import modelo.Cancion;
import modelo.Album;
import modelo.Musico;
import modelo.Podcast;
import modelo.Podcaster;
import modelo.Usuarios;
import modelo.BasedeDatos;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Reto4Grupo5 extends JFrame {

	private static final long serialVersionUID = 1L;

	BasedeDatos basededatos = new BasedeDatos();
	Metodos metodos = new Metodos();
	private boolean premium = false;
	private JFormattedTextField txtFRegistroFecNac;
	private JPasswordField pswFRegistroContra, pswFRegistroConfContra;
	private JTextField txtFRegistroNombre, txtFRegistroUsuario, txtFRegistroApellido, txtFNombreCancionMenu;
	private JButton btnRegistroGuardar, btnRegistroEditar, btnReproducir, btnAdelanteCancion, btnAtrasCancion,
			btnGestArtistas, btnGestAlbumes, btnGestCanciones, btnGestPodcasts, btnGestPodcaster, btnAceptarPodcaster;
	private JPanel panelArtistas, panelAlbumes, panelCanciones, panelReproduccion, panelPlaylist, panelMenu,
			panelCrudMusica, panelBtnMenu, panelAdmin, panelGestPodcaster, panelMenuGestMusica, panelEstadisticas;
	private JLabel lblReproduciendoSelec, lblAlbumSelec, lblNombreCrudPodcaster, lblInfo1CrudPodcaster,
			lblInfo2CrudPodcaster;
	private JList<String> listaPlaylist, listaMenu, listaCrudMusica, listaPodcaster;
	private int elementoGestionado = 0;
	private static Clip clip;
	private String[] podcasterTotales;
	private static long clipTimePosition = 0;
	private Usuarios usuarioIniciado = null;
	private ArrayList<Musico> musicos = new ArrayList<Musico>();
	private ArrayList<Podcaster> podcasters = new ArrayList<Podcaster>();
	private Podcaster podcasterElegido = new Podcaster();
	private Musico musicoElegido = new Musico();
	private Album albumElegido = new Album();
	private int audioElegido = -1, posicionPodcast = -1, opcionEscogida = -1;
	private int accion = 0;
	private JComboBox<String> cmbxArtista;
	private JTextField txtFNombreCrudPodcaster, txtFInfo1CrudPodcaster, txtFInfo2CrudPodcaster;
	private JLabel lblInfo3CrudPodcast;
	private JComboBox<String> cmbxCrudPodcast;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Reto4Grupo5 frame = new Reto4Grupo5();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws MiExcepcion
	 */
	public Reto4Grupo5() throws MiExcepcion {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 692, 500);
		// -----------------------------------------Variables del
		// programa----------------------------------------------------------------
		Image imgPredeterminada = new ImageIcon(Paths.get("").toAbsolutePath().toString() + "\\img\\predeterminado.jpg")
				.getImage();
		String imagenArtistas1 = "\\img\\imagenArtistas1.jpg", imagenArtistas2 = "\\img\\imagenArtistas2.jpg";
		String idMenu = "Menu", idAlbum = "Album", idCanciones = "Canciones", idPlaylist = "Playlist",
				idReproducir = "Reproducir", idAdmin = "Admin", idMenuGestMusica = "Menu Gestion Musica",
				idbtnMenu = "btnMenu", idEstadisticas = "Estadisticas", idPanelCrudMusica = "CrudMusica",
				idLogin = "Interfaz de Login", idRegistro = "Registro", idArtistas = "Interfaz de elección del artista",
				idGestionPodcaster = "Gestion Podcaster";
		LocalDate fecha_registro = LocalDate.now();
		LocalDate fecha_finpremium = fecha_registro.plusYears(1);
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String diaString = fecha_registro.format(formato);
		String premiumfinal = fecha_finpremium.format(formato);
		String admin = "ADMINISTRADOR", cliente = "CLIENTE";
		musicos = basededatos.conseguirArtistas();
		podcasters = basededatos.conseguirPodcasters();

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

		JLayeredPane layeredPane = new JLayeredPane();
		contentPane.add(layeredPane, "name_610782340430400");
		layeredPane.setLayout(new CardLayout(0, 0));

//-------------------------------------------Menu Bar-------------------------------------------------------------------
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JButton btnPerfil = new JButton("Inicia Sesion");
		btnPerfil.setEnabled(false);
		/**
		 * Te lleva al perfil con los datos rellenados para poder editar el usuario
		 */
		btnPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRegistroGuardar.setEnabled(false);
				btnRegistroEditar.setVisible(true);
				metodos.cambiardePantalla(layeredPane, idRegistro);
				txtFRegistroNombre.setText(usuarioIniciado.getNombre());
				txtFRegistroApellido.setText(usuarioIniciado.getApellido());
				txtFRegistroFecNac.setText(usuarioIniciado.getFec_nacimiento());
				pswFRegistroContra.setText(usuarioIniciado.getContrasena());

			}
		});
		menuBar.add(btnPerfil);

		JButton btnAtras = new JButton("Atras");
		btnAtras.setEnabled(false);
		/**
		 * Va hacia el panel anterior dependiendo de en cual este
		 */
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (panelArtistas.isVisible() || panelPlaylist.isVisible()) {
					metodos.cambiardePantalla(layeredPane, idMenu);

				} else if (panelAlbumes.isVisible()) {
					metodos.cambiardePantalla(layeredPane, idArtistas);

				} else if (panelCanciones.isVisible()) {
					metodos.cambiardePantalla(layeredPane, idAlbum);
				} else if (panelReproduccion.isVisible()) {
					if (opcionEscogida == 0) {
						metodos.cambiardePantalla(layeredPane, idCanciones);
					} else {
						metodos.cambiardePantalla(layeredPane, idAlbum);
					}
					btnReproducir.setText("Reproducir");
					if (!(clip == null) && clip.isRunning()) {
						clip.stop();
					}
				} else if (panelMenu.isVisible() || panelAdmin.isVisible()) {
					metodos.cambiardePantalla(layeredPane, idLogin);
					btnAtras.setEnabled(false);
					btnPerfil.setEnabled(false);
					btnPerfil.setText("Inicia Sesion");
				} else if (panelCrudMusica.isVisible() || panelGestPodcaster.isVisible()
						|| panelEstadisticas.isVisible()) {
					metodos.cambiardePantalla(layeredPane, idMenuGestMusica);
				} else if (panelBtnMenu.isVisible()) {
					metodos.cambiardePantalla(layeredPane, idReproducir);
				} else if (panelMenuGestMusica.isVisible()) {
					metodos.cambiardePantalla(layeredPane, idAdmin);
				}
			}

		});
		menuBar.add(btnAtras);

		// -------------------------------------------Final menu Bar, Inicio Panel
		// Login---------------------------------------------------------
		JPanel panelLogin = new JPanel();
		layeredPane.add(panelLogin, idLogin);
		panelLogin.setLayout(null);

		JLabel lblPreguntaCuenta = new JLabel("¿No tienes cuenta?\r\n");
		lblPreguntaCuenta.setForeground(Color.WHITE);
		lblPreguntaCuenta.setBounds(387, 376, 117, 14);
		panelLogin.add(lblPreguntaCuenta);

		JLabel lblInicioDeSesin = new JLabel("INICIO DE SESIÓN\r\n");
		lblInicioDeSesin.setForeground(Color.WHITE);
		lblInicioDeSesin.setFont(new Font("MS Gothic", Font.BOLD, 22));
		lblInicioDeSesin.setBounds(387, 50, 216, 24);
		panelLogin.add(lblInicioDeSesin);

		JLabel lblTipo = new JLabel("Tipo de Usuario");
		lblTipo.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 22));
		lblTipo.setForeground(Color.WHITE);
		lblTipo.setBounds(406, 242, 175, 24);
		panelLogin.add(lblTipo);

		JPanel panel_FotosFondo = new JPanel();
		panel_FotosFondo.setBackground(new Color(0, 0, 51));
		panel_FotosFondo.setBounds(0, 0, 339, 436);
		panelLogin.add(panel_FotosFondo);
		panel_FotosFondo.setLayout(null);

		JLabel lblImagenesArtistas = new JLabel("");
		lblImagenesArtistas
				.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + "\\img\\imagenartistas1.jpg"));
		lblImagenesArtistas.setBounds(10, 146, 319, 179);
		panel_FotosFondo.add(lblImagenesArtistas);
		metodos.cambiarImagenArtistas(lblImagenesArtistas, imagenArtistas1, imagenArtistas2);

		JLabel lblDescubre = new JLabel(
				"<html><p text-align: center> Descubre <br> A Nuestros Mejores <br> Artistas</p></html>");
		lblDescubre.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescubre.setForeground(Color.WHITE);
		lblDescubre.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
		lblDescubre.setBounds(70, 27, 191, 100);
		panel_FotosFondo.add(lblDescubre);

		JLabel lblFondo = new JLabel("");

		lblFondo.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + "\\img\\categorias.jpg"));
		lblFondo.setBounds(0, 0, 339, 425);
		panel_FotosFondo.add(lblFondo);

		JTextField txtFUsuario = new JTextField();
		txtFUsuario.setFont(new Font("MS Gothic", Font.BOLD, 12));
		txtFUsuario.setBounds(406, 137, 227, 24);
		panelLogin.add(txtFUsuario);
		txtFUsuario.setColumns(10);

		JPasswordField pswFContra = new JPasswordField();
		pswFContra.setFont(new Font("MS Gothic", Font.BOLD, 12));
		pswFContra.setBounds(406, 207, 227, 24);
		panelLogin.add(pswFContra);

		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setForeground(Color.WHITE);
		lblUsuario.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 22));
		lblUsuario.setBounds(406, 102, 216, 24);
		panelLogin.add(lblUsuario);

		JLabel lblContraseña = new JLabel("Contraseña");
		lblContraseña.setForeground(Color.WHITE);
		lblContraseña.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 22));
		lblContraseña.setBounds(406, 172, 216, 24);
		panelLogin.add(lblContraseña);

		JLabel lblCrearCuenta = new JLabel("Create una Cuenta");
		lblCrearCuenta.addMouseListener(new MouseAdapter() {
			/**
			 * va al panel de registro y establece todos los campos abiertos
			 * 
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				metodos.cambiardePantalla(layeredPane, idRegistro);
				txtFRegistroNombre.setEnabled(true);
				txtFRegistroApellido.setEnabled(true);
				txtFRegistroUsuario.setEnabled(true);
				pswFRegistroContra.setEnabled(true);
				txtFRegistroFecNac.setEnabled(true);
				pswFRegistroConfContra.setEnabled(true);
			}
		});
		lblCrearCuenta.setForeground(UIManager.getColor("List.selectionBackground"));
		lblCrearCuenta.setBounds(528, 376, 117, 14);
		panelLogin.add(lblCrearCuenta);

		JComboBox<String> cmbxTipo = new JComboBox<String>();
		cmbxTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * si el usuario va a iniciar sesion como administrador desaparecera la pregunta
				 * de iniciar sesion
				 */
				if (cmbxTipo.getSelectedItem().equals(admin)) {
					lblPreguntaCuenta.setVisible(false);
					lblCrearCuenta.setVisible(false);
				} else {
					lblPreguntaCuenta.setVisible(true);
					lblCrearCuenta.setVisible(true);
				}
			}
		});
		cmbxTipo.setModel(new DefaultComboBoxModel<String>(new String[] { cliente, admin }));
		cmbxTipo.setFont(new Font("MS Gothic", Font.BOLD, 13));
		cmbxTipo.setBounds(406, 278, 227, 22);
		panelLogin.add(cmbxTipo);

		JButton btnIniciarSesion = new JButton("Iniciar Sesión");
		btnIniciarSesion.addActionListener(new ActionListener() {
			/**
			 * comprueba el inicio de sesion, prepara el boton de perfil y cambia de
			 * pantalla dependiendo del usuario iniciado
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				// metodo que recoge el usuario introducido y comprueba si este esta en la base
				// de datos
				usuarioIniciado = basededatos.inicioSesion(txtFUsuario.getText(), new String(pswFContra.getPassword()));
				// si el usuario existe y el combobox es cliente, se inicia sesion correctamente
				if (usuarioIniciado != null && cmbxTipo.getSelectedItem().equals(cliente)) {
					JOptionPane.showMessageDialog(null, "Sesión iniciada correctamente");
					btnPerfil.setText(usuarioIniciado.getUsuario());
					btnPerfil.setEnabled(true);
					metodos.cambiardePantalla(layeredPane, idMenu);
					btnAtras.setEnabled(true);
					// si las credenciales de admin introducidas por el usuario coinciden entra en
					// el panel de admin
				} else if ((cmbxTipo.getSelectedItem().equals(admin) && txtFUsuario.getText().equals("admin")
						&& new String(pswFContra.getPassword()).equals("123"))) {
					metodos.cambiardePantalla(layeredPane, idAdmin);
					btnAtras.setEnabled(true);
					// salta error en caso de no coincidir
				} else {
					JOptionPane.showMessageDialog(null,
							"No se ha podido iniciar sesión. Usuario o contraseña incorrectos");
					txtFUsuario.setText("");
					pswFContra.setText("");

				}
			}

		});
		btnIniciarSesion.setForeground(Color.BLACK);
		btnIniciarSesion.setFont(new Font("MS Gothic", Font.BOLD, 12));
		btnIniciarSesion.setBackground(Color.LIGHT_GRAY);
		btnIniciarSesion.setBounds(454, 331, 127, 23);
		panelLogin.add(btnIniciarSesion);

		JLabel lblFondoNeon = new JLabel("");
		lblFondoNeon.setIcon(new ImageIcon((Paths.get("").toAbsolutePath().toString() + "\\img\\neon.jpg")));
		lblFondoNeon.setBounds(339, 0, 337, 428);
		panelLogin.add(lblFondoNeon);

		// ------------------------------------------- Fin Panel Login, Inicio Panel
		// Registro----------------------------------------------------------------
		JPanel panelRegistro = new JPanel();
		panelRegistro.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelRegistro, idRegistro);
		panelRegistro.setLayout(null);

		JLabel lblRegistroContra = new JLabel("Contraseña");
		lblRegistroContra.setForeground(Color.BLACK);
		lblRegistroContra.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRegistroContra.setBounds(38, 154, 114, 14);
		panelRegistro.add(lblRegistroContra);

		JLabel lblRegistroApellido = new JLabel("Apellido");
		lblRegistroApellido.setForeground(Color.BLACK);
		lblRegistroApellido.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRegistroApellido.setBounds(38, 79, 114, 27);
		panelRegistro.add(lblRegistroApellido);

		JLabel lblRegistroNombre = new JLabel("Nombre");
		lblRegistroNombre.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRegistroNombre.setForeground(Color.BLACK);
		lblRegistroNombre.setBounds(38, 54, 114, 14);
		panelRegistro.add(lblRegistroNombre);

		JLabel lblRegistroUsuario = new JLabel("Usuario");
		lblRegistroUsuario.setForeground(Color.BLACK);
		lblRegistroUsuario.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRegistroUsuario.setBounds(38, 118, 114, 14);
		panelRegistro.add(lblRegistroUsuario);

		JLabel lblRegistroConfContra = new JLabel("Confirmar Contraseña");
		lblRegistroConfContra.setForeground(Color.BLACK);
		lblRegistroConfContra.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRegistroConfContra.setBounds(38, 195, 188, 14);
		panelRegistro.add(lblRegistroConfContra);

		JLabel lblRegistroFecNac = new JLabel("Fecha de Nacimiento");
		lblRegistroFecNac.setForeground(Color.BLACK);
		lblRegistroFecNac.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRegistroFecNac.setBounds(38, 240, 234, 14);
		panelRegistro.add(lblRegistroFecNac);

		JLabel lblRegistroFecReg = new JLabel("Fecha de Registro");
		lblRegistroFecReg.setForeground(Color.BLACK);
		lblRegistroFecReg.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRegistroFecReg.setBounds(38, 278, 234, 27);
		panelRegistro.add(lblRegistroFecReg);

		JLabel lblRegistroFecLim = new JLabel("Vencimiento del Premium");
		lblRegistroFecLim.setVisible(false);
		lblRegistroFecLim.setForeground(Color.BLACK);
		lblRegistroFecLim.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRegistroFecLim.setBounds(38, 316, 234, 27);
		panelRegistro.add(lblRegistroFecLim);

		txtFRegistroNombre = new JTextField();
		txtFRegistroNombre.setEnabled(false);
		txtFRegistroNombre.setBounds(223, 55, 165, 20);
		panelRegistro.add(txtFRegistroNombre);
		txtFRegistroNombre.setColumns(10);

		txtFRegistroUsuario = new JTextField();
		txtFRegistroUsuario.setEnabled(false);
		txtFRegistroUsuario.setColumns(10);
		txtFRegistroUsuario.setBounds(223, 117, 165, 20);
		panelRegistro.add(txtFRegistroUsuario);
		try {
			// aplica un formato a un formated textField para que el usuario no pueda meter
			// algo que no sea una fecha
			MaskFormatter formatos = new MaskFormatter("####-##-##");
			formatos.setPlaceholderCharacter('-');
			txtFRegistroFecNac = new JFormattedTextField(formatos);
			txtFRegistroFecNac.setEnabled(false);
			txtFRegistroFecNac.setColumns(10);
			txtFRegistroFecNac.setBounds(223, 239, 165, 20);
			panelRegistro.add(txtFRegistroFecNac);
		} catch (ParseException ex) {
			throw new MiExcepcion("Error al crear el formato de fecha", ex.getErrorOffset());
		}

		JTextField txtFRegistroFecReg = new JTextField();
		txtFRegistroFecReg.setEditable(false);
		txtFRegistroFecReg.setColumns(10);
		txtFRegistroFecReg.setBounds(223, 283, 165, 20);
		panelRegistro.add(txtFRegistroFecReg);
		txtFRegistroFecReg.setText(diaString);

		JTextField txtFRegistroFecLim = new JTextField(premiumfinal);
		txtFRegistroFecLim.setVisible(false);
		txtFRegistroFecLim.setEditable(false);
		txtFRegistroFecLim.setColumns(10);
		txtFRegistroFecLim.setBounds(223, 321, 165, 20);
		panelRegistro.add(txtFRegistroFecLim);

		txtFRegistroApellido = new JTextField();
		txtFRegistroApellido.setEnabled(false);
		txtFRegistroApellido.setColumns(10);
		txtFRegistroApellido.setBounds(223, 86, 165, 20);
		panelRegistro.add(txtFRegistroApellido);

		pswFRegistroContra = new JPasswordField();
		pswFRegistroContra.setEnabled(false);
		pswFRegistroContra.setBounds(223, 153, 165, 19);
		panelRegistro.add(pswFRegistroContra);

		pswFRegistroConfContra = new JPasswordField();
		pswFRegistroConfContra.setEnabled(false);
		pswFRegistroConfContra.setBounds(223, 194, 165, 19);
		panelRegistro.add(pswFRegistroConfContra);

		JButton btnConfirmarCambios = new JButton("Confirmar Cambios");
		btnConfirmarCambios.addActionListener(new ActionListener() {
			/**
			 * Modifica los datos del usuario que estaba iniciado y le vuelve al login
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				basededatos.cambiarDatos(txtFRegistroNombre.getText(), txtFRegistroApellido.getText(),
						new String(pswFRegistroContra.getPassword()), txtFRegistroFecNac.getText(),
						usuarioIniciado.getUsuario());
				JOptionPane.showMessageDialog(null, "Datos Guardados con exito");
				metodos.cambiardePantalla(layeredPane, idLogin);
				btnPerfil.setText("Inicia Sesion");
				btnPerfil.setEnabled(false);
				btnConfirmarCambios.setVisible(false);
				txtFRegistroNombre.setEnabled(false);
				txtFRegistroApellido.setEnabled(false);
				pswFRegistroContra.setEnabled(false);
				txtFRegistroFecNac.setEnabled(false);
			}
		});
		btnConfirmarCambios.setVisible(false);
		btnConfirmarCambios.setBackground(new Color(215, 223, 234));
		btnConfirmarCambios.setBounds(489, 52, 145, 23);
		panelRegistro.add(btnConfirmarCambios);

		// editar datos
		btnRegistroEditar = new JButton("Editar datos");
		btnRegistroEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtFRegistroNombre.setEnabled(true);
				txtFRegistroApellido.setEnabled(true);
				pswFRegistroContra.setEnabled(true);
				txtFRegistroFecNac.setEnabled(true);
				btnConfirmarCambios.setVisible(true);
			}
		});
		btnRegistroEditar.setVisible(false);
		btnRegistroEditar.setBackground(new Color(215, 223, 234));
		btnRegistroEditar.setBounds(489, 238, 145, 23);
		panelRegistro.add(btnRegistroEditar);

		btnRegistroGuardar = new JButton("Guardar Datos");
		btnRegistroGuardar.addActionListener(new ActionListener() {
			/**
			 * registra el usuario en la base de datos y cambia al menu sino, saltara un
			 * error
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				boolean registrado = false;
				// metodo para registrarse
				registrado = metodos.registrarUsuario(txtFRegistroNombre.getText(), txtFRegistroApellido.getText(),
						txtFRegistroUsuario.getText(), new String(pswFRegistroContra.getPassword()),
						new String(pswFRegistroConfContra.getPassword()), txtFRegistroFecNac.getText(),
						txtFRegistroFecReg.getText(), txtFRegistroFecLim.getText(), premium);
				if (registrado) {
					btnRegistroGuardar.setEnabled(false);
					btnRegistroEditar.setVisible(true);
					txtFRegistroNombre.setEnabled(false);
					txtFRegistroApellido.setEnabled(false);
					txtFRegistroUsuario.setEnabled(false);
					txtFRegistroFecLim.setEnabled(false);
					pswFRegistroContra.setEnabled(false);
					pswFRegistroConfContra.setEnabled(false);
					txtFRegistroFecNac.setEnabled(false);
					metodos.cambiardePantalla(layeredPane, idLogin);

				}
			}
		});
		btnRegistroGuardar.setBackground(new Color(215, 223, 234));
		btnRegistroGuardar.setBounds(489, 278, 145, 23);
		panelRegistro.add(btnRegistroGuardar);

		JButton btnRegistroComprar = new JButton("Comprar Premium");
		btnRegistroComprar.addActionListener(new ActionListener() {
			/**
			 * Activa el premium
			 */
			public void actionPerformed(ActionEvent e) {
				premium = true;
				JOptionPane.showMessageDialog(null, "Premium Activado");
				lblRegistroFecLim.setVisible(true);
				txtFRegistroFecLim.setVisible(true);

			}
		});
		btnRegistroComprar.setBackground(new Color(215, 223, 234));
		btnRegistroComprar.setBounds(489, 320, 145, 23);
		panelRegistro.add(btnRegistroComprar);

//------------------------------------------- Fin Panel Registro, Inicio Panel Menu----------------------------------------------------------------
		panelMenu = new JPanel();
		panelMenu.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelMenu, idMenu);
		panelMenu.setLayout(null);
		JLabel lblNuevaMus = new JLabel("");
		lblNuevaMus.addMouseListener(new MouseAdapter() {
			/**
			 * Crea dinamicamente los artistas con sus respectivos albumes y canciones
			 * dinamicamente a traves de contadores y cambiando de posicion con variables,
			 * se mueve entre los paneles de artistas, albumes y canciones inserta la
			 * respectiva imagen y si no existe pone la imagen predeterminado, al final de
			 * la ejecucion la cancion seleecionada podra ser reproducida en el panel de
			 * reproduccion
			 * 
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				metodos.borrarPanel(panelArtistas);
				btnAtras.setEnabled(true);
				metodos.cambiardePantalla(layeredPane, idArtistas);
				int cambioX = 0;
				int cambioY = 0;
				int cont = 0;
				int cont2 = 0;
				// ----------------------------------------------------------------- Musica
				// --------------------------------------------------------------------------------------------
				// se generan los artistas
				do {

					JLabel lblFoto = new JLabel();
					lblFoto.addMouseListener(new MouseAdapter() {
						// se generan los albumes
						public void mouseClicked(MouseEvent e) {
							opcionEscogida = 0; // establece que se ha pinchado sobre musica
							metodos.cambiardePantalla(layeredPane, idAlbum);
							metodos.borrarPanel(panelAlbumes);
							int cont = 0;
							int cambioY = 0;
							// asigna un objeto musico en funcion de cual haya sido seleccionado
							musicoElegido = musicos.get(Integer.parseInt(lblFoto.getToolTipText()));
							if (!musicoElegido.getAlbumes().isEmpty()) { // si los albumes de ese musico estan vacios no
																			// entrara y saltara una notificacion
								do {
									JLabel lblalbumFoto = new JLabel();
									if (new File(Paths.get("").toAbsolutePath().toString() + "/img/"
											+ musicoElegido.getAlbumes().get(cont).getImagen()).exists()) {
										lblalbumFoto.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString()
												+ "/img/" + musicoElegido.getAlbumes().get(cont).getImagen()));
									} else {
										// Si no se encuentra la imagen, establece una imagen predeterminada
										ImageIcon iconoPredeterminado = new ImageIcon(
												imgPredeterminada.getScaledInstance(65, 63, Image.SCALE_SMOOTH));
										lblalbumFoto.setIcon(iconoPredeterminado);
									}
									lblalbumFoto.addMouseListener(new MouseAdapter() {
										// se generan las canciones
										public void mouseClicked(MouseEvent e) {
											int cambioX = 0;
											int cambioY = 0;
											int cont = 0;
											metodos.cambiardePantalla(layeredPane, idCanciones);
											metodos.borrarPanel(panelCanciones);
											// asigna un objeto album en funcion del escogido
											albumElegido = musicoElegido.getAlbumes()
													.get(Integer.parseInt(lblalbumFoto.getToolTipText()));
											Image img = new ImageIcon(Paths.get("").toAbsolutePath().toString()
													+ "\\img\\" + albumElegido.getImagen()).getImage();
											ImageIcon img2 = new ImageIcon(
													img.getScaledInstance(108, 117, Image.SCALE_SMOOTH));
											if (!albumElegido.getCanciones().isEmpty()) {// si ese album no contiene
																							// canciones saldra una
																							// notificacion
												do {
													JLabel lblFotoCancion = new JLabel();
													lblFotoCancion.addMouseListener(new MouseAdapter() {
														public void mouseClicked(MouseEvent e) {
															metodos.cambiardePantalla(layeredPane, idReproducir);
															audioElegido = Integer
																	.parseInt(lblFotoCancion.getToolTipText());
														}
													});
													if (new File(Paths.get("").toAbsolutePath().toString() + "\\img\\"
															+ albumElegido.getImagen()).exists()) {
														lblFotoCancion.setIcon(img2);
													} else {
														ImageIcon iconoPredeterminado = new ImageIcon(imgPredeterminada
																.getScaledInstance(108, 117, Image.SCALE_SMOOTH));
														lblFotoCancion.setIcon(iconoPredeterminado);
													}

													lblFotoCancion.setToolTipText(String.valueOf(cont));
													lblFotoCancion.setBounds(122 + cambioX, 90 + cambioY, 108, 117);
													panelCanciones.add(lblFotoCancion);

													JLabel lblNombreCancion = new JLabel(
															albumElegido.getCanciones().get(cont).getNombre());
													lblNombreCancion.setFont(new Font("SansSerif", Font.BOLD, 14));
													lblNombreCancion.setBounds(122 + cambioX, 208 + cambioY, 150, 32);
													panelCanciones.add(lblNombreCancion);
													// cambia la posicion autogenerada de las fotos
													if (cont % 2 == 0) {
														cambioX = 293;
													} else {
														cambioX = 0;
														cambioY = cambioY + 147;
													}
													cont++;
												} while (cont != albumElegido.getCanciones().size());
												cambioY = 0;
												cont = 0;

												JLabel lblNombre = new JLabel("<html><h1><i>"
														+ albumElegido.getTitulo().toUpperCase() + "</i></h1></html>");
												lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 17));
												lblNombre.setBounds(30, 30, 347, 58);
												panelCanciones.add(lblNombre);

											} else {
												JOptionPane.showMessageDialog(null,
														"El Album no tiene canciones actualmente, vuelva a la seleccion de artistas");

											}

										}
									});
									lblalbumFoto.setBounds(80, 81 + cambioY, 65, 63);
									lblalbumFoto.setToolTipText(String.valueOf(cont));
									panelAlbumes.add(lblalbumFoto);

									JLabel labeltxtAlbum = new JLabel(musicoElegido.getAlbumes().get(cont).getTitulo());
									labeltxtAlbum.setBounds(151, 107 + cambioY, 174, 22);
									panelAlbumes.add(labeltxtAlbum);

									cambioY = cambioY + 74;
									cont++;
								} while (cont != musicoElegido.getAlbumes().size());
								cambioY = 0;
								cont = 0;
								JLabel lblfotoDescripcion = new JLabel("");
								if (new File(Paths.get("").toAbsolutePath().toString() + "/img/"
										+ musicoElegido.getNombreArtista().replace(" ", "") + "Desc.jpg").exists()) {
									lblfotoDescripcion
											.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + "/img/"
													+ musicoElegido.getNombreArtista().replace(" ", "") + "Desc.jpg"));
								} else {

									// Si no se encuentra la imagen, establece una imagen predeterminada
									ImageIcon iconoPredeterminado = new ImageIcon(
											imgPredeterminada.getScaledInstance(188, 176, Image.SCALE_SMOOTH));
									lblfotoDescripcion.setIcon(iconoPredeterminado);
								}
								lblfotoDescripcion.setBounds(405, 11, 188, 176);
								panelAlbumes.add(lblfotoDescripcion);

								JLabel lblNombre = new JLabel("<html><h1><i>"
										+ musicoElegido.getNombreArtista().toUpperCase() + "</i></h1></html>");
								lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 17));
								lblNombre.setBounds(29, 11, 257, 59);
								panelAlbumes.add(lblNombre);

								JLabel lblResumenArtista = new JLabel(
										"<html><p>" + musicoElegido.getDescripcion() + "</p></html>");
								lblResumenArtista.setBounds(345, 168, 307, 235);
								panelAlbumes.add(lblResumenArtista);

							} else {
								JOptionPane.showMessageDialog(null,
										"El Artista no tiene albumes actualmente, vuelva a la seleccion de artistas");
							}
						}
					});
					if (new File(Paths.get("").toAbsolutePath().toString() + "/img/" + musicos.get(cont).getImagen())
							.exists()) {
						lblFoto.setIcon(new ImageIcon(
								Paths.get("").toAbsolutePath().toString() + "/img/" + musicos.get(cont).getImagen()));
					} else {
						// Si no se encuentra la imagen, establece una imagen predeterminada
						ImageIcon iconoPredeterminado = new ImageIcon(
								imgPredeterminada.getScaledInstance(147, 131, Image.SCALE_SMOOTH));
						lblFoto.setIcon(iconoPredeterminado);
					}
					lblFoto.setBounds(49 + cambioX, 30 + cambioY, 147, 131);
					lblFoto.setToolTipText(String.valueOf(cont));
					panelArtistas.add(lblFoto);

					JLabel lblTextoArtista = new JLabel(musicos.get(cont).getNombreArtista());
					lblTextoArtista.setHorizontalAlignment(SwingConstants.CENTER);
					lblTextoArtista.setFont(new Font("Sitka Banner", Font.PLAIN, 17));
					lblTextoArtista.setBounds(49 + cambioX, 172 + cambioY, 147, 23);
					panelArtistas.add(lblTextoArtista);
					cont++;
					cont2++;
					// cambia la posicion autogenerada de las fotos
					if (cont2 != 3) {
						cambioX = cambioX + 228;
					} else {
						cambioX = 0;
						cambioY = cambioY + 176;
						cont2 = 0;
					}

				} while (cont != musicos.size());

			}
		});
		lblNuevaMus.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + "/img/descubririMúsica.jpg"));
		lblNuevaMus.setBounds(92, 65, 124, 120);
		panelMenu.add(lblNuevaMus);

		// ----------------------------------------------------------------- Podcast
		// --------------------------------------------------------------------------------------------
		JLabel lblNuevoPod = new JLabel("");
		lblNuevoPod.addMouseListener(new MouseAdapter() {
			/**
			 * Crea dinamicamente los podcasters con sus respectivos podcasts dinamicamente
			 * a traves de contadores y cambiando de posicion con variables, se mueve entre
			 * los paneles de podcaster y podcast inserta la respectiva imagen y si no
			 * existe pone la imagen predeterminado, al final de la ejecucion la podcast
			 * seleecionado podra ser reproducido en el panel de reproduccion
			 * 
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				opcionEscogida = 1; // establece que se ha pinchado sobre podcast
				metodos.borrarPanel(panelArtistas);
				btnAtras.setEnabled(true);
				metodos.cambiardePantalla(layeredPane, idArtistas);
				podcasters = basededatos.conseguirPodcasters();
				int cont = 0;
				int cambioX = 0;
				int cambioY = 0;
				int cont2 = 0;
				if (!podcasters.isEmpty()) {
					do { // crea los podcasters segun los que existan en la base de datos
						JLabel lblFoto = new JLabel();
						lblFoto.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent e) {
								metodos.cambiardePantalla(layeredPane, idAlbum);
								// establece un objeto podcaster segun el que haya seleccionado el usuario
								podcasterElegido = podcasters.get(Integer.parseInt(lblFoto.getToolTipText()));
								metodos.borrarPanel(panelAlbumes);
								int cont = 0;
								int cambioY = 0;
								if (!podcasterElegido.getPodcasts().isEmpty()) {
									do { // crea los podcast segun el podcaster elegido
										JLabel lblCapitulo = new JLabel(
												podcasterElegido.getPodcasts().get(cont).getNombre());
										lblCapitulo.addMouseListener(new MouseAdapter() {
											public void mouseClicked(MouseEvent e) {
												metodos.cambiardePantalla(layeredPane, "Reproducir");
												audioElegido = Integer.parseInt(lblCapitulo.getToolTipText());
											}
										});
										lblCapitulo.setToolTipText(String.valueOf(cont));
										lblCapitulo.setBounds(29, 81 + cambioY, 270, 59);
										panelAlbumes.add(lblCapitulo);
										cambioY = cambioY + 50;
										cont++;
									} while (cont != podcasterElegido.getPodcasts().size());
									cambioY = 0;
									cont = 0;

									JLabel lblfotoArtista = new JLabel("");
									if (new File(Paths.get("").toAbsolutePath().toString() + "/img/"
											+ podcasterElegido.getNombreArtista().replace(" ", "") + "Desc.jpg")
											.exists()) {
										lblfotoArtista.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString()
												+ "/img/" + podcasterElegido.getNombreArtista().replace(" ", "")
												+ "Desc.jpg"));
									} else {
										// Si no se encuentra la imagen, establece una imagen predeterminada
										ImageIcon iconoPredeterminado = new ImageIcon(
												imgPredeterminada.getScaledInstance(188, 176, Image.SCALE_SMOOTH));
										lblfotoArtista.setIcon(iconoPredeterminado);
									}
									lblfotoArtista.setBounds(405, 60, 188, 176);
									panelAlbumes.add(lblfotoArtista);

									JLabel lblNombre = new JLabel("<html><h1><i>"
											+ podcasterElegido.getNombreArtista().toUpperCase() + "</i></h1></html>");
									lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 17));
									lblNombre.setBounds(29, 11, 257, 59);
									panelAlbumes.add(lblNombre);

									JLabel lblResumenArtista = new JLabel(
											"<html><p>" + podcasterElegido.getDescripcion() + "</p></html>");
									lblResumenArtista.setBounds(345, 168, 307, 235);
									panelAlbumes.add(lblResumenArtista);
								} else {
									JOptionPane.showMessageDialog(null,
											"este podcaster actualmente no tiene podcast disponibles");
								}
							}
						});
						if (new File(
								Paths.get("").toAbsolutePath().toString() + "/img/" + podcasters.get(cont).getImagen())
								.exists()) {
							lblFoto.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + "/img/"
									+ podcasters.get(cont).getImagen()));
						} else {
							// Si no se encuentra la imagen, establece una imagen predeterminada
							ImageIcon iconoPredeterminado = new ImageIcon(
									imgPredeterminada.getScaledInstance(147, 147, Image.SCALE_SMOOTH));
							lblFoto.setIcon(iconoPredeterminado);
						}
						lblFoto.setBounds(49 + cambioX, 56 + cambioY, 147, 147);
						lblFoto.setToolTipText(String.valueOf(cont));
						panelArtistas.add(lblFoto);

						JLabel lblTextoArtista = new JLabel(podcasterElegido.getNombreArtista());
						lblTextoArtista.setHorizontalAlignment(SwingConstants.CENTER);
						lblTextoArtista.setFont(new Font("Sitka Banner", Font.PLAIN, 17));
						lblTextoArtista.setBounds(57 + cambioX, 205 + cambioY, 130, 54);
						panelArtistas.add(lblTextoArtista);
						cont++;
						cont2++;
						// modifica la posicion segun la cantidad de podcasters que haya generado
						if (cont2 != 3) {
							cambioX = cambioX + 228;
						} else {
							cambioX = 0;
							cambioY = cambioY + 176;
							cont2 = 0;
						}

					} while (cont != podcasters.size());
				} else {
					JOptionPane.showMessageDialog(null, "No hay podcasts disponibles");
				}
				// cambia la posicion donde se generan los podcasters
				cambioX = 0;
				cambioY = 0;
				cont = 0;
				cont2 = 0;

			}
		});
		lblNuevoPod.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + "/img/podcast.jpg"));
		lblNuevoPod.setBounds(283, 65, 124, 120);
		panelMenu.add(lblNuevoPod);

		// ----------------------------------------------------------------- PlayList
		// --------------------------------------------------------------------------------------------
		JLabel lblPlayList = new JLabel("");
		lblPlayList.addMouseListener(new MouseAdapter() {
			/**
			 * Entra en el panel de playlist y muestra todas las playlist del usuario
			 * iniciado en una lista
			 * 
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				btnAtras.setEnabled(true);
				metodos.cambiardePantalla(layeredPane, idPlaylist);
				DefaultListModel<String> listModel = new DefaultListModel<>();
				listaPlaylist.setModel(listModel); // Establecer el modelo de lista en el JList
				for (int i = 0; i != usuarioIniciado.getPlaylists().size(); i++) {
					listModel.addElement(usuarioIniciado.getPlaylists().get(i).getTitulo());
				}
			}
		});
		lblPlayList.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + "/img/misplaylist (1).jpg"));
		lblPlayList.setBounds(474, 65, 124, 120);
		panelMenu.add(lblPlayList);

		JLabel lblNuevaMustxt = new JLabel("<html><p text-align: center>Descubre <br>Música Nueva</p></html>");
		lblNuevaMustxt.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNuevaMustxt.setHorizontalAlignment(SwingConstants.CENTER);
		lblNuevaMustxt.setFont(new Font("Perpetua", Font.PLAIN, 18));
		lblNuevaMustxt.setBounds(75, 187, 162, 54);
		panelMenu.add(lblNuevaMustxt);

		JLabel lblNuevoPodtxt = new JLabel("<html><p text-align: center>Descubre <br>Podcasts Nuevos</p></html>");
		lblNuevoPodtxt.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNuevoPodtxt.setHorizontalAlignment(SwingConstants.CENTER);
		lblNuevoPodtxt.setFont(new Font("Perpetua", Font.PLAIN, 18));
		lblNuevoPodtxt.setBounds(260, 187, 162, 54);
		panelMenu.add(lblNuevoPodtxt);

		JLabel lblPlayListtxt = new JLabel("<html><p text-align: center>Accede A<br>Tus Playlists</p></html>");
		lblPlayListtxt.setHorizontalTextPosition(SwingConstants.CENTER);
		lblPlayListtxt.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayListtxt.setFont(new Font("Perpetua", Font.PLAIN, 18));
		lblPlayListtxt.setBounds(448, 183, 162, 63);
		panelMenu.add(lblPlayListtxt);

//------------------------------------------- Fin Panel Menu, Inicio Panel Artistas----------------------------------------------------------------
		panelArtistas = new JPanel();
		panelArtistas.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelArtistas, idArtistas);
		panelArtistas.setLayout(null);
//------------------------------------------- Fin Panel Artistas, Inicio Panel Albumes----------------------------------------------------------------

		panelAlbumes = new JPanel();
		panelAlbumes.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelAlbumes, idAlbum);
		panelAlbumes.setLayout(null);

		// ------------------------------------------- Fin Panel Albumes, Inicio Panel
		// Reproduccion----------------------------------------------------------------

		panelReproduccion = new JPanel();
		panelReproduccion.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelReproduccion, idReproducir);
		panelReproduccion.setLayout(null);

		// -------------------------------------------------------------- Menu
		// reproducion -------------------------------------------------------
		JButton btnMenu = new JButton("Menu");
		btnMenu.addActionListener(new ActionListener() {
			/**
			 * cambia al panel de menu, establece el nombre de la cancion/podcast a usar y
			 * rellena la lista con las playlist del usuario
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				metodos.cambiardePantalla(layeredPane, idbtnMenu);
				if (opcionEscogida == 0) {
					txtFNombreCancionMenu.setText(albumElegido.getCanciones().get(audioElegido).getNombre());
				} else {
					txtFNombreCancionMenu.setText(podcasterElegido.getPodcasts().get(audioElegido).getNombre());
				}
				DefaultListModel<String> listModel = new DefaultListModel<>();
				listaMenu.setModel(listModel); // Establecer el modelo de lista en el JList
				for (int i = 0; i != usuarioIniciado.getPlaylists().size(); i++) {
					listModel.addElement(usuarioIniciado.getPlaylists().get(i).getTitulo());
				}
			}
		});
		btnMenu.setFont(new Font("Book Antiqua", Font.BOLD, 13));
		btnMenu.setBounds(34, 350, 139, 23);
		panelReproduccion.add(btnMenu);

////////////////////////////////////////Panel Boton Menu
//////////////////////////////////////// //////////////////////////////////////////////////////////
		panelBtnMenu = new JPanel();
		panelBtnMenu.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelBtnMenu, "btnMenu");
		panelBtnMenu.setLayout(null);

		JScrollPane scrollPaneBtnMenu = new JScrollPane();
		scrollPaneBtnMenu.setBounds(230, 130, 220, 220);
		panelBtnMenu.add(scrollPaneBtnMenu);

		listaMenu = new JList<String>();
		scrollPaneBtnMenu.setViewportView(listaMenu);

		txtFNombreCancionMenu = new JTextField();
		txtFNombreCancionMenu.setHorizontalAlignment(SwingConstants.CENTER);
		txtFNombreCancionMenu.setEditable(false);
		txtFNombreCancionMenu.setBounds(230, 99, 220, 20);
		panelBtnMenu.add(txtFNombreCancionMenu);
		txtFNombreCancionMenu.setColumns(10);

		JButton btnAnadirPlayList = new JButton("Añadir");
		btnAnadirPlayList.addActionListener(new ActionListener() {
			/**
			 * Inserta una cancion a una playlist
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				metodos.comprobarInsertarCancionPlaylist(albumElegido.getCanciones().get(audioElegido).getIdAudio(),
						listaMenu, usuarioIniciado);
			}
		});
		btnAnadirPlayList.setBounds(120, 374, 220, 27);
		panelBtnMenu.add(btnAnadirPlayList);

		JButton btnExportarCancion = new JButton("Exportar");
		btnExportarCancion.addActionListener(new ActionListener() {
			/**
			 * Crea un archivo txt con los datos de la cancion
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				if (opcionEscogida == 0) {
					metodos.exportarCancion(albumElegido.getCanciones().get(audioElegido));
				} else {
					metodos.exportarPodcast(podcasterElegido.getPodcasts().get(audioElegido));
				}
			}
		});
		btnExportarCancion.setBounds(342, 374, 220, 27);
		panelBtnMenu.add(btnExportarCancion);

		// -------------------------------------------------------------- Me gusta
		// -------------------------------------------------------
		JButton btnMeGusta = new JButton("Me Gusta");
		btnMeGusta.addActionListener(new ActionListener() {
			/**
			 * añade la cancion seleccionada a la playList me gusta comprobando primero que
			 * se este reproduciendo y que esa playlist no exista ya en esa playlist
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				if (clip != null && clip.isRunning()) {// comprueba que el audio se este reproduciendo
					boolean repetido = false;
					// busca en la playlist de me gusta el titulo de todas las canciones que estan
					// metidas y si esta intendando añadir una cancion que ya esta esta añadida le
					// salta error
					for (int i = 0; i != usuarioIniciado.getPlaylists().get(0).getCanciones().size(); i++) {
						if (usuarioIniciado.getPlaylists().get(0).getCanciones().get(i).getIdAudio() == albumElegido
								.getCanciones().get(audioElegido).getIdAudio()) {
							repetido = true;
						}
					}
					if (!repetido) {
						if (opcionEscogida == 0) {// añade una cancion a me gusta y suma uno en estadisticas
							basededatos.anadirCancionLike(usuarioIniciado,
									albumElegido.getCanciones().get(audioElegido).getIdAudio());
							basededatos.anadirEstadisticasLike(
									albumElegido.getCanciones().get(audioElegido).getIdAudio(), opcionEscogida);
						} else {// añade un podcast a me gusta y suma uno en estadisticas
							basededatos.anadirCancionLike(usuarioIniciado,
									podcasterElegido.getPodcasts().get(audioElegido).getIdAudio());
							basededatos.anadirEstadisticasLike(
									podcasterElegido.getPodcasts().get(audioElegido).getIdAudio(), opcionEscogida);
						}
						JOptionPane.showMessageDialog(null, "¡Canción añadida a Me gusta!");
					} else {
						JOptionPane.showMessageDialog(null, "Esta cancion ya se encuentra en tu PlayList de Me gusta");
					}
				} else {
					JOptionPane.showMessageDialog(null, "No hay ninguna canción reproduciéndose actualmente.");
				}
			}
		});

		btnMeGusta.setFont(new Font("Book Antiqua", Font.BOLD, 13));
		btnMeGusta.setBounds(468, 350, 139, 23);
		panelReproduccion.add(btnMeGusta);

		// -------------------------------------------------------------- Reproducir
				// -------------------------------------------------------
		btnAtrasCancion = new JButton("<");
		btnAtrasCancion.addActionListener(new ActionListener() {
			/**
			 * va a la cancion anterior a no ser que sea la primera cancion y si el usuario
			 * no es premium le pone una cancion aleatoria y le salta un anuncio
			 */
			public void actionPerformed(ActionEvent e) {
				if (!(audioElegido == 0)) {// hace la cancion a no ser que haya seleccionado la primera cancion
					btnReproducir.setText("Reproducir");
					if (!(clip == null) && clip.isRunning()) {
						clip.stop();
					}

					if (usuarioIniciado.getTipoCliente().equals("free")) {// si el usuario no es premium debera
						Random random = new Random(); // escuchar un anuncio
						try {
							metodos.reproducirAnuncio(clip, btnAtrasCancion, btnAdelanteCancion, btnReproducir,
									btnMeGusta, btnMenu, random);
						} catch (MiExcepcion e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						int audioAleatorio = 0;
						if (opcionEscogida == 0) {// asigna una cancion/podcast aleatoria
							audioAleatorio = random.nextInt(albumElegido.getCanciones().size());
						} else {
							audioAleatorio = random.nextInt(podcasterElegido.getPodcasts().size());
						}
						audioElegido = audioAleatorio;
					} else {
						audioElegido--;
					}
					// si el btn de adelante estaba deshabilitado al pusar en anterior se volvera a
					// habilitar

					if (!btnAdelanteCancion.isEnabled()) {
						btnAdelanteCancion.setEnabled(true);
					}

				} else {
					btnAtrasCancion.setEnabled(false);
					JOptionPane.showMessageDialog(null, "No se puede ir para atras");

				}
			}
		});
		btnAtrasCancion.setBounds(183, 350, 55, 23);
		panelReproduccion.add(btnAtrasCancion);

		btnAdelanteCancion = new JButton(">");
		btnAdelanteCancion.addActionListener(new ActionListener() {
			/**
			 * va a la cancion siguiente a no ser que sea la ultima cancion y si el usuario
			 * no es premium le pone una cancion aleatoria y le salta un anuncio
			 */
			public void actionPerformed(ActionEvent e) {
				if ((!(audioElegido == albumElegido.getCanciones().size() - 1) && opcionEscogida == 0)
						|| (!(posicionPodcast == podcasterElegido.getPodcasts().size() - 1)) && opcionEscogida == 1) {
					btnReproducir.setText("Reproducir");

					if (!(clip == null) && clip.isRunning()) {
						clip.stop();
					}

					if (usuarioIniciado.getTipoCliente().equals("free")) {// si el usuario no es premium debera
						Random random = new Random(); // escuchar un anuncio
						try {
							metodos.reproducirAnuncio(clip, btnAtrasCancion, btnAdelanteCancion, btnReproducir,
									btnMeGusta, btnMenu, random);
						} catch (MiExcepcion e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						int audioAleatorio = 0;
						if (opcionEscogida == 0) {// asigna una cancion/podcast aleatoria
							audioAleatorio = random.nextInt(albumElegido.getCanciones().size());
						} else {
							audioAleatorio = random.nextInt(podcasterElegido.getPodcasts().size());
						}
						audioElegido = audioAleatorio;
					} else {
						audioElegido++;
					}
					// si el btn de atras estaba deshabilitado al pusar en siguiente se volvera a
					// habilitar
					if (!btnAtrasCancion.isEnabled()) {
						btnAtrasCancion.setEnabled(true);
					}
				} else {
					btnAdelanteCancion.setEnabled(false);
					JOptionPane.showMessageDialog(null, "No se puede ir para Adelante");
				}
			}
		});
		btnAdelanteCancion.setBounds(403, 350, 55, 23);
		panelReproduccion.add(btnAdelanteCancion);

		JLabel lblReproduciendo = new JLabel("Reproduciendo:");
		lblReproduciendo.setFont(new Font("Book Antiqua", Font.BOLD, 14));
		lblReproduciendo.setBounds(83, 50, 168, 29);
		panelReproduccion.add(lblReproduciendo);

		JLabel lblAlbum = new JLabel("Album:");
		lblAlbum.setFont(new Font("Book Antiqua", Font.BOLD, 14));
		lblAlbum.setBounds(83, 77, 168, 29);
		panelReproduccion.add(lblAlbum);

		lblReproduciendoSelec = new JLabel("");
		lblReproduciendoSelec.setFont(new Font("Book Antiqua", Font.BOLD, 14));
		lblReproduciendoSelec.setBounds(195, 50, 263, 29);
		panelReproduccion.add(lblReproduciendoSelec);

		lblAlbumSelec = new JLabel("");
		lblAlbumSelec.setFont(new Font("Book Antiqua", Font.BOLD, 14));
		lblAlbumSelec.setBounds(195, 77, 263, 29);
		panelReproduccion.add(lblAlbumSelec);

		JButton btnX05 = new JButton("x 0.5");
		btnX05.setBounds(468, 193, 77, 23);
		panelReproduccion.add(btnX05);
		btnX05.setVisible(false);

		JButton btnX1 = new JButton("x 1");
		btnX1.setBounds(468, 227, 77, 23);
		panelReproduccion.add(btnX1);
		btnX1.setVisible(false);

		JButton btnX15 = new JButton("x 1.5");
		btnX15.setBounds(468, 261, 77, 23);
		panelReproduccion.add(btnX15);
		btnX15.setVisible(false);

		JLabel lblFotoReproduccion = new JLabel("");
		lblFotoReproduccion.setBounds(183, 129, 275, 210);
		panelReproduccion.add(lblFotoReproduccion);

		JLabel lblDuracion = new JLabel("Duracion:");
		lblDuracion.setFont(new Font("Book Antiqua", Font.BOLD, 14));
		lblDuracion.setBounds(83, 102, 168, 29);
		panelReproduccion.add(lblDuracion);

		JLabel lblDuracionSelec = new JLabel("");
		lblDuracionSelec.setFont(new Font("Book Antiqua", Font.BOLD, 14));
		lblDuracionSelec.setBounds(195, 102, 263, 29);
		panelReproduccion.add(lblDuracionSelec);

		btnReproducir = new JButton("Reproducir");
		btnReproducir.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (btnReproducir.getText().equals("Reproducir")) {// si en el boton pone reproducir, genera el clip con
																	// la cancion/podcast seleccionados
					try {
						File file = null;
						if (opcionEscogida == 0) {// genera la cancion y inserta sus datos
							Image img = new ImageIcon(
									Paths.get("").toAbsolutePath().toString() + "\\img\\" + albumElegido.getImagen())
									.getImage();
							ImageIcon img2 = new ImageIcon(img.getScaledInstance(275, 210, Image.SCALE_SMOOTH));
							lblAlbum.setText("Album:");
							// dentro de el file en el .reproducir se encuentra toda la informacion de la
							// cancion
							file = albumElegido.getCanciones().get(audioElegido).reproducir(lblDuracionSelec,
									lblReproduciendoSelec, btnX05, btnX1, btnX15);
							lblAlbumSelec.setText(albumElegido.getTitulo().toUpperCase());
							// si el file existe se le asigna la foto y sino se le establece una
							// predeterminada
							if (new File(
									Paths.get("").toAbsolutePath().toString() + "\\img\\" + albumElegido.getImagen())
									.exists()) {
								lblFotoReproduccion.setIcon(img2);
							} else {
								ImageIcon iconoPredeterminado = new ImageIcon(
										imgPredeterminada.getScaledInstance(275, 210, Image.SCALE_SMOOTH));
								lblFotoReproduccion.setIcon(iconoPredeterminado);
							}
						} else {// genera el podcast y inserta sus datos
							lblAlbum.setText("Podcast:");
							// dentro de el file en el .reproducir se encuentra toda la informacion de el
							// podcast
							file = podcasterElegido.getPodcasts().get(audioElegido).reproducir(lblDuracionSelec,
									lblReproduciendoSelec, btnX05, btnX1, btnX15);

							lblFotoReproduccion.setBounds(251, 129, 275, 210);
							if (new File(Paths.get("").toAbsolutePath().toString() + "/img/"
									+ podcasterElegido.getNombreArtista().replace(" ", "") + "Desc.jpg").exists()) {
								lblFotoReproduccion.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString()
										+ "/img/" + podcasterElegido.getImagen()));
							} else {
								ImageIcon iconoPredeterminado = new ImageIcon(
										imgPredeterminada.getScaledInstance(275, 210, Image.SCALE_SMOOTH));
								lblFotoReproduccion.setIcon(iconoPredeterminado);
							}
						}
						if (!file.exists()) {
							file = new File(Paths.get("").toAbsolutePath().toString() + "\\musica\\predeterminado.wav");
						}
						AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
						clip = AudioSystem.getClip();
						clip.open(audioInputStream);
						clip.start();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					btnReproducir.setText("Parar");
				} else if (btnReproducir.getText().equals("Parar")) {// para el clip y guarda la posicion en la que ha
																		// sido parada
					if (clip != null && clip.isRunning()) {
						clipTimePosition = clip.getMicrosecondPosition();
						clip.stop();
						btnReproducir.setText("Reanudar");
					}
				} else if (btnReproducir.getText().equals("Reanudar")) {// Reanuda el clip en la posicion que se paro
					btnReproducir.setText("Parar");
					clip.setMicrosecondPosition(clipTimePosition);
					clip.start();
				}
			}
		});
		btnReproducir.setFont(new Font("Book Antiqua", Font.BOLD, 13));
		btnReproducir.setBounds(251, 350, 139, 23);
		panelReproduccion.add(btnReproducir);

		// ------------------------------------------- Fin Panel Reproduccion, Inicio
		// Canciones ----------------------------------------------------------------
		panelCanciones = new JPanel();
		panelCanciones.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelCanciones, idCanciones);
		panelCanciones.setLayout(null);

		// Playlist ----------------------------------------------------------------
		panelPlaylist = new JPanel();
		panelPlaylist.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelPlaylist, idPlaylist);
		panelPlaylist.setLayout(null);

		JButton btnNuevaPlaylist = new JButton("Nueva Playlist");
		btnNuevaPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				usuarioIniciado = metodos.crearPlayList(usuarioIniciado, listaPlaylist);
			}
		});
		btnNuevaPlaylist.setBounds(379, 44, 120, 23);
		panelPlaylist.add(btnNuevaPlaylist);

		JButton btnBorrarPlaylist = new JButton("Borrar Playlist");
		btnBorrarPlaylist.addActionListener(new ActionListener() {
			/**
			 * Borra la playList siempre que no sea la de Me gusta
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				if (listaPlaylist.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(null, "Debes seleccionar una PlayList");
				} else {
					if (!listaPlaylist.getSelectedValue().toString().equals("Me gusta")) {
						basededatos.borrarPlayList(listaPlaylist, usuarioIniciado.getUsuario());
					} else {
						JOptionPane.showMessageDialog(null, "La PlayList Me Gusta no se puede borrar");
					}

				}
			}
		});
		btnBorrarPlaylist.setBounds(379, 78, 120, 23);
		panelPlaylist.add(btnBorrarPlaylist);

		JButton btnImportar = new JButton("Importar");
		btnImportar.addActionListener(new ActionListener() {
			/**
			 * Importa una playlist con un archivo .csv
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				usuarioIniciado = metodos.importarPlayList(usuarioIniciado, listaPlaylist);
			}
		});
		btnImportar.setBounds(379, 112, 120, 23);
		panelPlaylist.add(btnImportar);
		/**
		 * Exporta la playlist Seleccionada
		 */
		JButton btnExportar = new JButton("Exportar");
		btnExportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!listaPlaylist.isSelectionEmpty()) {
					metodos.exportarPlayList(usuarioIniciado, listaPlaylist.getSelectedIndex());
				} else {
					JOptionPane.showMessageDialog(null, "Antes de exportar debes seleccionar una playlist");
				}
			}
		});
		btnExportar.setBounds(379, 146, 120, 23);
		panelPlaylist.add(btnExportar);

		JScrollPane scrollPaneTablaPlaylist = new JScrollPane();
		scrollPaneTablaPlaylist.setBounds(354, 198, 302, 200);
		panelPlaylist.add(scrollPaneTablaPlaylist);

		// muestra todos los datos de la playlist en la que click-a
		JTable tablaInfoPlaylist = new JTable();
		scrollPaneTablaPlaylist.setViewportView(tablaInfoPlaylist);
		listaPlaylist = new JList<String>();
		listaPlaylist.addMouseListener(new MouseAdapter() {
			/**
			 * muestra la informacion de la playlist que se ha clickado
			 * 
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {

				basededatos.mostrarInfoPlaylist(tablaInfoPlaylist, usuarioIniciado.getIdUsuario(),
						listaPlaylist.getSelectedValue());
			}
		});
		listaPlaylist.setBounds(61, 44, 250, 354);
		panelPlaylist.add(listaPlaylist);

		// Panel
		// Admin----------------------------------------------------------------
		panelAdmin = new JPanel();
		panelAdmin.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelAdmin, idAdmin);
		panelAdmin.setLayout(null);

		JButton btnGestMusica = new JButton("Gestionar musica");
		btnGestMusica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				metodos.cambiardePantalla(layeredPane, idMenuGestMusica);
				btnGestPodcasts.setVisible(false);
				btnGestPodcaster.setVisible(false);
				btnGestArtistas.setVisible(true);
				btnGestAlbumes.setVisible(true);
				btnGestCanciones.setVisible(true);
			}
		});
		btnGestMusica.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnGestMusica.setBounds(263, 153, 167, 23);
		panelAdmin.add(btnGestMusica);

		JButton btnGestPodcats = new JButton("Gestionar podcast");
		btnGestPodcats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				metodos.cambiardePantalla(layeredPane, idMenuGestMusica);
				btnGestPodcasts.setVisible(true);
				btnGestPodcaster.setVisible(true);
				btnGestArtistas.setVisible(false);
				btnGestAlbumes.setVisible(false);
				btnGestCanciones.setVisible(false);
			}
		});
		btnGestPodcats.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnGestPodcats.setBounds(263, 199, 167, 23);
		panelAdmin.add(btnGestPodcats);

		JButton btnGestEstadisticas = new JButton("Estadisticas");
		btnGestEstadisticas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				metodos.cambiardePantalla(layeredPane, idEstadisticas);
			}
		});
		btnGestEstadisticas.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnGestEstadisticas.setBounds(263, 241, 167, 23);
		panelAdmin.add(btnGestEstadisticas);

		JLabel lblAdmin = new JLabel("Menu Gestión");
		lblAdmin.setFont(new Font("Cambria", Font.BOLD, 25));
		lblAdmin.setBounds(266, 86, 180, 56);
		panelAdmin.add(lblAdmin);

		// --------------------------------------------- Panel Menu GestionarMusica
		// ------------------------------------------------------------------------------
		panelMenuGestMusica = new JPanel();
		panelMenuGestMusica.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelMenuGestMusica, idMenuGestMusica);
		panelMenuGestMusica.setLayout(null);

		btnGestArtistas = new JButton("Gestionar artistas");
		btnGestArtistas.addActionListener(new ActionListener() {
			/**
			 * entra en el panel de gestion de artistas y establece el comboBox
			 */
			public void actionPerformed(ActionEvent e) {
				metodos.cambiardePantalla(layeredPane, idPanelCrudMusica);
				elementoGestionado = 1;
				basededatos.obtenerYActualizarLista(listaCrudMusica, elementoGestionado);
				// crea un array de artistas para establezerlos en el combobox
				String[] artistas = new String[musicos.size()];
				for (int i = 0; i != musicos.size(); i++) {
					artistas[i] = musicos.get(i).getNombreArtista();
				}
				cmbxArtista.setModel(new DefaultComboBoxModel<String>(artistas));
			}
		});
		btnGestArtistas.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnGestArtistas.setBounds(43, 141, 178, 94);
		panelMenuGestMusica.add(btnGestArtistas);

		btnGestAlbumes = new JButton("Gestionar Albumes");
		btnGestAlbumes.addActionListener(new ActionListener() {
			/**
			 * entra en el panel de gestion de albumes y establece el comboBox
			 */
			public void actionPerformed(ActionEvent e) {
				elementoGestionado = 2;
				metodos.cambiardePantalla(layeredPane, idPanelCrudMusica);
				// crea un array de artistas para establezerlos en el combobox
				String[] artistas = new String[musicos.size()];
				for (int i = 0; i != musicos.size(); i++) {
					artistas[i] = musicos.get(i).getNombreArtista();
				}
				cmbxArtista.setModel(new DefaultComboBoxModel<String>(artistas));
			}
		});
		btnGestAlbumes.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnGestAlbumes.setBounds(231, 141, 196, 94);
		panelMenuGestMusica.add(btnGestAlbumes);

		btnGestCanciones = new JButton("Gestionar canciones");
		btnGestCanciones.addActionListener(new ActionListener() {
			/**
			 * entra en el panel de gestion de canciones y establece el comboBox
			 */
			public void actionPerformed(ActionEvent e) {
				metodos.cambiardePantalla(layeredPane, idPanelCrudMusica);
				elementoGestionado = 3;
				// crea un array de artistas para establezerlos en el combobox
				String[] artistas = new String[musicos.size()];
				for (int i = 0; i != musicos.size(); i++) {
					artistas[i] = musicos.get(i).getNombreArtista();
				}

				cmbxArtista.setModel(new DefaultComboBoxModel<String>(artistas));
			}
		});
		btnGestCanciones.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnGestCanciones.setBounds(437, 141, 196, 94);
		panelMenuGestMusica.add(btnGestCanciones);

		btnGestPodcaster = new JButton("Gestionar Podcaster");
		btnGestPodcaster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				metodos.cambiardePantalla(layeredPane, idGestionPodcaster);
				elementoGestionado = 4;
				basededatos.obtenerYActualizarListaPodcaster(listaPodcaster, elementoGestionado);
				metodos.ocultarComponentesPodcaster(lblNombreCrudPodcaster, lblInfo1CrudPodcaster,
						lblInfo2CrudPodcaster, lblInfo3CrudPodcast, txtFNombreCrudPodcaster, txtFInfo1CrudPodcaster,
						txtFInfo2CrudPodcaster, btnAceptarPodcaster, cmbxCrudPodcast);
			}
		});
		btnGestPodcaster.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnGestPodcaster.setBounds(137, 246, 178, 94);
		panelMenuGestMusica.add(btnGestPodcaster);

		btnGestPodcasts = new JButton("Gestionar Podcasts");
		btnGestPodcasts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				metodos.cambiardePantalla(layeredPane, idGestionPodcaster);
				elementoGestionado = 5;
				metodos.ocultarComponentesPodcaster(lblNombreCrudPodcaster, lblInfo1CrudPodcaster,
						lblInfo2CrudPodcaster, lblInfo3CrudPodcast, txtFNombreCrudPodcaster, txtFInfo1CrudPodcaster,
						txtFInfo2CrudPodcaster, btnAceptarPodcaster, cmbxCrudPodcast);
			}
		});
		btnGestPodcasts.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnGestPodcasts.setBounds(342, 246, 178, 94);
		panelMenuGestMusica.add(btnGestPodcasts);

////////////////////////////////////////////Panel Estadisticas////////////////////////////////////////////////////
		panelEstadisticas = new JPanel();
		panelEstadisticas.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelEstadisticas, idEstadisticas);
		panelEstadisticas.setLayout(null);

		JScrollPane scrollPaneTabla = new JScrollPane();
		scrollPaneTabla.setBounds(10, 65, 600, 350);
		panelEstadisticas.add(scrollPaneTabla);

		JTable tablaEstadisticas = new JTable();
		scrollPaneTabla.setViewportView(tablaEstadisticas);

		JButton btnTopCanciones = new JButton("Top Canciones");
		btnTopCanciones.addActionListener(new ActionListener() {
			/**
			 * Genera la tabla de Top Canciones
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				basededatos.obtenerTopCanciones(tablaEstadisticas);
			}
		});
		btnTopCanciones.setBounds(10, 11, 155, 23);
		panelEstadisticas.add(btnTopCanciones);

		JButton btnTopPodcasts = new JButton("Top Podcasts");
		btnTopPodcasts.addActionListener(new ActionListener() {
			/**
			 * Genera la tabla de Top Podcasts
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				basededatos.obtenerTopPodcasts(tablaEstadisticas);
			}
		});
		btnTopPodcasts.setBounds(175, 11, 155, 23);
		panelEstadisticas.add(btnTopPodcasts);

		JButton btnTopPlaylist = new JButton("Top Playlist");
		btnTopPlaylist.addActionListener(new ActionListener() {
			/**
			 * Genera la tabla de Top Playlist
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				basededatos.obtenerTopPlaylist(tablaEstadisticas);
			}
		});
		btnTopPlaylist.setBounds(340, 11, 155, 23);
		panelEstadisticas.add(btnTopPlaylist);

		JButton btnMasEscuchados = new JButton("Mas Escuchados");
		btnMasEscuchados.addActionListener(new ActionListener() {
			/**
			 * Genera la tabla de Mas Escuchados
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				basededatos.obtenerMasEscuchadas(tablaEstadisticas);
			}
		});
		btnMasEscuchados.setBounds(505, 11, 155, 23);
		panelEstadisticas.add(btnMasEscuchados);

		////////////////////////////////// Panel Admin
		////////////////////////////////// /////////////////////////////////////////////
		panelCrudMusica = new JPanel();
		layeredPane.add(panelCrudMusica, idPanelCrudMusica);
		panelCrudMusica.setBackground(new Color(215, 223, 234));
		panelCrudMusica.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(77, 46, 211, 321);
		panelCrudMusica.add(scrollPane);

		JLabel lblNombreCrud = new JLabel("");
		lblNombreCrud.setBounds(397, 78, 259, 14);
		lblNombreCrud.setVisible(false);
		panelCrudMusica.add(lblNombreCrud);

		JLabel lblInfo1Crud = new JLabel("");
		lblInfo1Crud.setBounds(397, 122, 259, 14);
		panelCrudMusica.add(lblInfo1Crud);
		lblInfo1Crud.setVisible(false);

		JLabel lblInfo2Crud = new JLabel("");
		lblInfo2Crud.setBounds(397, 215, 259, 14);
		panelCrudMusica.add(lblInfo2Crud);
		lblInfo2Crud.setVisible(false);

		JTextField txtFNombreCrud = new JTextField();
		txtFNombreCrud.setBounds(397, 95, 259, 20);
		panelCrudMusica.add(txtFNombreCrud);
		txtFNombreCrud.setColumns(10);
		txtFNombreCrud.setVisible(false);

		JTextField txtFInfo1Crud = new JTextField();
		txtFInfo1Crud.setHorizontalAlignment(SwingConstants.LEFT);
		txtFInfo1Crud.setBounds(397, 138, 259, 70);
		panelCrudMusica.add(txtFInfo1Crud);
		txtFInfo1Crud.setColumns(10);
		txtFInfo1Crud.setVisible(false);

		JComboBox<String> cmbxCrudTipo = new JComboBox<String>();
		cmbxCrudTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Si esta gestionando canciones rellena la lista con las canciones del album
				 * que selecciona
				 */
				if (elementoGestionado == 3) {
					DefaultListModel<String> listModel = new DefaultListModel<>();
					listaCrudMusica.setModel(listModel);
					Album albumSeleccionado = musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes()
							.get(cmbxCrudTipo.getSelectedIndex());
					for (int i = 0; i != albumSeleccionado.getCanciones().size(); i++) {
						listModel.addElement(albumSeleccionado.getCanciones().get(i).getNombre());
					}
				}
			}
		});
		cmbxCrudTipo.setModel(new DefaultComboBoxModel<String>(metodos.crearModeloAnyos()));
		cmbxCrudTipo.setBounds(397, 232, 259, 22);
		panelCrudMusica.add(cmbxCrudTipo);
		cmbxCrudTipo.setVisible(false);

		JLabel lblCrudArtista = new JLabel("Artista:");
		lblCrudArtista.setBounds(397, 262, 259, 14);
		panelCrudMusica.add(lblCrudArtista);
		lblCrudArtista.setVisible(false);

		cmbxArtista = new JComboBox<String>();
		cmbxArtista.addActionListener(new ActionListener() {
			/**
			 * Si esta gestionando Canciones rellena el comboBox de crudTipo de los albumes
			 * del artista que ha seleccionado Si esta modificando albumes rellena la lista
			 * con los albumes del artista que ha seleccionado
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				// Establece todos los albumes en el combobox si gestionamos canciones.
				if (elementoGestionado == 3) {
					String[] albumes = new String[musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().size()];
					for (int i = 0; i != musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().size(); i++) {
						albumes[i] = musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().get(i).getTitulo();
					}
					cmbxCrudTipo.setModel(new DefaultComboBoxModel<String>(albumes));
				// Establece todos los albumes en la lista si gestionamos albumes.
				} else {
					DefaultListModel<String> listModel = new DefaultListModel<>();
					listaCrudMusica.setModel(listModel);
					// For para que aparezcan todos los albumes en la lista
					if (musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes() != null
							&& musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().size() > 0) {
						for (int i = 0; i != musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().size(); i++)
							listModel.addElement(
									musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().get(i).getTitulo());
					}
				}
			}
		});
		cmbxArtista.setVisible(false);
		cmbxArtista.setBounds(397, 283, 259, 22);
		panelCrudMusica.add(cmbxArtista);

		JButton btnAceptarCrudMusica = new JButton("ACEPTAR");
		btnAceptarCrudMusica.setVisible(false);
		btnAceptarCrudMusica.addActionListener(new ActionListener() {
			/**
			 * Dependiendo del elemento gestionado y de la accion añade o modifica album,
			 * cancion y Artistas Comprueba que al añadir no añada algo que ya exista
			 * Comprueba que al modificar no este la seleccion vacia Despues de hacer la
			 * accion que le corresponde oculta todos los componentes
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				if (!txtFNombreCrud.getText().equals("") && !txtFInfo1Crud.getText().equals("")) {
					// ----------------------------------- añadir ---------------------------------------
					if (accion == 1) {
						boolean artistaRepetido = false;
						if (elementoGestionado == 1) {
                            // recorre toda la lista para haber si hay algun artista repetido, en caso de no haber, lo añade
							for (int i = 0; i < listaCrudMusica.getModel().getSize(); i++) {
								if (listaCrudMusica.getModel().getElementAt(i).equals(txtFNombreCrud.getText())) {
									artistaRepetido = true;
								}
							}
							if (artistaRepetido) {
								JOptionPane.showMessageDialog(null, "El artista que deseas insertar ya existe");
							} else {
								basededatos.añadirElementoLista(txtFNombreCrud.getText(), txtFInfo1Crud.getText(),
										cmbxCrudTipo, listaCrudMusica, elementoGestionado, cmbxArtista, musicos);

							}
						} else if (elementoGestionado == 2) {
							for (int i = 0; i < listaCrudMusica.getModel().getSize(); i++) {
								if (listaCrudMusica.getModel().getElementAt(i).equals(txtFNombreCrud.getText())) {
									artistaRepetido = true;
								}
							}
							if (artistaRepetido) {
								JOptionPane.showMessageDialog(null,
										"El album que deseas insertar ya existe para ese artista");
							} else {
								basededatos.añadirElementoLista(txtFNombreCrud.getText(), txtFInfo1Crud.getText(),
										cmbxCrudTipo, listaCrudMusica, elementoGestionado, cmbxArtista, musicos);

							}
						} else {
							for (int i = 0; i < listaCrudMusica.getModel().getSize(); i++) {
								if (listaCrudMusica.getModel().getElementAt(i).equals(txtFNombreCrud.getText())) {
									artistaRepetido = true;
								}
							}
							if (artistaRepetido) {
								JOptionPane.showMessageDialog(null,
										"El album que deseas insertar ya existe para ese artista");
							} else {
								if (metodos.formatoDuracion(txtFInfo1Crud.getText())) {
									basededatos.añadirElementoLista(txtFNombreCrud.getText(), txtFInfo1Crud.getText(),
											cmbxCrudTipo, listaCrudMusica, elementoGestionado, cmbxArtista, musicos);
								} else {
									JOptionPane.showMessageDialog(null, "Formato de duración Incorrecto");
								}

							}
						}
					// ----------------------------------- modificar ---------------------------------------
					} else if (accion == 2) {
						if (elementoGestionado == 1) {
							if (listaCrudMusica.isSelectionEmpty()) {
								JOptionPane.showMessageDialog(null, "Debes seleccionar un artista para modificar");
							// recoge el elemento seleccionado en la lista para modificar su informacion
							} else {
								String nombreArtistaSeleccionado = listaCrudMusica.getSelectedValue();
								basededatos.modificarElementoLista(nombreArtistaSeleccionado, txtFNombreCrud.getText(),
										txtFInfo1Crud.getText(), cmbxCrudTipo.getSelectedItem(), listaCrudMusica);
							}
						} else if (elementoGestionado == 2) {
							basededatos.modificarAlbum(txtFNombreCrud.getText(), txtFInfo1Crud.getText(),
									cmbxCrudTipo.getSelectedItem(), listaCrudMusica,
									musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes()
											.get(listaCrudMusica.getSelectedIndex()));
						} else {
							if (metodos.formatoDuracion(txtFInfo1Crud.getText())) {
								basededatos.modificarCancion(txtFNombreCrud.getText(), txtFInfo1Crud.getText(),
										cmbxCrudTipo, listaCrudMusica, musicos, cmbxArtista);
							} else {
								JOptionPane.showMessageDialog(null, "Formato de duración Incorrecto");
							}

						}
					}
					metodos.ocultarComponentes(lblNombreCrud, lblInfo1Crud, lblInfo2Crud, txtFNombreCrud, txtFInfo1Crud,
							cmbxCrudTipo, btnAceptarCrudMusica, lblCrudArtista, cmbxArtista);
				} else {
					JOptionPane.showMessageDialog(null, "Todos los campos deben estar rellenados");
				}
			}
		});
		btnAceptarCrudMusica.setBounds(481, 392, 89, 23);
		panelCrudMusica.add(btnAceptarCrudMusica);
		btnAceptarCrudMusica.setVisible(false);

		JButton btnAñadirCrudMusica = new JButton("Añadir");
		btnAñadirCrudMusica.addActionListener(new ActionListener() {
			/**
			 * Establece el parametro accion y carga los campos
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				accion = 1;
				metodos.cargarCrudMusica(lblNombreCrud, lblInfo1Crud, lblInfo2Crud, txtFNombreCrud, txtFInfo1Crud,
						cmbxCrudTipo, lblCrudArtista, cmbxArtista, elementoGestionado);
				btnAceptarCrudMusica.setVisible(true);
			}
		});
		btnAñadirCrudMusica.setBounds(350, 44, 89, 23);
		panelCrudMusica.add(btnAñadirCrudMusica);

		JButton btnModificarCrudMusica = new JButton("Modificar");
		btnModificarCrudMusica.addActionListener(new ActionListener() {
			/**
			 * Establece la accion que esta intentando hacer, carga los campos y establece
			 * su valor al modificar un artista
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				if (elementoGestionado == 1) {
					// se recoge el indice de el musico seleccionado para despues poder sacar su informacion
					if (listaCrudMusica.getSelectedIndex() != -1) {
						Musico musicoSeleccionado = musicos.get(listaCrudMusica.getSelectedIndex());
						txtFNombreCrud.setText(musicoSeleccionado.getNombreArtista());
						txtFInfo1Crud.setText(musicoSeleccionado.getDescripcion());
						cmbxCrudTipo.setSelectedItem(musicoSeleccionado.getCaracteristica());
					} else {
						JOptionPane.showMessageDialog(null, "Debes seleccionar un artista para modificar");
					}

				}
				metodos.cargarCrudMusica(lblNombreCrud, lblInfo1Crud, lblInfo2Crud, txtFNombreCrud, txtFInfo1Crud,
						cmbxCrudTipo, lblCrudArtista, cmbxArtista, elementoGestionado);
				accion = 2;
				btnAceptarCrudMusica.setVisible(true);
			}

		});
		btnModificarCrudMusica.setBounds(449, 44, 89, 23);
		panelCrudMusica.add(btnModificarCrudMusica);

		JButton btnBorrarCrudMusica = new JButton("Borrar");
		btnBorrarCrudMusica.addActionListener(new ActionListener() {
			/**
			 * Borra el elemento seleccionado
			 */
			public void actionPerformed(ActionEvent e) {
				if (listaCrudMusica.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(null, "Debes seleccionar algo");
				} else {
					basededatos.borrarElementosLista(listaCrudMusica, elementoGestionado);
				}
			}
		});
		btnBorrarCrudMusica.setBounds(548, 44, 89, 23);
		panelCrudMusica.add(btnBorrarCrudMusica);

		listaCrudMusica = new JList<String>();
		listaCrudMusica.addMouseListener(new MouseAdapter() {
			/**
			 * al clickar encima de un elemento de la lista pondra sus datos en los campos
			 * 
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				if (elementoGestionado == 2 && accion == 2) {
					Album albumSeleccionado = musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes()
							.get(listaCrudMusica.getSelectedIndex());
					txtFNombreCrud.setText(albumSeleccionado.getTitulo());
					txtFInfo1Crud.setText(albumSeleccionado.getGenero());
				} else if ((elementoGestionado == 3 && accion == 2)) {
					Cancion cancionElegida = musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes()
							.get(cmbxCrudTipo.getSelectedIndex()).getCanciones()
							.get(listaCrudMusica.getSelectedIndex());
					txtFNombreCrud.setText(cancionElegida.getNombre());
					txtFInfo1Crud.setText(cancionElegida.getDuracion());
				}
			}
		});
		scrollPane.setViewportView(listaCrudMusica);

		// ---------------------------------------------------------- Gestionar podcast -----------------------------------------------------------------------------
		panelGestPodcaster = new JPanel();
		panelGestPodcaster.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelGestPodcaster, idGestionPodcaster);
		panelGestPodcaster.setLayout(null);

		listaPodcaster = new JList<String>();
		listaPodcaster.setBounds(77, 46, 211, 324);
		panelGestPodcaster.add(listaPodcaster);

		listaPodcaster.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (elementoGestionado == 4 && accion == 1) {
					Podcaster podcasterSeleccionado = podcasters.get(cmbxCrudPodcast.getSelectedIndex());
					txtFNombreCrudPodcaster.setText(podcasterSeleccionado.getNombreArtista());
					txtFInfo1CrudPodcaster.setText(podcasterSeleccionado.getGenero());
					txtFInfo2CrudPodcaster.setText(podcasterSeleccionado.getDescripcion());
				} else if ((elementoGestionado == 5 && accion == 2)) {
					Podcast podcastSeleccionado = podcasters.get(cmbxCrudPodcast.getSelectedIndex()).getPodcasts()
							.get(listaPodcaster.getSelectedIndex());
					txtFNombreCrudPodcaster.setText(podcastSeleccionado.getNombre());
					txtFInfo1CrudPodcaster.setText(podcastSeleccionado.getDuracion());
					txtFInfo2CrudPodcaster.setText(podcastSeleccionado.getColaboradores());
				}
			}
		});

		JButton btnBorrarPodcaster = new JButton("Borrar");
		btnBorrarPodcaster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (listaPodcaster.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(null, "Debes seleccionar algo");
				} else {
					basededatos.borrarElementosListaPodcaster(listaPodcaster, elementoGestionado);

				}

			}
		});
		btnBorrarPodcaster.setBounds(536, 43, 89, 23);
		panelGestPodcaster.add(btnBorrarPodcaster);

		lblNombreCrudPodcaster = new JLabel("");
		lblNombreCrudPodcaster.setVisible(false);
		lblNombreCrudPodcaster.setBounds(364, 77, 188, 14);
		panelGestPodcaster.add(lblNombreCrudPodcaster);

		lblInfo1CrudPodcaster = new JLabel("");
		lblInfo1CrudPodcaster.setVisible(false);
		lblInfo1CrudPodcaster.setBounds(364, 133, 188, 14);
		panelGestPodcaster.add(lblInfo1CrudPodcaster);

		lblInfo2CrudPodcaster = new JLabel("");
		lblInfo2CrudPodcaster.setVisible(false);
		lblInfo2CrudPodcaster.setBounds(364, 189, 188, 14);
		panelGestPodcaster.add(lblInfo2CrudPodcaster);

		btnAceptarPodcaster = new JButton("ACEPTAR");
		btnAceptarPodcaster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// En el caso de gestionar los podcasters 
				if (elementoGestionado == 4) {
					if (accion == 1) {
						if (!txtFNombreCrudPodcaster.getText().isEmpty() || !txtFInfo1CrudPodcaster.getText().isEmpty()
								|| !txtFInfo2CrudPodcaster.getText().isEmpty()) {

							basededatos.anadirPodcaster(txtFNombreCrudPodcaster, txtFInfo1CrudPodcaster,
									txtFInfo2CrudPodcaster, listaPodcaster);
							JOptionPane.showMessageDialog(null, "El podcaster ha sido insertado con exito.");
							basededatos.obtenerYActualizarListaPodcaster(listaPodcaster, elementoGestionado);

						} else {
							JOptionPane.showMessageDialog(null, "Debes insertar los datos del podcaster para añadirlo");
						}

					} else if (accion == 2) {

						if (listaPodcaster.isSelectionEmpty()) {
							JOptionPane.showMessageDialog(null, "Debes seleccionar un podcaster para modificar");
						} else {
							String nombrePodcasterSeleccionado = listaPodcaster.getSelectedValue();
							basededatos.modificarElementoListaPodcaster(nombrePodcasterSeleccionado,
									txtFNombreCrudPodcaster.getText(), txtFInfo1CrudPodcaster.getText(),
									txtFInfo2CrudPodcaster.getText(), listaPodcaster);
							JOptionPane.showMessageDialog(null, "El podcaster ha sido modificado con exito");
						}

					}
				}
				// En el caso de gestionar los podcasts 
				else {
					if (metodos.formatoDuracion(txtFInfo1CrudPodcaster.getText())) {
						if (accion == 1) {
							if (!txtFNombreCrudPodcaster.getText().isEmpty()
									|| !txtFInfo1CrudPodcaster.getText().isEmpty()
									|| !txtFInfo2CrudPodcaster.getText().isEmpty()) {

								basededatos.anadirPodcast(txtFNombreCrudPodcaster, txtFInfo1CrudPodcaster,
										txtFInfo2CrudPodcaster, listaPodcaster, cmbxCrudPodcast, podcasters);
								JOptionPane.showMessageDialog(null, "El podcaster ha sido insertado con exito.");

							} else {
								JOptionPane.showMessageDialog(null,
										"Debes insertar los datos del podcaster para añadirlo");
							}

						} else if (accion == 2) {

							if (listaPodcaster.isSelectionEmpty()) {
								JOptionPane.showMessageDialog(null, "Debes seleccionar un podcaster para modificar");
							} else {
								String nombrePodcasterSeleccionado = listaPodcaster.getSelectedValue();
								basededatos.modificarElementoListaPodcast(nombrePodcasterSeleccionado,
										txtFNombreCrudPodcaster.getText(), txtFInfo1CrudPodcaster.getText(),
										txtFInfo2CrudPodcaster.getText(),
										podcasters.get(cmbxCrudPodcast.getSelectedIndex()).getPodcasts()
												.get(listaPodcaster.getSelectedIndex()),
										listaPodcaster, cmbxCrudPodcast, podcasters);
								JOptionPane.showMessageDialog(null, "El podcaster ha sido modificado con exito");

							}

						}
					} else {
						JOptionPane.showMessageDialog(null, "Formato de duracion incorrecto");
					}
				}
				metodos.ocultarComponentesPodcaster(lblNombreCrudPodcaster, lblInfo2CrudPodcaster,
						lblInfo1CrudPodcaster, lblInfo3CrudPodcast, txtFNombreCrudPodcaster, txtFInfo1CrudPodcaster,
						txtFInfo2CrudPodcaster, btnAceptarPodcaster, cmbxCrudPodcast);
			}

		});
		btnAceptarPodcaster.setBounds(396, 380, 89, 23);
		panelGestPodcaster.add(btnAceptarPodcaster);

		JButton btnAñadirPodcaster = new JButton("Añadir");
		btnAñadirPodcaster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accion = 1;
				// carga la lista con su informacion correspondiente
				if (elementoGestionado == 4) {
					metodos.cargarCrudPodcaster(lblNombreCrudPodcaster, lblInfo1CrudPodcaster, lblInfo2CrudPodcaster,
							txtFNombreCrudPodcaster, txtFInfo1CrudPodcaster, txtFInfo2CrudPodcaster);
				} else {

					metodos.cargarCrudPodcast(lblNombreCrudPodcaster, lblInfo1CrudPodcaster, lblInfo2CrudPodcaster,
							txtFNombreCrudPodcaster, txtFInfo1CrudPodcaster, txtFInfo2CrudPodcaster, cmbxCrudPodcast,
							lblInfo3CrudPodcast);
				}
				btnAceptarPodcaster.setVisible(true);
			}
		});
		btnAñadirPodcaster.setBounds(329, 43, 89, 23);
		panelGestPodcaster.add(btnAñadirPodcaster);

		JButton btnModificarPodcaster = new JButton("Modificar");
		btnModificarPodcaster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accion = 2;
				// si gestiona podcasters y la lista no esta vacia, dependiendo el podcaster elegido por el usuario saca su informacion
				if (elementoGestionado == 4) {
					int i = listaPodcaster.getSelectedIndex();
					if (i != -1) {
						String nombrePodcaster = listaPodcaster.getSelectedValue();
						Podcaster podcasterSeleccionado = basededatos.obtenerPodcaster(nombrePodcaster);
						txtFNombreCrudPodcaster.setText(podcasterSeleccionado.getNombreArtista());
						txtFInfo1CrudPodcaster.setText(podcasterSeleccionado.getGenero());
						txtFInfo2CrudPodcaster.setText(podcasterSeleccionado.getDescripcion());

					} else {

						JOptionPane.showMessageDialog(null, "Debes seleccionar un podcaster para modificar");
					}
					metodos.cargarCrudPodcaster(lblNombreCrudPodcaster, lblInfo1CrudPodcaster, lblInfo2CrudPodcaster,
							txtFNombreCrudPodcaster, txtFInfo1CrudPodcaster, txtFInfo2CrudPodcaster);
				}
				// si gestiona podcasts y la lista no esta vacia, dependiendo el podcast elegido por el usuario saca su informacion
				else if (elementoGestionado == 5) {
					int i = listaPodcaster.getSelectedIndex();
					if (i != -1) {
						String nombrePodcast = listaPodcaster.getSelectedValue();
						Podcast podcastSeleccionado = basededatos.obtenerPodcast(nombrePodcast);
						txtFNombreCrudPodcaster.setText(nombrePodcast);
						txtFInfo1CrudPodcaster.setText(podcastSeleccionado.getDuracion());
						txtFInfo2CrudPodcaster.setText(podcastSeleccionado.getColaboradores());

					}
					metodos.cargarCrudPodcast(lblNombreCrudPodcaster, lblInfo1CrudPodcaster, lblInfo2CrudPodcaster,
							txtFNombreCrudPodcaster, txtFInfo1CrudPodcaster, txtFInfo2CrudPodcaster, cmbxCrudPodcast,
							lblInfo3CrudPodcast);
				}

				btnAceptarPodcaster.setVisible(true);
			}

		});
		btnModificarPodcaster.setBounds(437, 43, 89, 23);
		panelGestPodcaster.add(btnModificarPodcaster);

		txtFNombreCrudPodcaster = new JTextField();
		txtFNombreCrudPodcaster.setBounds(364, 102, 244, 20);
		panelGestPodcaster.add(txtFNombreCrudPodcaster);
		txtFNombreCrudPodcaster.setColumns(10);

		txtFInfo2CrudPodcaster = new JTextField();
		txtFInfo2CrudPodcaster.setColumns(10);
		txtFInfo2CrudPodcaster.setBounds(364, 214, 244, 88);
		panelGestPodcaster.add(txtFInfo2CrudPodcaster);

		txtFInfo1CrudPodcaster = new JTextField();
		txtFInfo1CrudPodcaster.setColumns(10);
		txtFInfo1CrudPodcaster.setBounds(364, 158, 244, 20);
		panelGestPodcaster.add(txtFInfo1CrudPodcaster);

		podcasterTotales = new String[podcasters.size()];
		// Almacenamiento de los podcasters que aparecene en el comboBox
		for (int i = 0; i != podcasters.size(); i++) {
			podcasterTotales[i] = podcasters.get(i).getNombreArtista();
		}
		cmbxCrudPodcast = new JComboBox<String>();
		cmbxCrudPodcast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel<String> listModel = new DefaultListModel<String>();
				listaPodcaster.setModel(listModel);

				for (int i = 0; i != podcasters.get(cmbxCrudPodcast.getSelectedIndex()).getPodcasts().size(); i++) {
					listModel.addElement(
							podcasters.get(cmbxCrudPodcast.getSelectedIndex()).getPodcasts().get(i).getNombre());
				}
			}
		});
		cmbxCrudPodcast.setModel(new DefaultComboBoxModel<String>(podcasterTotales));
		cmbxCrudPodcast.setVisible(false);
		cmbxCrudPodcast.setBounds(364, 335, 244, 22);
		panelGestPodcaster.add(cmbxCrudPodcast);

		lblInfo3CrudPodcast = new JLabel("");
		lblInfo3CrudPodcast.setVisible(false);
		lblInfo3CrudPodcast.setBounds(364, 313, 188, 14);
		panelGestPodcaster.add(lblInfo3CrudPodcast);

	}
}
