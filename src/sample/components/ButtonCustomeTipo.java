package sample.components;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import sample.UI.FrmPlatillos;
import sample.UI.FrmTipo;
import sample.models.TipoPlatilloDAO;

import java.util.Optional;

public class ButtonCustomeTipo extends TableCell<TipoPlatilloDAO, String>{
    private Button btnCelda;
    private TipoPlatilloDAO objTipoPlatillo;

    public ButtonCustomeTipo(int opc){

        switch (opc) {
            case 1:
                btnCelda = new Button("Editar");
                btnCelda.setOnAction(event -> {
                    objTipoPlatillo = ButtonCustomeTipo.this.getTableView().getItems().get(ButtonCustomeTipo.this.getIndex());
                    new FrmTipo(ButtonCustomeTipo.this.getTableView(), objTipoPlatillo);
                });
                break;
            case 2:
                btnCelda = new Button("Borrar");
                btnCelda.setOnAction(event -> {

                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                    alerta.setTitle("Mensaje del sistema :)");
                    alerta.setHeaderText("Confirmando acción");
                    alerta.setContentText("¿Realmente deseas borrar el registro?");
                    Optional<ButtonType> result = alerta.showAndWait();
                    if(result.get() == ButtonType.OK){
                        //Obtenemos el objTipoPlatillo de tipo TipoPlatilloDAO de acuerdo al renglón seleccionado
                        objTipoPlatillo = ButtonCustomeTipo.this.getTableView().getItems().get(ButtonCustomeTipo.this.getIndex());
                        objTipoPlatillo.delTipo();

                        //Actualizamos el TableView
                        ButtonCustomeTipo.this.getTableView().setItems(objTipoPlatillo.getAllTipo());
                        ButtonCustomeTipo.this.getTableView().refresh();
                    }
                });
                break;
        }
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if(!empty)
            setGraphic(btnCelda);
    }
}
