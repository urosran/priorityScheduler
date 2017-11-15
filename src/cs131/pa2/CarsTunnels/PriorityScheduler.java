package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;

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

	private PriorityQueue <Vehicle> waitingRoom = new PriorityQueue<Vehicle>();
	public ReentrantLock lock = new ReentrantLock();
	private ArrayList <Tunnel> tunnels = new ArrayList <Tunnel>();
	
//	private ArrayList<Integer> prioritySemaphore = new ArrayList<>();
//	private ArrayList<Integer> fullTunnelSemaphore = new ArrayList<>();	
	
	private HashMap<Vehicle, Tunnel> tunnelMap = new HashMap<>();
	
	private Condition tunnelIsfull;
	
	
	
	public PriorityScheduler(String name, Collection<Tunnel> tunnels) {
		super(name);
		this.tunnels = (ArrayList<Tunnel>)tunnels;	
	}

	@Override
	public boolean tryToEnterInner(Vehicle vehicle) throws InterruptedException {
		//when a vehicle tries to enter the tunnel it's placed in the waiting room 
		//so that we can determine its priority 
		lock.lock();
//		tunnelIsfull = lock.newCondition();
		boolean vehicleEntered = false;
		waitingRoom.add(vehicle);
		
		while(vehicleEntered==false) {
			if (waitingRoom.peek().getPriority()<= vehicle.getPriority()) {
			Iterator<Tunnel> tIterator = tunnels.iterator();

			while(tIterator.hasNext()) {
				Tunnel tunnel = tIterator.next();
					if (tunnel.tryToEnterInner(vehicle)) {
						waitingRoom.poll();
						vehicleEntered = true;
						tunnelMap.put(vehicle,tunnel);
						return true;
					}else {
						tunnelIsfull.await();
						vehicleEntered = true; 
					}	
					
				}
				}else {
					tunnelIsfull.await();
					vehicleEntered = true;
			}  
		}
		lock.unlock();
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
		//
		tunnelIsfull.signalAll();
		
		lock.unlock();
		
	}
	
}
