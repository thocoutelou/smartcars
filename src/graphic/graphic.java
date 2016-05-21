package graphic;

//引入所需要的包，很多是为了定义类型
import javafx.application.Application;// (extends) identifier le type 'Application' 
import javafx.stage.Stage;//identifier le type 'Stage'
import problem.GraphState;
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
	
	 public void start(Stage primaryStage) { //start（）是程序的入口
		 
		//这里采用网格布局
	     GridPane grid = new GridPane();
	     grid.setAlignment(Pos.CENTER);//默认为居中显示
	     grid.setHgap(10);//gap属性管理行列之间的间距,单位像素
	     grid.setVgap(10);
	     grid.setPadding(new Insets(25,25,25,25));//padding管理grid面板边缘周围的间距，上右下左
		 
	     
	     //增加文本TEXT标签LABEL文本域TEXT_FIELD：创建控件
	     //显示的文本TEXT
	     Text scenetitle = new Text("Welcome to Smartcars!");
	     scenetitle.setFont(Font.font("Tahoma",FontWeight.NORMAL,20));//设置变量的字体、粗细和字号
	     grid.add(scenetitle,0,0,2,1);//!!grid.add()函数将scenetitle变量添加到grid布局之中，第0列第0行列跨度为2行跨度为1
	     //创建Label对象，放到第0列，第1行
	     Label number = new Label("SVG(A number from 0 to 4)");
	     grid.add(number, 0, 1);
	     //创建文本输入框，放到第1列，第1行
	     TextField numberTextField = new TextField();
	     grid.add(numberTextField, 1, 1);
	     //同理设置下一栏
	     Label time = new Label("Date(A double number)");
	     grid.add(time, 0, 2);
	     TextField timeTextField = new TextField();
	     grid.add(timeTextField, 1, 2);
	     //设置输入时的错误提示信息（反馈）
	     final Label label = new Label();
	     GridPane.setConstraints(label, 0, 3);
	     GridPane.setColumnSpan(label, 2);
	     grid.getChildren().add(label);
	     
	     Scene scene = new Scene(grid,500,475);//场景scene的大小设置
	     
	     //设置按钮的信息
	     Button btn = new Button();
	     btn.setText("Enter");
	     HBox hbBtn = new HBox(10);//布局面板
	     hbBtn.setAlignment(Pos.BOTTOM_RIGHT);//将对应节点设置为靠右下对齐
	     hbBtn.getChildren().add(btn);//将按钮控件作为子节点
	     grid.add(hbBtn,1,4);
	     
	     
	     //点击按钮时，会进行操作
	     btn.setOnAction(new EventHandler<ActionEvent>(){
	    	 public void handle(ActionEvent event){
	    		 int svg ;
	    		 double date ;
	    		 //加入程序读数字和日期，读出信息后替换数字和日期
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
		    		     //加入我们的程序
		    		     GraphState graph4 = GraphState.parse(svg);
			    	     graph4.resolve();
			    		 graph4.setCurrentLocations(date);
	    			 }
	    			 /*catch(FileNotFoundException fnfe)
	    			 {
	    				 label.setText("This file number is not available.");
	    			 }*/
	    			 catch(Exception e)
	    			 {
	    				 label.setText("You put wrong data.");
	    			 }
	    		 
	    			
	    		 System.out.println("\n\nSuccess!");//点击按钮后会在控制台输出显示Success
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
