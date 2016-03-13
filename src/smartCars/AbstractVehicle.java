package smartCars;

import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.Stack;

/**
 * Définit un véhicule.
 * Le véhicule connaîtra son itinéraire
 * une fois celui-ci calculé.
 * @author cylla
 *
 */
public class AbstractVehicle {

	// identificateur des instances, s'incrémente à chaque instanciation...
	protected static int identificator = 0;
	// ... pour définir l'identifiant du véhicule créé
	public int identifier;
	
	// localise le point de départ de la voiture, sa destination,
	// et sera mis à jour si une requête de visualisation de la carte est formulée
	public Location location;
	// longueur de la voiture
	public double length;
	// espace moyen entre deux véhicules à l'arrêt
	public static double minSpaceBetweenVehicles = 0.7;
	
	// trajectoire du véhicule une fois calculée
	protected Stack<Road> path;
	// La trajectoire du véhicule est-elle calculée ?
	protected boolean pathCalculated = false;
	// sauvegarde les routes déjà empruntées en vue de les revoir
	protected Stack<Road> itinary;
	// évènements calculés par Dijkstra
	protected PriorityBlockingQueue<AbstractEvent> events;
	
	/**
	 * Constructeur à partir des information de localisation
	 * des points de départ et de destination du véhicule.
	 * @param location
	 */
	public AbstractVehicle(Location location)
	{
		identifier = identificator;
		identificator++;
		this.location = location;
	}
	
	/**
	 * Setter de la trajectoire du véhicule une fois calculée.
	 * @param path
	 */
	public void savePath(Stack<Road> path)
	{
		this.pathCalculated = true;
		this.path = path;
	}
	
	/**
	 * Compare les coûts des plus courts chemins de deux véhicules
	 * s'ils étaient calculés indépendamment,
	 * avec priorité pour le premier véhicule.
	 * @param graph
	 * @param a
	 * @param b
	 * @return véhicule dont la trajectoire optimale est moins coûteuse
	 */
	public static boolean inferiorPath(Graph graph, AbstractVehicle a, AbstractVehicle b)
	{
		Cost aPath = graph.costsMatrix[a.intersectionBeforeEnd().identifier][a.intersectionAfterStart().identifier];
		Cost bPath = graph.costsMatrix[b.intersectionBeforeEnd().identifier][b.intersectionAfterStart().identifier];
		return Cost.inferior(aPath, bPath);
	}
	
	
	//TODO mais encore beaucoup de travail avant cette méthode
	public void calculateLocation(float date, Graph graph)
	{
		
	}
	
	/**
	 * L'hypothèse est faite que la voiture
	 * ne peut se trouver dans une intersection à l'arrêt,
	 * et par conséquent à son départ.
	 * @return prochaine intersection après la position de départ du véhicule
	 */
	public AbstractIntersection intersectionAfterStart()
	{
		return location.nextIntersection();
	}
	
	/**
	 * L'hypothèse est faite que la voiture
	 * ne peut se trouver dans une intersection à l'arrêt,
	 * et par conséquent à son départ.
	 * @return intersection précédent après la destination du véhicule
	 */
	public AbstractIntersection intersectionBeforeEnd()
	{
		return location.finalIntersection();
	}
	
	/**
	 * Renvoie le véhicule dont la trajectoire optimale,
	 * calculée indépendamment des autres véhicules,
	 * présente le coût le moins important.
	 * @param graph
	 * @param vehicles à traiter
	 * @return véhicule dont la destination est la moins coûteuse
	 */
	public static AbstractVehicle lessPriorityVehicle(Graph graph, ArrayList<AbstractVehicle> vehicles)
	{
		AbstractVehicle lessPriorityVehicle = vehicles.get(0);
		for(AbstractVehicle v : vehicles)
		{
			if(inferiorPath(graph, v, lessPriorityVehicle))
			{
				lessPriorityVehicle = v;
			}
		}
		// suppression du véhicule traité des véhicules à traiter
		vehicles.remove(lessPriorityVehicle);
		return lessPriorityVehicle;
	}
	
	/**
	 * Forme la file (events) des évènements du véhicule.
	 */
	public static void buildEvents()
	{
		
	}

}





