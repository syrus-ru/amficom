package com.syrus.AMFICOM.server.load;

import javax.swing.*;
import javax.swing.border.*;
import oracle.jdeveloper.layout.*;

public class LoadMessageFrame_AboutBoxPanel1 extends JPanel
{
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel4 = new JLabel();

	Border border1 = new EtchedBorder();

	public LoadMessageFrame_AboutBoxPanel1()
	{
		try 
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		jLabel1.setText("AMFICOM server - load initial data");
		jLabel2.setText("Andrei Kroupennikov");
		jLabel3.setText("Copyright (c) Syrus Systems 2000");
		jLabel4.setText("Syrus Systems");
		this.setLayout(new XYLayout());
		this.setBorder(border1);
		this.add(jLabel1, new XYConstraints(5, 5, -1, -1));
		this.add(jLabel2, new XYConstraints(5, 15, -1, -1));
		this.add(jLabel3, new XYConstraints(5, 25, -1, -1));
		this.add(jLabel4, new XYConstraints(5, 35, -1, -1));
	}
}

 