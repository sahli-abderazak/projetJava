package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import Connexion.Connexion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminController {
	
	@FXML
    private TextField marque1;
    
    @FXML
    private TextField modele1;
    
    @FXML
    private TextField nbV1;
    
    @FXML
    private TextField tarif1;
    
    
    
    @FXML
    private TextField addresse1;

    @FXML
    private Button enregistrer;

    @FXML
    private TextField nom1;

    @FXML
    private TextField prenom1;

    @FXML
    private TextField tel1;
    
    
    
    private int selectedCarId;
    
    private int selectedCliId;
    @FXML
    private TextField img;
    public void initData(int id) {
        this.selectedCarId = id;
        System.out.println("id" + this.selectedCarId);

        try {
            // Connexion à la base de données
            Connection conn = Connexion.getConn();
            String query = "SELECT * FROM voiture WHERE idV = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            // Si une voiture avec l'ID spécifié est trouvée
            if (rs.next()) {
                // Récupérer les valeurs de la voiture depuis la base de données
                String marque = rs.getString("marque");
                String modele = rs.getString("modele");
                int nbVoitures = rs.getInt("nbV");
                float tarif = rs.getFloat("tarif");
                String imageUrl = rs.getString("imgV");

                // Afficher les valeurs dans les champs correspondants du formulaire
                marque1.setText(marque);
                modele1.setText(modele);
                nbV1.setText(String.valueOf(nbVoitures));
                tarif1.setText(String.valueOf(tarif));
                img.setText(imageUrl);
            } else {
                showAlert("Voiture non trouvée avec l'ID spécifié.");
            }

            // Fermeture des ressources
//            rs.close();
//            stmt.close();
//            conn.close();
        } catch (SQLException e) {
            showAlert("Erreur lors de la récupération des données de la voiture : " + e.getMessage());
        }
    }

    
    public void ajouterVoiture() {

        String marque = marque1.getText();
        String modele = modele1.getText();
        String nbVoituresText = nbV1.getText();
        String tarifText = tarif1.getText();
        String imageUrl = img.getText();

        // Vérification des champs vides
        if (marque.isEmpty() || modele.isEmpty() || nbVoituresText.isEmpty() || tarifText.isEmpty()
                || imageUrl.isEmpty()) {
            showAlert("Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Conversion des valeurs texte en types appropriés
            int nbVoitures = Integer.parseInt(nbVoituresText);
            float tarif = Float.parseFloat(tarifText);

            // Connexion à la base de données
            Connection conn = Connexion.getConn();
            String query = "INSERT INTO voiture (marque, modele, nbV, tarif, imgV) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Paramètres de la requête
            stmt.setString(1, marque);
            stmt.setString(2, modele);
            stmt.setInt(3, nbVoitures);
            stmt.setFloat(4, tarif);
            stmt.setString(5, imageUrl);

            // Exécution de la requête
            stmt.executeUpdate();

//            // Fermeture des ressources
//            stmt.close();
//            conn.close();

            // Affichage d'un message de confirmation
            showAlert("Voiture ajoutée avec succès !");
            
            // Récupérer la scène à partir de n'importe quel champ
            Stage currentStage = (Stage) marque1.getScene().getWindow();
            
            // Fermer la fenêtre actuelle
            currentStage.close();
//            VoitureController c = new VoitureController();
//			c.findAll();
//			c.initialize();

        } catch (NumberFormatException e) {
            showAlert("Veuillez saisir des valeurs numériques valides pour le nombre de voitures et le tarif.");
        } catch (SQLException e) {
            showAlert("Erreur lors de l'ajout de la voiture : " + e.getMessage());
        }
    }
    
    // Fonction utilitaire pour afficher une boîte de dialogue d'alerte
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("information ");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void modifierVoiture() {

        
        String marque = marque1.getText();
        String modele = modele1.getText();
        String nbVoituresText = nbV1.getText();
        String tarifText = tarif1.getText();
        String imageUrl = img.getText();

        
        if (marque.isEmpty() || modele.isEmpty() || nbVoituresText.isEmpty() || tarifText.isEmpty()
                || imageUrl.isEmpty()) {
            showAlert("Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Conversion des valeurs texte en types appropriés
            int nbVoitures = Integer.parseInt(nbVoituresText);
            float tarif = Float.parseFloat(tarifText);

            // Connexion à la base de données
            Connection conn = Connexion.getConn();
            String query = "UPDATE voiture SET marque=?, modele=?, nbV=?, tarif=?, imgV=? WHERE idV=?"; 
            PreparedStatement stmt = conn.prepareStatement(query);

            // Paramètres de la requête
            stmt.setString(1, marque);
            stmt.setString(2, modele);
            stmt.setInt(3, nbVoitures);
            stmt.setFloat(4, tarif);
            stmt.setString(5, imageUrl);
            stmt.setInt(6, selectedCarId);
            

            // Exécution de la requête
            stmt.executeUpdate();
            
         // Récupérer la scène à partir de n'importe quel champ
            Stage currentStage = (Stage) marque1.getScene().getWindow();
            
            // Fermer la fenêtre actuelle
            currentStage.close();


            // Affichage d'un message de confirmation
            showAlert("Voiture modifiée avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Veuillez saisir des valeurs numériques valides pour le nombre de voitures et le tarif.");
        } catch (SQLException e) {
            showAlert("Erreur lors de la modification de la voiture : " + e.getMessage());
        }
    }


    public void initDataClient(int id) {
        this.selectedCliId = id;
        System.out.println("ID du client : " + this.selectedCliId);

        try {
            Connection conn = Connexion.getConn();
            String query = "SELECT * FROM client WHERE idC = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String tel = rs.getString("tel");
                String adresse = rs.getString("adresse");

                nom1.setText(nom);
                prenom1.setText(prenom);
                tel1.setText(tel);
                addresse1.setText(adresse);
            } else {
                showAlert("Client non trouvé avec l'ID spécifié.");
            }

            
        } catch (SQLException e) {
            showAlert("Erreur lors de la récupération des données du client : " + e.getMessage());
        }
    }

    public void modifierClient() {
        String nom = nom1.getText();
        String prenom = prenom1.getText();
        String tel = tel1.getText();
        String adresse = addresse1.getText();

        // Vérifier si tous les champs sont remplis
        if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || adresse.isEmpty()) {
            showAlert("Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Connexion à la base de données
            Connection conn = Connexion.getConn();
            String query = "UPDATE client SET nom=?, prenom=?, tel=?, adresse=? WHERE idC=?";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Paramètres de la requête
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, tel);
            stmt.setString(4, adresse);
            stmt.setInt(5, selectedCliId);

            // Exécution de la requête
            stmt.executeUpdate();
           

            // Récupérer la scène à partir de n'importe quel champ
            Stage currentStage = (Stage) nom1.getScene().getWindow();
            
            // Fermer la fenêtre actuelle
            currentStage.close();

            // Affichage d'un message de confirmation
            showAlert("Client modifié avec succès !");
        } catch (SQLException e) {
            showAlert("Erreur lors de la modification du client : " + e.getMessage());
        }
    }

    
    
}
    
	
	


