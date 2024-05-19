package application;

import java.io.IOException;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Date;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import Connexion.Connexion;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
public class ReservationController {
	private int selectedCarId;
	@FXML
	private Button btn;
	@FXML
	private GridPane table;

	@FXML
	private TextField tadresse;

	@FXML
	private TextField tnom;

	@FXML
	private TextField tprenom;

	@FXML
	private TextField ttel;

	@FXML
    private DatePicker tdd;

    @FXML
    private DatePicker tdf;
   
	// Méthode pour initialiser les données avec l'ID de la voiture sélectionnée
	public void initData(int id) {
		this.selectedCarId = id;
		System.out.println("id" + this.selectedCarId);

	}

//	public void create(ActionEvent event) {
//	    Connection conn = Connexion.getConn();
//	    String queryClient = "INSERT INTO client(nom, prenom, tel, adresse) VALUES (?, ?, ?, ?)";
//	    String queryReservation = "INSERT INTO Reservation(idv, idc, date_debut, date_fin, total) VALUES (?, ?, ?, ?, ?)";
//
//	    try (PreparedStatement pstmtClient = conn.prepareStatement(queryClient, Statement.RETURN_GENERATED_KEYS);
//	         PreparedStatement pstmtReservation = conn.prepareStatement(queryReservation)) {
//
//	        // Récupération des valeurs des champs de texte
//	        String adresse1 = tadresse.getText();
//	        String nom1 = tnom.getText();
//	        String prenom1 = tprenom.getText();
//	        String tel1 = ttel.getText();
//	        LocalDate localDateDebut = tdd.getValue();
//	        LocalDate localDateFin = tdf.getValue();
//
//	        // Validation des champs
//	        if (!validateClientFields(nom1, prenom1, tel1, adresse1) || !validateDates(localDateDebut, localDateFin)) {
//	            return; // Arrêter l'exécution si les validations échouent
//	        }
//
//	        // Convertir LocalDate en Date
//	        Date dateDebut = Date.valueOf(localDateDebut);
//	        Date dateFin = Date.valueOf(localDateFin);
//
//	        // Insertion des données dans la table "client"
//	        pstmtClient.setString(1, nom1);
//	        pstmtClient.setString(2, prenom1);
//	        pstmtClient.setString(3, tel1);
//	        pstmtClient.setString(4, adresse1);
//
//	        int rowsInsertedClient = pstmtClient.executeUpdate();
//	        if (rowsInsertedClient > 0) {
//	            System.out.println("Un nouveau client a été inséré avec succès.");
//
//	            // Récupération de l'ID du client nouvellement inséré
//	            ResultSet generatedKeys = pstmtClient.getGeneratedKeys();
//	            if (generatedKeys.next()) {
//	                long idClient = generatedKeys.getLong(1);
//
//	                // Insertion des données dans la table "reservation"
//	                pstmtReservation.setLong(1, selectedCarId); // Utilisation de l'ID de voiture sélectionnée
//	                pstmtReservation.setLong(2, idClient);
//	                pstmtReservation.setDate(3, dateDebut);
//	                pstmtReservation.setDate(4, dateFin);
//
//	                // Obtention du tarif journalier de la voiture à partir de la base de données
//	                float tarifJournalier = getTarifJournalier(selectedCarId); // Méthode à implémenter
//
//	                // Calcul du total en utilisant le tarif journalier de la voiture
//	                float total = calculerTotal(dateDebut, dateFin, tarifJournalier);
//	                pstmtReservation.setFloat(5, total);
//
//	                int rowsInsertedReservation = pstmtReservation.executeUpdate();
//	                if (rowsInsertedReservation > 0) {
//	                    System.out.println("Une nouvelle réservation a été insérée avec succès.");
//	                    
//	                    showAlert1("Succès", "La réservation a été effectuée avec succès!\n le montant a payer est "+total+"DNT");
//	                    Stage currentStage = (Stage) tadresse.getScene().getWindow();
//	                    
//	                    // Fermer la fenêtre actuelle
//	                    currentStage.close();
//	                  
//	                    
//	                } else {
//	                    System.err.println("Échec de l'insertion de la réservation.");
//	                }
//	            } else {
//	                System.err.println("Impossible de récupérer l'ID du client nouvellement inséré.");
//	            }
//	        } else {
//	            System.err.println("Échec de l'insertion du client.");
//	        }
//	    } catch (SQLException e) {
//	        System.err.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
//	    }
//	}
	private boolean isCarAvailable(int voitureId, Date dateDebut, Date dateFin) {
	    Connection conn = Connexion.getConn();
	    String query = "SELECT COUNT(*) FROM Reservation WHERE idv = ? AND (date_debut <= ? AND date_fin >= ?)";
	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setInt(1, voitureId);
	        pstmt.setDate(2, dateFin); // vérifier si la date de début de la réservation existante est avant ou à la date de fin demandée
	        pstmt.setDate(3, dateDebut); // vérifier si la date de fin de la réservation existante est après ou à la date de début demandée
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            int reservedCount = rs.getInt(1);
	            int totalCars = getTotalCars(voitureId);
	            return reservedCount < totalCars; // Si le nombre de réservations est inférieur au nombre total de voitures, la voiture est disponible
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // En cas d'erreur, considérer que la voiture n'est pas disponible
	}

	private int getTotalCars(int voitureId) {
	    int totalCars = 0;
	    Connection conn = Connexion.getConn();
	    String query = "SELECT nbV FROM voiture WHERE idV = ?";
	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setInt(1, voitureId);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            totalCars = rs.getInt("nbV");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return totalCars;
	}

	public void create(ActionEvent event) {
	    Connection conn = Connexion.getConn();
	    String queryClient = "INSERT INTO client(nom, prenom, tel, adresse) VALUES (?, ?, ?, ?)";
	    String queryReservation = "INSERT INTO Reservation(idv, idc, date_debut, date_fin, total) VALUES (?, ?, ?, ?, ?)";

	    try (PreparedStatement pstmtClient = conn.prepareStatement(queryClient, Statement.RETURN_GENERATED_KEYS);
	         PreparedStatement pstmtReservation = conn.prepareStatement(queryReservation)) {

	        // Récupération des valeurs des champs de texte
	        String adresse1 = tadresse.getText();
	        String nom1 = tnom.getText();
	        String prenom1 = tprenom.getText();
	        String tel1 = ttel.getText();
	        LocalDate localDateDebut = tdd.getValue();
	        LocalDate localDateFin = tdf.getValue();

	        // Validation des champs
	        if (!validateClientFields(nom1, prenom1, tel1, adresse1) || !validateDates(localDateDebut, localDateFin)) {
	            return; // Arrêter l'exécution si les validations échouent
	        }

	        // Convertir LocalDate en Date
	        Date dateDebut = Date.valueOf(localDateDebut);
	        Date dateFin = Date.valueOf(localDateFin);

	        // Vérifier la disponibilité de la voiture
	        if (!isCarAvailable(selectedCarId, dateDebut, dateFin)) {
	            showAlert("Erreur", "La voiture n'est pas disponible pour les dates demandées.");
	            return; // Arrêter l'exécution si la voiture n'est pas disponible
	        }

	        // Insertion des données dans la table "client"
	        pstmtClient.setString(1, nom1);
	        pstmtClient.setString(2, prenom1);
	        pstmtClient.setString(3, tel1);
	        pstmtClient.setString(4, adresse1);

	        int rowsInsertedClient = pstmtClient.executeUpdate();
	        if (rowsInsertedClient > 0) {
	            System.out.println("Un nouveau client a été inséré avec succès.");

	            // Récupération de l'ID du client nouvellement inséré
	            ResultSet generatedKeys = pstmtClient.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                long idClient = generatedKeys.getLong(1);

	                // Insertion des données dans la table "reservation"
	                pstmtReservation.setLong(1, selectedCarId); // Utilisation de l'ID de voiture sélectionnée
	                pstmtReservation.setLong(2, idClient);
	                pstmtReservation.setDate(3, dateDebut);
	                pstmtReservation.setDate(4, dateFin);

	                // Obtention du tarif journalier de la voiture à partir de la base de données
	                float tarifJournalier = getTarifJournalier(selectedCarId); // Méthode à implémenter

	                // Calcul du total en utilisant le tarif journalier de la voiture
	                float total = calculerTotal(dateDebut, dateFin, tarifJournalier);
	                pstmtReservation.setFloat(5, total);

	                int rowsInsertedReservation = pstmtReservation.executeUpdate();
	                if (rowsInsertedReservation > 0) {
	                    System.out.println("Une nouvelle réservation a été insérée avec succès.");
	                    
	                    showAlert1("Succès", "La réservation a été effectuée avec succès!\n le montant à payer est " + total + " DNT");
	                    Stage currentStage = (Stage) tadresse.getScene().getWindow();
	                    
	                    // Fermer la fenêtre actuelle
	                    currentStage.close();
	                  
	                    
	                } else {
	                    System.err.println("Échec de l'insertion de la réservation.");
	                }
	            } else {
	                System.err.println("Impossible de récupérer l'ID du client nouvellement inséré.");
	            }
	        } else {
	            System.err.println("Échec de l'insertion du client.");
	        }
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
	    }
	}

	private boolean validateClientFields(String nom, String prenom, String tel, String adresse) {
	    if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || adresse.isEmpty()) {
	        showAlert("Erreur", "Tous les champs sont obligatoires.");
	        return false;
	    }

	    if (!tel.matches("\\d{8}")) {
	        showAlert("Erreur", "Numéro de téléphone invalide. Il doit contenir 8 chiffres.");
	        return false;
	    }

	    return true;
	}

	private boolean validateDates(LocalDate debut, LocalDate fin) {
	    if (debut == null || fin == null) {
	        showAlert("Erreur", "Veuillez spécifier une date de début et une date de fin.");
	        return false;
	    }

	    if (debut.isBefore(LocalDate.now())) {
	        showAlert("Erreur", "La date de début doit être aujourd'hui ou une date future.");
	        return false;
	    }

	    if (debut.isAfter(fin)) {
	        showAlert("Erreur", "La date de début doit être antérieure à la date de fin.");
	        return false;
	    }

	    return true;
	}


	private void showAlert(String titre, String message) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle(titre);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	private void showAlert1(String title, String content) {
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle(title); // Utiliser le titre personnalisé
	    alert.setHeaderText(null);
	    alert.setContentText(content);

	    alert.showAndWait();
	}
	
	private float getTarifJournalier(int voitureId) {
	    float tarif = 0.0f; // Par défaut
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	        conn = Connexion.getConn();
	        String query = "SELECT tarif FROM voiture WHERE idV = ?";
	        pstmt = conn.prepareStatement(query);
	        pstmt.setInt(1, voitureId);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            tarif = rs.getFloat("tarif");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
    }

	    return tarif;
	}



	 private float calculerTotal(Date dateDebut, Date dateFin, float tarifJournalier) {
	        // Conversion des dates en millisecondes
	        long debutMs = dateDebut.getTime();
	        long finMs = dateFin.getTime();
	        
	        // Calcul de la différence en jours
	        long diffMs = finMs - debutMs;
	        long diffJours = diffMs / (1000 * 60 * 60 * 24); // 1 jour = 24 heures * 60 minutes * 60 secondes * 1000 millisecondes
	        if(diffJours<0)
	        {
	        	float total=0;
	        	return total;
	        }else
	        {
	        // Calcul du total en multipliant la différence en jours par le tarif journalier
	        float total = diffJours * tarifJournalier;
	        
	        return total;
	        }
	    }


}
