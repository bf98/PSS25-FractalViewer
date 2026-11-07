package it.unibo.fractalviewer.mvc.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test della classe FractalRenderer.
 *
 * Verifica la generazione dell'immagine, la conversione
 * delle coordinate, la colorazione dei pixel e la gestione
 * dei parametri non validi.
 */
class FractalRendererTest {
	// Utilizziamo un calculator fittizio in modo da verificare esclusivamente
	// il comportamento di FractalRenderer, escludendo così possibili casi speciali legati a 
	// Mandelbrot, Julia etc (vedi: "test double", "test stub").
	private final FractalCalculator calculator = (real, imaginary, maxIterations) -> 5;
	private final FractalRenderer renderer = new FractalRenderer(calculator);

	/**
	 * Verifica che il renderer generi il numero corretto di pixel.
	 */
	@Test
	void testImageDimensions() {
		int[] pixels = renderer.render(200, 100, -0.5, 0.0, 0.005,100);
		assertEquals(20_000, pixels.length);
	}

	/**
	 * Verifica che risoluzioni non valide vengano rifiutate.
	 */
	@Test
	void testInvalidResolution() {
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(0, 600, 0, 0, 0.005, 100));
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(800, -600, 0, 0, 0.005, 100));
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(5000, 5000, 0, 0, 0.005, 100));
	}

	/**
	 * Verifica che parametri di rendering non validi
	 * generino un'eccezione.
	 */
	@Test
	void testInvalidParameters() {
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(10, 10, 0, 0, 0, 100));
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(10, 10, 0, 0, -1, 100));
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(10, 10, 0, 0, 1, 0));
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(10, 10, Double.NaN, 0, 1, 100));
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(10, 10, 0, Double.POSITIVE_INFINITY, 1, 100));
	}

	/**
	 * Verifica la conversione delle coordinate dei pixel
	 * in punti del piano complesso.
	 */
	@Test
	void testCoordinateMapping() {
		// Consideriamo un'immagine 2x2 con centro (0, 0) e scala 1.
		// Le componenti di ciascun pixel vengono così calcolate:
		// real = centerX + ( x - ( width / 2 ) ) * scale
		// imaginary = centerY - ( y - ( height / 2 ) ) * scale
		double[][] expected = {
														{ -1.0, 1.0 },
														{ 0.0, 1.0 },
														{ -1.0, 0.0 },
														{ 0.0, 0.0 },
													};

		int[] index = { 0 };

		FractalCalculator testCalculator = (real, imaginary, maxIterations) -> {

			int position = index[0]++;

			assertTrue(position < expected.length);
			assertEquals(expected[position][0], real, 1e-12);
			assertEquals(expected[position][1], imaginary, 1e-12);

			return 5;
		};

		FractalRenderer testRenderer = new FractalRenderer(testCalculator);
		testRenderer.render(2, 2, 0, 0, 1, 100);

		assertEquals(4, index[0]);
	}

	/**
	 * Verifica che i pixel siano memorizzati
	 * nell'ordine corretto all'interno dell'array.
	 */
	@Test
	void testPixelOrder() {
		// La funzione lambda in questione ci permette di generare due pixel con colori differenti.
		FractalCalculator testCalculator = (real, imaginary, maxIterations) ->
																				(int) (real + 1);
		FractalRenderer testRenderer = new FractalRenderer(testCalculator);

		int[] pixels = testRenderer.render(2, 1, 0, 0, 1, 100);

		// Secondo la formula per calcolare la tonalità di grigio:
		// 255 * 0 / 100 == 0
		// 255 * 1 / 100 == 2
		assertArrayEquals(
				new int[] {
					0xFF000000,
					0xFF020202,
				},
				pixels
		);
	}
	
	/**
	 * Verifica che il renderer utilizzi correttamente
	 * il calcolatore fornito.
	 */
	@Test
	void testCalculatorUsage() {
		int[] calls = {0};

		FractalCalculator testCalculator = (real, imaginary, maxIterations) -> {
			calls[0]++;
			assertEquals(100, maxIterations);
			return 50;
		};

		FractalRenderer testRenderer = new FractalRenderer(testCalculator);

		int[] pixels = testRenderer.render(2, 2, 0, 0, 1, 100);

		assertEquals(4, calls[0]);

		// 255 * 50 / 100 == 127 (7F)
		for (int pixel : pixels) {
			assertEquals(0xFF7F7F7F, pixel);
		}
	}

	/**
	 * Verifica che i punti che raggiungono il limite
	 * massimo in iterazioni siano rappresentati in nero.
	 */
	@Test
	void testInternalPointColor() {
		FractalRenderer testRenderer = new FractalRenderer((real, imaginary, maxIterations)
																												-> maxIterations);
		int[] pixels = testRenderer.render(2, 2, 0, 0, 1, 100);

		// Il calculator restituisce sempre maxIterations, quindi tutti i pixel devono essere neri.
		for (int pixel : pixels) {
			assertEquals(0xFF000000, pixel);
		}
	}

	/**
	 * Verifica che il renderer produca correttamente
	 * una tonalità intermedia di grigio.
	 */
	@Test
	void testGrayscaleColor() {
		FractalRenderer testRenderer = new FractalRenderer((real, imaginary, maxIterations)
																												-> 50);
		int[] pixels = testRenderer.render(2, 2, 0, 0, 1, 100);
		
		// 255 * 50 / 100 == 127 (7F)
		for (int pixel : pixels) {
			assertEquals(0xFF7F7F7F, pixel);
		}
	}

	/**
	 * Verifica che non sia possibile creare un renderer
	 * con un calcolatore nullo.
	 */
	@Test 
	void testNullCalculator() {
		assertThrows(NullPointerException.class,
									() -> new FractalRenderer(null));
	}
}
