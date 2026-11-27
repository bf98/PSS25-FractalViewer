package it.unibo.fractalviewer.mvc.controller;

import java.util.Objects;
import java.util.function.Supplier;

import it.unibo.fractalviewer.mvc.model.FractalModel;
import it.unibo.fractalviewer.mvc.model.FractalCalculator;
import it.unibo.fractalviewer.mvc.model.FractalRenderer;
import it.unibo.fractalviewer.mvc.model.ColorPalette;

/**
 * Coordina la gestione dello stato della visualizzazione (FractalModel)
 * e la generazione dell'immagine del frattale (FractalRenderer).
 */
public final class FractalController {

	private final FractalModel model;
	private final FractalRenderer renderer;

	/**
	 * Crea un controller utilizzando il FractalModel e il FractalRenderer.
	 *
	 * @param model Lo stato della visualizzazione.
	 * @param renderer Il componente per la generazione dei pixel.
	 * @throws NullPointerException Se un componente è NULL.
	 */
	public FractalController(final FractalModel model, final FractalRenderer renderer) {
		this.model = Objects.requireNonNull(model);
		this.renderer = Objects.requireNonNull(renderer);
	}

	/**
	 * Genera l'immagine utilizzando i parametri correnti.
	 *
	 * @return L'array dei pixel in formato ARGB.
	 */
	public int[] render() {
		return renderer.render(
			model.getWidth(),
			model.getHeight(),
			model.getCenterX(),
			model.getCenterY(),
			model.getScale(),
			model.getMaxIterations(),
			model.getCalculator(),
			model.getPalette()
		);
	}

	/**
	 * Prepara un'operazione di rendering utilizzando i parametri
	 * correnti della visualizzazione.
	 * I parametri vengono acquisiti prima dell'esecuzione,
	 * evitando che successive modifiche al FractalModel li alterino.
	 *
	 * @return Operazione per generare i pixel del frattale.
	 */
	public Supplier<int[]> prepareRender() {
		final int width = model.getWidth();
		final int height = model.getHeight();
		final double centerX = model.getCenterX();
		final double centerY = model.getCenterY();
		final double scale = model.getScale();
		final int maxIterations = model.getMaxIterations();

		final FractalCalculator calculator = model.getCalculator();
		final ColorPalette palette = model.getPalette();

		// Restituiamo l'operazione senza eseguire subito
		// il rendering.
		return () -> renderer.render(
													width,
													height,
													centerX,
													centerY,
													scale,
													maxIterations,
													calculator,
													palette);
	}

	/**
	 * Aggiorna il centro della visualizzazione in funzione
	 * dello spostamento effettuato sul canvas.
	 *
	 * Spiegazione del funzionamento:
	 * Se il mouse viene trascinato verso destra, dobbiamo spostare
	 * il centro della visualizzazione verso sinistra in modo da dare 
	 * l'impressione che il frattale segua il mouse.
	 *
	 * Le formule utilizzate sono:
	 * new_centerX = centerX - deltaX * scale
	 * new_centerY = centerY + deltaY * scale
	 *
	 * @param deltaX Lo spostamento orizzontale in pixel.
	 * @param deltaY Lo spostamento verticale in pixel.
	 */
	public void pan(final double deltaX, final double deltaY) {
		double scale = model.getScale();

		double centerX = model.getCenterX() - deltaX * scale;
		double centerY = model.getCenterY() + deltaY * scale;

		model.setCenter(centerX, centerY);
	}
	
	/**
	 * Aggiorna la scala della visualizzazione applicando
	 * un fattore di zoom rispetto al centro del frattale.
	 * 
	 * @param factor Il fattore di ingrandimento.
	 * @throws IllegalArgumentException Se il frattale di ingrandimento non è valido.
	 */
	public void zoom(final double factor) {
		if (!Double.isFinite(factor) || factor <= 0) {
			throw new IllegalArgumentException("Fattore di zoom non valido");
		}
		model.setScale(model.getScale() / factor);
	}

	/**
	 * Ripristina posizione e scala iniziali 
	 * della visualizzazione.
	 */
	public void resetNavigation() {
		model.resetNavigation();
	}

	/**
	 * Aggiorna la risoluzione e adatta la scala della visualizzazione.
	 *
	 * @param width La larghezza della visualizzazione.
	 * @param height L'altezza della visualizzazione.
	 * @throws IllegalArgumentException Se i parametri non sono validi.
	 */
	public void resizeVisualization(final int width, final int height) {
		int oldWidth = model.getWidth();
		int oldHeight = model.getHeight();
		double oldScale = model.getScale();

		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("La risoluzione deve essere positiva.");
		}

		double widthRatio = (double) oldWidth / width;
		double heightRatio = (double) oldHeight / height;
		// new_scale = old_scale * max(old_width / width, old_height / height)
		// Il rapporto maggiore mantiene visibile l'intera regione
		// precedente, eventualmente mostrando spazio aggiuntivo.
		double factor = Math.max(widthRatio, heightRatio);
		double newScale = oldScale * factor;

		model.setResolutionAndScale(width, height, newScale);
	}

	/**
	 * Aggiorna la risoluzione della visualizzazione.
	 *
	 * @param width La nuova larghezza.
	 * @param height La nuova altezza.
	 */
	public void setResolution(final int width, final int height) {
		model.setResolution(width, height);
	}

	/**
	 * Aggiorna il centro della visualizzazione.
	 *
	 * @param centerX La coordinata reale del centro.
	 * @param centerY La coordinata immaginaria del centro.
	 */
	public void setCenter(final double centerX, final double centerY) {
		model.setCenter(centerX, centerY);
	}

	/**
	 * Aggiorna la scala della visualizzazione.
	 *
	 * @param scale La nuova scala.
	 */
	public void setScale(final double scale) {
		model.setScale(scale);
	}

	/**
	 * Aggiorna il numero massimo di iterazioni.
	 *
	 * @param maxIterations Il nuovo limite di iterazioni.
	 */
	public void setMaxIterations(final int maxIterations) {
		model.setMaxIterations(maxIterations);
	}

	/**
	 * Aggiorna il FractalCalculator utilizzato nella visualizzazione, 
	 * quindi la tipologia di frattale mostrata.
	 *
	 * @param calculator Il nuovo Calculator.
	 */
	public void setCalculator(final FractalCalculator calculator) {
		model.setCalculator(calculator);
	}

	/**
	 * Aggiorna la palette della visualizzazione.
	 *
	 * @param palette La nuova palette.
	 */
	public void setPalette(final ColorPalette palette) {
		model.setPalette(palette);
	}

	public int getMaxIterations() {
		return model.getMaxIterations();
	}
}
