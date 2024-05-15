package modelo;

import java.util.ArrayList;

/**
 * Clase que representa a un usuario.
 */
public class Usuarios {
	private int idUsuario;
	private String nombre;
	private String apellido;
	private String usuario;
	private String contrasena;
	private String fec_nacimiento;
	private String fec_registro;
	private ArrayList<PlayList> playlists = new ArrayList<PlayList>();
	private String tipoCliente;

	/**
	 * Método para obtener el ID del usuario.
	 * 
	 * @return ID del usuario.
	 */
	public int getIdUsuario() {
		return idUsuario;
	}

	/**
	 * Método para establecer el ID del usuario.
	 * 
	 * @param idUsuario ID del usuario.
	 */
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * Método para obtener el tipo de cliente.
	 * 
	 * @return tipo de cliente
	 */
	public String getTipoCliente() {
		return tipoCliente;
	}

	/**
	 * Método para establecer el tipo de cliente.
	 * 
	 * @param tipoCliente tipo de cliente.
	 */
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Método para obtener la lista de playlists del usuario.
	 * 
	 * @return La lista de playlists del usuario.
	 */
	public ArrayList<PlayList> getPlaylists() {
		return playlists;
	}

	/**
	 * Método para establecer la lista de playlists del usuario.
	 * 
	 * @param playlists La lista de playlists del usuario.
	 */
	public void setPlaylists(ArrayList<PlayList> playlists) {
		this.playlists = playlists;
	}

	/**
	 * Método para obtener el nombre del usuario.
	 * 
	 * @return nombre del usuario.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Método para establecer el nombre del usuario.
	 * 
	 * @param nombre nombre del usuario.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Método para obtener el apellido del usuario.
	 * 
	 * @return apellido del usuario.
	 */
	public String getApellido() {
		return apellido;
	}

	/**
	 * Método para establecer el apellido del usuario.
	 * 
	 * @param apellido apellido del usuario.
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	/**
	 * Método para obtener el nombre de usuario.
	 * 
	 * @return nombre de usuario.
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Método para establecer el nombre de usuario.
	 * 
	 * @param usuario nombre de usuario.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Método para obtener la contraseña del usuario.
	 * 
	 * @return contraseña del usuario.
	 */
	public String getContrasena() {
		return contrasena;
	}

	/**
	 * Método para establecer la contraseña del usuario.
	 * 
	 * @param contrasena contraseña del usuario.
	 */
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	/**
	 * Método para obtener la fecha de nacimiento del usuario.
	 * 
	 * @return fecha de nacimiento del usuario.
	 */
	public String getFec_nacimiento() {
		return fec_nacimiento;
	}

	/**
	 * Método para establecer la fecha de nacimiento del usuario.
	 * 
	 * @param fec_nacimiento fecha de nacimiento del usuario.
	 */
	public void setFec_nacimiento(String fec_nacimiento) {
		this.fec_nacimiento = fec_nacimiento;
	}

	/**
	 * Método para obtener la fecha de registro del usuario.
	 * 
	 * @return fecha de registro del usuario.
	 */
	public String getFec_registro() {
		return fec_registro;
	}

	/**
	 * Método para establecer la fecha de registro del usuario.
	 * 
	 * @param fec_registro fecha de registro del usuario.
	 */
	public void setFec_registro(String fec_registro) {
		this.fec_registro = fec_registro;
	}

	/**
	 * Constructor de la clase Usuarios.
	 * 
	 * @param idUsuario      Identificador del usuario.
	 * @param nombre         Nombre del usuario.
	 * @param apellido       Apellido del usuario.
	 * @param usuario        Nombre de usuario.
	 * @param contrasena     Contraseña del usuario.
	 * @param fec_nacimiento Fecha de nacimiento del usuario.
	 * @param fec_registro   Fecha de registro del usuario.
	 * @param playlists      Lista de playlists del usuario.
	 * @param tipoCliente    Tipo de cliente.
	 */
	public Usuarios(int idUsuario, String nombre, String apellido, String usuario, String contrasena,
			String fec_nacimiento, String fec_registro, ArrayList<PlayList> playlists, String tipoCliente) {
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.apellido = apellido;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.fec_nacimiento = fec_nacimiento;
		this.fec_registro = fec_registro;
		this.playlists = playlists;
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Constructor de la clase Usuarios.
	 * 
	 * @param nombre         Nombre del usuario.
	 * @param apellido       Apellido del usuario.
	 * @param usuario        Nombre de usuario.
	 * @param contrasena     Contraseña del usuario.
	 * @param fec_nacimiento Fecha de nacimiento del usuario.
	 * @param fec_registro   Fecha de registro del usuario.
	 * @param tipoCliente    Tipo de cliente.
	 */
	public Usuarios(String nombre, String apellido, String usuario, String contrasena, String fec_nacimiento,
			String fec_registro, String tipoCliente) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.fec_nacimiento = fec_nacimiento;
		this.fec_registro = fec_registro;
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Constructor por defecto de la clase Usuarios.
	 */
	public Usuarios() {
	}

	/**
	 * Constructor de la clase Usuarios.
	 * 
	 * @param idUsuario      Identificador del usuario.
	 * @param nombre         Nombre del usuario.
	 * @param apellido       Apellido del usuario.
	 * @param usuario        Nombre de usuario.
	 * @param contrasena     Contraseña del usuario.
	 * @param fec_nacimiento Fecha de nacimiento del usuario.
	 * @param fec_registro   Fecha de registro del usuario.
	 * @param tipoCliente    Tipo de cliente.
	 * @param playlists      Lista de playlists del usuario.
	 */
	public Usuarios(int idUsuario, String nombre, String apellido, String usuario, String contrasena,
			String fec_nacimiento, String fec_registro, String tipoCliente, ArrayList<PlayList> playlists) {
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.apellido = apellido;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.fec_nacimiento = fec_nacimiento;
		this.fec_registro = fec_registro;
		this.playlists = playlists;
		this.tipoCliente = tipoCliente;
	}

	@Override
	public String toString() {
		return "Usuarios [idUsuario=" + idUsuario + ", nombre=" + nombre + ", apellido=" + apellido + ", usuario="
				+ usuario + ", contrasena=" + contrasena + ", fec_nacimiento=" + fec_nacimiento + ", fec_registro="
				+ fec_registro + ", playlists=" + playlists + ", tipoCliente=" + tipoCliente + "]";
	}

}
