package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;

import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

import javax.jws.soap.SOAPBinding;

public class PriorityScheduler extends Tunnel{

	private HashSet <Vehicle> waitingRoom = new HashSet<Vehicle>();
	public ReentrantLock lock = new ReentrantLock();
	private HashSet <String> tunnels = new HashSet <String>();
	private Object parent = new Object();
	
	
	public PriorityScheduler(String name) {
		super(name);
		// TODO get the copy of tunnels?
		tunnels.add(super.getName());
	}

	@Override
	public boolean tryToEnterInner(Vehicle vehicle) {
		waitingRoom.add(vehicle);
		Iterator iter = tunnels.iterator(); 
		while (it.hasNext()) {
			type type = (type) it.next();
			
		}
		if(vehicle.getPriority()<)
		if (!lock.isLocked()) {
			// lock the lock and have the car try and enter
		} else {
			// have the car wait
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void exitTunnelInner(Vehicle vehicle) {
		// TODO Auto-generated method stub
		
	}
	
}
