package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Action.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenuItem;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateMarkCommandAtomic;

public final class LinkPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem propertiesMenuItem = new JMenuItem();
	private JMenuItem addMarkMenuItem = new JMenuItem();
	private JMenuItem addToCollectorMenuItem = new JMenuItem();
	private JMenuItem removeFromCollectorMenuItem = new JMenuItem();
	private JMenuItem newCollectorMenuItem = new JMenuItem();
	private JMenuItem removeCollectorMenuItem = new JMenuItem();

	private MapPhysicalLinkElement link;
	
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

		MapPipePathElement collector = logicalNetLayer.getMapView().getMap().getCollector(link);
		addToCollectorMenuItem.setVisible(collector == null);
		newCollectorMenuItem.setVisible(collector == null);
		removeCollectorMenuItem.setVisible(collector != null);
		removeFromCollectorMenuItem.setVisible(collector != null);
		if(collector != null)
		{
			removeCollectorMenuItem.setText(
					LangModelMap.getString("RemoveCollector")
					+ " (" 
					+ collector.getName()
					+ ")");
		removeFromCollectorMenuItem.setText(
					LangModelMap.getString("RemoveFromCollector")
					+ " (" 
					+ collector.getName()
					+ ")");
		}
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
		newCollectorMenuItem.setText(LangModelMap.getString("CreateCollector"));
		newCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					newCollector();
				}
			});
		removeCollectorMenuItem.setText(LangModelMap.getString("RemoveCollector"));
		removeCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeCollector();
				}
			});

		addToCollectorMenuItem.setText(LangModelMap.getString("AddToCollector"));
		addToCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addToCollector();
				}
			});
		removeFromCollectorMenuItem.setText(LangModelMap.getString("RemoveFromCollector"));
		removeFromCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeFromCollector();
				}
			});

		this.add(removeMenuItem);
		this.add(propertiesMenuItem);
		this.add(addMarkMenuItem);
		this.addSeparator();
		this.add(addToCollectorMenuItem);
		this.add(removeFromCollectorMenuItem);
		this.addSeparator();
		this.add(newCollectorMenuItem);
		this.add(removeCollectorMenuItem);
	}

	private void showProperties()
	{
		super.showProperties(link);
	}

	private void removeLink()
	{
		super.removeMapElement(link);

		getLogicalNetLayer().repaint();
	}

	private void addMark()
	{
		if ( getLogicalNetLayer().getContext().getApplicationModel().isEnabled("mapActionMarkCreate"))
		{

			CreateMarkCommandAtomic command = new CreateMarkCommandAtomic(link, point);
			command.setLogicalNetLayer(logicalNetLayer);
			getLogicalNetLayer().getCommandList().add(command);
			getLogicalNetLayer().getCommandList().execute();
	
			getLogicalNetLayer().repaint();
		}
	}

	private void newCollector()
	{
		MapPipePathElement collector = super.createCollector();
		if(collector != null)
		{
			super.addLinkToCollector(collector, link);

			getLogicalNetLayer().repaint();

			getLogicalNetLayer().sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
	}

	private void addToCollector()
	{
		MapPipePathElement collector = super.selectCollector();
		if(collector != null)
		{
			super.addLinkToCollector(collector, link);
			
			getLogicalNetLayer().repaint();

			getLogicalNetLayer().sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
	}

	private void removeFromCollector()
	{
		MapPipePathElement collector = logicalNetLayer.getMapView().getMap().getCollector(link);
		if(collector != null)
		{
			super.removeLinkFromCollector(collector, link);
			
			getLogicalNetLayer().repaint();

			getLogicalNetLayer().sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
	}

	private void removeCollector()
	{
		getLogicalNetLayer().deselectAll();

		MapPipePathElement collector = logicalNetLayer.getMapView().getMap().getCollector(link);
		if(collector != null)
		{
			List list = new LinkedList();
			list.addAll(collector.getLinks());
			super.removeLinksFromCollector(collector, list);
			super.removeCollector(collector);

			getLogicalNetLayer().repaint();

			getLogicalNetLayer().sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
	}

}
