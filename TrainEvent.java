
/*
 * 
 * The trainEvent class, having the destination and expectedArrival data.
 * 
 */

public class TrainEvent			
{
	private int destination;
	private int expectedArrival;
	
	
	public TrainEvent()
	{
		setDestination(-1);
		setExpectedArrival(0);
	}

	public TrainEvent(int station,int timePeriod)
	{
		setDestination(station);
		setExpectedArrival(timePeriod);
	}

	public int getDestination()
	{
		return destination;
	}


	public void setDestination(int destination)
	{
		this.destination = destination;
	}


	public int getExpectedArrival()
	{
		return expectedArrival;
	}


	public void setExpectedArrival(int expectedArrival)
	{
		this.expectedArrival = expectedArrival;
	}
	
}
