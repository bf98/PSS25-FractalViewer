package it.unibo.fractalviewer.mvc.model;

/**
*  Definisce il comportamento comune delle palette di colore.
*/
public interface ColorPalette {
  /**
  *  Restituisce il colore ARGB associato a un punto.
  *  @param iterations Le iterazioni eseguite.
  *  @param maxIterations Il limite massimo di iterazioni.
  *  @return Il colore del punto in formato ARGB.
  */
  int colorFor(int iterations, int maxIterations);
  String getName();
}