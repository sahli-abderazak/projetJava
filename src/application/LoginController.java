package application;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class LoginController {
    @FXML
    private TextField password;

    @FXML
    private TextField user;

    @FXML
    private Button valider;
    
    @FXML
    private Button retour;

//    @FXML
//    public void handleSubmitButtonAction(ActionEvent event) {
//    	try {
//      BufferedReader bf= new BufferedReader(new FileReader("D:\\fichiers\\in.txt"));
//      String line;
//     while ((line=bf.readLine())!=null) 
//     {
//  	   String [] s=line.split("/");
//  	   if(s.length==2 && s[0].equals(user.getText())&&s[1].equals(password.getText()))
//  	   {
//  		   System.out.println("validee");
//  		   return ;
//  	   }
//     }
//     System.out.println("non validee");
//     
//  } catch (IOException e) {
//      e.printStackTrace();
//  }	
//    	
//}
    @FXML
    public void handleSubmitButtonAction(ActionEvent event) {
        boolean authentifie = false;
        try {
            BufferedReader bf = new BufferedReader(new FileReader("D:\\fichiers\\in.txt"));
            String line;
            while ((line = bf.readLine()) != null) {
                String[] s = line.split("/");
                if (s.length == 2 && s[0].equals(user.getText()) && s[1].equals(password.getText())) {
                    System.out.println("Valide");

                    // Charger la vue dash.fxml
                    Parent root = FXMLLoader.load(getClass().getResource("dash.fxml"));

                    // Créer une nouvelle scène
                    Scene scene = new Scene(root);

                    // Obtenir la scène actuelle à partir du bouton cliqué
                    Stage currentStage = (Stage) valider.getScene().getWindow();

                    // Modifier la scène de la fenêtre actuelle pour afficher la vue dash.fxml
                    currentStage.setScene(scene);
                    currentStage.show();

                    authentifie = true;
                    break;
                }
            }
            if (!authentifie) {
                System.out.println("Non valide");
                // Afficher un message d'erreur dans une boîte de dialogue
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur d'authentification");
                alert.setHeaderText(null);
                alert.setContentText("Nom d'utilisateur ou mot de passe invalide. Veuillez réessayer.");

                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void redirectToHome(ActionEvent event) {
        try {
            // Charger la vue login.fxml
            Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage currentStage = (Stage) retour.getScene().getWindow();

            // Modifier la scène de la fenêtre actuelle pour afficher la vue de connexion
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer les exceptions liées au chargement de la vue
        }
    }
}
