package com.syrus.AMFICOM.Client.General.UI;

import javax.swing.JButton;

public class ButtonRenderer 
		extends JButton 
		implements PropertyRenderer
{
	public ButtonRenderer()
	{
	}

	public ButtonRenderer(String s)
	{
		super(s);
	}
	
	public void setSelected(Object obj)
	{
		setText((obj == null) ? "" : obj.toString());
	}

}
