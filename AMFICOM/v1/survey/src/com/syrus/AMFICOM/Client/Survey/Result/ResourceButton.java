package com.syrus.AMFICOM.Client.Survey.Result;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ResourceButton extends JButton implements ActionListener 
{
	DataSourceInterface dsi;
	
	public ResourceButton(DataSourceInterface dsi)
	{
		super();
		this.dsi = dsi;
		
		jbInit();
	}

	public void jbInit()
	{
		setText("Загрузить");
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
	}
}

