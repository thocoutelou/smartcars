package tests;
import smartCars.*;

public class Test_Graph {
	
	static String project_location="/main/work/2015-1A/projet_info/smartcars/";
	
	int error_number = 0;

	public static void main(String[] args) {
		
		//Scenario 1 : création d'un graphe simple à partir d'une image svg
		String fileName=project_location + "media/map/1.svg";
		Graph graph1 = new Graph(fileName);

	}

}
