package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable{
	
	private Seller entity;
	
	private SellerService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	// ATRIBUTOS SELLER
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	// LABELS DE ERRO
	@FXML
	private Label labelErroName;
	
	@FXML
	private Label labelErroEmail;
	
	@FXML
	private Label labelErroBirthDate;
	
	@FXML
	private Label labelErroBaseSalary;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();           // FECHA A JANELA DO EVENTO ATUAL
		}
		catch (ValidationException e) {
			setErrorMessaqges(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener: dataChangeListeners){
			listener.onDataChange();
		}
	}

	private Seller getFormData() {                    // INSTANCIA E RETORNA UM OBJ
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation Error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals(" ")){
			exception.addErrors("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if (exception.getErrors().size() > 0) {  // SE TIVER ALGUMA EXCEÇÃO LANÇA O ERROR
			throw exception;
		}
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();           // FECHA A JANELA DO EVENTO ATUAL
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		gui.util.Constraints.setTextFieldInteger(txtId);
		gui.util.Constraints.setTextFieldMaxLength(txtName, 70);
		gui.util.Constraints.setTextFieldDouble(txtBaseSalary);
		gui.util.Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");

	}
	
	public void updateFormData() {                              // METODO DE ATUALIZAR DADOS
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));          // PEGA O NUMERO QUE É INTEIRO E TRANSFORMA EM STRING 
		txtName.setText(entity.getName()); 	
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));  // ATRIBUI AO DPBIRTH DATE O VALOR USANDO LOCAL DATE  APLICANDO O TO INSTANTE COM O HORARIO DO PC
		}
	}
	
	private void setErrorMessaqges (Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErroName.setText(errors.get("name"));
		}
	}
}
