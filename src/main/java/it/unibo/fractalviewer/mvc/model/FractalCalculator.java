package it.unibo.fractalviewer.mvc.model;

/**
 * Definisce il comportamento comune dei "calcolatori" di frattali.
 * Le implementazioni di questa interfaccia calcolano il numero di
 * iterazioni associate a un punto del piano complesso, seguendo 
 * le regole del frattale implementato.
 */
public interface FractalCalculator {

	/**
	 * Calcola il numero di iterazioni associate a un punto del piano complesso.
	 * @param real La parte reale del punto.
	 * @param imaginary La parte immaginaria del punto.
	 * @param maxIterations Il numero massimo di iterazioni.
	 * @return Il numero di iterazioni calcolate.
	 */
	int calculate(double real, double imaginary, int maxIterations);
}
