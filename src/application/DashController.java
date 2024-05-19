package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Connexion.Connexion;

public class DashController {

    @FXML
    private Text TVoi; 
    @FXML
    private Text Tresa; 
    @FXML
    private Text TCli; 
    
    @FXML
    private Button vButton;

    
    public void initialize() {
        
        afficherTotaux();
    }
    
    @FXML
    public void export(ActionEvent event) {
        String fxmlFile = null;
        if (event.getSource() instanceof javafx.scene.control.Button) {
            javafx.scene.control.Button clickedButton = (javafx.scene.control.Button) event.getSource();
            switch (clickedButton.getId()) {
                case "vButton":
                    fxmlFile = "voitureDash.fxml";
                    break;
                case "cButton":
                    fxmlFile = "clientDash.fxml";
                    break;
                case "rButton":
                    fxmlFile = "resaDash.fxml";
                    break;
                default:
                    break;
            }
        }

        if (fxmlFile != null) {
            // Charger la nouvelle page
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	// Méthode pour récupérer les totaux à partir de la base de données et les afficher
    private void afficherTotaux() {
        try {
            // Connexion à la base de données
            Connection conn = Connexion.getConn();

            // Requêtes SQL pour récupérer les totaux
            String queryVoitures = "SELECT sum(nbV) AS total_voitures FROM voiture";
            String queryReservations = "SELECT COUNT(*) AS total_reservations FROM reservation";
            String queryClients = "SELECT COUNT(*) AS total_clients FROM client";

            // Exécution des requêtes
            PreparedStatement stmtVoitures = conn.prepareStatement(queryVoitures);
            PreparedStatement stmtReservations = conn.prepareStatement(queryReservations);
            PreparedStatement stmtClients = conn.prepareStatement(queryClients);

            // Récupération des résultats
            ResultSet rsVoitures = stmtVoitures.executeQuery();
            ResultSet rsReservations = stmtReservations.executeQuery();
            ResultSet rsClients = stmtClients.executeQuery();

            // Affichage des résultats
            if (rsVoitures.next()) {
                int totalVoitures = rsVoitures.getInt("total_voitures");
                TVoi.setText(String.valueOf("Total voitures :"+totalVoitures));
            }

            if (rsReservations.next()) {
                int totalReservations = rsReservations.getInt("total_reservations");
                Tresa.setText(String.valueOf("Total reservations :"+totalReservations));
            }

            if (rsClients.next()) {
                int totalClients = rsClients.getInt("total_clients");
                TCli.setText(String.valueOf("Total clients :"+totalClients));
            }

            // Fermeture des ressources
//            rsVoitures.close();
//            stmtVoitures.close();
//            rsReservations.close();
//            stmtReservations.close();
//            rsClients.close();
//            stmtClients.close();
//            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleeSubmitButtonAction(ActionEvent event)
    {
   	 Platform.exit();
    }
}