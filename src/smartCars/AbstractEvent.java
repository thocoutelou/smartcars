package smartCars;

import java.util.Comparator;
import java.util.Stack;

public class AbstractEvent {

	protected static int identificator = 0;
	protected int identifier;
	protected int nature;
	
	protected AbstractVehicle vehicle;
	protected Road road;
	public double date;
	
	protected AbstractEvent(AbstractVehicle vehicle, Road road, double date)
	{
		this.identifier = identificator;
		identificator++;
		this.vehicle = vehicle;
		this.road = road;
		this.date = date;
	}

	public static class EventComparator implements Comparator<AbstractEvent>
	{
		public int compare(AbstractEvent eventA, AbstractEvent eventB)
		{
			if(eventA.date<=eventB.date) return 1;
			else return -1;
		}
	}
	
	// ne doit être appelée qu'après le calcul de Dijkstra sur le véhicule
	public static void vehicleEvents(AbstractVehicle vehicle) throws IllegalStateException
	{
		Stack<AbstractEvent> tempEvents = new Stack<AbstractEvent>();
		Stack<Road> path = vehicle.getPathCopy();
		
		if(!vehicle.pathCalculated)
		{
			throw new IllegalStateException("Dijkstra n'a pas été appliqué à ce véhicule.");
		}
		else
		{
			// *** EventVehicleStart ***
			AbstractEvent start = new EventVehicleStart(vehicle, path);
			tempEvents.add(start);
			
			AbstractEvent lastEventEnterRoad;
			
			while(true)
			{
				int lastEventNature = tempEvents.peek().nature;
				
				// *** EventWaitingOnRoad ***
				if(lastEventNature==0)
				{
					EventVehicleStart.nextEvent(tempEvents.peek(), tempEvents, path);
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
						EventEnterRoad.nextEvent(lastEventEnterRoad, tempEvents, path);
					}
				}
				
				lastEventNature = tempEvents.peek().nature;
				// l'évènement EventVehicleEnd survient ;
				// comprend le cas où la destination est plus loin
				// et sur la même route que le point de départ
				if(lastEventNature==4)
				{
					System.out.println("\nLe véhicule "+vehicle.identifier+" a été acheminé avec succès à destination.\n");
					
					System.out.println("Pile LIFO des évènements :");
					System.out.println(tempEvents);
					
					AbstractVehicle.lifoToFifo(vehicle, tempEvents);
					
					System.out.println("\nPile FIFO des évènements :");
					System.out.println(vehicle.events);
					
					break;
				}
				else
				{
					// *** EventLeavingRoad ***
					AbstractEvent lastEventWaitingOnRoad = tempEvents.peek();
					EventWaitingOnRoad.nextEvent(lastEventWaitingOnRoad, tempEvents);
					
					// *** EventEnterRoad ***
					AbstractEvent lastEventLeaveRoad = tempEvents.peek();
					EventLeaveRoad.nextEvent(lastEventLeaveRoad, tempEvents, path);
				}
			}
		}
	}
	
}
