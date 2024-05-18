package controlador;

import javax.swing.JOptionPane;

public class MiExcepcion extends Exception {

	private static final long serialVersionUID = 1L;

	public MiExcepcion(String mensaje, int errorOffset) {
		super(mensaje);
		JOptionPane.showMessageDialog(null, mensaje);
	}

	public MiExcepcion(String mensaje) {
		super(mensaje);
	}

	public MiExcepcion(String mensaje, Throwable causa) {
		super(mensaje, causa);
	}

}