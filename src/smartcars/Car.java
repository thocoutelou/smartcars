package smartcars;

import resources.Location;

/**
 * Jolie petite voiture.
 * Exemple d'extension d'AbstractVehicle.
 * @author cylla
 *
 */
public class Car extends AbstractVehicle {

	public Car(Location location)
	{
		super(location);
		length = 4.;
	}

}
