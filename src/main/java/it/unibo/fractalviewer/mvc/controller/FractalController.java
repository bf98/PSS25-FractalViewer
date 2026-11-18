package it.unibo.fractalviewer.mvc.controller;

import java.util.Objects;

import it.unibo.fractalviewer.mvc.model.FractalModel;
import it.unibo.fractalviewer.mvc.model.FractalRenderer;

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
			model.getMaxIterations()
		);
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
}
