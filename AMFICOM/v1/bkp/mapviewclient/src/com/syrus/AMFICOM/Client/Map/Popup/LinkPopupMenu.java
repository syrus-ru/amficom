package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateMarkCommand;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public final class LinkPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem propertiesMenuItem = new JMenuItem();
	
	private MapPhysicalLinkElement link;
	private JMenuItem addMarkMenuItem = new JMenuItem();
	
	private static LinkPopupMenu instance = new LinkPopupMenu();

	private LinkPopupMenu()
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
	
	public static LinkPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.link = (MapPhysicalLinkElement )me;
	}

	private void jbInit()
	{
		removeMenuItem.setText(LangModelMap.getString("Delete"));
		removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeLink();
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
		addMarkMenuItem.setText(LangModelMap.getString("AddMark"));
		addMarkMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addMark();
				}
			});
		this.add(removeMenuItem);
		this.add(propertiesMenuItem);
		this.add(addMarkMenuItem);
	}

	private void showProperties()
	{
		super.showProperties(link);
	}

	private void removeLink()
	{
		getLogicalNetLayer().deselectAll();
		link.setSelected(true);
		getLogicalNetLayer().delete();

		getLogicalNetLayer().repaint();
	}

	private void addMark()
	{
		if ( getLogicalNetLayer().getContext().getApplicationModel().isEnabled("mapActionMarkCreate"))
		{

			CreateMarkCommand command = new CreateMarkCommand(link, point);
			command.setLogicalNetLayer(logicalNetLayer);
			getLogicalNetLayer().getCommandList().add(command);
			getLogicalNetLayer().getCommandList().execute();
	
			getLogicalNetLayer().repaint();
		}
	}
}
