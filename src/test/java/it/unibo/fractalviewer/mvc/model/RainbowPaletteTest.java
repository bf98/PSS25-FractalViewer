package it.unibo.fractalviewer.mvc.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test della classe RainbowPalette.
 *
 * Verifica la conversione del numero di iterazioni
 * in colori ARGB.
 */
class RainbowPaletteTest {
    private final ColorPalette palette = new RainbowPalette();

    /**
     * Verifica che i punti che raggiungono o superano il 
     * limite massimo di iterazioni siano neri.
     */
    @Test
    void testInternalPointColor() {
        assertEquals(0xFF000000, palette.colorFor(100, 100));
        assertEquals(0xFF000000, palette.colorFor(150, 100));
    }

    /**
     * Verifica la corretta conversione delle iterazioni
     * in un colore ARGB.
     */
    @Test
    void testColor() {
        // 50 / 100 è 0.5, confrontare valore ARGB con HSBtoRGB(0.5f, 0.8f, 1.0f)
        assertEquals(0xFF33FFFF, palette.colorFor(50, 100));
    }

    /**
     * Verifica la colorazione dei punti con un numero di
     * iterazioni vicino agli estremi dell'intervallo.
     */
    @Test
    void testBoundaryIterations() {
        // Come in testColor, confrontare valore ARGB con HSBtoRGB(0.0f, 0.8f, 1.0f)
        assertEquals(0xFFFF3333, palette.colorFor(0, 100));
        assertNotEquals(0xFF000000, palette.colorFor(99, 100));
    }
}