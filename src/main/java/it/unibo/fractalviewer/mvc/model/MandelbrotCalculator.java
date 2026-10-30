package it.unibo.fractalviewer.mvc.model;

/**
 * Implementa l'algoritmo di calcolo dell'insieme di Mandelbrot.
 *
 * Spiegazione del funzionamento:
 * L'algoritmo utilizza la successione z(n+1) = z(n)^2 + c,
 * con z(0) = 0, dove c rappresenta il punto del piano complesso da analizzare.
 * Il calcolo termina quando il modulo al quadrato di z supera il valore 4 oppure
 * quando viene raggiunto il numero massimo di iterazioni consentite.
 */
public final class MandelbrotCalculator implements FractalCalculator {

		/** Raggio di fuga (al quadrato) utilizzato per rilevare la divergenza. */
		private  static final double ESCAPE_RADIUS_SQUARED = 4.0;

		/**
		 * Calcola il numero di iterazioni per un punto del piano complesso.
		 * 
		 * Spiegazione del funzionamento:
		 * Il metodo applica la successione di Mandelbrot finché il punto
		 * non supera il raggio di fuga oppure viene raggiunto il limite massimo
		 * di iterazioni.
		 *
		 * @param real La componente reale del punto complesso.
		 * @param imaginary La componente immaginaria del punto complesso.
		 * @param maxIterations Il numero massimo di iterazioni consentite.
		 * @return Il numero di iterazioni eseguite prima della fuga o del raggiungimento del limite massimo.
		 * @throws IllegalArgumentsException se i parametri non sono validi.
		 */
		@Override
		public int calculate(final double real, final double imaginary, final int maxIterations) {
			if (!Double.isFinite(real) || !Double.isFinite(imaginary) 
					|| maxIterations <= 0) {
				throw new IllegalArgumentException("Parametri di calcolo non validi.");
			}

		double zReal = 0.0;
		double zImaginary = 0.0;
		int iterations = 0;

		while (iterations < maxIterations) {
			double nextReal = zReal * zReal - zImaginary * zImaginary + real;
			double nextImaginary = 2.0 * zReal * zImaginary + imaginary;

			zReal = nextReal;
			zImaginary = nextImaginary;
			iterations++;

			if (zReal * zReal + zImaginary * zImaginary > ESCAPE_RADIUS_SQUARED) {
				break;
			}
		}
		return iterations;
	}
}	
