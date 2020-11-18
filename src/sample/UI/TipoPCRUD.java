package sample.UI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.components.ButtonCustomeTipo;
import sample.models.TipoPlatilloDAO;

public class TipoPCRUD extends Stage {

    private VBox vBoxTipo;
    private TableView<TipoPlatilloDAO> tbvTipo;
    private Button btnNuevo;
    private Scene escena;
    private TipoPlatilloDAO objTPDao;

    public TipoPCRUD(){

        objTPDao = new TipoPlatilloDAO();
        CrearUI();

        this.setTitle("Panel de administración del Restaurante \"El antojito\"");
        this.setScene(escena);
        this.show();

    }

    private void CrearUI() {

        tbvTipo = new TableView<>();
        CrearTabla();
        btnNuevo = new Button("Nuevo Tipo de Platillo");
        btnNuevo.setOnAction(event -> { new FrmTipo(tbvTipo, null); });
        vBoxTipo = new VBox();
        vBoxTipo.getChildren().addAll(tbvTipo, btnNuevo);
        escena = new Scene(vBoxTipo,800,300);

    }
    public void CrearTabla(){
        TableColumn<TipoPlatilloDAO, Integer> tbcIdTipo = new TableColumn<>("ID");
        tbcIdTipo.setCellValueFactory(new PropertyValueFactory<>("id_tipo"));

        TableColumn<TipoPlatilloDAO, String> tbcDscTipo = new TableColumn<>("Descripción");
        tbcDscTipo.setCellValueFactory(new PropertyValueFactory<>("dsc_tipo"));

        TableColumn<TipoPlatilloDAO, String> tbcEditar = new TableColumn<>("Editar");
        tbcEditar.setCellFactory(
                new Callback<TableColumn<TipoPlatilloDAO, String>, TableCell<TipoPlatilloDAO, String>>() {
                    @Override
                    public TableCell<TipoPlatilloDAO, String> call(TableColumn<TipoPlatilloDAO, String> param) {
                        return new ButtonCustomeTipo(1);
                    }
                }
        );

        TableColumn<TipoPlatilloDAO, String> tbcBorrar = new TableColumn<>("Borrar");
        tbcBorrar.setCellFactory(
                new Callback<TableColumn<TipoPlatilloDAO, String>, TableCell<TipoPlatilloDAO, String>>() {
                    @Override
                    public TableCell<TipoPlatilloDAO, String> call(TableColumn<TipoPlatilloDAO, String> param) {
                        return new ButtonCustomeTipo(2);
                    }
                }
        );

        tbvTipo.getColumns().addAll(tbcIdTipo, tbcDscTipo, tbcEditar, tbcBorrar);
        tbvTipo.setItems(objTPDao.getAllTipo());
    }

}
