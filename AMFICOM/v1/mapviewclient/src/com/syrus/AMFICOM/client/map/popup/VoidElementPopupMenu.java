package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.VoidElement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class VoidElementPopupMenu extends MapPopupMenu 
{

	private JMenuItem mapPropertiesMenuItem = new JMenuItem();
	private JMenuItem mapViewPropertiesMenuItem = new JMenuItem();
	private JMenuItem addSiteMenuItem = new JMenuItem();

	private static VoidElementPopupMenu instance = new VoidElementPopupMenu();

	private VoidElementPopupMenu()
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
	
	public static VoidElementPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setElement(Object me)
	{
	}
	
	private void jbInit()
	{
		mapPropertiesMenuItem.setText(LangModelMap.getString("MapProperties"));
		mapPropertiesMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showMapProperties();
				}
			});
		mapViewPropertiesMenuItem.setText(LangModelMap.getString("MapViewProperties"));
		mapViewPropertiesMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showMapViewProperties();
				}
			});
		addSiteMenuItem.setText(LangModelMap.getString("AddSite"));
		addSiteMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addSite();
				}
			});
		this.add(addSiteMenuItem);
		this.addSeparator();
		this.add(mapPropertiesMenuItem);
		this.add(mapViewPropertiesMenuItem);
	}

	private void showMapProperties()
	{
		super.showProperties(getLogicalNetLayer().getMapView().getMap());
	}

	private void showMapViewProperties()
	{
		super.showProperties(getLogicalNetLayer().getMapView());
	}

	private void addSite()
	{
		SiteNodeType proto = super.selectNodeProto();
		
		if(proto != null)
		{
			CreateSiteCommandAtomic command = new CreateSiteCommandAtomic(proto, point);
			command.setLogicalNetLayer(logicalNetLayer);
			getLogicalNetLayer().getCommandList().add(command);
			getLogicalNetLayer().getCommandList().execute();

			getLogicalNetLayer().repaint(false);
		}
	}
}
