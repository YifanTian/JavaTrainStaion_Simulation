

/*
 * 
 * The pssengerArrival class, contans the data of PassengerArrival.
 * 
 */

public class PassengerArrival			
{
	private int numPassengers;
	private int destinationTrainStation;
	private int timePeriod;
	private int expectedTimeOfArrival;
	
	public PassengerArrival()
	{
		numPassengers = 0;
		destinationTrainStation = -1;
		timePeriod = 0;
		expectedTimeOfArrival = 0;
	}
	
	
	public int getNumPassengers()
	{
		return numPassengers;
	}
	public void setNumPassengers(int numPassengers)
	{
		this.numPassengers = numPassengers;
	}
	public int getDestinationTrainStation()
	{
		return destinationTrainStation;
	}
	public void setDestinationTrainStation(int destinationTrainStation)
	{
		this.destinationTrainStation = destinationTrainStation;
	}
	public int getTimePeriod()
	{
		return timePeriod;
	}
	public void setTimePeriod(int timePeriod)
	{
		this.timePeriod = timePeriod;
	}
	public int getExpectedTimeOfArrival()
	{
		return expectedTimeOfArrival;
	}
	public void setExpectedTimeOfArrival(int expectedTimeOfArrival)
	{
		this.expectedTimeOfArrival = expectedTimeOfArrival;
	}


	public void addExpectedTimeOfArrival(int timePeriod2)
	{
		this.expectedTimeOfArrival += timePeriod2;
		
	}

	public void printInfo()
	{
		System.out.println("numPassengers: "+numPassengers);
		System.out.println("DestinationTrainStation: "+destinationTrainStation);
		System.out.println("timePeriod: "+timePeriod);
		System.out.println("expectedTimeOfArrival: "+expectedTimeOfArrival);
		System.out.println();
	}
	
	
}
