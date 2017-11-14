package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityScheduler extends Tunnel{

	private HashSet waitingRoom = new HashSet();
	public ReentrantLock lock = new ReentrantLock();

	public PriorityScheduler(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean tryToEnterInner(Vehicle vehicle) {
		waitingRoom.add(vehicle);

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
