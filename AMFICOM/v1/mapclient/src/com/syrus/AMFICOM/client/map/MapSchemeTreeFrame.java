package com.syrus.AMFICOM.Client.Map;

import javax.swing.JInternalFrame;
import java.awt.Component;

public class MapSchemeTreeFrame extends JInternalFrame
{
	public void setVisible(boolean isVisible)
	{
		super.setVisible(isVisible);
		try
		{
			Component comp = getContentPane().getComponent(0);
			if(comp != null)
				comp.setVisible(isVisible);
		}
		catch(Exception e)
		{
			
		}
	}
}