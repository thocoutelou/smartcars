package tests;
import java.io.File;
import java.io.IOException;

import smartCars.*;

public class Test_Graph {

	static String project_location;
	
	int error_number = 0;

	public static void main(String[] args) {
		
		//Temporaire
		try {
			Test_Graph.project_location = new File(".").getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Scenario 1 : création d'un graphe simple à partir d'une image svg
		String fileName=project_location + "/media/map/1.svg";
		Graph graph1 = new Graph(fileName);

	}

}
