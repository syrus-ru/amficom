package com.syrus.AMFICOM.Client.General.UI;

import javax.swing.JInternalFrame;

public class ObjectResourceNavigatorFrame extends JInternalFrame 
{
	public ObjectResourceNavigatorFrame()
	{
		super();
	}

	public ObjectResourceNavigatorFrame(String title)
	{
		super(title);
	}

	public void doDefaultCloseAction()
	{
		if (isMaximum())
		try
		{
			setMaximum(false);
		}
		catch (java.beans.PropertyVetoException ex)
		{
			ex.printStackTrace();
		}
		super.doDefaultCloseAction();
    }
}