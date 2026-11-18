package it.unibo.fractalviewer.mvc.model;

// Necessario per conversione HSB to RGB.
import java.awt.Color;

public final class RainbowPalette implements ColorPalette {
  @Override
  public int colorFor(final int iterations, final int maxIterations) {
    if (iterations >= maxIterations) {
      return 0xFF000000;
    }
    float hue = (float) iterations / maxIterations;

    return Color.HSBtoRGB(hue, 0.8f, 1.0f);
  }

  @Override
  public String getName() {
     return "Rainbow";
  }
}