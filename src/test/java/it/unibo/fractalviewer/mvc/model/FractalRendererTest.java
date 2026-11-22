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
	private final FractalRenderer renderer = new FractalRenderer();
	private final ColorPalette palette = new GrayscalePalette();

	/**
	 * Verifica che il renderer generi il numero corretto di pixel.
	 */
	@Test
	void testImageDimensions() {
		int[] pixels = renderer.render(200, 100, -0.5, 0.0, 0.005, 100, calculator, palette);
		assertEquals(20_000, pixels.length);
	}

	/**
	 * Verifica che risoluzioni non valide vengano rifiutate.
	 */
	@Test
	void testInvalidResolution() {
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(0, 600, 0, 0, 0.005, 100, calculator, palette));
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(800, -600, 0, 0, 0.005, 100, calculator, palette));
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(5000, 5000, 0, 0, 0.005, 100, calculator, palette));
	}

	/**
	 * Verifica che parametri di rendering non validi
	 * generino un'eccezione.
	 */
	@Test
	void testInvalidParameters() {
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(10, 10, 0, 0, 0, 100, calculator, palette));
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(10, 10, 0, 0, -1, 100, calculator, palette));
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(10, 10, 0, 0, 1, 0, calculator, palette));
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(10, 10, Double.NaN, 0, 1, 100, calculator, palette));
		assertThrows(IllegalArgumentException.class, 
								 () -> renderer.render(10, 10, 0, Double.POSITIVE_INFINITY, 1, 100, calculator, palette));
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

		renderer.render(2, 2, 0, 0, 1, 100, testCalculator, palette);

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
		int[] pixels = renderer.render(2, 1, 0, 0, 1, 100, testCalculator, palette);

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

		int[] pixels = renderer.render(2, 2, 0, 0, 1, 100, testCalculator, palette);

		assertEquals(4, calls[0]);

		// 255 * 50 / 100 == 127 (7F)
		for (int pixel : pixels) {
			assertEquals(0xFF7F7F7F, pixel);
		}
	}

	/**
	 * Verifica che il renderer utilizzi la palette ricevuta
	 * per determinare il colore dei pixel.
	 */
	@Test
	void testColorPalette() {
		// Istanziamo una palette fittizia che restituisce sempre lo stesso colore.
		ColorPalette palette = new ColorPalette() {
			@Override
			public int colorFor(final int iterations, final int maxIterations) {
				return 0xFFFF0000;
			}

			@Override
			public String getName() {
				return "Palette";
			}
		};

		int[] pixels = renderer.render(2, 2, 0.0, 0.0, 1.0, 100, calculator, palette);

		for (int pixel : pixels) {
			assertEquals(0xFFFF0000, pixel);
		}
	}

	/**
	 * Verifica che il renderer rifiuti una palette nulla.
	 */
	@Test
	void testNullPalette() {
		assertThrows(NullPointerException.class,
									() -> renderer.render(
												2, 2, 0.0, 0.0, 1.0, 100, calculator, null
									));
	}
}
