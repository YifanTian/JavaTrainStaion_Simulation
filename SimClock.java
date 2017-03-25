
/*
 * 
 * The clock class using System.currentTimeMillis() to achieve tick
 * 
 */

public class SimClock				
{
	private static int simulatedTime;
	private static int secondRate;
	
	public SimClock(int simulatedSecondRate)
	{
		simulatedTime = 0;
		secondRate = simulatedSecondRate;
	}

	public void simClock()
	{
		simulatedTime = 0;
	}
	
	public static void tick()
	{
		long initialTime = System.currentTimeMillis();
		while(true)
		{
			if (System.currentTimeMillis() >= (initialTime+secondRate))
				break;
		}
		simulatedTime++;
	}

	public static int getSimulatedTime()
	{
		return simulatedTime;
	}

	public void setSimulatedTime(int simulatedTime)
	{
		SimClock.simulatedTime = simulatedTime;
	}
	
}
