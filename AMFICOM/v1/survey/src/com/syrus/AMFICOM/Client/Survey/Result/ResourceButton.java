package com.syrus.AMFICOM.Client.Survey.Result;

import javax.swing.*;
import java.awt.event.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.*;

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

