package it.unibo.fractalviewer.mvc.view;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import it.unibo.fractalviewer.mvc.model.*;
import it.unibo.fractalviewer.mvc.controller.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseButton;

/**
 * Implementa la GUI dell'applicazione FractalViewer utilizzando JavaFX.
 */
public final class JFXFractalAppGUI extends Application {
    /** Larghezza iniziale del canvas, in pixel. */
    private static final int BASE_CANVAS_WIDTH = 640;

    /** Altezza iniziale del canvas, in pixel. */
    private static final int BASE_CANVAS_HEIGHT = 480;

    /** Larghezza variabile del canvas, in pixel. */
    private int canvasWidth = BASE_CANVAS_WIDTH;
    
    /** Altezza variabile del canvas, in pixel. */
    private int canvasHeight = BASE_CANVAS_HEIGHT;

    /** Larghezza iniziale della finestra, in pixel. */
    private static final int WINDOW_WIDTH = 1100;

    /** Altezza iniziale della finestra, in pixel. */
    private static final int WINDOW_HEIGHT = 720;

    /** Larghezza massima del campo per la larghezza del canvas, in pixel. */
    private static final int WIDTHFIELD_WIDTH = 190;

    /** Larghezza massima del campo per l'altezza del canvas, in pixel. */
    private static final int HEIGHTFIELD_WIDTH = 190;

		// Fattore di zoom applicato a ogni movimento
		// della rotellina del mouse.
		private static final double ZOOM_FACTOR = 1.2;

		private double lastMouseX;
		private double lastMouseY;

    /**
     * Configura e visualizza la finestra principale dell'applicazione.
     * 
     * @param primaryStage finestra principale dell'applicazione JavaFX.
     */
    @Override
    public void start(final Stage primaryStage) {
				// Inizializza i componenti per la gestione e rendering del frattale.
				FractalModel model = new FractalModel();
				model.setResolution(canvasWidth, canvasHeight);
				FractalRenderer renderer = new FractalRenderer();
				FractalController controller = new FractalController(model, renderer);
        // Crea il layout generale della GUI.
        BorderPane borderPane = new BorderPane();
        // Crea il canvas con le dimensioni iniziali definite nelle costanti.
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
				configureNavigation(canvas, controller);
        // Crea i pulsanti delle operazioni e l'area di testo per il log.
        Button applyButton = new Button("Applicare la risoluzione");
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
				// Crea spinner per numero di iterazioni massime.
				Spinner<Integer> iterationsSpinner = new Spinner<>(100, 1000, controller.getMaxIterations(), 50);
				iterationsSpinner.setEditable(true);
				iterationsSpinner.setPrefWidth(120);
        // Crea il pannello di scorrimento per il canvas.
        ScrollPane sl = new ScrollPane();
        // Assegna il messaggio restituito dai pulsanti e il testo che mostrano.
        Button exportButton = new Button("Esportare il frattale");
        Button resetNavigation = new Button("Resetta la navigazione del frattale alla posizione iniziale");
        resetNavigation.setOnAction(e -> {
					controller.resetNavigation();
					renderFractal(canvas, controller.render());
					textArea.appendText("Posizione iniziale del frattale ripristinata.\n");
				});
        // Crea i campi per il ridimensionamento del canvas.
        TextField heightField = new TextField();
        heightField.setPromptText("Inserisci l'altezza del canvas...");
        heightField.setAlignment(Pos.CENTER);
        TextField widthField = new TextField();
        widthField.setPromptText("Inserisci la larghezza del canvas...");
        widthField.setAlignment(Pos.CENTER);
        applyButton.setOnAction(e -> {
					try {
						// Legge le dimensioni inserite dall'utente.
						int width = Integer.parseInt(widthField.getText().trim());
						int height = Integer.parseInt(heightField.getText().trim());

						// Aggiorna risoluzione e scala nel FractalModel attraverso il metodo 
						// del FractalController.
						controller.resizeVisualization(width, height);
						
						// Aggiorna le dimensioni effettive del canvas.
						canvas.setWidth(width);
						canvas.setHeight(height);

						// Disegna il frattale con la nuova risoluzione.
						renderFractal(canvas, controller.render());

						textArea.appendText(
								"Risoluzione aggiornata: " + width + "x" + height + "\n"
						);
					} catch (NumberFormatException ex) {
						textArea.appendText(
								"Inserire valori interi validi.\n"
						);
					} catch (IllegalArgumentException ex) {
						textArea.appendText(
								"Risoluzione non valida.\n"
						);
					}
				});
        // Crea le opzioni disponibili per il tipo di frattale e la palette.
        ObservableList<String> fractals =FXCollections.observableArrayList
        (
            "Mandelbrot", "Julia"
        );
        ObservableList<ColorPalette> palettes =FXCollections.observableArrayList
        (
            new GrayscalePalette(),
            new RainbowPalette()
        );
        // Crea le ComboBox per la selezione delle opzioni.
        ComboBox<String> fractalType = new ComboBox<String>();
        // Applichiamo la selezione del frattale all'interno della ComboBox.
        fractalType.setItems(fractals);
		fractalType.getSelectionModel().selectedItemProperty().addListener(
			(observable, oldFractal, newFractal) -> {
				if (newFractal != null) {
					switch (newFractal) {
						    case "Mandelbrot":
						        controller.setCalculator(new MandelbrotCalculator());
						        break;
					        case "Julia":
						        controller.setCalculator(new JuliaCalculator());
						        break;
						    default:
						        return;
					    }
					    renderFractal(canvas, controller.render());
				    }
			}
		);
        // Stampa il nome del frattale quando è selezionato mediante un ascoltatore.
        fractalType.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldFractal, newFractal) -> {
                if (newFractal != null)
                {
                    textArea.appendText(
                        "Frattale selezionato " + newFractal + "\n"
                    );
                }
            }
        );
        fractalType.getSelectionModel().selectFirst();
        ComboBox<ColorPalette> paletteType = new ComboBox<ColorPalette>();
        paletteType.setItems(palettes);
        paletteType.setPromptText("Selezionare la palette");
        // Mostra il nome della palette dall'elenco a tendina.
        paletteType.setCellFactory(
            list -> new javafx.scene.control.ListCell<>()
            {
                @Override 
                protected void updateItem(ColorPalette palette, boolean empty)
                {
                    super.updateItem(palette, empty);
                    setText(empty || palette == null ? null : palette.getName());
                }
            }
        );
        // Mostra il nome della palette anche quando è selezionata.
        paletteType.setButtonCell(
            new javafx.scene.control.ListCell<>() 
            {
                @Override 
                protected void updateItem(ColorPalette palette, boolean empty)
                {
                    super.updateItem(palette, empty);
                    setText(empty || palette == null ? null : palette.getName());
                }
            }
        );
        // Ogni volta che è selezionata una palette, viene chiamato il metodo per 
        // ridisegnare il frattale.
        paletteType.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldPalette, newPalette) -> {
                if (newPalette != null )
                {
										controller.setPalette(newPalette);
										renderFractal(canvas, controller.render());
                    textArea.appendText(
                        "Palette selezionata " + newPalette.getName() + "\n"
                    );
                }
            }
        );
        paletteType.getSelectionModel().selectFirst();
        // Crea la toolbar contenente le opzioni di configurazione.
        ToolBar fractalOptions = new ToolBar(
            fractalType,
            paletteType
        );
		Label iterationsLabel = new Label("Iterazioni massime: ");
		VBox iterationsBox = new VBox(5, iterationsLabel, iterationsSpinner);
		Button applyIterationsButton = new Button("Applica iterazioni");
		applyIterationsButton.setOnAction(e -> {
			try {
				iterationsSpinner.increment(0);
				int iterations = iterationsSpinner.getValue();
				controller.setMaxIterations(iterations);
				renderFractal(canvas, controller.render());
				textArea.appendText("Numero massimo di iterazioni aggiornato: " +
					iterations + "\n");
			} catch (IllegalArgumentException ex) {
					textArea.appendText("Numero di iterazioni non valido.\n"); 
			    }
		});
		iterationsBox.getChildren().add(applyIterationsButton);
        iterationsBox.setAlignment(Pos.CENTER);
        widthField.setMaxWidth(WIDTHFIELD_WIDTH);
        heightField.setMaxWidth(HEIGHTFIELD_WIDTH);
        // Crea la VBox contenenti i parametri per la gestione del frattale e il log.
        VBox configBox = new VBox(
            10.0,
            iterationsBox,
            widthField,
            heightField,
            applyButton,
            exportButton,
            resetNavigation,
            textArea
        );
        configBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(configBox, Priority.SOMETIMES);
        VBox.setVgrow(textArea, Priority.SOMETIMES);
        // Configura il canvas e il comportamento dell'exportButton.
        exportButton.setOnAction(e -> handleExport(canvas, textArea, primaryStage));
        sl.setContent(canvas);
        borderPane.setRight(configBox);
        borderPane.setBottom(fractalOptions);
        borderPane.setCenter(sl);     
        Scene scene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("FractalViewer");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(500);
        primaryStage.setResizable(true);
				renderFractal(canvas, controller.render());
        primaryStage.show();
    }

    /**
     * Entry point's class.
     */
    public static final class Main {
        private Main() {
            // the constructor will never be called directly.
        }

        /**
         * Program's entry point.
         *
         * @param args ignored
         */
        public static void main(final String... args) {
            launch(JFXFractalAppGUI.class, args);
            /*
            The following line raises: Error: class it.unibo.fractalviewer.JavaFXApp$Main
            is not a subclass of javafx.application.Application
            Because if you do not provide the Application subclass to launch() it will consider the enclosing class)
            */
            // JavaFXApp.launch(args);
            // Whereas the following would do just fine:
            // JavaFXApp.run(args)
        }
    }

    /**
     * Esegue l'exportButton per esportare il canvas in un immagine formato PNG.
     * @param canvas Canvas contenente il frattale
     * @param textArea Messaggio stampato nella textArea a seconda dell'esito
     * @param primaryStage Finestra della GUI 
     */
    private void handleExport(Canvas canvas, TextArea textArea, Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("fractal.png");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file == null) {
            return;
        }

        // Esporta immagine visualizzata
        WritableImage image = new WritableImage(
            (int) canvas.getWidth(),
            (int) canvas.getHeight()
        );

        canvas.snapshot(null, image);

        try 
        {
            ImageIO.write
            (
                SwingFXUtils.fromFXImage(image, null),
                "png",
                file
            );
            textArea.appendText("Frattale esportato correttamente.\n");
        } 
        
        catch (IOException ex) 
        {
            textArea.appendText("Errore durante l'esportazione: " + ex + "\n");
            ex.printStackTrace();
        }
    }

    /**
     * Renderizza il frattale per applicare la palette selezionata.
     * @param calculator Algoritmo utilizzato per calcolare il frattale.
     * @param canvas Canvas contenente il frattale.
     * @param palette Palette selezionata.
     */
    private void renderFractal(Canvas canvas, final int[] pixels) {
			PixelWriter writer = canvas.getGraphicsContext2D().getPixelWriter();

			int width = (int) canvas.getWidth();
			int height = (int) canvas.getHeight();

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					writer.setArgb(x, y, pixels[y * width + x]);
				}
			}
    }

		/**
		 * Configura gli eventi del mouse
		 * relativi alla navigazione del frattale.
		 *
		 * @param canvas Il canvas della visualizzazione del frattale.
		 * @param controller Il controller della visualizzazione.
		 */
		private void configureNavigation(final Canvas canvas, final FractalController controller) {
			// Salva la posizione iniziale del puntatore.
			canvas.setOnMousePressed(event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					lastMouseX = event.getX();
					lastMouseY = event.getY();
				}
			});
			// Aggiorna la posizione del frattale durante il trascinamento.
			canvas.setOnMouseDragged(event -> {
				if (event.isPrimaryButtonDown()) {
					double deltaX = event.getX() - lastMouseX;
					double deltaY = event.getY() - lastMouseY;

					controller.pan(deltaX, deltaY);

					lastMouseX = event.getX();
					lastMouseY = event.getY();

					renderFractal(canvas, controller.render());
				}
			});
			// Aggiorna lo zoom attraverso la rotellina del mouse.
			canvas.setOnScroll(event -> {
				if (event.getDeltaY() != 0) {
					double factor = event.getDeltaY() > 0 
													? ZOOM_FACTOR
													: 1.0 / ZOOM_FACTOR;
					controller.zoom(factor);

					renderFractal(canvas, controller.render());
					event.consume();
				}
			});
		}
}