package smartCars;


import java.util.ArrayList;
import java.util.Stack;

public class Dijkstra {
	
	/**
	 * Actualise vehicle.path avec le chemin calculé dans le graphe
	 * à l'aide de l'algorithme Dijkstra
	 * sous la forme d'une pile (Stack) dont le sommet est l'intersection de départ.
	 * @param vehicle
	 * @throws IllegalStateException
	 */
	public void path(Graph graph, AbstractVehicle vehicle) throws IllegalStateException
	{
		AbstractIntersection origin = vehicle.intersectionAfterStart();
		ArrayList<Road> nextRoads = new ArrayList<Road>(origin.getLeavingRoads());
		// route[i] contient la route qui mène à i,
		// qui elle-même contient l'intersection d'origine
		Road route[] = new Road[graph.numberOfIntersections];
		
		boolean visited[] = new boolean[graph.numberOfIntersections];
		for(int i=0; i<graph.numberOfIntersections; i++) visited[i]=false;
		visited[origin.identifier] = true;
		
		Cost costs[] = new Cost[graph.numberOfIntersections];
		for(int i=0; i<graph.numberOfIntersections; i++) costs[i] = new Cost();
		costs[origin.identifier] = new Cost(0);
		
		
		while(!nextRoads.isEmpty())
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
			for(Road r : currentRoad.destination.getLeavingRoads())
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

		vehicle.path = buildPath(vehicle, route);
	}

	/**
	 * Transforme le tableau 'route' contenant dans chaque case
	 * la route précédent la route de même identifiant que l'index
	 * de la case du tableau en une pile 'path' plus adaptée.
	 * @param vehicle
	 * @param route
	 * @return Stack path (construit sur 'route')
	 */
	private Stack<Road> buildPath(AbstractVehicle vehicle, Road[] route)
	{
		Stack<Road> path = new Stack<Road>();
		path.push(vehicle.location.finalRoad);
		while(!path.peek().equals(vehicle.location.initialRoad))
		{
			path.push(route[path.peek().origin.identifier]);
		}
		return path;
	}
	
	//TODO
	public void changePath(ArrayList<Event> events)
	{
		
	}

}
