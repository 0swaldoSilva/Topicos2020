package sample.UI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.models.TipoPlatilloDAO;

public class FrmTipo extends Stage {
    private TextField txtTipo;
    private Button btnGuardar;
    private VBox vBox;
    private Scene escena;
    private TipoPlatilloDAO objTipo;
    private TableView<TipoPlatilloDAO> tbvTipo;
    private int opc;

    public FrmTipo(TableView<TipoPlatilloDAO> tbvTipo, TipoPlatilloDAO objPlatillo){
        if(objPlatillo!=null) {
            opc = 1;
            this.objTipo = objPlatillo;
        }
        else {
            opc = 2;
            this.objTipo = new TipoPlatilloDAO();
        }

        CrearUI();
        this.setTitle("Gestión de Tipo de Platillos");
        this.setScene(escena);
        this.show();

        this.tbvTipo = tbvTipo;
    }

    private void CrearUI() {
        txtTipo = new TextField();
        txtTipo.setText(objTipo.getDsc_tipo());
        btnGuardar = new Button("Guardar tipo de platillo");
        btnGuardar.setOnAction(event -> Guardar());
        vBox = new VBox();
        vBox.getChildren().addAll(txtTipo,btnGuardar);
        escena = new Scene(vBox, 250,250);
    }

    private void Guardar(){
        objTipo.setDsc_tipo(txtTipo.getText());
        if(opc == 1)
            objTipo.updTipo();
        else
            objTipo.insTipo();

        tbvTipo.setItems(objTipo.getAllTipo());
        tbvTipo.refresh();
        this.close();
    }
}
