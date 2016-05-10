package smartCars;


public class AbstractEvent implements Comparable<AbstractEvent> {

	protected static int identificator = 0;
	protected int identifier;
	protected int nature;
	protected AbstractVehicle vehicle;
	protected Road road;
	protected double date;
	
	protected AbstractEvent(AbstractVehicle vehicle, Road road, double date)
	{
		this.identifier = identificator;
		identificator++;
		this.vehicle = vehicle;
		this.road = road;
		this.date = date;
	}

	public int compareTo(AbstractEvent event)
	{
		if(this.date>event.date) return 1;
		else return -1;
	}
	
	// ne doit être appelée qu'après le calcul de Dijkstra sur le véhicule
	public static void vehiclesEvents(AbstractVehicle vehicle) throws IllegalStateException
	{
		if(!vehicle.pathCalculated)
		{
			throw new IllegalStateException("Dijkstra n'a pas été appliqué à ce véhicule");
		}
		else
		{
			// *** EventVehicleStart ***
			Road road = vehicle.path.pop();
			if(!road.equals(vehicle.location.initialRoad))
			{
				throw new IllegalStateException("Itinéraire Dijkstra faux");
			}
			EventVehicleStart start = new EventVehicleStart(vehicle);
			// seul évènement à ajouter manuellement au véhicule
			// puisqu'il n'est pas construit par une méthode nextEvent
			vehicle.events.add(start);
			
			while(!road.equals(vehicle.location.finalRoad))
			{
				int lastEventNature = vehicle.events.peek().nature;
				
				// *** EventWaitingOnRoad ***
				if(lastEventNature==0) start.nextEvent();
				else
				{
					if(lastEventNature!=3)
					{
						throw new IllegalStateException("La boucle de calcul des évènements n'est pas cyclique.");
					}
					else
					{
						EventEnterRoad lastEventEnterRoad = (EventEnterRoad) vehicle.events.peek();
						lastEventEnterRoad.nextEvent();
					}
				}
				
				lastEventNature = vehicle.events.peek().nature;
				// l'évènement EventVehicleEnd survient ;
				// comprend le cas où la destination est plus loin
				// et sur la même route que le point de départ
				if(lastEventNature==4) return;
				else
				{
					// *** EventLeavingRoad ***
					EventWaitingOnRoad lastEventWaitingOnRoad = (EventWaitingOnRoad) vehicle.events.peek();
					lastEventWaitingOnRoad.nextEvent();
					
					// *** EventEnterRoad ***
					EventLeaveRoad lastEventLeaveRoad = (EventLeaveRoad) vehicle.events.peek();
					lastEventLeaveRoad.nextEvent();
				}
			}
		}
	}
	
}
