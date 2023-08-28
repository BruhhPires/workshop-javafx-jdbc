package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable{

	@FXML
	private TableView<Department> tableViewDepartment; 		  // INSTANCIA A TABLE VIEW COM DEPARTMENT DE ARGUMENTO
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;   // COLUNA 1
	
	@FXML 
	private TableColumn<Department, String> tableColumName;   // COLUNA 2
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
 
	private void initializeNodes() {                             // INICIA O COMPORTAMENTO DAS COLUNAS
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); // PEGA UMA REFERENCIA PRO STAGE DO GETWINDOWN E FAZ O DOWNCAST
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // GARANTE QUE O TABLEVIEW ACOMPANHE A JANELA
	}
}
