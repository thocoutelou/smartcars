package graphic;

//��������Ҫ�İ����ܶ���Ϊ�˶�������
import javafx.application.Application;// (extends) identifier le type 'Application' 
import javafx.stage.Stage;//identifier le type 'Stage'
import javafx.scene.control.Button;//identifier le type 'Button'
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;

/**
 * 
 * @author shuwen
 *
 */
public class graphic extends Application {
	
	private int svg;
	private double date;
	
	 public void start(Stage primaryStage) { //start�����ǳ�������
		 Button enter = new Button();
	     enter.setText("Enter");
	     
	     //�����ťʱ������в���
	     enter.setOnAction(new EventHandler<ActionEvent>(){
	    	 public void handle(ActionEvent event){
	    		 //�����������ֺ����ڣ�������Ϣ���滻���ֺ�����
	    		 System.out.println("Enter");//�����ť����ڿ���̨�����ʾEnter
	    	 }
	     });
	     
	     StackPane root = new StackPane(); // ջ���֣��ӽڵ�����һ���ӣ���ջһ��
	     root.getChildren().add(enter);
	     Scene scene = new Scene(root,300,250);
	     
	     primaryStage.setTitle("Enter");
	     primaryStage.setScene(scene);
	     primaryStage.show();
	 }
	 
	 public static void main(String[] args){
		 launch(args);
	 }
}
