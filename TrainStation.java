
/*
 * The trainStation class, having data of passengers.
 * 
 */

public class TrainStation				
{
	
	private int[] totalTrainArrival; 
	private int[] totalTrainDest;			// passengers exited from specific trains
	
	private int totalPassengers;			// total passengers through this station
	
	private int[] passengerRequests;
	private int approachingtrain;			// used to cancel other trains
	
	public TrainStation()
	{
		passengerRequests = new int[5];					// 5 for current passengers to 5 stations	
		approachingtrain = -1;							// which train is coming
		
		totalTrainDest = new int[5];					// 5 for 5 trains, total 
		totalPassengers = 0;							// for total passengers
	}


	public int[] getPassengerRequests()			
	{
		return passengerRequests;
	}

	public void setPassengerRequests(int[] passengerRequests)
	{
		this.passengerRequests = passengerRequests;
	}
	
	
	public int getApproachingtrain()    
	{
		return approachingtrain;
	}

	public void setApproachingtrain(int approachingtrain)
	{
		this.approachingtrain = approachingtrain;
	}

	public void addPassengers(int destinationTrainStation, int numPassengers)
	{
		passengerRequests[destinationTrainStation] += numPassengers;
	}

	public void addTotal(int i)
	{
		setTotalPassengers(getTotalPassengers() + i);
	}

	public void addArrivalTrainTotal(int trainID, int i)
	{
		totalTrainArrival[trainID] += i;
	}

	public void addDestTrainTotal(int trainID, int i)
	{
		totalTrainDest[trainID] += i;
	}
	
	public int[] gettotalTrainDest()			
	{
		return totalTrainDest;
	}

	public int getTotalPassengers()
	{
		return totalPassengers;
	}

	public void setTotalPassengers(int totalPassengers)
	{
		this.totalPassengers = totalPassengers;
	}
	
}
