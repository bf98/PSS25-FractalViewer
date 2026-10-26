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

//Creazione della view della GUI, appropriatamente chiamata JFXFractalAppGUI
public final class JFXFractalAppGUI extends Application {
    
    //Definisco i valori di partenza del canvas e quelli minimi della finestra 
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 1100;
    private static final int WINDOW_HEIGHT = 720;
    private static final int WIDTHFIELD_WIDTH = 190;
    private static final int HEIGHTFIELD_WIDTH = 190;
    @Override
    //Imposto lo stage (ovverosia la finestra principale) dell'app e la chiamo primaryStage
    public void start(final Stage primaryStage) {
        //Vado poi a creare il layout della GUI
        //Per iniziare, creo il BorderPane per organizzare i componenti della GUI
        BorderPane borderPane = new BorderPane();
        //All'interno della toolbar vado a creare e ad inserire i pulsanti che utilizzeremo
        //Per la gestione del canvas e del frattale
        Button applyButton = new Button("Applicare la risoluzione");
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        ScrollPane sl = new ScrollPane();
        applyButton.setOnAction(e -> textArea.appendText("Risoluzione non applicabile.\n"));
        Button exportButton = new Button("Esportare il frattale");
        exportButton.setOnAction(e -> textArea.appendText("Non è possibile esportare il frattale.\n"));
        Button resetNavigation = new Button("Resetta la navigazione del frattale alla posizione iniziale");
        resetNavigation.setOnAction(e-> textArea.appendText("Il frattale è gia nella posizione iniziale\n"));
        TextField heightField = new TextField();
        heightField.setPromptText("Inserisci l'altezza del canvas...");
        heightField.setAlignment(Pos.CENTER);
        TextField widthField = new TextField();
        widthField.setPromptText("Inserisci la larghezza del canvas...");
        widthField.setAlignment(Pos.CENTER);
        ObservableList<String> fractals =FXCollections.observableArrayList(
        "Mandelbrot", "Julia"
    );
        ObservableList<String> palettes =FXCollections.observableArrayList(
        "Grayscale", "Rainbow"
    );
        ComboBox<String> fractalType = new ComboBox<String>();
        fractalType.setItems(fractals);
        fractalType.setPromptText("Selezionare il frattale");
        ComboBox<String> paletteType = new ComboBox<String>();
        paletteType.setItems(palettes);
        paletteType.setPromptText("Selezionare la palette");
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
        //Creo la VBox contenenti i parametri per la gestione del frattale e il log
        VBox paramBox = new VBox(configBox, textArea);
        VBox.setVgrow(configBox, Priority.SOMETIMES);
        VBox.setVgrow(textArea, Priority.SOMETIMES);
        //Creo il canvas sul quale verrà applicato il frattale, passandogli i valori
        //Definiti alla creazione dell'App
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