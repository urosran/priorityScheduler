package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

import javax.jws.soap.SOAPBinding;

public class PriorityScheduler extends Tunnel{

	private HashSet <Vehicle> waitingRoom = new HashSet<Vehicle>();
	public ReentrantLock lock = new ReentrantLock();
	private HashSet <Tunnel> tunnels = new HashSet <Tunnel>();
	private ArrayList<Integer> prioritySemaphore = new ArrayList<>();
	private ArrayList<Integer> fullTunnelSemaphore = new ArrayList<>();	
	
	public PriorityScheduler(String name, Collection<Tunnel> tunnels) {
		super(name);
		this.tunnels = (HashSet)tunnels;
		
	}

	@Override
	public boolean tryToEnterInner(Vehicle vehicle) throws InterruptedException {
		//when a vehicle tries to enter the tunnel it's placed in the waiting room 
		//so that we can determine its priority 
		waitingRoom.add(vehicle);
		//iterator for the waiting room
		Iterator<Vehicle> it = waitingRoom.iterator(); 
		int prioritySum = 0;
		//true false semaphore displaying if there is a  vehicle of higher priority in the waitingroom
		
		while (it.hasNext()) {
			Vehicle waitVehicle = it.next();
			if((vehicle.getPriority() > waitVehicle.getPriority())) {
				prioritySemaphore.add(1);
				prioritySum++;
			}else {
				prioritySemaphore.add(0);
			}
		}
		
		if (!lock.isLocked() && prioritySemaphore.size()>=prioritySum) {
			// lock the lock and have the car try and enter
			lock.lock();
			waitingRoom.remove(vehicle);
			return true;
		} else {
			// have the car wait
			vehicle.wait();
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void exitTunnelInner(Vehicle vehicle) {
		// TODO Auto-generated method stub
		
	}
	
}
