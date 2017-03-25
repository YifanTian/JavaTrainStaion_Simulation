import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

	/**
	 * Name          ID          netID
	 * Yifan Tian    78921267    yifant
	 * Fangjun Liu   000972140   fangjunl
	 */

/*
 *
 * The class Lab4TrainSimulation: 
 * the main application interface, 
 * implements the runnable. Here I also have the read and print function.
 * 
 */

public class Lab4TrainSimulation extends Applet implements Runnable		// the class for the lab4simulation, provide animation using Applet
{
	private TrainSystemManager manager;
	
//	private static int simulatedSecondRate = 100;
//	private static int totalSimulationTime = 1000;
	private static int simulatedSecondRate;
	private static int totalSimulationTime;
	private Thread t;
	
    private Train train1; 
    private Train train2; 
    private Train train3; 
    private Train train4; 
    private Train train5; 
    
    private Thread t1;
    private Thread t2;
    private Thread t3;
    private Thread t4;
    private Thread t5;
    
    private String trainmanager_text;
    private String train_text1;
    private String train_text2;
    private String train_text3;
    private String train_text4;
    private String train_text5;
    
    private int train1Location;
    private int train2Location;
    private int train3Location;
    private int train4Location;
    private int train5Location;
    
    private String arrival_text;
    private String simulationStatString;
    private int changeTime;
    
	ArrayList<ArrayList<PassengerArrival>> arrivalArray = new ArrayList<ArrayList<PassengerArrival>>();
	
	public Lab4TrainSimulation()
	{
		manager = new TrainSystemManager();  
		
		trainmanager_text = "manager1";
		train_text1 = "train1";
		train_text2 = "train2";
		train_text3 = "train3";
		train_text4 = "train4";
		train_text5 = "train5";
		
		arrival_text = "";
		changeTime = 0;
	}
	
	
    public void start()
    {
		readArrivalInfo();
		// 5 train threads
		train1 = new Train(0,manager,simulatedSecondRate);
		train2 = new Train(1,manager,simulatedSecondRate);
		train3 = new Train(2,manager,simulatedSecondRate);
		train4 = new Train(3,manager,simulatedSecondRate);
		train5 = new Train(4,manager,simulatedSecondRate);
		
		// update TrainSystemManager, fetch Passenger arrivals
		t1 = new Thread(train1);
		t1.setPriority(Thread.MAX_PRIORITY);
		t2 = new Thread(train2);
		t3 = new Thread(train3);
		t4 = new Thread(train4);
		t5 = new Thread(train5);
		
		updateTrainSystemManager(SimClock.getSimulatedTime(),arrivalArray);      // update TrainSystemManager by time		

		new SimClock(simulatedSecondRate);
		
		t = new Thread(this,"Animation");
		t.start();
    }
		
  public void run()
  {  
		File myOutFile = new File("../src/Log.txt"); //Constructs an instance of a file object for myFile.txt
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		
	  	try
		{
	  		PrintWriter pw = new PrintWriter(myOutFile);

		while(true)
		{
			System.out.println("Simulation Time: "+SimClock.getSimulatedTime());
			
			changeTime = (SimClock.getSimulatedTime()/5)*5;
			train_text1 = train1.trainString();
			train1Location = train1.getMovingState() == 0? (train1.getLocation())*(34*5):(train1Location+train1.getMovingState()*34)%850;
			train_text2 = train2.trainString();
			train2Location = train2.getMovingState() == 0? (train2.getLocation())*(34*5):(train2Location+train2.getMovingState()*34)%850;
			train_text3 = train3.trainString();
			train3Location = train3.getMovingState() == 0? (train3.getLocation())*(34*5):(train3Location+train3.getMovingState()*34)%850;
			train_text4 = train4.trainString();
			train4Location = train4.getMovingState() == 0? (train4.getLocation())*(34*5):(train4Location+train4.getMovingState()*34)%850;
			train_text5 = train5.trainString();
			train5Location = train5.getMovingState() == 0? (train5.getLocation())*(34*5):(train5Location+train5.getMovingState()*34)%850;
			
			arrival_text = arrivalString();
			updateTrainSystemManager(SimClock.getSimulatedTime(),arrivalArray);      // update TrainSystemManager by time		
			
			simulationStatString = manager.managerStatString();
			trainmanager_text = "Time:  " + SimClock.getSimulatedTime() + "\n" + manager.managerString();
			
			repaint();
			SimClock.tick();

			printTrainState(pw);
			
			if(SimClock.getSimulatedTime()>totalSimulationTime)
			{
				break;
			}
		}
			pw.close();
		}
	  		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	
    public void paint(Graphics g)
    {
        setBackground(Color.CYAN);
        Font currentFont = new Font("Impact", Font.BOLD, 22);
        g.setFont(currentFont);
        g.drawString(arrival_text, 100, 100);
        g.drawString(simulationStatString, 100, 150);
        g.drawString(trainmanager_text, 100, 200);
        
        g.setColor(Color.red);  
        g.drawString(train_text1, 100, 230);
        g.fill3DRect(330+train1Location,245,80,30,true); 
        g.fillOval(350+train1Location,245,75,29); 
        
        g.setColor(Color.magenta); 
        g.drawString(train_text2, 100, 300); 
        g.fill3DRect(330+train2Location,315,80,30,true); 
        g.fillOval(350+train2Location,315,75,29); 

        g.setColor(Color.yellow); 
        g.drawString(train_text3, 100, 370); 
        g.fill3DRect(330+train3Location,385,80,30,true); 
        g.fillOval(350+train3Location,385,75,29); 
        
        g.setColor(Color.blue); 
        g.drawString(train_text4, 100, 440);  
        g.fill3DRect(330+train4Location,455,80,30,true); 
        g.fillOval(350+train4Location,455,75,29);
        
        g.setColor(Color.white); 
        g.drawString(train_text5, 100, 510);  
        g.fill3DRect(330+train5Location,525,80,30,true); 
        g.fillOval(350+train5Location,525,75,29);
    }
	
	private void updateTrainSystemManager(int simulatedTime,
		ArrayList<ArrayList<PassengerArrival>> arrivalArray2)
	{
		for(int i = 0; i< 5; i++)
		{
			for(int j = 0; j < arrivalArray.get(i).size();j++)
			{
				if (arrivalArray.get(i).get(j).getExpectedTimeOfArrival() == SimClock.getSimulatedTime())
				{
					//update the train station state i: station, j: destination
					manager.getTrainStations()[i].addPassengers(arrivalArray.get(i).get(j).getDestinationTrainStation(),arrivalArray.get(i).get(j).getNumPassengers());
					arrivalArray.get(i).get(j).addExpectedTimeOfArrival(arrivalArray.get(i).get(j).getTimePeriod());
					//
					synchronized (manager)
					{
						manager.notifyAll();
					}
				}
			}	
		}
	}

	public void printTrainState(PrintWriter pw)
	{	
		
		if (!train_text1.equals(train1.trainString()))
		{
			pw.println(simulationStatString);
			pw.println("Simulation Time:  " + changeTime + "\n" + manager.managerString());
			pw.println(train1.trainString()); 
		}
		if (!train_text2.equals(train2.trainString()))
		{
			pw.println("Simulation Time: "+changeTime);
			pw.println(train2.trainString()); 
		}
		if (!train_text3.equals(train3.trainString()))
		{
			pw.println("Simulation Time: "+changeTime);
			pw.println(train3.trainString()); 
		}
		if (!train_text4.equals(train4.trainString()))
		{
			pw.println("Simulation Time: "+changeTime);
			pw.println(train4.trainString());
		}
		if (!train_text5.equals(train5.trainString()))
		{
			pw.println("Simulation Time: "+changeTime);
			pw.println(train5.trainString()); 
		}
	}
	
	public void readArrivalInfo()
	{
		int line;
		try{
			File myInFile = new File("../src/TrainConfig.txt");
			Scanner s = new Scanner(myInFile);
			s.useDelimiter("\n");
			String tempString = "";
			String[] temp;
			String[] info;
			line = 0;
			
			while (s.hasNext()) 
			{
			tempString = s.next();
			temp = tempString.split(";");
			if (line == 0)
			{
				totalSimulationTime = Integer.parseInt(temp[0]); 				// read the totalSimulationTime
			}
			if (line == 1)
			{  
				simulatedSecondRate = Integer.parseInt(temp[0]);				// read the simulatedSecondRate
			}
			if (line>1){
				ArrayList<PassengerArrival> PAarray = new ArrayList<PassengerArrival>();
				for (int i = 0; i< temp.length; i++)
				{
					info = temp[i].split(" ");
					PassengerArrival PA = new PassengerArrival();
					PA.setNumPassengers(Integer.parseInt(info[0]));
					PA.setDestinationTrainStation(Integer.parseInt(info[1]));
					PA.setTimePeriod(Integer.parseInt(info[2]));
					PA.setExpectedTimeOfArrival(Integer.parseInt(info[2]));
					//PA.setExpectedTimeOfArrival(0);
					PAarray.add(PA);  
					
				}
				arrivalArray.add(PAarray);
				}
				line++;
			}
			s.close(); //closes the scanner stream
				
		}  catch(FileNotFoundException e) {
			System.out.println("File not Found");
		}

	}
	
	public void printPassengersInfo()
	{
		for(int i = 0; i< arrivalArray.size(); i++)
		{
			for(int j = 0; j < arrivalArray.get(i).size(); j++)
			{
				arrivalArray.get(i).get(j).printInfo();
			}
		}
	}
	
	public String arrivalString()
	{
		String info = "Next batch of passengers:    ";
		for(int i = 0; i< arrivalArray.size(); i++)
		{
			for(int j = 0; j < arrivalArray.get(i).size(); j++)
			{
				info += arrivalArray.get(i).get(j).getExpectedTimeOfArrival()+": "+ i +"      ";
			}
		}
		return info;
	}
	
}
