package com.syrus.AMFICOM.Client.General.UI;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public abstract class WindowArranger
{
	protected JFrame mainframe;

	public WindowArranger(JFrame mainframe)
	{
		this.mainframe = mainframe;
	}

	public abstract void arrange();

	public static void normalize(JInternalFrame f)
	{
		setIconified(f, false);
		setMaximized(f, false);
	}

	public static void setIconified(JInternalFrame f, boolean b)
	{
		if((!b && f.isIcon()) || (b && !f.isIcon()))
		{
			try
			{
				f.setIcon(b);
			}
			catch (java.beans.PropertyVetoException pve)
			{
				pve.printStackTrace();
			}
		}
	}

	public static void setMaximized(JInternalFrame f, boolean b)
	{
		if((!b && f.isMaximum()) || (b && !f.isMaximum()))
		{
			try
			{
				f.setMaximum(b);
			}
			catch (java.beans.PropertyVetoException pve)
			{
				pve.printStackTrace();
			}
		}
	}
}
