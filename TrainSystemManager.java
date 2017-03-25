
/*
 * The TrainSystemManager class, having the container of train stations.
 * Also having the getRequestStation and pickupPassengers functions to 
 * let the trains work.
 * 
 */

public class TrainSystemManager		// contains an array of stations and some functions for train to use with Synchronizationed access
{
	private TrainStation[] trainStations;
	boolean printTag;
	
	public TrainSystemManager()
	{
		trainStations = new TrainStation[5];
		for(int i = 0; i<5;i++)
		{
			trainStations[i] = new TrainStation();
		}
		printTag = false;
	}
	
	public synchronized TrainStation[] getTrainStations() 	
	{
		return trainStations;
	}
	

	synchronized public int getRequestStation(int station, int trainID)			// get the station to go
	{
			int destination = station;
			for(int i = 0; i<5; i++)			//  find one destination
			{
				int[] passengers = trainStations[i].getPassengerRequests();
				if (sumPassengers(passengers) != 0 && trainStations[i].getApproachingtrain() < 0)	// use manager and approaching train
				{
					destination = i;
					break;
				}
			}
			return destination;
	}
	
	synchronized public int pickupPassengers(int currentTrainStation)		// use Synchronizationed access
	{
		int destStation = currentTrainStation;
		int[] pDestinations = new int[5];						// a local variable for waiting passengers
		for(int i = 0; i< 5; i++)
			pDestinations[i] = trainStations[currentTrainStation].getPassengerRequests()[i];	
		
		for(int i = currentTrainStation; i<= 4; i++)								// search by distance, up 
		{
			if(pDestinations[i] > 0)
			{
				destStation = i;
				break;
			}
		}
		
		if (destStation == currentTrainStation)
		{
			for(int i = currentTrainStation; i>= 0; i--)							// search by distance, dn
			{
				if(pDestinations[i] > 0)
				{
					destStation = i;
					break;
				}
			}
		}
		return destStation;
	}

	public void printinfo()
	{
		System.out.println("============ Stations Information ==========");
		for(int i = 0; i<5; i++)
		{
			System.out.println("station "+i+": "+trainStations[i].getApproachingtrain());
			for(int j = 0; j<5; j++)
			{	
				System.out.print(trainStations[i].getPassengerRequests()[j]+" ");
			}
			System.out.print("\n");
		}
		System.out.println("========================================");
	}
	
	
	public String managerStatString()			// print statistics of manager
	{
		
		String info = "Total Passengers through S: ";
	
		for(int i = 0; i<5; i++)
		{
			info += ("  '"+trainStations[i].getTotalPassengers()+"'   ");
			for(int j = 0; j<5; j++)
			{	
				info += trainStations[i].gettotalTrainDest()[j]+"  ";
			}
			info += "         ";
		}
		
		return info;
	}
	
	public String managerString()			// print state of manager
	{
		
		String info =  "        Stations:       ";
		for(int i = 0; i<5; i++)
		{
			info += ("  '"+trainStations[i].getApproachingtrain()+"'   ");
			for(int j = 0; j<5; j++)
			{	
				info += trainStations[i].getPassengerRequests()[j]+"  ";
			}
			info += "         ";
		}
		return info;
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

	public void setPrintTag(boolean tag)
	{
		printTag = tag;
	}
	
	public boolean getPrintTag()
	{
		return printTag;
	}


	
}
