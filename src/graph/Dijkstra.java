package graph;


import java.util.ArrayList;
import java.util.Stack;

import resources.Cost;
import smartcars.AbstractVehicle;

/**
 * Implémentation de l'algorithme de Dijkstra,
 * calculant le plus court chemin d'une intersection à une autre,
 * avec augmentation des coûts des routes déjà empruntées,
 * symbolisant le temps d'attente supplémentaire introduit.
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
		System.out.println("<---   Dijkstra pour le véhicule "+vehicle.getIdentifier()+"   --->");
		System.out.println("Coût approximatif de l'itinéraire : "+graph.getFiniteCost(vehicle.intersectionBeforeEnd().identifier, vehicle.intersectionAfterStart().identifier));
		
		AbstractIntersection origin = vehicle.intersectionAfterStart();
		ArrayList<Road> nextRoads = new ArrayList<Road>(origin.getLeavingRoads());
		// route[i] contient la route qui mène à i,
		// qui elle-même contient l'intersection d'origine
		Road route[] = new Road[AbstractIntersection.getIdentificator()];
		route[origin.identifier] = vehicle.getLocation().initialRoad;
		
		boolean visited[] = new boolean[AbstractIntersection.getIdentificator()];
		for(int i=0; i<AbstractIntersection.getIdentificator(); i++) visited[i]=false;
		visited[origin.identifier] = true;
		
		Cost costs[] = new Cost[AbstractIntersection.getIdentificator()];
		for(int i=0; i<AbstractIntersection.getIdentificator(); i++) costs[i] = new Cost();
		costs[origin.identifier] = new Cost(0);
		
		
		while(!nextRoads.isEmpty())
		{
			Road currentRoad = Road.minimum(nextRoads);
			nextRoads.remove(currentRoad);
			visited[currentRoad.getDestination().identifier] = true;
			
			Cost sum;
			sum = Cost.sum(costs[currentRoad.getOrigin().identifier], AbstractIntersection.intersectionCost(currentRoad));
			
			if(Cost.inferior(sum, costs[currentRoad.getDestination().identifier]))
			{
				costs[currentRoad.getDestination().identifier] = sum;
				route[currentRoad.getDestination().identifier] = currentRoad;
			}
			for(Road r : currentRoad.getDestination().getLeavingRoads())
			{
				if(!visited[r.getDestination().identifier])
				{
					nextRoads.add(r);
				}
			}
		}
		
		if(!visited[vehicle.getLocation().finalRoad.getDestination().identifier] & nextRoads.isEmpty())
		{
			throw new IllegalStateException("Le graphe n'est pas connexe.");
		}

		Stack<Road> path = buildPath(vehicle, route);
		increaseCosts(path);
		vehicle.savePath(path);
		System.out.println(vehicle.getPath());
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
		path.push(vehicle.getLocation().finalRoad);
		
		if(vehicle.getLocation().initialRoad.getIdentifier()==vehicle.getLocation().finalRoad.getIdentifier()
				& vehicle.getLocation().initialPosition>vehicle.getLocation().finalPosition)
		{
			path.push(route[path.peek().getOrigin().identifier]);
		}
		
		while(!path.peek().equals(vehicle.getLocation().initialRoad))
			{
				path.push(route[path.peek().getOrigin().identifier]);
			}

		return path;
	}
	
	/**
	 * Augmente les coûts des routes empruntées.
	 * @param path
	 */
	private static void increaseCosts(Stack<Road> path)
	{
		for(Road r : path)
		{
			r.increaseCost();
		}
	}

}
