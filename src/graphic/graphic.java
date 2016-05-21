package graphic;

//引入所需要的包，很多是为了定义类型
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
	
	 public void start(Stage primaryStage) { //start（）是程序的入口
		 Button enter = new Button();
	     enter.setText("Enter");
	     
	     //点击按钮时，会进行操作
	     enter.setOnAction(new EventHandler<ActionEvent>(){
	    	 public void handle(ActionEvent event){
	    		 //加入程序读数字和日期，读出信息后替换数字和日期
	    		 System.out.println("Enter");//点击按钮后会在控制台输出显示Enter
	    	 }
	     });
	     
	     StackPane root = new StackPane(); // 栈布局，子节点存放是一层层加，像栈一样
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
