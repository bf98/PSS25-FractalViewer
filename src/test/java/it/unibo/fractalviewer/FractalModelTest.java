package it.unibo.fractalviewer.mvc.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test della classe FractalModel.
 *
 * Verifica la gestione dei parametri della visualizzazione
 * e il rifiuto dei parametri non validi.
 */
class FractalModelTest {

	private final FractalModel model = new FractalModel();

	/**
	 * Verifica i valori iniziali dei parametri.
	 */
	@Test
	void testDefaultValues() {
		assertEquals(640, model.getWidth());
		assertEquals(480, model.getHeight());
		assertEquals(-0.5, model.getCenterX());
		assertEquals(0.0, model.getCenterY());
		assertEquals(0.005, model.getScale());
		assertEquals(100, model.getMaxIterations());
	}

	/**
	 * Verifica l'aggiornamento della risoluzione.
	 */
	@Test 
	void testSetResolution() {
		model.setResolution(800, 600);
		assertEquals(800, model.getWidth());
		assertEquals(600, model.getHeight());
	}

	/**
	 * Verifica l'aggiornamento del centro nel piano complesso.
	 */
	@Test
	void testSetCenter() {
		model.setCenter(-0.75, 0.25);
		assertEquals(-0.75, model.getCenterX());
		assertEquals(0.25, model.getCenterY());
	}

	/**
	 * Verifica l'aggiornamento della scala.
	 */
	@Test
	void testSetScale() {
		model.setScale(0.001);
		assertEquals(0.001, model.getScale());
	}

	/**
	 * Verifica l'aggiornamento del limite delle iterazioni.
	 */
	@Test
	void testSetMaxIterations() {
		model.setMaxIterations(500);
		assertEquals(500, model.getMaxIterations());
	}

	/**
	 * Verifica che i parametri non validi vengano rifiutati.
	 */
	@Test
	void testInvalidParameters() {
		// Verifica risoluzione non valida.
		assertThrows(IllegalArgumentException.class,
									() -> model.setResolution(0, 600));
		assertThrows(IllegalArgumentException.class,
									() -> model.setResolution(800, -1));
		assertThrows(IllegalArgumentException.class,
									() -> model.setResolution(5000, 5000));

		assertEquals(640, model.getWidth());
		assertEquals(480, model.getHeight());

		// Verifica coordinate del centro non valide.
		assertThrows(IllegalArgumentException.class,
									() -> model.setCenter(Double.NaN, 0));
		assertThrows(IllegalArgumentException.class,
									() -> model.setCenter(0, Double.POSITIVE_INFINITY));

		assertEquals(-0.5, model.getCenterX());
		assertEquals(0.0, model.getCenterY());

		// Verifica scala non valida.
		assertThrows(IllegalArgumentException.class,
									() -> model.setScale(0));
		assertThrows(IllegalArgumentException.class,
									() -> model.setScale(-1));
		assertThrows(IllegalArgumentException.class,
									() -> model.setScale(Double.NaN));
		assertThrows(IllegalArgumentException.class,
									() -> model.setScale(Double.POSITIVE_INFINITY));

		assertEquals(0.005, model.getScale());

		// Verifica numero massimo di iterazioni non valido.
		assertThrows(IllegalArgumentException.class,
									() -> model.setMaxIterations(0));
		assertThrows(IllegalArgumentException.class,
									() -> model.setMaxIterations(-10));

		assertEquals(100, model.getMaxIterations());
	}
}
