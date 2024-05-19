package application;

import java.util.List;

import Connexion.Connexion;

import application.ClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

//			Parent root = FXMLLoader.load(getClass().getResource("voitureajout.fxml"));

			
			Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
			
			Scene scene = new Scene(root, 860, 590);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			Connexion.getConn();
//			ClientController c = new ClientController();
//			List<Voiture> a =c.findAll();
//			c.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
