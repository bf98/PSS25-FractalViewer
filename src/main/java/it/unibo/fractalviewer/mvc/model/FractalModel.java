package it.unibo.fractalviewer.mvc.model;

/**
 * Conserva i parametri della visualizzazione corrente del frattale
 * (risoluzione dell'immagine, centro nel piano complesso, scala,
 * numero massimo di iterazioni consentite).
 */
public final class FractalModel {

	private int width = 640;
	private int height = 480;

	private double centerX = -0.5;
	private double centerY = 0.0;
	private double scale = 0.005;

	private int maxIterations = 100;

	private static final long MAX_PIXELS = 10_000_000L;

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
}
