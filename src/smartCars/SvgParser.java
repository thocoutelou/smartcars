package smartCars;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Cette classe permet de créer un graphe à partir d'une image svg. 
 * Elle en extrait également les voitures.
 * @author thomas
 */
public class SvgParser {
	
	// TODO URGENT
	// Parser les vitesses autorisées sur les routes (Road.speed) ; peut être le résultat d'une classification par palliers des routes en fonction de leur longueur.
	// Parser le temps moyen de traversée d'une intersection (AbstractIntersection.averageTime) ; peut être la conséquence du diamètre ou de la circonférence du cercle qui la représente dans l'image.
	// Parser une date de départ aléatoire aux véhicules (AbstractVehicle.location.initialDate).
	// Puis supprimer les valeurs par défaut que j'ai forcées au niveau des attributs des classes pour les tests.

	// ***** Commentaires de lecture et questions *****
		// -> pourquoi utiliser float plutôt que double ?
		// -> ajout d'une initialisation de fortune du coût d'une route
		// -> il faudra utiliser les constructeurs complets des objets,
		// quitte à modifier ces constructeurs, pour faciliter la lecture et cibler le debug :
		// commencer par définir les caractéristiques de l'objet à créer avant d'instancier la classe
		// -> je pense que l'exception NullPointerException est due à l'initialisation des routes de ce constructeur
		
		//TODO définir les normes de l'image (notamment les couleurs)
		/**
		 * Étapes du parsing de l'image:
		 * 0 : Ouvrir le fichier .svg
		 * 1 : sélectionner les informations dans le calque 1 (balise g)
		 * 2 : créer toute les intersections représentées par des cercles <circle/> puis les Road représentées par des <path/> avec les méthodes
		 * parseIntersection() et parseRoad()
		 * 	   un path a une intersection de départ si son premier sommet se trouve dans la zone d'un cercle (intersection)
		 */
		public static GraphState parseGraphState(String fileName) {

			/* La documentation du parser qu'on va utiliser est en ligne:
			 * https://openclassrooms.com/courses/structurez-vos-donnees-avec-xml/dom-exemple-d-utilisation-en-java
			 */

			// La classe DocumentBuilderFacorty est utilisée pour parser le xml de l'image svg.
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder;
			
			//Attributs du futur GraphState
			ArrayList<AbstractIntersection> intersections = new ArrayList<AbstractIntersection>();
			ArrayList<Road> roads = new ArrayList<Road>();
			Stack<AbstractVehicle> vehicles = new Stack<AbstractVehicle>();
			
			System.out.println("<---   Graph parse   --->");

			try {
				// On crée le parser
				builder = factory.newDocumentBuilder();       
				final Document document= builder.parse(new File(fileName));
				//On sélectionne la racine
				final Element racine = document.getDocumentElement();
				final NodeList racineNoeuds = racine.getChildNodes();
				final int nbRacineNoeuds = racineNoeuds.getLength();


				for (int i = 0; i<nbRacineNoeuds; i++) {
					//On se place dans le premier calque
					if (racineNoeuds.item(i).getNodeName() == "g"){
						final NodeList gNoeuds = racineNoeuds.item(i).getChildNodes();

						// Parcourt et création des intersections
						for(int j = 0; j<gNoeuds.getLength(); j++ ){
							// On filtre les éléments qui sont des noeuds
							if(gNoeuds.item(j).getNodeType() == Node.ELEMENT_NODE){
								final Element geometricFigure = (Element) gNoeuds.item(j);
								// Cas d'une intersection
								if(geometricFigure.getNodeName() == "circle" ){
									intersections.add(parseIntersection(geometricFigure));
								}
							}
						}


						// Parcourt des Road
						// Toutes les intersections doivent être instanciées avant la création des Road
						for(int j = 0; j<gNoeuds.getLength(); j++ ){
							// On filtre les éléments qui sont des noeuds
							if(gNoeuds.item(j).getNodeType() == Node.ELEMENT_NODE){
								final Element geometricFigure = (Element) gNoeuds.item(j);
								if(geometricFigure.getNodeName() == "path" ){
									roads.add(parseRoad(geometricFigure, intersections));
								}
							}
						}
						
						// Parcourt des Vehicule
						// Toutes les intersections et les Road doivent être instanciées avant la création des véhicule
						for(int j = 0; j<gNoeuds.getLength(); j++ ){
							// On filtre les éléments qui sont des noeuds
							if(gNoeuds.item(j).getNodeType() == Node.ELEMENT_NODE){
								final Element geometricFigure = (Element) gNoeuds.item(j);
								if(geometricFigure.getNodeName() == "rect" ){
									vehicles.add(parseVehicle(geometricFigure, intersections));
								}
							}
						}
					}
				}
			}
			catch (final ParserConfigurationException e) {
				e.printStackTrace();
			}
			catch (final SAXException e) {
				e.printStackTrace();
			}
			catch (final IOException e) {
				e.printStackTrace();
			}
			
			GraphState graphState = new GraphState(intersections, roads, vehicles);
			System.out.println(graphState);
			return graphState;
			//this.costsMatrix = Cost.floydWarshall(this);
		}
		
		private static AbstractIntersection parseIntersection(Element geometricFigure) throws IllegalArgumentException{
			if(geometricFigure.getNodeName() == "circle" ){
				CartesianCoordinate center = new CartesianCoordinate(Float.parseFloat(geometricFigure.getAttribute("cx")),
						Float.parseFloat(geometricFigure.getAttribute("cy")));
				Float radius = Float.parseFloat(geometricFigure.getAttribute("r"));
				AbstractIntersection parseIntersection = new AbstractIntersection(center, radius);
				System.out.println("Intersection " + parseIntersection.identifier + " parsed: " + parseIntersection);
				return parseIntersection;
			} else {
				throw new IllegalArgumentException("Not a circle");
			}

		}
		
		private static Road parseRoad(Element geometricFigure, ArrayList<AbstractIntersection> intersections) throws IllegalArgumentException{
			if(geometricFigure.getNodeName() != "path" ){
				throw new IllegalArgumentException(geometricFigure.getAttribute("id") + " is not a path");
			}
			
			CartesianCoordinate point1;
			CartesianCoordinate point2;
			AbstractIntersection origin = null;
			AbstractIntersection destination = null;
			double speed = 50;
			Cost cost;
			int lane = 1; // Attribut pas encore implémenté			
			
			
			//Détermination des attributs géométriques
			String d=geometricFigure.getAttribute("d");			// d contient les coordonnées du path
			// d est sous la forme d="m 395.9798,236.15895 6.06091,165.66502"
			String [] dParse = d.split("( |,)"); // Utilisation de regex
			if(dParse[0].equals("M") || dParse[0].equals("m")){
				if(dParse[0].equals("M")){
					point1 = new CartesianCoordinate(Float.parseFloat(dParse[1]), Float.parseFloat(dParse[2]));
					point2 = new CartesianCoordinate(Float.parseFloat(dParse[3]), Float.parseFloat(dParse[4]));
				} else { //dParse[0].equals("m")
					point1 = new CartesianCoordinate(Float.parseFloat(dParse[1]), Float.parseFloat(dParse[2]));
					point2 = new CartesianCoordinate(point1.x + Float.parseFloat(dParse[3]), point1.y + Float.parseFloat(dParse[4]));
				}
			}else{
				throw new IllegalArgumentException("Cannot parse " + geometricFigure.getAttribute("id"));
			}


			// Détermination de origin et destination
			Boolean isIntegrated = false;
			// Vérification de point1
			for(int i= 0; i<intersections.size(); i ++){
				if(point1.distanceFrom(intersections.get(i).center) <= intersections.get(i).radius){
					// On cherche alors pour le point2    
					for(int j=0; j<intersections.size(); j++){
						if(point2.distanceFrom(intersections.get(j).center) <= intersections.get(j).radius){
							// Le Road j a deux extrémités dans des intersections
							//On renseigne les informations de l'arête
							origin = intersections.get(i);
							destination = intersections.get(j);
							isIntegrated = true;
						}
					}
				}

			}
			if(! isIntegrated){
				throw new IllegalArgumentException(geometricFigure.getAttribute("id") + " has not been integrated to the graph");
			}
			
			// Détermination de speed
			if (geometricFigure.getAttribute("speed").isEmpty()) { // On utilise la valeur par défaut
				speed = 50000;
			} else {
				speed = Float.parseFloat(geometricFigure.getAttribute("speed"));
			}

			// Initialisation de cost
			// Le coût est pour l'instant la longueur de la route sur la vitesse urbaine
			cost = new Cost(CartesianCoordinate.distance(point1, point2)/speed);

			// Instanciation de Road
			Road parseRoad = new Road(point1, point2, origin, destination, speed, cost, lane);
			
			// Renseignement de origin d'un nouveau leavingRoad
			origin.getLeavingRoads().add(parseRoad);

			System.out.println("Road " + parseRoad.identifier + " parsed: " +
					parseRoad.point1 + "; " + parseRoad.point2);
			//this.roads.add(parseRoad);
			return parseRoad;
		}
		
		private static AbstractVehicle parseVehicle(Element geometricFigure, ArrayList<AbstractIntersection> intersections) throws IllegalArgumentException{
			if(geometricFigure.getNodeName() != "rect" ){
				throw new IllegalArgumentException(geometricFigure.getAttribute("id") + " is not a Vehicule");
			}
			
			// données géométriques
			// Les véhicules sont repérés par la position de l'angle gauche à l'avant
			CartesianCoordinate position;
			CartesianCoordinate destination;
			// données de position initiale
			Road initialRoad;
			double initialPosition;
			double initialDate;
			// données de position de la destination
			Road finalRoad;
			double finalPosition;
			
			position = new CartesianCoordinate(Float.parseFloat(geometricFigure.getAttribute("x")),
					Float.parseFloat(geometricFigure.getAttribute("y")));
			destination = new CartesianCoordinate(Float.parseFloat(geometricFigure.getAttribute("x_destination")),
					Float.parseFloat(geometricFigure.getAttribute("y_destination")));
			initialDate = Float.parseFloat(geometricFigure.getAttribute("initialDate"));
			
			// Projection de position et de destination sur le graph: on sélectionne le Road qui est le plus proche
			initialRoad = position.closestRoad(intersections);
			finalRoad = destination.closestRoad(intersections);
			
			// Détermination des coordonnées des points projettés
			CartesianCoordinate positionProjection = initialRoad.coordinateProjection(position);
			CartesianCoordinate finalProjection = initialRoad.coordinateProjection(destination);
			
			// Calcul de initialPosition et de finalPosition
			initialPosition = initialRoad.point1.distanceFrom(positionProjection);
			finalPosition = finalRoad.point1.distanceFrom(finalProjection);
			
			// finalDate=0. n'est pas significative
			Location vehiculeLocation = new Location(initialRoad, initialPosition, initialDate, finalRoad, finalPosition, 0.);
			return new AbstractVehicle(vehiculeLocation);
		}
		
		public static String getProjectLocation(){
			String projectLocation = new String();
			try
			{
				projectLocation = new File(".").getCanonicalPath();
			}
			catch (IOException e) {e.printStackTrace();};
			return projectLocation;
		}

}
