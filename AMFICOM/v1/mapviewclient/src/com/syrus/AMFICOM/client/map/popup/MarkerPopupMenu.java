package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;

import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkerController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;

public final class MarkerPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	
	private Marker marker;
	
	private static MarkerPopupMenu instance = new MarkerPopupMenu();

	private MarkerPopupMenu()
	{
		super();
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static MarkerPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setElement(Object me)
	{
		this.marker = (Marker)me;
	}

	private void jbInit()
	{
		removeMenuItem.setText(LangModelMap.getString("Delete"));
		removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeMark();
				}
			});
		this.add(removeMenuItem);
	}

	private void removeMark()
	{
		super.removeMapElement(marker);
		
		MarkerController mc = (MarkerController)getLogicalNetLayer().getMapViewController().getController(marker);
		mc.notifyMarkerDeleted(marker);

		getLogicalNetLayer().repaint(false);
	}
}
