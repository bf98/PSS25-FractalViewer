package it.unibo.fractalviewer.mvc.model;

import java.util.Objects;

/**
 * Conserva i parametri della visualizzazione corrente del frattale
 * (risoluzione dell'immagine, centro nel piano complesso, scala,
 * numero massimo di iterazioni consentite).
 */
public final class FractalModel {

	private int width = 640;
	private int height = 480;

	private static final double DEFAULT_CENTER_X = -0.5;
	private static final double DEFAULT_CENTER_Y = 0.0;
	private static final double DEFAULT_SCALE = 0.005;

	private double centerX = DEFAULT_CENTER_X;
	private double centerY = DEFAULT_CENTER_Y;
	private double scale = DEFAULT_SCALE; 

	private int maxIterations = 100;

	private FractalCalculator calculator = new MandelbrotCalculator();
	private ColorPalette palette = new GrayscalePalette();

	private static final long MAX_PIXELS = 10_000_000L;

	/**
	 * Ripristina i parametri iniziali della navigazione.
	 */
	public void resetNavigation() {
		this.centerX = DEFAULT_CENTER_X;
		this.centerY = DEFAULT_CENTER_Y;
		this.scale = DEFAULT_SCALE;
	}

	/**
	 * Aggiorna la risoluzione dell'immagine.
	 *
	 * @param width La larghezza dell'immagine.
	 * @param height L'altezza dell'immagine.
	 * @throws IllegalArgumentException Se la risoluzione non è valida.
	 */
	public void setResolution(final int width, final int height) {
		if (width <= 0 || height <= 0 || (long) width * height > MAX_PIXELS) {
			throw new IllegalArgumentException("La risoluzione deve essere positiva.");
		}
		this.width = width;
		this.height = height;
	}

	/**
	 * Aggiorna il centro della visualizzazione nel piano complesso.
	 *
	 * @param centerX La coordinata reale del centro.
	 * @param centerY La coordinata immaginaria del centro.
	 */
	public void setCenter(final double centerX, final double centerY) {
		if (!Double.isFinite(centerX) || !Double.isFinite(centerY)) {
			throw new IllegalArgumentException("Coordinate del centro non valide.");
		}
		this.centerX = centerX;
		this.centerY = centerY;
	}

	/**
	 * Aggiorna la scala della visualizzazione.
	 *
	 * @param scale La dimensione di un pixel nel piano complesso.
	 */
	public void setScale(final double scale) {
		if (!Double.isFinite(scale) || scale <= 0) {
			throw new IllegalArgumentException("La scala deve essere positiva e finita.");
		}
		this.scale = scale;
	}

	/**
	 * Aggiorna il numero massimo di iterazioni consentite.
	 *
	 * @param maxIterations Il numero massimo di iterazioni consentite.
	 */
	public void setMaxIterations(final int maxIterations) {
		if (maxIterations <= 0) {
			throw new IllegalArgumentException("Il numero di iterazioni deve essere positivo.");
		}
		this.maxIterations = maxIterations;
	}

	/**
	 * Aggiorna il FractalCalculator utilizzato nella visualizzazione, 
	 * quindi la tipologia di frattale mostrata.
	 *
	 * @param calculator Il nuovo Calculator.
	 * @throws NullPointerException Se il Calculator è NULL.
	 */
	public void setCalculator(final FractalCalculator calculator) {
		this.calculator = Objects.requireNonNull(calculator);
	}

	/**
	 * Aggiorna la palette utilizzata per la visualizzazione.
	 *
	 * @param palette La nuova palette di colori.
	 * @throws NullPointerException Se la palette è NULL.
	 */
	public void setPalette(final ColorPalette palette) {
		this.palette = Objects.requireNonNull(palette);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public double getCenterX() {
		return this.centerX;
	}

	public double getCenterY() {
		return this.centerY;
	}

	public double getScale() {
		return this.scale;
	}

	public int getMaxIterations() {
		return this.maxIterations;
	}

	public FractalCalculator getCalculator() {
		return this.calculator;
	}

	public ColorPalette getPalette() {
		return this.palette;
	}
}
