package smartCars;


import java.util.ArrayList;
import java.util.Stack;

public class Dijkstra {
	
	private Graph graph;
	private int numberOfIntersections = graph.getIntersections().size();
	
	
	public Stack<Road> path(AbstractVehicle vehicle) throws IllegalStateException
	{
		AbstractIntersection origin = vehicle.intersectionAfterStart();
		ArrayList<Road> nextRoads = new ArrayList<Road>(origin.leavingRoads);
		// route[i] contient la route qui mène à i,
		// qui elle-même contient l'intersection d'origine
		Road route[] = new Road[numberOfIntersections];
		
		boolean visited[] = new boolean[numberOfIntersections];
		for(int i=0; i<numberOfIntersections; i++) visited[i]=false;
		visited[origin.identifier] = true;
		
		Cost costs[] = new Cost[numberOfIntersections];
		for(int i=0; i<numberOfIntersections; i++) costs[i] = new Cost();
		costs[origin.identifier] = new Cost(0);
		
		
		while(!visited[origin.identifier] & !nextRoads.isEmpty())
		{
			Road currentRoad = Road.minimum(nextRoads);
			nextRoads.remove(currentRoad);
			visited[currentRoad.destination.identifier] = true;
			
			Cost sum;
			if(Time.Rush())
			{
				sum = Cost.sum(costs[currentRoad.origin.identifier], Cost.intersection(currentRoad));
			}
			else
			{
				sum = Cost.sum(costs[currentRoad.origin.identifier], currentRoad.cost);
			}
			
			if(Cost.inferior(sum, costs[currentRoad.destination.identifier]))
			{
				costs[currentRoad.destination.identifier] = sum;
				route[currentRoad.destination.identifier] = currentRoad;
			}
			for(Road r : currentRoad.destination.leavingRoads)
			{
				if(!visited[r.destination.identifier])
				{
					nextRoads.add(r);
				}
			}
		}
		
		if(!visited[origin.identifier] & nextRoads.isEmpty())
		{
			throw new IllegalStateException("Le graphe n'est pas fortement connexe");
		}

		return buildPath(vehicle, route);
	}

	
	public Stack<Road> buildPath(AbstractVehicle vehicle, Road[] route)
	{
		Stack<Road> path = new Stack<Road>();
		path.push(vehicle.location.finalRoad);
		while(!path.peek().equals(vehicle.location.initialRoad))
		{
			path.push(route[path.peek().origin.identifier]);
		}
		return path;
	}

}
