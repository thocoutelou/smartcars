package graphic;

//importer les paquet 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javafx.application.Application;// (extends) identifier le type 'Application' 
import javafx.stage.Stage;//identifier le type 'Stage'
import problem.GraphState;
import problem.SvgGenerator;
import problem.SvgParser;
import javafx.scene.control.Button;//identifier le type 'Button'
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.Scene;


/**
 * Crée une fenêtre graphique qui demande à l'utilisateur
 * quelle image traiter et à quelle date afficher les véhicules.
 * Lance donc une résolution complète.
 * @author Shuwen
 *
 */
public class graphic extends Application {
	
	//Private int svg;
	//Private double date;
	
	 public void start(Stage primaryStage) {  //start est le début pour le programme 
		 
		//La forme pour la fenêtre est la grille (grid en anglais)
	     GridPane grid = new GridPane();
	     grid.setAlignment(Pos.CENTER);//On paramétre qu'on met les caractères au centre par défaut.
	     grid.setHgap(10);//gap pour paramétrer l'intervalle entre les lignes 
	     grid.setVgap(10);//gap pour paramétrer l'intervalle entre les colonne
	     grid.setPadding(new Insets(25,25,25,25));//padding pour paramétrer l'intervalle de pourtour
		 
	     
	     //Paramétrer le TEXT, LABEL, TEXT_FIELD.
	     //Paramétrer le TEXT
	     Text scenetitle = new Text("Welcome to Smartcars!");
	     scenetitle.setFont(Font.font("Tahoma",FontWeight.NORMAL,20));//Paramétrer le type et la taille de texte. 
	     grid.add(scenetitle,0,0,2,1);//Le méthode grid.add() ajoute scenetitle à la grille (ligne 0, colonne 0, occupé 2 lignes, occupé 1 colonne) 
	     //Paramétrer le LABEL
	     Label number = new Label("SVG (integer)");
	     grid.add(number, 0, 1);
	     //Paramétrer le TEXT_FIELD
	     final TextField numberTextField = new TextField();
	     grid.add(numberTextField, 1, 1);
	     //Pareils qu'avant 
	     Label time = new Label("Date (in seconds)");
	     grid.add(time, 0, 2);
	     final TextField timeTextField = new TextField();
	     grid.add(timeTextField, 1, 2);
	     //Créer un label pour signaler, s'il y a une exception. 
	     final Label label = new Label();
	     GridPane.setConstraints(label, 0, 3);
	     GridPane.setColumnSpan(label, 2);
	     grid.getChildren().add(label);
	     
	     Scene scene = new Scene(grid,500,475);//Créer la scène. 
	     
	     //Paramétrer le bouton (button en anglais) 
	     Button btn = new Button();
	     btn.setText("Enter");
	     HBox hbBtn = new HBox(10);//Créer panneau HBox 
	     hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
	     hbBtn.getChildren().add(btn);//Mettre hbBtncomme un noeuds enfants de la grille 
	     grid.add(hbBtn,1,4);
	     
	     
	     //Les action après de cliquer l'Enter
	     btn.setOnAction(new EventHandler<ActionEvent>(){
	    	 @SuppressWarnings("resource")
			public void handle(ActionEvent event){
	    		 int svg ;
	    		 double date ;
	    		 //Capturer les datas que l'utilisateur remplit.
	    		 System.out.println(numberTextField.getText());
	    		 System.out.println(timeTextField.getText());
	    		 if ( (numberTextField.getText().equals("")) || (timeTextField.getText().equals(""))){
	    			 
	    			 System.out.print("Error!");
	    			 label.setText("You forgot some information.");
	    			 
	    		 }else{
	    			 try
	    			 {
	    				 System.out.println("JavaFX_Smartcars");
		    		     svg =Integer.parseInt(numberTextField.getText());
		    		     System.out.println("SVG Number : "+svg);
		    		     date = Double.parseDouble(timeTextField.getText());
		    		     System.out.println("Date : "+date);
		
	    				 // lance une FileNotFoundException qui peut être 'catch'
		    		     String file = new String(SvgParser.getProjectLocation()+"/media/exemple/"+svg+".svg");
	    				 new BufferedReader(new FileReader(file));
	    				 
		    		     //Appler les autres programmes
		    		     GraphState graph = GraphState.parse(svg);
			    	     graph.resolve();
			    		 graph.setCurrentLocations(date);
			    		 //Créer le file pour afficher l'image
			    		 String outputS = new String(SvgParser.getProjectLocation()+"/media/output/"+svg+".svg");
			    		 File outputF = new File(outputS);
			    		 new SvgGenerator(graph, outputF);
			    		 
			    		 label.setText("Success!\nView file /media/output/"+svg+".svg");
	    			 }
	    			 catch(FileNotFoundException e)
	    			 {
	    				 label.setText("File does not exist.");
	    			 }
	    			 catch(Exception e)
	    			 {
	    				 label.setText("You put wrong data.");
	    			 }
	    		 
	    			
	    		 System.out.println("\n\nFin!");
	    		 }
	    	 }
	     });
	
	     primaryStage.setTitle("JavaFX_Smartcars");	
	     primaryStage.setScene(scene);
	     primaryStage.show();	
	 }	
	 
	 public static void main(String[] args){	
		 launch(args);	
	 }
}
