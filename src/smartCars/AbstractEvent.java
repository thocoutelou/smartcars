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
		if(this.date>event.date) return -1;
		else return 1;
	}
	
	// ne doit être appelée qu'après le calcul de Dijkstra sur le véhicule
	public static void vehicleEvents(AbstractVehicle vehicle) throws IllegalStateException
	{
		if(!vehicle.pathCalculated)
		{
			throw new IllegalStateException("Dijkstra n'a pas été appliqué à ce véhicule");
		}
		else
		{
			// *** EventVehicleStart ***
			AbstractEvent start = new EventVehicleStart(vehicle);
			vehicle.tempEvents.add(start);
			
			AbstractEvent lastEventEnterRoad;
			
			while(true)
			{
				int lastEventNature = vehicle.tempEvents.peek().nature;
				
				// *** EventWaitingOnRoad ***
				if(lastEventNature==0)
				{
					EventVehicleStart.nextEvent(vehicle.tempEvents.peek());
				}
				else
				{
					if(lastEventNature!=3)
					{
						throw new IllegalStateException("La boucle de calcul des évènements n'est pas cyclique.");
					}
					else
					{
						lastEventEnterRoad = vehicle.tempEvents.peek();
						EventEnterRoad.nextEvent(lastEventEnterRoad);
					}
				}
				
				lastEventNature = vehicle.tempEvents.peek().nature;
				// l'évènement EventVehicleEnd survient ;
				// comprend le cas où la destination est plus loin
				// et sur la même route que le point de départ
				if(lastEventNature==4)
				{
					System.out.println("\nLe véhicule a été acheminé avec succès à destination.");
					
					System.out.println("Pile LIFO des évènements :");
					System.out.println(vehicle.tempEvents);
					System.out.println("\nPath désormais vide du véhicule :");
					System.out.println(vehicle.path);
					
					fifoToLifo(vehicle);
					
					System.out.println("\nPile FIFO des évènements :");
					System.out.println(vehicle.events);
					
					break;
				}
				else
				{
					// *** EventLeavingRoad ***
					AbstractEvent lastEventWaitingOnRoad = vehicle.tempEvents.peek();
					EventWaitingOnRoad.nextEvent(lastEventWaitingOnRoad);
					
					// *** EventEnterRoad ***
					AbstractEvent lastEventLeaveRoad = vehicle.tempEvents.peek();
					EventLeaveRoad.nextEvent(lastEventLeaveRoad);
					
					lastEventEnterRoad = vehicle.tempEvents.peek();
				}
			}
		}
	}
	
	public static void fifoToLifo(AbstractVehicle vehicle)
	{
		while(!vehicle.tempEvents.isEmpty())
		{
			vehicle.events.add(vehicle.tempEvents.pop());
		}
	}
	
}
