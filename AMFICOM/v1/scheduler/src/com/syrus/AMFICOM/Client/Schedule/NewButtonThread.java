package com.syrus.AMFICOM.Client.Schedule;

import java.awt.*;

public class NewButtonThread extends Thread {
	NewButton nb;
	Color col;
	boolean flag = true;

	public NewButtonThread(NewButton nb, Color col){
	this.nb = nb;
	this.col = col;
	}
	public void run()
	{
		while(true)
		{
			if (flag == true)
			{
				this.nb.setBackground(col);
				flag = false;
			}
			else if (flag == false)
			{
				this.nb.setBackground(Color.red);
				flag = true;
			}

			try
			{
				Thread.sleep(200);
			}
			catch(InterruptedException e){
			}
		}
	}
}

