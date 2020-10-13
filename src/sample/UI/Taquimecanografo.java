package sample.UI;


import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;

public class Taquimecanografo extends Stage {

    //Elementos para el toolbar.
    private ToolBar tlbMenu;
    private Button btnAbrir;

    //Elementos para la escritura.
    private TextArea txtContenido, txtEscritura;

    //Elementos para el teclado.
    private HBox[] arrHBoxTeclas = new HBox[6];
    private VBox vBoxTeclado;
    private Button[] arrBtnTeclado = new Button[14];

    //Elementos de agrupación global.
    private VBox vBoxPrincipal;
    private Scene escena;

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
        vBoxPrincipal.getChildren().addAll(tlbMenu, txtContenido, txtEscritura);
        vBoxPrincipal.setSpacing(10);
        vBoxPrincipal.setPadding(new Insets(10));
        escena = new Scene(vBoxPrincipal,800,500);

    }

    private void CrearToolbar() {

        //Crear barra de herramientas.
        tlbMenu = new ToolBar();
        btnAbrir = new Button();
        btnAbrir.setOnAction(event -> eventoTaqui(1));
        btnAbrir.setPrefSize(35,35);

        //Asignamos la imagen  al boton dentro del toolbar.
        Image img = new Image("sample/assets/Open.png");
        ImageView imv = new ImageView(img);
        imv.setFitHeight(35);
        imv.setPreserveRatio(true);
        btnAbrir.setGraphic(imv);
        tlbMenu.getItems().addAll(btnAbrir);

    }

    private void eventoTaqui(int opc) {

        switch (opc){
            case 1:
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Abrir archivo");
                File file = fileChooser.showOpenDialog(this);
                break;
        }

    }

    private void CrearEscritura() {

        txtContenido = new TextArea();
        txtContenido.setPrefRowCount(6);
        txtEscritura = new TextArea();
        txtEscritura.setPrefRowCount(6);

    }

    private void CrearTeclado() {



    }

}
