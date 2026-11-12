package it.unibo.fractalviewer.mvc.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.unibo.fractalviewer.mvc.model.FractalCalculator;
import it.unibo.fractalviewer.mvc.model.FractalModel;
import it.unibo.fractalviewer.mvc.model.FractalRenderer;

/**
 * Test della classe FractalController.
 *
 * Verifica il coordinamento tra FractalModel e FractalRenderer
 * e l'aggiornamento dei parametri della visualizzazione.
 */
class FractalControllerTest {
	
	// Utilizziamo un calculator fittizio in modo da verificare esclusivamente
	// il comportamento di FractalController, escludendo così possibili casi speciali legati a 
	// Mandelbrot, Julia etc (vedi: "test double", "test stub").
	private final FractalCalculator calculator = (real, imaginary, maxIterations) -> 5;
	private final FractalModel model = new FractalModel();
	private final FractalRenderer renderer = new FractalRenderer(calculator);
	private final FractalController controller = new FractalController(model, renderer);

	/**
	 * Verifica che il costruttore rifiuti argomenti NULL.
	 */
	@Test
	void testConstructor() {
		assertThrows(NullPointerException.class,
									() -> new FractalController(null, renderer));
		assertThrows(NullPointerException.class,
									() -> new FractalController(model, null));
		assertThrows(NullPointerException.class,
									() -> new FractalController(null, null));
	}

	/**
	 * Verifica la generazione dell'immagine utilizzando
	 * i parametri conservati nel FractalModel.
	 */
	@Test
	void testRender() {
		controller.setResolution(200, 100);
		int[] pixels = controller.render();

		assertNotNull(pixels);
		assertEquals(200 * 100, pixels.length);

		assertEquals(0xFF0C0C0C, pixels[0]);
	}

	/**
	 * Verifica l'aggiornamento della risoluzione.
	 */
	@Test
	void testSetResolution() {
		controller.setResolution(800, 600);

		assertEquals(800, model.getWidth());
		assertEquals(600, model.getHeight());
		assertEquals(800 * 600, controller.render().length);
	}

	/**
	 * Verifica l'aggiornamento del centro 
	 * della visualizzazione.
	 */
	@Test
	void testSetCenter() {
		controller.setCenter(-0.75, 0.25);

		assertEquals(-0.75, model.getCenterX());
		assertEquals(0.25, model.getCenterY());
	}

	/**
	 * Verifica l'aggiornamento della scala.
	 */
	@Test
	void testSetScale() {
		controller.setScale(0.001);

		assertEquals(0.001, model.getScale());
	}

	/**
	 * Verifica l'aggiornamento del numero massimo
	 * di iterazioni consentite.
	 */
	@Test
	void testSetMaxIterations() {
		controller.setMaxIterations(500);

		assertEquals(500, model.getMaxIterations());
	}

	/**
	 * Verifica che il FractalController rifiuti parametri non validi
	 * senza alterare lo stato del FractalModel.
	 */
	@Test
	void testInvalidParameters() {
		assertThrows(IllegalArgumentException.class,
									() -> controller.setResolution(0, 600));

		assertThrows(IllegalArgumentException.class,
									() -> controller.setCenter(Double.NaN, 0));

		assertThrows(IllegalArgumentException.class,
									() -> controller.setScale(-1));

		assertThrows(IllegalArgumentException.class,
									() -> controller.setMaxIterations(0));

		assertEquals(640, model.getWidth());
		assertEquals(480, model.getHeight());

		assertEquals(-0.5, model.getCenterX());
		assertEquals(0.0, model.getCenterY());
		
		assertEquals(0.005, model.getScale());
		assertEquals(100, model.getMaxIterations());
	}
}
