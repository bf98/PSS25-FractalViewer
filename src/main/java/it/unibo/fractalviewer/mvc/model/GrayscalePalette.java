package it.unibo.fractalviewer.mvc.model;

/**
 * Implementa una palette in scala di grigi.
 */
public final class GrayscalePalette implements ColorPalette {
  @Override
  public int colorFor(final int iterations, final int maxIterations) {
    if (iterations >= maxIterations) {
      return 0xFF000000;
    }
    int gray = (int) (255.0 * iterations / maxIterations);
    
    return 0xFF000000 | (gray << 16) | (gray << 8) | gray;
  }

  @Override
  public String getName() {
    return "Grayscale";
  }
}