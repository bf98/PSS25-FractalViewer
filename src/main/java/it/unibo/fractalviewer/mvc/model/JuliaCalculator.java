package it.unibo.fractalviewer.mvc.model;

/**
 * Implementa l'algoritmo di calcolo dell'insieme di Julia.
 *
 * <p>Spiegazione del funzionamento:
 *
 * <p>L'algoritmo utilizza la stessa successione di Mandelbrot, ovverosia
 * z(n+1) = z(n)^2 + c, ma con z(0) equivalente al punto di partenza del piano complesso
 * da analizzare e con c assegnabile.
 *
 * <p>Il calcolo termina quando il modulo al quadrato di z supera il valore 4 oppure
 * quando viene raggiunto il numero massimo di iterazioni consentite.
 */
public final class JuliaCalculator implements FractalCalculator {

		/** Raggio di fuga (al quadrato) utilizzato per rilevare la divergenza. */
		private static final double ESCAPE_RADIUS_SQUARED = 4.0;

        /** Parte reale della costante complessa c. */
        private final double cReal;

        /** Parte immaginaria della costante complessa c. */
        private final double cImaginary;

		/** Parte reale della costante complessa c usata dal costruttore predefinito. */
		private static final double C_REAL_VALUE = -0.7;

		/** Parte immaginaria della costante complessa c usata dal costruttore predefinito. */
		private static final double C_IMAGINARY_VALUE = 0.27015;

		/**
		 * Crea un calcolatore con la costante predefinita.
		 */
		public JuliaCalculator() {
            this(C_REAL_VALUE, C_IMAGINARY_VALUE);
        }

		/**
		 * Crea un calcolatore con la costante c indicata.
		 *
		 * @param cReal La componente reale della costante complessa.
		 * @param cImaginary La componente immaginaria della costante complessa.
		 */
        public JuliaCalculator(final double cReal, final double cImaginary) {
            this.cReal = cReal;
            this.cImaginary = cImaginary;
        }

		/**
		 * Calcola il numero di iterazioni per un punto del piano complesso.
		 *
		 * <p>Spiegazione del funzionamento:
		 *
		 * <p>Il metodo applica la successione di Julia finché il punto
		 * non supera il raggio di fuga oppure viene raggiunto il limite massimo
		 * di iterazioni.
		 *
		 * @param real La componente reale del punto di partenza.
		 * @param imaginary La componente immaginaria del punto di partenza.
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
			final double nextReal = zReal * zReal - zImaginary * zImaginary + cReal;
			final double nextImaginary = 2.0 * zReal * zImaginary + cImaginary;

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

