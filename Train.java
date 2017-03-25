import java.util.ArrayList;

/*
 * 
 * The train class, including train thread
 *  also define the creation of moving event.
 * 
 */

public class Train implements Runnable				
{
	
	private int trainID;
	private int currentTrainStation;			// The current train station the train is at
	private int numPassenger;					// The current number of passengers in the train.
	private int totalLoadedPassengers;			
	private int[] totalUnloadedPassengers;
	private ArrayList<TrainEvent> moveQueue;	//define the movement of a train and the anticipated time of arriving at a destination.
	private int[] passengerDestinations;	// ith element represents the number of current passengers who’s destination is the ith train station.
	private TrainSystemManager manager;
	
	private int destStation;
	private String currentState;
	private String oldState;
	
    private final static int loadingTime = 10;
    private final static int unloadingTime = 10;
    private final static int passingRate = 5;
	
	private int movingState;
	private int secondRate;
	
	
	public Train(int newtrainID,TrainSystemManager newmanager,int simulatedSecondRate)
	{
		trainID = newtrainID;
		manager = newmanager;
		numPassenger = 0;
		currentTrainStation = 0;
		secondRate = simulatedSecondRate;
		destStation = 0;
		movingState = 0;
		currentState = "";
		oldState = "";
		passengerDestinations = new int[5];
		totalUnloadedPassengers = new int[5];
		moveQueue = new ArrayList<TrainEvent>();
	}

	public void run() 
	{
		boolean ready = true;
		long waketime = 0;
		int passtime = 0;
		currentState = "";
		while(true) {
			
			if (System.currentTimeMillis() >= waketime)
				ready = true;
				
			if ( (sumPassengers(manager.getTrainStations()[currentTrainStation].getPassengerRequests()) == 0 || 
			(manager.getTrainStations()[currentTrainStation].getApproachingtrain() >= 0 & manager.getTrainStations()[currentTrainStation].getApproachingtrain() != trainID) )    
				&& ready && moveQueue.isEmpty()) 	                    // currentTrainStation is a empty station or currentTrainStation has train already
					{													
					
						movingState = 0;																// initially at rest
						synchronized (manager)
						{
							destStation = manager.getRequestStation(currentTrainStation,trainID);		// no two trains can call this at the same time
							if (destStation!=currentTrainStation)
								manager.getTrainStations()[destStation].setApproachingtrain(trainID);				// approaching that station
							
//							try
//							{
//								manager.wait();
//							}
//							catch (InterruptedException e)
//							{
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
						}
						System.out.println(movingState);
											
						passtime = Math.abs(destStation-currentTrainStation)*passingRate;
						movingState = destStation==currentTrainStation? 0:(destStation-currentTrainStation)>0? 1:-1;
							
						if (manager.getTrainStations()[currentTrainStation].getApproachingtrain() == this.trainID & currentTrainStation != destStation)
							manager.getTrainStations()[currentTrainStation].setApproachingtrain(-1);				// leave this station
							
						currentState = "travel to " + destStation + " from "+currentTrainStation;
						
						currentTrainStation = destStation;	
						ready = false;
						waketime = System.currentTimeMillis() + passtime*secondRate;
					}	
			
			else if(ready) 			// arriving a station having passengers
				{	
					if (moveQueue.isEmpty()) {			// currentTrainStation is not a empty station, build missions
						
						movingState = 0;
						manager.getTrainStations()[currentTrainStation].setApproachingtrain(trainID); 
						int[] pDestinations = new int[5];						// a local variable for waiting passengers
						passengerDestinations = new int[5];
						
						for(int i = 0; i< 5; i++)
							pDestinations[i] = manager.getTrainStations()[currentTrainStation].getPassengerRequests()[i];	
						
						synchronized (manager)
						{
							destStation = manager.pickupPassengers(currentTrainStation);
							if(destStation != currentTrainStation) 				// if destStation change, then create moveEvent
							{
								manager.getTrainStations()[currentTrainStation].getPassengerRequests()[destStation] = 0;
								if (manager.getTrainStations()[currentTrainStation].getApproachingtrain() == this.trainID)
									manager.getTrainStations()[currentTrainStation].setApproachingtrain(-1);				// loading one bunch of passengers and leave this station
	
								currentState = "loading passengers to "+ destStation + " at "+ currentTrainStation;
								passengerDestinations[destStation] = pDestinations[destStation];
								
								totalLoadedPassengers += passengerDestinations[destStation];				//Keep track of the total number of passengers that entered the train throughout the simulation.
								totalUnloadedPassengers[destStation] += passengerDestinations[destStation];	//Keep track of the total number of passengers that exited this train on a specific train station.
								manager.getTrainStations()[currentTrainStation].addTotal(passengerDestinations[destStation]);		// Keep track of the total number of passengers requesting a trains ride on this station.
								manager.getTrainStations()[destStation].addDestTrainTotal(trainID, passengerDestinations[destStation]);  // Keep track of the total number of passengers that exited a train on the train station.
								
								numPassenger += pDestinations[destStation];
								moveQueue.add(new TrainEvent(destStation,loadingTime));
							}
						}
						
						ready = false;
						waketime = System.currentTimeMillis() + loadingTime*secondRate;						// the time on loading passengers
				}
				
				else {
					
					deliveryPeriod();				// deliver passengers
				
				}
			}
		}
	}		
	
	public void deliveryPeriod()				// the period on delivery, including travel and unloading
	{
		int len = moveQueue.size();
		destStation = moveQueue.get(len-1).getDestination();
		int oldStation = currentTrainStation;
		int passtime = 0;
		long waketime = 0;
		
		for(int j = 0; j<len; j++)													// travel to destinations in moveQueue
		{
			
			TrainEvent currentEvent = moveQueue.remove(0);
			oldStation = currentTrainStation;
			
			passtime = Math.abs(currentEvent.getDestination()-oldStation)*passingRate;
			movingState = (currentEvent.getDestination()-oldStation)>0? 1:-1;
			currentState = "travel to " + currentEvent.getDestination() + " from "+oldStation;
			
			waketime = System.currentTimeMillis() + passtime*secondRate;
			while(true)
			{
				if(System.currentTimeMillis()>=waketime)
					break;
			}

			movingState = 0;
			currentTrainStation = currentEvent.getDestination();
			currentState = "unloading at " + currentTrainStation;
			numPassenger -= passengerDestinations[currentEvent.getDestination()];	// unload passenger
			passengerDestinations[currentTrainStation] = 0;
			
			waketime = System.currentTimeMillis() + unloadingTime*secondRate;
			while(true)
			{
				if(System.currentTimeMillis()>=waketime)
					break;
			}
		}
	}
	
	
	public int sumPassengers(int[] stationPassengers)
	{
		int sum = 0;
		for(int i = 0; i< stationPassengers.length;i++)
		{
			sum += stationPassengers[i];
		}
		return sum;
	}
	
	public void trainInfo()
	{
		System.out.println("----------------Train "+ trainID + " Infomation:--------");
		System.out.println("CurrentTrainStation: "+currentTrainStation);
		System.out.println("NumPassenger: "+numPassenger);
		
		for(int i = 0; i<5; i++)
		{
			System.out.println("Destinations "+ i + " have numPassenger: "+passengerDestinations[i]);
		}	
		System.out.println("-------------------------------------------------------");
		
	}
	
	public boolean stateChange()
	{
		return currentState != oldState?true:false;
	}
	
	
	public String trainString()
	{
		String info = "Train:    "+ trainID;
		try{
			info += ("    Current:  "+currentTrainStation+"   ");
			info += ("NumP:   "+numPassenger+"   [");
			for(int i = 0; i<5; i++)
			{
				info += ("    " + passengerDestinations[i]+"  ");
			}	
			info += "]     ";
			info += currentState;
			info += "    TotalP:  "+totalLoadedPassengers;
			info += "     PFromStations:  ";
			for(int i = 0; i<5; i++)
			{
				info += ("  " + totalUnloadedPassengers[i]+"  ");
			}
		}
		catch(NullPointerException e) {
			info = "Train:    "+ trainID;
		}
		return info;
	}

	public int getLocation()
	{
		return currentTrainStation;
	}

	public int getMovingState()
	{
		return movingState;
	}

	public void setMovingState(int movingState)
	{
		this.movingState = movingState;
	}
	
}
