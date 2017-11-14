package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.jws.soap.SOAPBinding;
import javax.lang.model.element.VariableElement;
import javax.swing.text.AbstractDocument.LeafElement;

public class PriorityScheduler extends Tunnel{

	private PriorityQueue <Vehicle> waitingRoom = new PriorityQueue<Vehicle>();
	public ReentrantLock lock = new ReentrantLock();
	private HashSet <Tunnel> tunnels = new HashSet <Tunnel>();
	private ArrayList<Integer> prioritySemaphore = new ArrayList<>();
	private ArrayList<Integer> fullTunnelSemaphore = new ArrayList<>();	
	
	private final Condition notFull;  
	
	
	
	public PriorityScheduler(String name, Collection<Tunnel> tunnels) {
		super(name);
		this.tunnels = (HashSet)tunnels;
		
	}

	@Override
	public boolean tryToEnterInner(Vehicle vehicle) throws InterruptedException {
		//when a vehicle tries to enter the tunnel it's placed in the waiting room 
		//so that we can determine its priority 
		lock.lock();
		notFull = lock.newCondition();
		boolean vehicleEntered = false;
		waitingRoom.add(vehicle);
		
		while(vehicleEntered==false) {
		Iterator<Tunnel> tIterator = tunnels.iterator();
		Tunnel tunnel = tIterator.next(); 
		
		while(tIterator.hasNext()) {
			if (waitingRoom.peek().getPriority()<= vehicle.getPriority()) {
				if (tunnel.tryToEnterInner(vehicle)) {
					vehicleEntered = true; 
					return true;
				}else {
					notFull.await();
					vehicleEntered = true; 
				}	
				
			}else {
				notFull.await();
				vehicleEntered = true; 
			}
		} 
	}
		lock.unlock();
		return false; 
}
	
		
		
	

	@Override
	public void exitTunnelInner(Vehicle vehicle) {
		// TODO Auto-generated method stub
		
	}
	
}
