package it.unibo.fractalviewer.mvc;

import javafx.application.Application;
import it.unibo.fractalviewer.mvc.view.JFXFractalAppGUI;

/**
 * This application is a porting of the Swing MVC application from OOP course at UNIBO.
 */


public final class FractalViewerApp {

	 private FractalViewerApp() {

	 }

	 /**
		* @param args
		*            ignored
		*/
	 public static void main(final String... args) {
			 Application.launch(JFXFractalAppGUI.class);
	 }
}
