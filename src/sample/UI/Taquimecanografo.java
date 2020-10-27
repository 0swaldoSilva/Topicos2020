package sample.UI;


import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.file.Path;

public class Taquimecanografo extends Stage implements EventHandler <KeyEvent>{

    //Arreglos para etiquetar los botones del teclado
    private String[] arLblBtn1 = {"°","1","2","3","4","5","6","7","8","9","0","'","¿","     BS    "};
    private String[] arLblBtn2 = {"Tab","Q","W","E","R","T","Y","U","I","O","P","´","+"," }  "};
    private String[] arLblBtn3 = {"MAYUS","A","S","D","F","G","H","J","K","L","Ñ","{","ENTER",};
    private String[] arLblBtn4 = {"    SHIFT    ","Z","X","C","V","B","N","M",",",".","-","                "};
    private String[] arLblBtn5 = {"CTRL","ALT","                              SPACE                              ","ALT GR","<","  "};

    //Elementos para el toolbar.
    private ToolBar tlbMenu;
    private Button btnAbrir;

    //Elementos para la escritura.
    private TextArea txtContenido, txtEscritura;
    private VBox vBox;

    //Elementos para el teclado.
    private HBox[] arrHBoxTeclas = new HBox[5];
    private Button[] arrBtnTeclado1 = new Button[14];
    private Button[] arrBtnTeclado2 = new Button[14];
    private Button[] arrBtnTeclado3 = new Button[13];
    private Button[] arrBtnTeclado4 = new Button[12];
    private Button[] arrBtnTeclado5 = new Button[6];
    private VBox vBoxTeclado;

    //Elementos de agrupación global.
    private VBox vBoxPrincipal;
    private Scene escena;

    //Elementos para leer el archivo.
    private File file;
    private BufferedReader br;

    //Detectar errores, no palabras...
    private int contPalabras = 0, contWords = 0, contError = 0, numChar = 0, cont = 0;
    private Label lblPalabras, lblcontWords, lblError;
    private File tempFile;
    private BufferedWriter bw;
    private StreamTokenizer st1, st2;

    //Contador de tiempo.
    private HBox hBoxInferior;
    private Label lblContador;
    private String texto;
    private int segundero, minutero;

    private Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Platform.runLater(new Runnable() {
                public void run() {
                    segundero++;

                    if (segundero == 60){
                        minutero++;
                        segundero = 0;
                    }

                    actuLabel();
                }
            });
        }
    });

    public Taquimecanografo(){

        CrearUI();
        this.setTitle("Tutor de Taquimecanografía");
        this.setScene(escena);
        this.show();

    }

    private void CrearUI() {

        CrearToolbar();
        CrearEscritura();
        CrearTeclado();

        vBoxPrincipal = new VBox();
        vBoxPrincipal.getChildren().addAll(tlbMenu, txtContenido, txtEscritura, hBoxInferior);
        vBoxPrincipal.setSpacing(10);
        vBoxPrincipal.setPadding(new Insets(10));
        escena = new Scene(vBoxPrincipal,800,500);
        escena.getStylesheets().add("sample/css/taqui_styles.css");

    }

    private void CrearToolbar() {

        //Crear barra de herramientas.
        tlbMenu = new ToolBar();
        btnAbrir = new Button();
        btnAbrir.setOnAction(event -> eventoTaqui(1));
        btnAbrir.setPrefSize(35,35);

        //Asignamos la imagen  al boton dentro del toolbar.
        Image img = new Image("sample/assets/OpenFile.png");
        ImageView imv = new ImageView(img);
        imv.setFitHeight(30);
        imv.setPreserveRatio(true);
        btnAbrir.setGraphic(imv);

        tlbMenu.getItems().addAll(btnAbrir);

    }

    private void eventoTaqui(int opc) {

        String read = "";
        switch (opc){
            case 1:
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Abrir archivo");
                    file = fileChooser.showOpenDialog(this);

                    int cant = file.toString().length();

                    if (file.toString().charAt(cant - 1) != 't' || file.toString().charAt(cant - 2) != 'x' || file.toString().charAt(cant - 3) != 't') {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error de entrada/salida");
                        alert.setHeaderText("Parece que intentaste abrir un archivo erroneo");
                        alert.setContentText("Por favor elige un archivo de tipo texto (.txt)");
                        alert.showAndWait();
                    } else {
                        br = new BufferedReader(new FileReader(file));
                        String valor = br.readLine();

                        while (valor != null) {
                            read += valor + "\n";
                            valor = br.readLine();
                        }

                        txtContenido.setText(read);
                        txtEscritura.setEditable(true);
                        txtEscritura.setText("");
                        contWords = 0; contError = 0; numChar = 0;
                        numPalabrasTXT();

                        if (timer.isRunning()) {
                            timer.stop();
                        }

                        segundero = 0; minutero = 0;
                        actuLabel();
                        timer.start();
                    }
                }catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error de entrada/salida");
                    alert.setHeaderText("Hubo un problema con la elección del archivo");
                    alert.setContentText("Por favor vuelva a intentarlo.");
                    alert.showAndWait();
                }
                break;
        }
    }

    private void CrearEscritura() {

        txtContenido = new TextArea();
        txtContenido.setEditable(false);
        txtContenido.setText("Elige un archivo de texto...");
        txtContenido.setPrefRowCount(6);

        txtEscritura = new TextArea();
        txtEscritura.setEditable(false);
        txtEscritura.setPrefRowCount(6);
        txtEscritura.setOnKeyPressed(this::handle);
        txtEscritura.setOnKeyReleased(this::released);

        crearArchivo();

    }

    private void CrearTeclado() {

        hBoxInferior = new HBox();
        hBoxInferior.setSpacing(50);

        vBox = new VBox();
        vBox.setSpacing(3);

        vBoxTeclado = new VBox();
        vBoxTeclado.setSpacing(8);

        lblContador = new Label("00:00");
        lblContador.setId("lblContador");
        lblPalabras = new Label("Número de palabras en el archivo: " + contPalabras);
        lblcontWords = new Label("Número de palabras escritas: " + contWords);
        lblError = new Label("Número de errores: " + contError);
        lblError.setId("lblError");

        for(int i = 0; i < arrHBoxTeclas.length; i++){
            arrHBoxTeclas[i] = new HBox();
            arrHBoxTeclas[i].setSpacing(10);
        }

        for(int i = 0; i < arrBtnTeclado1.length; i++){
            arrBtnTeclado1[i] = new Button(arLblBtn1[i]);
            arrBtnTeclado1[i].setStyle("-fx-background-color: #B9FF8B;");
            arrBtnTeclado2[i] = new Button(arLblBtn2[i]);
            arrBtnTeclado2[i].setStyle("-fx-background-color: #B9FF8B;");
            if (i<13){
                arrBtnTeclado3[i] = new Button(arLblBtn3[i]);
                arrBtnTeclado3[i].setStyle("-fx-background-color: #B9FF8B;");

                arrHBoxTeclas[2].getChildren().add(arrBtnTeclado3[i]);

            }
            if(i<12){
                arrBtnTeclado4[i] = new Button(arLblBtn4[i]);
                arrBtnTeclado4[i].setStyle("-fx-background-color: #B9FF8B;");

                arrHBoxTeclas[3].getChildren().add(arrBtnTeclado4[i]);
            }
            if (i<6){
                arrBtnTeclado5[i] = new Button(arLblBtn5[i]);
                arrBtnTeclado5[i].setStyle("-fx-background-color: #B9FF8B;");

                arrHBoxTeclas[4].getChildren().add(arrBtnTeclado5[i]);
            }
            arrHBoxTeclas[0].getChildren().add(arrBtnTeclado1[i]);
            arrHBoxTeclas[1].getChildren().add(arrBtnTeclado2[i]);
        }

        vBoxTeclado.getChildren().addAll(arrHBoxTeclas[0], arrHBoxTeclas[1], arrHBoxTeclas[2], arrHBoxTeclas[3], arrHBoxTeclas[4]);

        vBox.getChildren().addAll(lblContador, lblPalabras, lblcontWords, lblError);

        hBoxInferior.getChildren().addAll(vBoxTeclado,vBox);

    }

    @Override
    public void handle (KeyEvent event) {

        //Encender teclas del teclado.
        switch (event.getCode().toString()){
            case "UNDEFINED":
                if(event.getText().equals("|") || event.getText().equals("°")) {
                    arrBtnTeclado1[0].setStyle("-fx-background-color: #FE9618;");
                }
                else{
                    if(event.getText().equals("¿")) {
                        arrBtnTeclado1[12].setStyle("-fx-background-color: #FE9618;");
                    }
                    else{
                        if (event.getText().toUpperCase().equals("Ñ")){
                            arrBtnTeclado3[10].setStyle("-fx-background-color: #FE9618;");
                        }
                    }
                }
                break;
            case "DIGIT1":
                arrBtnTeclado1[1].setStyle("-fx-background-color: #FE9618;");
                break;
            case "DIGIT2":
                arrBtnTeclado1[2].setStyle("-fx-background-color: #FE9618;");
                break;
            case "DIGIT3":
                arrBtnTeclado1[3].setStyle("-fx-background-color: #FE9618;");
                break;
            case "DIGIT4":
                arrBtnTeclado1[4].setStyle("-fx-background-color: #FE9618;");
                break;
            case "DIGIT5":
                arrBtnTeclado1[5].setStyle("-fx-background-color: #FE9618;");
                break;
            case "DIGIT6":
                arrBtnTeclado1[6].setStyle("-fx-background-color: #FE9618;");
                break;
            case "DIGIT7":
                arrBtnTeclado1[7].setStyle("-fx-background-color: #FE9618;");
                break;
            case "DIGIT8":
                arrBtnTeclado1[8].setStyle("-fx-background-color: #FE9618;");
                break;
            case "DIGIT9":
                arrBtnTeclado1[9].setStyle("-fx-background-color: #FE9618;");
                break;
            case "DIGIT0":
                arrBtnTeclado1[10].setStyle("-fx-background-color: #FE9618;");
                break;
            case "QUOTE":
                arrBtnTeclado1[11].setStyle("-fx-background-color: #FE9618;");
                break;
            case "BACK_SPACE":
                arrBtnTeclado1[13].setStyle("-fx-background-color: #FE9618;");
                break;
            case "TAB":
                if (numChar > 0) {
                    arrBtnTeclado2[0].setStyle("-fx-background-color: #FE9618;");
                }
                break;
            case "Q":
                arrBtnTeclado2[1].setStyle("-fx-background-color: #FE9618;");
                break;
            case "W":
                arrBtnTeclado2[2].setStyle("-fx-background-color: #FE9618;");
                break;
            case "E":
                arrBtnTeclado2[3].setStyle("-fx-background-color: #FE9618;");
                break;
            case "R":
                arrBtnTeclado2[4].setStyle("-fx-background-color: #FE9618;");
                break;
            case "T":
                arrBtnTeclado2[5].setStyle("-fx-background-color: #FE9618;");
                break;
            case "Y":
                arrBtnTeclado2[6].setStyle("-fx-background-color: #FE9618;");
                break;
            case "U":
                arrBtnTeclado2[7].setStyle("-fx-background-color: #FE9618;");
                break;
            case "I":
                arrBtnTeclado2[8].setStyle("-fx-background-color: #FE9618;");
                break;
            case "O":
                arrBtnTeclado2[9].setStyle("-fx-background-color: #FE9618;");
                break;
            case "P":
                arrBtnTeclado2[10].setStyle("-fx-background-color: #FE9618;");
                break;
            case "PLUS":
                arrBtnTeclado2[12].setStyle("-fx-background-color: #FE9618;");
                break;
            case "BRACERIGHT":
                arrBtnTeclado2[13].setStyle("-fx-background-color: #FE9618;");
                break;
            case "CAPS":
                arrBtnTeclado3[0].setStyle("-fx-background-color: #FE9618;");
                break;
            case "A":
                arrBtnTeclado3[1].setStyle("-fx-background-color: #FE9618;");
                break;
            case "S":
                arrBtnTeclado3[2].setStyle("-fx-background-color: #FE9618;");
                break;
            case "D":
                arrBtnTeclado3[3].setStyle("-fx-background-color: #FE9618;");
                break;
            case "F":
                arrBtnTeclado3[4].setStyle("-fx-background-color: #FE9618;");
                break;
            case "G":
                arrBtnTeclado3[5].setStyle("-fx-background-color: #FE9618;");
                break;
            case "H":
                arrBtnTeclado3[6].setStyle("-fx-background-color: #FE9618;");
                break;
            case "J":
                arrBtnTeclado3[7].setStyle("-fx-background-color: #FE9618;");
                break;
            case "K":
                arrBtnTeclado3[8].setStyle("-fx-background-color: #FE9618;");
                break;
            case "L":
                arrBtnTeclado3[9].setStyle("-fx-background-color: #FE9618;");
                break;
            case "BRACELEFT":
                arrBtnTeclado3[11].setStyle("-fx-background-color: #FE9618;");
                break;
            case "ENTER":
                arrBtnTeclado3[12].setStyle("-fx-background-color: #FE9618;");
                break;
            case "SHIFT":
                if(event.isShiftDown())
                    arrBtnTeclado4[0].setStyle("-fx-background-color: #FE9618;");
                break;
            case "Z":
                arrBtnTeclado4[1].setStyle("-fx-background-color: #FE9618;");
                break;
            case "X":
                arrBtnTeclado4[2].setStyle("-fx-background-color: #FE9618;");
                break;
            case "C":
                arrBtnTeclado4[3].setStyle("-fx-background-color: #FE9618;");
                break;
            case "V":
                arrBtnTeclado4[4].setStyle("-fx-background-color: #FE9618;");
                break;
            case "B":
                arrBtnTeclado4[5].setStyle("-fx-background-color: #FE9618;");
                break;
            case "N":
                arrBtnTeclado4[6].setStyle("-fx-background-color: #FE9618;");
                break;
            case "M":
                arrBtnTeclado4[7].setStyle("-fx-background-color: #FE9618;");
                break;
            case "COMMA":
                arrBtnTeclado4[8].setStyle("-fx-background-color: #FE9618;");
                break;
            case "PERIOD":
                arrBtnTeclado4[9].setStyle("-fx-background-color: #FE9618;");
                break;
            case "MINUS":
                arrBtnTeclado4[10].setStyle("-fx-background-color: #FE9618;");
                break;
            case "CONTROL":
                if(event.isControlDown())
                    arrBtnTeclado5[0].setStyle("-fx-background-color: #FE9618;");
                break;
            case "ALT":
                if(event.isAltDown()){
                    arrBtnTeclado5[1].setStyle("-fx-background-color: #FE9618;");
                }
                break;
            case "SPACE":
                arrBtnTeclado5[2].setStyle("-fx-background-color: #FE9618;");
                break;
            case "ALT_GRAPH":
                if(event.isAltDown() && event.isControlDown())
                    arrBtnTeclado5[3].setStyle("-fx-background-color: #FE9618;");
                break;
            case "LESS":
                arrBtnTeclado5[4].setStyle("-fx-background-color: #FE9618;");
                break;
        }

        numWordWrite();

    }

    private void released(KeyEvent event){

        //Apagar teclas del teclado
        switch (event.getCode().toString()){
            case "UNDEFINED":
                if(event.getText().equals("|") || event.getText().equals("°")) {
                    arrBtnTeclado1[0].setStyle("-fx-background-color: #B9FF8B;");
                }
                else{
                    if(event.getText().equals("¿")) {
                        arrBtnTeclado1[12].setStyle("-fx-background-color: #B9FF8B;");
                    }
                    else{
                        if (event.getText().toUpperCase().equals("Ñ")){
                            arrBtnTeclado3[10].setStyle("-fx-background-color: #B9FF8B;");
                        }
                    }
                }
                break;
            case "DIGIT1":
                arrBtnTeclado1[1].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "DIGIT2":
                arrBtnTeclado1[2].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "DIGIT3":
                arrBtnTeclado1[3].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "DIGIT4":
                arrBtnTeclado1[4].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "DIGIT5":
                arrBtnTeclado1[5].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "DIGIT6":
                arrBtnTeclado1[6].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "DIGIT7":
                arrBtnTeclado1[7].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "DIGIT8":
                arrBtnTeclado1[8].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "DIGIT9":
                arrBtnTeclado1[9].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "DIGIT0":
                arrBtnTeclado1[10].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "QUOTE":
                arrBtnTeclado1[11].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "BACK_SPACE":
                arrBtnTeclado1[13].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "TAB":
                arrBtnTeclado2[0].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "Q":
                arrBtnTeclado2[1].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "W":
                arrBtnTeclado2[2].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "E":
                arrBtnTeclado2[3].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "R":
                arrBtnTeclado2[4].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "T":
                arrBtnTeclado2[5].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "Y":
                arrBtnTeclado2[6].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "U":
                arrBtnTeclado2[7].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "I":
                arrBtnTeclado2[8].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "O":
                arrBtnTeclado2[9].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "P":
                arrBtnTeclado2[10].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "PLUS":
                arrBtnTeclado2[12].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "BRACERIGHT":
                arrBtnTeclado2[13].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "CAPS":
                arrBtnTeclado3[0].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "A":
                arrBtnTeclado3[1].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "S":
                arrBtnTeclado3[2].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "D":
                arrBtnTeclado3[3].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "F":
                arrBtnTeclado3[4].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "G":
                arrBtnTeclado3[5].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "H":
                arrBtnTeclado3[6].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "J":
                arrBtnTeclado3[7].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "K":
                arrBtnTeclado3[8].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "L":
                arrBtnTeclado3[9].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "BRACELEFT":
                arrBtnTeclado3[11].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "ENTER":
                arrBtnTeclado3[12].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "SHIFT":
                arrBtnTeclado4[0].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "Z":
                arrBtnTeclado4[1].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "X":
                arrBtnTeclado4[2].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "C":
                arrBtnTeclado4[3].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "V":
                arrBtnTeclado4[4].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "B":
                arrBtnTeclado4[5].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "N":
                arrBtnTeclado4[6].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "M":
                arrBtnTeclado4[7].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "COMMA":
                arrBtnTeclado4[8].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "PERIOD":
                arrBtnTeclado4[9].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "MINUS":
                arrBtnTeclado4[10].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "CONTROL":
                arrBtnTeclado5[0].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "ALT":
                arrBtnTeclado5[1].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "SPACE":
                arrBtnTeclado5[2].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "ALT_GRAPH":
                arrBtnTeclado5[3].setStyle("-fx-background-color: #B9FF8B;");
                break;
            case "LESS":
                arrBtnTeclado5[4].setStyle("-fx-background-color: #B9FF8B;");
                break;
        }

        numWordWrite();

        txtEscritura.setTextFormatter(new TextFormatter<String>(
            change -> change.getControlNewText().length() < txtContenido.getText().length() ? change : null));

        String code = event.getCode().toString();

        if(code != "SHIFT" && code != "CONTROL" && code !="ALT" && code != "ALT_GRAPH"
                && code != "CAPS" && code != "BACK_SPACE" && code != "LEFT" && code != "RIGHT"
                && code != "UP" && code != "DOWN"  && code != "DELETE" && code != "HOME"
                && code != "END" && code != "PAGE_UP" && code != "PAGE_DOWN" && code != "PRINTSCREEN"
                && code != "NUM_LOCK" && code != "WINDOWS" && code != "ESCAPE" && contWords >= 1) {
            try {
                if (txtEscritura.getText().charAt(numChar) != txtContenido.getText().charAt(numChar)) {
                    contError++;
                    lblError.setText("Número de errores: " + contError);
                }
                if(numChar >= 0 && numChar < txtContenido.getText().length()) {
                    numChar++;
                }
            }catch (Exception e){}
            if(txtEscritura.getText().length() == (txtContenido.getText().length() - 1)){
                for (int i = 0; i < (txtContenido.getText().length() - 1); i++){
                    if(txtEscritura.getText().charAt(i) != txtContenido.getText().charAt(i)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Tienes un error");
                        alert.setHeaderText("Revisa el texto, debe haber algún error.");
                        alert.showAndWait();
                        cont = 0;
                        break;
                    }
                    else{
                        cont++;
                        if(cont == (txtContenido.getText().length() - 1)){
                            timer.stop();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Práctica concluida");
                            alert.setHeaderText("Has terminado la práctica, felicidades.");
                            alert.setContentText("Total de errores: "+ contError +
                                    "\nHas concluido en un tiempo igual a: " + (minutero<10?"0":"")+minutero +
                                    ":"+(segundero<10?"0":"")+segundero);
                            alert.showAndWait();
                            txtContenido.setText("Elige un archivo de texto...");
                            txtEscritura.setEditable(false);
                            txtEscritura.setText("");
                            cont = 0; contWords = 0; contPalabras = 0; contError = 0; numChar = 0;
                            segundero = 0; minutero = 0;
                            actuLabel();
                            break;
                        }
                    }
                }
            }
        }
        else{
            if(numChar > 0) {
                if (code == "BACK_SPACE" && numChar >= 1) {
                    contError++;
                    lblError.setText("Número de errores: " + contError);
                }
                numChar--;
            }
        }

    }

    private  void numWordWrite(){
        contWords = 0;
        try {
            bw = new BufferedWriter(new FileWriter(tempFile));
            bw.write(txtEscritura.getText());
            bw.close();

            st1 = new StreamTokenizer(new FileReader(tempFile));
            while (st1.nextToken() != StreamTokenizer.TT_EOF) {
                if (st1.ttype == StreamTokenizer.TT_WORD || st1.ttype == StreamTokenizer.TT_NUMBER) {
                    contWords++;
                }
            }
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de entrada/salida");
            alert.setHeaderText("Hubo un problema :c");
            alert.setContentText("Por favor vuelva a intentarlo");
            alert.showAndWait();
        }

        lblcontWords.setText("Número de palabras escritas: " + contWords);
    }

    private void numPalabrasTXT() throws IOException {
        contPalabras = 0;
        st2 = new StreamTokenizer(new FileReader(file));
        while(st2.nextToken() != StreamTokenizer.TT_EOF){
            if (st2.ttype == StreamTokenizer.TT_WORD || st2.ttype == StreamTokenizer.TT_NUMBER){
                contPalabras++;
            }
        }
    }

    private void actuLabel(){
        texto = (minutero<10?"0":"")+minutero+":"+(segundero<10?"0":"")+segundero;
        lblContador.setText(texto);


        lblcontWords.setText("Número de palabras escritas: " + contWords);
        lblError.setText("Número de errores: " + contError);
        lblPalabras.setText("Número de palabras en el archivo: " + contPalabras);
    }

    private void crearArchivo(){
        try {
            tempFile = File.createTempFile("archivoTemp", ".txt");
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de entrada/salida");
            alert.setHeaderText("Hubo un problema :c");
            alert.setContentText("Por favor vuelva a intentarlo");
            alert.showAndWait();
        }
    }

}
