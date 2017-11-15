package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;
import cs131.pa2.Abstract.Log.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.jws.soap.SOAPBinding;
import javax.lang.model.element.VariableElement;
import javax.swing.plaf.metal.MetalIconFactory.PaletteCloseIcon;
import javax.swing.text.AbstractDocument.LeafElement;

public class PriorityScheduler extends Tunnel{
	
	private final PriorityQueue <Vehicle> waitingRoom = new PriorityQueue<Vehicle>((x, y) -> Integer.compare(y.getPriority(), x.getPriority()));
	private final ReentrantLock lock = new ReentrantLock();
	private final Collection <Tunnel> tunnels;


	private final HashMap<Vehicle, Tunnel> tunnelMap = new HashMap<>();

	private final Condition tunnelIsfull = lock.newCondition();
 


	public PriorityScheduler(String name, Collection<Tunnel> tunnels, Log log) {
		super(name, log);
		this.tunnels = tunnels;	
	}

	@Override
	public boolean tryToEnterInner(Vehicle vehicle) throws InterruptedException {
		//when a vehicle tries to enter the tunnel it's placed in the waiting room 
		//so that we can determine its priority 
		lock.lock();
		try {
			//have a boolean to track if the car has entered the tunnel or not
			boolean vehicleEntered = false;
			waitingRoom.add(vehicle);
				//while not in the tunnel
			while(vehicleEntered==false) {
					//check if the vehicle is the top priority if it is
				if (waitingRoom.peek().getPriority()<=vehicle.getPriority()) {
					Iterator<Tunnel> tIterator = tunnels.iterator();
					//try to find an empty tunnel for it
					while(tIterator.hasNext()) {
						Tunnel tunnel = tIterator.next();
						if (tunnel.tryToEnterInner(vehicle)) {
							waitingRoom.remove(vehicle);
							//if the vehicle entered remove it from the waiting queue  
							vehicleEntered = true;
							//track what vehicle goes where
							tunnelMap.put(vehicle,tunnel);
							return true;
						}else {
							tunnelIsfull.await();
							vehicleEntered = false; 
						}	
					}
				}else {
					//if the tunnel is full or if the vehichle is not the highest priority go to sleep
					tunnelIsfull.await();
					vehicleEntered = false;
				}  
			}
		//if the vehicle has finished unlock the lock
		}finally {
			lock.unlock();
		}
		return false;
	}
	@Override
	public void exitTunnelInner(Vehicle vehicle) {
		lock.lock();
		//obtain a tunnel that the vehicle is in
		Tunnel tunnel = tunnelMap.get(vehicle);
		tunnel.exitTunnelInner(vehicle);
		//remove the vehicle for that tunnel
		tunnelMap.remove(vehicle);
		//wake up all other threads
		tunnelIsfull.signalAll();

		lock.unlock();

	}

}
