package smartCars;


import java.util.Stack;

public class Dijkstra {
	
	private Graph graph;
	private int numberOfIntersections = graph.getIntersections().size();
	
	
	public void djikstra(AbstractVehicle vehicle)
	{
		AbstractIntersection origin = vehicle.intersectionAfterStart();
		// Stack: structure LIFO (last in first out), pour un parcours du graphe en profondeur
		// Méthodes: empty(), peek(), pop(), push(e), search(e)
		// Pile des voisins aux derniers noeuds traités...
		Stack<AbstractIntersection> nextIntersections = new Stack<AbstractIntersection>();
		// ... initialisée avec le noeud choisi :
		nextIntersections.add(origin);
		
		boolean visited[] = new boolean[numberOfIntersections];
		for(int i=0; i<numberOfIntersections; i++) visited[i]=false;
		visited[origin.identifier] = true;
		
		Cost costs[] = new Cost[numberOfIntersections];
		for(int i=0; i<numberOfIntersections; i++) costs[i] = new Cost();
		costs[origin.identifier] = new Cost(0);
		
		
		while(!nextIntersections.isEmpty())
		{
			
		}
		
	}

}
