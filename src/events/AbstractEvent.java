package events;

import java.util.Comparator;
import java.util.Stack;

import graph.Road;
import smartcars.AbstractVehicle;

public class AbstractEvent {

	// identificateur des instances, s'incrémente à chaque instanciation...
	private static int identificator = 0;
	// ... pour définir l'identifiant de l'évènement créée
	@SuppressWarnings("unused")
	private int identifier;
	// nature de l'évènement :
	// 0 -> EventVehicleStart
	// 1 -> EventWaitingOnRoad
	// 2 -> EventLeaveRoad
	// 3 -> EventEnterRoad
	// 4 -> EventVehicleEnd
	protected int nature;
	
	// véhicule qui subit l'évènement (intrinsèque au véhicule)
	private AbstractVehicle vehicle;
	// route qui voit l'évènement se dérouler
	protected Road road;
	// date de l'évènement
	private double date;
	// La date réelle a-t-elle été calculée ?
	private boolean trueDate = false;
	
	// si l'évènement fils est un EventWaitingOnRoad,
	// date réelle (après son calcul) de l'évènement EventLeavingRoad qui suit
	protected double leavingDate;
	// si l'évènement est un EventLeavingRoad,
	// évènement EventWaitingOnRoad qui précède
	protected AbstractEvent eventWaitingOnRoad;
	
	protected AbstractEvent(AbstractVehicle vehicle, Road road, double date)
	{
		this.identifier = identificator;
		identificator++;
		this.vehicle = vehicle;
		this.road = road;
		this.date = date+Math.random();
	}

	/**
	 * Classe utilisée à des fins comparatives dans des structures pré-conçues.
	 * Elle n'est plus utilisée car java.util.PriorityQueue ne vérifie l'ordre
	 * de ses éléments qu'à l'ajout d'un nouvel élément et non lors de la suppression
	 * d'un ancien élément. Or, la date (attribut à comparer) des évènements
	 * peut être changée une fois que ceux-ci sont dans la structure.
	 * @author cylla
	 *
	 */
	public static class Chronologic implements Comparator<AbstractEvent>
	{
		public int compare(AbstractEvent eventA, AbstractEvent eventB)
		{
			if(eventA.getDate()<eventB.getDate()) return -1;
			else return 1;
		}
	}
	
	/**
	 * Constitue la liste des évènements du véhicule, avec de fausses dates.
	 * Ne doit être appelée qu'après le calcul de Dijkstra sur le véhicule.
	 * @param vehicle
	 * @throws IllegalStateException
	 */
	public static void vehicleEvents(AbstractVehicle vehicle) throws IllegalStateException
	{
		Stack<AbstractEvent> tempEvents = new Stack<AbstractEvent>();
		vehicle.setTempPath();
		
		if(!vehicle.getPathCalculated())
		{
			throw new IllegalStateException("Dijkstra n'a pas été appliqué à ce véhicule.");
		}
		else
		{
			// *** EventVehicleStart ***
			AbstractEvent start = new EventVehicleStart(vehicle);
			tempEvents.add(start);
			
			AbstractEvent lastEventEnterRoad;
			
			while(true)
			{
				int lastEventNature = tempEvents.peek().nature;
				
				// *** EventWaitingOnRoad ***
				if(lastEventNature==0)
				{
					EventVehicleStart.nextEvent(tempEvents.peek(), tempEvents);
				}
				else
				{
					if(lastEventNature!=3)
					{
						throw new IllegalStateException("La boucle de calcul des évènements n'est pas cyclique.");
					}
					else
					{
						lastEventEnterRoad = tempEvents.peek();
						EventEnterRoad.nextEvent(lastEventEnterRoad, tempEvents);
					}
				}
				
				lastEventNature = tempEvents.peek().nature;
				// l'évènement EventVehicleEnd survient ;
				// comprend le cas où la destination est plus loin
				// et sur la même route que le point de départ
				if(lastEventNature==4)
				{
					System.out.println("\nLe véhicule "+vehicle.getIdentifier()+" a été acheminé avec succès à destination.\n");
					
					vehicle.setEvents(tempEvents);
					
					System.out.println("\nPile FIFO des évènements :");
					System.out.println(vehicle.getEvents());
					
					break;
				}
				else
				{
					// *** EventLeavingRoad ***
					AbstractEvent lastEventWaitingOnRoad = tempEvents.peek();
					EventWaitingOnRoad.nextEvent(lastEventWaitingOnRoad, tempEvents);
					
					// *** EventEnterRoad ***
					AbstractEvent lastEventLeaveRoad = tempEvents.peek();
					EventLeaveRoad.nextEvent(lastEventLeaveRoad, tempEvents);
				}
			}
		}
	}
	
	
	// *** Getters ***

	public int getNature() {
		return nature;
	}

	public AbstractVehicle getVehicle() {
		return vehicle;
	}

	public Road getRoad() {
		return road;
	}

	public double getDate() {
		return date;
	}

	public void setDate(double date) {
		this.date = date;
	}

	public boolean getTrueDate() {
		return trueDate;
	}

	public void isTrueDate(boolean trueDate) {
		this.trueDate = true;
	}

	public double getLeavingDate() {
		return leavingDate;
	}

	public AbstractEvent getEventWaitingOnRoad() {
		return eventWaitingOnRoad;
	}
	
}
