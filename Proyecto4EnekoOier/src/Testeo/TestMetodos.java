package Testeo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
		// Llamada al método a testear
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

	@Test
	public void testCrearModeloAnyos() {

		// Llamar al método
		String[] resultado = metodos.crearModeloAnyos();

		// Verificar el tamaño del array

		// Imprimir la longitud del array para depuración
		System.out.println("Longitud del array: " + resultado.length);

		// Verificar el tamaño del array
		assertEquals("El array debe tener 45 elementos", 45, resultado.length);

		// Verificar los valores del array
		for (int i = 0; i < resultado.length; i++) {

			assertEquals("El valor en la posición " + i + " no es el esperado", String.valueOf(1980 + i), resultado[i]);
		}

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

	@Test
	public void testFormatoDuracion_CasoValido() {
		assertTrue(metodos.formatoDuracion("12:34:56"));
	}

	@Test
	public void testFormatoDuracion_CasoInvalido_Null() {

		assertFalse(metodos.formatoDuracion(null));
	}

	@Test
	public void testFormatoDuracion_CasoInvalido_FormatoIncorrecto() {
		assertFalse(metodos.formatoDuracion("123456"));
	}

	@Test
	public void testFormatoDuracion_CasoInvalido_HorasFueraDeRango() {

		assertFalse(metodos.formatoDuracion("25:00:00"));
	}

	@Test
	public void testFormatoDuracion_CasoInvalido_MinutosFueraDeRango() {

		assertFalse(metodos.formatoDuracion("12:60:00"));
	}

	@Test
	public void testFormatoDuracion_CasoInvalido_SegundosFueraDeRango() {

		assertFalse(metodos.formatoDuracion("12:34:60"));
	}

	@Test
	public void testFormatoFecha() {
		
		// Casos válidos
		assertTrue(metodos.formatofecha("2024-01-31"));
		assertTrue(metodos.formatofecha("1970-02-28"));
		assertTrue(metodos.formatofecha("2024-12-01"));

		// Casos inválidos
		assertFalse(metodos.formatofecha("2025-01-01"));
		assertFalse(metodos.formatofecha("1969-12-31"));
		assertFalse(metodos.formatofecha("2000-02-29"));
		assertFalse(metodos.formatofecha("1970-13-01"));
		assertFalse(metodos.formatofecha("1970-02-30"));
		assertFalse(metodos.formatofecha("2001-02-29"));
		assertFalse(metodos.formatofecha("2024-11-31"));
	}
}
