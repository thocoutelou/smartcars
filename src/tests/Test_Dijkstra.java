package tests;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;
import smartCars.*;

public class Test_Dijkstra {
	
	private Graph graph;

	@Test
	public void test()
	{
		//TODO
	}
	
	@Before
	public void littleGraph()
	{
		ArrayList<AbstractIntersection> intersections = new ArrayList<AbstractIntersection>();
		ArrayList<Road> leavingRoads = new ArrayList<Road>();
		for(int i=0; i<4; i++)
		{
			intersections.add(new AbstractIntersection());
		}
	}

}
