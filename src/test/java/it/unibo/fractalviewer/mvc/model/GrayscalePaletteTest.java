package it.unibo.fractalviewer.mvc.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test della classe GrayscalePalette.
 *
 * Verifica la conversione del numero di iterazioni
 * in colori ARGB.
 */
class GrayscalePaletteTest {
    private final ColorPalette palette = new GrayscalePalette();

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
     * in un colore ARGB in scala di grigi.
     */
    @Test
    void testColor() {
        // 255 * 50 / 100 equivale a circa 127 (7F in esadecimale).
        assertEquals(0xFF7F7F7F, palette.colorFor(50, 100));
    }

    /**
     * Verifica la colorazione dei punti con un numero di
     * iterazioni vicino agli estremi dell'intervallo.
     */
    @Test
    void testBoundaryIterations() {
        assertEquals(0xFF000000, palette.colorFor(0, 100));
        // 255 * 99 / 100 equivale a circa 252 (FC in esadecimale).
        assertEquals(0xFFFCFCFC, palette.colorFor(99, 100));
    }
}