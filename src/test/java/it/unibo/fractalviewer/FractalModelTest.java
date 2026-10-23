package it.unibo.fractalviewer.mvc.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FractalModelTest {
	@Test 
	void testInitialResolution() {
		FractalModel model = new FractalModel();

		assertEquals(640, model.getWidth());
		assertEquals(480, model.getHeight());
	}

	@Test
	void testResolutionChange() {
		FractalModel model = new FractalModel();
		model.setResolution(1920, 1080);

		assertEquals(1920, model.getWidth());
		assertEquals(1080, model.getHeight());
	}

	@Test
	void testInvalidResolution() {
		FractalModel model = new FractalModel();
		assertThrows(
				IllegalArgumentException.class,
				() -> model.setResolution(-100, 600)
		);
	}
}
