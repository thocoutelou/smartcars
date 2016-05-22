package resources;

import events.AbstractEvent;
import events.EventWaitingOnRoad;
import graph.Road;
import problem.GraphState;
import smartcars.AbstractVehicle;

/**
 * Classe un peu fantaisiste qui implémente le temps virtuel.
 * Contient des méthodes de calcul de dates et de durées.
 * @author cylla
 *
 */
public class Time {
	
	// date courante
	public static double time;

	/**
	 * Calcule le temps requis pour se déplacer
	 * d'une certaine distance sur une certaine route.
	 * @param road
	 * @param distance
	 * @return durée d'un déplacement de distance sur road
	 */
	public static double duration(Road road, double distance)
	{
		return 3.6*distance/road.getSpeed();
	}
	
	/**
	 * Calcule la distance parcourue en une certaine durée sur une certaine route.
	 * @param road
	 * @param duration
	 * @return distance parcourue en duration sur road
	 */
	public static double distance(Road road, double duration)
	{
		return road.getSpeed()*duration/3.6;
	}
	
	/**
	 * Calcule des dates réelles des évènements.
	 * Méthode clé de résolution du problème.
	 * Requiert une liste de tous les évènements à survenir dans le graphe,
	 * de dates fausses pourvu que les évènements
	 * d'un même véhicule soient correctement ordonnés.
	 * @param graph
	 */
	public static synchronized void realDates(GraphState graph)
	{
		System.out.println("<---   Calcul des dates réelles   --->");
		// initialisation
		PriorityQueue eventsCopy = graph.getAllEventsCopy();
		graph.setVehiclesTempEvents();
		AbstractEvent event;
		AbstractEvent vehicleEvent;
		double difference;
		
		while(!eventsCopy.qisEmpty())
		{
			event = eventsCopy.qremove();
			vehicleEvent = event.getVehicle().getTempEvents().qremove();
			
			System.out.print(event+"   ");
			System.out.println(event.getDate());
			if(!event.equals(vehicleEvent))
			{
				event = eventsCopy.qremove();
				vehicleEvent = event.getVehicle().getTempEvents().qremove();
				throw new IllegalStateException("Les évènements ne sont pas retournés dans l'ordre.");
			}

			if(event.getNature()==1 & event.getTrueDate())
			{
				EventWaitingOnRoad.setLeavingDate(event);
				// la ligne suivante doit être exécutée
				// impérativement après le calcul de leavingDate
				event.getRoad().getEventsWaitingOnRoad().qadd(event);
			}
			else if(event.getNature()==1 & !event.getTrueDate())
			{
				difference = event.getDate();
				event.setDate(EventWaitingOnRoad.relativeDate(event));
				event.isTrueDate(true);
				System.out.print("Changement de date : "+event+"   ");
				System.out.println(event.getDate());
				difference = event.getDate()-difference;
				event.getVehicle().getTempEvents().qdates(difference);
				event.getVehicle().getTempEvents().qadd(event);
				eventsCopy.qadd(event);
			}
			else if(event.getNature()==2 & event.getTrueDate())
			{
				if(!event.getRoad().getEventsWaitingOnRoad().qremove().equals(event.getEventWaitingOnRoad()))
				{
					throw new IllegalStateException("La liste des EventWaitingOnRoad est corrompue.");
				}
			}
			else if(event.getNature()==2 & !event.getTrueDate())
			{
				difference = event.getEventWaitingOnRoad().getLeavingDate()-event.getDate();
				event.getVehicle().getTempEvents().qdates(difference);
				event.setDate(event.getEventWaitingOnRoad().getLeavingDate());
				event.isTrueDate(true);
				event.getVehicle().getTempEvents().qadd(event);
				eventsCopy.qadd(event);
			}
			else if(event.getNature()==4)
			{
				event.getVehicle().getLocation().finalDate=event.getDate();
			}
		}
		System.out.println();
	}

	public static double endingTime(GraphState graph) {
		double end = 0.;
		for(AbstractVehicle v : graph.getVehicles())
		{
			if(v.getLocation().finalDate>end)
			{
				end = v.getLocation().finalDate;
			}
		}
		return end;
	}
	
	
	/* Bonus : système de gestion des heures de pointe.

	// 0. correspond à minuit
	private final static double morningRushStart = 25200.; //7h
	private final static double morningRushEnd = 32400.; //9h
	private final static double eveningRushStart = 61200.; //17h
	private final static double eveningRushEnd = 68400.; //19h
	
	
	public static boolean rush()
	{
		double dayTime = Time.time % 86400.;
		boolean morningRush = (dayTime>morningRushStart)&(dayTime<morningRushEnd);
		boolean eveningRush = (dayTime>eveningRushStart)&(dayTime<eveningRushEnd);
		return morningRush|eveningRush;
	}
	
	*/

}
