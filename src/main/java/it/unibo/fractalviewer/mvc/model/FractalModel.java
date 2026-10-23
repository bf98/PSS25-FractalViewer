package it.unibo.fractalviewer.mvc.model;

public final class FractalModel {
	private int width = 640;
	private int height = 480;

	public void setResolution(final int width, final int height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("La risoluzione deve essere positiva");
		}
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
}
