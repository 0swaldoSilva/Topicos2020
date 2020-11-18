package sample.UI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard extends Stage {

    private Scene escena;
    private HBox hBox;
    private VBox vBoxPlatillo, vBoxTipo;
    private Button btnPlatillo, btnTipo;

    public Dashboard(){
        CrearUI();
        this.setTitle("Panel de administración del Restaurante \"El antojito\"");
        this.setScene(escena);
        this.setMaximized(true);
        this.show();
    }

    private void CrearUI() {
        hBox = new HBox();
        vBoxPlatillo = new VBox();
        vBoxTipo = new VBox();
        btnPlatillo = new Button("PLATILLO");
        btnPlatillo.setOnAction(event -> opcion(1));
        btnTipo = new Button("TIPO DE PLATILLO");
        btnTipo.setOnAction(event -> opcion(2));
        hBox.getChildren().addAll(btnPlatillo,btnTipo);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        escena = new Scene(hBox);
        escena.getStylesheets().add("sample/css/restaurante_styles.css");
    }

    private void opcion(int opc) {
        switch (opc){
            case 1: new PlatilloCRUD(); break;
            case 2: new TipoPCRUD();    break;
        }
    }

}
