package tests;

import org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import smartCars.Graph;
import smartCars.SvgParser;

public class SvgParserTest {

		String project_location;
	@Test
	public void test() {
		try {
			project_location = new File(".").getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Scenario 1 : création d'un graphe simple à partir d'une image svg
		String fileName=project_location + "/media/map/1.svg";
		Graph graph1 = SvgParser.parseGraphState(fileName);
		System.out.println(graph1);
		//fail("Test1");
		
		
		//Scénario 2
		fileName=project_location + "/media/map/2.svg";
		Graph graph2 = SvgParser.parseGraphState(fileName);
		System.out.println(graph2);
		
		//Scénario 3 : parse d'un véhicule
		fileName=project_location + "/media/map/3.svg";
		Graph graph3 = SvgParser.parseGraphState(fileName);
		System.out.println(graph3);
		
		return;
	}

}
