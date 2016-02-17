package smartCars;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Le graphe est représenté par la liste de ses noeuds (intersections AbstractIntersection),
 * chacune d'entre elles contenant la liste de ses arêtes (routes Road) directement connectées.
 * L'hypothèse est faite que le graphe est FORTEMENT CONNEXE.
 */
public class Graph {
	
	// (intersections) définit le graphe même
	private ArrayList<AbstractIntersection> intersections;
	
	// (startDefault) définit un point de départ pour les AbstractVehicle instanciés sans précision
	public static Location startDefault;
	
	public static ArrayList<AbstractVehicle> vehiclesInGraph;
	

	public Graph(ArrayList<AbstractIntersection> intersections, Location startDefault, ArrayList<AbstractVehicle> vehicles)
	{
		this.intersections = intersections;
		Graph.startDefault = startDefault;
		Graph.vehiclesInGraph = vehicles;
	}
	
	
	//TODO contructeur à partir d'un image vectorielle
	//TODO définir les normes de l'image (notamment les couleurs)
	/*
	 * Étapes du parsing de l'image:
	 * 0 : Ouvrir le fichier .svg
	 * 1 : sélectionner les informations dans le calque 1 (balise g)
	 * 2 : créer toute les intersections représentées par des cercles <circle/>
	 * 3 : créer les road dans les intersections, représentés par des <path/>
	 * 	   un path a une intersection de départ si son premier sommet se trouve dans la zone d'un cercle (intersection)
	 */
	public Graph(String fileName) {
		String line = null;
		
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
	}

	public ArrayList<AbstractIntersection> getIntersections()
	{
		return intersections;
	}
	
}