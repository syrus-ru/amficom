package com.syrus.AMFICOM.Client.Schedule;



public class CurrentTime extends Thread {
	TestPlan tp;

	public CurrentTime(TestPlan tp)
	{
		this.tp = tp;
	}
	public void run()
	{
		while(true)
		{
			this.tp.repaint();
			try
			{
			sleep(1000);
			}
			catch(InterruptedException e){
			System.out.print("Thread panel");
			}
		}
	}
}

