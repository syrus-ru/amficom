package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateMarkCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateMarkerCommandAtomic;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;

import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenuItem;

public final class MeasurementPathPopupMenu extends MapPopupMenu 
{
	private JMenuItem propertiesMenuItem = new JMenuItem();
	private JMenuItem addMarkerMenuItem = new JMenuItem();

	private MapMeasurementPathElement path;
	
	private static MeasurementPathPopupMenu instance = new MeasurementPathPopupMenu();

	private MeasurementPathPopupMenu()
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
	
	public static MeasurementPathPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.path = (MapMeasurementPathElement )me;

		addMarkerMenuItem.setVisible(
			getLogicalNetLayer().getContext().getApplicationModel().isEnabled(
					MapApplicationModel.ACTION_USE_MARKER));
	}

	private void jbInit()
	{
		propertiesMenuItem.setText(LangModelMap.getString("Properties"));
		propertiesMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showProperties();
				}
			});
		addMarkerMenuItem.setText(LangModelMap.getString("AddMarker"));
		addMarkerMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addMarker();
				}
			});
		this.add(propertiesMenuItem);
		this.add(addMarkerMenuItem);
	}

	private void showProperties()
	{
		super.showProperties(path);
	}

	private void addMarker()
	{
		CreateMarkerCommandAtomic command = new CreateMarkerCommandAtomic(path, point);
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();

		getLogicalNetLayer().repaint();
	}
}
