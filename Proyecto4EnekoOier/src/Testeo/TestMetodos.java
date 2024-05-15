package Testeo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.junit.Test;

import controlador.*;

public class TestMetodos {
	Metodos metodos = new Metodos();

	// --------------------------------------------------------------- Test validar
	// segunda linea --------------------------------------------------------
	@Test
	public void testValidarSegundaLinea_NumerosValidos() {

		// Segunda línea con números válidos
		String segundaLinea = "1;2;3;4;5";
		// Llamada al método a testear.
		int[] resultado = Metodos.validarSegundaLinea(segundaLinea);
		// Comprobación de que el array contiene los números esperados.
		assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, resultado);
	}

	@Test
	public void testValidarSegundaLinea_NumerosNoValidos() {

		// Segunda línea con números no válidos
		String segundaLinea = "1;a;3;4;5";
		// Llamada al método a testear
		int[] resultado = Metodos.validarSegundaLinea(segundaLinea);
		// Comprobación de que se devolvió un array vacío
		assertEquals(0, resultado.length);
	}

	@Test
	public void testValidarSegundaLinea_SegundaLineaVacia() {

		// Segunda línea vacía
		String segundaLinea = "";
		// Llamada al método a testear
		int[] resultado = Metodos.validarSegundaLinea(segundaLinea);
		// Comprobación de que se devolvió un array vacío
		assertEquals(0, resultado.length);
	}

	// --------------------------------------------------------------- Test validar
	// segunda linea --------------------------------------------------------
	@Test
	public void testMostrarComponentes() {
		JLabel lblNombreArtista = new JLabel();
		JLabel lblDescripcionArtista = new JLabel();
		JLabel lblCaracteristicaArtista = new JLabel();
		JTextField txtFNombreArtista = new JTextField();
		JTextField txtFDescripcionArtista = new JTextField();
		JComboBox<String> comboBox = new JComboBox<String>();
		JButton btnArtistaAceptar = new JButton();
		metodos.mostrarComponentes(lblNombreArtista, lblDescripcionArtista, lblCaracteristicaArtista, txtFNombreArtista,
				txtFDescripcionArtista, comboBox, btnArtistaAceptar);
		assertTrue(lblNombreArtista.isVisible());
		assertTrue(lblDescripcionArtista.isVisible());
		assertTrue(lblCaracteristicaArtista.isVisible());
		assertTrue(txtFNombreArtista.isVisible());
		assertTrue(txtFDescripcionArtista.isVisible());
		assertTrue(comboBox.isVisible());
		assertTrue(btnArtistaAceptar.isVisible());
	}
}
