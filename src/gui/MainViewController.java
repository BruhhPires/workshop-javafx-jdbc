package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemdepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
	
	private synchronized void loadView (String absoluteName) {   //METODO QUE CARREGA A TELA "syncronized" GARANTE QUE ESTE PROCESSAMENTO NÃO VAI SER INTERROMPIDO DURANTE O MULTITREADH
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		VBox newVBox = loader.load();
		
		Scene mainScene = Main.getMainScene();
		VBox mainVbox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();  // O GET ROOT PEGA A PRIMEIRA LINHA QUE É O SCROLLPANE(CAST) E VBOX (VBOX)
		
		Node mainMenu = mainVbox.getChildren().get(0); 					// AQUI PEGAREMOS O VALOR DA PRIMEIRA LINHA DO VBOX, QUE DESEJAMOS MANTER AO CLICAR
		mainVbox.getChildren().clear();                                 // AGORA LIMPAMOS TUDO, QUANDO CLICAMOS PRA RECEBER OUTRA TELA
		mainVbox.getChildren().add(mainMenu);							// ADICIONAMOS O MENU QUE JÁ TINHAMOS MANTIDO NA TELA NOVA -> DEPOIS DE CLICAR
		mainVbox.getChildren().addAll(newVBox.getChildren()); 			// E ADICIOANMOS OS FILHOS DO ABOUT, PARA APARECER APENAS AS INFORMAÇÕES
		
		}
		catch (IOException e) {                     // TRATATIVA DE ERRO
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
