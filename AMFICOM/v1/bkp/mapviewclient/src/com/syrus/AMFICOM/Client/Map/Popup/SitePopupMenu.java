package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public final class SitePopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem propertiesMenuItem = new JMenuItem();
	
	private SiteNode site;

	private static SitePopupMenu instance = new SitePopupMenu();

	private SitePopupMenu()
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
	
	public static SitePopupMenu getInstance()
	{
		return instance;
	}
	
	public void setElement(Object me)
	{
		this.site = (SiteNode)me;
	}

	private void jbInit() 
	{
		removeMenuItem.setText(LangModelMap.getString("Delete"));
		removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeSite();
				}
			});
		propertiesMenuItem.setText(LangModelMap.getString("Properties"));
		propertiesMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showProperties();
				}
			});
		this.add(removeMenuItem);
		this.addSeparator();
		this.add(propertiesMenuItem);
	}

	private void showProperties()
	{
		super.showProperties(site);
		getLogicalNetLayer().sendMapEvent(new MapEvent(site, MapEvent.MAP_ELEMENT_CHANGED));
	}

	private void removeSite()
	{
		super.removeMapElement(site);
//		DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(site);
//		command.setLogicalNetLayer(logicalNetLayer);
//		getLogicalNetLayer().getCommandList().add(command);
//		getLogicalNetLayer().getCommandList().execute();

		getLogicalNetLayer().repaint(false);
	}
}
