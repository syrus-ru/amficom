package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceSelectionDialog;
import com.syrus.AMFICOM.Client.Map.Command.Action.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;

import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public final class LinkPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem propertiesMenuItem = new JMenuItem();
	private JMenuItem addMarkMenuItem = new JMenuItem();
	private JMenuItem newCollectorMenuItem = new JMenuItem();
	private JMenuItem addToCollectorMenuItem = new JMenuItem();

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
		addToCollectorMenuItem.setText(LangModelMap.getString("AddToCollector"));
		addToCollectorMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addToCollector();
				}
			});
		this.add(removeMenuItem);
		this.add(propertiesMenuItem);
		this.add(addMarkMenuItem);
		this.add(newCollectorMenuItem);
		this.add(addToCollectorMenuItem);
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

	private void newCollector()
	{
		String inputValue = JOptionPane.showInputDialog(
				Environment.getActiveWindow(), 
				"Введите имя коллектора", 
				"Коллектор1");
		if(inputValue != null)
		{
			MapPipePathElement collector = new MapPipePathElement(
					logicalNetLayer.getContext().getDataSourceInterface().GetUId(
							MapPipePathElement.typ),
					link.getMap());

			MapElementState state = link.getState();
			collector.addLink(link);
			MapLinkProtoElement proto = (MapLinkProtoElement )Pool.get(MapLinkProtoElement.typ, MapLinkProtoElement.COLLECTIOR);
			link.setMapProtoId(proto.getId());

			MapElementStateChangeCommand command = new MapElementStateChangeCommand(link, state, link.getState());
			command.setLogicalNetLayer(logicalNetLayer);
			getLogicalNetLayer().getCommandList().add(command);
			getLogicalNetLayer().getCommandList().execute();
			
			getLogicalNetLayer().repaint();
		}
	}

	private void addToCollector()
	{
		MapPipePathElement collector;
		
		List list = logicalNetLayer.getMapView().getMap().getCollectors();
		
		ObjectResourceSelectionDialog dialog = new ObjectResourceSelectionDialog(list);
			
		dialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);

		dialog.show();

		if(dialog.getReturnCode() == ObjectResourceSelectionDialog.RET_OK)
		{
			collector = (MapPipePathElement )dialog.getSelected();
			if(collector != null)
			{
				MapElementState state = link.getState();
				collector.addLink(link);
				link.setMapProtoId(MapLinkProtoElement.COLLECTIOR);

				MapElementStateChangeCommand command = new MapElementStateChangeCommand(link, state, link.getState());
				command.setLogicalNetLayer(logicalNetLayer);
				getLogicalNetLayer().getCommandList().add(command);
				getLogicalNetLayer().getCommandList().execute();
				
				getLogicalNetLayer().repaint();
			}
		}
	}
}
