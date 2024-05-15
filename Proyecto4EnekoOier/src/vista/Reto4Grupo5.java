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
import modelo.Album;
import modelo.Musico;
import modelo.Podcaster;
import modelo.Tipo;
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
	private JPanel contentPane;
	BasedeDatos basededatos = new BasedeDatos();
	Metodos metodos = new Metodos();
	private boolean premium = false;
	private JFormattedTextField txtFRegistroFecNac;
	private JPasswordField pswFRegistroContra, pswFRegistroConfContra;
	private JTextField txtFRegistroNombre, txtFRegistroUsuario, txtFRegistroApellido, txtFNombreCancionMenu;
	private JButton btnRegistroGuardar, btnRegistroEditar, btnReproducir, btnAdelanteCancion, btnAtrasCancion;
	private JPanel panelArtistas, panelAlbumes, panelCanciones, panelReproduccion, panelPlaylist, panelMenu,
			panelCrudMusica;
	private JLabel lblReproduciendoSelec, lblAlbumSelec;
	private JList<String> listaPlaylist, listaMenu, listaCrudMusica;
	private int cambioX = 0, cambioY = 0, cont = 0, cont2 = 0;
	private int elementoGestionado = 0;
	private static Clip clip;
	private static long clipTimePosition = 0; // Almacena la posición de la canción al detenerla
	private Usuarios usuarioIniciado = null;
	private ArrayList<Musico> musicos = new ArrayList<Musico>();
	private ArrayList<Podcaster> podcasters = new ArrayList<Podcaster>();
	private Podcaster podcasterElegido = new Podcaster();
	private Musico musicoElegido = new Musico();
	private Album albumElegido = new Album();
	private int audioElegido = -1, posicionPodcast = -1, opcionEscogida = -1;
	private int accion = 0;
	private JComboBox<String> cmbxArtista;
	private Image imgPredeterminada = new ImageIcon(
			Paths.get("").toAbsolutePath().toString() + "\\img\\predeterminado.jpg").getImage();

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
	 */
	public Reto4Grupo5() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 692, 500);
		String imagenArtistas1 = "\\img\\imagenArtistas1.jpg", imagenArtistas2 = "\\img\\imagenArtistas2.jpg";
		String idLogin = "Interfaz de Login", idRegistro = "Registro", idArtistas = "Interfaz de elección del artista";
		String idMenu = "Menu", idAlbum = "Album", idCanciones = "Canciones", idPlaylist = "Playlist",
				idReproducir = "Reproducir";
		String idGestion = "Gestion", idMenuGestMusica = "Menu Gestion Musica";
		String idbtnMenu = "btnMenu", idEstadisticas = "Estadisticas", idPanelCrudMusica = "CrudMusica";
		LocalDate fecha_registro = LocalDate.now();
		LocalDate fecha_finpremium = fecha_registro.plusYears(1);
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String diaString = fecha_registro.format(formato);
		String premiumfinal = fecha_finpremium.format(formato);

		contentPane = new JPanel();
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
				} else if (panelMenu.isVisible()) {
					metodos.cambiardePantalla(layeredPane, idLogin);
					btnAtras.setEnabled(false);
					btnPerfil.setEnabled(false);
					btnPerfil.setText("Inicia Sesion");
				} else if (panelCrudMusica.isVisible()) {
					metodos.cambiardePantalla(layeredPane, idMenuGestMusica);
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
				if (cmbxTipo.getSelectedItem().equals("ADMINISTRADOR")) {
					lblPreguntaCuenta.setVisible(false);
					lblCrearCuenta.setVisible(false);
				} else {
					lblPreguntaCuenta.setVisible(true);
					lblCrearCuenta.setVisible(true);
				}
			}
		});
		cmbxTipo.setModel(new DefaultComboBoxModel<String>(new String[] { "CLIENTE", "ADMINISTRADOR" }));
		cmbxTipo.setFont(new Font("MS Gothic", Font.BOLD, 13));
		cmbxTipo.setBounds(406, 278, 227, 22);
		panelLogin.add(cmbxTipo);

		JButton btnIniciarSesion = new JButton("Iniciar Sesión");
		btnIniciarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("hola");

				usuarioIniciado = basededatos.inicioSesion(txtFUsuario.getText(), new String(pswFContra.getPassword()));

				if (usuarioIniciado != null
						|| (cmbxTipo.getSelectedItem().equals("ADMINISTRADOR") && txtFUsuario.getText().equals("admin")
								&& new String(pswFContra.getPassword()).equals("123"))) {
					JOptionPane.showMessageDialog(null, "Sesión iniciada correctamente");

					if ((!cmbxTipo.getSelectedItem().equals("ADMINISTRADOR"))) {
						btnPerfil.setText(usuarioIniciado.getUsuario());
						btnPerfil.setEnabled(true);
						metodos.cambiardePantalla(layeredPane, "Menu");
					} else {
						metodos.cambiardePantalla(layeredPane, idGestion);
					}

					btnAtras.setEnabled(true);
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
//------------------------------------------- Fin Panel Login, Inicio Panel Menu----------------------------------------------------------------
		panelMenu = new JPanel();
		panelMenu.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelMenu, idMenu);
		panelMenu.setLayout(null);
		JLabel lblNuevaMus = new JLabel("");
		lblNuevaMus.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				metodos.borrarPanel(panelArtistas);
				btnAtras.setEnabled(true);
				metodos.cambiardePantalla(layeredPane, idArtistas);
				musicos = basededatos.conseguirArtistas();
				cont = 0;
				cont2 = 0;
				do {

					JLabel lblFoto = new JLabel();
					lblFoto.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							opcionEscogida = 0;
							metodos.cambiardePantalla(layeredPane, idAlbum);
							metodos.borrarPanel(panelAlbumes);
							cont = 0;
							musicoElegido = musicos.get(Integer.parseInt(lblFoto.getToolTipText()));
							do {
								JLabel lblalbumFoto = new JLabel();
								try {
									lblalbumFoto.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString()
											+ "/img/" + musicoElegido.getAlbumes().get(cont).getImagen()));
								} catch (Exception e1) {
									// Si no se encuentra la imagen, establece una imagen predeterminada
									ImageIcon iconoPredeterminado = new ImageIcon(
											imgPredeterminada.getScaledInstance(65, 63, Image.SCALE_SMOOTH));
									lblalbumFoto.setIcon(iconoPredeterminado);
								}
								lblalbumFoto.addMouseListener(new MouseAdapter() {
									public void mouseClicked(MouseEvent e) {
										cont = 0;
										metodos.cambiardePantalla(layeredPane, idCanciones);
										metodos.borrarPanel(panelCanciones);
										albumElegido = musicoElegido.getAlbumes()
												.get(Integer.parseInt(lblalbumFoto.getToolTipText()));
										Image img = new ImageIcon(Paths.get("").toAbsolutePath().toString() + "\\img\\"
												+ albumElegido.getImagen()).getImage();
										ImageIcon img2 = new ImageIcon(
												img.getScaledInstance(108, 117, Image.SCALE_SMOOTH));

										do {
											JLabel lblFotoCancion = new JLabel();
											lblFotoCancion.addMouseListener(new MouseAdapter() {
												public void mouseClicked(MouseEvent e) {
													metodos.cambiardePantalla(layeredPane, idReproducir);
													audioElegido = Integer.parseInt(lblFotoCancion.getToolTipText());
												}
											});
											try {
												lblFotoCancion.setIcon(img2);
											} catch (Exception e1) {
												// Si no se encuentra la imagen, establece una imagen predeterminada
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
							try {
								lblfotoDescripcion.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString()
										+ "/img/" + musicoElegido.getNombreArtista().replace(" ", "") + "Desc.jpg"));
							} catch (Exception e1) {

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

						}
					});
					try {
						lblFoto.setIcon(new ImageIcon(
								Paths.get("").toAbsolutePath().toString() + "/img/" + musicos.get(cont).getImagen()));
					} catch (Exception e1) {
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
					if (cont2 != 3) {
						cambioX = cambioX + 228;
					} else {
						cambioX = 0;
						cambioY = cambioY + 176;
						cont2 = 0;
					}

				} while (cont != musicos.size());
				cambioX = 0;
				cambioY = 0;
				cont = 0;
				cont2 = 0;

			}
		});
		lblNuevaMus.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + "/img/descubririMúsica.jpg"));
		lblNuevaMus.setBounds(92, 65, 124, 120);
		panelMenu.add(lblNuevaMus);

		JLabel lblNuevoPod = new JLabel("");
		lblNuevoPod.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				opcionEscogida = 1;
				metodos.borrarPanel(panelArtistas);
				btnAtras.setEnabled(true);
				metodos.cambiardePantalla(layeredPane, idArtistas);
				podcasters = basededatos.conseguirPodcasters();
				cont = 0;
				cambioX = 0;
				cont2 = 0;
				do {
					JLabel lblFoto = new JLabel();
					lblFoto.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							metodos.cambiardePantalla(layeredPane, idAlbum);

							podcasterElegido = podcasters.get(Integer.parseInt(lblFoto.getToolTipText()));
							metodos.borrarPanel(panelAlbumes);
							cont = 0;
							cambioY = 0;
							do {
								JLabel lblCapitulo = new JLabel(podcasterElegido.getPodcasts().get(cont).getNombre());
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
							try {
								lblfotoArtista.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + "/img/"
										+ podcasterElegido.getNombreArtista().replace(" ", "") + "Desc.jpg"));
							} catch (Exception e1) {
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
						}
					});
					System.out.println(podcasters.get(cont).getImagen());
					try {
						lblFoto.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + "/img/"
								+ podcasters.get(cont).getImagen()));
					} catch (Exception e1) {
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
					if (cont2 != 3) {
						cambioX = cambioX + 228;
					} else {
						cambioX = 0;
						cambioY = cambioY + 176;
						cont2 = 0;
					}

				} while (cont != podcasters.size());

				cambioX = 0;
				cambioY = 0;
				cont = 0;
				cont2 = 0;

			}
		});
		lblNuevoPod.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString() + "/img/podcast.jpg"));
		lblNuevoPod.setBounds(283, 65, 124, 120);
		panelMenu.add(lblNuevoPod);

		JLabel lblPlayList = new JLabel("");
		lblPlayList.addMouseListener(new MouseAdapter() {

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
//------------------------------------------- Fin Panel Menu, Inicio Panel Registro----------------------------------------------------------------
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
			MaskFormatter formatos = new MaskFormatter("####-##-##");
			formatos.setPlaceholderCharacter('-');
			txtFRegistroFecNac = new JFormattedTextField(formatos);
			txtFRegistroFecNac.setEnabled(false);
			txtFRegistroFecNac.setColumns(10);
			txtFRegistroFecNac.setBounds(223, 239, 165, 20);
			panelRegistro.add(txtFRegistroFecNac);
		} catch (ParseException ex) {
			ex.printStackTrace();
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
			public void actionPerformed(ActionEvent e) {
				boolean registrado = false;
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

//------------------------------------------- Fin Panel Registro, Inicio Panel Artistas----------------------------------------------------------------
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

		JButton btnMenu = new JButton("Menu");
		btnMenu.addActionListener(new ActionListener() {
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

		JButton btnMeGusta = new JButton("Me Gusta");
		btnMeGusta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (clip != null && clip.isRunning()) {
					if (opcionEscogida == 0) {
						basededatos.anadirCancionLike(usuarioIniciado,
								albumElegido.getCanciones().get(audioElegido).getIdAudio());
						basededatos.anadirEstadisticasLike(albumElegido.getCanciones().get(audioElegido).getIdAudio(),
								opcionEscogida);
					} else {
						basededatos.anadirCancionLike(usuarioIniciado,
								podcasterElegido.getPodcasts().get(audioElegido).getIdAudio());
						basededatos.anadirEstadisticasLike(
								podcasterElegido.getPodcasts().get(audioElegido).getIdAudio(), opcionEscogida);
					}
					JOptionPane.showMessageDialog(null, "¡Canción añadida a Me gusta!");
				} else {
					JOptionPane.showMessageDialog(null, "No hay ninguna canción reproduciéndose actualmente.");
				}
			}
		});

		btnMeGusta.setFont(new Font("Book Antiqua", Font.BOLD, 13));
		btnMeGusta.setBounds(468, 350, 139, 23);
		panelReproduccion.add(btnMeGusta);

		btnAtrasCancion = new JButton("<");
		btnAtrasCancion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(audioElegido == 0)) {
					btnReproducir.setText("Reproducir");
					if (!(clip == null) && clip.isRunning()) {
						clip.stop();
					}

					if (!usuarioIniciado.getTipoCliente().equals(Tipo.premium)) {
						try {
							Random random = new Random();
							int numeroAleatorio = random.nextInt(6) + 1;
							File file = new File(Paths.get("").toAbsolutePath().toString() + "\\musica\\anuncio"
									+ numeroAleatorio + ".wav");
							AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
							clip = AudioSystem.getClip();
							clip.open(audioInputStream);
							clip.start();
							Thread.sleep(clip.getMicrosecondLength() / 1000);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						audioElegido--;
					}
					if (!btnAdelanteCancion.isEnabled()) {
						btnAdelanteCancion.setEnabled(true);
					}

				} else {
					btnAtrasCancion.setEnabled(false);
					JOptionPane.showMessageDialog(null, "No se puede ir para atras neno");

				}
			}
		});
		btnAtrasCancion.setBounds(183, 350, 55, 23);
		panelReproduccion.add(btnAtrasCancion);

		btnAdelanteCancion = new JButton(">");
		btnAdelanteCancion.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if ((!(audioElegido == albumElegido.getCanciones().size() - 1) && opcionEscogida == 0)
						|| (!(posicionPodcast == podcasterElegido.getPodcasts().size() - 1)) && opcionEscogida == 1) {
					btnReproducir.setText("Reproducir");

					if (!(clip == null) && clip.isRunning()) {
						clip.stop();
					}

					if (!usuarioIniciado.getTipoCliente().equals(Tipo.premium)) {
						try {
							Random random = new Random();
							int numeroAleatorio = random.nextInt(6) + 1;
							File file = new File(Paths.get("").toAbsolutePath().toString() + "\\musica\\anuncio"
									+ numeroAleatorio + ".wav");
							AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
							clip = AudioSystem.getClip();
							clip.open(audioInputStream);
							clip.start();
							Thread.sleep(clip.getMicrosecondLength() / 1000);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						audioElegido++;

					}
					if (!btnAtrasCancion.isEnabled()) {
						btnAtrasCancion.setEnabled(true);
					}
				} else {
					btnAdelanteCancion.setEnabled(false);
					JOptionPane.showMessageDialog(null, "No se puede ir para Adelante neno");
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
				if (btnReproducir.getText().equals("Reproducir")) {
					try {
						File file = null;
						if (opcionEscogida == 0) {
							Image img = new ImageIcon(
									Paths.get("").toAbsolutePath().toString() + "\\img\\" + albumElegido.getImagen())
									.getImage();
							ImageIcon img2 = new ImageIcon(img.getScaledInstance(275, 210, Image.SCALE_SMOOTH));
							lblAlbum.setText("Album:");
							file = albumElegido.getCanciones().get(audioElegido).reproducir(lblDuracionSelec,
									lblReproduciendoSelec, btnX05, btnX1, btnX15);
							lblAlbumSelec.setText(albumElegido.getTitulo().toUpperCase());
							lblFotoReproduccion.setIcon(img2);
						} else {
							lblAlbum.setText("Podcast:");
							file = podcasterElegido.getPodcasts().get(audioElegido).reproducir(lblDuracionSelec,
									lblReproduciendoSelec, btnX05, btnX1, btnX15);
							lblFotoReproduccion.setBounds(251, 129, 275, 210);
							lblFotoReproduccion.setIcon(new ImageIcon(Paths.get("").toAbsolutePath().toString()
									+ "/img/" + podcasterElegido.getImagen()));
						}
						AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
						clip = AudioSystem.getClip();
						clip.open(audioInputStream);
						clip.start();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					btnReproducir.setText("Parar");
				} else if (btnReproducir.getText().equals("Parar")) {
					if (clip != null && clip.isRunning()) {
						clipTimePosition = clip.getMicrosecondPosition();
						clip.stop();
						System.out.println("Canción detenida.");
						btnReproducir.setText("Reanudar");
					}
				} else if (btnReproducir.getText().equals("Reanudar")) {
					btnReproducir.setText("Parar");
					clip.setMicrosecondPosition(clipTimePosition);
					clip.start();
					System.out.println("Canción reanudada.");
				}
			}
		});
		btnReproducir.setFont(new Font("Book Antiqua", Font.BOLD, 13));
		btnReproducir.setBounds(251, 350, 139, 23);
		panelReproduccion.add(btnReproducir);

		// ------------------------------------------- Fin Panel Reproduccion, Inicio
		// Panel
		// Gestion----------------------------------------------------------------

		JPanel panelAdmin = new JPanel();
		panelAdmin.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelAdmin, idGestion);
		panelAdmin.setLayout(null);

		JButton btnGestMusica = new JButton("Gestionar musica");
		btnGestMusica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				metodos.cambiardePantalla(layeredPane, idMenuGestMusica);
			}
		});
		btnGestMusica.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnGestMusica.setBounds(263, 153, 167, 23);
		panelAdmin.add(btnGestMusica);

		JButton btnGestPodcats = new JButton("Gestionar podcast");
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
		lblAdmin.setFont(new Font("Rockwell Condensed", Font.PLAIN, 25));
		lblAdmin.setBounds(293, 86, 123, 56);
		panelAdmin.add(lblAdmin);
		// ------------------------------------------- Fin Panel Gestion, Inicio
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
			public void actionPerformed(ActionEvent e) {
				if (listaPlaylist.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(null, "Debes seleccionar una PlayList");
				} else {
					basededatos.borrarPlayList(listaPlaylist, usuarioIniciado.getUsuario());
				}
			}
		});
		btnBorrarPlaylist.setBounds(379, 78, 120, 23);
		panelPlaylist.add(btnBorrarPlaylist);

		JButton btnImportar = new JButton("Importar");
		btnImportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				usuarioIniciado = metodos.importarPlayList(usuarioIniciado, listaPlaylist);
			}
		});
		btnImportar.setBounds(379, 112, 120, 23);
		panelPlaylist.add(btnImportar);

		JButton btnExportar = new JButton("Exportar");
		btnExportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int posicionSeleccionada = listaPlaylist.getSelectedIndex();
				metodos.exportarPlayList(usuarioIniciado, posicionSeleccionada);
			}
		});
		btnExportar.setBounds(379, 146, 120, 23);
		panelPlaylist.add(btnExportar);

		JScrollPane scrollPaneTablaPlaylist = new JScrollPane();
		scrollPaneTablaPlaylist.setBounds(354, 198, 302, 200);
		panelPlaylist.add(scrollPaneTablaPlaylist);

		JTable tablaInfoPlaylist = new JTable();
		scrollPaneTablaPlaylist.setViewportView(tablaInfoPlaylist);
		listaPlaylist = new JList<String>();
		listaPlaylist.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				basededatos.mostrarInfoPlaylist(tablaInfoPlaylist, usuarioIniciado.getIdUsuario(),
						listaPlaylist.getSelectedValue());
			}
		});
		listaPlaylist.setBounds(61, 44, 250, 354);
		panelPlaylist.add(listaPlaylist);
		// Panel Menu GestionarMusica
		JPanel panelMenuGestMusica = new JPanel();
		panelMenuGestMusica.setBackground(new Color(215, 223, 234));
		layeredPane.add(panelMenuGestMusica, idMenuGestMusica);
		panelMenuGestMusica.setLayout(null);

		JButton btnGestArtistas = new JButton("Gestionar artistas");
		btnGestArtistas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				metodos.cambiardePantalla(layeredPane, idPanelCrudMusica);
				elementoGestionado = 1;
				basededatos.obtenerYActualizarLista(listaCrudMusica, elementoGestionado);
			}
		});
		btnGestArtistas.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnGestArtistas.setBounds(43, 141, 178, 94);
		panelMenuGestMusica.add(btnGestArtistas);

		JButton btnGestAlbumes = new JButton("Gestionar Albumes");
		btnGestAlbumes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				elementoGestionado = 2;
				metodos.cambiardePantalla(layeredPane, idPanelCrudMusica);
			}
		});
		btnGestAlbumes.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnGestAlbumes.setBounds(231, 141, 196, 94);
		panelMenuGestMusica.add(btnGestAlbumes);

		JButton btnGestCanciones = new JButton("Gestionar canciones");
		btnGestCanciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				metodos.cambiardePantalla(layeredPane, idPanelCrudMusica);
				elementoGestionado = 3;
			}
		});
		btnGestCanciones.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnGestCanciones.setBounds(437, 141, 196, 94);
		panelMenuGestMusica.add(btnGestCanciones);

		//////////////////////////////////////// Panel Boton Menu
		//////////////////////////////////////// //////////////////////////////////////////////////////////
		JPanel panelBtnMenu = new JPanel();
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
			public void actionPerformed(ActionEvent e) {
				metodos.comprobarInsertarCancionPlaylist(albumElegido.getCanciones().get(audioElegido).getIdAudio(),
						listaMenu, usuarioIniciado);
			}
		});
		btnAnadirPlayList.setBounds(120, 374, 220, 27);
		panelBtnMenu.add(btnAnadirPlayList);

		JButton btnExportarCancion = new JButton("Exportar");
		btnExportarCancion.addActionListener(new ActionListener() {
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

//////////////////////////////////////////////////////////////////////////////////////////////////////////
		JPanel panelEstadisticas = new JPanel();
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
			public void actionPerformed(ActionEvent e) {
				basededatos.obtenerTopCanciones(tablaEstadisticas);
			}
		});
		btnTopCanciones.setBounds(10, 11, 155, 23);
		panelEstadisticas.add(btnTopCanciones);

		JButton btnTopPodcasts = new JButton("Top Podcasts");
		btnTopPodcasts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				basededatos.obtenerTopPodcasts(tablaEstadisticas);
			}
		});
		btnTopPodcasts.setBounds(175, 11, 155, 23);
		panelEstadisticas.add(btnTopPodcasts);

		JButton btnTopPlaylist = new JButton("Top Playlist");
		btnTopPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				basededatos.obtenerTopPlaylist(tablaEstadisticas);
			}
		});
		btnTopPlaylist.setBounds(340, 11, 155, 23);
		panelEstadisticas.add(btnTopPlaylist);

		JButton btnMasEscuchados = new JButton("Mas Escuchados");
		btnMasEscuchados.addActionListener(new ActionListener() {
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
		cmbxCrudTipo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (elementoGestionado == 3 && accion == 2) {
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

		ArrayList<Musico> musicos = basededatos.conseguirArtistas();
		String[] artistas = new String[musicos.size()];
		for (int i = 0; i != musicos.size(); i++) {
			artistas[i] = musicos.get(i).getNombreArtista();
		}
		cmbxArtista = new JComboBox<String>();
		cmbxArtista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Establece todos los albumes en el combobox si gestionamos canciones
				if (elementoGestionado == 3) {
					String[] albumes = new String[musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().size()];
					for (int i = 0; i != musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().size(); i++) {
						albumes[i] = musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().get(i).getTitulo();
					}
					cmbxCrudTipo.setModel(new DefaultComboBoxModel<String>(albumes));
					// Establece todos los albumes en la lista si gestionamos albumes
				} else {
					DefaultListModel<String> listModel = new DefaultListModel<>();
					listaCrudMusica.setModel(listModel);
					// For para que aparezcan todos los albumes en la lista
					for (int i = 0; i != musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().size(); i++)
						listModel.addElement(
								musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().get(i).getTitulo());
				}
			}
		});
		cmbxArtista.setModel(new DefaultComboBoxModel<String>(artistas));
		cmbxArtista.setVisible(false);
		cmbxArtista.setBounds(397, 283, 259, 22);
		panelCrudMusica.add(cmbxArtista);

		JButton btnAceptarCrudMusica = new JButton("ACEPTAR");
		btnAceptarCrudMusica.setVisible(false);
		btnAceptarCrudMusica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!txtFNombreCrud.getText().equals("") && !txtFInfo1Crud.getText().equals("")) {
					if (accion == 1) {
						boolean artistaRepetido = false;
						if (elementoGestionado == 1) {

							for (int i = 0; i < listaCrudMusica.getModel().getSize(); i++) {
								if (listaCrudMusica.getModel().getElementAt(i).equals(txtFNombreCrud.getText())) {
									artistaRepetido = true;
								}
							}
							if (artistaRepetido) {
								JOptionPane.showMessageDialog(null, "El artista que deseas insertar ya existe");
							} else {
								basededatos.añadirElementoLista(txtFNombreCrud.getText(), txtFInfo1Crud.getText(),
										cmbxCrudTipo.getSelectedItem(), listaCrudMusica, elementoGestionado,
										musicos.get(cmbxArtista.getSelectedIndex()));
								Album albumNuevo = new Album(txtFNombreCrud.getText(),
										Integer.parseInt(cmbxCrudTipo.getSelectedItem().toString()),
										txtFInfo1Crud.getText());
								musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().add(albumNuevo);
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
										cmbxCrudTipo.getSelectedItem(), listaCrudMusica, elementoGestionado,
										musicos.get(cmbxArtista.getSelectedIndex()));
								Album albumNuevo = new Album(txtFNombreCrud.getText(),
										Integer.parseInt(cmbxCrudTipo.getSelectedItem().toString()),
										txtFInfo1Crud.getText());
								musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes().add(albumNuevo);
							}
						}
					} else if (accion == 2) {
						if (elementoGestionado == 1) {
							if (listaCrudMusica.isSelectionEmpty()) {
								JOptionPane.showMessageDialog(null, "Debes seleccionar un artista para modificar");
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
							basededatos.modificarAlbum(txtFNombreCrud.getText(), txtFInfo1Crud.getText(),
									cmbxCrudTipo.getSelectedItem(), listaCrudMusica,
									musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes()
											.get(listaCrudMusica.getSelectedIndex()));
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

			public void actionPerformed(ActionEvent e) {
				accion = 1;
				if (elementoGestionado == 1) {
					metodos.cargarCrudArtistas(lblNombreCrud, lblInfo1Crud, lblInfo2Crud, txtFNombreCrud, txtFInfo1Crud,
							cmbxCrudTipo);

				} else {
					metodos.cargarCrudAlbumesycancion(lblNombreCrud, lblInfo1Crud, lblInfo2Crud, txtFNombreCrud,
							txtFInfo1Crud, cmbxCrudTipo, lblCrudArtista, cmbxArtista, elementoGestionado);
				}
				btnAceptarCrudMusica.setVisible(true);
			}
		});
		btnAñadirCrudMusica.setBounds(350, 44, 89, 23);
		panelCrudMusica.add(btnAñadirCrudMusica);

		JButton btnModificarCrudMusica = new JButton("Modificar");
		btnModificarCrudMusica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (elementoGestionado == 1) {
					if (listaCrudMusica.getSelectedIndex() != -1) {
						Musico musicoSeleccionado = musicos.get(listaCrudMusica.getSelectedIndex());
						txtFNombreCrud.setText(musicoSeleccionado.getNombreArtista());
						txtFInfo1Crud.setText(musicoSeleccionado.getDescripcion());
						cmbxCrudTipo.setSelectedItem(musicoSeleccionado.getCaracteristica());
						metodos.cargarCrudArtistas(lblNombreCrud, lblInfo1Crud, lblInfo2Crud, txtFNombreCrud,
								txtFInfo1Crud, cmbxCrudTipo);
					} else {
						JOptionPane.showMessageDialog(null, "Debes seleccionar un artista para modificar");
					}

				} else if (elementoGestionado == 2) {
					metodos.cargarCrudAlbumesycancion(lblNombreCrud, lblInfo1Crud, lblInfo2Crud, txtFNombreCrud,
							txtFInfo1Crud, cmbxCrudTipo, lblCrudArtista, cmbxArtista, elementoGestionado);

				}
				accion = 2;
				btnAceptarCrudMusica.setVisible(true);
			}

		});
		btnModificarCrudMusica.setBounds(449, 44, 89, 23);
		panelCrudMusica.add(btnModificarCrudMusica);

		JButton btnBorrarCrudMusica = new JButton("Borrar");
		btnBorrarCrudMusica.addActionListener(new ActionListener() {
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
			@Override
			public void mouseClicked(MouseEvent e) {
				if (elementoGestionado == 2 && accion == 2) {
					Album albumSeleccionado = musicos.get(cmbxArtista.getSelectedIndex()).getAlbumes()
							.get(listaCrudMusica.getSelectedIndex());
					txtFNombreCrud.setText(albumSeleccionado.getTitulo());
					txtFInfo1Crud.setText(albumSeleccionado.getGenero());
				}
			}
		});
		scrollPane.setViewportView(listaCrudMusica);
		//////////////////// Panel gestionar artista //////////////////////////////

		// botones gestionar musica

		///////////////////////////////////

	}
}
