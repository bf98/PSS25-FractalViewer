
package it.unibo.fractalviewer.mvc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JuliaCalculatorTest {

    private final FractalCalculator calculator = new JuliaCalculator(0.0, 0.0);

    /**
     * Verifica che l'origine non superi il raggio di fuga.
     */
    @Test
    void testOrigin() {
        assertEquals(100, calculator.calculate(0.0, 0.0, 100));
    }

    /**
     * Verifica che un punto interno all'insieme
     * raggiunga il numero massimo di iterazioni.
     */
    @Test
    void testInternalPoint() {
        assertEquals(100, calculator.calculate(-1.0, 0.0, 100));
    }

    /**
     * Verifica che un punto esterno all'insieme
     * superi il raggio di fuga alla prima iterazione.
     */
    @Test
    void testExternalPoint() {
        assertEquals(1, calculator.calculate(3.0, 3.0, 100));
    }

    /**
     * Verifica che il calcolo termini quando viene
     * raggiunto il numero massimo di iterazioni.
     */
    @Test
    void testMaxIterations() {
        assertEquals(5, calculator.calculate(0.0, 0.0, 5));
    }

    /**
     * Verifica che un numero massimo di iterazioni
     * non positivo venga rifiutato.
     */
    @Test
    void testInvalidIterations() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(0.0, 0.0, 0));
    }

    /**
     * Verifica che coordinate non finite (come NaN e infinito)
     * generino un'eccezione.
     */
    @Test
    void testInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(Double.NaN, 0.0, 100));

        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(0.0, Double.POSITIVE_INFINITY, 100));
    }
}
