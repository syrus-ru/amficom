package com.syrus.AMFICOM.Client.Map;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;

public class MyMapToolTip extends JToolTip 
{
	LogicalNetLayer lnl;
	MultiRowToolTipUI bttui = new MultiRowToolTipUI();
	
	public MyMapToolTip(LogicalNetLayer lnl)
	{
		super();
		this.lnl = lnl;
		setUI(bttui);
	}
/*
	public String getTipText()
	{
		System.out.println("get tip text");
		if(lnl == null)
			return "";
		MapContext mc = lnl.getMapContext();
		MapElement me = mc.getCurrentMapElement(lnl.getCurrentPoint());

		return me.getToolTipText();
	}
	
	public void setTipText(String text)
	{
		System.out.println("set tip text");
		if(lnl == null)
			return;
		MapContext mc = lnl.getMapContext();
		MapElement me = mc.getCurrentMapElement(lnl.getCurrentPoint());

		super.setToolTipText(me.getToolTipText());
	}
*/
}
