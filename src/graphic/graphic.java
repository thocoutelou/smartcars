package graphic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

//��������Ҫ�İ�ܶ���Ϊ�˶�������
import javafx.application.Application;// (extends) identifier le type 'Application' 
import javafx.stage.Stage;//identifier le type 'Stage'
import problem.GraphState;
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
 * 
 * @author shuwen
 *
 */
public class graphic extends Application {
	
	//private int svg;
	//private double date;
	
	 public void start(Stage primaryStage) { //start�����ǳ�������
		 
		//���������񲼾�
	     GridPane grid = new GridPane();
	     grid.setAlignment(Pos.CENTER);//Ĭ��Ϊ������ʾ
	     grid.setHgap(10);//gap���Թ�������֮��ļ��,��λ����
	     grid.setVgap(10);
	     grid.setPadding(new Insets(25,25,25,25));//padding����grid����Ե��Χ�ļ�࣬��������
		 
	     
	     //�����ı�TEXT��ǩLABEL�ı���TEXT_FIELD�������ؼ�
	     //��ʾ���ı�TEXT
	     Text scenetitle = new Text("Welcome to Smartcars!");
	     scenetitle.setFont(Font.font("Tahoma",FontWeight.NORMAL,20));//���ñ��������塢��ϸ���ֺ�
	     grid.add(scenetitle,0,0,2,1);//!!grid.add()����scenetitle������ӵ�grid����֮�У���0�е�0���п��Ϊ2�п��Ϊ1
	     //����Label���󣬷ŵ���0�У���1��
	     Label number = new Label("SVG(A number from 0 to 4)");
	     grid.add(number, 0, 1);
	     //�����ı�����򣬷ŵ���1�У���1��
	     final TextField numberTextField = new TextField();
	     grid.add(numberTextField, 1, 1);
	     //ͬ��������һ��
	     Label time = new Label("Date(A double number)");
	     grid.add(time, 0, 2);
	     final TextField timeTextField = new TextField();
	     grid.add(timeTextField, 1, 2);
	     //��������ʱ�Ĵ�����ʾ��Ϣ��������
	     final Label label = new Label();
	     GridPane.setConstraints(label, 0, 3);
	     GridPane.setColumnSpan(label, 2);
	     grid.getChildren().add(label);
	     
	     Scene scene = new Scene(grid,500,475);//����scene�Ĵ�С����
	     
	     //���ð�ť����Ϣ
	     Button btn = new Button();
	     btn.setText("Enter");
	     HBox hbBtn = new HBox(10);//�������
	     hbBtn.setAlignment(Pos.BOTTOM_RIGHT);//����Ӧ�ڵ�����Ϊ�����¶���
	     hbBtn.getChildren().add(btn);//����ť�ؼ���Ϊ�ӽڵ�
	     grid.add(hbBtn,1,4);
	     
	     
	     //�����ťʱ������в���
	     btn.setOnAction(new EventHandler<ActionEvent>(){
	    	 @SuppressWarnings("resource")
			public void handle(ActionEvent event){
	    		 int svg ;
	    		 double date ;
	    		 //�����������ֺ����ڣ�������Ϣ���滻���ֺ�����
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
	    				 String file = new String(SvgParser.getProjectLocation()+"/media/exemple/"+svg+".svg");
	    				 // lance une FileNotFoundException qui peut être 'catch'
	    				 new BufferedReader(new FileReader(file));
		    		     //�������ǵĳ���
		    		     GraphState graph4 = GraphState.parse(svg);
			    	     graph4.resolve();
			    		 graph4.setCurrentLocations(date);
	    			 }
	    			 catch(FileNotFoundException e)
	    			 {
	    				 label.setText("File does not exist.");
	    			 }
	    			 catch(Exception e)
	    			 {
	    				 label.setText("You put wrong data.");
	    			 }
	    		 
	    			
	    		 System.out.println("\n\nSuccess!");//�����ť����ڿ���̨�����ʾSuccess
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
