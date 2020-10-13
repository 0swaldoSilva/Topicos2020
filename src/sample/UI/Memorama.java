package sample.UI;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

import static java.lang.Thread.sleep;

public class Memorama extends Stage implements EventHandler{

    //Redimensionar la ventana Stage.
    private Stage stage = new Stage();

    private String[] arImagenes = {"1.png","2.png","3.png","4.png","5.png","6.png","7.png","8.png","9.png","10.png"};

    private Label lblTarjetas;
    private TextField txtNoTarjetas;
    private Button btnAceptar, btnAceptar2;
    private HBox hBox;
    private VBox vBox;
    private GridPane gdpMesa;
    private Button[][] arTarjetas;
    private String[][] arAsignacion;

    private int noPares;

    //Imagenes aleatorias en el memorama
    private Integer[] arrRandom;

    //Elementos para la comparación
    private int click, i1, i2, j1, j2;
    private String card1 = "", card2 = "";

    //intentos
    private Label lblIntentos;
    private int contador;

    //click en boton de una pareja encontrada
    private int[][] arrVerificarClcik;

    //Control de click en la misma imagen.
    private int[][] arrControlClick;

    //Detectar que finalizo el juego.
    private int paresEncontrados = 0;

    //Redimensionar
    private int posicionI, posicionJ;

    private Scene escena;

    public Memorama(){

        CrearUI();
        stage.setTitle("Memorama :D");
        stage.setScene(escena);
        stage.centerOnScreen();
        stage.show();

    }

    private void CrearUI() {

        lblTarjetas = new Label("Número de pares:");
        txtNoTarjetas = new TextField();
        btnAceptar = new Button("Comenzar");
        lblIntentos = new Label();

        //Forma número 1 (Intermedio)
        btnAceptar.addEventHandler(MouseEvent.MOUSE_CLICKED, this);

        /*Forma número 2 (Mi preferida)
        btnAceptar.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventosMemorama(1));
        btnAceptar2.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventosMemorama(2));

        Forma número 3 (Menos recomenable)
        btnAceptar.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Mi tercer evento desde una clase anónima");
            }
        });*/

        hBox = new HBox();
        hBox.getChildren().addAll(lblTarjetas, txtNoTarjetas, btnAceptar, lblIntentos);
        hBox.setSpacing(10);

        gdpMesa = new GridPane();
        vBox = new VBox();
        vBox.getChildren().addAll(hBox, gdpMesa);
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(15));

        escena = new Scene(vBox, 475,390);

    }

    @Override
    public void handle(Event event) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        Image img1 = new Image("sample/assets/Error.png");
        ImageView imv1 = new ImageView(img1);
        imv1.setFitHeight(300);
        imv1.setPreserveRatio(true);

        try {
            noPares = Integer.parseInt(txtNoTarjetas.getText());


            if (noPares > 1 && noPares < 11) {
                btnAceptar.setText("Reiniciar");

                vBox.getChildren().remove(gdpMesa);

                gdpMesa = new GridPane();

                if (noPares <= 5 || noPares == 7) {
                    posicionI = 2;
                    posicionJ = noPares;
                }
                else{
                    if (noPares == 6 || noPares == 9) {
                        posicionI = 3;
                        posicionJ = noPares / 3 * 2;
                    }
                    else{
                        if (noPares == 8 || noPares == 10) {
                            posicionI = 4;
                            posicionJ = noPares / 2;;
                        }
                    }
                }

                arAsignacion = new String[posicionI][posicionJ];
                arTarjetas = new Button[posicionI][posicionJ];
                arrVerificarClcik = new int[posicionI][posicionJ];
                arrControlClick = new int[posicionI][posicionJ];

                for (int i = 0; i < posicionI; i++) {
                    for (int j = 0; j < posicionJ; j++) {

                        arrVerificarClcik[i][j] = 0;
                        arrControlClick[i][j] = 0;

                        Image img = new Image("sample/assets/Incognito.png");
                        ImageView imv = new ImageView(img);
                        imv.setFitWidth(90);
                        imv.setPreserveRatio(true);

                        arTarjetas[i][j] = new Button();
                        int finalI = i;
                        int finalJ = j;
                        arTarjetas[i][j].setGraphic(imv);
                        arTarjetas[i][j].setOnAction(event1 -> verTarjeta(finalI, finalJ));
                        arTarjetas[i][j].addEventHandler(MouseEvent.MOUSE_EXITED, this::verificarParejas);

                        gdpMesa.add(arTarjetas[i][j], j, i);
                    }
                }

                //Redimencionar la ventana automaticamente.
                switch (noPares){
                    case 2:
                    case 4:
                    case 3:
                        stage.setHeight(390);
                        stage.setWidth(475);
                        stage.centerOnScreen();
                        break;
                    case 5:
                        stage.setHeight(390);
                        stage.setWidth(580);
                        stage.centerOnScreen();
                        break;
                    case 6:
                        stage.setHeight(530);
                        stage.setWidth(472);
                        stage.centerOnScreen();
                        break;
                    case 7:
                        stage.setHeight(390);
                        stage.setWidth(790);
                        stage.centerOnScreen();
                        break;
                    case 8:
                        stage.setHeight(663);
                        stage.setWidth(475);
                        stage.centerOnScreen();
                        break;
                    case 9:
                        stage.setHeight(530);
                        stage.setWidth(683);
                        stage.centerOnScreen();
                        break;
                    case 10:
                        stage.setHeight(663);
                        stage.setWidth(577);
                        stage.centerOnScreen();
                        break;
                }


                revolver();

                vBox.getChildren().add(gdpMesa);

                //Valor para click al iniciar y reiniciar.
                click = 0;

                //Valor para los intentos al iniciar y reiniciar.
                contador = 0;
                lblIntentos.setText("intento(s) = " + contador);
            } else {
                alert.setTitle("No pudimos generar el juego, F en el chat ");
                alert.setHeaderText("Número de pares NO valido");
                alert.setGraphic(imv1);
                alert.setContentText("Debes ingresar un número de 2-10");
                alert.showAndWait();
            }
        }catch (Exception e){
            alert.setTitle("No pudimos generar el juego, F en el chat ");
            alert.setHeaderText("Número de pares NO valido");
            alert.setGraphic(imv1);
            alert.setContentText("Debes ingresar un número de 2-10");
            alert.showAndWait();
        }

    }

    private void verTarjeta(int finalI, int finalJ) {

        if (controlClicks(finalI, finalJ)) {

            Image img = new Image("sample/assets/" + arAsignacion[finalI][finalJ]);
            ImageView imv = new ImageView(img);
            imv.setFitWidth(90);
            imv.setPreserveRatio(true);
            arTarjetas[finalI][finalJ].setGraphic(imv);

            //Controlar el doble click en la misma imagen
            arrControlClick[finalI][finalJ]++;

            if(arrControlClick[finalI][finalJ] < 2) {
                //Elementos para verificar si son la misma tarjeta.
                click++;

                if (click == 1) {
                    card1 = arAsignacion[finalI][finalJ];
                    i1 = finalI;
                    j1 = finalJ;
                } else {
                    card2 = arAsignacion[finalI][finalJ];
                    i2 = finalI;
                    j2 = finalJ;
                }
            }
        }

    }

    private void revolver() {

        shuffle();

        int posX, posY, imgRandom, cont=0;

        for (int i = 0; i < posicionI; i++) {
            for (int j = 0; j < posicionJ; j++) {
                arAsignacion[i][j] = new String();
            }
        }

        for (int i = 0; i < noPares; ) {
            posX = (int) (Math.random() * posicionI);
            posY = (int) (Math.random() * posicionJ);
            imgRandom = arrRandom[i];

            if (arAsignacion[posX][posY].equals("")) {
                arAsignacion[posX][posY] = arImagenes[imgRandom];
                cont++;
            }

            if (cont == 2) { //sirve para comprobar que la imagen se asignó 2 veces
                i++;
                cont = 0;
            }
        }

    }

    private void shuffle() {
        arrRandom = new Integer[10];

        for (int i = 0; i < arrRandom.length; i++) {
            arrRandom[i]=i;
        }

        List<Integer> lista = Arrays.asList(arrRandom); //convertimos el arreglo en lista.
        Collections.shuffle(lista); //mezclamos los valores dentro de la lista.
        lista.toArray(arrRandom); //regresamos la lista a un arreglo.
    }

    private void verificarParejas(MouseEvent exit) {

        if(click == 2) {
            //contar de intentos incrementa.
            contador++;
            lblIntentos.setText("intento(s) = " + contador);

            if (!card1.equals(card2)) {

                try{
                    sleep(400);
                }catch(Exception e){ }

                //Voltear cartas.
                Image img1 = new Image("sample/assets/Incognito.png");
                ImageView imv1 = new ImageView(img1);
                imv1.setFitWidth(90);
                imv1.setPreserveRatio(true);
                arTarjetas[i1][j1].setGraphic(imv1);

                Image img2 = new Image("sample/assets/Incognito.png");
                ImageView imv2 = new ImageView(img2);
                imv2.setFitWidth(90);
                imv2.setPreserveRatio(true);
                arTarjetas[i2][j2].setGraphic(imv2);

                arrControlClick[i1][j1] = 0;
                arrControlClick[i2][j2] = 0;
            }
            else{ //Si las cartas son iguales.

                arrVerificarClcik[i1][j1] = 1;
                arrVerificarClcik[i2][j2] = 1;

                //Eviar mensaje de juego terminado.
                Image img = new Image("sample/assets/Win.png");
                ImageView imv = new ImageView(img);
                imv.setFitHeight(300);
                imv.setPreserveRatio(true);

                paresEncontrados++;

                if(paresEncontrados == noPares){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("GAME OVER :D");
                    alert.setHeaderText("¡Felicidades has terminado el juego \nen " + contador + " intento(s)!");
                    alert.setGraphic(imv);
                    alert.showAndWait();

                    //Reiniciar valores del arreglo arrVerificarClick[][] y pares encontrados.
                    for (int i=0; i<2; i++) {
                        for (int j = 0; j < noPares; j++) {
                            arrVerificarClcik[i1][j1] = 0;
                        }
                    }
                    paresEncontrados = 0;
                }
            }

            //Reiniciamos el click.
            click = 0;
        }

    }

    private boolean controlClicks(int i, int j){
        if(arrVerificarClcik[i][j] != 1){
            return true;
        }
        else{
            return false;
        }
    }

}
