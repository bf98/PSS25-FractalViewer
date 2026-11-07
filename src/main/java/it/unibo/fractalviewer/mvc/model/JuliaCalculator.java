package it.unibo.fractalviewer.mvc.model;

public final class JuliaCalculator implements FractalCalculator{

/**
 * Implementa l'algoritmo di calcolo dell'insieme di Julia.
 *
 * Spiegazione del funzionamento:
 * L'algoritmo utilizza lo stesso di Mandelbrot, ovverosia
 * la successione z(n+1) = z(n)^2 + c, ma con z(0) equivalente al punto del piano complesso 
 * da analizzare e con c fisso.
 * Il calcolo termina quando il modulo al quadrato di z supera il valore 4 oppure
 * quando viene raggiunto il numero massimo di iterazioni consentite.
 */

		/** Raggio di fuga (al quadrato) utilizzato per rilevare la divergenza. */
		private  static final double ESCAPE_RADIUS_SQUARED = 4.0;

        /** Componenti della costante complessa c. */
                
        private final double cReal;
        private final double cImaginary;

        /** Inserisco poi i costruttori per inizializzare le due variabili. */
        public JuliaCalculator () {
            this(-0.7, 0.27015);
        }

        public JuliaCalculator(double cReal, double cImaginary)
        {
            this.cReal = cReal;
            this.cImaginary = cImaginary;
        }

		/**
		 * Calcola il numero di iterazioni per un punto del piano complesso.
		 * 
		 * Spiegazione del funzionamento:
		 * Il metodo applica la successione di Julia finché il punto
		 * non supera il raggio di fuga oppure viene raggiunto il limite massimo
		 * di iterazioni.
		 *
		 * @param real La componente reale del punto complesso.
		 * @param imaginary La componente immaginaria del punto complesso.
		 * @param maxIterations Il numero massimo di iterazioni consentite.
		 * @return Il numero di iterazioni eseguite prima della fuga o del raggiungimento del limite massimo.
		 * @throws IllegalArgumentException se i parametri non sono validi.
		 */
		@Override
		public int calculate(final double real, final double imaginary, final int maxIterations) {
			if (!Double.isFinite(real) || !Double.isFinite(imaginary) 
					|| maxIterations <= 0) {
				throw new IllegalArgumentException("Parametri di calcolo non validi.");
			}

		double zReal = real;
		double zImaginary = imaginary;
		int iterations = 0;

		while (iterations < maxIterations) {
			double nextReal = zReal * zReal - zImaginary * zImaginary + cReal;
			double nextImaginary = 2.0 * zReal * zImaginary + cImaginary;

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

