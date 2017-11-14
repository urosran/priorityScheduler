package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;

import java.util.LinkedList;

public class BasicTunnel extends Tunnel{
//keeping track of what is in the tunnel
	private LinkedList<Vehicle> vehicles;



	public BasicTunnel(String name) {
		super(name);
		this.vehicles = new LinkedList<>();
	}

	@Override
	public synchronized boolean tryToEnterInner(Vehicle vehicle) {
		//var counting how many cars are in the tunnel 
		int carNum = 0;
		
		if (vehicles != null){
			//looping trough the tunnel to check for what's inside the tunnel
			for (Vehicle v : vehicles) {
				//if there is a sled nothing else can enter				
				if (v instanceof Sled){
					return false;
				}
				//checking direction
				if ((vehicle.getDirection()!=v.getDirection())){
					return false;
				}
				//know how many cars are in the tunnel
				if (v instanceof Car){
					carNum++;
				}
				
			}
		}
		//no sled can enter if there are any cars in the tunnel 
		if (carNum>0 && vehicle instanceof Sled){
			return false;
		//or if the there are more than 3 cars in the tunnel
		}else if(carNum>=3){
			return false;
		}else {
			//enter the tunnel
			vehicles.add(vehicle);
			return true;
		}
	}

	@Override
	public synchronized void exitTunnelInner(Vehicle vehicle) {
		if (vehicles.size()!=0){
			vehicles.remove(vehicle);
		}
	}
	
}
