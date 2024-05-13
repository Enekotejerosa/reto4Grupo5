package controlador;

public class MiExcepcion extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MiExcepcion() {
		super();
	}

	MiExcepcion(String mensaje) {
		System.out.println("Imagen no encontrada, se inserta la predeterminada");
	}
}