package com.syrus.AMFICOM.Client.Map;

import javax.swing.JInternalFrame;

public class MapSchemeTreeFrame extends JInternalFrame
{
	public void setVisible(boolean isVisible)
	{
		super.setVisible(isVisible);
		getContentPane().getComponent(0).setVisible(isVisible);
	}
}