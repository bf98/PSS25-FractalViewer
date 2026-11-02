package it.unibo.fractalviewer.mvc.model;

import java.util.Objects;

/**
 * Genera un'immagine di un frattale a partire da un algoritmo
 * che implementa l'interfaccia FractalCalculator.
 *
 * Spiegazione del funzionamento:
 * Il renderer converte le coordinate dei pixel in punti del 
 * piano complesso e assegna un color a ciascun pixel in base 
 * al numero di iterazioni calcolate.
 */
public final class FractalRenderer {

	private static final long MAX_PIXELS = 10_000_000L;

	private final FractalCalculator calculator;

	/** 
	 * Crea un renderer utilizzando il calcolatore (i.e. algoritmo) specificato.
	 *
	 * @param calculator L'algoritmo utilizzato per il calcolo.
	 * @throws NullPointerException Se calculator è NULL.
	 */
	public FractalRenderer(final FractalCalculator calculator) {
		this.calculator = Objects.requireNonNull(calculator);
	}

	/**
	 * Genera un'immagine del frattale, in formato ARGB. 
	 *
	 * @param width La larghezza dell'immagine.
	 * @param height L'altezza dell'immagine.
	 * @param centerX La coordinata reale del centro.
	 * @param centerY La coordinata immaginare del centro.
	 * @param scale L'unità del piano complesso per pixel.
	 * @param maxIterations Il numero massimo di iterazioni consentite.
	 * @return L'array contenente i colori ARGB dei pixel.
	 * @throws IllegalArgumentException Se i parametri non sono validi.
	 */
	public int[] render(final int width, final int height, final double centerX, final double centerY, final double scale, final int maxIterations) {

		if (width <= 0 || height <= 0|| (long) width * height > MAX_PIXELS || !Double.isFinite(centerX)
				|| !Double.isFinite(centerY) || !Double.isFinite(scale) || scale <= 0 || maxIterations <= 0) {
					throw new IllegalArgumentException("Parametri di rendering non validi.");
				}
		
		int[] pixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double real = centerX + (x - width / 2.0) * scale;
				double imaginary = centerY - (y - height / 2.0) * scale;

				int iterations = calculator.calculate(real, imaginary, maxIterations);

				// Tutto questo verrà poi sostituito con palette di colore.
				int color;

				if (iterations >= maxIterations) {
					// Rappresenta in nero i punti che raggiungo il limite.
					// Il byte più significativo (FF) rappresenta l'opacità del colore.
					// Seguono poi i byte per Red, Green, Blue.
					color = 0xFF000000;
				} else {
					// Calcola l'intensità del grigio in funzione delle iterazioni.
					int gray = (int) (255.0 * iterations / maxIterations);

					// "Compone" il colore ARGB finale, utilizzando
					// la stessa intensità per i canali rosso, verde e blu, con opacità massima.
					color = 0xFF000000 | (gray << 16) | (gray << 8) | gray;
				}

				pixels[y * width + x] = color;
			}
		}

		return pixels;
	}
}
