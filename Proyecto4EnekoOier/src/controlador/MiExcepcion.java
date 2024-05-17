package controlador;

import javax.swing.JOptionPane;

import com.google.protobuf.TextFormat.ParseException;

public class MiExcepcion extends ParseException {

	private static final long serialVersionUID = 1L;

	public MiExcepcion(String mensaje, int errorOffset) {
		super(mensaje);
		JOptionPane.showMessageDialog(null, mensaje);
	}

}