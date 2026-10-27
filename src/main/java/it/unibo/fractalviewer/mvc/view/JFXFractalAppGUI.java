package it.unibo.fractalviewer.mvc.view;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Implementa la GUI dell'applicazione FractalViewer utilizzando JavaFX.
 */
public final class JFXFractalAppGUI extends Application {
    /** Larghezza iniziale del canvas, in pixel. */
    private static final int CANVAS_WIDTH = 800;

    /** Altezza iniziale del canvas, in pixel. */
    private static final int CANVAS_HEIGHT = 600;

    /** Larghezza iniziale della finestra, in pixel. */
    private static final int WINDOW_WIDTH = 1100;

    /** Altezza iniziale della finestra, in pixel. */
    private static final int WINDOW_HEIGHT = 720;

    /** Larghezza massima del campo per la larghezza del canvas, in pixel. */
    private static final int WIDTHFIELD_WIDTH = 190;

    /** Larghezza massima del campo per l'altezza del canvas, in pixel. */
    private static final int HEIGHTFIELD_WIDTH = 190;

    /**
     * Configura e visualizza la finestra principale dell'applicazione.
     * 
     * @param primaryStage finestra principale dell'applicazione JavaFX.
     */
    @Override
    public void start(final Stage primaryStage) {
        // Crea il layout generale della GUI.
        BorderPane borderPane = new BorderPane();
        // Crea i pulsanti delle operazioni e l'area di testo per il log.
        Button applyButton = new Button("Applicare la risoluzione");
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        // Crea il pannello di scorrimento per il canvas.
        ScrollPane sl = new ScrollPane();
        // Assegna il messaggio restituito dai pulsanti e il testo che mostrano.
        applyButton.setOnAction(e -> textArea.appendText("Risoluzione non applicabile.\n"));
        Button exportButton = new Button("Esportare il frattale");
        exportButton.setOnAction(e -> textArea.appendText("Non è possibile esportare il frattale.\n"));
        Button resetNavigation = new Button("Resetta la navigazione del frattale alla posizione iniziale");
        resetNavigation.setOnAction(e-> textArea.appendText("Il frattale è gia nella posizione iniziale\n"));
        // Crea i campi per il ridimensionamento del canvas.
        TextField heightField = new TextField();
        heightField.setPromptText("Inserisci l'altezza del canvas...");
        heightField.setAlignment(Pos.CENTER);
        TextField widthField = new TextField();
        widthField.setPromptText("Inserisci la larghezza del canvas...");
        widthField.setAlignment(Pos.CENTER);
        // Crea le opzioni disponibili per il tipo di frattale e la palette.
        ObservableList<String> fractals =FXCollections.observableArrayList(
        "Mandelbrot", "Julia"
    );
        ObservableList<String> palettes =FXCollections.observableArrayList(
        "Grayscale", "Rainbow"
    );
        // Crea le ComboBox per la selezione delle opzioni.
        ComboBox<String> fractalType = new ComboBox<String>();
        fractalType.setItems(fractals);
        fractalType.setPromptText("Selezionare il frattale");
        ComboBox<String> paletteType = new ComboBox<String>();
        paletteType.setItems(palettes);
        paletteType.setPromptText("Selezionare la palette");
        // Crea la toolbar contenente le opzioni di configurazione.
        ToolBar fractalOptions = new ToolBar(
            fractalType,
            paletteType
        );
        VBox configBox = new VBox(
            20.0,
            applyButton,
            exportButton,
            resetNavigation,
            widthField,
            heightField
        );
        widthField.setMaxWidth(WIDTHFIELD_WIDTH);
        heightField.setMaxWidth(HEIGHTFIELD_WIDTH);
        configBox.setAlignment(Pos.CENTER);
        // Crea la VBox contenenti i parametri per la gestione del frattale e il log.
        VBox paramBox = new VBox(configBox, textArea);
        VBox.setVgrow(configBox, Priority.SOMETIMES);
        VBox.setVgrow(textArea, Priority.SOMETIMES);
        // Crea il canvas con le dimensioni iniziali definite nelle costanti.
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        sl.setContent(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, CANVAS_WIDTH,CANVAS_HEIGHT);
        borderPane.setRight(paramBox);
        borderPane.setBottom(fractalOptions);
        borderPane.setCenter(sl);     
        Scene scene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("FractalViewer");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(500);
        primaryStage.setResizable(true);
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
}