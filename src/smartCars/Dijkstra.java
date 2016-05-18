package smartCars;


import java.util.ArrayList;
import java.util.Stack;

/**
 * Implémentation de l'algorithme de Dijkstra,
 * calculant le plus court chemin d'une intersection à une autre.
 * @author cylla
 *
 */
public class Dijkstra {
	
	/**
	 * Actualise vehicle.path avec le chemin calculé dans le graphe
	 * à l'aide de l'algorithme Dijkstra
	 * sous la forme d'une pile (Stack) dont le sommet est l'intersection de départ.
	 * @param vehicle
	 * @throws IllegalStateException
	 */
	public static void calculatePath(Graph graph, AbstractVehicle vehicle) throws IllegalStateException
	{
		System.out.println("<---   Dijkstra pour le véhicule "+vehicle.identifier+"   --->");
		
		AbstractIntersection origin = vehicle.intersectionAfterStart();
		ArrayList<Road> nextRoads = new ArrayList<Road>(origin.getLeavingRoads());
		// route[i] contient la route qui mène à i,
		// qui elle-même contient l'intersection d'origine
		Road route[] = new Road[AbstractIntersection.identificator];
		route[origin.identifier] = vehicle.location.initialRoad;
		
		boolean visited[] = new boolean[AbstractIntersection.identificator];
		for(int i=0; i<AbstractIntersection.identificator; i++) visited[i]=false;
		visited[origin.identifier] = true;
		
		Cost costs[] = new Cost[AbstractIntersection.identificator];
		for(int i=0; i<AbstractIntersection.identificator; i++) costs[i] = new Cost();
		costs[origin.identifier] = new Cost(0);
		
		
		while(!nextRoads.isEmpty())
		{
			Road currentRoad = Road.minimum(nextRoads);
			nextRoads.remove(currentRoad);
			visited[currentRoad.destination.identifier] = true;
			
			Cost sum;
			sum = Cost.sum(costs[currentRoad.origin.identifier], Cost.intersection(currentRoad));
			
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
		
		if(!visited[vehicle.location.finalRoad.destination.identifier] & nextRoads.isEmpty())
		{
			throw new IllegalStateException("Le graphe n'est pas connexe.");
		}

		vehicle.savePath(buildPath(vehicle, route));
		System.out.println(vehicle.path);
	}

	/**
	 * Transforme le tableau 'route', contenant dans chaque case
	 * la route précédent la route de même identifiant que l'index
	 * de la case du tableau, en une pile 'path' plus adaptée.
	 * @param vehicle
	 * @param route
	 * @return Stack path (construit sur 'route')
	 */
	private static Stack<Road> buildPath(AbstractVehicle vehicle, Road[] route)
	{
		Stack<Road> path = new Stack<Road>();
		path.push(vehicle.location.finalRoad);
		
		if(vehicle.location.initialRoad.identifier==vehicle.location.finalRoad.identifier
				& vehicle.location.initialPosition>vehicle.location.finalPosition)
		{
			path.push(route[path.peek().origin.identifier]);
		}
		
		while(!path.peek().equals(vehicle.location.initialRoad))
			{
				path.push(route[path.peek().origin.identifier]);
			}

		return path;
	}

}
