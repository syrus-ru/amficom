package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;

import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MarkerController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public final class MarkerPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	
	private MapMarker marker;
	
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
	
	public void setMapElement(MapElement me)
	{
		this.marker = (MapMarker )me;
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
		
		MarkerController mc = (MarkerController )getLogicalNetLayer().getMapViewController().getController(marker);
		mc.notifyMarkerDeleted(marker);

		getLogicalNetLayer().repaint(false);
	}
}
